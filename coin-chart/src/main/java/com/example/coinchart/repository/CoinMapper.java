package com.example.coinchart.repository;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CoinMapper {

    void save(Coin coin);

    List<Coin> getCoin(String market);

    List<Coin> getCoins();

    void saveExchange(Exchange exchange);

    void updateExchange(Exchange exchange);

    Exchange getExchange(String currencyCode);

    List<Exchange> getExchanges();
}
