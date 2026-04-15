package com.novager.imagecrawler.util;

import java.util.Locale;

public final class ImageTypeUtils {

    private ImageTypeUtils() {
    }

    public static boolean isImageContentType(String contentType) {
        return contentType != null && contentType.toLowerCase(Locale.ROOT).startsWith("image/");
    }

    public static String extensionFromContentType(String contentType) {
        if (contentType == null || !contentType.contains("/")) {
            return ".img";
        }

        String subtype = contentType.substring(contentType.indexOf('/') + 1);
        int semicolon = subtype.indexOf(';');
        if (semicolon > -1) {
            subtype = subtype.substring(0, semicolon);
        }
        subtype = subtype.trim().toLowerCase(Locale.ROOT);

        return switch (subtype) {
            case "jpeg" -> ".jpg";
            case "svg+xml" -> ".svg";
            default -> "." + subtype;
        };
    }

    public static boolean looksLikeImageUrl(String url) {
        String normalized = url == null ? "" : url.toLowerCase(Locale.ROOT);
        return normalized.matches(".*\\.(png|jpe?g|gif|webp|bmp|svg|avif)([?#].*)?$");
    }
}
