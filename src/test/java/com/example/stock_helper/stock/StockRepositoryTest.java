package com.example.stock_helper.stock;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StockRepositoryTest {
    @Autowired
    StockRepository stockRepository;

    @Test
    public void 시간으로주식수검색(){
        //given
        String time = "2023-02-09 18:56:00.000";
        long stockCnt = 2764;
        //when
        long count = stockRepository.countBySearchTime(time);

        // then
        System.out.println("count = " + count);
        Assertions.assertEquals(stockCnt,count);
    }

}