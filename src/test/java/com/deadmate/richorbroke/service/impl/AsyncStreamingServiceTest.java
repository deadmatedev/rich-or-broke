package com.deadmate.richorbroke.service.impl;

import com.deadmate.richorbroke.config.AsyncStreamingServiceConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AsyncStreamingServiceTest {

    @Mock
    private AsyncStreamingServiceConfig config;
    @InjectMocks
    private AsyncStreamingService service;

    @Test
    void givenInMemoryUrlStream_whenStreamingFromUrl_shouldReturnGIFBytes() throws IOException  {
        // given
        when(config.getBufferSize()).thenReturn(16);
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
