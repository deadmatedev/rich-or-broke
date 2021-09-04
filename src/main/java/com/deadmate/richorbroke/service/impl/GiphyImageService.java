package com.deadmate.richorbroke.service.impl;

import com.deadmate.richorbroke.client.GiphyClient;
import com.deadmate.richorbroke.config.GiphyClientConfig;
import com.deadmate.richorbroke.exception.ApiException;
import com.deadmate.richorbroke.model.Image;
import com.deadmate.richorbroke.model.giphy.GiphyImage;
import com.deadmate.richorbroke.model.giphy.GiphySearchResult;
import com.deadmate.richorbroke.model.giphy.GiphySearchResults;
import com.deadmate.richorbroke.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class GiphyImageService implements ImageService {

    private final GiphyClient client;
    private final Random random = new Random();
    private final GiphyClientConfig config;

    @Override
    public Image getRandomImage(String query) {
        GiphySearchResults results = client.searchGifs(query, random.nextInt(config.getMaxRandomOffset()), 1, config.getApiKey());
        GiphySearchResult result = Optional.ofNullable(results)
                .map(GiphySearchResults::getData)
                .map(data -> data.isEmpty() ? null : data.get(0))
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,
                        "Giphy brought no search results for query: " + query));
        GiphyImage original = result.getImages().getOriginal();
        return new Image(
                result.getId(),
                config.getImageUrlFormat().replace("%", result.getId()),
                Long.parseLong(original.getSize()),
                original.getHash());
    }
}
