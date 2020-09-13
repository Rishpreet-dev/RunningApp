package com.androiddevs.runningappyt.other

import android.graphics.Color

object Constants {

    const val RUNNING_DATABASE_NAME="runningDB"
    const val REQUEST_LOCATION_PERMISSION=1

    const val ACTION_START_OR_RESUME_SERVICE="ACTION_START_OR_RESUME_SERVICE"
    const val ACTION_PAUSE_SERVICE="ACTION_PAUSE_SERVICE"
    const val ACTION_STOP_SERVICE="ACTION_STOP_SERVICE"

    const val ACTION_SHOW_TRACKING_FRAGMENT="ACTION_SHOW_TRACKING_FRAGMENT"

    const val LOCATION_UPDATE_INTERVAL=5000L
    const val FASTEST_LOCATION_INTERVAL=2000L

    const val POLYLINE_COLOR=Color.RED
    const val POLYLINE_WIDTH=8f

    const val CAMERA_ZOOM=20f

    const val DELAY_TIME=50L

    const val NOTIFICATION_CHANNEL_NAME="Tracking_Channel"
    const val NOTIFICATION_CHANNEL_ID="TrackingChannelID"
    const val NOTIFICATION_ID=1


    const val SHARED_PREFERNCES_NAME="sharedPref"

    const val KEY_FIRST_TIME_TOGGLE="KEY_FIRST_TIME_TOGGLE"
    const val KEY_NAME="KEY_NAME"
    const val KEY_WEIGhT="KEY_WEIGHT"
}