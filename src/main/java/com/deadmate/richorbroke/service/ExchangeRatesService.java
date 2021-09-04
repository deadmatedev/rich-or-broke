package com.deadmate.richorbroke.service;

import com.deadmate.richorbroke.model.openexhangerates.ExchangeRates;

import java.time.LocalDate;

public interface ExchangeRatesService {

    ExchangeRates getLatest(String base);

    ExchangeRates getHistorical(String base, LocalDate date);
}
