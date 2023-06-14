package nowsci.com.temperateweather.daily.adapter.holder;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import nowsci.com.temperateweather.R;
import nowsci.com.temperateweather.daily.adapter.DailyWeatherAdapter;

public class MarginHolder extends DailyWeatherAdapter.ViewHolder {

    public MarginHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_weather_daily_margin, parent, false));
    }

    @Override
    public void onBindView(DailyWeatherAdapter.ViewModel model, int position) {
        // do nothing.
    }
}
