package team.sugarsmile.cprms.util;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.digest.Digester;

public class SM3 {


    public SM3() {
        // 初始化 SM3 摘要算法
    }

    /**
     * 对输入字符串进行 SM3 加密
     *
     * @param input 待加密的字符串
     * @return 加密后的十六进制字符串
     */
    public static String encrypt(String input) {
        // 执行摘要
        return SmUtil.sm3().digestHex(input);
    }

    /**
     * 对输入字节数组进行 SM3 加密
     *
     * @param input 待加密的字节数组
     * @return 加密后的十六进制字符串
     */
    public static String encrypt(byte[] input) {
        // 执行摘要
        return SmUtil.sm3().digestHex(input);
    }

    public static void main(String[] args) {
        SM3 encryptor = new SM3();
        String input = "zjut123456";
        String encrypted = encryptor.encrypt(input);
        System.out.println("加密后的字符串: " + encrypted);
    }
}
