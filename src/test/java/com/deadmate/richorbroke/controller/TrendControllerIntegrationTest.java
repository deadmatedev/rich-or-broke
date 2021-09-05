package com.deadmate.richorbroke.controller;

import com.deadmate.richorbroke.exception.ApiException;
import com.deadmate.richorbroke.model.Image;
import com.deadmate.richorbroke.service.StreamingService;
import com.deadmate.richorbroke.service.TrendService;
import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import feign.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = TrendController.class)
@AutoConfigureMockMvc(addFilters = false)
class TrendControllerIntegrationTest {

    @MockBean
    private TrendService trendImageService;
    @MockBean
    private StreamingService streamingService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void givenValidCurrencyCode_shouldReturnAGifImage() throws Exception {
        // given
        when(trendImageService.getTrend("USD"))
                .thenReturn(new Image("uhf87asdh",
                        "about:blank",
                        3L,
                        "uhf87asdh"));
        when(streamingService.getStreamFrom("about:blank"))
                .thenReturn(outputStream -> {
                    outputStream.write("GIF".getBytes(StandardCharsets.UTF_8));
                    outputStream.flush();
                    outputStream.close();
                });
        // when
        mockMvc.perform(get("/trend/USD"))
        // then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_GIF))
                .andExpect(content().bytes("GIF".getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    void givenInvalidEndpointIsCalled_shouldReturnApiErrorNotFoundResponse() throws Exception {
        // given
        // when
        mockMvc.perform(get("/bla"))
                // then
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void givenUnsupportedCurrencyCode_shouldReturnApiErrorBadRequestResponse() throws Exception {
        // given
        when(trendImageService.getTrend("AAA"))
                .thenThrow(new ApiException(HttpStatus.BAD_REQUEST, "Unsupported currency code."));
        // when
        mockMvc.perform(get("/trend/AAA"))
                // then
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void givenFeignCallFails_shouldReturnApiErrorForbiddenResponse() throws Exception {
        // given
        when(trendImageService.getTrend("USD"))
                .thenThrow(FeignException.errorStatus("",
                        Response.builder()
                                .status(403)
                                .request(Request.create(
                                        Request.HttpMethod.GET,
                                        "https://openexchangerates.com/",
                                        Collections.emptyMap(),
                                        null,
                                        StandardCharsets.UTF_8,
                                        new RequestTemplate()))
                                .build()));
        // when
        mockMvc.perform(get("/trend/USD"))
                // then
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
