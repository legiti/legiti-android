package com.legiti

import org.json.JSONObject
import org.json.JSONException
import android.util.Base64
import com.legiti.helpers.InvalidCredentials


class LegitiConfig(var authToken: String) {

    internal var legitiDevEnv: Boolean = false
    private val INTERNAL_KEYWORD: String = "internal_"

    init {
        require(isValid(authToken)) {
            throw InvalidCredentials("Legiti Exception 9002: authToken not valid")
        }
        this.authToken = checkIfInternal(this.authToken)
    }

    private fun checkIfInternal(authToken: String): String {
        if (authToken.contains(this.INTERNAL_KEYWORD)) this.legitiDevEnv = true
        return authToken.removePrefix(this.INTERNAL_KEYWORD)
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