//
//  InspetorResourceService.kt
//  inspetor-android-sdk
//
//  Created by Matheus Sato on 12/4/19.
//  Copyright © 2019 Inspetor. All rights reserved.
//
package com.inspetor


interface InspetorResourceService {

    /**
     * Send account data to Inspetor
     *
     * @param account_id;
     * @param action
     * @return void
     */
    fun trackAccountAction(account_id: String, action: AccountAction): Boolean

    /**
     * Send auth data to Inspetor
     *
     * @param account_email;
     * @param action
     * @return void
     */
    fun trackAccountAuthAction(account_email: String, account_id: String, action: AuthAction): Boolean

    /**
     * Send event data to Inspetor
     *
     * @param action
     * @return void
     */
    fun trackEventAction(event_id: String, action: EventAction): Boolean


    /**
     * Send pass recovery data to Inspetor
     *
     * @param action
     * @return void
     */
    fun trackPasswordRecoveryAction(accountEmail: String, action: PassRecoveryAction): Boolean

    /**
     * Send item transfer data to Inspetor
     *
     * @param transfer_id;
     * @param action
     * @return void
     */
    fun trackItemTransferAction(transfer_id: String, action: TransferAction): Boolean

    /**
     * Send Sale data to Inspetor
     *
     * @param sale_id;
     * @param action
     * @return void
     */
    fun trackSaleAction(sale_id: String, action: SaleAction): Boolean

    /**
     * Send page data to Inspetor
     *
     * @param page_title;
     * @return void
     */
    fun trackPageView(page_title: String): Boolean

}