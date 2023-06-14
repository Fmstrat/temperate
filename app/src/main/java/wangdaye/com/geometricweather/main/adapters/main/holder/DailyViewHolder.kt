package nowsci.com.temperateweather.main.adapters.main.holder

import android.annotation.SuppressLint
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import nowsci.com.temperateweather.R
import nowsci.com.temperateweather.common.basic.GeoActivity
import nowsci.com.temperateweather.common.basic.models.Location
import nowsci.com.temperateweather.common.ui.adapters.TagAdapter
import nowsci.com.temperateweather.common.ui.decotarions.GridMarginsDecoration
import nowsci.com.temperateweather.common.ui.widgets.trend.TrendRecyclerView
import nowsci.com.temperateweather.common.utils.DisplayUtils
import nowsci.com.temperateweather.main.adapters.trend.DailyTrendAdapter
import nowsci.com.temperateweather.main.layouts.TrendHorizontalLinearLayoutManager
import nowsci.com.temperateweather.main.utils.MainThemeColorProvider.Companion.getColor
import nowsci.com.temperateweather.main.widgets.TrendRecyclerViewScrollBar
import nowsci.com.temperateweather.settings.SettingsManager
import nowsci.com.temperateweather.theme.ThemeManager
import nowsci.com.temperateweather.theme.resource.providers.ResourceProvider
import nowsci.com.temperateweather.theme.weatherView.WeatherViewController

class DailyViewHolder(
    parent: ViewGroup
) : AbstractMainCardViewHolder(
    LayoutInflater
        .from(parent.context)
        .inflate(R.layout.container_main_daily_trend_card, parent, false)
) {
    private val title: TextView = itemView.findViewById(R.id.container_main_daily_trend_card_title)
    private val subtitle: TextView = itemView.findViewById(R.id.container_main_daily_trend_card_subtitle)
    private val tagView: RecyclerView = itemView.findViewById(R.id.container_main_daily_trend_card_tagView)
    private val trendRecyclerView: TrendRecyclerView = itemView.findViewById(R.id.container_main_daily_trend_card_trendRecyclerView)
    private val scrollBar = TrendRecyclerViewScrollBar()

    init {
        trendRecyclerView.setHasFixedSize(true)
        trendRecyclerView.addItemDecoration(scrollBar)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindView(
        activity: GeoActivity,
        location: Location,
        provider: ResourceProvider,
        listAnimationEnabled: Boolean,
        itemAnimationEnabled: Boolean,
        firstCard: Boolean
    ) {
        super.onBindView(
            activity,
            location,
            provider,
            listAnimationEnabled,
            itemAnimationEnabled,
            firstCard
        )
        val weather = location.weather ?: return
        val colors = ThemeManager
            .getInstance(context)
            .weatherThemeDelegate
            .getThemeColors(
                context,
                WeatherViewController.getWeatherKind(weather),
                location.isDaylight
            )

        title.setTextColor(colors[0])

        if (TextUtils.isEmpty(weather.current.dailyForecast)) {
            subtitle.visibility = View.GONE
        } else {
            subtitle.visibility = View.VISIBLE
            subtitle.text = weather.current.dailyForecast
        }

        val trendAdapter = DailyTrendAdapter(activity, trendRecyclerView).apply {
            bindData(location)
        }
        val tagList = trendAdapter.adapters.map {
            TagAdapter.Tag {
                it.getDisplayName(activity)
            }
        }

        if (tagList.size < 2) {
            tagView.visibility = View.GONE
        } else {
            tagView.visibility = View.VISIBLE
            val decorCount = tagView.itemDecorationCount
            for (i in 0 until decorCount) {
                tagView.removeItemDecorationAt(0)
            }
            tagView.addItemDecoration(
                GridMarginsDecoration(
                    context.resources.getDimension(R.dimen.little_margin),
                    context.resources.getDimension(R.dimen.normal_margin),
                    tagView
                )
            )
            tagView.layoutManager = TrendHorizontalLinearLayoutManager(context)
            tagView.adapter = TagAdapter(
                tagList,
                getColor(location, R.attr.colorOnPrimary),
                getColor(location, R.attr.colorOnSurface),
                getColor(location, R.attr.colorPrimary),
                DisplayUtils.getWidgetSurfaceColor(
                    DisplayUtils.DEFAULT_CARD_LIST_ITEM_ELEVATION_DP,
                    getColor(location, R.attr.colorPrimary),
                    getColor(location, R.attr.colorSurface)
                ),
                { _, _, newPosition ->
                    trendAdapter.selectedIndex = newPosition
                    return@TagAdapter false
                },
                0
            )
        }
        trendRecyclerView.layoutManager = TrendHorizontalLinearLayoutManager(
            context,
            if (DisplayUtils.isLandscape(context)) 7 else 5
        )
        trendRecyclerView.setLineColor(getColor(location, R.attr.colorOutline))
        trendRecyclerView.adapter = trendAdapter
        trendRecyclerView.setKeyLineVisibility(
            SettingsManager.getInstance(context).isTrendHorizontalLinesEnabled
        )
        scrollBar.resetColor(location)
    }
}