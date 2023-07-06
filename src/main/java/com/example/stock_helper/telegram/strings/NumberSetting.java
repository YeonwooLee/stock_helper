package com.example.stock_helper.telegram.strings;

import lombok.Getter;

@Getter
public enum NumberSetting {
    RISE_RATE_FOR_SCHEDULE(9),
    HUNDRED_MILLION_FOR_SCHEDULE(300);

    private int num;
    NumberSetting(int num){
        this.num = num;
    }


}
