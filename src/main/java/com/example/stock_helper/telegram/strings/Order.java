package com.example.stock_helper.telegram.strings;

import lombok.Getter;

@Getter
public enum Order {
    TODAY_HOT_STOCK("!"),
    EXCUTE_CYBOS_PLUS("연결!"),
    MAKE_STOCK_LIST("주식리스트생성!"),
    GET_LAST_STOCK_LIST_SIZE("마지막길이!"),
    STOCK_DICTIONARY("ㅡㅡ");

    Order(String orderCode){
        this.orderCode = orderCode;
    }
    private String orderCode;
}