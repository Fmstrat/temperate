package nowsci.com.temperateweather.background.polling.services.permanent.update;

import android.content.Context;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import nowsci.com.temperateweather.TemperateWeather;
import nowsci.com.temperateweather.background.polling.services.basic.ForegroundUpdateService;
import nowsci.com.temperateweather.common.basic.models.Location;
import nowsci.com.temperateweather.background.polling.services.permanent.PermanentServiceHelper;
import nowsci.com.temperateweather.remoteviews.NotificationHelper;
import nowsci.com.temperateweather.remoteviews.WidgetHelper;

/**
 * Foreground normal update service.
 * */

@AndroidEntryPoint
public class ForegroundNormalUpdateService extends ForegroundUpdateService {

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
        PermanentServiceHelper.updatePollingService(this, failed);
    }

    @Override
    public int getForegroundNotificationId() {
        return TemperateWeather.NOTIFICATION_ID_UPDATING_NORMALLY;
    }
}
