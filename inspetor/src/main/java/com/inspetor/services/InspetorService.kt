//
//  InspetorService.kt
//  inspetor-android-sdk
//
//  Created by Matheus Sato on 12/4/19.
//  Copyright © 2019 Inspetor. All rights reserved.
//
package com.inspetor.services

import android.content.Context

interface InspetorService {

    /**
     * Send account creation data to Inspetor
     *
     * @param accountId
     * @return void
     */
    fun trackAccountCreation(accountId: String): Boolean?

    /**
     * Send account update data to Inspetor
     *
     * @param accountId
     * @return void
     */
    fun trackAccountUpdate(accountId: String): Boolean?

    /**
     * Send account deletion data to Inspetor
     *
     * @param accountId
     * @return void
     */
    fun trackAccountDeletion(accountId: String): Boolean?

    /**
     * Send event creation data to Inspetor
     *
     * @param eventId
     * @return void
     */
    fun trackEventCreation(eventId: String): Boolean?

    /**
     * Send event update data to Inspetor
     *
     * @param eventId
     * @return void
     */
    fun trackEventUpdate(eventId: String): Boolean?

    /**
     * Send event deletion data to Inspetor
     *
     * @param eventId
     * @return void
     */
    fun trackEventDeletion(eventId: String): Boolean?

    /**
     * Send item transfer creation data to Inspetor
     *
     * @param transferId
     * @return void
     */
    fun trackItemTransferCreation(transferId: String): Boolean?

    /**
     * Send item transfer update data to Inspetor
     *
     * @param transferId
     * @return void
     */
    fun trackItemTransferUpdate(transferId: String): Boolean?


    /**
     * Send account login data to Inspetor
     *
     * @param accountEmail
     * @param accountId
     * @return void
     */
    fun trackLogin(accountEmail: String, accountId: String?): Boolean?

    /**
     * Send account logout data to Inspetor
     *
     * @param accountEmail
     * @param accountId
     * @return void
     */
    fun trackLogout(accountEmail: String, accountId: String?): Boolean?

    /**
     * Send password reset data to Inspetor
     *
     * @param accountEmail
     * @return void
     */
    fun trackPasswordReset(accountEmail: String): Boolean?

    /**
     * Send password recovery data to Inspetor
     *
     * @param accountEmail
     * @return void
     */
    fun trackPasswordRecovery(accountEmail: String): Boolean?

    /**
     * Send sale creation data to Inspetor
     *
     * @param saleId
     * @return void
     */
    fun trackSaleCreation(saleId: String): Boolean?

    /**
     * Send sale update data to Inspetor
     *
     * @param saleId
     * @return void
     */
    fun trackSaleUpdate(saleId: String): Boolean?

    /**
     * Send screen data to Inspetor
     *
     * @param pageTitle
     * @return void
     */
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
    fun setup(authToken: String, inspetorEnv: Boolean = false)

    /**
     * Verify if config exists
     *
     * @return boolean
     */
    fun isConfigured(): Boolean
}