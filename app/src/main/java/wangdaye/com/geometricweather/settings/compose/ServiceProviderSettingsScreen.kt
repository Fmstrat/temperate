package nowsci.com.temperateweather.settings.compose

import android.content.Context
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import nowsci.com.temperateweather.BuildConfig
import nowsci.com.temperateweather.TemperateWeather.Companion.instance
import nowsci.com.temperateweather.R
import nowsci.com.temperateweather.common.basic.models.options.provider.LocationProvider
import nowsci.com.temperateweather.common.basic.models.options.provider.WeatherSource
import nowsci.com.temperateweather.common.utils.helpers.SnackbarHelper
import nowsci.com.temperateweather.db.DatabaseHelper
import nowsci.com.temperateweather.settings.SettingsManager
import nowsci.com.temperateweather.settings.preference.bottomInsetItem
import nowsci.com.temperateweather.settings.preference.clickablePreferenceItem
import nowsci.com.temperateweather.settings.preference.composables.ListPreferenceView
import nowsci.com.temperateweather.settings.preference.composables.PreferenceScreen
import nowsci.com.temperateweather.settings.preference.composables.PreferenceView
import nowsci.com.temperateweather.settings.preference.listPreferenceItem

@Composable
fun ServiceProviderSettingsScreen(
    context: Context,
    navController: NavHostController,
    paddingValues: PaddingValues,
) = PreferenceScreen(paddingValues = paddingValues) {
    listPreferenceItem(R.string.settings_title_weather_source) { id ->
        ListPreferenceView(
            titleId = id,
            valueArrayId = R.array.weather_source_values,
            nameArrayId = R.array.weather_sources,
            selectedKey = SettingsManager.getInstance(context).weatherSource.id,
            onValueChanged = { sourceId ->
                SettingsManager
                    .getInstance(context)
                    .weatherSource = WeatherSource.getInstance(sourceId)

                val locationList = DatabaseHelper.getInstance(context).readLocationList()
                val index = locationList.indexOfFirst { it.isCurrentPosition }
                if (index >= 0) {
                    locationList[index] = locationList[index].copy(
                        weather = null,
                        weatherSource = SettingsManager.getInstance(context).weatherSource
                    ).copy()
                    DatabaseHelper.getInstance(context).deleteWeather(locationList[index])
                    DatabaseHelper.getInstance(context).writeLocationList(locationList)
                }
            }
        )
    }

    listPreferenceItem(R.string.settings_title_location_service) { id ->
        // read configuration from data store.
        var currentSelectedKey = SettingsManager.getInstance(context).locationProvider.id
        var valueList = stringArrayResource(R.array.location_service_values)
        var nameList = stringArrayResource(R.array.location_services)

        // clear invalid config by build flavor.
        if (BuildConfig.FLAVOR.contains("fdroid")
            || BuildConfig.FLAVOR.contains("gplay")) {
            valueList = arrayOf(valueList[1], valueList[3])
            nameList = arrayOf(nameList[1], nameList[3])
        }
        if (!valueList.contains(currentSelectedKey)) {
            currentSelectedKey = LocationProvider.NATIVE.id
        }

        ListPreferenceView(
            title = stringResource(id),
            summary = { _, key -> nameList[valueList.indexOfFirst { it == key }] },
            selectedKey = currentSelectedKey,
            valueArray = valueList,
            nameArray = nameList,
            onValueChanged = { sourceId ->
                SettingsManager
                    .getInstance(context)
                    .locationProvider = LocationProvider.getInstance(sourceId)

                SnackbarHelper.showSnackbar(
                    context.getString(R.string.feedback_restart),
                    context.getString(R.string.restart)
                ) {
                    instance.recreateAllActivities()
                }
            }
        )
    }

    clickablePreferenceItem(R.string.settings_title_service_provider_advanced) {
        PreferenceView(
            titleId = it,
            summaryId = R.string.settings_summary_service_provider_advanced,
        ) {
            navController.navigate(SettingsScreenRouter.ServiceProviderAdvanced.route)
        }
    }

    bottomInsetItem()
}