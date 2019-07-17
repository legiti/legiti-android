//
//  InspetorConfig.ktnspetor-android-sdk
//
//  Created by Matheus Sato on 12/4/19.
//  Copyright © 2019 Inspetor. All rights reserved.
//
package com.inspetor

data class InspetorConfig(
    var trackerName: String,
    var appId: String,
    var devEnv: Boolean = InspetorDependencies.DEFAULT_DEV_MODE
)