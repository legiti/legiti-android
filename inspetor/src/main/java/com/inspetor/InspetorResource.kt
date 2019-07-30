//
//  InspetorResource.kt
//  inspetor-android-sdk
//
//  Created by Matheus Sato on 12/4/19.
//  Copyright © 2019 Inspetor. All rights reserved.
//
package com.inspetor

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Base64
import androidx.core.app.ActivityCompat
import com.snowplowanalytics.snowplow.tracker.Tracker
import com.snowplowanalytics.snowplow.tracker.events.SelfDescribing
import com.snowplowanalytics.snowplow.tracker.payload.SelfDescribingJson
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

internal class InspetorResource(_config: InspetorConfig): InspetorResourceService {
    private var tracker: Tracker?
    private var context: Context?

    init {
        SnowManager.init(_config)
        this.tracker = null
        this.context = null
    }

    override fun setContext(context: Context) {
        this.context = context
        tracker = SnowManager.setupTracker(context.applicationContext) ?: throw fail("Inspetor Exception 9000: Internal error.")
    }

    private fun fail(message: String): Throwable {
        throw Exception(message)
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

    override fun trackAccountAuthAction(account_email: String, action: AuthAction): Boolean {
        val datamap: HashMap<String, String>? = hashMapOf(
            "auth_account_email" to encodeData(account_email),
            "auth_timestamp" to encodeData(getNormalizedTimestamp())
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
            "pass_recovey_email" to encodeData(accountEmail),
            "pass_recovey_timestamp" to encodeData(getNormalizedTimestamp())
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

    private fun trackUnstructuredEvent(schema: String, data: HashMap<String, String>, action: String) {
        val inspetorData = SelfDescribingJson(schema, data)
        val contextMap: HashMap<String, String>? = hashMapOf(
            "action" to action
        )
        val inspetorContext = SelfDescribingJson(
            InspetorDependencies.FRONTEND_CONTEXT_SCHEMA_VERSION, contextMap)
        val contexts: ArrayList<SelfDescribingJson>? = arrayListOf(inspetorContext)

        if(tracker != null) {
//            Tracker.instance().globalContexts.clear()
        } else {
            tracker = this.context?.let { SnowManager.setupTracker(it) }
        }

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
}
