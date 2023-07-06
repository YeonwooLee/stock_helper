package com.example.stock_helper.python;

import com.example.stock_helper.PathString;
import com.example.stock_helper.python.cybos5.CybosException;
import com.example.stock_helper.telegram.strings.PyErrorMsg;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class ReadPython {
    static PathString pathString = PathString.getInstance();
    public <T> T readPythonFile(Class<T> responseType,String pythonProgramName, String[] args) throws IOException, CybosException {
        //Group = com.mysite
        //Artifact = sbb
        //내부 패키지 = pythonScript
        // String workingDir = System.getProperty("user.dir");
        // System.out.println("workingDir = " + workingDir);


        // String path = workingDir + "\\src\\main\\java\\com\\example\\stock_helper\\pythonScript";
        String path = new ClassPathResource(pathString.PYTHON_FILE_PATH).getFile().toString();
        String command = String.format("python %s\\%s.py", path, pythonProgramName);
        System.out.println("command = " + command);
        for (String arg : args) {
            command += " " + arg;
        }
        log.info(String.format("파이썬 파일 실행, command = %s", command));
        String result = "";
        T mo = null;

        System.setProperty("PYTHONIOENCODING", "UTF-8");
        Process p = Runtime.getRuntime().exec(command);
        // Process p = new ProcessBuilder(command).start();


        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream(),"UTF-8"));
        String output = in.readLine(); //파이썬 dict 형식으로 출력(print)
        //스트림 닫기
        in.close();
        // System.out.println("output = " + output);
        //ERROR 처리
        if(output.contains(PyErrorMsg.NO_STOCK_NAME_ERROR.getMsgFormat())){
            throw new CybosException(PyErrorMsg.NO_STOCK_NAME_ERROR.getMsgFormat());
        }


        mo = new Gson().fromJson(output, responseType);//파이썬 dict형식 출력을 MyObject형식으로 변경
        return mo;
    }
}