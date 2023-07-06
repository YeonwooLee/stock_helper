package com.example.stock_helper.stock;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class StockId implements Serializable {
    private static final long serialVersionUID = -6651160910827639463L;
    //검색시간
    // private String searchTime;//검색 시간(추정)
    @Column
    private String searchTime;
    @Column // @SerializedName("stock_code")//파이썬 리턴(dict)에서는 key가 stock_code임
    private String stockCode;//종목코드
}
