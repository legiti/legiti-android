package com.inspetor.helpers

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.DocumentsContract
import android.provider.Settings
import com.scottyab.rootbeer.RootBeer
import com.snowplowanalytics.snowplow.tracker.payload.SelfDescribingJson
import java.net.NetworkInterface
import java.security.MessageDigest
import java.util.*

internal class InspetorDeviceData(private val androidContext: Context) {

    private var deviceData: HashMap<String, Any?>? = null

    internal fun getDeviceData(): HashMap<String, Any?> {

        if (this.deviceData != null) {
            return this.deviceData!!
        }

        val data: HashMap<String, Any?> = hashMapOf(
            "device_fingerprint" to this.getDeviceFingerprint(),
            "is_rooted" to this.getIsRooted(),
            "is_simulator" to this.getIsSimulator(),
            "is_vpn" to this.getIsVPNConnected()
        )
        this.deviceData = data

        return data
    }

    private fun getIsRooted(): Boolean {
        return RootBeer(this.androidContext).isRootedWithoutBusyBoxCheck
    }

    private fun getIsSimulator(): Boolean {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.toLowerCase().contains("droid4x")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.HARDWARE == "goldfish"
                || Build.HARDWARE == "vbox86"
                || Build.PRODUCT == "sdk"
                || Build.PRODUCT == "google_sdk"
                || Build.PRODUCT == "sdk_x86"
                || Build.PRODUCT == "vbox86p"
                || Build.BOARD.toLowerCase().contains("nox")
                || Build.BOOTLOADER.toLowerCase().contains("nox")
                || Build.HARDWARE.toLowerCase().contains("nox")
                || Build.PRODUCT.toLowerCase().contains("nox")
                || Build.SERIAL.toLowerCase().contains("nox")
    }

    private fun getIsVPNConnected(): Boolean {
        val connectivityManager = this.androidContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkList: ArrayList<String> = arrayListOf()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork
            val caps = connectivityManager.getNetworkCapabilities(activeNetwork)
            val vpnInUse = caps.hasTransport(NetworkCapabilities.TRANSPORT_VPN)

            if (vpnInUse) {
                return vpnInUse
            }
        }

        try {
            for (networkInterface in Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (networkInterface.isUp)
                    networkList.add(networkInterface.name)
            }
        } catch (ex: Exception) {

        }

        return networkList.contains("tun0") || networkList.contains("ppp0")
    }

    private fun getDeviceFingerprint(): String? {
        val device_fingerprint = Settings.Secure.getString(this.androidContext.contentResolver, Settings.Secure.ANDROID_ID) ?: return null

        val messageDigest = MessageDigest.getInstance("SHA-256")
        val digest = messageDigest.digest(device_fingerprint.toByteArray())
        return digest.fold("", { str, it -> str + "%02x".format(it) })


    }

}