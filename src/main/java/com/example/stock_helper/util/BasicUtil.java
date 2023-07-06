package com.example.stock_helper.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BasicUtil {
    //객체를 객체로
    public static <T> T objectToObject(Object object,Class<T> responseType,boolean ignoreUnknownProperties){//ignoreUnknownProperties = 없는필드 무시
        ObjectMapper objectMapper = new ObjectMapper();
        if(ignoreUnknownProperties) objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        T afterConvert = objectMapper.convertValue(object,responseType);
        return afterConvert;
    }


}