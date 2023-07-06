package com.example.stock_helper.util.crawling;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

// @SpringBootTest
class MyCrawlerTest {
    @Autowired
    MyCrawler myCrawler;
    @Test
    public void 파이널선언테스트(){
        //given
        final ArrayList<Integer> a = new ArrayList<>();
        a.add(3);
        System.out.println("a = " + a);

        //when

        // then
    }

    @Test
    public void 최초테스트() throws InterruptedException {
        //given
        Map<String, String> map = myCrawler.temp();
        //when


        // then
        Assertions.assertEquals("[전선, 광케이블, 윤석열, https://blog.naver.com/nazoosikwang/222992093919]",map.get("가온전선"));
    }

}