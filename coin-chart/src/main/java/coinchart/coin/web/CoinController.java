package coinchart.coin.web;

import coinchart.coin.domain.Coin;
import coinchart.coin.service.CoinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        Map<String, Double> exchanges = coinService.getExchanges();
        log.info("exchanges={}", exchanges);
        model.addAttribute("exchanges", exchanges);
        return "coin/coinChart";
    }

    @ResponseBody
    @PostMapping
    public List<Coin.Response> getCoin(String ticker, String exchange) {
        List<Coin.Response> coin = coinService.getCoin(ticker, exchange);
        log.info("coin={}", coin);
        return coin;
    }

    @ResponseBody
    @PostMapping("/{currencyCode}")
    public List<Coin.Response> getCoins(@PathVariable String currencyCode) {
        List<Coin.Response> coins = coinService.getCoins(currencyCode);
        log.info("coins={}", coins);
        return coins;
    }
}
