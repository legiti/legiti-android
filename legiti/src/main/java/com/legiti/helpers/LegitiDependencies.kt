package com.legiti.helpers

object LegitiDependencies {
    //Emitter and Tracker basic config
    const val DEFAULT_BASE64_OPTION: Boolean = true
    const val DEFAULT_COLLECTOR_URL: String = "heimdall-prod.lgtcdn.net/" //Remember to add the "/" to the end of the url
    const val DEFAULT_COLLECTOR_PROD_PATH: String = "post"
    const val DEFAULT_COLLECTOR_STAGING_PATH: String = "post-staging"
    val DEFAULT_HTTP_METHOD_TYPE: HttpMethodType = HttpMethodType.POST
    val DEFAULT_PROTOCOL_TYPE: RequestSecurityProtocol = RequestSecurityProtocol.HTTPS
    val DEFAULT_BUFFER_SIZE_OPTION: BufferOptionSize = BufferOptionSize.SINGLE

    // Schema versions
    const val FRONTEND_CONTEXT_SCHEMA_VERSION:       String = "iglu:com.legiti/legiti_context_frontend/jsonschema/1-0-0"
    const val FRONTEND_FINGERPRINT_SCHEMA_VERSION:   String = "iglu:com.legiti/legiti_fingerprint_frontend/jsonschema/1-0-1"
    const val FRONTEND_USER_SCHEMA_VERSION:          String = "iglu:com.legiti/legiti_user_frontend/jsonschema/1-0-0"
    const val FRONTEND_AUTH_SCHEMA_VERSION:          String = "iglu:com.legiti/legiti_auth_frontend/jsonschema/1-0-0"
    const val FRONTEND_PASS_RECOVERY_SCHEMA_VERSION: String = "iglu:com.legiti/legiti_pass_recovery_frontend/jsonschema/1-0-0"
    const val FRONTEND_PASS_RESET_SCHEMA_VERSION:    String = "iglu:com.legiti/legiti_pass_reset_frontend/jsonschema/1-0-0"
    const val FRONTEND_ORDER_SCHEMA_VERSION:         String = "iglu:com.legiti/legiti_order_frontend/jsonschema/1-0-0"
}
