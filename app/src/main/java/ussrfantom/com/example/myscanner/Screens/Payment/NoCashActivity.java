package ussrfantom.com.example.myscanner.Screens.Payment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import ussrfantom.com.example.myscanner.R;

public class NoCashActivity extends AppCompatActivity {
    private String qr;
    private String pay;
    TextView textViewPrice;
    private ImageView imageViewQr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_cash);
        textViewPrice = findViewById(R.id.textViewPrice);
        imageViewQr = findViewById(R.id.imageViewqr_code);

        Intent intent = getIntent();
        qr = intent.getStringExtra("Key");
        pay = intent.getStringExtra("Key2");
        String a = pay + " " + "руб.";
        textViewPrice.setText(a);
        getCode();
    }

    private void getCode() {
        try {
            qrCode();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void qrCode() throws WriterException {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        BitMatrix bitMatrix = multiFormatWriter.encode(qr, BarcodeFormat.QR_CODE, 350, 300);
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
        imageViewQr.setImageBitmap(bitmap);
    }
}