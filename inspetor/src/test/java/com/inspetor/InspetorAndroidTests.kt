package com.inspetor

import android.content.Context
import kotlin.test.*
import org.junit.Test
import org.junit.Assert.*
import java.lang.Exception
import org.mockito.*

class InspetorAndroidTests {

    private val UNIT_TEST_DEFAULT_TRACKER_NAME = "inspetor.test"
    private val UNIT_TEST_DEFAULT_APP_ID = "0123456789"


    @Test(expected = java.lang.IllegalArgumentException::class)
    fun testSetContextWithoutConfig() {
        Inspetor.sharedInstance().setContext(
            context = Mockito.mock(Context::class.java)
        )
    }
    
    @Test(expected = Exception::class)
    fun testInvalidTrackerNameTooManyFields() {
        val invalidTrackerNameTooManyFields = "improper.tracker.name.format"

        Inspetor.sharedInstance().setup(
            appId = UNIT_TEST_DEFAULT_APP_ID,
            trackerName = invalidTrackerNameTooManyFields,
            devEnv = true
        )
    }

    @Test(expected = Exception::class)
    fun invalidTrackerNameTooFewFields() {
        val invalidTrackerNameTooFewFields = "improper_tracker_name_format"

        Inspetor.sharedInstance().setup(
            appId = UNIT_TEST_DEFAULT_APP_ID,
            trackerName = invalidTrackerNameTooFewFields,
            devEnv = true
        )
    }

    @Test(expected = Exception::class)
    fun invalidTrackerNameNoClientName() {
        val invalidTrackerNameNoClientName = ".improper_tracker_name_format"

        Inspetor.sharedInstance().setup(
            trackerName = invalidTrackerNameNoClientName,
            appId = UNIT_TEST_DEFAULT_APP_ID,
            devEnv = true
        )
    }

    @Test(expected = Exception::class)
    fun invalidTrackerNameNoProductName() {
        val invalidTrackerNameNoProductName = "improper_tracker_name_format."

        Inspetor.sharedInstance().setup(
            appId = UNIT_TEST_DEFAULT_APP_ID,
            trackerName = invalidTrackerNameNoProductName,
            devEnv = true
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun testIncorrectSetup() {

        Inspetor.sharedInstance().setup(
            appId = UNIT_TEST_DEFAULT_APP_ID,
            trackerName = "ingresse",
            devEnv = true
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun testAccountCreationWithoutConfig() {
        InspetorClient().trackAccountCreation("123")
    }

    @Test(expected = IllegalArgumentException::class)
    fun testAccountUpdateWithoutConfig() {
        InspetorClient().trackAccountCreation("123")
    }

    @Test(expected = IllegalArgumentException::class)
    fun testAccountDeletionWithoutConfig() {
        InspetorClient().trackAccountCreation("123")
    }

    @Test(expected = IllegalArgumentException::class)
    fun testAuthLoginWithoutConfig() {
        InspetorClient().trackLogin("123", "123")
    }

    @Test(expected = IllegalArgumentException::class)
    fun testAuthLogoutWithoutConfig() {
        InspetorClient().trackLogout("123", null)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testEventCreationWithoutConfig() {
        InspetorClient().trackEventCreation("123")
    }

    @Test(expected = IllegalArgumentException::class)
    fun testEventUpdateWithoutConfig() {
        InspetorClient().trackEventCreation("123")
    }

    @Test(expected = IllegalArgumentException::class)
    fun testEventDeletionWithoutConfig() {
        InspetorClient().trackEventCreation("123")
    }

    @Test(expected = IllegalArgumentException::class)
    fun testTransferCreationWithoutConfig() {
        InspetorClient().trackItemTransferCreation("123")
    }

    @Test(expected = IllegalArgumentException::class)
    fun testTransferUpdateWithoutConfig() {
        InspetorClient().trackItemTransferUpdate("123")
    }

    @Test(expected = IllegalArgumentException::class)
    fun testPassRecoveryWithoutConfig() {
        InspetorClient().trackPasswordRecovery("email@email.com")
    }

    @Test(expected = IllegalArgumentException::class)
    fun testPassResetWithoutConfig() {
        InspetorClient().trackPasswordReset("email@email.com")
    }

    @Test(expected = IllegalArgumentException::class)
    fun testSaleCreationWithoutConfig() {
        InspetorClient().trackSaleCreation("123")
    }

    @Test(expected = IllegalArgumentException::class)
    fun testSaleUpdateWithoutConfig() {
        InspetorClient().trackSaleUpdate("123")
    }

}