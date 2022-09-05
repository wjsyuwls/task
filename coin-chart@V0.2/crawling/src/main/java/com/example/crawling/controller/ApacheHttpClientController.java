package com.example.crawling.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@Slf4j
@RestController
public class ApacheHttpClientController {

    @GetMapping("/tickers")
    public ResponseEntity tickers() {

        ObjectMapper objectMapper = new ObjectMapper();
        Object objValue = null;

        try {
            URI uri = new URI("https://api.upbit.com/v1/market/all");
            uri = new URIBuilder(uri)
                    .build();

            CloseableHttpClient httpClient = HttpClients.custom()
                    .setMaxConnTotal(100)
                    .setMaxConnPerRoute(100)
                    .build();

            HttpResponse httpResponse = httpClient.execute(new HttpGet(uri));
            HttpEntity entity = httpResponse.getEntity();
            String content = EntityUtils.toString(entity);
            System.out.println("content = " + content);
            objValue = objectMapper.readValue(content, Object.class);
            System.out.println("objValue = " + objValue);

        } catch (Exception e) {

        }

        return ResponseEntity.ok(objValue);
    }

    @GetMapping("/ticker")
    public ResponseEntity ticker(String ticker) {

        ObjectMapper objectMapper = new ObjectMapper();
        Object objValue = null;

        try {
            URI uri = new URI("https://api.upbit.com/v1/ticker");
            uri = new URIBuilder(uri)
                    .addParameter("markets", ticker)
                    .build();

            CloseableHttpClient httpClient = HttpClients.custom()
                    .setMaxConnTotal(100)
                    .setMaxConnPerRoute(100)
                    .build();

            HttpResponse httpResponse = httpClient.execute(new HttpGet(uri));
            HttpEntity entity = httpResponse.getEntity();
            String content = EntityUtils.toString(entity);
            System.out.println("content = " + content);
            objValue = objectMapper.readValue(content, Object.class);
            System.out.println("objValue = " + objValue);

        } catch (Exception e) {

        }

        return ResponseEntity.ok(objValue);
    }

    @GetMapping("/exchanges")
    public ResponseEntity exchanges() {

        ObjectMapper objectMapper = new ObjectMapper();
        Object objValue = null;

        try {
            URI uri = new URI("https://quotation-api-cdn.dunamu.com/v1/forex/recent?codes=FRX.KRWUSD,FRX.KRWJPY,FRX.KRWCNY,FRX.KRWEUR");
            uri = new URIBuilder(uri)
                    .build();

            CloseableHttpClient httpClient = HttpClients.custom()
                    .setMaxConnTotal(100)
                    .setMaxConnPerRoute(100)
                    .build();

            HttpResponse httpResponse = httpClient.execute(new HttpGet(uri));
            HttpEntity entity = httpResponse.getEntity();
            String content = EntityUtils.toString(entity);
            log.info("content={}", content);
            objValue = objectMapper.readValue(content, Object.class);
            log.info("objValue={}", objValue);

        } catch (Exception e) {

        }

        return ResponseEntity.ok(objValue);
    }
}
