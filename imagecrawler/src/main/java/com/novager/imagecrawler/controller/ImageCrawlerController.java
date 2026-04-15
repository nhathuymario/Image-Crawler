package com.novager.imagecrawler.controller;

import com.novager.imagecrawler.dto.ImageDownloadRequest;
import com.novager.imagecrawler.dto.ImageDownloadResponse;
import com.novager.imagecrawler.dto.ImageExtractRequest;
import com.novager.imagecrawler.dto.ImageExtractResponse;
import com.novager.imagecrawler.service.ImageCrawlerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/images")
public class ImageCrawlerController {

    private final ImageCrawlerService imageCrawlerService;

    public ImageCrawlerController(ImageCrawlerService imageCrawlerService) {
        this.imageCrawlerService = imageCrawlerService;
    }

    @PostMapping("/extract")
    public ResponseEntity<ImageExtractResponse> extract(@Valid @RequestBody ImageExtractRequest request) {
        return ResponseEntity.ok(imageCrawlerService.extract(request));
    }

    @PostMapping("/download")
    public ResponseEntity<ImageDownloadResponse> download(@Valid @RequestBody ImageDownloadRequest request) {
        return ResponseEntity.ok(imageCrawlerService.download(request));
    }
}
