package coinchart.coin.feignclient.dto;

import lombok.*;

@Data
public class Ticker {

    private String market;
    private String trade_price;
}
