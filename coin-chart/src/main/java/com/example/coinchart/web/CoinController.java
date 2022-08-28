package com.example.coinchart.web;

import com.example.coinchart.repository.Coin;
import com.example.coinchart.service.CoinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/chart")
@RequiredArgsConstructor
public class CoinController {

    private final CoinService coinService;

    @GetMapping
    public String chart(Model model) {
        List<Coin> coins = coinService.getCoins();
        log.info("coins={}", coins);

        Map<String, Double> exchange = coinService.getExchanges();
        log.info("exchange={}", exchange);

        model.addAttribute("coins", coins);
        model.addAttribute("exchange", exchange);

        return "chart";
    }

    @ResponseBody
    @PostMapping("/coin")
    public List<Coin> getCoin(String ticker) {
        List<Coin> coin = coinService.getCoin(ticker);
        log.info("coin={}", coin);

        return coin;
    }
}
