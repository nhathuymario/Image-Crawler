package com.novager.imagecrawler.service;

import com.novager.imagecrawler.config.CrawlerProperties;
import com.novager.imagecrawler.exception.CrawlException;
import com.novager.imagecrawler.util.UrlUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

@Component
public class HtmlImageExtractor {

    private final CrawlerProperties properties;

    public HtmlImageExtractor(CrawlerProperties properties) {
        this.properties = properties;
    }

        public List<String> extractImageUrls(String pageUrl, int maxImages, boolean includeExternal, boolean productOnly) {
        try {
            Document document = Jsoup.connect(pageUrl)
                    .userAgent(properties.getUserAgent())
                    .timeout(properties.getReadTimeoutMs())
                    .get();

            Map<String, Integer> scoredUrls = new LinkedHashMap<>();
            collectAttributeUrls(document, pageUrl, "img[src]", "src", includeExternal, scoredUrls);
            collectAttributeUrls(document, pageUrl, "img[data-src]", "data-src", includeExternal, scoredUrls);
            collectAttributeUrls(document, pageUrl, "source[src]", "src", includeExternal, scoredUrls);
            collectSrcSetUrls(document, pageUrl, "img[srcset]", "srcset", includeExternal, scoredUrls);
            collectSrcSetUrls(document, pageUrl, "source[srcset]", "srcset", includeExternal, scoredUrls);

            List<Map.Entry<String, Integer>> ranked = scoredUrls.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .toList();

            List<String> productRanked = ranked.stream()
                .filter(entry -> entry.getValue() >= 1)
                .map(Map.Entry::getKey)
                .toList();

            List<String> ordered = productOnly
                ? (!productRanked.isEmpty() ? productRanked : ranked.stream().map(Map.Entry::getKey).toList())
                : ranked.stream().map(Map.Entry::getKey).toList();

            return ordered.stream().limit(maxImages).toList();
        } catch (IOException ex) {
            throw new CrawlException("Cannot fetch page: " + pageUrl, ex);
        }
    }

    private void collectAttributeUrls(Document document,
                                      String pageUrl,
                                      String selector,
                                      String attribute,
                                      boolean includeExternal,
                                      Map<String, Integer> sink) {
        for (Element element : document.select(selector)) {
            String raw = element.attr(attribute).trim();
            addCandidate(pageUrl, raw, includeExternal, element, sink, null);
        }
    }

    private void collectSrcSetUrls(Document document,
                                   String pageUrl,
                                   String selector,
                                   String attribute,
                                   boolean includeExternal,
                                   Map<String, Integer> sink) {
        for (Element element : document.select(selector)) {
            String srcSet = element.attr(attribute);
            if (srcSet.isBlank()) {
                continue;
            }
            String[] candidates = srcSet.split(",");
            for (String candidate : candidates) {
                String[] parts = candidate.trim().split("\\s+");
                String raw = parts[0];
                String descriptor = parts.length > 1 ? parts[1] : "";
                addCandidate(pageUrl, raw, includeExternal, element, sink, descriptor);
            }
        }
    }

    private void addCandidate(String pageUrl,
                              String raw,
                              boolean includeExternal,
                              Element element,
                              Map<String, Integer> sink,
                              String srcSetDescriptor) {
        if (raw == null || raw.isBlank() || raw.startsWith("data:")) {
            return;
        }

        String absolute = UrlUtils.toAbsoluteUrl(pageUrl, raw);
        if (!UrlUtils.isHttpUrl(absolute)) {
            return;
        }
        if (!includeExternal && !UrlUtils.sameHost(pageUrl, absolute)) {
            return;
        }

        int score = calculateProductScore(absolute, element, srcSetDescriptor);
        sink.merge(absolute, score, Math::max);
    }

    private int calculateProductScore(String imageUrl, Element element, String srcSetDescriptor) {
        int score = 0;
        String url = imageUrl.toLowerCase(Locale.ROOT);
        String context = buildContext(element);

        if (containsAny(url, "product", "prod", "detail", "sku", "item", "sp", "catalog")) {
            score += 4;
        }
        if (containsAny(url, "jpg", "jpeg", "webp", "avif", "png")) {
            score += 1;
        }
        if (containsAny(url, "icon", "logo", "sprite", "favicon", "emoji", "badge", "tracking", "pixel")) {
            score -= 5;
        }
        if (containsAny(context, "product", "price", "item", "sku", "thumb", "gallery")) {
            score += 3;
        }
        if (containsAny(context, "icon", "logo", "social", "brand", "menu", "avatar", "placeholder")) {
            score -= 4;
        }
        if (url.endsWith(".svg")) {
            score -= 3;
        }

        int width = parseDimension(element, "width", "data-width");
        int height = parseDimension(element, "height", "data-height");
        int largest = Math.max(width, height);
        if (largest > 0 && largest <= 90) {
            score -= 5;
        }
        if (largest >= 240) {
            score += 3;
        }

        int descriptorWidth = parseSrcSetWidth(srcSetDescriptor);
        if (descriptorWidth >= 240) {
            score += 3;
        }
        if (descriptorWidth > 0 && descriptorWidth <= 90) {
            score -= 4;
        }

        return score;
    }

    private String buildContext(Element element) {
        List<String> fields = new ArrayList<>();
        fields.add(element.attr("class"));
        fields.add(element.attr("id"));
        fields.add(element.attr("alt"));
        fields.add(element.attr("title"));
        fields.add(element.attr("data-name"));
        fields.add(element.attr("aria-label"));
        return String.join(" ", fields).toLowerCase(Locale.ROOT);
    }

    private boolean containsAny(String text, String... terms) {
        for (String term : terms) {
            if (text.contains(term)) {
                return true;
            }
        }
        return false;
    }

    private int parseDimension(Element element, String... attrs) {
        for (String attr : attrs) {
            String value = element.attr(attr);
            if (value == null || value.isBlank()) {
                continue;
            }
            String normalized = value.replaceAll("[^0-9]", "");
            if (normalized.isBlank()) {
                continue;
            }
            try {
                return Integer.parseInt(normalized);
            } catch (NumberFormatException ignored) {
                // Keep trying other attributes.
            }
        }
        return 0;
    }

    private int parseSrcSetWidth(String descriptor) {
        if (descriptor == null || descriptor.isBlank()) {
            return 0;
        }
        String trimmed = descriptor.trim().toLowerCase(Locale.ROOT);
        if (!trimmed.endsWith("w")) {
            return 0;
        }
        String numeric = trimmed.substring(0, trimmed.length() - 1).replaceAll("[^0-9]", "");
        if (numeric.isBlank()) {
            return 0;
        }
        try {
            return Integer.parseInt(numeric);
        } catch (NumberFormatException ignored) {
            return 0;
        }
    }
}
