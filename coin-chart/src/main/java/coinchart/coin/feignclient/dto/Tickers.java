package coinchart.coin.feignclient.dto;

import lombok.Data;

@Data
public class Tickers {

    private String market;
    private String korean_name;
    private String english_name;
}
