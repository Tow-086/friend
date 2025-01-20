package com.friendcommon.util;


import java.util.UUID;

/**
 * @author Administrator
 * @date 2024/7/13 15:47
 * @description CodeGeneratorUtil
 */
public class CodeGeneratorUtil {
    /**
     * 生成指定长度的验证码
     * @param length 长度
     * @return
     */
    public static String generateCode(int length){
        return UUID.randomUUID().toString().substring(0, length);
    }

}

