package com.deadmate.richorbroke.service;

import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

public interface StreamingService {

    StreamingResponseBody getStreamFrom(final String url);
}
