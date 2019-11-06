//
//  InspetorResource.kt
//  inspetor-android-sdk
//
//  Created by Matheus Sato on 12/4/19.
//  Copyright © 2019 Inspetor. All rights reserved.
//
package com.inspetor

import android.content.Context
import android.os.Build
import android.util.Base64
import com.inspetor.helpers.*
import com.inspetor.services.InspetorService
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.HashMap

class InspetorClient : InspetorService {
    private var inspetorResource: InspetorResource?
    private var inspetorConfig: InspetorConfig?
    private var doneSetup: Boolean
    private var androidContext: Context?

    init {
        this.inspetorResource = null
        this.inspetorConfig = null
        this.doneSetup = false
        this.androidContext = null
    }

    override fun setup(trackerName: String, appId: String, devEnv: Boolean, inspetorEnv: Boolean) {
        if (appId.isEmpty() || trackerName.isEmpty()) {
            throw Exception("Exception 9001: appId and trackerName are required parameters.")
        }

        require(validateTrackerName(trackerName)) { "Inspetor Exception 9002: trackerName should have 2 terms (e.g. \"tracker.name\")." }

        this.inspetorConfig = InspetorConfig(trackerName, appId, devEnv, inspetorEnv)

        if (this.androidContext != null) {
            this.setContext(androidContext!!)
            doneSetup = true
        } else {
            throw Exception("Inspetor Exception 9000: Could not get the context please pass it to the setContext function.")
        }

    }

    override fun setContext(context: Context) {
        require(hasConfig()) { "Inspetor Exception 9001: appId and trackerName are required parameters."}

        val config = this.inspetorConfig

        if (this.androidContext == null) {
            this.androidContext = context
        }

        this.inspetorResource = InspetorResource(config!!, context)
    }

    override fun isConfigured(): Boolean {
        require(hasConfig()) { "Inspetor Exception 9001: appId and trackerName are required parameters."}

        return doneSetup
    }

    override fun trackLogin(accountEmail: String, accountId: String?): Boolean? {
        require(hasConfig()) { "Inspetor Exception 9001: appId and trackerName are required parameters."}

        val data = this.createJson(id = accountEmail, prefix = "auth", idSuffix = "accountEmail")
        data["auth_account_id"] =  this.encodeData(accountId)

        return inspetorResource?.trackAccountAuthAction(data, AuthAction.ACCOUNT_LOGIN_ACTION)
    }

    override fun trackLogout(accountEmail: String, accountId: String?): Boolean? {
        require(hasConfig()) { "Inspetor Exception 9001: appId and trackerName are required parameters."}

        val data = this.createJson(id = accountEmail, prefix = "auth", idSuffix = "accountEmail")
        data["auth_account_id"] =  this.encodeData(accountId)

        return inspetorResource?.trackAccountAuthAction(data, AuthAction.ACCOUNT_LOGOUT_ACTION)
    }

    override fun trackAccountCreation(accountId: String): Boolean? {
        require(hasConfig()) { "Inspetor Exception 9001: appId and trackerName are required parameters."}

        val data = this.createJson(id = accountId, prefix = "account")
        return inspetorResource?.trackAccountAction(data, AccountAction.ACCOUNT_CREATE_ACTION)
    }

    override fun trackAccountUpdate(accountId: String): Boolean? {
        require(hasConfig()) { "Inspetor Exception 9001: appId and trackerName are required parameters."}

        val data = this.createJson(id = accountId, prefix = "account")
        return inspetorResource?.trackAccountAction(data, AccountAction.ACCOUNT_UPDATE_ACTION)
    }

    override fun trackAccountDeletion(accountId: String): Boolean? {
        require(hasConfig()) { "Inspetor Exception 9001: appId and trackerName are required parameters."}

        val data = this.createJson(id = accountId, prefix = "account")
        return inspetorResource?.trackAccountAction(data, AccountAction.ACCOUNT_DELETE_ACTION)
    }

    override fun trackSaleCreation(saleId: String): Boolean? {
        require(hasConfig()) { "Inspetor Exception 9001: appId and trackerName are required parameters."}

        val data = this.createJson(id = saleId, prefix = "sale")
        return inspetorResource?.trackSaleAction(data, SaleAction.SALE_CREATE_ACTION)
    }

    override fun trackSaleUpdate(saleId: String): Boolean? {
        require(hasConfig()) { "Inspetor Exception 9001: appId and trackerName are required parameters."}

        val data = this.createJson(id = saleId, prefix = "sale")
        return inspetorResource?.trackSaleAction(data, SaleAction.SALE_UPDATE_STATUS_ACTION)
    }

