package com.ailide.apartmentsabc.utils;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.uuzuche.lib_zxing.activity.CodeUtils;

public class CodeUtil {

    public static Bitmap createQrCode(String content, int w, int h, Bitmap logo) {
        return CodeUtils.createImage(content, w, h, logo);
    }

    public static Bitmap createBarCode(String content, int w, int h) {
        return creatBarcode(content, w, h);
    }

    public static Bitmap creatBarcode(String contents, int desiredWidth, int desiredHeight) {
        Bitmap ruseltBitmap = null;
        BarcodeFormat barcodeFormat = BarcodeFormat.CODE_128;

        ruseltBitmap = encodeAsBitmap(contents, barcodeFormat,
                desiredWidth, desiredHeight);

        return ruseltBitmap;
    }

    protected static Bitmap encodeAsBitmap(String contents,
                                           BarcodeFormat format, int desiredWidth, int desiredHeight) {
        final int WHITE = 0xFFFFFFFF;
        final int BLACK = 0xFF000000;

        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result = null;
        try {
            result = writer.encode(contents, format, desiredWidth,
                    desiredHeight, null);
        } catch (WriterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        // All are 0, or black, by default
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }
}