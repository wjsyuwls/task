package coinchart.coin.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class Coin {

    @Setter
    @Getter
    public static class Request {
        private String market;
        private String tradePrice;
        private String saveDateTime;
    }

    @Getter
    @Setter
    @ToString
    public static class Response {
        private String currencyCode;
        private String market;
        private String tradePrice;
        private String date;
    }

    @Getter
    @AllArgsConstructor
    public static class ResponseDB {
        private String market;
        private String tradePrice;
        private String date;
    }
}
