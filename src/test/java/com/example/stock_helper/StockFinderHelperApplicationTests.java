package com.example.stock_helper;

import com.example.stock_helper.python.cybos5.CybosException;
import com.example.stock_helper.stock.Stock;
import com.example.stock_helper.python.StockFinder;
import com.example.stock_helper.stock.StockRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

@SpringBootTest
class StockFinderHelperApplicationTests {
	@Autowired
	StockFinder stockFinder;
	@Autowired
	StockRepository stockRepository;


	@Test
	public void 파이썬에서전체주식리스트생성() throws IOException, CybosException {
	    //given

	    //when
		List<Stock> stocks = stockFinder.getStocks();
		String s = stocks.get(0).toString();
		System.out.println("s = " + s);
		// then

		Assertions.assertEquals("동화약품",stocks.get(0).getStockName());
	}

	@Test
	public void 주식저장() throws IOException, CybosException {
	    //given
		List<Stock> stocks = stockFinder.getStocks();
		Stock stock = stocks.get(0);
		stockRepository.save(stock);
	    //when

	    // then
	}
}
