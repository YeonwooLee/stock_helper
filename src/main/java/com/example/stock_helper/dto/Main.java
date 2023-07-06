package com.example.stock_helper.dto;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    public static void main(String[] args) throws IOException{
        String s = br.readLine();
        String[] beforeMin = s.split("[-]");
        ArrayList<Integer> list = new ArrayList<>();
        for(int i=0;i<beforeMin.length;i++){
            list.add(cal(beforeMin[i]));
        }

        int result = list.get(0);
        for(int i=1;i<list.size();i++){
            result-=list.get(i);
        }

        System.out.println(result);

    }
    static int cal(String s){
        String[] arr = s.split("[+]");
        int result = 0;
        for(String value:arr){
            result += Integer.parseInt(value);
        }
        return result;

    }
}
