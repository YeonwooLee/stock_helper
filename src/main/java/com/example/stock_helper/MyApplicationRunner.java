package com.example.stock_helper;

import com.example.stock_helper.python.CybosConnection;
import com.example.stock_helper.python.cybos5.CybosException;
import com.example.stock_helper.stock.StockService;
import com.example.stock_helper.telegram.EchoBot;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;


@Component
@RequiredArgsConstructor
public class MyApplicationRunner implements ApplicationRunner {
    private final CybosConnection cybosConnection;
    private final StockService stockService;
    private final EchoBot echoBot;

    @Override
    public void run(ApplicationArguments args) throws IOException, CybosException, TelegramApiException {
        // return;
        echoBot.sendMsg("-856041870","프로그램 시작");
        cybosConnection.runCybos();

        stockService.reportCurrentTime();
    }
}
