package com.novager.imagecrawler.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "crawler")
public class CrawlerProperties {

    private String userAgent = "Mozilla/5.0 (compatible; ImageCrawlerBot/1.0)";
    private int connectTimeoutMs = 5000;
    private int readTimeoutMs = 10000;
    private int maxImagesPerPage = 100;
    private String downloadDir = "downloads";

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public int getConnectTimeoutMs() {
        return connectTimeoutMs;
    }

    public void setConnectTimeoutMs(int connectTimeoutMs) {
        this.connectTimeoutMs = connectTimeoutMs;
    }

    public int getReadTimeoutMs() {
        return readTimeoutMs;
    }

    public void setReadTimeoutMs(int readTimeoutMs) {
        this.readTimeoutMs = readTimeoutMs;
    }

    public int getMaxImagesPerPage() {
        return maxImagesPerPage;
    }

    public void setMaxImagesPerPage(int maxImagesPerPage) {
        this.maxImagesPerPage = maxImagesPerPage;
    }

    public String getDownloadDir() {
        return downloadDir;
    }

    public void setDownloadDir(String downloadDir) {
        this.downloadDir = downloadDir;
    }
}
