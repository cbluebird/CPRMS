package team.sugarsmile.cprms.util;

import cn.hutool.crypto.symmetric.SymmetricCrypto;
public class SM4 {

    //key必须是16字节，即128位
    final static String key = "sm4demo123456789";
    //指明加密算法和秘钥
    static SymmetricCrypto sm4 = new SymmetricCrypto("SM4/ECB/PKCS5Padding", key.getBytes());
    //加密为16进制，也可以加密成base64/字节数组
    public static String encryptSm4(String plaintext) {

        return sm4.encryptHex(plaintext);
    }
    //解密
    public static String decryptSm4(String ciphertext) {

        return sm4.decryptStr(ciphertext);
    }
    public static void main(String[] args) {

        String content = "zjut123456";
        String plain = encryptSm4(content);
        String cipher = decryptSm4(plain);
        System.out.println(plain + "\n" + cipher);
    }
}