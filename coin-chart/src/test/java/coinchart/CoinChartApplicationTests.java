package coinchart;

import coinchart.coin.feignclient.dto.Exchange;
import coinchart.coin.feignclient.service.ExchangeFeignClient;
import coinchart.coin.feignclient.service.TickerFeignClient;
import coinchart.coin.feignclient.service.TickersFeignClient;
import coinchart.coin.repository.CoinMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@RequiredArgsConstructor
class CoinChartApplicationTests {

	private final CoinMapper coinMapper;
	private final ExchangeFeignClient exchangeFeignClient;
	private final TickersFeignClient tickersFeignClient;
	private final TickerFeignClient tickerFeignClient;


	@Test
	void contextLoads() {

		List<Exchange.Request> exchanges = exchangeFeignClient.getExchange();

		List<Exchange.Response> dbExchanges = coinMapper.getExchanges();

		for (Exchange.Request exchange : exchanges) {
			if (dbExchanges.size() == 0) {  //first
				coinMapper.saveExchanges(exchange);
			} else {
				coinMapper.updateExchanges(exchange);
			}
		}

	}

}
