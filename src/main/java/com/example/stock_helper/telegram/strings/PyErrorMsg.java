package com.example.stock_helper.telegram.strings;

import lombok.Getter;

@Getter
public enum PyErrorMsg {
    NO_STOCK_NAME_ERROR("NO_CYBOS_CONNECTION_ERROR");
    PyErrorMsg(String msgFormat){
        this.msgFormat = msgFormat;
    }
    private String msgFormat;
}