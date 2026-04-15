package com.novager.imagecrawler.util;

import java.net.URI;
import java.util.UUID;

public final class FileNameUtils {

    private FileNameUtils() {
    }

    public static String safeFileName(String imageUrl, String extension, int index) {
        String baseName = "img_" + index;
        try {
            URI uri = URI.create(imageUrl);
            String path = uri.getPath();
            if (path != null && !path.isBlank()) {
                String candidate = path.substring(path.lastIndexOf('/') + 1);
                if (!candidate.isBlank()) {
                    baseName = sanitize(candidate);
                }
            }
        } catch (IllegalArgumentException ignored) {
            // Keep fallback name.
        }

        if (!baseName.contains(".")) {
            baseName = baseName + extension;
        }
        return baseName;
    }

    public static String withCollisionSuffix(String fileName, int attempt) {
        String dotSuffix;
        String filePart;
        int dot = fileName.lastIndexOf('.');
        if (dot > 0) {
            filePart = fileName.substring(0, dot);
            dotSuffix = fileName.substring(dot);
        } else {
            filePart = fileName;
            dotSuffix = "";
        }
        return filePart + "_" + attempt + dotSuffix;
    }

    public static String randomName(String extension) {
        return UUID.randomUUID().toString().replace("-", "") + extension;
    }

    private static String sanitize(String value) {
        String sanitized = value.replaceAll("[^a-zA-Z0-9._-]", "_");
        return sanitized.length() > 120 ? sanitized.substring(0, 120) : sanitized;
    }
}
