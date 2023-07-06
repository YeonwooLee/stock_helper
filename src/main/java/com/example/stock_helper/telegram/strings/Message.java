package com.example.stock_helper.telegram.strings;


import lombok.Getter;

@Getter
public enum Message {
    ALERT_SOMETHING("입니다\n"),
    TODAY_HOT_STOCK_MSG_HEADER_AUTO("<오늘의 HOT 자동알림 %d%%, %d억>\n"),
    TODAY_HOT_STOCK_MSG_HEADER("<오늘의 핫한 주식 목록>\n"),
    NOT_EXIST_STOCK_NAME("존재하지 않는 주식명: [%s]"),
    ONE_STOCK_INF("◎ %s [%.2f%%(%d위) / 거래대금 %d억(%d위)]\n(시총 : %d억 / PER : %.2f)\n(기관순매수 : %d주 / 외인순매수 : %d주)"),
    HOT_STOCK_INF("◎ %s [%.2f%%(%d위) / 거래대금 %d억(%d위)]\n(시총 : %d억 / PER : %.2f)\n(기관순매수 : %d주 / 외인순매수 : %d주)\n"),
    CONNECT_SUCCESS("연결성공"),
    MAKE_STOCK_LIST_SUCCESS("%d개 주식리스트갱신성공"),
    MAKE_STOCK_LIST_FAIL("주식리스트갱신실패"),
    LAST_STOCK_LIST_SIZE("마지막 주식 리스트 크기 = %d"),
    CONNECT_FAIL("연결실패"),
    STOCK_DICT_MESSAGE("%s\n>>>%s"),
    RE_CONNECT_ALERT("연결을 재시도합니다 반응이 올 때까지 대기해주세요."),
    ZOOSIKSAJUN("%s\n>>>%s\n기준시:%s");

    Message(String msgFormat){
        this.msgFormat = msgFormat;
    }
    private String msgFormat;
}