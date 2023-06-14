package nowsci.com.temperateweather.remoteviews.presenters.notification;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Icon;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Date;

import nowsci.com.temperateweather.TemperateWeather;
import nowsci.com.temperateweather.R;
import nowsci.com.temperateweather.common.basic.models.Location;
import nowsci.com.temperateweather.common.basic.models.options.unit.TemperatureUnit;
import nowsci.com.temperateweather.common.basic.models.weather.Base;
import nowsci.com.temperateweather.common.basic.models.weather.Weather;
import nowsci.com.temperateweather.remoteviews.presenters.AbstractRemoteViewsPresenter;
import nowsci.com.temperateweather.theme.ThemeManager;
import nowsci.com.temperateweather.theme.resource.ResourceHelper;
import nowsci.com.temperateweather.theme.resource.providers.ResourceProvider;
import nowsci.com.temperateweather.theme.resource.ResourcesProviderFactory;
import nowsci.com.temperateweather.settings.SettingsManager;
import nowsci.com.temperateweather.theme.weatherView.WeatherViewController;
import nowsci.com.temperateweather.common.utils.LanguageUtils;
import nowsci.com.temperateweather.common.utils.helpers.LunarHelper;

class NativeNormalNotificationIMP extends AbstractRemoteViewsPresenter {

    static void buildNotificationAndSendIt(
            Context context,
            Location location,
            TemperatureUnit temperatureUnit,
            boolean daytime,
            boolean tempIcon,
            boolean canBeCleared
    ) {
        Weather weather = location.getWeather();
        if (weather == null) {
            return;
        }

        ResourceProvider provider = ResourcesProviderFactory.getNewInstance();

        LanguageUtils.setLanguage(
                context,
                SettingsManager.getInstance(context).getLanguage().getLocale()
        );

        // create channel.
        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    TemperateWeather.NOTIFICATION_CHANNEL_ID_NORMALLY,
                    TemperateWeather.getNotificationChannelName(
                            context,
                            TemperateWeather.NOTIFICATION_CHANNEL_ID_NORMALLY
                    ),
                    NotificationManager.IMPORTANCE_LOW
            );
            channel.setShowBadge(false);
            channel.setImportance(NotificationManager.IMPORTANCE_HIGH);
            channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            manager.createNotificationChannel(channel);
        }

        // get manager & builder.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                context,
                TemperateWeather.NOTIFICATION_CHANNEL_ID_NORMALLY
        );

        // set notification level.
        builder.setPriority(NotificationCompat.PRIORITY_MAX);

        // set notification visibility.
        builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        // set small icon.
        builder.setSmallIcon(
                tempIcon ? ResourceHelper.getTempIconId(
                        context,
                        temperatureUnit.getValueWithoutUnit(
                                SettingsManager.getInstance(context).isNotificationFeelsLike() ?
                                weather.getCurrent().getTemperature().getRealFeelTemperature() :
                                weather.getCurrent().getTemperature().getTemperature()
                        )
                ) : ResourceHelper.getDefaultMinimalXmlIconId(
                        weather.getCurrent().getWeatherCode(),
                        daytime
                )
        );

        // large icon.
        builder.setLargeIcon(
                drawableToBitmap(
                        ResourceHelper.getWidgetNotificationIcon(
                                provider, weather.getCurrent().getWeatherCode(),
                                daytime, false, false
                        )
                )
        );

        StringBuilder subtitle = new StringBuilder();
        subtitle.append(location.getCityName(context));
        if (SettingsManager.getInstance(context).getLanguage().isChinese()) {
            subtitle.append(", ").append(LunarHelper.getLunarDate(new Date()));
        } else {
            subtitle.append(", ")
                    .append(context.getString(R.string.refresh_at))
                    .append(" ")
                    .append(Base.getTime(context, weather.getBase().getUpdateDate()));
        }
        builder.setSubText(subtitle.toString());

        StringBuilder content = new StringBuilder();
        if (!tempIcon) {
            content.append(
                    SettingsManager.getInstance(context).isNotificationFeelsLike() ?
                    weather.getCurrent().getTemperature().getRealFeelTemperature(context, temperatureUnit) :
                    weather.getCurrent().getTemperature().getTemperature(context, temperatureUnit))
                    .append(" ");
        }
        content.append(weather.getCurrent().getWeatherText());
        builder.setContentTitle(content.toString());

        StringBuilder contentText = new StringBuilder();
        if (weather.getCurrent().getAirQuality().isValid()) {
            contentText.append(context.getString(R.string.air_quality))
                    .append(" - ")
                    .append(weather.getCurrent().getAirQuality().getAqiText());
        } else {
            contentText.append(context.getString(R.string.wind))
                    .append(" - ")
                    .append(weather.getCurrent().getWind().getLevel());
        }
        builder.setContentText(contentText.toString());

        builder.setColor(
                ThemeManager.getInstance(context).getWeatherThemeDelegate().getThemeColors(
                        context, WeatherViewController.getWeatherKind(weather), daytime
                )[0]
        );

        // set clear flag
        builder.setOngoing(!canBeCleared);

        // set only alert once.
        builder.setOnlyAlertOnce(true);

        builder.setContentIntent(
                getWeatherPendingIntent(context, null, TemperateWeather.NOTIFICATION_ID_NORMALLY)
        );

        Notification notification = builder.build();
        if (!tempIcon && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                notification.getClass()
                        .getMethod("setSmallIcon", Icon.class)
                        .invoke(
                                notification,
                                ResourceHelper.getMinimalIcon(
                                        provider, weather.getCurrent().getWeatherCode(), daytime)
                        );
            } catch (Exception ignore) {
                // do nothing.
            }
        }

        // commit.
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED) {
            manager.notify(TemperateWeather.NOTIFICATION_ID_NORMALLY, notification);
        }
    }
}
