package nowsci.com.temperateweather.settings.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import nowsci.com.temperateweather.R
import nowsci.com.temperateweather.common.basic.GeoActivity
import nowsci.com.temperateweather.common.ui.widgets.Material3Scaffold
import nowsci.com.temperateweather.common.ui.widgets.generateCollapsedScrollBehavior
import nowsci.com.temperateweather.common.ui.widgets.insets.FitStatusBarTopAppBar
import nowsci.com.temperateweather.settings.compose.ServiceProviderSettingsScreen
import nowsci.com.temperateweather.settings.compose.SettingsProviderAdvancedSettingsScreen
import nowsci.com.temperateweather.settings.compose.SettingsScreenRouter
import nowsci.com.temperateweather.theme.compose.TemperateWeatherTheme

class SelectProviderActivity : GeoActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TemperateWeatherTheme(lightTheme = !isSystemInDarkTheme()) {
                ContentView()
            }
        }
    }

    @Composable
    private fun ContentView() {
        val scrollBehavior = generateCollapsedScrollBehavior()

        Material3Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                FitStatusBarTopAppBar(
                    title = stringResource(R.string.settings_title_service_provider),
                    onBackPressed = { finish() },
                    scrollBehavior = scrollBehavior,
                )
            },
        ) { paddings ->
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = SettingsScreenRouter.ServiceProvider.route
            ) {
                composable(SettingsScreenRouter.ServiceProvider.route) {
                    ServiceProviderSettingsScreen(
                        context = this@SelectProviderActivity,
                        navController = navController,
                        paddingValues = paddings,
                    )
                }
                composable(SettingsScreenRouter.ServiceProviderAdvanced.route) {
                    SettingsProviderAdvancedSettingsScreen(
                        context = this@SelectProviderActivity,
                        paddingValues = paddings,
                    )
                }
            }
        }
    }

    @Preview
    @Composable
    private fun DefaultPreview() {
        TemperateWeatherTheme(lightTheme = isSystemInDarkTheme()) {
            ContentView()
        }
    }
}