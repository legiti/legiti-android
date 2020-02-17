package com.legiti.helpers

import android.content.Context
import com.legiti.LegitiConfig
import com.snowplowanalytics.snowplow.tracker.DevicePlatforms
import com.snowplowanalytics.snowplow.tracker.Emitter
import com.snowplowanalytics.snowplow.tracker.Subject
import com.snowplowanalytics.snowplow.tracker.Tracker
import com.snowplowanalytics.snowplow.tracker.emitter.BufferOption
import com.snowplowanalytics.snowplow.tracker.emitter.HttpMethod
import com.snowplowanalytics.snowplow.tracker.emitter.RequestSecurity

object SnowplowManager {

    private const val DEFAULT_TRACKER_NAME = "com.legiti.android.tracker"

    private lateinit var trackerName: String
    private lateinit var appId: String
    private lateinit var collectorUri: String
    private lateinit var httpMethod: HttpMethod
    private lateinit var protocolType: RequestSecurity
    private lateinit var bufferOption: BufferOption
    private var base64Encoded: Boolean = false

    fun init(config: LegitiConfig) {
        trackerName = this.DEFAULT_TRACKER_NAME
        appId = config.authToken
        collectorUri = this.createCollectorUrl(config)
        base64Encoded = InspetorDependencies.DEFAULT_BASE64_OPTION
        bufferOption = switchBufferOptionSize(InspetorDependencies.DEFAULT_BUFFER_SIZE_OPTION)
        httpMethod = switchHttpMethod(InspetorDependencies.DEFAULT_HTTP_METHOD_TYPE)
        protocolType = switchSecurityProtocol(InspetorDependencies.DEFAULT_PROTOCOL_TYPE)
    }

    fun setupTracker(androidContext: Context): Tracker? {
        val emitter = Emitter.EmitterBuilder(collectorUri, androidContext.applicationContext)
            .method(httpMethod)
            .option(bufferOption)
            .security(protocolType)
            .build() ?: throw fail()

        return Tracker.init(
            Tracker.TrackerBuilder(emitter,
                trackerName,
                appId, androidContext.applicationContext)
                .base64(base64Encoded)
                .platform(DevicePlatforms.Mobile)
                .subject(Subject.SubjectBuilder().context(androidContext).build())
                .sessionContext(true)
                .sessionCheckInterval(10) // Checks every 10 seconds (default is 15)
                .foregroundTimeout(300)   // Timeout after 5 minutes (default is 10)
                .backgroundTimeout(120)
                .geoLocationContext(false) // Since we are not being able to get the location anyway
                .applicationContext(true)
                .mobileContext(true)
                .build()
        ) ?: throw fail()
    }

    private fun fail(message: String = "Inspetor Exception 9000: Internal error."): Throwable {
        throw Exception(message)
    }

    private fun switchBufferOptionSize(bufferOptionSize: BufferOptionSize): BufferOption {
        return when(bufferOptionSize) {
            BufferOptionSize.SINGLE -> BufferOption.Single
            BufferOptionSize.DEFAULT -> BufferOption.DefaultGroup
            BufferOptionSize.HEAVY -> BufferOption.HeavyGroup
        }
    }

    private fun switchSecurityProtocol(requestSecurityProtocol: RequestSecurityProtocol): RequestSecurity {
        return when (requestSecurityProtocol) {
            RequestSecurityProtocol.HTTP -> RequestSecurity.HTTP
            RequestSecurityProtocol.HTTPS -> RequestSecurity.HTTPS
        }
    }

    private fun switchHttpMethod(httpMethodType: HttpMethodType): HttpMethod {
        return when (httpMethodType) {
            HttpMethodType.GET -> HttpMethod.GET
            HttpMethodType.POST -> HttpMethod.POST
        }
    }

    private fun createCollectorUrl(config: LegitiConfig): String {
        val path = when (config.inspetorDevEnv) {
            true -> InspetorDependencies.DEFAULT_COLLECTOR_STAGING_PATH
            false -> InspetorDependencies.DEFAULT_COLLECTOR_PROD_PATH
        }

        return InspetorDependencies.DEFAULT_COLLECTOR_URL + path
    }

}