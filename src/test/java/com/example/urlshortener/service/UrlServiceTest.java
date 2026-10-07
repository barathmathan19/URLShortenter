package com.example.urlshortener.service;

import com.example.urlshortener.entity.UrlMapping;
import com.example.urlshortener.repository.UrlRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UrlServiceTest {

    @Mock
    private UrlRepository urlRepository;

    @InjectMocks
    private UrlService urlService;

    @Test
    void encode_zeroId_returnsFirstAlphabetCharacter() {
        String result = urlService.encode(0);
        assertEquals("0", result); // Based on "0123456789abcdef..."
    }

    @Test
    void encode_largeId_returnsCorrectBase62String() {
        // 62 * 62 = 3844. E.g., ID 3844 should convert to "100" in Base62
        String result = urlService.encode(3844);
        assertEquals("100", result);
    }

    @Test
    void decode_validShortUrl_returnsCorrectId() {
        Long id = urlService.decode("100");
        assertEquals(3844, id);
    }

    @Test
    void getOriginalUrl_urlExists_returnsUrlString() {
        String shortUrl = "b"; // decode("b") -> ID 11
        UrlMapping mockMapping = new UrlMapping("https://spring.io");

        when(urlRepository.findById(11)).thenReturn(Optional.of(mockMapping));

        String result = urlService.getOriginalUrl(shortUrl);

        assertEquals("https://spring.io", result);
        verify(urlRepository, times(1)).findById(11);
    }

    @Test
    void getOriginalUrl_urlDoesNotExist_throwsRuntimeException() {
        String shortUrl = "invalid";
        Integer decodedId = Math.toIntExact(urlService.decode(shortUrl));

        when(urlRepository.findById(decodedId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            urlService.getOriginalUrl(shortUrl);
        });

        assertEquals("URL not found", exception.getMessage());
    }
}