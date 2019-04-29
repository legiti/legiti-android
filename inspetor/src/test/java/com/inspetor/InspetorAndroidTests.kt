package com.inspetor

import android.content.Context
import org.junit.Test
import org.junit.Assert.*
import org.mockito.Mockito

class InspetorAndroidTests {

    private val UNIT_TEST_DEFAULT_TRACKER_NAME = "inspetor.test-tracker"
    private val UNIT_TEST_DEFAULT_APP_ID = "0123456789"

    private var validDataSource = InspetorDependencies(
        appId = UNIT_TEST_DEFAULT_APP_ID,
        trackerName = UNIT_TEST_DEFAULT_TRACKER_NAME
    )

    private var invalidDataSource = InspetorDependencies(
        appId = UNIT_TEST_DEFAULT_APP_ID,
        trackerName = "ingresse"
    )

    @Test
    fun testVerifySetup() {
        val inspetorTestVerifySetup = InspetorResource()
        inspetorTestVerifySetup.setup(validDataSource)
        assertTrue(inspetorTestVerifySetup.appId != "" && inspetorTestVerifySetup.trackerName != "")
    }

    @Test
    fun testSupportsOptionalParams() {
        val inspetorTestOptionalParams = InspetorResource()
        val nonDefaultUri = "random.uri.com"

        validDataSource.apply { collectorUri = nonDefaultUri }

        inspetorTestOptionalParams.setup(validDataSource)

        // Provided values are altered
        assertNotEquals(inspetorTestOptionalParams.collectorUri, InspetorConfig.DEFAULT_COLLECTOR_URI)
        assertEquals(inspetorTestOptionalParams.collectorUri, nonDefaultUri)

        // Nil values remain default
        assertEquals(inspetorTestOptionalParams.base64Encoded, InspetorConfig.DEFAULT_BASE64_OPTION)
        assertEquals(inspetorTestOptionalParams.httpMethodType, InspetorConfig.DEFAULT_HTTP_METHOD_TYPE)
        assertEquals(inspetorTestOptionalParams.protocolType, InspetorConfig.DEFAULT_PROTOCOL_TYPE)
        assertEquals(inspetorTestOptionalParams.bufferOption, InspetorConfig.DEFAULT_BUFFERSIZE_OPTION)
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

    private fun validateTrackerName(trackerName: String): Boolean {
        val trackerNameArray = trackerName.split(InspetorConfig.DEFAULT_INSPETOR_TRACKER_NAME_SEPARATOR)
        return (trackerNameArray.count() == 2 &&
                trackerNameArray[0].count() > 1 &&
                trackerNameArray[1].count() > 1)
    }
}