package com.deadmate.richorbroke.service.impl;

import com.deadmate.richorbroke.config.AsyncStreamingServiceConfig;
import com.deadmate.richorbroke.service.StreamingService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.InputStream;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class AsyncStreamingService implements StreamingService {

    private final AsyncStreamingServiceConfig config;

    @Override
    public StreamingResponseBody getStreamFrom(final String url) {
        return outputStream -> {
            try (InputStream inputStream = new URL(url).openStream(); outputStream) {
                IOUtils.copyLarge(inputStream, outputStream, new byte [config.getBufferSize()]);
                outputStream.flush();
            }
        };
    }
}
