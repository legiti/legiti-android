//
//  InspetorResourceService.kt
//  inspetor-android-sdk
//
//  Created by Matheus Sato on 12/4/19.
//  Copyright © 2019 Inspetor. All rights reserved.
//
package com.inspetor.services

import com.inspetor.helpers.*


interface InspetorResourceService {

    /**
     * Send account data to Inspetor
     *
     * @param account_id;
     * @param action
     * @return void
     */
    fun trackAccountAction(data: HashMap<String, String?>, action: AccountAction): Boolean

    /**
     * Send auth data to Inspetor
     *
     * @param account_email;
     * @param action
     * @return void
     */
    fun trackAccountAuthAction(data: HashMap<String, String?>,  action: AuthAction): Boolean

    /**
     * Send event data to Inspetor
     *
     * @param action
     * @return void
     */
    fun trackEventAction(data: HashMap<String, String?>, action: EventAction): Boolean


    /**
     * Send pass recovery data to Inspetor
     *
     * @param action
     * @return void
     */
    fun trackPasswordRecoveryAction(data: HashMap<String, String?>, action: PassRecoveryAction): Boolean

    /**
     * Send item transfer data to Inspetor
     *
     * @param transfer_id;
     * @param action
     * @return void
     */
    fun trackItemTransferAction(data: HashMap<String, String?>, action: TransferAction): Boolean

    /**
     * Send Sale data to Inspetor
     *
     * @param sale_id;
     * @param action
     * @return void
     */
    fun trackSaleAction(data: HashMap<String, String?>, action: SaleAction): Boolean

    /**
     * Send page data to Inspetor
     *
     * @param page_title;
     * @return void
     */
    fun trackPageView(page_title: String): Boolean

}