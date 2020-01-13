package com.jiekey;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: shijunjie
 * @time: 2020/1/11 14:37
 */
@Slf4j
@Data
@Accessors(chain = true)
public class Configuration {

    /**
     * 配置处理压缩文件的对象映射  扩展名-->对象实例
     */
    public Configuration(){
        crackMap.put("zip", new ZipCrack());
        crackMap.put("rar", new RarCrack());
    }

    //处理结果
    private String result;

    //==============处理压缩包的映射对象
    private Map<String, FileCrack> crackMap = new HashMap<>();

    //==============ProcessHandler配置==============

    /**
     * 存活的线程个数 默认为CPU个数的2倍
     */
    private int alive = Runtime.getRuntime().availableProcessors() * 2;

    /**
     * 压缩文件路径
     */
    private File file;

    //==============PasswordGenerator配置=============

    /**
     * 密码生成最小长度
     */
    private int minLen = 2;

    /**
     * 密码生成最大长度
     */
    private int maxLen = 6;

    /**
     * 是否使用A-Z生成密码 默认使用
     */
    private boolean enableUpperCase  = true;

    private char[] upperCase = {
            'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'
    };

    /**
     * 是否使用a-z生成密码 默认使用
     */
    private boolean enableLowerCase = true;

    private char[] lowerCase = {
            'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'
    };

    /**
     * 是否使用0-9生成密码 默认使用
     */
    private boolean enableNumbers = true;

    private char[] numbers = {
            '0','1','2','3','4'
    };

    /**
     * 是否使用特殊字符生成密码 默认使用
     */
    private boolean enableSymbol = true;

    private char[] symbol = {
            '~', '!','@','#','$','%','^','&','*','(',')','_','+','<','>','?','-','=',',','/','[',']','{','}','\\','|','"','"',':','\'','.'
    };

    /**
     * 总字符
     */
    private char[] characters;

    /**
     * 获取处理压缩文件的对象
     * @return
     */
    public FileCrack getFileCrack(){
        if (file == null){
            log.error("未设置压缩文件");
            System.exit(0);
        }
        String absolutePath = file.getAbsolutePath();
        String extension = absolutePath.substring(absolutePath.lastIndexOf(".") + 1).toLowerCase();
        return crackMap.get(extension);
    }

    public Configuration build(){
        //验证密码规则合法性, 不合法直接退出系统
        if (minLen <= 1 || maxLen <= 1 || minLen > maxLen){
            log.error("minLen和maxLen必须都大于1，且minLen小于等于maxLen，但是当前minLen为{}，maxLen为{}", minLen, maxLen);
            System.exit(0);
        }
        if (!enableUpperCase && !enableLowerCase && !enableNumbers && !enableSymbol){
            log.error("enableUpperCase、enableLowerCase、enableNumbers、enableSymbol至少有一个为true");
            System.exit(0);
        }

        //根据规则生成字符集
        if (characters != null) return this;
        int size = (enableUpperCase ? upperCase.length : 0)
                + (enableLowerCase ? lowerCase.length : 0)
                + (enableNumbers ? numbers.length : 0)
                + (enableSymbol ? symbol.length : 0);
        this.characters = new char[size];
        int ix = 0;
        if (enableUpperCase){
            for (int i = 0; i < upperCase.length; i++, ix++) {
                characters[ix] = upperCase[i];
            }
        }
        if (enableLowerCase){
            for (int i = 0; i < lowerCase.length; i++, ix++) {
                characters[ix] = lowerCase[i];
            }
        }
        if (enableNumbers){
            for (int i = 0; i < numbers.length; i++, ix++) {
                characters[ix] = numbers[i];
            }
        }
        if (enableSymbol){
            for (int i = 0; i < symbol.length; i++, ix++) {
                characters[ix] = symbol[i];
            }
        }
        return this;
    }

    public Configuration info(){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < characters.length; i++) {
            sb.append(characters[i]).append(" ");
        }
        if (file != null){
            log.info("解压文件:{}", file.getAbsolutePath());
        }
        log.info("准备生成{}-{}位的密码", minLen, maxLen);
        log.info("字符长度:{}", characters.length);
        log.info("{}",sb.toString());
        return this;
    }

    public static void main(String[] args) {
        new Configuration().setFile(new File("D:\\demo.zip")).setEnableLowerCase(false).build().info();
    }

}
