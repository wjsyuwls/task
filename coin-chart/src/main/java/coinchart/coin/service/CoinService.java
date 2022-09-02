package coinchart.coin.service;

import coinchart.coin.domain.Coin;
import coinchart.coin.feignclient.dto.Exchange;

import java.util.List;
import java.util.Map;

public interface CoinService {

    List<Coin.Response> getCoin(String ticker, String exchange);

    List<Coin.Response> getCoins(String currencyCode);

    Map<String, Double> getExchanges();
}
