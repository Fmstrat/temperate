package nowsci.com.temperateweather.main.adapters.main.holder;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.FloatEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import nowsci.com.temperateweather.R;
import nowsci.com.temperateweather.common.basic.GeoActivity;
import nowsci.com.temperateweather.common.basic.models.Location;
import nowsci.com.temperateweather.common.basic.models.weather.Weather;
import nowsci.com.temperateweather.common.ui.widgets.ArcProgress;
import nowsci.com.temperateweather.main.adapters.AqiAdapter;
import nowsci.com.temperateweather.main.utils.MainThemeColorProvider;
import nowsci.com.temperateweather.theme.ThemeManager;
import nowsci.com.temperateweather.theme.resource.providers.ResourceProvider;
import nowsci.com.temperateweather.theme.weatherView.WeatherViewController;

public class AirQualityViewHolder extends AbstractMainCardViewHolder {

    private final TextView mTitle;

    private final ArcProgress mProgress;
    private final RecyclerView mRecyclerView;
    private AqiAdapter mAdapter;

    private int mAqiIndex;

    private boolean mEnable;
    @Nullable private AnimatorSet mAttachAnimatorSet;

    public AirQualityViewHolder(ViewGroup parent) {
        super(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.container_main_aqi, parent, false)
        );

        mTitle = itemView.findViewById(R.id.container_main_aqi_title);
        mProgress = itemView.findViewById(R.id.container_main_aqi_progress);
        mRecyclerView = itemView.findViewById(R.id.container_main_aqi_recyclerView);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindView(GeoActivity activity, @NonNull Location location,
                           @NonNull ResourceProvider provider,
                           boolean listAnimationEnabled, boolean itemAnimationEnabled, boolean firstCard) {
        super.onBindView(activity, location, provider,
                listAnimationEnabled, itemAnimationEnabled, firstCard);

        Weather weather = location.getWeather();
        assert weather != null;

        mAqiIndex = weather.getCurrent().getAirQuality().getAqiIndex() == null
                ? 0
                : weather.getCurrent().getAirQuality().getAqiIndex();

        mEnable = true;

        mTitle.setTextColor(
                ThemeManager
                        .getInstance(context)
                        .getWeatherThemeDelegate()
                        .getThemeColors(
                                context,
                                WeatherViewController.getWeatherKind(weather),
                                location.isDaylight()
                        )[0]
        );

        if (itemAnimationEnabled) {
            mProgress.setProgress(0);
            mProgress.setText(String.format("%d", 0));
            mProgress.setProgressColor(
                    ContextCompat.getColor(context, R.color.colorLevel_1),
                    MainThemeColorProvider.isLightTheme(context, location)
            );
            mProgress.setArcBackgroundColor(MainThemeColorProvider.getColor(location, R.attr.colorOutline));
        } else {
            int aqiColor = weather.getCurrent().getAirQuality().getAqiColor(mProgress.getContext());
            mProgress.setProgress(mAqiIndex);
            mProgress.setText(String.format("%d", mAqiIndex));

            mProgress.setProgressColor(
                    aqiColor,
                    MainThemeColorProvider.isLightTheme(context, location)
            );
            mProgress.setArcBackgroundColor(
                    ColorUtils.setAlphaComponent(aqiColor, (int) (255 * 0.1))
            );
        }

        mProgress.setTextColor(MainThemeColorProvider.getColor(location, R.attr.colorTitleText));
        mProgress.setBottomText(weather.getCurrent().getAirQuality().getAqiText());
        mProgress.setBottomTextColor(MainThemeColorProvider.getColor(location, R.attr.colorBodyText));
        mProgress.setContentDescription(mAqiIndex + ", " + weather.getCurrent().getAirQuality().getAqiText());

        mAdapter = new AqiAdapter(context, location, itemAnimationEnabled);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onEnterScreen() {
        if (itemAnimationEnabled && mEnable && mLocation != null && mLocation.getWeather() != null) {
            Weather weather = mLocation.getWeather();

            int aqiColor = weather.getCurrent().getAirQuality().getAqiColor(mProgress.getContext());

            ValueAnimator progressColor = ValueAnimator.ofObject(
                    new ArgbEvaluator(),
                    ContextCompat.getColor(context, R.color.colorLevel_1),
                    aqiColor
            );
            progressColor.addUpdateListener(animation -> mProgress.setProgressColor(
                    (Integer) animation.getAnimatedValue(),
                    MainThemeColorProvider.isLightTheme(context, mLocation)
            ));

            ValueAnimator backgroundColor = ValueAnimator.ofObject(
                    new ArgbEvaluator(),
                    MainThemeColorProvider.getColor(
                            mLocation.isDaylight(),
                            R.attr.colorOutline
                    ),
                    ColorUtils.setAlphaComponent(aqiColor, (int) (255 * 0.1))
            );
            backgroundColor.addUpdateListener(animation ->
                    mProgress.setArcBackgroundColor((Integer) animation.getAnimatedValue())
            );

            ValueAnimator aqiNumber = ValueAnimator.ofObject(new FloatEvaluator(), 0, mAqiIndex);
            aqiNumber.addUpdateListener(animation -> {
                mProgress.setProgress((Float) animation.getAnimatedValue());
                mProgress.setText(String.format("%d", (int) mProgress.getProgress()));
            });

            mAttachAnimatorSet = new AnimatorSet();
            mAttachAnimatorSet.playTogether(progressColor, backgroundColor, aqiNumber);
            mAttachAnimatorSet.setInterpolator(new DecelerateInterpolator());
            mAttachAnimatorSet.setDuration((long) (1500 + mAqiIndex / 400f * 1500));
            mAttachAnimatorSet.start();

            mAdapter.executeAnimation();
        }
    }

    @Override
    public void onRecycleView() {
        super.onRecycleView();
        if (mAttachAnimatorSet != null && mAttachAnimatorSet.isRunning()) {
            mAttachAnimatorSet.cancel();
        }
        mAttachAnimatorSet = null;
        if (mAdapter != null) {
            mAdapter.cancelAnimation();
        }
    }
}