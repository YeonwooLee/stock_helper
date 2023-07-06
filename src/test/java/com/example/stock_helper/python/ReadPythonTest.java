package com.example.stock_helper.python;

import com.example.stock_helper.python.cybos5.CybosException;
import com.example.stock_helper.stock.Stock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

class ReadPythonTest {
    @Autowired
    ReadPython readPython;
    @Test
    public void 파이썬_임시_테스트() throws IOException, CybosException {
        //given

        //when
        Integer getStockDetailDTO = readPython.readPythonFile(Integer.class, "cybos5\\temp", new String[]{"111123123"});
        // then
        System.out.println("getStockDetailDTO = " + getStockDetailDTO);

    }

    @Test
    public void 파이썬_받아오기_테스트() throws IOException, CybosException {
        //given
        String findStockName = "삼성전자";

        //when
        Stock getStockDetailDTO = readPython.readPythonFile(Stock.class, "cybos5\\getStockDetail", new String[]{findStockName});
        // then
        System.out.println("getStockDetailDTO = " + getStockDetailDTO);
        String temp = getStockDetailDTO.getStockName();
        String result = temp;
        assertEquals(findStockName,result);
    }

    @Test
    public void temp(){
        //given
        String temp = "asdf";
        //when
        String a = temp.replace("a", "!!");
        // then
        System.out.println("a = " + a);
        System.out.println("temp = " + temp);
    }
}