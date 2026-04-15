package com.novager.imagecrawler.dto;

import java.util.ArrayList;
import java.util.List;

public class ImageDownloadResponse {

    private int totalRequested;
    private int totalDownloaded;
    private List<String> savedFiles = new ArrayList<>();
    private List<String> failedUrls = new ArrayList<>();

    public int getTotalRequested() {
        return totalRequested;
    }

    public void setTotalRequested(int totalRequested) {
        this.totalRequested = totalRequested;
    }

    public int getTotalDownloaded() {
        return totalDownloaded;
    }

    public void setTotalDownloaded(int totalDownloaded) {
        this.totalDownloaded = totalDownloaded;
    }

    public List<String> getSavedFiles() {
        return savedFiles;
    }

    public void setSavedFiles(List<String> savedFiles) {
        this.savedFiles = savedFiles;
    }

    public List<String> getFailedUrls() {
        return failedUrls;
    }

    public void setFailedUrls(List<String> failedUrls) {
        this.failedUrls = failedUrls;
    }
}
