package nowsci.com.temperateweather.daily.adapter.holder;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import nowsci.com.temperateweather.R;
import nowsci.com.temperateweather.common.basic.models.options.unit.TemperatureUnit;
import nowsci.com.temperateweather.daily.adapter.DailyWeatherAdapter;
import nowsci.com.temperateweather.daily.adapter.model.Overview;
import nowsci.com.temperateweather.theme.resource.providers.ResourceProvider;
import nowsci.com.temperateweather.theme.resource.ResourcesProviderFactory;
import nowsci.com.temperateweather.settings.SettingsManager;
import nowsci.com.temperateweather.common.ui.widgets.AnimatableIconView;

public class OverviewHolder extends DailyWeatherAdapter.ViewHolder {

    private AnimatableIconView mIcon;
    private final TextView mTitle;

    private final ResourceProvider mProvider;
    private final TemperatureUnit mTemperatureUnit;

    public OverviewHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_weather_daily_overview, parent, false));
        itemView.setOnClickListener(v -> mIcon.startAnimators());

        mIcon = itemView.findViewById(R.id.item_weather_daily_overview_icon);
        mTitle = itemView.findViewById(R.id.item_weather_daily_overview_text);

        mProvider = ResourcesProviderFactory.getNewInstance();
        mTemperatureUnit = SettingsManager.getInstance(parent.getContext()).getTemperatureUnit();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindView(DailyWeatherAdapter.ViewModel model, int position) {
        Overview overview = (Overview) model;
        mIcon.setAnimatableIcon(
                mProvider.getWeatherIcons(overview.getHalfDay().getWeatherCode(), overview.isDaytime()),
                mProvider.getWeatherAnimators(overview.getHalfDay().getWeatherCode(), overview.isDaytime())
        );
        mTitle.setText(overview.getHalfDay().getWeatherText()
                + ", " + overview.getHalfDay().getTemperature().getTemperature(mTitle.getContext(), mTemperatureUnit));
    }
}
