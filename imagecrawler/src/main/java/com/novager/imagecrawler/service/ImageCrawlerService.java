package com.novager.imagecrawler.service;

import com.novager.imagecrawler.config.CrawlerProperties;
import com.novager.imagecrawler.dto.ImageDownloadRequest;
import com.novager.imagecrawler.dto.ImageDownloadResponse;
import com.novager.imagecrawler.dto.ImageExtractRequest;
import com.novager.imagecrawler.dto.ImageExtractResponse;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ImageCrawlerService {

    private final HtmlImageExtractor htmlImageExtractor;
    private final ImageDownloadService imageDownloadService;
    private final CrawlerProperties properties;

    public ImageCrawlerService(HtmlImageExtractor htmlImageExtractor,
                               ImageDownloadService imageDownloadService,
                               CrawlerProperties properties) {
        this.htmlImageExtractor = htmlImageExtractor;
        this.imageDownloadService = imageDownloadService;
        this.properties = properties;
    }

    public ImageExtractResponse extract(ImageExtractRequest request) {
        int maxImages = request.getMaxImages() != null
                ? request.getMaxImages()
                : properties.getMaxImagesPerPage();
        boolean includeExternal = Boolean.TRUE.equals(request.getIncludeExternal());
        boolean productOnly = !Boolean.FALSE.equals(request.getProductOnly());

        List<String> imageUrls = htmlImageExtractor.extractImageUrls(
            request.getUrl(),
            maxImages,
            includeExternal,
            productOnly
        );
        return new ImageExtractResponse(request.getUrl(), imageUrls.size(), imageUrls);
    }

    public ImageDownloadResponse download(ImageDownloadRequest request) {
        return imageDownloadService.downloadImages(
                request.getImageUrls(),
                request.getTargetDirectory(),
                request.isOverwrite()
        );
    }
}
