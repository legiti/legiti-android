//
//  InspetorResource.kt
//  inspetor-android-sdk
//
//  Created by Matheus Sato on 12/4/19.
//  Copyright © 2019 Inspetor. All rights reserved.
//
package com.inspetor

import android.content.Context
import com.snowplowanalytics.snowplow.tracker.*;
import com.snowplowanalytics.snowplow.tracker.Emitter
import com.snowplowanalytics.snowplow.tracker.DevicePlatforms
import com.snowplowanalytics.snowplow.tracker.Tracker.init
import com.snowplowanalytics.snowplow.tracker.emitter.BufferOption
import com.snowplowanalytics.snowplow.tracker.emitter.HttpMethod
import com.snowplowanalytics.snowplow.tracker.emitter.RequestSecurity
import com.snowplowanalytics.snowplow.tracker.events.SelfDescribing
import com.snowplowanalytics.snowplow.tracker.events.Structured
import com.snowplowanalytics.snowplow.tracker.payload.SelfDescribingJson
import kotlin.collections.HashMap

class InspetorResource: InspetorService {
    private var trackerName: String = ""
    private var appId: String = ""
    private var base64Encoded: Boolean = false
    private var collectorUri: String = ""
    private var httpMethod: HttpMethod? = null
    private var protocolType: RequestSecurity? = null
    private var bufferOption: BufferOption? = null
    private var tracker: Tracker? = null
    private var subject: Subject? = null
    private var clientName: String? = null

    companion object {
        @JvmStatic fun main(args: Array<String>) {}
    }

    override fun setContext(context: Context){
        setupTracker(context)
    }

    override fun verifyTracker(): Boolean {
        require(verifySetup())

        return (tracker != null)
    }

    override fun setup(dependencies: InspetorDependencies) {
        require(validateTrackerName(dependencies.trackerName))

        trackerName = dependencies.trackerName
        appId = dependencies.appId
        clientName = dependencies.trackerName.split(InspetorConfig.DEFAULT_INSPETOR_TRACKER_NAME_SEPARATOR)[0]
        base64Encoded = dependencies.base64Encoded
        collectorUri = dependencies.collectorUri
        switchBufferOptionSize(dependencies.bufferOptionSize)
        switchHttpMethod(dependencies.httpMethodType)
        switchSecurityProtocol(dependencies.requestSecurityProtocol)

        require(verifySetup())
    }

    override fun setActiveUser(userId: String) {
        require(verifyTracker())
        tracker?.subject?.setUserId(userId)
    }

    override fun unsetActiveUser() {
        if (!verifyTracker()) {
            return
        }
        tracker?.subject?.setUserId("")
    }

    override fun trackLogin(userId: String) {
        require(verifyTracker())

        setActiveUser(userId)
        val data: HashMap<String, String> = HashMap<String, String>()
        data["userId"] = userId
        trackUnstructuredEvent(InspetorConfig.FRONTEND_LOGIN_SCHEMA_VERSION, data)
    }

    override fun trackLogout(userId: String) {
        require(verifyTracker())

        val data: HashMap<String, String> = HashMap<String, String>()
        data["userId"] = userId
        trackUnstructuredEvent(InspetorConfig.FRONTEND_LOGOUT_SCHEMA_VERSION, data)
        unsetActiveUser()
    }

    override fun trackAccountCreation(userId: String) {
        require(verifyTracker())

        val data: HashMap<String, String> = HashMap<String, String>()
        data["userId"] = userId
        trackUnstructuredEvent(InspetorConfig.FRONTEND_ACCOUNT_CREATION_SCHEMA_VERSION, data)
    }

    override fun trackAccountUpdate(userId: String) {
        require(verifyTracker())

        val data: HashMap<String, String> = HashMap<String, String>()
        data["userId"] = userId
        trackUnstructuredEvent(InspetorConfig.FRONTEND_ACCOUNT_UPDATE_SCHEMA_VERSION, data)
    }

    override fun trackCreateOrder(transactionId: String) {
        require(verifyTracker())

        val data: HashMap<String, String> = HashMap<String, String>()
        data["transactionId"] = transactionId
        trackUnstructuredEvent(InspetorConfig.FRONTEND_CREATE_ORDER_SCHEMA_VERSION, data)
    }

