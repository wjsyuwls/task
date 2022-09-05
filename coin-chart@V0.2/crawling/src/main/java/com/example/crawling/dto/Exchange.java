package com.example.crawling.dto;

import lombok.Data;

@Data
public class Exchange {
    
    private String currencyCode;
    private String basePrice;
    private String usDollarRate;
    private String date;
    private String time;
}
