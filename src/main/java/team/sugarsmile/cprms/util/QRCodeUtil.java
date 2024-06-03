package team.sugarsmile.cprms.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import team.sugarsmile.cprms.exception.ErrorCode;
import team.sugarsmile.cprms.exception.SystemException;

import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * @author XiMo
 */
public class QRCodeUtil {
    public static BufferedImage genQRCode(String content, int color, int width, int height) {
        try {
            HashMap<EncodeHintType, Object> typeObjectHashMap = new HashMap<>();
            typeObjectHashMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            typeObjectHashMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
            typeObjectHashMap.put(EncodeHintType.MARGIN, 0);

            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            BitMatrix bitMatrix;
            bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, width, height, typeObjectHashMap);

            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bufferedImage.setRGB(x, y, bitMatrix.get(x, y) ? color : 0xFFFFFF);
                }
            }

            return bufferedImage;
        } catch (WriterException e) {
            throw new SystemException(ErrorCode.ZXING_ERROR.getCode(), e.getMessage(), e);
        }
    }
}
