package com.huawei.dtse.locationkitv5

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.huawei.hms.location.*


//::created by c7j at 30.03.2020 01:19
class LocationBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        log("onReceive() hms location broadcast")
        val deliverIntent = Intent(ACTION_DELIVER_LOCATION)
        val action = intent.action
        if (action == ACTION_PROCESS_LOCATION) {
            val activityConversionResult = ActivityConversionResponse.getDataFromIntent(intent)
            if (activityConversionResult != null) {
                val list =
                    activityConversionResult.activityConversionDatas as ArrayList<ActivityConversionData>
                deliverIntent.putParcelableArrayListExtra(EXTRA_HMS_LOCATION_CONVERSION, list)
            }

            val activityRecognitionResult = ActivityIdentificationResponse.getDataFromIntent(intent)
            if (activityRecognitionResult != null && MainActivity.isListenActivityIdentification) {
                val list =
                    activityRecognitionResult.activityIdentificationDatas as ArrayList<ActivityIdentificationData>
                deliverIntent.putParcelableArrayListExtra(EXTRA_HMS_LOCATION_RECOGNITION, list)
            }
        }
        context.sendBroadcast(deliverIntent)
    }


    companion object {
        const val ACTION_PROCESS_LOCATION = "com.huawei.hms.location.ACTION_PROCESS_LOCATION"
        const val ACTION_DELIVER_LOCATION = "ACTION_DELIVER_LOCATION"

        const val EXTRA_HMS_LOCATION_RECOGNITION = "EXTRA_HMS_LOCATION_RECOGNITION"
        const val EXTRA_HMS_LOCATION_CONVERSION = "EXTRA_HMS_LOCATION_CONVERSION"

        const val REQUEST_PERIOD = 5000L


        fun statusFromCode(code: Int): String? = when (code) {
            ActivityIdentificationData.VEHICLE -> "VEHICLE"
            ActivityIdentificationData.BIKE -> "BIKE"
            ActivityIdentificationData.FOOT -> "FOOT"
            ActivityIdentificationData.STILL -> "STILL"
            ActivityIdentificationData.OTHERS -> "OTHERS"
            ActivityIdentificationData.WALKING -> "WALKING"
            ActivityIdentificationData.RUNNING -> "RUNNING"
            ActivityConversionInfo.EXIT_ACTIVITY_CONVERSION -> "OUT FROM STILL ACTIVITY"
            ActivityConversionInfo.ENTER_ACTIVITY_CONVERSION -> "IN STILL ACTIVITY"
            else -> "UNDEFINED"
        }
    }
}