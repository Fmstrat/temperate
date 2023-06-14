package nowsci.com.temperateweather.weather;

import androidx.annotation.NonNull;

import javax.inject.Inject;

import nowsci.com.temperateweather.common.basic.models.options.provider.WeatherSource;
import nowsci.com.temperateweather.weather.services.AccuWeatherService;
import nowsci.com.temperateweather.weather.services.CaiYunWeatherService;
import nowsci.com.temperateweather.weather.services.MfWeatherService;
import nowsci.com.temperateweather.weather.services.OwmWeatherService;
import nowsci.com.temperateweather.weather.services.WeatherService;

public class WeatherServiceSet {

    private final WeatherService[] mWeatherServices;

    @Inject
    public WeatherServiceSet(AccuWeatherService accuWeatherService,
                             CaiYunWeatherService caiYunWeatherService,
                             MfWeatherService mfWeatherService,
                             OwmWeatherService owmWeatherService) {
        mWeatherServices = new WeatherService[] {
                accuWeatherService,
                caiYunWeatherService,
                mfWeatherService,
                owmWeatherService
        };
    }

    @NonNull
    public WeatherService get(WeatherSource source) {
        switch (source) {
            case OWM:
                return mWeatherServices[3];

            case MF:
                return mWeatherServices[2];

            case CAIYUN:
                return mWeatherServices[1];

            default: // ACCU.
                return mWeatherServices[0];
        }
    }

    @NonNull
    public WeatherService[] getAll() {
        return mWeatherServices;
    }
}
