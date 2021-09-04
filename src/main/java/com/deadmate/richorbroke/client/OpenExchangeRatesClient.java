package com.deadmate.richorbroke.client;

import com.deadmate.richorbroke.model.openexhangerates.ExchangeRates;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "openexchangerates", url = "${client.config.openexchangerates.url}")
public interface OpenExchangeRatesClient {

    // https://openexchangerates.org/api/latest.json?base=RUB&app_id=b96a08ac38bc4910b9d3de2c71764859
    @GetMapping(value = "/latest.json?base={base}&app_id={app_id}")
    ExchangeRates getLatest(@PathVariable("base") String base,
                            @PathVariable("app_id") String appId);

    // https://openexchangerates.org/api/historical/2021-09-03.json?base=RUB&app_id=b96a08ac38bc4910b9d3de2c71764859
    @Cacheable(value = "historical", key = "#date.toString()")
    @GetMapping(value = "/historical/{date}.json?base={base}&app_id={app_id}")
    ExchangeRates getHistorical(@PathVariable("date") String date,
                                @PathVariable("base") String base,
                                @PathVariable("app_id") String appId);
}


