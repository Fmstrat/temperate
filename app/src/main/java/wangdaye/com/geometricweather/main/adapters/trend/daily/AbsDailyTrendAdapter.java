package nowsci.com.temperateweather.main.adapters.trend.daily;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import java.util.TimeZone;

import nowsci.com.temperateweather.R;
import nowsci.com.temperateweather.common.basic.GeoActivity;
import nowsci.com.temperateweather.common.basic.models.Location;
import nowsci.com.temperateweather.common.basic.models.weather.Daily;
import nowsci.com.temperateweather.common.basic.models.weather.Weather;
import nowsci.com.temperateweather.common.ui.widgets.trend.TrendRecyclerView;
import nowsci.com.temperateweather.common.ui.widgets.trend.TrendRecyclerViewAdapter;
import nowsci.com.temperateweather.common.ui.widgets.trend.item.DailyTrendItemView;
import nowsci.com.temperateweather.common.utils.helpers.IntentHelper;
import nowsci.com.temperateweather.main.utils.MainThemeColorProvider;

public abstract class AbsDailyTrendAdapter extends TrendRecyclerViewAdapter<AbsDailyTrendAdapter.ViewHolder>  {

    private final GeoActivity mActivity;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final DailyTrendItemView dailyItem;

        ViewHolder(View itemView) {
            super(itemView);
            dailyItem = itemView.findViewById(R.id.item_trend_daily);
        }

        @SuppressLint({"SetTextI18n, InflateParams", "DefaultLocale"})
        void onBindView(GeoActivity activity, Location location,
                        StringBuilder talkBackBuilder, int position) {
            Context context = itemView.getContext();
            Weather weather = location.getWeather();
            TimeZone timeZone = location.getTimeZone();

            assert weather != null;
            Daily daily = weather.getDailyForecast().get(position);

            if (daily.isToday(timeZone)) {
                talkBackBuilder.append(", ").append(context.getString(R.string.today));
                dailyItem.setWeekText(context.getString(R.string.today));
            } else {
                talkBackBuilder.append(", ").append(daily.getWeek(context));
                dailyItem.setWeekText(daily.getWeek(context));
            }

            talkBackBuilder.append(", ").append(daily.getLongDate(context));
            dailyItem.setDateText(daily.getShortDate(context));

            dailyItem.setTextColor(
                    MainThemeColorProvider.getColor(location, R.attr.colorTitleText),
                    MainThemeColorProvider.getColor(location, R.attr.colorBodyText)
            );

            dailyItem.setOnClickListener(v -> onItemClicked(activity, location, getAdapterPosition()));
        }
    }

    public AbsDailyTrendAdapter(GeoActivity activity, Location location) {
        super(location);
        mActivity = activity;
    }

    protected static void onItemClicked(GeoActivity activity, Location location, int adapterPosition) {
        if (activity.isActivityResumed()) {
            IntentHelper.startDailyWeatherActivity(activity, location.getFormattedId(), adapterPosition);
        }
    }

    public GeoActivity getActivity() {
        return mActivity;
    }

    public abstract boolean isValid(Location location);

    public String getKey() {
        return getClass().getName();
    }

    public abstract String getDisplayName(Context context);

    public abstract void bindBackgroundForHost(TrendRecyclerView host);
}
