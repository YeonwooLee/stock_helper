package com.example.stock_helper.telegram.strings;

import lombok.Getter;

@Getter
public enum Chat {
    STOCK_SEARCH("-856041870");

    Chat(String chatId){
        this.chatId = chatId;
    }
    private String chatId;
}
