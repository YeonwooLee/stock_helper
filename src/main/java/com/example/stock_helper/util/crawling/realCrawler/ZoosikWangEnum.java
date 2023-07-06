package com.example.stock_helper.util.crawling.realCrawler;

import lombok.Getter;

@Getter
public enum ZoosikWangEnum {
    SITE_URL("https://blog.naver.com/nazoosikwang"),
    XPATH_OF_ELEMENT_GROUP("//*[@id=\"post-view223020286265\"]/div/div[2]");


    private String value;
    ZoosikWangEnum(String value){
        this.value=value;
    }
}



