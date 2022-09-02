package coinchart.coin.feignclient.service;

import coinchart.coin.feignclient.dto.Ticker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "TickerFeignClient", url = "https://api.upbit.com/v1/ticker")
public interface TickerFeignClient {

    @GetMapping
    List<Ticker> getTicker(@RequestParam(value = "markets") String markets);
}
