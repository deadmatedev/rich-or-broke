package com.deadmate.richorbroke.client;

import com.deadmate.richorbroke.model.giphy.GiphySearchResults;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "giphy",  url = "${client.config.giphy.url}")
public interface GiphyClient {

    // https://api.giphy.com/v1/gifs/search?q=broke&offset=0&limit=1&api_key=brGQqlRAgW2GH0KfKRycU5WcUraHGnkA
    // https://i.giphy.com/media/ZGH8VtTZMmnwzsYYMf/giphy.gif
    @Cacheable(value = "giphy", key = "#offset + #query")
    @GetMapping(value = "/gifs/search?q={query}&offset={offset}&limit={limit}&api_key={api_key}")
    GiphySearchResults searchGifs(@PathVariable("query") String query,
                                  @PathVariable("offset") int offset,
                                  @PathVariable("limit") int limit,
                                  @PathVariable("api_key") String apiKey);
}
