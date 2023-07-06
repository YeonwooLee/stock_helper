package com.example.stock_helper.telegram;
import com.example.stock_helper.python.CybosConnection;
import com.example.stock_helper.python.cybos5.CybosException;
import com.example.stock_helper.stock.Stock;
import com.example.stock_helper.python.StockFinder;
import com.example.stock_helper.stock.StockService;
import com.example.stock_helper.telegram.strings.Chat;
import com.example.stock_helper.telegram.strings.Message;
import com.example.stock_helper.telegram.strings.MyErrorMsg;
import com.example.stock_helper.telegram.strings.Order;
import com.example.stock_helper.util.MyConverter;
import com.example.stock_helper.util.crawling.MyCrawler;
import com.example.stock_helper.util.crawling.realCrawler.ZoosikSajun;
import com.example.stock_helper.util.crawling.realCrawler.ZoosikSajunRepository;
import com.example.stock_helper.util.crawling.realCrawler.ZoosikSajunService;
import com.example.stock_helper.util.crawling.realCrawler.ZoosikWangCrawler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;


import static com.example.stock_helper.telegram.strings.Message.RE_CONNECT_ALERT;
import static com.example.stock_helper.telegram.strings.Message.TODAY_HOT_STOCK_MSG_HEADER;

@RequiredArgsConstructor
@Component
public class EchoBot extends TelegramLongPollingBot {

    private final StockFinder stockFinder;
    private final MyConverter myConverter;
    private final CybosConnection cybosConnection;
    private final StockService stockService;

    // private final MyCrawler myCrawler;
    // private final ZoosikWangCrawler zoosikWangCrawler;
    private final ZoosikSajunService zoosikSajunService;


    private final String ROOM_STOCK_SEARCH = "-856041870";

    @Override
    public String getBotUsername() {
        return StockEye2Bot.BOT_USER_NAME;
    }

