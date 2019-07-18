package com.inspetor

import android.content.Context
import com.snowplowanalytics.snowplow.tracker.DevicePlatforms
import com.snowplowanalytics.snowplow.tracker.Emitter
import com.snowplowanalytics.snowplow.tracker.Subject
import com.snowplowanalytics.snowplow.tracker.Tracker
import com.snowplowanalytics.snowplow.tracker.emitter.BufferOption
import com.snowplowanalytics.snowplow.tracker.emitter.HttpMethod
import com.snowplowanalytics.snowplow.tracker.emitter.RequestSecurity
import com.snowplowanalytics.snowplow.tracker.events.SelfDescribing
import com.snowplowanalytics.snowplow.tracker.payload.SelfDescribingJson

object SnowManager {
    private lateinit var trackerName: String
    private lateinit var appId: String
    private lateinit var collectorUri: String
    private lateinit var httpMethod: HttpMethod
    private lateinit var protocolType: RequestSecurity
    private lateinit var bufferOption: BufferOption
    private var base64Encoded: Boolean = false

    fun init(config: InspetorConfig) {
        this.trackerName = config.trackerName
        this.appId = config.appId
        this.collectorUri = InspetorDependencies.DEFAULT_COLLECTOR_URI
        this.base64Encoded = InspetorDependencies.DEFAULT_BASE64_OPTION
        this.bufferOption = switchBufferOptionSize(InspetorDependencies.DEFAULT_BUFFERSIZE_OPTION)
        this.httpMethod = switchHttpMethod(InspetorDependencies.DEFAULT_HTTP_METHOD_TYPE)
        this.protocolType = switchSecurityProtocol(InspetorDependencies.DEFAULT_PROTOCOL_TYPE)
        if (config.devEnv) {
            this.collectorUri = InspetorDependencies.DEFAULT_COLLECTOR_DEV_URI
        }

        require(verifySetup())
    }

    private fun verifySetup(): Boolean {
        return (appId != "" && trackerName != "")
    }

    fun setupTracker(context: Context): Tracker? {
        val emitter = Emitter.EmitterBuilder(collectorUri, context)
            .method(httpMethod)
            .option(bufferOption)
            .security(protocolType)
            .build() ?: throw fail("Inspetor Exception 9000: Internal error.")

        Tracker.instance().globalContexts.clear()

        return Tracker.init(
            Tracker.TrackerBuilder(emitter, trackerName, appId, context)
                .base64(base64Encoded)
                .platform(DevicePlatforms.Mobile)
                .subject(Subject.SubjectBuilder().context(context).build())
                .sessionContext(true)
                .sessionCheckInterval(5) // Checks every 10 seconds (default is 15)
                .foregroundTimeout(180)   // Timeout after 5 minutes (default is 10)
                .backgroundTimeout(60)
                .geoLocationContext(true)
                .applicationContext(true)
                .mobileContext(true)
                .build()
        ) ?: throw fail("Inspetor Exception 9000: Internal error.")
    }

    private fun fail(message: String): Throwable {
        throw Exception(message)
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

}