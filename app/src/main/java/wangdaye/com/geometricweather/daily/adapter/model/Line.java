package nowsci.com.temperateweather.daily.adapter.model;

import nowsci.com.temperateweather.daily.adapter.DailyWeatherAdapter;

public class Line implements DailyWeatherAdapter.ViewModel {

    public static boolean isCode(int code) {
        return code == -1;
    }

    @Override
    public int getCode() {
        return -1;
    }
}
