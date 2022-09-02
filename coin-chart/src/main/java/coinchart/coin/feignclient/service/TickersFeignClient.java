package coinchart.coin.feignclient.service;

import coinchart.coin.feignclient.dto.Tickers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "TickersFeignClient", url = "https://api.upbit.com/v1/market/all")
public interface TickersFeignClient {

    @GetMapping
    List<Tickers> getTickers();
}
