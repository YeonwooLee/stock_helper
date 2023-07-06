package com.example.stock_helper.util.crawling.realCrawler;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ZoosikSajunRepository extends JpaRepository<ZoosikSajun,Integer> {
    ZoosikSajun findByStockName(String stockName);
    List<ZoosikSajun> findByStockNameOrderByIdDesc(String StockName);
}
