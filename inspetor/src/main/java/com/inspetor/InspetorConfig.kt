//
//  InspetorConfig.ktnspetor-android-sdk
//
//  Created by Matheus Sato on 12/4/19.
//  Copyright © 2019 Inspetor. All rights reserved.
//
package com.inspetor

data class InspetorConfig(
    var trackerName: String,
    var appId: String,
    var base64Encoded: Boolean = InspetorDependencies.DEFAULT_BASE64_OPTION,
    var collectorUri: String = InspetorDependencies.DEFAULT_COLLECTOR_URI,
    var httpMethodType: HttpMethodType = InspetorDependencies.DEFAULT_HTTP_METHOD_TYPE,
    var requestSecurityProtocol: RequestSecurityProtocol = InspetorDependencies.DEFAULT_PROTOCOL_TYPE,
    var bufferOptionSize: BufferOptionSize = InspetorDependencies.DEFAULT_BUFFERSIZE_OPTION
)