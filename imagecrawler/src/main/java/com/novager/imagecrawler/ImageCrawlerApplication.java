package com.novager.imagecrawler;

import com.novager.imagecrawler.config.CrawlerProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(CrawlerProperties.class)
public class    ImageCrawlerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImageCrawlerApplication.class, args);
    }

}
