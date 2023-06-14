package nowsci.com.temperateweather.main.fragments

import nowsci.com.temperateweather.common.basic.GeoFragment
import nowsci.com.temperateweather.common.bus.EventBus

class ModifyMainSystemBarMessage

abstract class MainModuleFragment: GeoFragment() {

    protected fun checkToSetSystemBarStyle() {
        EventBus
            .instance
            .with(ModifyMainSystemBarMessage::class.java)
            .postValue(ModifyMainSystemBarMessage())
    }

    abstract fun setSystemBarStyle()
}