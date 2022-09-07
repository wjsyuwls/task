package com.example.coinchart.service;

import com.example.coinchart.domain.Coin;
import com.example.coinchart.domain.Exchange;
import com.example.coinchart.repository.CoinMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoinServiceImpl implements CoinService {

    private final CoinMapper coinMapper;

    @Override
    public Map<String, BigDecimal> getExchanges() {
        Map<String, BigDecimal> exchangeStore = new HashMap<>();

        Exchange USD = coinMapper.getExchange("USD").get();

        //1USD : KRW
        BigDecimal USD_KRW = new BigDecimal(USD.getBasePrice());
        exchangeStore.put("KRW", USD_KRW);

        //JPY100 : KRW -> 1USD : JPY
        //(1USD : KRW) / ((100JPY : KRW) / JPY100)
        Exchange JPY = coinMapper.getExchange("JPY").get();
        BigDecimal JPYBASEPRICE = new BigDecimal(JPY.getBasePrice());
        BigDecimal USD_JPY = USD_KRW.divide((JPYBASEPRICE.divide(new BigDecimal(100))), RoundingMode.HALF_EVEN);
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
