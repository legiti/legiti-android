//
//  InspetorResource.kt
//  inspetor-android-sdk
//
//  Created by Matheus Sato on 12/4/19.
//  Copyright © 2019 Inspetor. All rights reserved.
//
package com.inspetor

import android.content.Context
import android.os.Build
import android.provider.Settings.Secure
import android.util.Base64
import com.snowplowanalytics.snowplow.tracker.Tracker
import com.snowplowanalytics.snowplow.tracker.events.SelfDescribing
import com.snowplowanalytics.snowplow.tracker.payload.SelfDescribingJson
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import com.scottyab.rootbeer.RootBeer
import java.net.NetworkInterface.getNetworkInterfaces
import android.net.NetworkCapabilities
import android.net.ConnectivityManager
import com.snowplowanalytics.snowplow.tracker.events.ScreenView
import java.security.MessageDigest

internal class InspetorResource(_config: InspetorConfig): InspetorResourceService {

    private var tracker: Tracker?
    private var context: Context?
    private var mobileContext: SelfDescribingJson?
    private var deviceId: String?

    init {
        SnowManager.init(_config)
        this.tracker = null
        this.context = null
        this.mobileContext = null
        this.deviceId = null
    }

    override fun setContext(context: Context) {
        this.context = context
        this.mobileContext = setupInspetorContext(context)
        tracker = SnowManager.setupTracker(context.applicationContext) ?: throw fail("Inspetor Exception 9000: Internal error.")
    }

    override fun trackAccountAction(account_id: String, action: AccountAction): Boolean {
        val datamap: HashMap<String, String>? = hashMapOf(
            "account_id" to encodeData(account_id),
            "account_timestamp" to encodeData(getNormalizedTimestamp())
        )
        if (datamap != null) {
            trackUnstructuredEvent(InspetorDependencies.FRONTEND_ACCOUNT_SCHEMA_VERSION, datamap, action.rawValue())
        }

        return true
    }

    override fun trackAccountAuthAction(account_email: String, account_id: String, action: AuthAction): Boolean {
        val datamap: HashMap<String, String>? = hashMapOf(
            "auth_account_email" to encodeData(account_email),
            "auth_timestamp" to encodeData(getNormalizedTimestamp()),
            "auth_account_id" to encodeData(account_id)
        )
        if (datamap != null) {
            trackUnstructuredEvent(InspetorDependencies.FRONTEND_AUTH_SCHEMA_VERSION, datamap, action.rawValue())
        }

        if (action.rawValue() == AuthAction.ACCOUNT_LOGIN_ACTION.rawValue()) {
            tracker?.subject?.setUserId(account_email)
        }

        return true
    }

    override fun trackEventAction(event_id: String, action: EventAction): Boolean {
        val datamap: HashMap<String, String>? = hashMapOf(
            "event_id" to encodeData(event_id),
            "event_timestamp" to encodeData(getNormalizedTimestamp())
        )
        if (datamap != null) {
            trackUnstructuredEvent(InspetorDependencies.FRONTEND_EVENT_SCHEMA_VERSION, datamap, action.rawValue())
        }

        return true
    }

    override fun trackPasswordRecoveryAction(accountEmail: String, action: PassRecoveryAction): Boolean {
        val datamap: HashMap<String, String>? = hashMapOf(
            "pass_recovery_email" to encodeData(accountEmail),
            "pass_recovery_timestamp" to encodeData(getNormalizedTimestamp())
        )
        if (datamap != null) {
            trackUnstructuredEvent(InspetorDependencies.FRONTEND_PASS_RECOVERY_SCHEMA_VERSION, datamap, action.rawValue())
        }

        return true
    }

    override fun trackItemTransferAction(transfer_id: String, action: TransferAction): Boolean {
        val datamap: HashMap<String, String>? = hashMapOf(
            "transfer_id" to encodeData(transfer_id),
            "transfer_timestamp" to encodeData(getNormalizedTimestamp())
        )
        if (datamap != null) {
            trackUnstructuredEvent(InspetorDependencies.FRONTEND_TRANSFER_SCHEMA_VERSION, datamap, action.rawValue())
        }

        return true
    }

