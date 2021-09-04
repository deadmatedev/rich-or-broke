package com.deadmate.richorbroke.service.impl;

import com.deadmate.richorbroke.config.RichOrBrokeServiceConfig;
import com.deadmate.richorbroke.exception.ApiException;
import com.deadmate.richorbroke.model.Image;
import com.deadmate.richorbroke.model.openexhangerates.ExchangeRates;
import com.deadmate.richorbroke.service.ExchangeRatesService;
import com.deadmate.richorbroke.service.ImageService;
import com.deadmate.richorbroke.service.TrendService;
import com.deadmate.richorbroke.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RichOrBrokeTrendService implements TrendService {

    private final ExchangeRatesService exchangeRatesService;
    private final ImageService imageService;
    private final RichOrBrokeServiceConfig config;
    private final Clock clock = Clock.systemDefaultZone();

    @Override
    public Image getTrend(String currencyCode) {
        validate(currencyCode);

        String base = config.getBaseCurrencyCode();
        Double now = getCurrencyRate(currencyCode,
                exchangeRatesService.getLatest(base));
        Double was = getCurrencyRate(currencyCode,
                exchangeRatesService.getHistorical(base, LocalDate.now(clock).minusDays(1)));

        double diff = now - was;
        String query = diff > Constants.EPSILON
                ? config.getUpTrendQuery()
                : config.getDownTrendQuery();
        if (Math.abs(diff) < Constants.EPSILON) {
            query = config.getZeroTrendQuery();
        }

        Image image = imageService.getRandomImage(query);
        log.debug("Query: '{}': {}/{}: was {} -> now {}, diff: {}, gif: {}",
                query, currencyCode, base, was, now, diff, image);
        return image;
    }

    private void validate(String currencyCode) {
        if (!Constants.VALID_CURRENCY_CODE.matcher(currencyCode).find()) {
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "Invalid currency code. See " + config.getHelpUrl());
        }
    }

    private Double getCurrencyRate(String currencyCode, ExchangeRates rates) {
        return Optional.ofNullable(rates.getRates().get(currencyCode))
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST,
                        "Unsupported currency code. See " + config.getHelpUrl()));
    }
}
