package com.example.stock_helper.python;

import com.example.stock_helper.python.cybos5.CybosException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class CybosConnectionTest {
    @Autowired
    CybosConnection cybosConnection;
    
    @Test
    public void 싸이보스플러스연결테스트() throws IOException, CybosException {
        //given
        
        //when
        cybosConnection.runCybos();
        // then
    }

}