package wangdaye.com.geometricweather.location.utils;

public class LocationException extends Exception {

    public LocationException(int code, String msg) {
        super(msg + "(code = " + code + ")");
    }
}
