package com.example.crawling.repository;

import com.example.crawling.dto.Exchange;

import java.util.List;
import java.util.Optional;

public interface CoinMapper {

    List<Exchange> getExchanges();

    void saveExchanges(Exchange exchange);

    void updateExchanges(Exchange exchange);
}
