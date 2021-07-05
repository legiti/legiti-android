package com.legiti.helpers

import android.content.Context
import com.legiti.LegitiConfig
import com.snowplowanalytics.snowplow.Snowplow
import com.snowplowanalytics.snowplow.configuration.EmitterConfiguration
import com.snowplowanalytics.snowplow.configuration.NetworkConfiguration
import com.snowplowanalytics.snowplow.configuration.TrackerConfiguration
import com.snowplowanalytics.snowplow.controller.TrackerController
import com.snowplowanalytics.snowplow.emitter.BufferOption
import com.snowplowanalytics.snowplow.network.HttpMethod
import com.snowplowanalytics.snowplow.tracker.*

object SnowplowManager {

    private const val DEFAULT_TRACKER_NAME = "legiti.android.tracker"

    private lateinit var appId: String
    private lateinit var postPath: String

    fun init(config: LegitiConfig) {
        appId = config.authToken
        postPath = if (config.legitiDevEnv) LegitiDependencies.DEFAULT_COLLECTOR_STAGING_PATH else LegitiDependencies.DEFAULT_COLLECTOR_PROD_PATH
    }

    fun setupTracker(androidContext: Context): TrackerController {
        val trackerConfiguration = TrackerConfiguration(this.appId)
            .sessionContext(true)
            .applicationContext(true)
            .platformContext(true)
            .geoLocationContext(true)
            .base64encoding(LegitiDependencies.DEFAULT_BASE64_OPTION)
            .devicePlatform(DevicePlatform.Mobile)
            .exceptionAutotracking(false)
            .installAutotracking(false)
            .screenViewAutotracking(false)

        val networkConfiguration = NetworkConfiguration(
            LegitiDependencies.DEFAULT_COLLECTOR_URL,
            LegitiDependencies.DEFAULT_HTTP_METHOD_TYPE
        ).customPostPath(this.postPath)

        val emitterConfiguration = EmitterConfiguration()
            .bufferOption(LegitiDependencies.DEFAULT_BUFFER_SIZE_OPTION)

        return Snowplow.createTracker(
            androidContext,
            this.DEFAULT_TRACKER_NAME,
            networkConfiguration,
            trackerConfiguration,
            emitterConfiguration
        )
    }

}
