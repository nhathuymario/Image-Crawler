package com.novager.imagecrawler.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.novager.imagecrawler.config.CrawlerProperties;
import com.novager.imagecrawler.dto.ImageDownloadRequest;
import com.novager.imagecrawler.dto.ImageDownloadResponse;
import com.novager.imagecrawler.dto.ImageExtractRequest;
import com.novager.imagecrawler.dto.ImageExtractResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ImageCrawlerServiceTest {

    @Mock
    private HtmlImageExtractor htmlImageExtractor;

    @Mock
    private ImageDownloadService imageDownloadService;

    private ImageCrawlerService imageCrawlerService;

    @BeforeEach
    void setUp() {
        CrawlerProperties properties = new CrawlerProperties();
        properties.setMaxImagesPerPage(50);
        imageCrawlerService = new ImageCrawlerService(htmlImageExtractor, imageDownloadService, properties);
    }

    @Test
    void extract_ShouldReturnFoundImages() {
        ImageExtractRequest request = new ImageExtractRequest();
        request.setUrl("https://example.com");
        request.setIncludeExternal(false);
        request.setProductOnly(true);

        when(htmlImageExtractor.extractImageUrls("https://example.com", 50, false, true))
                .thenReturn(List.of("https://example.com/a.jpg", "https://example.com/b.png"));

        ImageExtractResponse response = imageCrawlerService.extract(request);

        assertEquals(2, response.getTotalFound());
        assertEquals(2, response.getImageUrls().size());
    }

    @Test
    void download_ShouldDelegateToDownloadService() {
        ImageDownloadRequest request = new ImageDownloadRequest();
        request.setImageUrls(List.of("https://example.com/a.jpg"));
        request.setTargetDirectory("downloads");

        ImageDownloadResponse expected = new ImageDownloadResponse();
        expected.setTotalRequested(1);
        expected.setTotalDownloaded(1);

        when(imageDownloadService.downloadImages(request.getImageUrls(), "downloads", false))
                .thenReturn(expected);

        ImageDownloadResponse response = imageCrawlerService.download(request);

        assertEquals(1, response.getTotalRequested());
        assertEquals(1, response.getTotalDownloaded());
    }
}
