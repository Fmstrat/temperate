package nowsci.com.temperateweather.background.polling.work.worker;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.hilt.work.HiltWorker;
import androidx.work.WorkerParameters;
import androidx.work.impl.utils.futures.SettableFuture;

import java.util.List;

import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;
import nowsci.com.temperateweather.background.polling.PollingManager;
import nowsci.com.temperateweather.common.basic.models.Location;
import nowsci.com.temperateweather.location.LocationHelper;
import nowsci.com.temperateweather.remoteviews.presenters.notification.ForecastNotificationIMP;
import nowsci.com.temperateweather.weather.WeatherHelper;

@HiltWorker
public class TomorrowForecastUpdateWorker extends AsyncUpdateWorker {

    @AssistedInject
    public TomorrowForecastUpdateWorker(@Assisted @NonNull Context context,
                                        @Assisted @NonNull WorkerParameters workerParams,
                                        LocationHelper locationHelper,
                                        WeatherHelper weatherHelper) {
        super(context, workerParams, locationHelper, weatherHelper);
    }

    @Override
    public void updateView(Context context, Location location) {
        if (ForecastNotificationIMP.isEnable(context, false)) {
            ForecastNotificationIMP.buildForecastAndSendIt(context, location, false);
        }
    }

    @Override
    public void updateView(Context context, List<Location> locationList) {
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void handleUpdateResult(SettableFuture<Result> future, boolean failed) {
        future.set(failed ? Result.failure() : Result.success());
        PollingManager.resetTomorrowForecastBackgroundTask(
                getApplicationContext(), false, true);
    }
}
