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
import android.util.Base64
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
        this.base64Encoded = _config.base64Encoded
        this.collectorUri = _config.collectorUri
        this.bufferOption = switchBufferOptionSize(_config.bufferOptionSize)
        this.httpMethod = switchHttpMethod(_config.httpMethodType)
        this.protocolType = switchSecurityProtocol(_config.requestSecurityProtocol)
        this.tracker = null

        require(verifySetup())
    }

    override fun setContext(context: Context) {
        tracker = setupTracker(context)
    }

    override fun trackAccountAction(account_id: String, action: AccountAction): Boolean {
        val datamap: HashMap<String, String>? = hashMapOf()
        datamap?.set("account_id", encodeData(account_id))
        datamap?.set("account_timestamp", getNormalizedTimestamp())
        if (datamap != null) {
            trackUnstructuredEvent(InspetorDependencies.FRONTEND_ACCOUNT_SCHEMA_VERSION, datamap, action.rawValue())
        }

        return true
    }

    override fun trackAccountAuthAction(account_id: String, action: AuthAction): Boolean {
        val datamap: HashMap<String, String>? = hashMapOf()
        datamap?.set("auth_account_id", encodeData(account_id))
        datamap?.set("auth_account_timestamp", encodeData(getNormalizedTimestamp()))
        if (datamap != null) {
            trackUnstructuredEvent(InspetorDependencies.FRONTEND_AUTH_SCHEMA_VERSION, datamap, action.rawValue())
        }

        return true
    }

    override fun trackEventAction(event_id: String, action: EventAction): Boolean {
        val datamap: HashMap<String, String>? = hashMapOf()
        datamap?.set("event_id", encodeData(event_id))
        datamap?.set("event_timestamp", encodeData(getNormalizedTimestamp()))
        if (datamap != null) {
            trackUnstructuredEvent(InspetorDependencies.FRONTEND_EVENT_SCHEMA_VERSION, datamap, action.rawValue())
        }

        return true
    }

    override fun trackPasswordRecoveryAction(account_email: String, action: PassRecoveryAction): Boolean {
        val datamap: HashMap<String, String>? = hashMapOf()
        datamap?.set("pass_recovey_email", encodeData(account_email))
        datamap?.set("pass_recovey_timestamp", encodeData(getNormalizedTimestamp()))
        if (datamap != null) {
            trackUnstructuredEvent(InspetorDependencies.FRONTEND_PASS_RECOVERY_SCHEMA_VERSION, datamap, action.rawValue())
        }

        return true
    }

    override fun trackItemTransferAction(transfer_id: String, action: TransferAction): Boolean {
        val datamap: HashMap<String, String>? = hashMapOf()
        datamap?.set("transfer_id", encodeData(transfer_id))
        datamap?.set("transfer_timestamp", encodeData(getNormalizedTimestamp()))
        if (datamap != null) {
            trackUnstructuredEvent(InspetorDependencies.FRONTEND_TRANSFER_SCHEMA_VERSION, datamap, action.rawValue())
        }

        return true
    }

    override fun trackSaleAction(sale_id: String, action: SaleAction): Boolean {
        val datamap: HashMap<String, String>? = hashMapOf()
        datamap?.set("sale_id", encodeData(sale_id))
        datamap?.set("sale_timestamp", encodeData(getNormalizedTimestamp()))
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
        val emitter = Emitter.EmitterBuilder(collectorUri, context)
            .method(httpMethod)
            .option(bufferOption)
            .security(protocolType)
            .build()

        return init(Tracker.TrackerBuilder(emitter, trackerName, appId, context)
                .base64(base64Encoded)
                .platform(DevicePlatforms.Mobile)
                .subject(Subject.SubjectBuilder().build())
                .sessionContext(true)
                .sessionCheckInterval(10)
                .foregroundTimeout(300)
                .backgroundTimeout(120)
                .geoLocationContext(true)
                .mobileContext(true)
                .build())
    }

    private fun trackUnstructuredEvent(schema: String, data: HashMap<String, String>, action: String) {
        val inspetorData = SelfDescribingJson(schema, data)
        val inspetorContext = SelfDescribingJson(
            InspetorDependencies.FRONTEND_CONTEXT_SCHEMA_VERSION, action)
        val contexts: ArrayList<SelfDescribingJson>? = null
        contexts?.add(inspetorContext)

        tracker?.track(
            SelfDescribing.builder()
                .eventData(inspetorData)
//                .customContext(contexts)
                .build()
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
        return Base64.encodeToString(data.toByteArray(), Base64.DEFAULT)
    }
}
