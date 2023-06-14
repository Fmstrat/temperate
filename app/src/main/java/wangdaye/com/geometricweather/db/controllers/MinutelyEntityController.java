package nowsci.com.temperateweather.db.controllers;

import androidx.annotation.NonNull;

import java.util.List;

import nowsci.com.temperateweather.common.basic.models.options.provider.WeatherSource;
import nowsci.com.temperateweather.db.entities.DaoSession;
import nowsci.com.temperateweather.db.entities.MinutelyEntity;
import nowsci.com.temperateweather.db.entities.MinutelyEntityDao;
import nowsci.com.temperateweather.db.converters.WeatherSourceConverter;

public class MinutelyEntityController extends AbsEntityController {

    // insert.

    public static void insertMinutelyList(@NonNull DaoSession session,
                                          @NonNull List<MinutelyEntity> entityList) {
        session.getMinutelyEntityDao().insertInTx(entityList);
    }

    // delete.

    public static void deleteMinutelyEntityList(@NonNull DaoSession session,
                                                @NonNull List<MinutelyEntity> entityList) {
        session.getMinutelyEntityDao().deleteInTx(entityList);
    }

    // select.

    public static List<MinutelyEntity> selectMinutelyEntityList(@NonNull DaoSession session,
                                                                @NonNull String cityId, @NonNull WeatherSource source) {
        return getNonNullList(
                session.getMinutelyEntityDao()
                        .queryBuilder()
                        .where(
                                MinutelyEntityDao.Properties.CityId.eq(cityId),
                                MinutelyEntityDao.Properties.WeatherSource.eq(
                                        new WeatherSourceConverter().convertToDatabaseValue(source)
                                )
                        ).list()
        );
    }
}
