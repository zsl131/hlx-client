package com.zslin.tools.qr;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.zslin.basic.tools.ConfigTools;
import org.docx4j.org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.UUID;

/**
 * Created by 钟述林 393156105@qq.com on 2017/4/12 11:54.
 * 生成二维码
 */
@Component
public class QrGenerateTools {

    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;
    private static final String CODE = "utf-8";

    @Autowired
    private ConfigTools configTools;

    /**
     * 生成一维码（128）
     *
     * @param str
     * @return
     */
    public byte[] getBarcode(String str) {
        /*if (width == null || width < 200) {
            width = 200;
        }

        if (height == null || height < 50) {
            height = 50;
        }*/

        int width = 150; int height = 50;
        String format = "jpg";

        try {
            // 文字编码
            Hashtable<EncodeHintType, String> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, CODE);

            BitMatrix bitMatrix = new MultiFormatWriter().encode(str,
                    BarcodeFormat.CODE_128, width, height, hints);

            File f = new File(configTools.getUploadPath("qr/")+ UUID.randomUUID().toString()+"."+format);
            writeToFile(bitMatrix, format, f);

            InputStream is = new FileInputStream(f);
            byte[] bytes = IOUtils.toByteArray(is);
            is.close();
            f.delete(); //删除临时文件

            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] genQr(String text) {
        try {
            int width = 100;
            int height = 100;
            String format = "jpg";
            Hashtable hints= new Hashtable();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height,hints);
            File f = new File(configTools.getUploadPath("qr/")+ UUID.randomUUID().toString()+"."+format);
            writeToFile(bitMatrix, format, f);

            InputStream is = new FileInputStream(f);
            byte[] bytes = IOUtils.toByteArray(is);
            is.close();
            f.delete(); //删除临时文件
            return bytes;
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (Exception e) {
        }
        return null;
    }

    public BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }

    public void writeToFile(BitMatrix matrix, String format, File file)
            throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        if (!ImageIO.write(image, format, file)) {
            throw new IOException("Could not write an image of format " + format + " to " + file);
        }
    }
}
