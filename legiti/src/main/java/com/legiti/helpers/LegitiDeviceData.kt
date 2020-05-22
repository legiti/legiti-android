package com.legiti.helpers

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaDrm
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.Settings
import android.util.Log
import com.scottyab.rootbeer.RootBeer
import java.net.NetworkInterface
import java.security.MessageDigest
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.log

internal class LegitiDeviceData(private val androidContext: Context) {

    private val WIDEVINE_UUID = UUID(-0x121074568629b532L, -0x5c37d8232ae2de13L)
    private val PLAYREADY_UUID = UUID(-0x65fb0f8667bfbd7aL, -0x546d19a41f77a06bL)
    private val CLEARKEY_UUID = UUID(-0x1d8e62a7567a4c37L, 0x781AB030AF78D30EL)
    private val COMMON_PSSH_UUID = UUID(0x1077EFECC0B24D02L, -0x531cc3e1ad1d04b5L)

    val COMMON_DRM_PROVIDERS: Array<UUID> = arrayOf(
        this.WIDEVINE_UUID,
        this.PLAYREADY_UUID,
        this.CLEARKEY_UUID,
        this.COMMON_PSSH_UUID
    )

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

    private fun ByteArray.toHexString() = joinToString("") { "%02x".format(it) }

    // Based on https://beltran.work/blog/2018-03-27-device-unique-id-android/
    // and https://stackoverflow.com/a/59050182
    private fun getDeviceFingerprint(): String? {
        var meDrm: MediaDrm? = null
        var deviceFingerprint: String? = null

        for (provider in this.COMMON_DRM_PROVIDERS) {
            try {
                meDrm = MediaDrm(provider)
                var rawFingerprint = meDrm.getPropertyByteArray(MediaDrm.PROPERTY_DEVICE_UNIQUE_ID)
                var md = MessageDigest.getInstance("SHA-256")
                md.update(rawFingerprint)
                deviceFingerprint =  md.digest().toHexString()
                break
            } catch (e: Exception) {
                // Provider not available
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // This only works on Android P (28)
            meDrm?.close()
        } else {
            meDrm?.release()
        }

        return deviceFingerprint
    }
}

