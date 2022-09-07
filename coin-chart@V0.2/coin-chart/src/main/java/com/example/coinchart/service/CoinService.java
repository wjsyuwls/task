package com.example.coinchart.service;

import com.example.coinchart.domain.Coin;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface CoinService {

    Map<String, BigDecimal> getExchanges();

    List<Coin> getCoins();

    List<Coin> getCoin(String ticker);
}
