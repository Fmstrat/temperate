package nowsci.com.temperateweather.daily.adapter.model;

import nowsci.com.temperateweather.common.basic.models.weather.HalfDay;
import nowsci.com.temperateweather.daily.adapter.DailyWeatherAdapter;

public class Overview implements DailyWeatherAdapter.ViewModel {

    private HalfDay halfDay;
    private boolean daytime;

    public Overview(HalfDay halfDay, boolean daytime) {
        this.halfDay = halfDay;
        this.daytime = daytime;
    }

    public HalfDay getHalfDay() {
        return halfDay;
    }

    public void setHalfDay(HalfDay halfDay) {
        this.halfDay = halfDay;
    }

    public boolean isDaytime() {
        return daytime;
    }

    public void setDaytime(boolean daytime) {
        this.daytime = daytime;
    }

    public static boolean isCode(int code) {
        return code == 1;
    }

    @Override
    public int getCode() {
        return 1;
    }
}
