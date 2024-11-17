package org.example.travelexpertsproductjsp.utils;

public class InputSanitizer {
    /*private static final PolicyFactory policy = new HtmlPolicyBuilder()
            .allowElements("b", "i", "u", "a") // Allow certain HTML elements
            .allowAttributes("href").onElements("a") // Allow "href" attribute on "a" elements
            .toFactory();*/

    public static String sanitize(String input) {
        if (input == null) {
            return null;
        }
        // Sanitize the input using the defined policy
        //return policy.sanitize(input);
        // Replace special characters with their HTML entity equivalents
        return input
                .replaceAll("&", "&amp;")
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;")
                .replaceAll("\"", "&quot;")
                .replaceAll("'", "&#x27;");
    }
}
