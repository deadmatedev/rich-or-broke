package com.deadmate.richorbroke.controller;

import com.deadmate.richorbroke.model.Image;
import com.deadmate.richorbroke.service.StreamingService;
import com.deadmate.richorbroke.service.TrendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestController
@RequiredArgsConstructor
public class TrendController {

    private final TrendService trendService;
    private final StreamingService streamingService;

    @GetMapping(path = "/trend/{currency_code}")
    public ResponseEntity<StreamingResponseBody> getTrend(@PathVariable("currency_code") String currencyCode) {
        Image image = trendService.getTrend(currencyCode);
        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_GIF)
                .contentLength(image.getSize())
                .cacheControl(CacheControl.noCache())
                .body(streamingService.getStreamFrom(image.getUrl()));
    }
}
