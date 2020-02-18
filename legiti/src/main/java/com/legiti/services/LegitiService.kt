package com.legiti.services

import android.content.Context

interface LegitiService {

    /**
     * Send user creation data to Legiti
     *
     * @param userId
     * @return Boolean
     */
    fun trackUserCreation(userId: String): Boolean?

    /**
     * Send user update data to Legiti
     *
     * @param userId
     * @return Boolean
     */
    fun trackUserUpdate(userId: String): Boolean?

    /**
     * Send user login data to Legiti
     *
     * @param userEmail
     * @param userId
     * @return Boolean
     */
    fun trackLogin(userEmail: String, userId: String?): Boolean?

    /**
     * Send user logout data to Legiti
     *
     * @param userEmail
     * @param userId
     * @return Boolean
     */
    fun trackLogout(userEmail: String, userId: String?): Boolean?

    /**
     * Send password reset data to Legiti
     *
     * @param userId
     * @return Boolean
     */
    fun trackPasswordReset(userId: String): Boolean?

    /**
     * Send password recovery data to Legiti
     *
     * @param userEmail
     * @return Boolean
     */
    fun trackPasswordRecovery(userEmail: String): Boolean?

    /**
     * Send order creation data to Legiti
     *
     * @param orderId
     * @return Boolean
     */
    fun trackOrderCreation(orderId: String): Boolean?

    fun trackPageView(pageTitle: String): Boolean?

    /**
     * Send context to Legiti
     *
     * @param context
     * @return void
     */
    fun setContext(context: Context)

    /**
     * Set config to Legiti
     *
     * @param trackerName
     * @param appId
     * @param devEnv
     * @return void
     */
    fun setup(authToken: String, legitiDevEnv: Boolean = false)

    /**
     * Verify if config exists
     *
     * @return boolean
     */
    fun isConfigured(): Boolean
}