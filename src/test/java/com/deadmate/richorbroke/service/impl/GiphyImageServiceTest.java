package com.deadmate.richorbroke.service.impl;

import com.deadmate.richorbroke.client.GiphyClient;
import com.deadmate.richorbroke.config.GiphyClientConfig;
import com.deadmate.richorbroke.exception.ApiException;
import com.deadmate.richorbroke.model.Image;
import com.deadmate.richorbroke.model.giphy.GiphyImage;
import com.deadmate.richorbroke.model.giphy.GiphyImages;
import com.deadmate.richorbroke.model.giphy.GiphySearchResult;
import com.deadmate.richorbroke.model.giphy.GiphySearchResults;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GiphyImageServiceTest {

    @Mock
    private GiphyClient client;
    @Mock
    private GiphyClientConfig config;
    @InjectMocks
    private GiphyImageService service;

    @Test
    void givenValidQuery_shouldReturnAGifImage() {
        // given
        String query = "lol";

        when(config.getMaxRandomOffset()).thenReturn(10);
        when(config.getApiKey()).thenReturn("zzz");
        when(config.getImageUrlFormat()).thenReturn("https://%.gif");

        Random random = Mockito.mock(Random.class);
        when(random.nextInt(config.getMaxRandomOffset())).thenReturn(5);
        ReflectionTestUtils.setField(service, "random", random);

        GiphyImage image = new GiphyImage();
        image.setSize("256");
        image.setHash("9300f0d7098");
        GiphyImages images = new GiphyImages();
        images.setOriginal(image);
        GiphySearchResult searchResult = new GiphySearchResult();
        searchResult.setId("opwrjpoasfd");
        searchResult.setImages(images);
        GiphySearchResults searchResults = new GiphySearchResults();
        searchResults.setData(Collections.singletonList(searchResult));

        when(client.searchGifs(query, 5, 1, config.getApiKey()))
                .thenReturn(searchResults);
        // when
        Image result = service.getRandomImage(query);
        // then
        assertEquals("opwrjpoasfd", result.getId());
        assertEquals("https://opwrjpoasfd.gif", result.getUrl());
        assertEquals("9300f0d7098", result.getHash());
        assertEquals(256L, result.getSize());
    }

    @Test
    void givenInvalidQuery_shouldThrowApiExceptionNotFound() {
        // given
        String query = "";

        when(config.getMaxRandomOffset()).thenReturn(10);
        when(config.getApiKey()).thenReturn("zzz");

        Random random = Mockito.mock(Random.class);
        when(random.nextInt(config.getMaxRandomOffset())).thenReturn(8);
        ReflectionTestUtils.setField(service, "random", random);

        GiphySearchResults searchResults = new GiphySearchResults();
        searchResults.setData(Collections.emptyList());

        when(client.searchGifs(query, 8, 1, config.getApiKey()))
                .thenReturn(searchResults);
        // when
        ApiException e = assertThrows(ApiException.class,
                () -> service.getRandomImage(query));
        // then
        assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
        assertEquals("Giphy brought no search results for query: ", e.getMessage());
    }
}