package com.novager.imagecrawler.util;

import java.net.URI;

public final class UrlUtils {

    private UrlUtils() {
    }

    public static boolean isHttpUrl(String rawUrl) {
        try {
            URI uri = URI.create(rawUrl);
            String scheme = uri.getScheme();
            return "http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme);
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    public static String toAbsoluteUrl(String baseUrl, String maybeRelative) {
        try {
            URI base = URI.create(baseUrl);
            return base.resolve(maybeRelative).toString();
        } catch (IllegalArgumentException ex) {
            return maybeRelative;
        }
    }

    public static boolean sameHost(String left, String right) {
        try {
            URI leftUri = URI.create(left);
            URI rightUri = URI.create(right);
            return leftUri.getHost() != null
                    && leftUri.getHost().equalsIgnoreCase(rightUri.getHost());
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}
