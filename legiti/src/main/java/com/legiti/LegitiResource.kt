package com.legiti

import android.content.Context
import com.snowplowanalytics.snowplow.controller.TrackerController
import com.snowplowanalytics.snowplow.event.SelfDescribing
import com.snowplowanalytics.snowplow.event.ScreenView
import com.snowplowanalytics.snowplow.payload.SelfDescribingJson
import java.util.*
import com.legiti.helpers.*
import com.legiti.services.LegitiResourceService
import kotlin.collections.HashMap

internal class LegitiResource(config: LegitiConfig, androidContext: Context, legitiFingerprint: LegitiFingerprint):
    LegitiResourceService {

    private var spTracker: TrackerController
    private var androidContext: Context
    private var legitiDeviceData: HashMap<String, Any?>
    private var legitiFingerprint: LegitiFingerprint

    var currentUserId: String? = null

    init {
        SnowplowManager.init(config)
        this.androidContext = androidContext
        this.legitiFingerprint = legitiFingerprint
        this.spTracker = SnowplowManager.setupTracker(this.androidContext) ?: throw fail("Legiti Exception 9000: Internal error.")
        this.legitiDeviceData = LegitiDeviceData(androidContext).getDeviceData()
    }

    override fun trackUserAction(data: HashMap<String, String?>, action: UserAction): Boolean {
        this.trackUnstructuredEvent(
            LegitiDependencies.FRONTEND_USER_SCHEMA_VERSION,
            data,
            action.rawValue()
        )

        return true
    }

    override fun trackUserAuthAction(data: HashMap<String, String?>, action: AuthAction): Boolean {
        this.trackUnstructuredEvent(
            LegitiDependencies.FRONTEND_AUTH_SCHEMA_VERSION,
            data,
            action.rawValue()
        )

        return true
    }

    override fun trackPasswordRecoveryAction(data: HashMap<String, String?>, action: PasswordAction): Boolean {
        this.trackUnstructuredEvent(
            LegitiDependencies.FRONTEND_PASS_RECOVERY_SCHEMA_VERSION,
            data,
            action.rawValue()
        )

        return true
    }

    override fun trackPasswordResetAction(data: HashMap<String, String?>, action: PasswordAction): Boolean {
        this.trackUnstructuredEvent(
            LegitiDependencies.FRONTEND_PASS_RESET_SCHEMA_VERSION,
            data,
            action.rawValue()
        )

        return true
    }

    override fun trackOrderAction(data: HashMap<String, String?>, action: OrderAction): Boolean {
        this.trackUnstructuredEvent(
            LegitiDependencies.FRONTEND_ORDER_SCHEMA_VERSION,
            data,
            action.rawValue()
        )

        return true
    }

    override fun trackPageView(page_title: String): Boolean {
        val spContexts: ArrayList<SelfDescribingJson> = arrayListOf()
        this.getFingerprintContext().let{ spContexts.add(it) }

        this.spTracker.track(
            ScreenView.builder()
                .name(page_title)
                .id(UUID.randomUUID().toString())
                .contexts(spContexts)
                .build() ?: throw fail("Legiti Exception 9000: Internal error.")
        )

        return true
    }

    private fun trackUnstructuredEvent(schema: String, data: HashMap<String, String?>, action: String) {
        val legitiData = SelfDescribingJson(schema, data)

        val spContexts: ArrayList<SelfDescribingJson> = arrayListOf(
            this.setupActionContext(action)
        )


        this.getFingerprintContext().let{ spContexts.add(it) }

        this.spTracker.track(
            SelfDescribing.builder()
                .eventData(legitiData)
                .contexts(spContexts)
                .build() ?: throw fail("Legiti Exception 9000: Internal error.")
        )
    }

    private fun setupActionContext(action: String): SelfDescribingJson {
        val contextMap: HashMap<String, String> = hashMapOf(
            "action" to action
        )

        return SelfDescribingJson(
            LegitiDependencies.FRONTEND_CONTEXT_SCHEMA_VERSION,
            contextMap
        )

    }

    private fun fail(message: String): Throwable {
        throw Exception(message)
    }

    private fun getFingerprintContext(): SelfDescribingJson {
        val deviceData = this.legitiDeviceData
        deviceData["logged_user_id"] = this.currentUserId
        deviceData["device_fingerprint"] = this.legitiFingerprint.deviceFingerprint

        return SelfDescribingJson(
            LegitiDependencies.FRONTEND_FINGERPRINT_SCHEMA_VERSION,
            deviceData
        )
    }


}
