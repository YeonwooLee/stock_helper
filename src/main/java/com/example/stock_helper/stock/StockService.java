package com.example.stock_helper.stock;

import com.example.stock_helper.python.CybosConnection;
import com.example.stock_helper.python.StockFinder;
import com.example.stock_helper.python.cybos5.CybosException;
import com.example.stock_helper.telegram.strings.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;

import static com.example.stock_helper.telegram.strings.MyErrorMsg.DIFFERENCE_STOCK_COUNT;
import static com.example.stock_helper.telegram.strings.MyErrorMsg.NO_STOCK_NAME_ERROR;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockService {
    private final StockFinder stockFinder;
    private final StockRepository stockRepository;
    private final CybosConnection cybosConnection;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    //5분마다 업뎃침
    @Scheduled(cron = "30 */5 * * * *") //5분마다 매 n분30초에 업뎃
    // @Scheduled(cron = "30 */1 * * * *") //1분마다 매 n분30초에 업뎃
    public void scheduledReportCurrentTime() throws IOException, CybosException {
        LocalTime currentTime = LocalTime.now();
        LocalTime startTime = LocalTime.of(8, 14);
        LocalTime endTime = LocalTime.of(18, 1);
        log.info("스케줄러 동작 시간: {}",currentTime.toString());

        //장중 시간이 아닌 경우
        if (!(currentTime.isAfter(startTime) && currentTime.isBefore(endTime))) {
            // It's between 18:01 and 08:14, so skip execution
            log.info("시간이 장중이 아닙니다");
            return;
        }
        log.info("주식 정보 업데이트 시작");
        reportCurrentTime();
    }

    //주식 최신화 with pythonScript
    // @Scheduled(cron = "30 */30 * * * *") //
    @Transactional
    public int reportCurrentTime() throws IOException, CybosException {
        // stockFinder.setTempStock(stockFinder.getStocks());
        log.info("주식리스트 갱신 {}", dateFormat.format(new Date()));
        String lastCheckTime= getLastTime();//최초 리스트 작성 시점


        boolean flag = true;
        List<Stock> stocks = new ArrayList<>();
        //TODO db에 주식 리스트 갱신 현재시간+주식코드 = pk, 주식명, 현재가, 등수 등
        try{
            stocks = stockFinder.getStocks();
        }catch(CybosException e){
            cybosConnection.runCybos();
            stocks = stockFinder.getStocks();
        }finally {
            if(stocks.size()==0){
                throw new RuntimeException("주식 리스트 갱신 실패");
            }
        }

        boolean isOk = validateStockList(stocks,lastCheckTime);
        log.info("isOk={}",isOk);
        log.info("isOk="+isOk);
        if(isOk){
            log.info("신규 주식 목록 저장 start");
            for (Stock stock : stocks) {
                stockRepository.save(stock);
            }
            log.info("신규 주식 목록 저장 완료");
            return stocks.size();

        }else{
            log.info("주식 수 부족");
            throw new RuntimeException(String.format(DIFFERENCE_STOCK_COUNT.getMsgFormat(),stocks.size()));
        }



    }

    //최신스탁리스트
    public List<Stock> getLatestStock(){
        return stockRepository.findLatestStock();
    }

    //단일 주식
    public Stock getStockDetail(String stockName){
        // List<Stock> stocks = getStocks(); //지금 따끈하게 구어오는 전체 주식 리스트
        List<Stock> stocks = stockRepository.findLatestStockByStockName(stockName);
        //주식명이 주식 리스트에 없음 에러
        if(stocks.size()==0) throw new RuntimeException(String.format(NO_STOCK_NAME_ERROR.getMsgFormat(),stockName));
        return stocks.get(0);
    }
    //오늘의 핫한 주식
    public List<String> makeTodayHotStock(float riseRate, long hundredMillion){
        log.info("todayHot >>> riseRate = {}, hundredMillion = {}",riseRate, hundredMillion);
        List<Stock> stocks = getLatestStock();//
        List<Stock> result = new ArrayList<>();

        if(riseRate<0){
            for(Stock st : stocks){
                if(st.getStockTransactionAmount()>=hundredMillion && st.getStockRise()<=riseRate){
                    result.add(st);
                }
            }
        }
        else{
            for(Stock st : stocks){
                if(st.getStockTransactionAmount()>=hundredMillion && st.getStockRise()>=riseRate){
                    result.add(st);
                }
            }
        }



        //이름순정렬
        Collections.sort(result, new Comparator<Stock>(){
            @Override
            public int compare(Stock s1, Stock s2){
                return s1.getStockName().compareTo(s2.getStockName());
            }
        });

        List<String> finalResult = new ArrayList<>(); //스트링 포멧 맞추기
        for(int i=0;i<result.size();i++){//Stock 리스트 순회
            Stock stock = result.get(i);//현재 주식

            //문자열화 하기 위한 데이터 필드
            String stockName = stock.getStockName();
            float stockRise = stock.getStockRise();
            int riseRank = stock.getRiseRank();
            long stockTransactionAmount = stock.getStockTransactionAmount() / 100000000;
            int amountRank = stock.getAmountRank();
            float per = stock.getPer();
            long marketCapitalization = stock.getMarketCapitalization()/100000000;
            long dayForeignNetPurchase = stock.getDayForeignNetPurchase();
            long dayInstitutionalNetPurchase = stock.getDayInstitutionalNetPurchase();

            //문자열 포멧팅
            String strStock = String.format(Message.HOT_STOCK_INF.getMsgFormat(),
                    stockName,
                    stockRise,
                    riseRank,
                    stockTransactionAmount,
                    amountRank,
                    marketCapitalization,
                    per,
                    dayInstitutionalNetPurchase,
                    dayForeignNetPurchase);

            //최종결과리스트에 삽입
            finalResult.add(strStock);
        }
        return finalResult;
    }
    //오늘의 핫한 주식 이름만(이브닝리포트 변환용)
    public List<String> makeTodayHotStockOnlyName(float riseRate, long hundredMillion){
        log.info("todayHot >>> riseRate = {}, hundredMillion = {}",riseRate, hundredMillion);
        List<Stock> stocks = getLatestStock();//
        List<String> finalResult = new ArrayList<>(); //스트링 포멧 맞추기
        if(riseRate<0){
            for(Stock st : stocks){
                if(st.getStockTransactionAmount()>=hundredMillion && st.getStockRise()<=riseRate){
                    finalResult.add(st.getStockName());
                }
            }
        }
        else{
            for(Stock st : stocks){
                if(st.getStockTransactionAmount()>=hundredMillion && st.getStockRise()>=riseRate){
                    finalResult.add(st.getStockName());
                }
            }
        }
        return finalResult;
    }

    //마지막 검색 시간
    public String getLastTime(){
        String lastSearchTime = stockRepository.findMaxSearchTime().get(0);
        log.info("마지막 검색 시간 = {}",lastSearchTime);
        return lastSearchTime;
    }
    
    //처음 검색 시간
    public String getFirstTime(){
        return stockRepository.findMinSearchTime().get(0);
    }

    //마지막 주식 리스트 길이
    public long getLastStockListSize(){
        return stockRepository.countBySearchTime(getLastTime());
    }
    //새로받아온 주식 리스트 검증
    boolean validateStockList(List<Stock> newStocks, String firstCheckTime){//이번에 만든 주식 리스트, 이전 검색 시점

        long lastStockListSize = stockRepository.countBySearchTime(firstCheckTime);
        log.info("마지막 주식 사이즈 = {}",lastStockListSize);
        if(lastStockListSize==0) {
            log.info("서버에서 프로젝트 최초 구동");
            return true;
        }

        long newStockListSize = newStocks.size();
        log.info("최근 주식 사이즈 = {}",newStocks.size());

        boolean result = (newStockListSize>=lastStockListSize-15)?true:false;
        log.info("새로만든 주식 목록이ㅣ 기존 주식보다 크다 = {}",result);
        return result;
    }

    public boolean passMinuteLastCheck(String time,int diff) throws ParseException {
        //TODO 마지막 검색 시간보다 3분 이상 지났으면 reportCurrentTime() 
        //TODO String input = "2023-02-08 00:00:00.000"을 시간화 하는 것부터 시작
        return true;
    }
}