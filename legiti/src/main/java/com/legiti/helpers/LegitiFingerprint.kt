package com.legiti.helpers

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.lang.Runnable
import java.lang.Exception
import java.util.UUID
import android.os.Build
import android.media.MediaDrm
import java.security.MessageDigest
import java.lang.StringBuilder
import java.util.concurrent.TimeUnit

// Based on https://tinyurl.com/4tesf328
class LegitiFingerprint {

    internal var deviceFingerprint: String? = null
    internal var didExecute: Boolean = false

    private val executor = Executors.newFixedThreadPool(2)

    private val WIDEVINE_UUID = UUID(-0x121074568629b532L, -0x5c37d8232ae2de13L)
    private val PLAYREADY_UUID = UUID(-0x65fb0f8667bfbd7aL, -0x546d19a41f77a06bL)
    private val CLEARKEY_UUID = UUID(-0x1d8e62a7567a4c37L, 0x781AB030AF78D30EL)
    private val COMMON_PSSH_UUID = UUID(0x1077EFECC0B24D02L, -0x531cc3e1ad1d04b5L)

    private val COMMON_DRM_PROVIDERS: Array<UUID> = arrayOf(
        this.WIDEVINE_UUID,
        this.PLAYREADY_UUID,
        this.CLEARKEY_UUID,
        this.COMMON_PSSH_UUID
    )

    private fun ByteArray.toHexString() = joinToString("") { "%02x".format(it) }

    internal fun execute() {

        // since getDeviceId causes ANR (i.e. the function hangs forever),
        // we need it in a separate task
        // here we use Future, because it has built-in cancel mechanism
        val future = executor.submit {
            val deviceId = getDeviceFingerprint()
            this.deviceFingerprint = deviceId

            // the function may come back after a long time,
            // (since thread can't guaranteed to be cancelled),
            // we need to check if an interrupt signal has been raised
            if (!Thread.currentThread().isInterrupted) {
                this.deviceFingerprint = deviceId
                this.didExecute = true
            }
        }

        // since Future.get is a blocking call,
        // we need to run it on a different thread.
        executor.execute {
            try {
                future[2, TimeUnit.SECONDS]
            } catch (e: Exception) {
                // if any exception happens (timeout, interrupt, etc),
                // cancel the task (raise an interrupt signal)
                future.cancel(true)
                this.didExecute = true
                this.deviceFingerprint = null
            }

            this.didExecute = false
            executor.shutdownNow()
        }
    }

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