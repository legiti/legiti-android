package com.legiti

import android.content.Context
import android.os.Build
import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.mockito.*
import com.legiti.helpers.InvalidCredentials
import org.robolectric.annotation.Config
import java.util.*


@RunWith(RobolectricTestRunner::class)
@Config(sdk = intArrayOf(Build.VERSION_CODES.O_MR1))  // So it runs using java 9
class UnitTests {

    private val AUTH_TOKEN = "internal_sandbox_eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJwcmluY2lwYWxJZCI6Imluc3BldG9yX3Rlc3QifQ.cJimBzTsFCC5LMurLelIax_-0ejXYEOZdYIL7Q3GEEQ"

    @Test(expected = InvalidCredentials::class)
    fun testSetContextWithoutConfig() {
        Legiti.sharedInstance().setContext(
            context = Mockito.mock(Context::class.java)
        )
    }

    @Test(expected = InvalidCredentials::class)
    fun testSetupWithoutAuthToken() {
        LegitiConfig("")
    }

    @Test(expected = InvalidCredentials::class)
    fun testSetupWithInvalidAuthToken() {
        LegitiConfig("123")
    }

    @Test(expected = InvalidCredentials::class)
    fun testSetupWithAuthTokenMissingPart() {
        val invalidAuthToken = AUTH_TOKEN.split(".").subList(0, 1).joinToString(".")
        LegitiConfig(invalidAuthToken)
    }

    @Test(expected = InvalidCredentials::class)
    fun testSetupWithTokenMissingPrincipalId() {
        val splittedToken = AUTH_TOKEN.split(".")
        val middlePart = "{\"missing_principal_id\": \"not_principal_id\"}".toByteArray()
        val encodedMiddlePart = Base64.getEncoder().encodeToString(middlePart)
        val invalidAuthToken = arrayOf(splittedToken[0], encodedMiddlePart, splittedToken[2]).joinToString(".")

        LegitiConfig(invalidAuthToken)
    }

    @Test
    fun testSetupWithAuthTokenInUpperCase() {
        val splittedToken = AUTH_TOKEN.split(".")
        val middlePart = ("{\"principalId\": \"inspetor_test_SANDBOX\"}").toByteArray()
        val encodedMiddlePart = Base64.getEncoder().encodeToString(middlePart)
        val authToken = arrayOf(splittedToken[0], encodedMiddlePart, splittedToken[2]).joinToString(".")

        assertTrue(LegitiConfig.isValid(authToken))
    }

    @Test
    fun testSetupWithInternalAuthToken() {
        val config = LegitiConfig(AUTH_TOKEN)

        assertEquals(AUTH_TOKEN.removePrefix("internal_"), config.authToken)
        assertTrue(config.legitiDevEnv)
    }

    @Test
    fun testSetupWithoutInternalAuthToken() {
        val config = LegitiConfig(AUTH_TOKEN.removePrefix("internal_"))

        assertEquals(AUTH_TOKEN.removePrefix("internal_"), config.authToken)
        assertFalse(config.legitiDevEnv)
    }

    @Test(expected = InvalidCredentials::class)
    fun testUserCreationWithoutConfig() {
        LegitiClient().trackUserCreation("123")
    }

    @Test(expected = InvalidCredentials::class)
    fun testUserUpdateWithoutConfig() {
        LegitiClient().trackUserUpdate("123")
    }

    @Test(expected = InvalidCredentials::class)
    fun testAuthLoginWithoutConfig() {
        LegitiClient().trackLogin("123", "123")
    }

    @Test(expected = InvalidCredentials::class)
    fun testAuthLogoutWithoutConfig() {
        LegitiClient().trackLogout("123", null)
    }

    @Test(expected = InvalidCredentials::class)
    fun testPassRecoveryWithoutConfig() {
        LegitiClient().trackPasswordRecovery("email@email.com")
    }

    @Test(expected = InvalidCredentials::class)
    fun testOrderCreationWithoutConfig() {
        LegitiClient().trackOrderCreation("123")
    }
}