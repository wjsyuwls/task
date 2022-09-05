package com.example.crawling.service;

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

import java.net.URI;

@Slf4j
@RequiredArgsConstructor
public class Crawling {

    private final CoinMapper coinMapper;

    //    @Scheduled(cron = "0 */30 * * * *") //every 30m
    public void crawling() {

        JSONArray exchanges = exchangeInfo();
        for (int i = 0; i < exchanges.length(); i++) {
            JSONObject object = exchanges.getJSONObject(i);
            String currencyCode = object.get("currencyCode").toString();
            String basePrice = object.get("basePrice").toString();
            String usDollarRate = object.get("usDollarRate").toString();
            String date = object.get("date").toString();
            String time = object.get("time").toString();

            Exchange exchange = null;
            if (currencyCode.equals("USD")) {
                exchange = new Exchange();
                exchange.setCurrencyCode(currencyCode);
                exchange.setBasePrice(basePrice);
                exchange.setUsDollarRate(usDollarRate);
                exchange.setDate(date);
                exchange.setTime(time);
            }
        }

//        JSONObject BTCUSDT = symbolPrice("BTCUSDT");
//        JSONObject ETHBTC = symbolPrice("ETHBTC");
//        JSONObject BCHBTC = symbolPrice("BCHBTC");
//        JSONObject LTCBTC = symbolPrice("LTCBTC");
//        JSONObject SOLBTC = symbolPrice("SOLBTC");
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
