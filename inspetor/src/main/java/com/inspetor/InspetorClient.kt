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
    private var androidContext: Context?

    init {
        this.inspetorResource = null
        this.inspetorConfig = null
        this.androidContext = null
    }

    override fun setup(authToken: String, inspetorEnv: Boolean) {
        if (!InspetorConfig.Companion.isValid(authToken)) {
            throw InvalidCredentials("Inspetor Exception 9002: authToken not valid")
        }


        this.inspetorConfig = InspetorConfig(authToken, inspetorEnv)

        if (this.androidContext != null) {
            this.setContext(androidContext!!)
        } else {
            throw ContextNotSetup("Inspetor Exception 9000: Could not get the context please pass it to the setContext function.")
        }

    }

    override fun setContext(context: Context) {
        this.hasConfig()

        val config = this.inspetorConfig

        if (this.androidContext == null) {
            this.androidContext = context
        }

        this.inspetorResource = InspetorResource(config!!, context)
    }

    override fun isConfigured(): Boolean {
        return try {
            this.hasConfig()
            true
        } catch (ex: InvalidCredentials) {
            false
        }
    }

    override fun trackLogin(accountEmail: String, accountId: String?): Boolean? {
        this.hasConfig()

        val data = this.createJson(id = accountEmail, prefix = "auth", idSuffix = "account_email")
        data["auth_account_id"] =  this.encodeData(accountId)

        return inspetorResource?.trackAccountAuthAction(data, AuthAction.ACCOUNT_LOGIN_ACTION)
    }

    override fun trackLogout(accountEmail: String, accountId: String?): Boolean? {
        this.hasConfig()

        val data = this.createJson(id = accountEmail, prefix = "auth", idSuffix = "account_email")
        data["auth_account_id"] =  this.encodeData(accountId)

        return inspetorResource?.trackAccountAuthAction(data, AuthAction.ACCOUNT_LOGOUT_ACTION)
    }

    override fun trackAccountCreation(accountId: String): Boolean? {
        this.hasConfig()

        val data = this.createJson(id = accountId, prefix = "account")
        return inspetorResource?.trackAccountAction(data, AccountAction.ACCOUNT_CREATE_ACTION)
    }

    override fun trackAccountUpdate(accountId: String): Boolean? {
        this.hasConfig()

        val data = this.createJson(id = accountId, prefix = "account")
        return inspetorResource?.trackAccountAction(data, AccountAction.ACCOUNT_UPDATE_ACTION)
    }

    override fun trackAccountDeletion(accountId: String): Boolean? {
        this.hasConfig()

        val data = this.createJson(id = accountId, prefix = "account")
        return inspetorResource?.trackAccountAction(data, AccountAction.ACCOUNT_DELETE_ACTION)
    }

    override fun trackSaleCreation(saleId: String): Boolean? {
        this.hasConfig()

        val data = this.createJson(id = saleId, prefix = "sale")
        return inspetorResource?.trackSaleAction(data, SaleAction.SALE_CREATE_ACTION)
    }

    override fun trackSaleUpdate(saleId: String): Boolean? {
        this.hasConfig()

        val data = this.createJson(id = saleId, prefix = "sale")
        return inspetorResource?.trackSaleAction(data, SaleAction.SALE_UPDATE_STATUS_ACTION)
    }

    override fun trackEventCreation(eventId: String): Boolean? {
        this.hasConfig()

        val data = this.createJson(id = eventId, prefix = "event")
        return inspetorResource?.trackEventAction(data, EventAction.EVENT_CREATE_ACTION)
    }

    override fun trackEventUpdate(eventId: String): Boolean? {
        this.hasConfig()

        val data = this.createJson(id = eventId, prefix = "event")
        return inspetorResource?.trackEventAction(data, EventAction.EVENT_UPDATE_ACTION)
    }

    override fun trackEventDeletion(eventId: String): Boolean? {
        this.hasConfig()

        val data = this.createJson(id = eventId, prefix = "event")
        return inspetorResource?.trackEventAction(data, EventAction.EVENT_DELETE_ACTION)
    }

    override fun trackItemTransferCreation(transferId: String): Boolean? {
        this.hasConfig()

        val data = this.createJson(id = transferId, prefix = "transfer")
        return inspetorResource?.trackItemTransferAction(data, TransferAction.TRANSFER_CREATE_ACTION)
    }

    override fun trackItemTransferUpdate(transferId: String): Boolean? {
        this.hasConfig()

        val data = this.createJson(id = transferId, prefix = "transfer")
        return inspetorResource?.trackItemTransferAction(data, TransferAction.TRANSFER_UPDATE_STATUS_ACTION)
    }

    override fun trackPasswordReset(accountEmail: String): Boolean? {
        this.hasConfig()

        val data = this.createJson(id = accountEmail, prefix = "pass_recovery", idSuffix = "email")
        return inspetorResource?.trackPasswordRecoveryAction(data, PassRecoveryAction.PASSWORD_RESET_ACTION)
    }

    override fun trackPasswordRecovery(accountEmail: String): Boolean? {
        this.hasConfig()

        val data = this.createJson(id = accountEmail, prefix = "pass_recovery", idSuffix = "email")
        return inspetorResource?.trackPasswordRecoveryAction(data, PassRecoveryAction.PASSWORD_RECOVERY_ACTION)
    }

    override fun trackPageView(pageTitle: String): Boolean? {
        this.hasConfig()
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

    private fun hasConfig() {
        if (this.inspetorConfig == null) {
            throw InvalidCredentials("Inspetor Exception 9001: Library not configured.")
        }
    }

    internal fun setContextWithoutConfig(context: Context?): Boolean {
        if (context != null) {
            this.androidContext = context
            return true
        }

        return false
    }

}