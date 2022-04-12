package com.legiti.helpers

import android.content.Context
import android.media.MediaDrm
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.Settings
import com.scottyab.rootbeer.RootBeer
import java.net.NetworkInterface
import java.security.MessageDigest
import java.util.*
import kotlin.collections.HashMap

internal class LegitiDeviceData(private val androidContext: Context) {

    private var deviceData: HashMap<String, Any?>? = null

    internal fun getDeviceData(): HashMap<String, Any?> {

        if (this.deviceData != null) {
            return this.deviceData!!
        }

        val data: HashMap<String, Any?> = hashMapOf(
            "customer_device_fingerprint" to this.getCustomerDeviceFingerprint(),
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
            return caps?.hasTransport(NetworkCapabilities.TRANSPORT_VPN) ?: return false
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


    private fun getCustomerDeviceFingerprint(): String? {
        return Settings.Secure.getString(this.androidContext.contentResolver, Settings.Secure.ANDROID_ID) ?: return null
    }

}

