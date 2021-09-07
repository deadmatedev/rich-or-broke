package com.deadmate.richorbroke.service.impl;

import com.deadmate.richorbroke.service.StreamingService;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.InputStream;
import java.net.URL;

@Service
public class AsyncStreamingService implements StreamingService {

    @Override
    public StreamingResponseBody getStreamFrom(final String url) {
        return outputStream -> {
            try (InputStream inputStream = new URL(url).openStream()) {
                IOUtils.copyLarge(inputStream, outputStream, new byte[1024 * 64]);
                outputStream.close();
            }
        };
    }
}
