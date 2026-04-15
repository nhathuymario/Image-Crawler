package com.novager.imagecrawler.dto;

import java.util.List;

public class ImageExtractResponse {

    private String sourceUrl;
    private int totalFound;
    private List<String> imageUrls;

    public ImageExtractResponse() {
    }

    public ImageExtractResponse(String sourceUrl, int totalFound, List<String> imageUrls) {
        this.sourceUrl = sourceUrl;
        this.totalFound = totalFound;
        this.imageUrls = imageUrls;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public int getTotalFound() {
        return totalFound;
    }

    public void setTotalFound(int totalFound) {
        this.totalFound = totalFound;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}
