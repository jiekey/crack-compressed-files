package com.jiekey;

import lombok.SneakyThrows;

import java.io.File;

/**
 * @description:
 * @author: shijunjie
 * @time: 2020/1/10 11:23
 */
public class Main {

    @SneakyThrows
    public static void main(String[] args) {
        Configuration configuration = new Configuration()
                .setFile(new File("E:\\11.ZIP"))
                .setMinLen(6)
                .setMaxLen(6)
                .setEnableUpperCase(true)
                .setEnableLowerCase(true)
                .setEnableSymbol(true)
                .setEnableNumbers(true)
                .build()
                .info();
        PasswordGenerator passwordGenerator = new PasswordGenerator(configuration);
        ProcessHandler processHandler = new ProcessHandler(configuration, passwordGenerator);
        int status = processHandler.handler();
        String successFlag = "===================密码破解成功===================";
        String errorFlag =   "===================密码破解失败===================";
        String counterFlag = "共产生密码"+passwordGenerator.getCounter()+"个，破解"+processHandler.getCrackTimes()+"次";
        if (status == 1){
            System.out.println(String.format("%s\r\n%27s\r\n%30s\r\n%s", successFlag, configuration.getResult(), counterFlag, successFlag));
        } else if (status == -1){
            System.err.println(String.format("%s\r\n%30s\r\n%s", errorFlag, counterFlag, errorFlag));
        }

    }

}
