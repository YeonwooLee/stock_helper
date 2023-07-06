package com.example.stock_helper.python;

import com.example.stock_helper.PathString;
import com.example.stock_helper.python.cybos5.CybosException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import org.springframework.stereotype.Component;

import java.io.*;


@Component
@RequiredArgsConstructor
@Slf4j
public class CybosConnection {
    private final ReadPython readPython;

    private PathString pathString = PathString.getInstance();
    public boolean runCybos() throws IOException, CybosException {
        log.info("CYBOS를 실행합니다");
        ObjectMapper mapper = new ObjectMapper();
        String workingDir = System.getProperty("user.dir");



        File file = new ClassPathResource(pathString.USER_INFO).getFile();
        System.out.println("file.toString() = " + file.toString());

        CybosUser cybosUser = mapper.readValue(file, CybosUser.class);

        String id = cybosUser.getId();
        String pwd = cybosUser.getPwd();
        String pwdcert = cybosUser.getPwdcert();


        // String[] command = new String[] {"C:\\DAISHIN\\STARTER\\ncStarter.exe", "/prj:cp", "/id:"+id, "/pwd:"+pwd, "/pwdcert:"+pwdcert, "/autostart"};
        String[] command = new String[] {pathString.DAISHIN_STARTER, "/prj:cp", "/id:"+id, "/pwd:"+pwd, "/pwdcert:"+pwdcert, "/autostart"};

        Process process = new ProcessBuilder(command).start();
        InputStream is = process.getInputStream();//Get an inputstream from the process which is being executed
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);

        String s = readPython.readPythonFile(String.class, "gui\\pyCode\\guiController", new String[]{"20"});
        System.out.println("s is"+s);
        log.info("Cybos 실행 완료");
        return true;
    }
}
