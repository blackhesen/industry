package com.hesen.security;

import java.util.UUID;

/**
 * UUID为128位，取高低64位分别处理，转为64制字符
 * 64位每低6位转为int对应一个64制字符数组
 * Created by LeonWang on 2015/9/8.
 */
public class UUIdUtil {
    private final static char[] DIGITS64 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_".toCharArray();

    public static String next() {
        UUID u = UUID.randomUUID();
//      return u.toString();
        return toIDString(u.getMostSignificantBits()) + toIDString(u.getLeastSignificantBits());
    }

    private static String toIDString(long l) {
        char[] buf = "00000000000".toCharArray(); // 限定11位长度
        int length = 11;
        long least = 63L; // 0x0000003FL
        do {
            buf[--length] = DIGITS64[(int) (l & least)]; // l & least取低6位
            /* 无符号的移位只有右移，没有左移
             * 使用“>>>”进行移位
             */
            l >>>= 6;
        } while (l != 0);
        return new String(buf);
    }

    public static void main(String[] args) {
        long time = System.currentTimeMillis();
        for (int i=0; i<1000000L; i++) {
            next();
        }
        System.out.println(next());
        System.out.println(System.currentTimeMillis() - time);
    }
}
