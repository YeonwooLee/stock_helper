package com.example.stock_helper.python;

import com.example.stock_helper.PathString;
import com.example.stock_helper.python.cybos5.CybosException;
import com.example.stock_helper.stock.Stock;
import com.example.stock_helper.stock.StockService;
import com.example.stock_helper.telegram.strings.Message;
import com.example.stock_helper.util.BasicUtil;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

import static com.example.stock_helper.telegram.strings.MyErrorMsg.NO_STOCK_NAME_ERROR;

@Slf4j
@Component
@RequiredArgsConstructor
@Setter
public class StockFinder {
    private final ReadPython readPython;

    static PathString pathString = PathString.getInstance();
    //전체 주식 리스트
    public List<Stock> getStocks() throws IOException, CybosException {
        Stock[] stocks = readPython.readPythonFile(Stock[].class, "allStockInfo", new String[]{""});
        return Arrays.asList(stocks);
    }

}
