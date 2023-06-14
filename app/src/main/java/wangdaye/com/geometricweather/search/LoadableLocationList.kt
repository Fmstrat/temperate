package nowsci.com.temperateweather.search

import nowsci.com.temperateweather.common.basic.models.Location

class LoadableLocationList(
    val dataList: List<Location>,
    val status: Status
) {
    enum class Status {
        LOADING, ERROR, SUCCESS
    }
}