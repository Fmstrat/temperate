package nowsci.com.temperateweather.remoteviews.config;

import android.view.View;
import android.widget.RemoteViews;

import dagger.hilt.android.AndroidEntryPoint;
import nowsci.com.temperateweather.R;
import nowsci.com.temperateweather.remoteviews.presenters.TextWidgetIMP;

/**
 * Text widget config activity.
 * */

@AndroidEntryPoint
public class TextWidgetConfigActivity extends AbstractWidgetConfigActivity {

    @Override
    public void initView() {
        super.initView();
        mTextColorContainer.setVisibility(View.VISIBLE);
        mTextSizeContainer.setVisibility(View.VISIBLE);
        mAlignEndContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public RemoteViews getRemoteViews() {
        return TextWidgetIMP.getRemoteViews(this, getLocationNow(), textColorValueNow, textSize, alignEnd);
    }

    @Override
    public String getConfigStoreName() {
        return getString(R.string.sp_widget_text_setting);
    }
}