package com.example.urlshortener.service;

import com.example.urlshortener.entity.UrlMapping;
import com.example.urlshortener.repository.UrlRepository;
import org.springframework.stereotype.Service;

@Service
public class UrlService {

    private final UrlRepository urlRepository;
    private static final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int BASE = 62;

    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public String encode(Integer id) {
        if (id == 0) return String.valueOf(ALPHABET.charAt(0));

        StringBuilder shortUrl = new StringBuilder();
        while (id > 0) {
            int remainder = id % BASE;
            shortUrl.append(ALPHABET.charAt(remainder));
            id = id / BASE;
        }
        return shortUrl.reverse().toString();
    }

    public Integer decode(String shortUrl) {
        int id = 0;
        for (int i = 0; i < shortUrl.length(); i++) {
            char c = shortUrl.charAt(i);
            int value = ALPHABET.indexOf(c);
            id = (id * BASE) + value;
        }
        return id;
    }

    public String shortenUrl(String longUrl) {
        UrlMapping newUrl = new UrlMapping(longUrl);
        UrlMapping savedUrl = urlRepository.save(newUrl);
        return encode(savedUrl.getId());
    }

    public String getOriginalUrl(String shortUrl) {
        Integer id = decode(shortUrl);
        UrlMapping mapping = urlRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("URL not found"));
        return mapping.getOriginalUrl();
    }
}