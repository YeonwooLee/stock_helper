package com.example.stock_helper.telegram.strings;

import lombok.Getter;

@Getter
public enum MyErrorMsg {
    NO_STOCK_NAME_ERROR("존재하지 않는 주식명: [%s]"),
    DISCONNECT_MAYBE("연결 끊김 의심"),
    DIFFERENCE_STOCK_COUNT("주식 수 다름 현재검색된주식수:%s");

    MyErrorMsg(String msgFormat){
        this.msgFormat = msgFormat;
    }
    private String msgFormat;
}