package nowsci.com.temperateweather.common.basic.models.options

import android.content.Context
import nowsci.com.temperateweather.R
import nowsci.com.temperateweather.common.basic.models.options._basic.BaseEnum
import nowsci.com.temperateweather.common.basic.models.options._basic.Utils

enum class WidgetWeekIconMode(
    override val id: String
): BaseEnum {

    AUTO("auto"),
    DAY("day"),
    NIGHT("night");

    companion object {

        @JvmStatic
        fun getInstance(
            value: String
        ) = when (value) {
            "day" -> DAY
            "night" -> NIGHT
            else -> AUTO
        }
    }

    override val valueArrayId = R.array.week_icon_mode_values
    override val nameArrayId = R.array.week_icon_modes

    override fun getName(context: Context) = Utils.getName(context, this)
}