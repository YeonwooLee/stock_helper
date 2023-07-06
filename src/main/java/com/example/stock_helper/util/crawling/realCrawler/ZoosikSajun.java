package com.example.stock_helper.util.crawling.realCrawler;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
public class ZoosikSajun {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String stockName;
    private String theme;
    private String searchTime;

}
