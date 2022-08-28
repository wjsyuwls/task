package com.example.coinchart.feignclient;

import lombok.Data;

@Data
public class UpBitTickers {

    private String market;
    private String korean_name;
    private String english_name;
    private String market_warning;
}
