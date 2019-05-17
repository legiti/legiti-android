package com.inspetor

import com.snowplowanalytics.snowplow.tracker.emitter.BufferOption
import com.snowplowanalytics.snowplow.tracker.emitter.HttpMethod
import com.snowplowanalytics.snowplow.tracker.emitter.RequestSecurity
import org.junit.Test
import org.junit.Assert.*

class InspetorAndroidTests {

    private val UNIT_TEST_DEFAULT_TRACKER_NAME = "inspetor.test"
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
    fun testIncorrectVerifySetup() {
        val inspetorTestVerifySetup = InspetorResource()
        var invalid = false
        try {
            inspetorTestVerifySetup.setup(invalidDataSource)
        } catch (e: Throwable) {
            invalid = true
        }

        assertTrue(invalid)
    }

    @Test
    fun testCorrectVerifySetup() {
        val inspetorTestVerifySetup = InspetorResource()
        var invalid = false
        try{
            inspetorTestVerifySetup.setup(validDataSource)
        } catch (e: Throwable) {
            invalid = true
        }

        assertFalse(invalid)
    }

    @Test
    fun testSupportsOptionalParams() {
        val inspetorTestOptionalParams = InspetorResource()
        val nonDefaultUri = "random.uri.com"
        var invalid = false

        try {
            validDataSource.apply { collectorUri = nonDefaultUri }
            inspetorTestOptionalParams.setup(validDataSource)
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

    private fun validateTrackerName(trackerName: String): Boolean {
        val trackerNameArray = trackerName.split(InspetorConfig.DEFAULT_INSPETOR_TRACKER_NAME_SEPARATOR)
        return (trackerNameArray.count() == 2 &&
                trackerNameArray[0].count() > 1 &&
                trackerNameArray[1].count() > 1)
    }

    private fun switchBufferOptionSize (bufferOptionSize: BufferOptionSize): BufferOption {
        return when(bufferOptionSize) {
            BufferOptionSize.SINGLE -> BufferOption.Single
            BufferOptionSize.HEAVY -> BufferOption.HeavyGroup
            else -> BufferOption.DefaultGroup
        }
    }

    private fun switchSecurityProtocol (protocolType: RequestSecurityProtocol): RequestSecurity {
        return when (protocolType) {
            RequestSecurityProtocol.HTTP -> RequestSecurity.HTTP
            else -> RequestSecurity.HTTPS
        }
    }

    private fun switchHttpMethod (httpMethodType: HttpMethodType): HttpMethod {
        return when (httpMethodType) {
            HttpMethodType.GET -> HttpMethod.GET
            else -> HttpMethod.POST
        }
    }
}