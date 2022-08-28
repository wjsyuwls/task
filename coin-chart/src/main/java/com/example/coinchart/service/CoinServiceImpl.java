package com.example.coinchart.service;

import com.example.coinchart.feignclient.*;
import com.example.coinchart.repository.Coin;
import com.example.coinchart.repository.CoinRepository;
import com.example.coinchart.repository.Exchange;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoinServiceImpl implements CoinService {

    private final CoinRepository coinRepository;
    private final UpBitTickerFeignClient upBitTickerFeignClient;
    private final UpBitTickersFeignClient upBitTickersFeignClient;
    private final UpBitExchangeFeignClient upBitExchangeFeignClient;

    @Override
    public List<Coin> getCoin(String market) {
        return coinRepository.getCoin(market);
    }

    @Override
    public List<Coin> getCoins() {
        List<Exchange> exchanges = coinRepository.getExchanges();
        List<Coin> coins = coinRepository.getCoins();

        List<Coin> newCoins = new ArrayList<>();

        //KRW
        for (Coin coin : coins) {
            newCoins.add(coin);
        }

        //KRW -> USD, JPY
        for (Exchange exchange : exchanges) {
            for (Coin coin : coins) {
                String tradePrice = String.format("%.4f", Double.parseDouble(coin.getTradePrice()) / Double.parseDouble(exchange.getBasePrice()));

                Coin newCoin = new Coin();
                newCoin.setCurrencyCode(exchange.getCurrencyCode());
                newCoin.setMarket(coin.getMarket());
                newCoin.setTradePrice(tradePrice);
                newCoin.setDate(coin.getDate());

                newCoins.add(newCoin);
            }
        }

        return newCoins;
    }

    @Override
    public Map<String, Double> getExchanges() {
        List<Exchange> exchanges = coinRepository.getExchanges();
        Exchange usd = coinRepository.getExchange("USD");

        Double krw_usd = Double.parseDouble(usd.getBasePrice());    //1USD : KRW

        Map<String, Double> store = new HashMap<>();

        for (Exchange exchange : exchanges) {
            log.info("exchange={}", exchange);

            //1USD : KRW
            if (exchange.getCurrencyCode().equals("USD")) {
                store.put("KRW", Double.parseDouble(exchange.getBasePrice()));
            }

            //JPY100 : KRW -> 1USD : JPY
            if (exchange.getCurrencyCode().equals("JPY")) {
                //(1USD : KRW) / ((100JPY : KRW) / JPY100)
                double usd_jpy = Double.parseDouble(String.format("%.2f", (krw_usd / (Double.parseDouble(exchange.getBasePrice()) / 100))));
                store.put(exchange.getCurrencyCode(), usd_jpy);
            }
        }

        return store;
    }

    @Scheduled(cron = "0 */30 * * * *")     // every 30m
//    @Scheduled(cron = "*/30 * * * * *")   // every 30s
    public void save() {

        //save exchange
        List<Exchange> exchanges = upBitExchangeFeignClient.getExchange();
        log.info("exchanges={}", exchanges);

        List<Exchange> getExchange = coinRepository.getExchanges();

        for (Exchange exchange : exchanges) {
            if (getExchange.size() == 0) {  //first
                coinRepository.saveExchange(exchange);
            } else {
                coinRepository.updateExchange(exchange);
            }
        }

        //save coin
        List<UpBitTickers> tickers = upBitTickersFeignClient.getUpBitTickers();
        log.info("tickers={}", tickers);

        String localDateTimeFormat = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        for (UpBitTickers ticker : tickers) {
            try {   //default KRW
                if (ticker.getMarket().contains("KRW")) {
                    List<UpBitTicker> findTicker = upBitTickerFeignClient.getTicker(ticker.getMarket());
                    log.info("ticker={}", findTicker);

                    String market = findTicker.get(0).getMarket();  //KRW-BTC
                    String newMarket = market.substring(market.lastIndexOf("-") + 1);   //BTC

                    Coin coin = new Coin();
                    coin.setCurrencyCode("KRW");
                    coin.setMarket(newMarket);
                    coin.setTradePrice(findTicker.get(0).getTrade_price());
                    coin.setDate(localDateTimeFormat);
                    coinRepository.save(coin);
                }
                Thread.sleep(90);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
