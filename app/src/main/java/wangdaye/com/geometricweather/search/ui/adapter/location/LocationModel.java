package nowsci.com.temperateweather.search.ui.adapter.location;

import android.content.Context;

import androidx.annotation.NonNull;

import nowsci.com.temperateweather.R;
import nowsci.com.temperateweather.common.basic.models.Location;
import nowsci.com.temperateweather.common.basic.models.options.provider.WeatherSource;
import nowsci.com.temperateweather.settings.SettingsManager;

public class LocationModel {

    public @NonNull Location location;

    public @NonNull WeatherSource weatherSource;

    public @NonNull String title;
    public @NonNull String subtitle;

    public LocationModel(
            @NonNull Context context,
            @NonNull Location location
    ) {
        this.location = location;

        this.weatherSource = location.isCurrentPosition()
                ? SettingsManager.getInstance(context).getWeatherSource()
                : location.getWeatherSource();

        title = location.isCurrentPosition()
                ? context.getString(R.string.current_location)
                : location.getCityName(context);

        if (!location.isCurrentPosition() || location.isUsable()) {
            subtitle = location.toString();
        } else {
            subtitle = context.getString(R.string.feedback_not_yet_location);
        }
    }

    public boolean areItemsTheSame(@NonNull LocationModel newItem) {
        return location.getFormattedId().equals(
                newItem.location.getFormattedId()
        );
    }

    public boolean areContentsTheSame(@NonNull LocationModel newItem) {
        return location.equals(newItem.location);
    }
}
