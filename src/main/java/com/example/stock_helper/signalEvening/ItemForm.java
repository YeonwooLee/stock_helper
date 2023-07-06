package com.example.stock_helper.signalEvening;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ItemForm {
    private float stockRise;//상승률
    private long stockTransactionAmount;//거래대금
    private MultipartFile attachFile;//이브닝리포트
    private boolean writeDate;//날짜표기할껀지
    private boolean writeStockName;//명시적 주식명 표기 할껀지
}
