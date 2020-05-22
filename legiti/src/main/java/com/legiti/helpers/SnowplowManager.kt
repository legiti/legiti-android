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

    private const val DEFAULT_TRACKER_NAME = "legiti.android.tracker"

    private lateinit var trackerName: String
    private lateinit var appId: String
    private lateinit var collectorUri: String
    private lateinit var postPath: String
    private lateinit var httpMethod: HttpMethod
    private lateinit var protocolType: RequestSecurity
    private lateinit var bufferOption: BufferOption
    private var base64Encoded: Boolean = false

    fun init(config: LegitiConfig) {
        trackerName = this.DEFAULT_TRACKER_NAME
        appId = config.authToken
        collectorUri = LegitiDependencies.DEFAULT_COLLECTOR_URL
        postPath = if (config.legitiDevEnv) LegitiDependencies.DEFAULT_COLLECTOR_STAGING_PATH else LegitiDependencies.DEFAULT_COLLECTOR_PROD_PATH
        base64Encoded = LegitiDependencies.DEFAULT_BASE64_OPTION
        bufferOption = switchBufferOptionSize(LegitiDependencies.DEFAULT_BUFFER_SIZE_OPTION)
        httpMethod = switchHttpMethod(LegitiDependencies.DEFAULT_HTTP_METHOD_TYPE)
        protocolType = switchSecurityProtocol(LegitiDependencies.DEFAULT_PROTOCOL_TYPE)
    }

    fun setupTracker(androidContext: Context): Tracker? {
        val emitter = Emitter.EmitterBuilder(collectorUri, androidContext.applicationContext)
            .method(httpMethod)
            .option(bufferOption)
            .customPostPath(postPath)
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
                .foregroundTimeout(300) // Timeout after 5 minutes (default is 10)
                .backgroundTimeout(120)
                .geoLocationContext(true)
                .applicationContext(true)
                .mobileContext(true)
                .build()
        ) ?: throw fail()
    }

    private fun fail(message: String = "Legiti Exception 9000: Internal error."): Throwable {
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

}