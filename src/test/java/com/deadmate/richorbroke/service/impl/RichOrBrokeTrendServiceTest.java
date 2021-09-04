package com.deadmate.richorbroke.service.impl;

import com.deadmate.richorbroke.config.RichOrBrokeServiceConfig;
import com.deadmate.richorbroke.exception.ApiException;
import com.deadmate.richorbroke.model.Image;
import com.deadmate.richorbroke.model.openexhangerates.ExchangeRates;
import com.deadmate.richorbroke.service.ExchangeRatesService;
import com.deadmate.richorbroke.service.ImageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RichOrBrokeTrendServiceTest {

    @Mock
    private ExchangeRatesService exchangeRatesService;
    @Mock
    private ImageService imageService;
    @Mock
    private RichOrBrokeServiceConfig config;
    @InjectMocks
    private RichOrBrokeTrendService service;

    @ParameterizedTest
    // given
    @ValueSource(strings = { "1", "_", "abc", "code", "Z ", "B23", "AA" })
    void givenInvalidCurrencyCode_shouldThrowApiExceptionBadRequest(String currencyCode) {
        // when
        ApiException e = assertThrows(ApiException.class,
                () -> service.getTrend(currencyCode));
        // then
        assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
        assertTrue(e.getMessage().startsWith("Invalid currency code."));
    }

    @ParameterizedTest
    // given
    @ValueSource(strings = { "AAA", "ABC", "ZZZ" })
    void givenUnsupportedCurrencyCode_shouldThrowBadRequestApiException(String currencyCode) {
        // given
        String base = "USD";
        when(config.getBaseCurrencyCode()).thenReturn(base);
        when(config.getHelpUrl()).thenReturn("https://help.com");
        when(exchangeRatesService.getLatest(base))
                .thenReturn(new ExchangeRates(
                        "disclaimer",
                        "MIT",
                        0,
                       "USD",
                       Map.of("USD", 1.0,
                               "RUB", 73.6)));
        // when
        ApiException e = assertThrows(ApiException.class,
                () -> service.getTrend(currencyCode));
        // then
        assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
        assertTrue(e.getMessage().startsWith("Unsupported currency code."));
    }

    @Test
    void givenPositiveRatesDifference_shouldReturnRichImage() {
        // given
        String currencyCode = "RUB";
        String base = "USD";
        when(config.getBaseCurrencyCode()).thenReturn(base);
        when(config.getUpTrendQuery()).thenReturn("rich");
        when(exchangeRatesService.getLatest(base))
                .thenReturn(new ExchangeRates(
                        "disclaimer",
                        "MIT",
                        0,
                        "USD",
                        Map.of("USD", 1.0,
                                "RUB", 73.6)));    // now
        Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        ReflectionTestUtils.setField(service, "clock", clock);
        when(exchangeRatesService.getHistorical(base, LocalDate.now(clock).minusDays(1)))
                .thenReturn(new ExchangeRates(
                        "disclaimer",
                        "MIT",
                        0,
                        "USD",
                        Map.of("USD", 1.0,
                                "RUB", 72.6)));    // was
        when(imageService.getRandomImage(config.getUpTrendQuery()))
                .thenReturn(new Image("u8er9h8wh",
                        "https://rich.gif",
                        64,
                        "hash"));
        // when
        Image image = service.getTrend(currencyCode);
        // then
        assertEquals("u8er9h8wh", image.getId());
    }

    @Test
    void givenNegativeRatesDifference_shouldReturnBrokeImage() {
        // given
        String currencyCode = "EUR";
        String base = "USD";
        when(config.getBaseCurrencyCode()).thenReturn(base);
        when(config.getDownTrendQuery()).thenReturn("broke");
        when(exchangeRatesService.getLatest(base))
                .thenReturn(new ExchangeRates(
                        "disclaimer",
                        "MIT",
                        0,
                        "USD",
                        Map.of("USD", 1.0,
                                "EUR", 0.8)));    // now
        Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        ReflectionTestUtils.setField(service, "clock", clock);
        when(exchangeRatesService.getHistorical(base, LocalDate.now(clock).minusDays(1)))
                .thenReturn(new ExchangeRates(
                        "disclaimer",
                        "MIT",
                        0,
                        "USD",
                        Map.of("USD", 1.0,
                                "EUR", 0.9)));    // was
        when(imageService.getRandomImage(config.getDownTrendQuery()))
                .thenReturn(new Image("tyr9065",
                        "https://broke.gif",
                        84,
                        "hash"));
        // when
        Image image = service.getTrend(currencyCode);
        // then
        assertEquals("tyr9065", image.getId());
    }

    @Test
    void givenZeroRatesDifference_shouldReturnBrokeImage() {
        // given
        String currencyCode = "USD";
        String base = "USD";
        when(config.getBaseCurrencyCode()).thenReturn(base);
        when(config.getZeroTrendQuery()).thenReturn("broke");
        when(exchangeRatesService.getLatest(base))
                .thenReturn(new ExchangeRates(
                        "disclaimer",
                        "MIT",
                        0,
                        "USD",
                        Map.of("USD", 1.0)));    // now
        Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        ReflectionTestUtils.setField(service, "clock", clock);
        when(exchangeRatesService.getHistorical(base, LocalDate.now(clock).minusDays(1)))
                .thenReturn(new ExchangeRates(
                        "disclaimer",
                        "MIT",
                        0,
                        "USD",
                        Map.of("USD", 1.0)));    // was
        when(imageService.getRandomImage(config.getZeroTrendQuery()))
                .thenReturn(new Image("rrr245ds",
                        "https://still.gif",
                        74,
                        "hash"));
        // when
        Image image = service.getTrend(currencyCode);
        // then
        assertEquals("rrr245ds", image.getId());
    }
}