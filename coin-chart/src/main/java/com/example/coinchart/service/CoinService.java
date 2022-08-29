package com.example.coinchart.service;

import com.example.coinchart.repository.Coin;
import com.example.coinchart.repository.Exchange;

import java.util.List;
import java.util.Map;

public interface CoinService {

    List<Coin> getCoin(String market, String exchange);

    List<Coin> getCoins();

    Map<String, Double> getExchanges();
}
