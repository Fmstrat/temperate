package nowsci.com.temperateweather.weather;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import nowsci.com.temperateweather.common.basic.models.Location;
import nowsci.com.temperateweather.common.basic.models.options.provider.WeatherSource;
import nowsci.com.temperateweather.common.basic.models.weather.Weather;
import nowsci.com.temperateweather.common.rxjava.BaseObserver;
import nowsci.com.temperateweather.common.rxjava.ObserverContainer;
import nowsci.com.temperateweather.common.rxjava.SchedulerTransformer;
import nowsci.com.temperateweather.common.utils.NetworkUtils;
import nowsci.com.temperateweather.common.utils.helpers.AsyncHelper;
import nowsci.com.temperateweather.db.DatabaseHelper;
import nowsci.com.temperateweather.weather.services.WeatherService;

public class WeatherHelper {

    private final WeatherServiceSet mServiceSet;
    private final CompositeDisposable mCompositeDisposable;

    public interface OnRequestWeatherListener {
        void requestWeatherSuccess(@NonNull Location requestLocation);
        void requestWeatherFailed(@NonNull Location requestLocation);
    }

    public interface OnRequestLocationListener {
        void requestLocationSuccess(String query, List<Location> locationList);
        void requestLocationFailed(String query);
    }

    @Inject
    public WeatherHelper(WeatherServiceSet weatherServiceSet,
                         CompositeDisposable compositeDisposable) {
        mServiceSet = weatherServiceSet;
        mCompositeDisposable = compositeDisposable;
    }

    public void requestWeather(Context c, Location location, @NonNull final OnRequestWeatherListener l) {
        final WeatherService service = mServiceSet.get(location.getWeatherSource());
        if (!NetworkUtils.isAvailable(c)) {
            l.requestWeatherFailed(location);
            return;
        }

        service.requestWeather(c, location.copy(), new WeatherService.RequestWeatherCallback() {

            @Override
            public void requestWeatherSuccess(@NonNull Location requestLocation) {
                Weather weather = requestLocation.getWeather();
                if (weather != null) {
                    DatabaseHelper.getInstance(c).writeWeather(requestLocation, weather);
                    if (weather.getYesterday() == null) {
                        weather.setYesterday(
                                DatabaseHelper.getInstance(c).readHistory(requestLocation, weather)
                        );
                    }
                    l.requestWeatherSuccess(requestLocation);
                } else {
                    requestWeatherFailed(requestLocation);
                }
            }

            @Override
            public void requestWeatherFailed(@NonNull Location requestLocation) {
                l.requestWeatherFailed(
                        Location.copy(
                                requestLocation,
                                DatabaseHelper.getInstance(c).readWeather(requestLocation)
                        )
                );
            }
        });
    }

    public void requestLocation(Context context, String query, List<WeatherSource> enabledSources,
                                @NonNull final OnRequestLocationListener l) {
        if (enabledSources == null || enabledSources.isEmpty()) {
            AsyncHelper.delayRunOnUI(() -> l.requestLocationFailed(query), 0);
            return;
        }

        // generate weather services.
        final WeatherService[] services = new WeatherService[enabledSources.size()];
        for (int i = 0; i < services.length; i ++) {
            services[i] = mServiceSet.get(enabledSources.get(i));
        }

        // generate observable list.
        List<Observable<List<Location>>> observableList = new ArrayList<>();
        for (int i = 0; i < services.length; i ++) {
            int finalI = i;
            observableList.add(
                    Observable.create(emitter ->
                            emitter.onNext(services[finalI].requestLocation(context, query)))
            );
        }

        Observable.zip(observableList, objects -> {
            List<Location> locationList = new ArrayList<>();
            for (Object o : objects) {
                locationList.addAll((List<Location>) o);
            }
            return locationList;
        }).compose(SchedulerTransformer.create())
                .subscribe(new ObserverContainer<>(mCompositeDisposable, new BaseObserver<List<Location>>() {
                    @Override
                    public void onSucceed(List<Location> locationList) {
                        if (locationList != null && locationList.size() != 0) {
                            l.requestLocationSuccess(query, locationList);
                        } else {
                            onFailed();
                        }
                    }

                    @Override
                    public void onFailed() {
                        l.requestLocationFailed(query);
                    }
                }));
    }

    public void cancel() {
        for (WeatherService s : mServiceSet.getAll()) {
            s.cancel();
        }
        mCompositeDisposable.clear();
    }
}
