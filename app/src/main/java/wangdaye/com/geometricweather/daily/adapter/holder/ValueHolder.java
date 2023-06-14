package nowsci.com.temperateweather.daily.adapter.holder;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import nowsci.com.temperateweather.R;
import nowsci.com.temperateweather.daily.adapter.DailyWeatherAdapter;
import nowsci.com.temperateweather.daily.adapter.model.Value;

public class ValueHolder extends DailyWeatherAdapter.ViewHolder {

    private final TextView mTitle;
    private final TextView mValue;

    public ValueHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_weather_daily_value, parent, false));
        mTitle = itemView.findViewById(R.id.item_weather_daily_value_title);
        mValue = itemView.findViewById(R.id.item_weather_daily_value_value);
    }

    @SuppressLint("RtlHardcoded")
    @Override
    public void onBindView(DailyWeatherAdapter.ViewModel model, int position) {
        Value v = (Value) model;
        mTitle.setText(v.getTitle());
        mValue.setText(v.getValue());

        itemView.setContentDescription(mTitle.getText() + ", " + mValue.getText());
    }
}
