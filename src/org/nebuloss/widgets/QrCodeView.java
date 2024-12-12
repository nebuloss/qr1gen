package org.nebuloss.widgets;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

public class QrCodeView extends ImageView{


    public QrCodeView(Context context){
        super(context);
    }

    public void generateQRCode(String text) throws WriterException{
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, getWidth(), getWidth());

        Bitmap bitmap = createBitmap(bitMatrix);
        this.setImageBitmap(bitmap);
    }

    private Bitmap createBitmap(BitMatrix bitMatrix) {
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bitmap.setPixel(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
    
        return bitmap;
    }
}
