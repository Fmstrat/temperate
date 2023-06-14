package nowsci.com.temperateweather.settings.compose

sealed class SettingsScreenRouter(val route: String) {
    object Root : SettingsScreenRouter("nowsci.com.temperateweather.settings.root")
    object Appearance : SettingsScreenRouter("nowsci.com.temperateweather.settings.appearance")
    object ServiceProvider : SettingsScreenRouter("nowsci.com.temperateweather.settings.providers")
    object ServiceProviderAdvanced : SettingsScreenRouter("nowsci.com.temperateweather.settings.advanced")
    object Unit : SettingsScreenRouter("nowsci.com.temperateweather.settings.unit")
}