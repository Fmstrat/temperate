package nowsci.com.temperateweather.background.polling

import android.content.Context
import android.widget.Toast
import nowsci.com.temperateweather.R
import nowsci.com.temperateweather.common.basic.models.Location
import nowsci.com.temperateweather.common.basic.models.weather.Weather
import nowsci.com.temperateweather.common.bus.EventBus
import nowsci.com.temperateweather.common.utils.helpers.AsyncHelper
import nowsci.com.temperateweather.db.DatabaseHelper
import nowsci.com.temperateweather.location.LocationHelper
import nowsci.com.temperateweather.weather.WeatherHelper
import nowsci.com.temperateweather.weather.WeatherHelper.OnRequestWeatherListener

class PollingUpdateHelper(
    private val context: Context,
    private val locationHelper: LocationHelper,
    private val weatherHelper: WeatherHelper
) {
    private var isUpdating = false

    private var ioController: AsyncHelper.Controller? = null
    private var locationList = emptyList<Location>().toMutableList()
    private var listener: OnPollingUpdateListener? = null

    interface OnPollingUpdateListener {

        fun onUpdateCompleted(
            location: Location,
            old: Weather?,
            succeed: Boolean,
            index: Int,
            total: Int
        )
        fun onPollingCompleted(locationList: List<Location>?)
    }

    // control.

    fun pollingUpdate() {
        if (isUpdating) {
            return
        }
        isUpdating = true

        ioController = AsyncHelper.runOnIO({ emitter ->
            val list = DatabaseHelper.getInstance(context).readLocationList().map {
                it.copy(weather = DatabaseHelper.getInstance(context).readWeather(it))
            }
            emitter.send(list, true)
        }, { locations: List<Location>?, _: Boolean ->
            locations?.let {
                locationList = it.toMutableList()
                requestData(0, false)
            }
        })
    }

    fun cancel() {
        isUpdating = false

        ioController?.cancel()
        locationHelper.cancel()
        weatherHelper.cancel()
    }

    private fun requestData(position: Int, located: Boolean) {
        if (locationList[position].weather?.isValid(0.25f) == true) {
            RequestWeatherCallback(position, locationList.size).requestWeatherSuccess(locationList[position])
            return
        }

        if (locationList[position].isCurrentPosition && !located) {
            locationHelper.requestLocation(
                context,
                locationList[position],
                true,
                RequestLocationCallback(position, locationList.size)
            )
            return
        }

        weatherHelper.requestWeather(
            context,
            locationList[position],
            RequestWeatherCallback(position, locationList.size)
        )
    }

    // interface.

    fun setOnPollingUpdateListener(l: OnPollingUpdateListener?) {
        listener = l
    }

    // on request location listener.

    private inner class RequestLocationCallback(
        private val index: Int,
        private val total: Int
    ) : LocationHelper.OnRequestLocationListener {

        override fun requestLocationSuccess(requestLocation: Location) {
            locationList[index] = requestLocation

            if (requestLocation.isUsable) {
                requestData(index, true)
            } else {
                requestLocationFailed(requestLocation)
                Toast.makeText(
                    context,
                    context.getString(R.string.feedback_not_yet_location),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        override fun requestLocationFailed(requestLocation: Location) {
            if (locationList[index].isUsable) {
                requestData(index, true)
            } else {
                RequestWeatherCallback(index, total).requestWeatherFailed(locationList[index])
            }
        }
    }

    // on request weather listener.

    private inner class RequestWeatherCallback(
        private val index: Int,
        private val total: Int
    ) : OnRequestWeatherListener {

        override fun requestWeatherSuccess(requestLocation: Location) {
            val oldWeather = locationList[index].weather

            if (requestLocation.weather != null
                && (oldWeather == null || requestLocation.weather.base.timeStamp != oldWeather.base.timeStamp)) {
                locationList[index] = requestLocation

                EventBus.instance
                    .with(Location::class.java)
                    .postValue(requestLocation)

                listener?.onUpdateCompleted(requestLocation, oldWeather, true, index, total)

                checkToRequestNextOrCompleted()
            } else {
                requestWeatherFailed(requestLocation)
            }
        }

        override fun requestWeatherFailed(requestLocation: Location) {
            val old = locationList[index].weather
            locationList[index] = requestLocation

            listener?.onUpdateCompleted(requestLocation, old, false, index, total)

            checkToRequestNextOrCompleted()
        }

        private fun checkToRequestNextOrCompleted() {
            if (!isUpdating) {
                return
            }

            val next = index + 1
            if (next < total) {
                requestData(next, false)
                return
            }

            listener?.onPollingCompleted(locationList)
            isUpdating = false
        }
    }
}