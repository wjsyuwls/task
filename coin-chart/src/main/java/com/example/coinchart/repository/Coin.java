package com.example.coinchart.repository;

import lombok.Data;

@Data
public class Coin {

    private String currencyCode;
    private String market;
    private String tradePrice;
    private String date;
}
