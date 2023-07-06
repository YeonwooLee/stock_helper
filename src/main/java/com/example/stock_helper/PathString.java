package com.example.stock_helper;


import org.springframework.security.core.parameters.P;

public class PathString {
    private static PathString instance;
    private PathString() {};
    public static PathString getInstance(){
        if(instance==null){
            instance = new PathString();
        }
        return instance;
    }
    public static final String USER_INFO = "userinfo.json";
    public static final String DAISHIN_STARTER = "C:\\DAISHIN\\STARTER\\ncStarter.exe";
    public static final String PYTHON_FILE_PATH = "pythonScript";
}
