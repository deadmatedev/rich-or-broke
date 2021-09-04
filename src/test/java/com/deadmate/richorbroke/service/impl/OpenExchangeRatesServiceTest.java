package com.deadmate.richorbroke.service.impl;

import com.deadmate.richorbroke.client.OpenExchangeRatesClient;
import com.deadmate.richorbroke.config.OpenExchangeRatesClientConfig;
import com.deadmate.richorbroke.model.openexhangerates.ExchangeRates;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OpenExchangeRatesServiceTest {

    @Mock
    private OpenExchangeRatesClient client;
    @Mock
    private OpenExchangeRatesClientConfig config;
    @InjectMocks
    private OpenExchangeRatesService service;

    @Test
    void givenCurrencyCode_shouldReturnLatestExchangeRates() {
        // given
        when(config.getAppId()).thenReturn("xxx");
        when(client.getLatest("RUB", config.getAppId()))
                .thenReturn(new ExchangeRates(
                        "disclaimer",
                        "MIT",
                        0L,
                        "USD",
                        Map.of("USD", 1.0d,
                                "RUB", 73.6d)));
        // when
        ExchangeRates exchangeRates = service.getLatest("RUB");
        // then
        assertNotNull(exchangeRates.getRates().get("RUB"));
    }

    @Test
    void givenDateAndCurrencyCode_shouldReturnHistoricalExchangeRates() {
        // given
        when(config.getAppId()).thenReturn("yyy");
        when(client.getHistorical("2021-08-31", "RUB", config.getAppId()))
                .thenReturn(new ExchangeRates(
                        "disclaimer",
                        "MIT",
                        0L,
                        "USD",
                        Map.of("USD", 1.0d,
                                "RUB", 75.8d)));
        // when
        ExchangeRates exchangeRates = service.getHistorical("RUB",
                LocalDate.of(2021, 8, 31));
        // then
        assertNotNull(exchangeRates.getRates().get("RUB"));
    }
}