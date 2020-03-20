package com.legiti.services

import android.content.Context

interface LegitiService {

    /**
     * Send user creation data to Legiti
     *
     * @param userId
     */
    fun trackUserCreation(userId: String)

    /**
     * Send user update data to Legiti
     *
     * @param userId
     */
    fun trackUserUpdate(userId: String)

    /**
     * Send user login data to Legiti
     *
     * @param userEmail
     * @param userId
     */
    fun trackLogin(userEmail: String, userId: String?)

    /**
     * Send user logout data to Legiti
     *
     * @param userEmail
     * @param userId
     */
    fun trackLogout(userEmail: String, userId: String?)

    /**
     * Send password reset data to Legiti
     *
     * @param userId
     */
    fun trackPasswordReset(userId: String)

    /**
     * Send password recovery data to Legiti
     *
     * @param userEmail
     */
    fun trackPasswordRecovery(userEmail: String)

    /**
     * Send order creation data to Legiti
     *
     * @param orderId
     */
    fun trackOrderCreation(orderId: String)

    fun trackPageView(pageTitle: String)

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