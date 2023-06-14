package nowsci.com.temperateweather.background.polling.services.basic;

import android.content.Context;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import nowsci.com.temperateweather.TemperateWeather;
import nowsci.com.temperateweather.background.polling.PollingManager;
import nowsci.com.temperateweather.common.basic.models.Location;
import nowsci.com.temperateweather.remoteviews.NotificationHelper;
import nowsci.com.temperateweather.remoteviews.WidgetHelper;

/**
 * Awake foreground update service.
 * */

@AndroidEntryPoint
public class AwakeForegroundUpdateService extends ForegroundUpdateService {

    @Override
    public void updateView(Context context, Location location) {
        WidgetHelper.updateWidgetIfNecessary(context, location);
    }

    @Override
    public void updateView(Context context, List<Location> locationList) {
        WidgetHelper.updateWidgetIfNecessary(context, locationList);
        NotificationHelper.updateNotificationIfNecessary(context, locationList);
    }

    @Override
    public void handlePollingResult(boolean failed) {
        PollingManager.resetAllBackgroundTask(this, false);
    }

    @Override
    public int getForegroundNotificationId() {
        return TemperateWeather.NOTIFICATION_ID_UPDATING_AWAKE;
    }
}
