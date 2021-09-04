package com.deadmate.richorbroke.service.impl;

import com.deadmate.richorbroke.client.OpenExchangeRatesClient;
import com.deadmate.richorbroke.config.OpenExchangeRatesClientConfig;
import com.deadmate.richorbroke.model.openexhangerates.ExchangeRates;
import com.deadmate.richorbroke.service.ExchangeRatesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class OpenExchangeRatesService implements ExchangeRatesService {

    private final OpenExchangeRatesClient client;
    private final OpenExchangeRatesClientConfig config;

    @Override
    public ExchangeRates getLatest(String base) {
        return client.getLatest(base, config.getAppId());
    }

    @Override
    public ExchangeRates getHistorical(String base, LocalDate date) {
        return client.getHistorical(date.toString(), base, config.getAppId());
    }
}
