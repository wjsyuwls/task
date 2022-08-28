package com.example.coinchart.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "UpBitTickerFeignClient", url = "https://api.upbit.com/v1/ticker")
public interface UpBitTickerFeignClient {

    @GetMapping
    List<UpBitTicker> getTicker(@RequestParam(value = "markets") String markets);
}

