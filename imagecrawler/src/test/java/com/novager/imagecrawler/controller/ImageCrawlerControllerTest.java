package com.novager.imagecrawler.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.novager.imagecrawler.dto.ImageDownloadRequest;
import com.novager.imagecrawler.dto.ImageDownloadResponse;
import com.novager.imagecrawler.dto.ImageExtractRequest;
import com.novager.imagecrawler.dto.ImageExtractResponse;
import com.novager.imagecrawler.service.ImageCrawlerService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

class ImageCrawlerControllerTest {

    private final ImageCrawlerService imageCrawlerService = Mockito.mock(ImageCrawlerService.class);
    private final ImageCrawlerController imageCrawlerController = new ImageCrawlerController(imageCrawlerService);

    @Test
    void extractEndpoint_ShouldReturnExtractedImages() {
        ImageExtractResponse response = new ImageExtractResponse(
                "https://example.com",
                2,
                List.of("https://example.com/a.jpg", "https://example.com/b.jpg")
        );
        when(imageCrawlerService.extract(any())).thenReturn(response);

        ImageExtractRequest request = new ImageExtractRequest();
        request.setUrl("https://example.com");
        request.setMaxImages(10);
        request.setIncludeExternal(false);

        ResponseEntity<ImageExtractResponse> result = imageCrawlerController.extract(request);

        assertEquals(200, result.getStatusCode().value());
        assertEquals(2, result.getBody().getTotalFound());
    }

    @Test
    void downloadEndpoint_ShouldReturnDownloadSummary() {
        ImageDownloadResponse response = new ImageDownloadResponse();
        response.setTotalRequested(2);
        response.setTotalDownloaded(1);
        response.setSavedFiles(List.of("downloads/a.jpg"));
        response.setFailedUrls(List.of("https://example.com/b.jpg"));
        when(imageCrawlerService.download(any())).thenReturn(response);

        ImageDownloadRequest request = new ImageDownloadRequest();
        request.setImageUrls(List.of("https://example.com/a.jpg", "https://example.com/b.jpg"));
        request.setTargetDirectory("downloads");
        request.setOverwrite(false);

        ResponseEntity<ImageDownloadResponse> result = imageCrawlerController.download(request);

        assertEquals(200, result.getStatusCode().value());
        assertEquals(2, result.getBody().getTotalRequested());
        assertEquals(1, result.getBody().getTotalDownloaded());
    }
}
