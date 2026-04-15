package com.novager.imagecrawler.service;

import java.util.List;

public interface DynamicImageExtractor {

    default List<String> extractImageUrls(String pageUrl, int maxImages, boolean includeExternal) {
        return List.of();
    }
}
