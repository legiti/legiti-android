//
//  InspetorConfig.ktnspetor-android-sdk
//
//  Created by Matheus Sato on 12/4/19.
//  Copyright © 2019 Inspetor. All rights reserved.
//
package com.inspetor

import org.json.JSONObject
import org.json.JSONException
import android.util.Base64
import com.inspetor.helpers.InvalidCredentials

class InspetorConfig(val authToken: String, val inspetorEnv: Boolean) {
    var devEnv: Boolean = true

    init {
        require(isValid(authToken)) { throw InvalidCredentials("Inspetor Exception 9002: authToken not valid") }
        val splittedToken = this.authToken.split(".")
        val principalId = getPrincipalId(splittedToken[1])
        this.devEnv = principalId!!.contains("sandbox", ignoreCase = true)
    }


    // This is how you do static methods in kotlin
    companion object {

        fun isValid(authToken: String): Boolean {
            if (authToken.isEmpty() or authToken.isBlank()) {
                return false
            }

            val splittedToken = authToken.split(".")
            if (splittedToken.size != 3) {
                return false
            }

            if (getPrincipalId(splittedToken[1]) == null) {
                return false
            }

            return true
        }

        fun getPrincipalId(authTokenPart: String): String? {
            val decodedAuthToken = String(Base64.decode(authTokenPart, Base64.NO_PADDING))
            return try {
                JSONObject(decodedAuthToken).getString("principalId")
            } catch (e: JSONException) {
                null
            }

        }
    }


}