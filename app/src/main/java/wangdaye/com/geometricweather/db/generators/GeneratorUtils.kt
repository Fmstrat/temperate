package nowsci.com.temperateweather.db.generators

object GeneratorUtils {

    @JvmStatic
    fun nonNull(string: String?): String {
        return string ?: ""
    }
}