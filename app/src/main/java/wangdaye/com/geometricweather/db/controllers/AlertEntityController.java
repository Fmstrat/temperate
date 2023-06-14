package nowsci.com.temperateweather.db.controllers;

import androidx.annotation.NonNull;

import java.util.List;

import nowsci.com.temperateweather.common.basic.models.options.provider.WeatherSource;
import nowsci.com.temperateweather.db.entities.AlertEntity;
import nowsci.com.temperateweather.db.entities.AlertEntityDao;
import nowsci.com.temperateweather.db.entities.DaoSession;
import nowsci.com.temperateweather.db.converters.WeatherSourceConverter;

public class AlertEntityController extends AbsEntityController {

    // insert.

    public static void insertAlertList(@NonNull DaoSession session,
                                @NonNull List<AlertEntity> entityList) {
        session.getAlertEntityDao().insertInTx(entityList);
    }

    // delete.

    public static void deleteAlertList(@NonNull DaoSession session,
                                @NonNull List<AlertEntity> entityList) {
        session.getAlertEntityDao().deleteInTx(entityList);
    }

    // search.

    public static List<AlertEntity> selectLocationAlertEntity(@NonNull DaoSession session,
                                                              @NonNull String cityId,
                                                              @NonNull WeatherSource source) {
        return getNonNullList(
                session.getAlertEntityDao()
                        .queryBuilder()
                        .where(
                                AlertEntityDao.Properties.CityId.eq(cityId),
                                AlertEntityDao.Properties.WeatherSource.eq(
                                        new WeatherSourceConverter().convertToDatabaseValue(source)
                                )
                        ).list()
        );
    }
}
