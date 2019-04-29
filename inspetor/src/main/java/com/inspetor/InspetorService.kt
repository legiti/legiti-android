//
//  InspetorService.kt
//  inspetor-android-sdk
//
//  Created by Matheus Sato on 12/4/19.
//  Copyright © 2019 Inspetor. All rights reserved.
//
package com.inspetor

import android.content.Context
import com.snowplowanalytics.snowplow.tracker.Tracker
import com.snowplowanalytics.snowplow.tracker.emitter.BufferOption
import com.snowplowanalytics.snowplow.tracker.emitter.HttpMethod
import com.snowplowanalytics.snowplow.tracker.emitter.RequestSecurity

interface InspetorService {
    var base64Encoded: Boolean
    var trackerName: String
    var appId: String
    var bufferOption: BufferOption?
    var collectorUri : String
    var httpMethodType : HttpMethod?
    var protocolType : RequestSecurity?

    fun setup(dependencies: InspetorDependencies)

    fun setActiveUser(userId: String)

    fun unsetActiveUser()

    fun trackLogin(userId: String)

    fun trackLogout(userId: String)

    fun trackAccountCreation(userId: String)

    fun trackAccountUpdate(userId: String)

    fun trackCreateOrder(transactionId: String)

    fun trackPayOrder(transactionId: String)

    fun trackCancelOrder(transactionId: String)

    fun trackTicketTransfer(ticketId: String, userId: String, recipient: String)

    fun trackRecoverPasswordRequest(email: String)

    fun trackChangePassword(email: String)

    fun setContext(context: Context)

    fun verifyTracker(): Boolean
}