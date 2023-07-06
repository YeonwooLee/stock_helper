package com.example.stock_helper.util;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MyConverter {
    //제네릭 리스트를 엔터로 구분한 문자열로
    public <T> String listToMsg(List<T> list) {
        String result = list.stream()
                .map(Object::toString)
                .collect(Collectors.joining("\n"));
        return result;
    }
}
