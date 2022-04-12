package com.legiti

import android.content.Context
import android.os.Build
import android.util.Base64
import android.util.Log
import com.legiti.helpers.*
import com.legiti.services.LegitiService
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.HashMap

class LegitiClient : LegitiService {
    private var legitiResource: LegitiResource?
    private var legitiConfig: LegitiConfig?
    private var androidContext: Context?
    private var legitiFingerprint: LegitiFingerprint

    init {
        this.legitiFingerprint = LegitiFingerprint()
        this.legitiFingerprint.execute()
        this.legitiResource = null
        this.legitiConfig = null
        this.androidContext = null
    }


    override fun setup(authToken: String) {
        this.legitiConfig = LegitiConfig(authToken)

        if (this.androidContext != null) {
            this.setContext(androidContext!!)
        } else {
            throw ContextNotSetup("Legiti Exception 9003: Could not get the context please pass it to the setContext function.")
        }

    }

    override fun setContext(context: Context) {
        this.hasConfig()

        val config = this.legitiConfig

        if (this.androidContext == null) {
            this.androidContext = context
        }

        this.legitiResource = LegitiResource(config!!, context, this.legitiFingerprint)
    }

    override fun isConfigured(): Boolean {
        return try {
            this.hasConfig()
            true
        } catch (ex: InvalidCredentials) {
            false
        }
    }

    override fun trackLogin(userEmail: String, userId: String?) {
        this.hasConfig()

        val data = this.createJson(id = userEmail, prefix = "auth", idSuffix = "user_email")
        data["auth_user_id"] =  this.encodeData(userId)
        legitiResource?.currentUserId = if (userId.isNullOrEmpty()) null else userId

        legitiResource?.trackUserAuthAction(data, AuthAction.USER_LOGIN_ACTION)
    }

    override fun trackLogout(userEmail: String, userId: String?) {
        this.hasConfig()

        val data = this.createJson(id = userEmail, prefix = "auth", idSuffix = "user_email")
        data["auth_user_id"] =  this.encodeData(userId)
        legitiResource?.currentUserId = null

        legitiResource?.trackUserAuthAction(data, AuthAction.USER_LOGOUT_ACTION)
    }

    override fun trackUserCreation(userId: String) {
        this.hasConfig()

        val data = this.createJson(id = userId, prefix = "user")
        legitiResource?.trackUserAction(data, UserAction.USER_CREATE_ACTION)
    }

    override fun trackUserUpdate(userId: String) {
        this.hasConfig()

        val data = this.createJson(id = userId, prefix = "user")
        legitiResource?.trackUserAction(data, UserAction.USER_UPDATE_ACTION)
    }

    override fun trackOrderCreation(orderId: String) {
        this.hasConfig()

        val data = this.createJson(id = orderId, prefix = "order")
        legitiResource?.trackOrderAction(data, OrderAction.ORDER_CREATE_ACTION)
    }

    override fun trackPasswordRecovery(userEmail: String) {
        this.hasConfig()

        val data = this.createJson(id = userEmail, prefix = "pass_recovery", idSuffix = "email")
        legitiResource?.trackPasswordRecoveryAction(data, PasswordAction.PASSWORD_RECOVERY_ACTION)
    }

    override fun trackPageView(pageTitle: String) {
        this.hasConfig()
        legitiResource?.trackPageView(pageTitle)
    }

    private fun getLegitiTimestamp(): String {
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
        data[timestampProperty] = this.encodeData(this.getLegitiTimestamp())

        return data
    }

    private fun hasConfig() {
        if (this.legitiConfig == null) {
            throw InvalidCredentials("Legiti Exception 9001: Library not configured.")
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