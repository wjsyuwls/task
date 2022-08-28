package com.example.coinchart.repository;

import lombok.Data;

@Data
public class Exchange {

    private String currencyCode;
    private String basePrice;
    private String usDollarRate;
    private String date;
    private String time;
}
