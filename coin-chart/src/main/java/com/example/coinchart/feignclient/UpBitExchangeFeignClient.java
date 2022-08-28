package com.example.coinchart.feignclient;

import com.example.coinchart.repository.Exchange;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "UpBitExchangeFeignClient", url = "https://quotation-api-cdn.dunamu.com/v1/forex/recent?codes=FRX.KRWUSD,FRX.KRWJPY,FRX.KRWCNY,FRX.KRWEUR")
public interface UpBitExchangeFeignClient {

    @GetMapping
    List<Exchange> getExchange();
}
