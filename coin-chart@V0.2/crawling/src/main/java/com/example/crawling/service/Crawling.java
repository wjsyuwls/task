package com.example.crawling.service;

import com.example.crawling.dto.Coin;
import com.example.crawling.dto.Exchange;
import com.example.crawling.repository.CoinMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class Crawling {

    private final CoinMapper coinMapper;

    @Scheduled(cron = "0 */30 * * * *") //every 30m
    public void saveExchange() {
        JSONArray exchanges = exchangeInfo();
        for (int i = 0; i < exchanges.length(); i++) {
            JSONObject response = exchanges.getJSONObject(i);

            String currencyCode = response.get("currencyCode").toString();
            String basePrice = response.get("basePrice").toString();
            String usDollarRate = response.get("usDollarRate").toString();
            String date = response.get("date").toString();
            String time = response.get("time").toString();

            Exchange exchange = null;
            if (currencyCode.equals("USD") || currencyCode.equals("JPY")) {
                exchange = new Exchange();
                exchange.setCurrencyCode(currencyCode);
                exchange.setBasePrice(basePrice);
                exchange.setUsDollarRate(usDollarRate);
                exchange.setDate(date);
                exchange.setTime(time);

                if (coinMapper.getExchanges().size() == 2) {
                    coinMapper.updateExchange(exchange);
                } else { //first
                    coinMapper.saveExchange(exchange);
                }
            }
        }
    }

    @Scheduled(cron = "0 */30 * * * *") //every 30m
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

    public JSONArray exchangeInfo() {
        String response = null;

        try {
            //http client 생성
            CloseableHttpClient httpClient = HttpClients.createDefault();

            //get 메서드와 URL 설정
            HttpGet httpGet = new HttpGet("https://quotation-api-cdn.dunamu.com/v1/forex/recent?codes=FRX.KRWUSD,FRX.KRWJPY,FRX.KRWCNY,FRX.KRWEUR");
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

        return new JSONArray(response);
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
