package com.novager.imagecrawler.service;

import com.novager.imagecrawler.config.CrawlerProperties;
import com.novager.imagecrawler.dto.ImageDownloadResponse;
import com.novager.imagecrawler.exception.CrawlException;
import com.novager.imagecrawler.util.FileNameUtils;
import com.novager.imagecrawler.util.ImageTypeUtils;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ImageDownloadService {

    private final HttpClient httpClient;
    private final CrawlerProperties properties;

    public ImageDownloadService(HttpClient httpClient, CrawlerProperties properties) {
        this.httpClient = httpClient;
        this.properties = properties;
    }

    public ImageDownloadResponse downloadImages(List<String> imageUrls, String targetDirectory, boolean overwrite) {
        ImageDownloadResponse response = new ImageDownloadResponse();
        response.setTotalRequested(imageUrls.size());

        Path baseDir = resolveTargetDirectory(targetDirectory);
        try {
            Files.createDirectories(baseDir);
        } catch (IOException ex) {
            throw new CrawlException("Cannot create download directory: " + baseDir, ex);
        }

        int downloaded = 0;
        int index = 1;
        for (String imageUrl : imageUrls) {
            try {
                HttpRequest request = HttpRequest.newBuilder(URI.create(imageUrl))
                        .GET()
                        .timeout(Duration.ofMillis(properties.getReadTimeoutMs()))
                        .header("User-Agent", properties.getUserAgent())
                        .build();

                HttpResponse<byte[]> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());
                String contentType = httpResponse.headers().firstValue("Content-Type").orElse(null);

                if (httpResponse.statusCode() < 200 || httpResponse.statusCode() >= 300) {
                    response.getFailedUrls().add(imageUrl);
                    continue;
                }
                if (!ImageTypeUtils.isImageContentType(contentType)) {
                    response.getFailedUrls().add(imageUrl);
                    continue;
                }

                String extension = ImageTypeUtils.extensionFromContentType(contentType);
                String fileName = FileNameUtils.safeFileName(imageUrl, extension, index++);
                Path outputFile = resolveOutputFile(baseDir, fileName, overwrite);
                Files.write(outputFile, httpResponse.body());

                response.getSavedFiles().add(outputFile.toString());
                downloaded++;
            } catch (Exception ex) {
                response.getFailedUrls().add(imageUrl);
            }
        }

        response.setTotalDownloaded(downloaded);
        return response;
    }

    private Path resolveTargetDirectory(String targetDirectory) {
        String directory = (targetDirectory == null || targetDirectory.isBlank())
                ? properties.getDownloadDir()
                : targetDirectory;
        return Paths.get(directory).toAbsolutePath().normalize();
    }

    private Path resolveOutputFile(Path baseDir, String fileName, boolean overwrite) {
        Path output = baseDir.resolve(fileName);
        if (overwrite || !Files.exists(output)) {
            return output;
        }

        int attempt = 1;
        Path candidate = output;
        while (Files.exists(candidate)) {
            candidate = baseDir.resolve(FileNameUtils.withCollisionSuffix(fileName, attempt++));
        }
        return candidate;
    }
}
