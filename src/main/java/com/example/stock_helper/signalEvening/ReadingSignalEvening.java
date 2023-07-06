package com.example.stock_helper.signalEvening;

import com.example.stock_helper.stock.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class ReadingSignalEvening {
    private final StockService stockService;

    public String parsing(String filePath, ItemForm form) throws IOException {
        boolean writeDate = form.isWriteDate();
        //make path that platform-independent
        // Path filePath = Paths.get("C:","PdfBox_Examples","my_doc.pdf");
        float stockRise = form.getStockRise();
        long stockTransactionAmount = form.getStockTransactionAmount();

        log.info("stockRise={}, 거래대금={}",stockRise,stockTransactionAmount);
        List<String> stockList = stockService.makeTodayHotStockOnlyName(stockRise,stockTransactionAmount*100000000);
        log.info("stockList={}",stockList);


        //Loading an existing document
        File file = new File(filePath);
        PDDocument document = PDDocument.load(file);

        //Instantiate PDFTextStripper class
        PDFTextStripper pdfStripper = new PDFTextStripper();

        //Retrieving text from PDF document
        String text = pdfStripper.getText(document);
        // System.out.println(text);

        //Closing the document
        document.close();


        //파싱용 재료 생성
        //1. 기존 문자열
        String[] before = text.split("\n");
        //2. 오늘의 주식 리스트

        //파싱
        String after = extractCore(before, stockList,writeDate,form);
        System.out.println("시작");
        // System.out.println(after);

        return after;
    }

    String extractCore(String[] before, List<String> stockList, boolean writeDate, ItemForm form){
        //시그널 이브닝에 없는 애들 체크용
        List<String> notInSignalEvening = new ArrayList<>();
        for(String s:stockList){
            notInSignalEvening.add(s);
        }


        //결과
        String res = "";
        //key: 테마명
        //value: List [종목문단1, 종목문단2]
        Map<String,List<String>> map = new LinkedHashMap<>();

        //문단 저장용 전역 String
        String temp = "";
        //테마 저장용
        String curTheme = "";

        //모드
        boolean isAppendMode = false; //문단 추가 모드

        for(int i=0;i<before.length;i++){
            String cur = before[i];
            if(isAppendMode){
                //문단 추가 모드

                temp+=cur+"\n</br>";//있으면 줄변경 리포트와 맞춤
                // temp+=cur;//줄변경 없앰 과정에서 추가
                
                //빈 줄 만나면 문단 추가 모드 종료
                if(cur.length()==2) {
                    temp+="</br></br></br>";//줄변경 없앰 과정에서 추가
                    isAppendMode=false;
                    map.get(curTheme).add(temp);
                }
            }else{
                //문단 추가 모드 X (==탐색모드)

                if(isTheme(cur)){
                    //cur이 테마명이면
                    //map에 없는 테마면 넣는다
                    map.putIfAbsent(cur,new ArrayList<>());
                    curTheme = cur;//테마설정
                }else if(isHotStock(cur,stockList,notInSignalEvening).isContain){
                    //stockList의 원소 중에 cur의 내용에 포함되는 원소가 있다면

                    temp=cur+"\n</br>";//temp를 현재행으로 초기화

                    //내가 문단 시작이 아닌 경우 앞부분 추가
                    int t = i;
                    while(--t>=0){
                        String back = before[t];
                        if(isTheme(back) || back.length()==2) break;//테마명이나 공백 만나면 종료
                        temp = back+temp;
                    }



                    isAppendMode=true;//문단 추가 모드 실행
                    if(writeDate){//날짜표기체크시
                        LocalDateTime now = LocalDateTime.now();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        String formattedDateTime = now.format(formatter);
                        temp = String.format("검색일:%s<br/> %s",formattedDateTime,temp);


                    }
                    if(form.isWriteStockName()){
                        temp = "주식명="+isHotStock(cur,stockList,notInSignalEvening).stockName+"</br>"+temp;
                    }
                    

                }
            }
        }

        //map의 내용을 String화
        for(String key: map.keySet()){
            List<String> list = map.get(key);

            // 테마명 기재
            res+="</br>"+key+"</br>";
            //문단기재
            for(String s:list){
                res+=s;
            }
        }
        for(int i=notInSignalEvening.size()-1;i>=0;i--){
            String cur = notInSignalEvening.get(i);
            if(res.contains(cur)){
                //결과텍스트에 종목 있으면
                notInSignalEvening.remove(i);//이브닝에 없는 목록에서 삭제한다
            }
        }
        res+="<h2>시그널이브닝에는 없는 리스트</h2></br></br>";
        res+=notInSignalEvening.toString();
        return res;
    }

    boolean isTheme(String s){
        return s.contains("<") && s.contains(">");
    }

    IsHotStockResult isHotStock(String s, List<String> stockList, List<String> notInSignalEvening){
        IsHotStockResult result = new IsHotStockResult();
        for(String stock:stockList){
            if(s.contains(stock)) {
                // System.out.println("s = " + s);
                // System.out.println("stock = " + stock);
                // System.out.println();

                result.isContain = true;
                result.stockName = stock;

                if(notInSignalEvening.contains(stock)){
                    notInSignalEvening.remove(stock);
                }
                break;
            }
        }
        return result;
    }

    static class IsHotStockResult{
        boolean isContain;
        String stockName;

    }
}