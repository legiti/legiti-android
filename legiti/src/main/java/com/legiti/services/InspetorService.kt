//
//  InspetorService.kt
//  com.legiti-android-sdk
//
//  Created by Matheus Sato on 12/4/19.
//  Copyright © 2019 Inspetor. All rights reserved.
//
package com.legiti.services

import android.content.Context

interface InspetorService {

    /**
     * Send user creation data to Inspetor
     *
     * @param userId
     * @return Boolean
     */
    fun trackUserCreation(userId: String): Boolean?

    /**
     * Send user update data to Inspetor
     *
     * @param userId
     * @return Boolean
     */
    fun trackUserUpdate(userId: String): Boolean?

    /**
     * Send user login data to Inspetor
     *
     * @param userEmail
     * @param userId
     * @return Boolean
     */
    fun trackLogin(userEmail: String, userId: String?): Boolean?

    /**
     * Send user logout data to Inspetor
     *
     * @param userEmail
     * @param userId
     * @return Boolean
     */
    fun trackLogout(userEmail: String, userId: String?): Boolean?

    /**
     * Send password reset data to Inspetor
     *
     * @param userId
     * @return Boolean
     */
    fun trackPasswordReset(userId: String): Boolean?

    /**
     * Send password recovery data to Inspetor
     *
     * @param userEmail
     * @return Boolean
     */
    fun trackPasswordRecovery(userEmail: String): Boolean?

    /**
     * Send order creation data to Inspetor
     *
     * @param orderId
     * @return Boolean
     */
    fun trackOrderCreation(orderId: String): Boolean?

    fun trackPageView(pageTitle: String): Boolean?

    /**
     * Send context to InspetorClient
     *
     * @param context
     * @return void
     */
    fun setContext(context: Context)

    /**
     * Set config to InspetorClient
     *
     * @param trackerName
     * @param appId
     * @param devEnv
     * @return void
     */
    fun setup(authToken: String, inspetorDevEnv: Boolean = false)

    /**
     * Verify if config exists
     *
     * @return boolean
     */
    fun isConfigured(): Boolean
}