package com.example.urlshortener.service;

import com.example.urlshortener.entity.UrlMapping;
import com.example.urlshortener.repository.UrlRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class UrlService {

    private final UrlRepository urlRepository;
    private static final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int BASE = 62;
    private static final Pattern STRICT_NUMERIC_ID = Pattern.compile("^\\d+$");

    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    // CHANGED TO PUBLIC: Base62 Encoding logic so tests can access it
    public String encode(long id) {
        if (id == 0) {
            return String.valueOf(ALPHABET.charAt(0));
        }
        StringBuilder sb = new StringBuilder();
        while (id > 0) {
            sb.append(ALPHABET.charAt((int) (id % BASE)));
            id /= BASE;
        }
        return sb.reverse().toString();
    }

    // CHANGED TO PUBLIC: Base62 Decoding logic so tests can access it
    public Long decode(String str) {
        long id = 0;
        for(int i=0;i<str.length();i++){
            int index = ALPHABET.indexOf(str.charAt(i));
            if(index == -1){
                throw new IllegalArgumentException("Invalid Base62 character");
            }
            id =id * BASE + index;
        }
        return id;
    }

    public String shortenUrl(String longUrl) {
        UrlMapping savedUrl = urlRepository.save(new UrlMapping(longUrl));
        return encode(savedUrl.getId());
    }

    // Caches the literal string URL. Skips DB lookup if present in Redis.
    @Cacheable(value = "urls", key = "#shortUrl")
    public String getOriginalUrl(String shortUrl) {
        long id = decode(shortUrl);
        UrlMapping mapping = urlRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("URL not found"));
        return mapping.getOriginalUrl();
    }

    // Records the click independently of the cache lookup
    public void recordClick(String shortUrl) {
        long id = decode(shortUrl);
        urlRepository.incrementClickCount(id);
    }

    // Example of Cache Eviction for administrative deletion
    @CacheEvict(value = "urls", key = "#shortUrl")
    public void deleteShortUrl(String shortUrl) {
        long id = decode(shortUrl);
        urlRepository.deleteById(id);
    }
}