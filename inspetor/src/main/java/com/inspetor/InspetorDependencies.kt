//
//  InspetorDependencies.kt
//  inspetor-android-sdk
//
//  Created by Matheus Sato on 12/4/19.
//  Copyright © 2019 Inspetor. All rights reserved.
//
package com.inspetor

data class InspetorDependencies(
    var trackerName: String,
    var appId: String,
    var base64Encoded: Boolean = InspetorConfig.DEFAULT_BASE64_OPTION,
    var collectorUri: String = InspetorConfig.DEFAULT_COLLECTOR_URI,
    var httpMethodType: HttpMethodType = InspetorConfig.DEFAULT_HTTP_METHOD_TYPE,
    var requestSecurityProtocol: RequestSecurityProtocol = InspetorConfig.DEFAULT_PROTOCOL_TYPE,
    var bufferOptionSize: BufferOptionSize = InspetorConfig.DEFAULT_BUFFERSIZE_OPTION
)