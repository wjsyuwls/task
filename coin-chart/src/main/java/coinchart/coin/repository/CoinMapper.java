package coinchart.coin.repository;

import coinchart.coin.domain.Coin;
import coinchart.coin.feignclient.dto.Exchange;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CoinMapper {

    void saveCoin(Coin.Request coin);

    List<Coin.ResponseDB> getCoin(String ticker);

    List<Coin.ResponseDB> getCoins();

    void saveExchanges(Exchange.Request exchange);

    void updateExchanges(Exchange.Request exchange);

    Optional<Exchange.Response> getExchange(String currencyCode);

    List<Exchange.Response> getExchanges();
}
