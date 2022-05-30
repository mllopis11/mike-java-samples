package mike.samples.utilities;

public class Utils {

    private Utils() {
    }

    /**
     * @param value    value to strip
     * @param defValue default value if value is null
     * @return value with any leading and trailing whitespace or empty string if
     *         null
     */
    public static String strip(String value, String... defValue) {
	var str = value != null ? value.strip() : "";
	return !str.isEmpty() ? str : Utils.defautValue(defValue);
    }

    /**
     * @param defValue optional default value
     * @return return the default value if present otherwise an empty value
     */
    private static String defautValue(String... defValue) {
	return defValue.length > 0 && defValue[0] != null ? defValue[0] : "";
    }

    /**
     * Remove the part of the exception message preceeding the colon (:)
     * 
     * @param ex an exception
     * @return the sanitized exception message or default message if the exception
     *         message is null
     */
    static String sanitizeMessage(Exception ex) {
	return ex.getMessage() != null ? ex.getMessage().replaceAll("(\\w+\\.):", "") : "no such root cause found";
    }
}
