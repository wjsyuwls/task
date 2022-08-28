package com.example.coinchart.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CoinRepository {

    private final CoinMapper coinMapper;

    public void save(Coin coin) {
        coinMapper.save(coin);
    }

    public List<Coin> getCoin(String market) {
        return coinMapper.getCoin(market);
    }

    public List<Coin> getCoins() {
        return coinMapper.getCoins();
    }

    public void saveExchange(Exchange exchange) {
        coinMapper.saveExchange(exchange);
    }

    public void updateExchange(Exchange exchange) {
        coinMapper.updateExchange(exchange);
    }

    public Exchange getExchange(String currencyCode) {
        return coinMapper.getExchange(currencyCode);
    }
    public List<Exchange> getExchanges() {
        return coinMapper.getExchanges();
    }
}
