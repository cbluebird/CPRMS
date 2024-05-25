package team.sugarsmile.cprms.util;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

    public class URLMatcher {
        public static void main(String[] args) {
            String urlPattern = "/auth/*";
            String urlToMatch = "/auth/login";

            // Convert the URL pattern to a regular expression
            String regex = convertPatternToRegex(urlPattern);

            // Compile the regex
            Pattern pattern = Pattern.compile(regex);

            // Match the URL
            Matcher matcher = pattern.matcher(urlToMatch);

            if (matcher.matches()) {
                System.out.println("URL matches the pattern.");
            } else {
                System.out.println("URL does not match the pattern.");
            }
        }

        public static String convertPatternToRegex(String urlPattern) {
            StringBuffer regex = new StringBuffer();
            regex.append("^"); // Start of the line

            for (int i = 0; i < urlPattern.length(); i++) {
                char c = urlPattern.charAt(i);
                switch (c) {
                    case '*':
                        regex.append(".*"); // Match any character (0 or more times)
                        break;
                    case '.':
                        regex.append("\\."); // Escape dot
                        break;
                    default:
                        regex.append(c); // Append the character as it is
                        break;
                }
            }

            regex.append("$"); // End of the line
            return regex.toString();
        }
    }