    override fun trackPayOrder(transactionId: String) {
        require(verifyTracker())

        val data: HashMap<String, String> = HashMap<String, String>()
        data["transactionId"] = transactionId
        trackUnstructuredEvent(InspetorConfig.FRONTEND_PAY_ORDER_SCHEMA_VERSION, data)
    }

    override fun trackCancelOrder(transactionId: String) {
        require(verifyTracker())

        val data: HashMap<String, String> = HashMap<String, String>()
        data["transactionId"] = transactionId
        trackUnstructuredEvent(InspetorConfig.FRONTEND_CANCEL_ORDER_SCHEMA_VERSION, data)
    }

    override fun trackTicketTransfer(ticketId: String, userId: String, recipient: String) {
        require(verifyTracker())

        val data: HashMap<String, String> = HashMap<String, String>()
        data["ticketId"] = ticketId
        data["user"] = userId
        data["recipient"] = recipient
        trackUnstructuredEvent(InspetorConfig.FRONTEND_TICKET_TRANSFER_SCHEMA_VERSION, data)
    }

    override fun trackRecoverPasswordRequest(email: String) {
        require(verifyTracker())

        val data: HashMap<String, String> = HashMap<String, String>()
        data["email"] = email
        trackUnstructuredEvent(InspetorConfig.FRONTEND_RECOVER_PASSWORD_SCHEMA_VERSION, data)
    }

    override fun trackChangePassword(email: String) {
        require(verifyTracker())

        val data: HashMap<String, String> = HashMap<String, String>()
        data["email"] = email
        trackUnstructuredEvent(InspetorConfig.FRONTEND_CHANGE_PASSWORD_SCHEMA_VERSION, data)
    }

    private fun initializeTracker(context: Context): Tracker? {
        val emitter = Emitter.EmitterBuilder(collectorUri, context)
            .method(httpMethod)
            .option(bufferOption)
            .security(protocolType)
            .build()

        if (tracker == null) {
           return init(
                Tracker.TrackerBuilder(emitter, trackerName, appId, context)
                    .base64(base64Encoded)
                    .platform(DevicePlatforms.Mobile)
                    .sessionContext(true)
                    .sessionCheckInterval(10)
                    .foregroundTimeout(300)
                    .backgroundTimeout(120).build()
            )
        }

        return tracker
    }

    private fun trackUnstructuredEvent(schema: String, data: HashMap<String, String>) {
        val eventData: SelfDescribingJson = SelfDescribingJson(schema, data)
        tracker?.track(
            SelfDescribing.builder()
                .eventData(eventData)
                .build()
        )
    }

    private fun structuredEventBuilderHelper(
        category: String,
        action: String,
        label: String,
        property: String
    ): Unit? {

        return tracker?.track(
            Structured.builder()
                .category(category)
                .action(action)
                .label(label)
                .property(property)
                .build()
        )
    }

    private fun setupTracker(context: Context) {
        require(verifySetup())

        tracker = initializeTracker(context)
        tracker?.subject = if (subject == null) Subject.SubjectBuilder().build() else subject

        require(verifyTracker())
    }

    private fun verifySetup(): Boolean {
        return (appId != "" && trackerName != "")
    }

    private fun validateTrackerName(trackerName: String): Boolean {
        val trackerNameArray = trackerName.split(InspetorConfig.DEFAULT_INSPETOR_TRACKER_NAME_SEPARATOR)
        return (trackerNameArray.count() == 2 &&
                trackerNameArray[0].count() > 1 &&
                trackerNameArray[1].count() > 1)
    }

    private fun switchBufferOptionSize (bufferOptionSize: BufferOptionSize) {
        bufferOption = when(bufferOptionSize) {
            BufferOptionSize.SINGLE -> BufferOption.Single
            BufferOptionSize.HEAVY -> BufferOption.HeavyGroup
            else -> BufferOption.DefaultGroup
        }
    }

    private fun switchSecurityProtocol (requestSecurityProtocol: RequestSecurityProtocol) {
        protocolType = when (requestSecurityProtocol) {
            RequestSecurityProtocol.HTTP -> RequestSecurity.HTTP
            else -> RequestSecurity.HTTPS
        }
    }

    private fun switchHttpMethod (httpMethodType: HttpMethodType) {
         httpMethod = when (httpMethodType) {
            HttpMethodType.GET -> HttpMethod.GET
            else -> HttpMethod.POST
        }
    }
}

