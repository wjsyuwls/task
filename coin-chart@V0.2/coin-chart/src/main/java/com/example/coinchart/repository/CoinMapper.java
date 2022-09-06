package com.example.coinchart.repository;

import com.example.coinchart.domain.Coin;
import com.example.coinchart.domain.Exchange;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CoinMapper {

    List<Exchange> getExchanges();

    Optional<Exchange> getExchange(String currencyCode);

    List<Coin> getCoins();

    List<Coin> getCoin(String symbol);
}
