package com.example.stock_helper.util.crawling.realCrawler;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ZoosikWangCrawlerTest {
    @Autowired
    ZoosikWangCrawler zoosikWangCrawler;
    @Test
    void crawling() {
        zoosikWangCrawler.movePage();
        zoosikWangCrawler.crawling();

    }
}