package nowsci.com.temperateweather.db.converters

import org.greenrobot.greendao.converter.PropertyConverter
import nowsci.com.temperateweather.common.basic.models.options.provider.WeatherSource

class WeatherSourceConverter : PropertyConverter<WeatherSource, String?> {

    override fun convertToEntityProperty(databaseValue: String?) =
        // use get instance method but not getValue method.
        WeatherSource.getInstance(databaseValue ?: "")

    override fun convertToDatabaseValue(entityProperty: WeatherSource) =
        entityProperty.id
}