package coinchart.coin.service;

import coinchart.coin.domain.Coin;
import coinchart.coin.feignclient.dto.Exchange;
import coinchart.coin.repository.CoinMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CoinServiceImpl implements CoinService {

    private final CoinMapper coinMapper;

    @Override
    public List<Coin.Response> getCoin(String ticker, String exchange) {
        List<Coin.ResponseDB> findCoin = coinMapper.getCoin(ticker);

        Exchange.Response getExchange = null;
        if (!exchange.equals("KRW")) {
            getExchange = coinMapper.getExchange(exchange).get();
        }

        List<Coin.Response> newCoins = new ArrayList<>();

        if (exchange.equals("KRW")) { //default KRW
            for (Coin.ResponseDB coin : findCoin) {
                Coin.Response newCoin = new Coin.Response();

                newCoin.setCurrencyCode("KRW");
                newCoin.setMarket(coin.getMarket());
                newCoin.setTradePrice(coin.getTradePrice());
                newCoin.setDate(coin.getDate());

                newCoins.add(newCoin);
            }
        } else {
            for (Coin.ResponseDB coin : findCoin) {
                Coin.Response newCoin = new Coin.Response();

                String tradePrice = String.format("%.4f", Double.parseDouble(coin.getTradePrice()) / Double.parseDouble(getExchange.getBasePrice()));

                newCoin.setMarket(coin.getMarket());
                newCoin.setCurrencyCode(exchange);
                newCoin.setTradePrice(tradePrice);
                newCoin.setDate(coin.getDate());

                newCoins.add(newCoin);
            }
        }
        return newCoins;
    }

    @Override
    public List<Coin.Response> getCoins(String currencyCode) {
        List<Exchange.Response> exchanges = coinMapper.getExchanges();
        List<Coin.ResponseDB> coins = coinMapper.getCoins();

        List<Coin.Response> newCoins = new ArrayList<>();

        if (currencyCode.equals("KRW")) {
            for (Coin.ResponseDB coin : coins) {
                setCoins(newCoins, null, coin, null);
            }
            return newCoins;
        } else {
            //KRW -> USD, JPY
            for (Exchange.Response exchange : exchanges) {
                if (exchange.getCurrencyCode().equals(currencyCode)) {
                    for (Coin.ResponseDB coin : coins) {
                        String tradePrice = String.format("%.4f", Double.parseDouble(coin.getTradePrice()) / Double.parseDouble(exchange.getBasePrice()));

                        setCoins(newCoins, exchange, coin, tradePrice);
                    }
                }
            }
            return newCoins;
        }
    }

    private static void setCoins(List<Coin.Response> newCoins, Exchange.Response exchange, Coin.ResponseDB coin, String tradePrice) {
        Coin.Response newCoin = new Coin.Response();
        if (exchange == null) {
            newCoin.setCurrencyCode("KRW");
        } else {
            newCoin.setCurrencyCode(exchange.getCurrencyCode());
        }
        newCoin.setMarket(coin.getMarket());
        if (tradePrice == null) {
            newCoin.setTradePrice(coin.getTradePrice());
        } else {
            newCoin.setTradePrice(tradePrice);
        }
        newCoin.setDate(coin.getDate());

        newCoins.add(newCoin);
    }

    @Override
    public Map<String, Double> getExchanges() {
        List<Exchange.Response> exchanges = coinMapper.getExchanges();
        Exchange.Response usd = coinMapper.getExchange("USD").get();

        double krw_usd = Double.parseDouble(usd.getBasePrice());

        Map<String, Double> store = new HashMap<>();

        for (Exchange.Response exchange : exchanges) {

            //1USD : KRW
            if (exchange.getCurrencyCode().equals("USD")) {
                store.put("KRW", Double.parseDouble(exchange.getBasePrice()));
            }

            //JPY100 : KRW -> 1USD : JPY
            if (exchange.getCurrencyCode().equals("JPY")) {
                //(1USD : KRW) / ((100JPY : KRW) / JPY100)
                double usd_jpy = Double.parseDouble(String.format("%.2f", (krw_usd / (Double.parseDouble(exchange.getBasePrice()) / 100))));
                store.put(exchange.getCurrencyCode(), usd_jpy);
            }
        }

        return store;
    }
}
