package com.example.stock_helper.telegram;


import com.example.stock_helper.python.StockFinder;
import com.example.stock_helper.stock.Stock;
import com.example.stock_helper.stock.StockRepository;
import com.example.stock_helper.stock.StockService;
import com.example.stock_helper.telegram.strings.Message;
import com.example.stock_helper.telegram.strings.NumberSetting;
import com.example.stock_helper.util.MyConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.example.stock_helper.telegram.strings.Chat.STOCK_SEARCH;
import static com.example.stock_helper.telegram.strings.Message.TODAY_HOT_STOCK_MSG_HEADER_AUTO;
import static com.example.stock_helper.telegram.strings.NumberSetting.HUNDRED_MILLION_FOR_SCHEDULE;
import static com.example.stock_helper.telegram.strings.NumberSetting.RISE_RATE_FOR_SCHEDULE;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduledTasks {
    private final StockFinder stockFinder;
    private final TelegramBotsApi telegramBotsApi;
    private final EchoBot echoBot;
    private final MyConverter myConverter;
    private final StockService stockService;


    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    private static final long MILLION = 100_000_000;

    @Scheduled(cron = "0 0 12,18 * * *") //0초 0분 12시, 18시 매일 매월 매년
    public void autoHotAlert() throws TelegramApiException {//오늘의 HOT 주식 자동 알림
        String timeText = dateFormat.format(new Date())+Message.ALERT_SOMETHING.getMsgFormat();//시간 + 입니다.
        // stockFinder.setTempStock(stockFinder.getStocks());
        log.info("자동생성 리스트");
        List<String> stockStrings = stockService.makeTodayHotStock(RISE_RATE_FOR_SCHEDULE.getNum(),
                HUNDRED_MILLION_FOR_SCHEDULE.getNum()*MILLION);//300을 300억으로변환하기위해 1억 곱함


        String todayHotStockMsgHeaderAuto = String.format(//<오늘의 자동알림 HOT %d%, %d억>
                TODAY_HOT_STOCK_MSG_HEADER_AUTO.getMsgFormat(),
                RISE_RATE_FOR_SCHEDULE.getNum(),HUNDRED_MILLION_FOR_SCHEDULE.getNum()
        );

        String listToMsg = myConverter.listToMsg(stockStrings);
        echoBot.execute(SendMessage.builder()
                .text(timeText + todayHotStockMsgHeaderAuto + listToMsg)//리스트를 \n 구분 스트링으로
                .chatId(STOCK_SEARCH.getChatId())
                .build());

    }
}