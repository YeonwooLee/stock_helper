package com.example.stock_helper.stock;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StockRepository extends JpaRepository<Stock,String> {
    @Query("SELECT s FROM Stock s WHERE s.stockId.searchTime = (SELECT MAX(stockId.searchTime) FROM Stock)")
    List<Stock> findLatestStock();

    @Query("SELECT s FROM Stock s WHERE s.stockId.searchTime = (SELECT MAX(stockId.searchTime) FROM Stock) AND s.stockName = :stockName")
    List<Stock> findLatestStockByStockName(@Param("stockName") String stockName);

    @Query("SELECT MAX(stockId.searchTime) From Stock")
    List<String> findMaxSearchTime();
    @Query("SELECT MIN(stockId.searchTime) From Stock")
    List<String> findMinSearchTime();

    //searchTIme에 검색한 주식 리스트의 원소가 몇개였는지
    //새로 주식 리스트 받아올 때 숫자 맞는지 검증용으로 사용됨
    @Query("SELECT COUNT(*) FROM Stock s WHERE s.stockId.searchTime = :searchTime")
    Long countBySearchTime(@Param("searchTime") String searchTime);

}
