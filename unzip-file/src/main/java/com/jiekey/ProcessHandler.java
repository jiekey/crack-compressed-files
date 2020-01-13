package com.jiekey;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description:
 * @author: shijunjie
 * @time: 2020/1/10 10:58
 */
@Slf4j
public class ProcessHandler {

    private PasswordGenerator passwordGenerator;
    private Configuration configuration;
    private boolean isCheck = false;
    private FileCrack fileCrack;
    //破解状态  0初始值 1破解完成 -1密码用完仍未破解
    private AtomicInteger status = new AtomicInteger(0);
    //密码破解次数
    @Getter
    private volatile long crackTimes;

    public ProcessHandler(Configuration configuration, PasswordGenerator passwordGenerator){
        this.configuration = configuration;
        this.passwordGenerator = passwordGenerator;
    }


    public int handler() throws Exception {
        fileCrack = configuration.getFileCrack();
        //检验文件合法性
        if (!isCheck){
            fileCrack.check(configuration.getFile());
        }
        //破解
        List<Thread> tasks = new ArrayList<>();
        for (int i = 0; i < configuration.getAlive(); i++) {
            Thread thread = new Thread(new Activity());
            tasks.add(thread);
            thread.start();
        }
        while (true){
            int s = status.get();
            if (s == 1 || s == -1){
                return s;
            }
        }
    }

    class Activity implements Runnable {

        @SneakyThrows
        @Override
        public void run() {
            while (status.get() == 0){
                String password = passwordGenerator.getPassword();
                crackTimes++;
                if (password != null){
                    log.info("正使用密码 {} 破解", password);
                    boolean crack = fileCrack.crack(configuration.getFile(), password);
                    if (crack){
                        log.info("破解完成");
                        configuration.setResult(password);
                        status.set(1);
                    }
                } else {
                    if (status.get() == 0){
                        log.info("密码使用完仍未破解");
                        status.set(-1);
                    }
                }
                Thread.currentThread().interrupt();
            }
            //log.info("线程{}结束",Thread.currentThread().getName());
        }
    }

}
