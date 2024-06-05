package team.sugarsmile.cprms.util;

import cn.hutool.crypto.symmetric.SymmetricCrypto;

import java.io.InputStream;
import java.util.Properties;

public class SM4Util {
    private static SymmetricCrypto sm4;

    static {
        Properties properties = new Properties();
        InputStream inputStream = SM4Util.class.getClassLoader().getResourceAsStream("sm4.properties");

        try {
            properties.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }

        sm4 = new SymmetricCrypto("SM4/ECB/PKCS5Padding", properties.getProperty("key").getBytes());
    }

    public static String encrypt(String plaintext) {
        return sm4.encryptHex(plaintext);
    }

    public static String decrypt(String ciphertext) {
        return sm4.decryptStr(ciphertext);
    }
}