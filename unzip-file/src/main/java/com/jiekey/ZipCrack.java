package com.jiekey;

import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * @description:
 * @author: shijunjie
 * @time: 2020/1/10 10:56
 */
@Slf4j
public class ZipCrack implements FileCrack {

    @Override
    public boolean check(File file) throws Exception {
        if (file.exists()){
            ZipFile zFile = new ZipFile(file);
            zFile.setFileNameCharset("GBK");
            if (zFile.isValidZipFile()){
                return true;
            } else {
                throw new ZipException("压缩包文件检验不合法");
            }
        } else {
            throw new FileNotFoundException("文件不存在");
        }
    }

    @Override
    public synchronized boolean crack(File file, String password) {
        try {
            ZipFile zFile = new ZipFile(file);
            zFile.setFileNameCharset("GBK");
            zFile.setPassword(password.toCharArray());
            zFile.extractAll(file.getParentFile().getAbsolutePath());
        }catch (Exception e){
            return false;
        }
        return true;
    }
}
