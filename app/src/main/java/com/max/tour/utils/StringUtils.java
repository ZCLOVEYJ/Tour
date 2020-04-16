package com.max.tour.utils;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.EncryptUtils;

import java.util.Random;

/**
 * Copyright (C) 2019, Relx
 * StringUtils
 * <p>
 * Description
 *
 * @author ZhengChen
 * @version 2.2
 * <p>
 * Ver 2.2, 2020-04-14, ZhengChen, Create file
 */
public class StringUtils {

    public static String getCheckCode() {
        String ZiMu = "1234567890";
        String result = "";
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            int index = random.nextInt(ZiMu.length());
            char c = ZiMu.charAt(index);
            result += c;
        }
        return result;

    }

    public static String encode(String password){
        byte[] pwd = ConvertUtils.string2Bytes(password);
        byte[] encodePwd = EncodeUtils.base64Encode(pwd);
        return ConvertUtils.bytes2String(encodePwd);
    }

}
