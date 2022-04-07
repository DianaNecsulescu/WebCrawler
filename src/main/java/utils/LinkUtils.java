package utils;

public class LinkUtils {

    public static final String LINK_TAG = "a[href]";
    public static final String ABSOLUT_PATH = "abs:href";
    private static final String DOMAIN = "https://tomblomfield.com/";

    public static boolean isValidLink(String url) {
        if (url == null || url.isEmpty()) {
            return false;
        }

        return url.startsWith(DOMAIN);
    }
}
