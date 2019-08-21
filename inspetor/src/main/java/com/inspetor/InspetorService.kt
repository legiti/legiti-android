//
//  InspetorService.kt
//  inspetor-android-sdk
//
//  Created by Matheus Sato on 12/4/19.
//  Copyright © 2019 Inspetor. All rights reserved.
//
package com.inspetor

import android.app.Activity
import android.content.Context

interface InspetorService {

    /**
     * Send account creation data to Inspetor
     *
     * @param account_id
     * @return void
     */
    fun trackAccountCreation(account_id: String): Boolean?

    /**
     * Send account update data to Inspetor
     *
     * @param account_id
     * @return void
     */
    fun trackAccountUpdate(account_id: String): Boolean?

    /**
     * Send account deletion data to Inspetor
     *
     * @param account_id
     * @return void
     */
    fun trackAccountDeletion(account_id: String): Boolean?

    /**
     * Send event creation data to Inspetor
     *
     * @param event_id
     * @return void
     */
    fun trackEventCreation(event_id: String): Boolean?

    /**
     * Send event update data to Inspetor
     *
     * @param event_id
     * @return void
     */
    fun trackEventUpdate(event_id: String): Boolean?

    /**
     * Send event deletion data to Inspetor
     *
     * @param event_id
     * @return void
     */
    fun trackEventDeletion(event_id: String): Boolean?

    /**
     * Send item transfer creation data to Inspetor
     *
     * @param transfer_id
     * @return void
     */
    fun trackItemTransferCreation(transfer_id: String): Boolean?

    /**
     * Send item transfer update data to Inspetor
     *
     * @param transfer_id
     * @return void
     */
    fun trackItemTransferUpdate(transfer_id: String): Boolean?


    /**
     * Send account login data to Inspetor
     *
     * @param account_email
     * @param account_id
     * @return void
     */
    fun trackLogin(account_email: String, account_id: String): Boolean?

    /**
     * Send account logout data to Inspetor
     *
     * @param account_email
     * @param account_id
     * @return void
     */
    fun trackLogout(account_email: String, account_id: String): Boolean?

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
     * @param sale_id
     * @return void
     */
    fun trackSaleCreation(sale_id: String): Boolean?

    /**
     * Send sale update data to Inspetor
     *
     * @param sale_id
     * @return void
     */
    fun trackSaleUpdate(sale_id: String): Boolean?

    /**
     * Send screen data to Inspetor
     *
     * @param screen_name
     * @return void
     */
    fun trackScreenView(screen_name: String): Boolean?

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
    fun setup(trackerName: String, appId: String, devEnv: Boolean?, inspetorEnv: Boolean?)

    /**
     * Verify if config exists
     *
     * @return boolean
     */
    fun isConfigured(): Boolean
}