    @Override
    public String getBotToken() {
        return StockEye2Bot.BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage mensagem = null;
            try {
                mensagem = responder(update);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            // System.out.println("받은 메시지 정보 = " + mensagem);
            try {
                try {
                    execute(mensagem);
                }catch(TelegramApiException e){
                    if(e.getMessage().contains("message is too long")){
                        String longMsg = mensagem.getText();
                        String front = longMsg.substring(0,longMsg.length()/2);
                        String back = longMsg.substring(longMsg.length()/2);
                        System.out.println(mensagem.getChatId());

                        execute(SendMessage.builder()
                                .text(front)
                                .chatId(mensagem.getChatId())
                                .build());
                        execute(SendMessage.builder()
                                .text(back)
                                .chatId(mensagem.getChatId())
                                .build());
                    }else{
                        e.printStackTrace();
                    }
                }

                //개씹임시추가시작
                var chatId = update.getMessage().getChatId().toString();
                var temp = "데이터 기준시" + stockService.getLastTime();
                temp+=chatId;

                execute(SendMessage.builder()
                        .text(temp)
                        .chatId(chatId)
                        .build());
                //개씹임시추가종료
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
    public void sendMsg(String chatId, String msg) throws TelegramApiException {
        execute(SendMessage.builder()
                .text(msg)
                .chatId(chatId)
                .build());
    }
    private SendMessage responder(Update update) throws TelegramApiException {
        var userText = update.getMessage().getText(); //들어온 채팅
        var chatId = update.getMessage().getChatId().toString();
        String order = "";
        var msg =  userText;//답장 기본적으로 유저 인풋 그대로


        //TODO 만약 최근 3분 안에 업뎃 친게 없다면 업뎃친다.
        try {
            makeUsableState();
        } catch (ParseException e) {
            msg = "주식리스트 검증 오류";
            e.printStackTrace();
        }


        if(userText.startsWith("이거로시작하면") && chatId.equals(Chat.STOCK_SEARCH.getChatId())){
            msg = "이 메세지를 출력한다";
        }//주식상세정보
        else if(userText.startsWith(".") && chatId.equals(Chat.STOCK_SEARCH.getChatId())){//.로 시작하는 메세지 && STOCK_SEARCH방
            order = userText.replace(".", "");
            msg = stockDetailToString(order);
        }// !!float 상승률,int 억
        else if(userText.startsWith(Order.TODAY_HOT_STOCK.getOrderCode())){
            msg = "ERROR!";
            try{
                order = userText.replace(Order.TODAY_HOT_STOCK.getOrderCode(),"");//명령에서 명령코드("!") 제거 -> !float 상승률,int 억
                msg = TODAY_HOT_STOCK_MSG_HEADER.getMsgFormat() + myConverter.listToMsg(getTodayHotStock(order));
            }catch (Exception e){
                e.printStackTrace();
            }


        }else if(userText.startsWith(Order.EXCUTE_CYBOS_PLUS.getOrderCode())){
            msg = runCybosPlus();
        }else if(userText.startsWith(Order.MAKE_STOCK_LIST.getOrderCode())){
            int newStockListSize = 0;//주식리스트 갱신
            try {
                msg = mkNesStockList();

            } catch (IOException e) {
                msg = e.getMessage();
                // e.printStackTrace();
            } catch (CybosException e){
                msg = e.getMessage()+RE_CONNECT_ALERT.getMsgFormat();
                sendMsg(ROOM_STOCK_SEARCH,msg);

                msg = runCybosPlus();
                if(msg.equals(Message.CONNECT_SUCCESS.getMsgFormat())){
                    msg = "연결성공";
                }
                // e.printStackTrace();
            }


        }else if(userText.startsWith(Order.GET_LAST_STOCK_LIST_SIZE.getOrderCode())){
            long lastStockListSize = stockService.getLastStockListSize();//주식리스트 갱신
            msg = String.format(Message.LAST_STOCK_LIST_SIZE.getMsgFormat(),lastStockListSize);
        }
        else if(userText.startsWith(Order.STOCK_DICTIONARY.getOrderCode())){
            order = userText.replace(Order.STOCK_DICTIONARY.getOrderCode(), "");//ㅡㅡ를 ""로
            try{
                ZoosikSajun lastZoosikByName = zoosikSajunService.findLastZoosikByName(order);
                String theme = lastZoosikByName.getTheme();
                String searchTime = lastZoosikByName.getSearchTime();
                msg = String.format(Message.ZOOSIKSAJUN.getMsgFormat(),order,theme,searchTime);
            }catch (RuntimeException e){
                msg=e.getMessage();
            }



        }



        if(chatId.equals(Chat.STOCK_SEARCH.getChatId())){//채팅아이디가 이거면
            //이짓을 한다
            // msg="★ROOM [STOCK_SEARCH]★\n"+msg;
            msg = msg;
        }



        return SendMessage.builder()
                .text(msg)
                .chatId(chatId)
                .build();
    }

    private String mkNesStockList() throws IOException, CybosException {
        String msg = "";
        int newStockListSize = stockService.reportCurrentTime();
        msg = String.format(Message.MAKE_STOCK_LIST_SUCCESS.getMsgFormat(),newStockListSize);
        return msg;
    }
    private boolean makeUsableState() throws ParseException {
        //TODO 이거 구현
        stockService.passMinuteLastCheck("currentTime",3);
        return true;
    }


    private String runCybosPlus(){
        boolean result;
        try {
            result = cybosConnection.runCybos(); // 연결성공
            mkNesStockList();
        } catch (IOException | CybosException e) {
            e.printStackTrace();
            result = false; //연결실패
        }


        return result?Message.CONNECT_SUCCESS.getMsgFormat():Message.CONNECT_FAIL.getMsgFormat();
        
    }
    //오늘의 핫한주식
    private List<String> getTodayHotStock(String order){//order = "상승률,몇억이상"
        String[] orders = order.split(",");
        float riseRate = Float.parseFloat(orders[0]);//상승률
        long hundredMillion = Long.parseLong(orders[1])*100000000;//몇 억 이상인지 찾는용
        List<String> todayHotStocks = stockService.makeTodayHotStock(riseRate,hundredMillion);
        return todayHotStocks;
    }

    //주식 메인 정보
    private String stockDetailToString(String stockName){
        String result = "임시";
        try {
            Stock stock = stockService.getStockDetail(stockName);


            float stockRise = stock.getStockRise();
            int riseRank = stock.getRiseRank();
            long stockTransactionAmount = stock.getStockTransactionAmount() / 100000000;
            int amountRank = stock.getAmountRank();
            float per = stock.getPer();
            long marketCapitalization = stock.getMarketCapitalization()/100000000;
            long dayForeignNetPurchase = stock.getDayForeignNetPurchase();
            long dayInstitutionalNetPurchase = stock.getDayInstitutionalNetPurchase();

            result = String.format(Message.ONE_STOCK_INF.getMsgFormat(),
                    stockName,
                    stockRise,
                    riseRank,
                    stockTransactionAmount,
                    amountRank,
                    marketCapitalization,
                    per,
                    dayInstitutionalNetPurchase,
                    dayForeignNetPurchase);
            return result;
        }catch(RuntimeException e){
            if (e.getMessage().equals(String.format(MyErrorMsg.NO_STOCK_NAME_ERROR.getMsgFormat(),stockName))){
                e.printStackTrace();
                return String.format(Message.NOT_EXIST_STOCK_NAME.getMsgFormat(),stockName);
            }
            return MyErrorMsg.DISCONNECT_MAYBE.getMsgFormat();
        }
    }



}