package team.sugarsmile.cprms.util;

import cn.hutool.crypto.SmUtil;

public class SM3Util {
    public static String encrypt(String input) {
        return SmUtil.sm3().digestHex(input);
    }
}
