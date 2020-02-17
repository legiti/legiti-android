//
//  LegitiDependencies.kt
//  com.legiti-android-sdk
//
//  Created by Matheus Sato on 12/4/19.
//  Copyright © 2019 Legiti. All rights reserved.
//
package com.legiti.helpers

object LegitiDependencies {
    //Emitter and Tracker basic config
    const val DEFAULT_BASE64_OPTION: Boolean = true
    const val DEFAULT_COLLECTOR_URL: String = "heimdall-prod.inspcdn.net/" //Remember to add the "/" to the end of the url
    const val DEFAULT_COLLECTOR_PROD_PATH: String = "prod"
    const val DEFAULT_COLLECTOR_STAGING_PATH: String = "staging"
    val DEFAULT_HTTP_METHOD_TYPE: HttpMethodType = HttpMethodType.POST
    val DEFAULT_PROTOCOL_TYPE: RequestSecurityProtocol = RequestSecurityProtocol.HTTPS
    val DEFAULT_BUFFER_SIZE_OPTION: BufferOptionSize = BufferOptionSize.SINGLE

    // Schema versions
    const val FRONTEND_CONTEXT_SCHEMA_VERSION:       String = "iglu:com.com.legiti/inspetor_context/jsonschema/1-0-0"
    const val FRONTEND_FINGERPRINT_SCHEMA_VERSION:   String = "iglu:com.com.legiti/inspetor_fingerprint_frontend/jsonschema/1-0-3"
    const val FRONTEND_USER_SCHEMA_VERSION:          String = "iglu:com.com.legiti/inspetor_user_frontend/jsonschema/1-0-0"
    const val FRONTEND_AUTH_SCHEMA_VERSION:          String = "iglu:com.com.legiti/inspetor_auth_frontend/jsonschema/1-0-4"
    const val FRONTEND_PASS_RECOVERY_SCHEMA_VERSION: String = "iglu:com.com.legiti/inspetor_pass_recovery_frontend/jsonschema/1-0-0"
    const val FRONTEND_PASS_RESET_SCHEMA_VERSION:    String = "iglu:com.com.legiti/inspetor_pass_reset_frontend/jsonschema/1-0-0"
    const val FRONTEND_ORDER_SCHEMA_VERSION:         String = "iglu:com.com.legiti/inspetor_order_frontend/jsonschema/1-0-0"
}
