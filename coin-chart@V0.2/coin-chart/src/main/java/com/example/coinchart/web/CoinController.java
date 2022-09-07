package com.example.coinchart.web;

import com.example.coinchart.domain.Coin;
import com.example.coinchart.service.CoinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/coin")
@RequiredArgsConstructor
public class CoinController {

    private final CoinService coinService;

    @GetMapping("/chart")
    public String chart(Model model) {
        Map<String, BigDecimal> exchanges = coinService.getExchanges();
        List<Coin> coins = coinService.getCoins();
        log.info("exchanges={}", exchanges);
        log.info("coins={}", new JSONArray(coins));
        model.addAttribute("exchanges", exchanges);
        model.addAttribute("coins", new JSONArray(coins));
        return "coin/coinChart";
    }

    @ResponseBody
    @PostMapping
    public Map<String, List<Coin>> getCoin(@RequestParam(value = "tickers[]") List<String> tickers) {
        log.info("tickers={}", tickers);

        Map<String, List<Coin>> coin = new HashMap<>();
        for (String ticker : tickers) {
            coin.put(ticker, coinService.getCoin(ticker));
        }
        log.info("coin={}", coin);

        return coin;
    }
}
