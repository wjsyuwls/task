package com.example.crawling.service;

import com.example.crawling.dto.Coin;
import com.example.crawling.repository.CoinMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@SpringBootTest
class CrawlingTest {

    @Autowired
    CoinMapper coinMapper;
    @Autowired
    Crawling crawling = new Crawling(coinMapper);

    @Test
    public void httpClient() {
        JSONObject response = crawling.symbolPrice("BCHUSDT");
        log.info("response={}", response);
    }

    @Test
    public void saveCoin() {
        //assume usdt = usd
        //default usd
        String[] ticker = {"BTCUSDT", "ETHUSDT", "BCHUSDT", "LTCUSDT", "SOLUSDT", "ETCUSDT"};

        String localDateTimeFormat = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        for (int i = 0; i < ticker.length; i++) {

            JSONObject response = symbolPrice(ticker[i]);

            Coin coin = new Coin();
            String symbol = response.get("symbol").toString();
            coin.setSymbol(symbol.substring(0, 3));
            coin.setPrice(response.get("price").toString());
            coin.setSaveDateTime(localDateTimeFormat);

            coinMapper.saveCoin(coin);
        }
    }

    public JSONObject symbolPrice(String symbol) {
        String response = null;

        try {
            //http client 생성
            CloseableHttpClient httpClient = HttpClients.createDefault();

            //get 메서드와 URL 설정
            HttpGet httpGet = new HttpGet("https://api.binance.com/api/v1/ticker/price");
            URI uri = new URIBuilder(httpGet.getURI())
                    .addParameter("symbol", symbol)
                    .build();
            httpGet.setURI(uri);
            httpGet.addHeader("Content-type", "application/json");

            //get 요청
            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);

            HttpEntity entity = httpResponse.getEntity();
            response = EntityUtils.toString(entity, "UTF-8");
            log.info("response={}", response);

            httpClient.close();
        } catch (Exception e) {
            log.info("err", e);
        }

        return new JSONObject(response);
    }
}