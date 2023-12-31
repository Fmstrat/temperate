package nowsci.com.temperateweather.theme.resource;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import nowsci.com.temperateweather.TemperateWeather;
import nowsci.com.temperateweather.theme.resource.providers.ChronusResourceProvider;
import nowsci.com.temperateweather.theme.resource.providers.DefaultResourceProvider;
import nowsci.com.temperateweather.theme.resource.providers.IconPackResourcesProvider;
import nowsci.com.temperateweather.theme.resource.providers.PixelResourcesProvider;
import nowsci.com.temperateweather.theme.resource.providers.ResourceProvider;
import nowsci.com.temperateweather.settings.SettingsManager;

public class ResourcesProviderFactory {

    public static ResourceProvider getNewInstance() {
        return getNewInstance(
                SettingsManager
                        .getInstance(TemperateWeather.getInstance())
                        .getIconProvider()
        );
    }

    public static ResourceProvider getNewInstance(String packageName) {
        Context context = TemperateWeather.getInstance();

        DefaultResourceProvider defaultProvider = new DefaultResourceProvider();

        if (DefaultResourceProvider.isDefaultIconProvider(packageName)) {
            return defaultProvider;
        }

        if (PixelResourcesProvider.isPixelIconProvider(packageName)) {
            return new PixelResourcesProvider(defaultProvider);
        }

        if (IconPackResourcesProvider.isIconPackIconProvider(context, packageName)) {
            return new IconPackResourcesProvider(context, packageName, defaultProvider);
        }

        if (ChronusResourceProvider.isChronusIconProvider(context, packageName)) {
            return new ChronusResourceProvider(context, packageName, defaultProvider);
        }

        return new IconPackResourcesProvider(context, packageName, defaultProvider);
    }

    public static List<ResourceProvider> getProviderList(Context context) {
        List<ResourceProvider> providerList = new ArrayList<>();

        DefaultResourceProvider defaultProvider = new DefaultResourceProvider();

        providerList.add(defaultProvider);
        providerList.add(new PixelResourcesProvider(defaultProvider));

        // temperate weather icon provider.
        providerList.addAll(IconPackResourcesProvider.getProviderList(context, defaultProvider));

        // chronus icon pack.
        List<ChronusResourceProvider> chronusIconPackList
                = ChronusResourceProvider.getProviderList(context, defaultProvider);
        for (int i = chronusIconPackList.size() - 1; i >= 0; i --) {
            for (int j = 0; j < providerList.size(); j ++) {
                if (chronusIconPackList.get(i).equals(providerList.get(j))) {
                    chronusIconPackList.remove(i);
                    break;
                }
            }
        }
        providerList.addAll(chronusIconPackList);

        return providerList;
    }
}
