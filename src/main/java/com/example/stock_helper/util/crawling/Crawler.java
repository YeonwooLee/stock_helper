package com.example.stock_helper.util.crawling;

import lombok.RequiredArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public abstract class Crawler<T> {
    public final ChromeOptions chromeOption;//싱글톤 크롬옵셥 -- 없애도 될듯?
    public final WebDriver driver;//싱글톤 드라이버

    public final String url;
    public final String xpathOfElementGroup;


    public abstract void movePage();

    public abstract T crawling();


}