    override fun trackEventCreation(eventId: String): Boolean? {
        require(hasConfig()) { "Inspetor Exception 9001: appId and trackerName are required parameters."}

        val data = this.createJson(id = eventId, prefix = "event")
        return inspetorResource?.trackEventAction(data, EventAction.EVENT_CREATE_ACTION)
    }

    override fun trackEventUpdate(eventId: String): Boolean? {
        require(hasConfig()) { "Inspetor Exception 9001: appId and trackerName are required parameters."}

        val data = this.createJson(id = eventId, prefix = "event")
        return inspetorResource?.trackEventAction(data, EventAction.EVENT_UPDATE_ACTION)
    }

    override fun trackEventDeletion(eventId: String): Boolean? {
        require(hasConfig()) { "Inspetor Exception 9001: appId and trackerName are required parameters."}

        val data = this.createJson(id = eventId, prefix = "event")
        return inspetorResource?.trackEventAction(data, EventAction.EVENT_DELETE_ACTION)
    }

    override fun trackItemTransferCreation(transferId: String): Boolean? {
        require(hasConfig()) { "Inspetor Exception 9001: appId and trackerName are required parameters."}

        val data = this.createJson(id = transferId, prefix = "transfer")
        return inspetorResource?.trackItemTransferAction(data, TransferAction.TRANSFER_CREATE_ACTION)
    }

    override fun trackItemTransferUpdate(transferId: String): Boolean? {
        require(hasConfig()) { "Inspetor Exception 9001: appId and trackerName are required parameters."}

        val data = this.createJson(id = transferId, prefix = "transfer")
        return inspetorResource?.trackItemTransferAction(data, TransferAction.TRANSFER_UPDATE_STATUS_ACTION)
    }

    override fun trackPasswordReset(accountEmail: String): Boolean? {
        require(hasConfig()) { "Inspetor Exception 9001: appId and trackerName are required parameters."}

        val data = this.createJson(id = accountEmail, prefix = "pass_recovery", idSuffix = "email")
        return inspetorResource?.trackPasswordRecoveryAction(data, PassRecoveryAction.PASSWORD_RESET_ACTION)
    }

    override fun trackPasswordRecovery(accountEmail: String): Boolean? {
        require(hasConfig()) { "Inspetor Exception 9001: appId and trackerName are required parameters."}

        val data = this.createJson(id = accountEmail, prefix = "pass_recovery", idSuffix = "email")
        return inspetorResource?.trackPasswordRecoveryAction(data, PassRecoveryAction.PASSWORD_RECOVERY_ACTION)
    }

    override fun trackPageView(pageTitle: String): Boolean? {
        require(hasConfig()) { "Inspetor Exception 9001: appId and trackerName are required parameters."}

        return inspetorResource?.trackPageView(pageTitle)
    }


    private fun getInspetorTimestamp(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            val dateTime = LocalDateTime.now(ZoneId.of("UTC"))
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'+00:00'")
            dateTime.format(formatter)
        } else {
            val dateFormat = SimpleDateFormat("yyyy/MM/dd'T'HH:mm:ss'+00:00'", Locale.US)
            val date = Date()
            dateFormat.format(date)
        }
    }

    private fun encodeData(stringToEncode: String?): String? {
        if (stringToEncode != null) {
            return Base64.encodeToString(stringToEncode.toByteArray(), Base64.NO_WRAP)
        }
        return null
    }

    private fun createJson(
        id: String,
        prefix: String,
        idSuffix: String = "id",
        timestampSuffix: String = "timestamp"
    ): HashMap<String, String?> {

        val data = HashMap<String, String?>()

        val idProperty = prefix.plus("_").plus(idSuffix)
        val timestampProperty = prefix.plus("_").plus(timestampSuffix)

        data[idProperty] = this.encodeData(id)
        data[timestampProperty] = this.encodeData(this.getInspetorTimestamp())

        return data
    }


    private fun validateTrackerName(trackerName: String): Boolean {
        val trackerNameArray = trackerName.split(InspetorDependencies.DEFAULT_INSPETOR_TRACKER_NAME_SEPARATOR)
        return (trackerNameArray.count() == 2 &&
                trackerNameArray[0].count() > 1 &&
                trackerNameArray[1].count() > 1)
    }

    private fun hasConfig(): Boolean {
        return (!inspetorConfig?.appId.isNullOrBlank() && !inspetorConfig?.trackerName.isNullOrBlank())
    }

    internal fun setContextWithoutConfig(context: Context?): Boolean {
        if (context != null) {
            this.androidContext = context
            return true
        }

        return false
    }

}