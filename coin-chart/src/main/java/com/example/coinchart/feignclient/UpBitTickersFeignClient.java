package com.example.coinchart.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "UpBitTickersFeignClient", url = "https://api.upbit.com/v1/market/all")
public interface UpBitTickersFeignClient {

    @GetMapping
    List<UpBitTickers> getUpBitTickers();
}
