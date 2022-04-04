package com.example.loveforjava;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

/**
 * This activity displays the User Profile's QR code so that the used could login via QR code on another device
 */
public class GenerateQRCodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qr_code);

        Intent i = getIntent();
        String userID = (String) i.getSerializableExtra("USERID");

        ImageView qrCode = findViewById(R.id.display_qrcode);

        /*  WEBSITE : https://www.geeksforgeeks.org
        *   SOLUTION : https://www.geeksforgeeks.org/how-to-generate-qr-code-in-android/
        *   AUTHOR : https://auth.geeksforgeeks.org/user/chaitanyamunje/articles
        * */
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);

        int width = point.x;
        int height = point.y;

        int dimen = width < height ? width : height;
        dimen = dimen * 3 / 4;

        QRGEncoder qrgEncoder = new QRGEncoder("LOVEFORJAVA" + userID, null, QRGContents.Type.TEXT, dimen);
        try {
            // getting our qrcode in the form of bitmap.
            Bitmap bitmap = qrgEncoder.encodeAsBitmap();
            // the bitmap is set inside our image
            // view using .setimagebitmap method.
            qrCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            // this method is called for
            // exception handling.
            Log.e("Tag", e.toString());
        }

    }
}
