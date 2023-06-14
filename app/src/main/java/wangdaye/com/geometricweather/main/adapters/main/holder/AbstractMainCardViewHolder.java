package nowsci.com.temperateweather.main.adapters.main.holder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import nowsci.com.temperateweather.R;
import nowsci.com.temperateweather.common.basic.GeoActivity;
import nowsci.com.temperateweather.common.basic.models.Location;
import nowsci.com.temperateweather.main.adapters.main.FirstCardHeaderController;
import nowsci.com.temperateweather.main.utils.MainThemeColorProvider;
import nowsci.com.temperateweather.theme.ThemeManager;
import nowsci.com.temperateweather.theme.resource.providers.ResourceProvider;
import nowsci.com.temperateweather.theme.weatherView.WeatherThemeDelegate;

public abstract class AbstractMainCardViewHolder extends AbstractMainViewHolder {

    private FirstCardHeaderController mFirstCardHeaderController;
    protected @Nullable Location mLocation;

    @SuppressLint("ObjectAnimatorBinding")
    public AbstractMainCardViewHolder(@NonNull View view) {
        super(view);
    }

    @CallSuper
    public void onBindView(GeoActivity activity, @NonNull Location location,
                           @NonNull ResourceProvider provider,
                           boolean listAnimationEnabled, boolean itemAnimationEnabled, boolean firstCard) {
        super.onBindView(activity, location, provider, listAnimationEnabled, itemAnimationEnabled);
        mLocation = location;

        WeatherThemeDelegate delegate = ThemeManager
                .getInstance(activity)
                .getWeatherThemeDelegate();

        CardView card = (CardView) itemView;
        card.setRadius(delegate.getHomeCardRadius(activity));
        card.setElevation(delegate.getHomeCardElevation(activity));
        card.setCardBackgroundColor(
                MainThemeColorProvider.getColor(location, R.attr.colorMainCardBackground)
        );

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) card.getLayoutParams();
        params.setMargins(
                delegate.getHomeCardMargins(context),
                0,
                delegate.getHomeCardMargins(context),
                delegate.getHomeCardMargins(context)
        );
        card.setLayoutParams(params);

        if (firstCard) {
            mFirstCardHeaderController = new FirstCardHeaderController(activity, location);
            mFirstCardHeaderController.bind((LinearLayout) card.getChildAt(0));
        }
    }

    @SuppressLint("MissingSuperCall")
    @Deprecated
    @Override
    public void onBindView(Context context, @NonNull Location location,
                           @NonNull ResourceProvider provider,
                           boolean listAnimationEnabled, boolean itemAnimationEnabled) {
        throw new RuntimeException("Deprecated method.");
    }

    @Override
    public void onRecycleView() {
        super.onRecycleView();
        if (mFirstCardHeaderController != null) {
            mFirstCardHeaderController.unbind();
            mFirstCardHeaderController = null;
        }
    }
}
