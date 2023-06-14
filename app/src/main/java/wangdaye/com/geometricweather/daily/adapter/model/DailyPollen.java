package nowsci.com.temperateweather.daily.adapter.model;

import nowsci.com.temperateweather.common.basic.models.weather.Pollen;
import nowsci.com.temperateweather.daily.adapter.DailyWeatherAdapter;

public class DailyPollen implements DailyWeatherAdapter.ViewModel {

    private Pollen pollen;

    public DailyPollen(Pollen pollen) {
        this.pollen = pollen;
    }

    public Pollen getPollen() {
        return pollen;
    }

    public void setPollen(Pollen pollen) {
        this.pollen = pollen;
    }

    public static boolean isCode(int code) {
        return code == 6;
    }

    @Override
    public int getCode() {
        return 6;
    }
}
