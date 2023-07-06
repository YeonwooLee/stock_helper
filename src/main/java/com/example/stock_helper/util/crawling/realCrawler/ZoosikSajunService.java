package com.example.stock_helper.util.crawling.realCrawler;


import com.example.stock_helper.telegram.strings.MyErrorMsg;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ZoosikSajunService {
    private final ZoosikSajunRepository zoosikSajunRepository;
    private final ZoosikWangCrawler zoosikWangCrawler;

    public ZoosikSajun findLastZoosikByName(String stockName){
        List<ZoosikSajun> zoosik = zoosikSajunRepository.findByStockNameOrderByIdDesc(stockName);
        if(zoosik.size()==0){
            throw new RuntimeException("주식사전미등재"+stockName);
        }
        return zoosik.get(0);
    }
    @Scheduled(cron = "0 0 */12 * * *")
    @Transactional
    public List<ZoosikSajun> updateZoosikSajun(){
        String searchTime = getCurTime();
        List<ZoosikSajun> list = zoosikWangCrawler.crawling();
        for(int i=0;i<list.size();i++){
            ZoosikSajun cur = list.get(i);
            cur.setSearchTime(searchTime);//시간지정
            zoosikSajunRepository.save(cur);
        }
        log.info("주식사전업데이트");
        return list;

    }
    private String getCurTime(){
        Date now = new Date();

        // 현재 날짜/시간 출력
        // System.out.println(now); // Thu Jun 17 06:57:32 KST 2021
        //

        // 포맷팅 정의
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 ss초");

        // 포맷팅 적용
        String formatedNow = formatter.format(now);
        return formatedNow;

    }
}
