package nowsci.com.temperateweather.common.di;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import nowsci.com.temperateweather.db.DatabaseHelper;
import nowsci.com.temperateweather.settings.SettingsManager;

@InstallIn(SingletonComponent.class)
@Module
public class UtilsModule {

    @Provides
    public DatabaseHelper provideDatabaseHelper(@ApplicationContext Context context) {
        return DatabaseHelper.getInstance(context);
    }

    @Provides
    public SettingsManager provideSettingsOptionManager(@ApplicationContext Context context) {
        return SettingsManager.getInstance(context);
    }
}
