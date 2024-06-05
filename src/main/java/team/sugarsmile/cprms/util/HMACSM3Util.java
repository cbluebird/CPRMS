package team.sugarsmile.cprms.util;

import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.encoders.Hex;

import java.io.InputStream;
import java.util.Properties;

public class HMACSM3Util {

    private static HMac hmac;

    static {
        Properties properties = new Properties();
        InputStream inputStream = SM4Util.class.getClassLoader().getResourceAsStream("sm4.properties");

        try {
            properties.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }

        byte[] keyBytes = properties.getProperty("key").getBytes();
        hmac = new HMac(new SM3Digest());
        hmac.init(new KeyParameter(keyBytes));
    }

    public static String generateHMACSM3(String message) {
        byte[] messageBytes = message.getBytes();
        hmac.update(messageBytes, 0, messageBytes.length);
        byte[] result = new byte[hmac.getMacSize()];
        hmac.doFinal(result, 0);
        return Hex.toHexString(result);
    }

    public static boolean verifyHMACSM3(String message, String hmac) {
        String generatedHMAC = generateHMACSM3(message);
        return generatedHMAC.equals(hmac);
    }

}
