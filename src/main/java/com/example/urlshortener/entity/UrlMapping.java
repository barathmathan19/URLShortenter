package com.example.urlshortener.entity;

import jakarta.persistence.*;

@Entity
public class UrlMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "url_sep")
    @SequenceGenerator(name = "url_seq", sequenceName = "url_mapping_sequence", allocationSize = 50)
    private Long id;

    private String originalUrl;

    public UrlMapping() {}

    public UrlMapping(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public Long getId() {
        return id;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    // Inside com.example.urlshortener.entity.UrlMapping

    @Column(nullable = false)
    private Long clickCount = 0L;

    public Long getClickCount() { return clickCount; }
    public void setClickCount(Long clickCount) { this.clickCount = clickCount; }
}