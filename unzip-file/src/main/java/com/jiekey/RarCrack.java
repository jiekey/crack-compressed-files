package com.jiekey;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * @description:
 * @author: shijunjie
 * @time: 2020/1/10 10:56
 */
@Slf4j
public class RarCrack implements FileCrack {

    @Override
    public boolean check(File file) throws Exception {
        if (!file.exists()){
            throw new FileNotFoundException("文件不存在");
        }
        return true;
    }

    @Override
    public boolean crack(File file, String password) {

        return true;
    }

}
