package com.inspetor

import org.junit.Test
import org.junit.Assert.*

class InspetorAndroidTests {

    private val UNIT_TEST_DEFAULT_TRACKER_NAME = "inspetor.test"
    private val UNIT_TEST_DEFAULT_APP_ID = "0123456789"

    private var validDataSource = InspetorConfig(
        appId = UNIT_TEST_DEFAULT_APP_ID,
        trackerName = UNIT_TEST_DEFAULT_TRACKER_NAME
    )

    private var invalidDataSource = InspetorConfig(
        appId = UNIT_TEST_DEFAULT_APP_ID,
        trackerName = "ingresse"
    )

    @Test
    fun testCorrectSetup() {
        val inspetorClient = InspetorClient()
        var invalid = false
        try{
            inspetorClient.setup(validDataSource)
        } catch (e: Throwable) {
            invalid = true
        }

        assertFalse(invalid)
    }

    @Test
    fun testSupportsOptionalParams() {
        val inspetorClient = InspetorClient()
        val nonDefaultUri = "random.uri.com"
        var invalid = false
        validDataSource.apply { collectorUri = nonDefaultUri }
        try {
            inspetorClient.setup(validDataSource)
        } catch (e: Throwable) {
            invalid = true
        }

        assertFalse(invalid)
    }

    @Test
    fun testTrackerNameFormatValidation() {
        val invalidTrackerNameTooManyFields = "improper.tracker.name.format"
        invalidDataSource.apply { trackerName = invalidTrackerNameTooManyFields}
        assertFalse(validateTrackerName(invalidTrackerNameTooManyFields))

        val invalidTrackerNameTooFewFields = "improper_tracker_name_format"
        invalidDataSource.apply { trackerName = invalidTrackerNameTooFewFields}
        assertFalse(validateTrackerName(invalidTrackerNameTooFewFields))

        val invalidTrackerNameNoClientName = ".improper_tracker_name_format"
        invalidDataSource.apply { trackerName = invalidTrackerNameNoClientName}
        assertFalse(validateTrackerName(invalidTrackerNameNoClientName))

        val invalidTrackerNameNoProductName = "improper_tracker_name_format."
        invalidDataSource.apply { trackerName = invalidTrackerNameNoProductName}
        assertFalse(validateTrackerName(invalidTrackerNameNoProductName))

        val validTrackerName = "valid.tracker_name"
        invalidDataSource.apply { trackerName = validTrackerName}
        assertTrue(validateTrackerName(validTrackerName))
    }

    @Test(expected = IllegalArgumentException::class)
    fun testIncorrectSetup() {
        val inspetorClient = InspetorClient()
        inspetorClient.setup(invalidDataSource)
    }


    @Test(expected = IllegalArgumentException::class)
    fun testAccountCreationWithoutConfig() {
        val inspetorClient = InspetorClient()
        inspetorClient.trackAccountCreation("123")
    }

    @Test(expected = IllegalArgumentException::class)
    fun testAccountUpdateWithoutConfig() {
        val inspetorClient = InspetorClient()
        inspetorClient.trackAccountCreation("123")
    }

    @Test(expected = IllegalArgumentException::class)
    fun testAccountDeletionWithoutConfig() {
        val inspetorClient = InspetorClient()
        inspetorClient.trackAccountCreation("123")
    }

    @Test(expected = IllegalArgumentException::class)
    fun testAuthLoginWithoutConfig() {
        val inspetorClient = InspetorClient()
        inspetorClient.trackLogin("123")
    }

    @Test(expected = IllegalArgumentException::class)
    fun testAuthLogoutWithoutConfig() {
        val inspetorClient = InspetorClient()
        inspetorClient.trackLogout("123")
    }

    @Test(expected = IllegalArgumentException::class)
    fun testEventCreationWithoutConfig() {
        val inspetorClient = InspetorClient()
        inspetorClient.trackEventCreation("123")
    }

    @Test(expected = IllegalArgumentException::class)
    fun testEventUpdateWithoutConfig() {
        val inspetorClient = InspetorClient()
        inspetorClient.trackEventCreation("123")
    }

    @Test(expected = IllegalArgumentException::class)
    fun testEventDeletionWithoutConfig() {
        val inspetorClient = InspetorClient()
        inspetorClient.trackEventCreation("123")
    }

    @Test(expected = IllegalArgumentException::class)
    fun testTransferCreationWithoutConfig() {
        val inspetorClient = InspetorClient()
        inspetorClient.trackItemTransferCreation("123")
    }

    @Test(expected = IllegalArgumentException::class)
    fun testTransferUpdateWithoutConfig() {
        val inspetorClient = InspetorClient()
        inspetorClient.trackItemTransferUpdate("123")
    }

    @Test(expected = IllegalArgumentException::class)
    fun testPassRecoveryWithoutConfig() {
        val inspetorClient = InspetorClient()
        inspetorClient.trackPasswordRecovery("email@email.com")
    }

    @Test(expected = IllegalArgumentException::class)
    fun testPassResetWithoutConfig() {
        val inspetorClient = InspetorClient()
        inspetorClient.trackPasswordReset("email@email.com")
    }

    @Test(expected = IllegalArgumentException::class)
    fun testSaleCreationWithoutConfig() {
        val inspetorClient = InspetorClient()
        inspetorClient.trackSaleCreation("123")
    }

    @Test(expected = IllegalArgumentException::class)
    fun testSaleUpdateWithoutConfig() {
        val inspetorClient = InspetorClient()
        inspetorClient.trackSaleUpdate("123")
    }
    private fun validateTrackerName(trackerName: String): Boolean {
        val trackerNameArray = trackerName.split(InspetorDependencies.DEFAULT_INSPETOR_TRACKER_NAME_SEPARATOR)
        return (trackerNameArray.count() == 2 &&
                trackerNameArray[0].count() > 1 &&
                trackerNameArray[1].count() > 1)
    }

}