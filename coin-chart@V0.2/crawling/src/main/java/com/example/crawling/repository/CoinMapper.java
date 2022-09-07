package com.example.crawling.repository;

import com.example.crawling.dto.Coin;
import com.example.crawling.dto.Exchange;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CoinMapper {

    List<Exchange> getExchanges();

    void saveExchange(Exchange exchange);

    void updateExchange(Exchange exchange);

    void saveCoin(Coin coin);
}
