package com.deadmate.richorbroke.service.impl;

import com.deadmate.richorbroke.service.StreamingService;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AsyncStreamingServiceTest {

    private final StreamingService service =
            new AsyncStreamingService();

    @Test
    void givenInMemoryUrlStream_whenStreamingFromUrl_shouldReturnGIFBytes() throws IOException  {
        // given
        URL.setURLStreamHandlerFactory(protocol -> "inmemory".equals(protocol)
            ? new URLStreamHandler() {
                protected URLConnection openConnection(URL url) {
                    return new URLConnection(url) {
                        public void connect() {
                            // no implementation needed
                        }
                        public InputStream getInputStream() {
                            return new ByteArrayInputStream("GIF".getBytes(StandardCharsets.UTF_8));
                        }
                    };
                }
            }
            : null);
        // when
        StreamingResponseBody body = service.getStreamFrom("inmemory:broke.gif");
        // then
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        body.writeTo(outputStream);
        assertEquals("GIF", outputStream.toString(StandardCharsets.UTF_8));
    }
}