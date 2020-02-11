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
     * Send user data to Inspetor
     *
     * @param account_id;
     * @param action
     * @return void
     */
    fun trackUserAction(data: HashMap<String, String?>, action: UserAction): Boolean

    /**
     * Send auth data to Inspetor
     *
     * @param account_email;
     * @param action
     * @return void
     */
    fun trackUserAuthAction(data: HashMap<String, String?>,  action: AuthAction): Boolean

    /**
     * Send pass recovery data to Inspetor
     *
     * @param action
     * @return void
     */
    fun trackPasswordRecoveryAction(data: HashMap<String, String?>, action: PassRecoveryAction): Boolean


    /**
     * Send Order data to Inspetor
     *
     * @param sale_id;
     * @param action
     * @return void
     */
    fun trackOrderAction(data: HashMap<String, String?>, action: OrderAction): Boolean

    /**
     * Send page data to Inspetor
     *
     * @param page_title;
     * @return void
     */
    fun trackPageView(page_title: String): Boolean

}