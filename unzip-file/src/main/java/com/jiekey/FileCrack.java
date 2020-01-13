package com.jiekey;

import java.io.File;

public interface FileCrack {

    /**
     * 检验文件的合法性
     * @param file
     * @throws Exception
     */
    boolean check(File file) throws Exception;

    /**
     * 破解文件，如果能用密码破解，返回true
     * @param file
     * @param password
     * @return
     * @throws Exception
     */
    boolean crack(File file, String password);

}
