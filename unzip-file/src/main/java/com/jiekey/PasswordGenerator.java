package com.jiekey;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

/**
 * @description: 自定义规则密码生成器
 * @author: shijunjie
 * @time: 2020/1/10 12:32
 */
@Slf4j
public class PasswordGenerator {

    private Configuration configuration;
    @Getter
    private long counter = 0;
    ConcurrentLinkedQueue<String> passwords = new ConcurrentLinkedQueue<>();

    public PasswordGenerator(Configuration configuration){
        this.configuration = configuration;
        new Thread(new Generator(passwords)).start();
    }

    public synchronized String getPassword() {
        if (passwords.size() == 0){
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            }catch (InterruptedException e) {

            }
        }
        return passwords.poll();
    }

    class Generator implements Runnable{
        private ConcurrentLinkedQueue passwords;

        public Generator(ConcurrentLinkedQueue passwords){
            this.passwords = passwords;
        }

        @SneakyThrows
        @Override
        public void run() {
            Set<Integer> set = new HashSet<>();
            int len = configuration.getCharacters().length < configuration.getMaxLen() ? configuration.getCharacters().length : configuration.getMaxLen();
            for (int i = configuration.getMinLen(); i <= len; i++) {
                create("", set,  i);
            }
        }

        private boolean create(String s, Set<Integer> iL, int m) {
            if(m == 0) {
                passwords.add(s);
                counter++;
                return false;
            }
            char[] characters = configuration.getCharacters();
            Set<Integer> set;
            for(int i = 0; i < characters.length; i++) {
                set = new HashSet<>();
                set.addAll(iL);
                if(!iL.contains(i)) {
                    String str = s + characters[i];
                    set.add(i);
                    create(str, set, m-1);
                }
            }
            return true;
        }

    }


    public static void main(String[] args) throws Exception {
        PasswordGenerator pg = new PasswordGenerator(
                    new Configuration()
                        .setEnableUpperCase(false)
                        .setEnableLowerCase(false)
                        .setEnableSymbol(false)
                        .build()
                        .info()
        );
        while (true){
            String password = pg.getPassword();
            if (password == null) System.exit(0);
            System.out.println(password);
        }

        //System.out.println("共生成"+ pg.getCounter()+"个密码");

    }

}
