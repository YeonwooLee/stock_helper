package com.example.stock_helper.stock;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
@Entity
public class Stock {
    @EmbeddedId
    private StockId stockId;

    // @SerializedName("stock_name") //파이썬 리턴(dict)에서는 key가 stock_name임
    private String stockName;//종목명


    private float stockRise; //상승률
    private int riseRank;//상승 순위

    private long stockTransactionAmount; //거래대금
    private int amountRank;//거래대금 순위

    
    //가격
    private int openingPrice;//시가
    private int currentPrice;//현재가
    private int preClosingPrice;//전일 종가

    //시가총액
    private long marketCapitalization;//시가총액


    //PER
    private float per;//per
    private int perRank;//per순위

    //기관과외인
    private long dayForeignNetPurchase;
    private long dayInstitutionalNetPurchase;

    private long issuedShare;//상장주식 수



    public String testString(){
        StringBuilder sb = new StringBuilder();
        sb.append("종목명:"+stockName+"\n");
        sb.append("종목코드:"+stockId.getStockCode()+"\n");
        sb.append("상승률:"+stockRise+"\n");
        sb.append("상승 순위:"+riseRank+"\n");
        sb.append("거래대금:"+stockTransactionAmount+"\n");
        sb.append("거래대금 순위:"+amountRank+"\n");
        sb.append("시가:"+openingPrice+"\n");
        sb.append("현재가:"+currentPrice+"\n");
        sb.append("검색 시간(추정):"+stockId.getSearchTime()+"\n");

        return sb.toString();

    }
}
