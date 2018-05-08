package io.renren.common.utils;

import com.swetake.util.Qrcode;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by huanghuibin on 2018/4/16.
 */
public class QRCodeUtils {

    public BufferedImage encoderQRCoder(String content) {
        try {
            Qrcode handler = new Qrcode();
            handler.setQrcodeErrorCorrect('M');
            handler.setQrcodeEncodeMode('B');
            handler.setQrcodeVersion(7);

            byte[] contentBytes = content.getBytes("UTF-8");

            BufferedImage bufImg = new BufferedImage(216, 216, BufferedImage.TYPE_INT_RGB);

            Graphics2D gs = bufImg.createGraphics();

            gs.setBackground(Color.white);
            gs.clearRect(0, 0, 216, 216);

            //设定图像颜色：BLACK
            gs.setColor(Color.BLACK);

            //设置偏移量  不设置肯能导致解析出错
            int pixoff = 18;
            //输出内容：二维码
            if(contentBytes.length > 0 && contentBytes.length < 124) {
                boolean[][] codeOut = handler.calQrcode(contentBytes);
                for(int i = 0; i < codeOut.length; i++) {
                    for(int j = 0; j < codeOut.length; j++) {
                        if(codeOut[j][i]) {
                            gs.fillRect(j * 4 + pixoff, i * 4 + pixoff,4, 4);
                        }
                    }
                }
            } else {
                System.err.println("QRCode content bytes length = " + contentBytes.length + " not in [ 0,120 ]. ");
            }
            gs.dispose();
            bufImg.flush();
            return bufImg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }
}
