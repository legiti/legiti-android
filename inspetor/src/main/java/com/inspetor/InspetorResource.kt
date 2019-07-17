//
//  InspetorResource.kt
//  inspetor-android-sdk
//
//  Created by Matheus Sato on 12/4/19.
//  Copyright © 2019 Inspetor. All rights reserved.
//
package com.inspetor

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Base64
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import com.snowplowanalytics.snowplow.tracker.DevicePlatforms
import com.snowplowanalytics.snowplow.tracker.Emitter
import com.snowplowanalytics.snowplow.tracker.Subject
import com.snowplowanalytics.snowplow.tracker.Tracker
import com.snowplowanalytics.snowplow.tracker.Tracker.init
import com.snowplowanalytics.snowplow.tracker.emitter.BufferOption
import com.snowplowanalytics.snowplow.tracker.emitter.HttpMethod
import com.snowplowanalytics.snowplow.tracker.emitter.RequestSecurity
import com.snowplowanalytics.snowplow.tracker.events.SelfDescribing
import com.snowplowanalytics.snowplow.tracker.payload.SelfDescribingJson
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

internal class InspetorResource(_config: InspetorConfig): InspetorResourceService {
    private var trackerName: String
    private var appId: String
    private var base64Encoded: Boolean
    private var collectorUri: String
    private var clientName: String
    private var httpMethod: HttpMethod
    private var protocolType: RequestSecurity
    private var bufferOption: BufferOption
    private var tracker: Tracker?

    init {
        this.trackerName = _config.trackerName
        this.appId = _config.appId
        this.clientName = _config.trackerName.split(InspetorDependencies.DEFAULT_INSPETOR_TRACKER_NAME_SEPARATOR)[0]
        this.collectorUri = InspetorDependencies.DEFAULT_COLLECTOR_URI
        this.base64Encoded = InspetorDependencies.DEFAULT_BASE64_OPTION
        this.bufferOption = switchBufferOptionSize(InspetorDependencies.DEFAULT_BUFFERSIZE_OPTION)
        this.httpMethod = switchHttpMethod(InspetorDependencies.DEFAULT_HTTP_METHOD_TYPE)
        this.protocolType = switchSecurityProtocol(InspetorDependencies.DEFAULT_PROTOCOL_TYPE)

        if (_config.devEnv) {
            this.collectorUri = InspetorDependencies.DEFAULT_COLLECTOR_DEV_URI
        }

        this.tracker = null

        require(verifySetup())
    }

    override fun setContext(context: Context) {
        tracker = setupTracker(context) ?: throw fail("Inspetor Exception 9000: Internal error.")
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

    override fun trackAccountAuthAction(account_id: String, action: AuthAction): Boolean {
        val datamap: HashMap<String, String>? = hashMapOf(
            "auth_account_id" to encodeData(account_id),
            "auth_timestamp" to encodeData(getNormalizedTimestamp())
        )
        if (datamap != null) {
            trackUnstructuredEvent(InspetorDependencies.FRONTEND_AUTH_SCHEMA_VERSION, datamap, action.rawValue())
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

    override fun trackPasswordRecoveryAction(account_email: String, action: PassRecoveryAction): Boolean {
        val datamap: HashMap<String, String>? = hashMapOf(
            "pass_recovey_email" to encodeData(account_email),
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

    private fun switchBufferOptionSize (bufferOptionSize: BufferOptionSize): BufferOption {
        return when(bufferOptionSize) {
            BufferOptionSize.SINGLE -> BufferOption.Single
            BufferOptionSize.DEFAULT -> BufferOption.DefaultGroup
            BufferOptionSize.HEAVY -> BufferOption.HeavyGroup
        }
    }

    private fun switchSecurityProtocol (requestSecurityProtocol: RequestSecurityProtocol): RequestSecurity {
        return when (requestSecurityProtocol) {
            RequestSecurityProtocol.HTTP -> RequestSecurity.HTTP
            RequestSecurityProtocol.HTTPS -> RequestSecurity.HTTPS
        }
    }

    private fun switchHttpMethod (httpMethodType: HttpMethodType): HttpMethod {
        return when (httpMethodType) {
            HttpMethodType.GET -> HttpMethod.GET
            HttpMethodType.POST -> HttpMethod.POST
        }
    }

    private fun verifySetup(): Boolean {
        return (appId != "" && trackerName != "")
    }

    private fun setupTracker(context: Context): Tracker? {
        val geoContext = verifyPermission(context)
        val emitter = Emitter.EmitterBuilder(collectorUri, context)
            .method(httpMethod)
            .option(bufferOption)
            .security(protocolType)
            .build() ?: throw fail("Inspetor Exception 9000: Internal error.")

        return init(Tracker.TrackerBuilder(emitter, trackerName, appId, context)
                .base64(base64Encoded)
                .platform(DevicePlatforms.Mobile)
                .subject(Subject.SubjectBuilder().build())
                .sessionContext(true)
                .sessionCheckInterval(10)
                .foregroundTimeout(300)
                .backgroundTimeout(120)
                .geoLocationContext(geoContext)
                .mobileContext(true)
                .build()) ?: throw fail("Inspetor Exception 9000: Internal error.")
    }

    private fun trackUnstructuredEvent(schema: String, data: HashMap<String, String>, action: String) {
        val inspetorData = SelfDescribingJson(schema, data)
        val contextMap: HashMap<String, String>? = hashMapOf(
            "action" to action
        )
        val inspetorContext = SelfDescribingJson(
            InspetorDependencies.FRONTEND_CONTEXT_SCHEMA_VERSION, contextMap)
        val contexts: ArrayList<SelfDescribingJson>? = arrayListOf(inspetorContext)

        tracker?.track(
            SelfDescribing.builder()
                .eventData(inspetorData)
                .customContext(contexts)
                .build() ?: throw fail("Inspetor Exception 9000: Internal error.")
        )
        tracker?.emitter?.flush() ?: throw fail("Inspetor Exception 9000: Internal error.")
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

    private fun verifyPermission(context: Context): Boolean {
        if (checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false
        }

        return true
    }
}
