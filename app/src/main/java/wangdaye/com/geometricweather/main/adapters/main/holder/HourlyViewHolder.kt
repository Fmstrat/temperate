package nowsci.com.temperateweather.main.adapters.main.holder

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import nowsci.com.temperateweather.R
import nowsci.com.temperateweather.common.basic.GeoActivity
import nowsci.com.temperateweather.common.basic.models.Location
import nowsci.com.temperateweather.common.basic.models.weather.Base
import nowsci.com.temperateweather.common.basic.models.weather.Minutely
import nowsci.com.temperateweather.common.ui.adapters.TagAdapter
import nowsci.com.temperateweather.common.ui.decotarions.GridMarginsDecoration
import nowsci.com.temperateweather.common.ui.widgets.precipitationBar.PrecipitationBar
import nowsci.com.temperateweather.common.ui.widgets.trend.TrendRecyclerView
import nowsci.com.temperateweather.common.utils.DisplayUtils
import nowsci.com.temperateweather.main.adapters.trend.HourlyTrendAdapter
import nowsci.com.temperateweather.main.layouts.TrendHorizontalLinearLayoutManager
import nowsci.com.temperateweather.main.utils.MainThemeColorProvider.Companion.getColor
import nowsci.com.temperateweather.main.utils.MainThemeColorProvider.Companion.isLightTheme
import nowsci.com.temperateweather.main.widgets.TrendRecyclerViewScrollBar
import nowsci.com.temperateweather.settings.SettingsManager
import nowsci.com.temperateweather.theme.ThemeManager
import nowsci.com.temperateweather.theme.resource.providers.ResourceProvider
import nowsci.com.temperateweather.theme.weatherView.WeatherViewController

private fun needToShowMinutelyForecast(minutelyList: List<Minutely>) =
    minutelyList.firstOrNull { (it.precipitationIntensity ?: 0.0) > 0.0 } != null

class HourlyViewHolder(
    parent: ViewGroup
) : AbstractMainCardViewHolder(
        LayoutInflater
            .from(parent.context)
            .inflate(R.layout.container_main_hourly_trend_card, parent, false)
) {
    private val title: TextView = itemView.findViewById(R.id.container_main_hourly_trend_card_title)
    private val subtitle: TextView = itemView.findViewById(R.id.container_main_hourly_trend_card_subtitle)
    private val tagView: RecyclerView = itemView.findViewById(R.id.container_main_hourly_trend_card_tagView)
    private val trendRecyclerView: TrendRecyclerView = itemView.findViewById(R.id.container_main_hourly_trend_card_trendRecyclerView)
    private val scrollBar: TrendRecyclerViewScrollBar = TrendRecyclerViewScrollBar()
    private val minutelyContainer: LinearLayout = itemView.findViewById(R.id.container_main_hourly_trend_card_minutely)
    private val minutelyTitle: TextView = itemView.findViewById(R.id.container_main_hourly_trend_card_minutelyTitle)
    private val precipitationBar: PrecipitationBar = itemView.findViewById(R.id.container_main_hourly_trend_card_minutelyBar)
    private val minutelyStartText: TextView = itemView.findViewById(R.id.container_main_hourly_trend_card_minutelyStartText)
    private val minutelyCenterText: TextView = itemView.findViewById(R.id.container_main_hourly_trend_card_minutelyCenterText)
    private val minutelyEndText: TextView = itemView.findViewById(R.id.container_main_hourly_trend_card_minutelyEndText)
    private val minutelyStartLine: View = itemView.findViewById(R.id.container_main_hourly_trend_card_minutelyStartLine)
    private val minutelyEndLine: View = itemView.findViewById(R.id.container_main_hourly_trend_card_minutelyEndLine)

    init {
        trendRecyclerView.setHasFixedSize(true)
        trendRecyclerView.addItemDecoration(scrollBar)

        minutelyContainer.setOnClickListener { /* do nothing. */ }
    }

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

        if (TextUtils.isEmpty(weather.current.hourlyForecast)) {
            subtitle.visibility = View.GONE
        } else {
            subtitle.visibility = View.VISIBLE
            subtitle.text = weather.current.hourlyForecast
        }

        val trendAdapter = HourlyTrendAdapter(activity, trendRecyclerView).apply {
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
                { _, _, newPosition: Int ->
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

        val minutelyList = weather.minutelyForecast
        if (minutelyList.size >= 3 && needToShowMinutelyForecast(minutelyList)) {
            minutelyContainer.visibility = View.VISIBLE
            precipitationBar.precipitationIntensities = minutelyList.map {
                it.precipitationIntensity ?: 0.0
            }.toTypedArray()
            precipitationBar.indicatorGenerator = object : PrecipitationBar.IndicatorGenerator {
                override fun getIndicatorContent(precipitation: Double) =
                    SettingsManager
                        .getInstance(activity)
                        .precipitationIntensityUnit
                        .getValueText(activity, precipitation.toFloat())
            }

            val size = minutelyList.size
            minutelyStartText.text = Base.getTime(context, minutelyList[0].date)
            minutelyCenterText.text = Base.getTime(context, minutelyList[(size - 1) / 2].date)
            minutelyEndText.text = Base.getTime(context, minutelyList[size - 1].date)
            minutelyContainer.contentDescription =
                activity.getString(R.string.content_des_minutely_precipitation)
                    .replace("$1", Base.getTime(context, minutelyList[0].date))
                    .replace("$2", Base.getTime(context, minutelyList[size - 1].date))
        } else {
            minutelyContainer.visibility = View.GONE
        }

        minutelyTitle.setTextColor(colors[0])

        precipitationBar.precipitationColor = ThemeManager
            .getInstance(context)
            .weatherThemeDelegate
            .getThemeColors(
                context,
                WeatherViewController.getWeatherKind(weather),
                location.isDaylight
            )[0]
        precipitationBar.subLineColor = getColor(location, R.attr.colorOutline)
        precipitationBar.highlightColor = getColor(location, R.attr.colorPrimary)
        precipitationBar.setShadowColors(colors[0], colors[1], isLightTheme(itemView.context, location))

        minutelyStartText.setTextColor(getColor(location, R.attr.colorBodyText))
        minutelyCenterText.setTextColor(getColor(location, R.attr.colorBodyText))
        minutelyEndText.setTextColor(getColor(location, R.attr.colorBodyText))

        minutelyStartLine.setBackgroundColor(getColor(location, R.attr.colorOutline))
        minutelyEndLine.setBackgroundColor(getColor(location, R.attr.colorOutline))
    }
}