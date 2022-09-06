package com.example.coinchart.service;

import com.example.coinchart.domain.Coin;
import com.example.coinchart.domain.Exchange;
import com.example.coinchart.repository.CoinMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoinServiceImpl implements CoinService {

    private final CoinMapper coinMapper;

    @Override
    public Map<String, Double> getExchanges() {
        Map<String, Double> exchangeStore = new HashMap<>();

        Exchange USD = coinMapper.getExchange("USD").get();

        Double USD_KRW = Double.parseDouble(USD.getBasePrice());
        //1USD : KRW
        exchangeStore.put("KRW", USD_KRW);

        Exchange JPY = coinMapper.getExchange("JPY").get();
        //JPY100 : KRW -> 1USD : JPY
        //(1USD : KRW) / ((100JPY : KRW) / JPY100)
        Double USD_JPY = Double.parseDouble(String.format("%.2f", (USD_KRW / (Double.parseDouble(JPY.getBasePrice()) / 100))));
        exchangeStore.put("JPY", USD_JPY);

        return exchangeStore;
    }

    @Override
    public List<Coin> getCoins() {
        return coinMapper.getCoins();
    }

    @Override
    public List<Coin> getCoin(String ticker) {
        return coinMapper.getCoin(ticker);
    }
}
