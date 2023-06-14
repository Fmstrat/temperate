package nowsci.com.temperateweather.remoteviews.presenters

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.widget.RemoteViews
import androidx.annotation.LayoutRes
import nowsci.com.temperateweather.TemperateWeather
import nowsci.com.temperateweather.R
import nowsci.com.temperateweather.background.receiver.widget.WidgetMaterialYouCurrentProvider
import nowsci.com.temperateweather.common.basic.models.Location
import nowsci.com.temperateweather.common.basic.models.options.NotificationTextColor
import nowsci.com.temperateweather.settings.SettingsManager
import nowsci.com.temperateweather.theme.resource.ResourceHelper
import nowsci.com.temperateweather.theme.resource.ResourcesProviderFactory

class MaterialYouCurrentWidgetIMP: AbstractRemoteViewsPresenter() {

    companion object {

        @JvmStatic
        fun isEnable(context: Context): Boolean {
            return AppWidgetManager.getInstance(
                context
            ).getAppWidgetIds(
                ComponentName(
                    context,
                    WidgetMaterialYouCurrentProvider::class.java
                )
            ).isNotEmpty()
        }

        @JvmStatic
        fun updateWidgetView(context: Context, location: Location) {
            AppWidgetManager.getInstance(context).updateAppWidget(
                ComponentName(context, WidgetMaterialYouCurrentProvider::class.java),
                buildRemoteViews(context, location, R.layout.widget_material_you_current)
            )
        }
    }
}

private fun buildRemoteViews(
    context: Context,
    location: Location,
    @LayoutRes layoutId: Int,
): RemoteViews {

    val views = RemoteViews(
        context.packageName,
        layoutId
    )

    val weather = location.weather
    val dayTime = location.isDaylight

    val provider = ResourcesProviderFactory.getNewInstance()

    val settings = SettingsManager.getInstance(context)
    val temperatureUnit = settings.temperatureUnit

    if (weather == null) {
        return views
    }

    // current.

    views.setImageViewUri(
        R.id.widget_material_you_current_currentIcon,
        ResourceHelper.getWidgetNotificationIconUri(
            provider,
            weather.current.weatherCode,
            dayTime,
            false,
            NotificationTextColor.LIGHT
        )
    )

    views.setTextViewText(
        R.id.widget_material_you_current_currentTemperature,
        weather.current.temperature.getShortTemperature(context, temperatureUnit)
    )

    // pending intent.
    views.setOnClickPendingIntent(
        android.R.id.background,
        AbstractRemoteViewsPresenter.getWeatherPendingIntent(
            context,
            location,
            TemperateWeather.WIDGET_MATERIAL_YOU_CURRENT_PENDING_INTENT_CODE_WEATHER
        )
    )

    return views
}