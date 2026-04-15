package com.novager.imagecrawler.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class ImageExtractRequest {

    @NotBlank(message = "url is required")
    private String url;

    @Positive(message = "maxImages must be > 0")
    private Integer maxImages;

    private Boolean includeExternal = Boolean.FALSE;

    private Boolean productOnly = Boolean.TRUE;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getMaxImages() {
        return maxImages;
    }

    public void setMaxImages(Integer maxImages) {
        this.maxImages = maxImages;
    }

    public Boolean getIncludeExternal() {
        return includeExternal;
    }

    public void setIncludeExternal(Boolean includeExternal) {
        this.includeExternal = includeExternal;
    }

    public Boolean getProductOnly() {
        return productOnly;
    }

    public void setProductOnly(Boolean productOnly) {
        this.productOnly = productOnly;
    }
}
