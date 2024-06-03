package team.sugarsmile.cprms.util;

public class URLMatcherUtil {
    public static String convertPatternToRegex(String urlPattern) {
        StringBuilder regex = new StringBuilder();
        regex.append("^");

        for (int i = 0; i < urlPattern.length(); i++) {
            char c = urlPattern.charAt(i);
            switch (c) {
                case '*' -> regex.append(".*");
                case '.' -> regex.append("\\.");
                default -> regex.append(c);
            }
        }

        regex.append("$");
        return regex.toString();
    }
}