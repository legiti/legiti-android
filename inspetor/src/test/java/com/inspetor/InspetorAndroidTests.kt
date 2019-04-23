package com.inspetor

import android.content.Context
import org.junit.Test
import org.junit.Assert.*
import org.mockito.Mockito

class InspetorAndroidTests {

    private val UNIT_TEST_DEFAULT_TRACKER_NAME = "inspetor.test-tracker"
    private val UNIT_TEST_DEFAULT_APP_ID = "0123456789"

    private var context = Mockito.mock(Context::class.java)

    var validDataSource = InspetorDependencies(
        appId = "123",
        trackerName = "ingresse.test"
    )

    var invalidDataSource = InspetorDependencies(
        appId = "",
        trackerName = "ingresse.test"
    )

    @Test
    fun testVerifySetup() {
        val inspetorTestVerifySetup = InspetorResource(context)

        inspetorTestVerifySetup.setup(validDataSource)
        assertTrue(inspetorTestVerifySetup.verifySetup())
    }

    @Test
    fun testSupportsOptionalParams() {
        val inspetorTestOptionalParams = InspetorResource(context)
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
        var inspetor = InspetorResource(context)

        val invalidTrackerNameTooManyFields = "improper.tracker.name.format"
        assertFalse(inspetor.validateTrackerName(invalidTrackerNameTooManyFields))

        val invalidTrackerNameTooFewFields = "improper_tracker_name_format"
        assertFalse(inspetor.validateTrackerName(invalidTrackerNameTooFewFields))

        val invalidTrackerNameNoClientName = ".improper_tracker_name_format"
        assertFalse(inspetor.validateTrackerName(invalidTrackerNameNoClientName))

        val invalidTrackerNameNoProductName = "improper_tracker_name_format."
        assertFalse(inspetor.validateTrackerName(invalidTrackerNameNoProductName))

        val validTrackerName = "valid.tracker_name"
        assertTrue(inspetor.validateTrackerName(validTrackerName))
    }

}