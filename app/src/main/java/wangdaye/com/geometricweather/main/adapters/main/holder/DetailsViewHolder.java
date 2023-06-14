package wangdaye.com.geometricweather.main.adapters.main.holder;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import wangdaye.com.geometricweather.R;
import wangdaye.com.geometricweather.common.basic.GeoActivity;
import wangdaye.com.geometricweather.common.basic.models.Location;
import wangdaye.com.geometricweather.main.adapters.DetailsAdapter;
import wangdaye.com.geometricweather.theme.ThemeManager;
import wangdaye.com.geometricweather.theme.resource.providers.ResourceProvider;
import wangdaye.com.geometricweather.theme.weatherView.WeatherViewController;

public class DetailsViewHolder extends AbstractMainCardViewHolder {

    private final TextView mTitle;
    private final RecyclerView mDetailsRecyclerView;

    public DetailsViewHolder(ViewGroup parent) {
        super(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.container_main_details, parent, false)
        );

        mTitle = itemView.findViewById(R.id.container_main_details_title);
        mDetailsRecyclerView = itemView.findViewById(R.id.container_main_details_recyclerView);
    }

    @Override
    public void onBindView(GeoActivity activity, @NonNull Location location, @NonNull ResourceProvider provider,
                           boolean listAnimationEnabled, boolean itemAnimationEnabled, boolean firstCard) {
        super.onBindView(activity, location, provider,
                listAnimationEnabled, itemAnimationEnabled, firstCard);

        if (location.getWeather() != null) {
            mTitle.setTextColor(
                    ThemeManager
                            .getInstance(context)
                            .getWeatherThemeDelegate()
                            .getThemeColors(
                                    context,
                                    WeatherViewController.getWeatherKind(location.getWeather()),
                                    location.isDaylight()
                            )[0]
            );

            mDetailsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            mDetailsRecyclerView.setAdapter(new DetailsAdapter(context, location));
        }
    }
}
