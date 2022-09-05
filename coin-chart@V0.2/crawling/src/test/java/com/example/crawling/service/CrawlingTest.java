package com.example.crawling.service;

import com.example.crawling.repository.CoinMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
class CrawlingTest {

    @Autowired
    CoinMapper coinMapper;
    @Autowired
    Crawling crawling = new Crawling(coinMapper);

    @Test
    public void httpClient() {
        crawling.crawling();

//        JSONArray response = crawling.exchangeInfo();
//        log.info("response={}", response);
    }
}