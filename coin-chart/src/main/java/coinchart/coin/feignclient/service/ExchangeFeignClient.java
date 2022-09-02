package coinchart.coin.feignclient.service;

import coinchart.coin.feignclient.dto.Exchange;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "ExchangeFeignClient", url = "https://quotation-api-cdn.dunamu.com/v1/forex/recent?codes=FRX.KRWUSD,FRX.KRWJPY,FRX.KRWCNY,FRX.KRWEUR")
public interface ExchangeFeignClient {

    @GetMapping
    List<Exchange.Request> getExchange();
}
