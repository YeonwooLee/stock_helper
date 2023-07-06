package com.example.stock_helper.util.crawling;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class MyCrawler {
    private final ChromeOptions chromeOption;
    private final WebDriver driver;
    public Map<String,String> temp() throws InterruptedException {

        // 옵션
        // 옵션만 싱글톤화
        // ChromeOptions options = new ChromeOptions();
        // options.addArguments("headless");

        // Create a new instance of the ChromeDriver
        // WebDriverManager.chromedriver().setup();
        // WebDriver driver = new ChromeDriver(chromeOption);



        // driver.get("https://bonigarcia.dev/selenium-webdriver-java/");
        // driver.findElement(By.partialLinkText("form")).click();

        // Navigate to the desired webpage
        driver.get("https://blog.naver.com/nazoosikwang");
        driver.switchTo().frame("mainFrame");//frame id

        // Find the element by ID and click on it
        // WebElement element = driver.findElement(By.partialLinkText("산업"));//이런 것도 있다

        //주식사전 클릭
        WebElement element = driver.findElement(By.xpath("//*[@id=\"category282\"]"));
        element.click();

        //개별p를 클래스로 가져옴
        // List<WebElement> elements = driver.findElements(By.className("se-text-paragraph-align-"));

        //원본주식 tag
        WebElement zoosikDict = driver.findElement(By.xpath("//*[@id=\"post-view223020286265\"]/div/div[2]"));
        //문자열추출
        String[] zoosikArr = zoosikDict.getText().split("\n");

        // Get the page source
        String pageSource = driver.getPageSource();



        Map<String,String> map = new HashMap<>();
        for(int i=0;i< zoosikArr.length;i++){
            String cur = zoosikArr[i];
            if(!cur.contains(":")) continue;//:없으면 continue;
            String[] keyAndVal = cur.split(":",2);

            String key = keyAndVal[0];
            List<String> vals= Arrays.stream(keyAndVal[1].replace("/ h",",h").replace(" ","").split(",")).toList();

            map.put(key,vals.toString());
        }
        // Close the WebDriver
        // driver.quit();

        return map;
    }
}
