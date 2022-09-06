package com.example.coinchart.service;

import com.example.coinchart.domain.Coin;
import com.example.coinchart.domain.Exchange;
import com.example.coinchart.repository.CoinMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class CoinServiceImplTest {

    @Autowired
    private CoinMapper coinMapper;

    @Test
    public void getCoins() {
        List<Coin> coins = coinMapper.getCoins();
        log.info("coins={}", coins);
    }

    @Test
    public void getExchanges() {
        Map<String, Double> exchangeStore = new HashMap<>();

        Exchange USD = coinMapper.getExchange("USD").get();

        //1USD : KRW
        Double USD_KRW = Double.parseDouble(USD.getBasePrice());
        exchangeStore.put("KRW", USD_KRW);

        //JPY100 : KRW -> 1USD : JPY
        //(1USD : KRW) / ((100JPY : KRW) / JPY100)
        Exchange JPY = coinMapper.getExchange("JPY").get();
        Double USD_JPY = Double.parseDouble(String.format("%.2f", (USD_KRW / (Double.parseDouble(JPY.getBasePrice()) / 100))));
        exchangeStore.put("JPY", USD_JPY);

        log.info("환율={}", exchangeStore);
    }

}