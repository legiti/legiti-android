//
//  InspetorConfig11.kt
//  inspetor-android-sdk
//
//  Created by Matheus Sato on 12/4/19.
//  Copyright © 2019 Inspetor. All rights reserved.
//
package com.inspetor

object InspetorDependencies {
    //Emitter and Tracker basic config
    const val DEFAULT_DEV_MODE: Boolean = false
    const val DEFAULT_INSPETOR_MODE: Boolean = false
    const val DEFAULT_BASE64_OPTION: Boolean = true
    const val DEFAULT_COLLECTOR_URI: String = "heimdall-staging.inspcdn.net/prod"
    const val DEFAULT_COLLECTOR_DEV_URI: String = "heimdall-staging.inspcdn.net/staging"
    const val DEFAULT_COLLECTOR_INSPETOR_URI: String = "test.useinspetor.com"
    const val DEFAULT_INSPETOR_TRACKER_NAME_SEPARATOR: String = "."
    val DEFAULT_HTTP_METHOD_TYPE: HttpMethodType = HttpMethodType.POST
    val DEFAULT_PROTOCOL_TYPE: RequestSecurityProtocol = RequestSecurityProtocol.HTTPS
    val DEFAULT_BUFFERSIZE_OPTION: BufferOptionSize = BufferOptionSize.SINGLE //1

    // Schema versions
    const val FRONTEND_CONTEXT_SCHEMA_VERSION:       String = "iglu:com.inspetor/inspetor_context/jsonschema/1-0-0"
    const val FRONTEND_FINGERPRINT_SCHEMA_VERSION:   String = "iglu:com.inspetor/inspetor_fingerprint_frontend/jsonschema/1-0-3"
    const val FRONTEND_ACCOUNT_SCHEMA_VERSION:       String = "iglu:com.inspetor/inspetor_account_frontend/jsonschema/1-0-0"
    const val FRONTEND_AUTH_SCHEMA_VERSION:          String = "iglu:com.inspetor/inspetor_auth_frontend/jsonschema/1-0-3"
    const val FRONTEND_EVENT_SCHEMA_VERSION:         String = "iglu:com.inspetor/inspetor_event_frontend/jsonschema/1-0-0"
    const val FRONTEND_PASS_RECOVERY_SCHEMA_VERSION: String = "iglu:com.inspetor/inspetor_pass_recovery_frontend/jsonschema/1-0-0"
    const val FRONTEND_SALE_SCHEMA_VERSION:          String = "iglu:com.inspetor/inspetor_sale_frontend/jsonschema/1-0-0"
    const val FRONTEND_TRANSFER_SCHEMA_VERSION:      String = "iglu:com.inspetor/inspetor_transfer_frontend/jsonschema/1-0-0"
}
