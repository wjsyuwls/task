package com.example.coinchart.service;

import com.example.coinchart.domain.Coin;

import java.util.List;
import java.util.Map;

public interface CoinService {

    Map<String, Double> getExchanges();

    List<Coin> getCoins();

    List<Coin> getCoin(String ticker);
}
