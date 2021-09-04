package com.deadmate.richorbroke.model.openexhangerates;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public class ExchangeRates {
    private final String disclaimer;
    private final String license;
    private final long timestamp;
    private final String base;
    private final Map<String, Double> rates;
}
