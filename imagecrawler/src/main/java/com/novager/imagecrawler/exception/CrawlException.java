package com.novager.imagecrawler.exception;

public class CrawlException extends RuntimeException {

    public CrawlException(String message) {
        super(message);
    }

    public CrawlException(String message, Throwable cause) {
        super(message, cause);
    }
}
