package coinchart.coin.service;

import coinchart.coin.domain.Coin;
import coinchart.coin.feignclient.dto.Exchange;
import coinchart.coin.feignclient.dto.Ticker;
import coinchart.coin.feignclient.dto.Tickers;
import coinchart.coin.feignclient.service.ExchangeFeignClient;
import coinchart.coin.feignclient.service.TickerFeignClient;
import coinchart.coin.feignclient.service.TickersFeignClient;
import coinchart.coin.repository.CoinMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class CoinSaveService {

    private final CoinMapper coinMapper;
    private final ExchangeFeignClient exchangeFeignClient;
    private final TickersFeignClient tickersFeignClient;
    private final TickerFeignClient tickerFeignClient;

    @Scheduled(cron = "0 */30 * * * *") // every 30m
    public void save() {

        //save exchange
        List<Exchange.Request> exchanges = exchangeFeignClient.getExchange();

        List<Exchange.Response> dbExchanges = coinMapper.getExchanges();

        for (Exchange.Request exchange : exchanges) {
            if (dbExchanges.size() == 0) {  //first
                coinMapper.saveExchanges(exchange);

            } else {
                coinMapper.updateExchanges(exchange);
            }
        }

        //save coin
        String localDateTimeFormat = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        List<Tickers> tickers = tickersFeignClient.getTickers();

        for (Tickers ticker : tickers) {
            try {   //default KRW
                if (ticker.getMarket().contains("KRW")) {
                    List<Ticker> findTicker = tickerFeignClient.getTicker(ticker.getMarket());

                    String market = findTicker.get(0).getMarket(); //KRW-BTC
                    String subMarket = market.substring(market.lastIndexOf("-") + 1); //BTC

                    Coin.Request coin = new Coin.Request();
                    coin.setMarket(subMarket);
                    coin.setTradePrice(findTicker.get(0).getTrade_price());
                    coin.setSaveDateTime(localDateTimeFormat);
                    coinMapper.saveCoin(coin);
                }
                Thread.sleep(90);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
