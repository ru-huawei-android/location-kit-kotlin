package com.huawei.dtse.locationkitv5

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.Location
import com.huawei.dtse.locationkitv5.utils.log
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

            if (LocationResult.hasResult(intent)) {
                val result = LocationResult.extractResult(intent)
                if (result != null) {
                    val list = result.locations as ArrayList<Location>
                    deliverIntent.putParcelableArrayListExtra(EXTRA_HMS_LOCATION_RESULT, list)
                }
            }

            if (LocationAvailability.hasLocationAvailability(intent)) {
                val locationAvailability = LocationAvailability.extractLocationAvailability(intent)
                deliverIntent.putExtra(EXTRA_HMS_LOCATION_AVAILABILITY, locationAvailability?.isLocationAvailable == true)
            }
        }
        context.sendBroadcast(deliverIntent)
    }


    companion object {
        const val ACTION_PROCESS_LOCATION = "com.huawei.hms.location.ACTION_PROCESS_LOCATION"
        const val ACTION_DELIVER_LOCATION = "ACTION_DELIVER_LOCATION"
        const val EXTRA_HMS_LOCATION_RECOGNITION = "EXTRA_HMS_LOCATION_RECOGNITION"
        const val EXTRA_HMS_LOCATION_CONVERSION = "EXTRA_HMS_LOCATION_CONVERSION"
        const val EXTRA_HMS_LOCATION_RESULT = "EXTRA_HMS_LOCATION_RESULT"
        const val EXTRA_HMS_LOCATION_AVAILABILITY = "EXTRA_HMS_LOCATION_AVAILABILITY"
        const val REQUEST_PERIOD = 5000L
        private const val VEHICLE = 100
        private const val BIKE = 101
        private const val FOOT = 102
        private const val STILL = 103
        private const val OTHERS = 104
        private const val TILTING = 105
        private const val WALKING = 107
        private const val RUNNING = 108

        fun statusFromCode(code: Int): String? = when (code) {
            VEHICLE -> "VEHICLE"
            BIKE -> "BIKE"
            FOOT -> "FOOT"
            STILL -> "STILL"
            OTHERS -> "OTHERS"
            TILTING -> "TILTING"
            WALKING -> "WALKING"
            RUNNING -> "RUNNING"
            else -> "UNDEFINED"
        }
    }
}
