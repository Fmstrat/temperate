package nowsci.com.temperateweather.db.converters;

import org.greenrobot.greendao.converter.PropertyConverter;

import nowsci.com.temperateweather.common.basic.models.weather.WeatherCode;

public class WeatherCodeConverter implements PropertyConverter<WeatherCode, String> {

    @Override
    public WeatherCode convertToEntityProperty(String databaseValue) {
        // use get instance method but not getValue method.
        return WeatherCode.getInstance(databaseValue);
    }

    @Override
    public String convertToDatabaseValue(WeatherCode entityProperty) {
        return entityProperty.getId();
    }
}
