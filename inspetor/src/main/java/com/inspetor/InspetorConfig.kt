//
//  InspetorConfig.kt
//  inspetor-android-sdk
//
//  Created by Matheus Sato on 12/4/19.
//  Copyright © 2019 Inspetor. All rights reserved.
//
package com.inspetor

import com.snowplowanalytics.snowplow.tracker.emitter.BufferOption
import com.snowplowanalytics.snowplow.tracker.emitter.HttpMethod
import com.snowplowanalytics.snowplow.tracker.emitter.RequestSecurity

object InspetorConfig {
    //Emitter and Tracker basic config
    const val DEFAULT_BASE64_OPTION: Boolean = true
    const val DEFAULT_COLLECTOR_URI: String = "analytics-staging.useinspetor.com"
    const val DEFAULT_INSPETOR_TRACKER_NAME_SEPARATOR: String = "."
    val DEFAULT_HTTP_METHOD_TYPE: HttpMethod = HttpMethod.POST
    val DEFAULT_PROTOCOL_TYPE: RequestSecurity = RequestSecurity.HTTPS
    val DEFAULT_BUFFERSIZE_OPTION: BufferOption = BufferOption.Single //25

    // Schema versions
    const val FRONTEND_PAY_ORDER_SCHEMA_VERSION: String = "iglu:com.inspetor/frontend_pay_order/jsonschema/1-0-0"
    const val FRONTEND_CANCEL_ORDER_SCHEMA_VERSION: String = "iglu:com.inspetor/frontend_cancel_order/jsonschema/1-0-0"
    const val FRONTEND_CREATE_ORDER_SCHEMA_VERSION: String = "iglu:com.inspetor/frontend_create_order/jsonschema/1-0-0"
    const val FRONTEND_TICKET_TRANSFER_SCHEMA_VERSION: String = "iglu:com.inspetor/frontend_ticket_transfer/jsonschema/1-0-1"
    const val FRONTEND_CHANGE_PASSWORD_SCHEMA_VERSION: String = "iglu:com.inspetor/frontend_change_password/jsonschema/1-0-1"
    const val FRONTEND_ACCOUNT_UPDATE_SCHEMA_VERSION: String = "iglu:com.inspetor/frontend_account_update/jsonschema/1-0-1"
    const val FRONTEND_ACCOUNT_CREATION_SCHEMA_VERSION: String = "iglu:com.inspetor/frontend_account_creation/jsonschema/1-0-1"
    const val FRONTEND_LOGIN_SCHEMA_VERSION: String = "iglu:com.inspetor/frontend_login/jsonschema/1-0-1"
    const val FRONTEND_LOGOUT_SCHEMA_VERSION: String = "iglu:com.inspetor/frontend_logout/jsonschema/1-0-1"
    const val FRONTEND_RECOVER_PASSWORD_SCHEMA_VERSION: String = "iglu:com.inspetor/frontend_recover_password/jsonschema/1-0-2"
}
