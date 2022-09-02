package coinchart.coin.feignclient.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class Exchange {

    @Getter
    @Setter
    @ToString
    public static class Request {
        private String currencyCode;
        private String basePrice;
        private String usDollarRate;
        private String date;
        private String time;
    }

    @Getter
    @AllArgsConstructor
    public static class Response {
        private String currencyCode;
        private String basePrice;
    }
}
