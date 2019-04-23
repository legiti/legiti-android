//
//  InspetorDependencies.kt
//  inspetor-android-sdk
//
//  Created by Matheus Sato on 12/4/19.
//  Copyright © 2019 Inspetor. All rights reserved.
//
package com.inspetor

import com.snowplowanalytics.snowplow.tracker.emitter.BufferOption
import com.snowplowanalytics.snowplow.tracker.emitter.HttpMethod
import com.snowplowanalytics.snowplow.tracker.emitter.RequestSecurity

data class InspetorDependencies(
    var trackerName: String,
    var appId: String,
    var base64Encoded: Boolean = InspetorConfig.DEFAULT_BASE64_OPTION,
    var collectorUri: String = InspetorConfig.DEFAULT_COLLECTOR_URI,
    var httpMethodType: HttpMethod = InspetorConfig.DEFAULT_HTTP_METHOD_TYPE,
    var bufferOption: BufferOption = InspetorConfig.DEFAULT_BUFFERSIZE_OPTION,
    var protocolType: RequestSecurity = InspetorConfig.DEFAULT_PROTOCOL_TYPE
)