    override fun trackSaleAction(sale_id: String, action: SaleAction): Boolean {
        val datamap: HashMap<String, String>? = hashMapOf(
            "sale_id" to encodeData(sale_id),
            "sale_timestamp" to encodeData(getNormalizedTimestamp())
        )
        if (datamap != null) {
            trackUnstructuredEvent(InspetorDependencies.FRONTEND_SALE_SCHEMA_VERSION, datamap, action.rawValue())
        }

        return true
    }

    override fun trackPageView(page_title: String): Boolean {
        val contexts: ArrayList<SelfDescribingJson>? = arrayListOf()
        this.mobileContext?.let{ contexts?.add(it) }
        this.tracker?.track(
            ScreenView.builder()
                .name(page_title)
                .id(UUID.randomUUID().toString())
                .customContext(contexts)
                .build())

        return true
    }

    private fun trackUnstructuredEvent(schema: String, data: HashMap<String, String>, action: String) {
        val inspetorData = SelfDescribingJson(schema, data)
        val contextMap: HashMap<String, String>? = hashMapOf(
            "action" to action
        )
        val inspetorContext = SelfDescribingJson(
            InspetorDependencies.FRONTEND_CONTEXT_SCHEMA_VERSION, contextMap
        )
        val contexts: ArrayList<SelfDescribingJson>? = arrayListOf(inspetorContext)

        if(tracker == null) {
            tracker = this.context?.let { SnowManager.setupTracker(it) }
        }

        this.mobileContext?.let{ contexts?.add(it) }

        tracker?.track(
            SelfDescribing.builder()
                .eventData(inspetorData)
                .customContext(contexts)
                .build() ?: throw fail("Inspetor Exception 9000: Internal error.")
        )

        tracker?.emitter?.flush()
    }

    private fun getNormalizedTimestamp(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            val dateTime = LocalDateTime.now(ZoneId.of("UTC"))
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'+00:00'")
            dateTime.format(formatter)
        } else {
            val dateFormat = SimpleDateFormat("yyyy/MM/dd'T'HH:mm:ss'+00:00'", Locale.US)
            val date = Date()
            dateFormat.format(date)
        }
    }

    private fun encodeData(data: String): String {
        return Base64.encodeToString(data.toByteArray(), Base64.NO_WRAP)
    }

    private fun fail(message: String): Throwable {
        throw Exception(message)
    }

    private fun setupInspetorContext(context: Context): SelfDescribingJson {
        val contextMap: HashMap<String, Any?>
        val isSimulator: Boolean = checkBasic()
        val isRooted = RootBeer(context)
        val isVPN = checkVPN(context)

        if (this.deviceId == null) {
            this.deviceId = Secure.getString(context.contentResolver, Secure.ANDROID_ID)
        }

        contextMap = hashMapOf(
            "device_fingerprint" to hashDeviceId(this.deviceId),
            "is_rooted" to isRooted.isRootedWithoutBusyBoxCheck,
            "is_simulator" to isSimulator,
            "is_vpn" to isVPN
        )

        return SelfDescribingJson(
            InspetorDependencies.FRONTEND_FINGERPRINT_SCHEMA_VERSION, contextMap
        )
    }

    private fun checkBasic(): Boolean {
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

    private fun checkVPN(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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
            for (networkInterface in Collections.list(getNetworkInterfaces())) {
                if (networkInterface.isUp)
                    networkList.add(networkInterface.name)
            }
        } catch (ex: Exception) {

        }

        return networkList.contains("tun0") || networkList.contains("ppp0")
    }

    private fun hashDeviceId(data: String?): String? {
        if (data == null) {
            return null
        }

        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(data.toByteArray())
        return digest.fold("", { str, it -> str + "%02x".format(it) })
    }
}
