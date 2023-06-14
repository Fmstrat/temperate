package nowsci.com.temperateweather.weather.apis;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import nowsci.com.temperateweather.weather.json.atmoaura.AtmoAuraQAResult;

/**
 * API Atmo AURA
 * Covers Auvergne-Rhône-Alpes
 */

public interface AtmoAuraIqaApi {

    @GET("air2go/full_request")
    Observable<AtmoAuraQAResult> getQAFull(@Query("api_token") String api_token,
                                           @Query("latitude") String latitude,
                                           @Query("longitude") String longitude);

}