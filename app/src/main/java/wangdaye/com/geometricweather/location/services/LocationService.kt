package wangdaye.com.geometricweather.location.services

import android.Manifest
import android.app.Notification
import wangdaye.com.geometricweather.GeometricWeather.Companion.getNotificationChannelName
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import androidx.annotation.RequiresApi
import android.app.NotificationChannel
import wangdaye.com.geometricweather.GeometricWeather
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.core.app.NotificationCompat
import wangdaye.com.geometricweather.R

abstract class LocationService {

    // location.

    data class Result(
        val latitude: Float,
        val longitude: Float
    )
    interface LocationCallback {
        fun onCompleted(result: Result?)
    }

    abstract fun requestLocation(context: Context, callback: LocationCallback)
    abstract fun cancel()

    // permission.

    abstract val permissions: Array<String>
    open fun hasPermissions(context: Context): Boolean {
        val permissions = permissions
        for (p in permissions) {
            if (p == Manifest.permission.ACCESS_COARSE_LOCATION
                || p == Manifest.permission.ACCESS_FINE_LOCATION) {
                continue
            }
            if (ActivityCompat.checkSelfPermission(context, p) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }

        val coarseLocation = ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val fineLocation = ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        return coarseLocation || fineLocation
    }

    // notification.

    @RequiresApi(api = Build.VERSION_CODES.O)
    fun getLocationNotificationChannel(context: Context): NotificationChannel {
        val channel = NotificationChannel(
            GeometricWeather.NOTIFICATION_CHANNEL_ID_LOCATION,
            getNotificationChannelName(
                context,
                GeometricWeather.NOTIFICATION_CHANNEL_ID_LOCATION
            ),
            NotificationManager.IMPORTANCE_MIN
        )
        channel.setShowBadge(false)
        channel.lightColor = ContextCompat.getColor(context, R.color.colorPrimary)
        return channel
    }

    fun getLocationNotification(context: Context): Notification {
        return NotificationCompat
            .Builder(context, GeometricWeather.NOTIFICATION_CHANNEL_ID_LOCATION)
            .setSmallIcon(R.drawable.ic_location)
            .setContentTitle(context.getString(R.string.feedback_request_location))
            .setContentText(context.getString(R.string.feedback_request_location_in_background))
            .setBadgeIconType(NotificationCompat.BADGE_ICON_NONE)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
            .setAutoCancel(true)
            .setProgress(0, 0, true)
            .build()
    }
}