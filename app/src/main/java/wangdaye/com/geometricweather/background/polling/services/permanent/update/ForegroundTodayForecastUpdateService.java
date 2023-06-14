package nowsci.com.temperateweather.background.polling.services.permanent.update;

import android.content.Context;

import androidx.core.app.NotificationCompat;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import nowsci.com.temperateweather.TemperateWeather;
import nowsci.com.temperateweather.R;
import nowsci.com.temperateweather.background.polling.services.basic.ForegroundUpdateService;
import nowsci.com.temperateweather.common.basic.models.Location;
import nowsci.com.temperateweather.remoteviews.presenters.notification.ForecastNotificationIMP;

/**
 * Foreground Today forecast update service.
 * */

@AndroidEntryPoint
public class ForegroundTodayForecastUpdateService extends ForegroundUpdateService {

    @Override
    public void updateView(Context context, Location location) {
        if (ForecastNotificationIMP.isEnable(this, true)) {
            ForecastNotificationIMP.buildForecastAndSendIt(context, location, true);
        }
    }

    @Override
    public void updateView(Context context, List<Location> locationList) {
    }

    @Override
    public void handlePollingResult(boolean failed) {
        // do nothing.
    }

    @Override
    public NotificationCompat.Builder getForegroundNotification(int total) {
        return super.getForegroundNotification(total).setContentTitle(
                getString(R.string.geometric_weather) + " " + getString(R.string.forecast)
        );
    }

    @Override
    public int getForegroundNotificationId() {
        return TemperateWeather.NOTIFICATION_ID_UPDATING_TODAY_FORECAST;
    }
}