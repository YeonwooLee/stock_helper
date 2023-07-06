package com.example.stock_helper.util.crawling;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
/*
빈으로 빼서 싱글톤으로 쓰려는 시도 했었는데 이러면 driver.get('A') 크롤링중에 driver.get('b')당하면 섞이지 않나 싶어서 철회함
옵션은 괜찮으니 옵션만 빈으로
*/
public class MyCrawlerConfig {
    private WebDriver driver;
    private ChromeOptions options;




    @Bean
    @Lazy
    public WebDriver getWebDriver() {
        if (driver == null) {
            options = new ChromeOptions();
            options.addArguments("headless");
            options.addArguments("--remote-allow-origins=*");
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver(options);
        }
        return driver;
    }

    @Bean
    @Lazy
    public ChromeOptions getChromeOptions() {
        if (options == null) {
            options = new ChromeOptions();
            options.addArguments("headless");
        }
        return options;
    }

}