package ussrfantom.com.example.myscanner.Screens.QrCode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import ussrfantom.com.example.myscanner.R;
import ussrfantom.com.example.myscanner.Screens.Payment.CashActivity;
import ussrfantom.com.example.myscanner.Screens.Payment.EquaringActivity;
import ussrfantom.com.example.myscanner.Screens.Payment.NoCashActivity;

public class QrCode extends AppCompatActivity {

    private String qr;
    private ImageView imageViewQr;
    private String pay;
    TextView textViewPrice;
    TextView textViewCash;
    TextView textViewNoCash;
    TextView textViewEquaring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);
        textViewPrice = findViewById(R.id.textViewPrice);
        imageViewQr = findViewById(R.id.imageViewqr_code);
        textViewCash = findViewById(R.id.textViewPay1);
        textViewNoCash = findViewById(R.id.textViewPay2);
        textViewEquaring = findViewById(R.id.textViewPay3);


        Intent intent = getIntent();
        qr = intent.getStringExtra("Key");
        pay = intent.getStringExtra("Key2");
        System.out.println(pay);
        String a = pay + " " + "руб.";
        textViewPrice.setText(a);
        getCode();

        //нажатие на первый текст для оплаты
        textViewCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QrCode.this, CashActivity.class);
                intent.putExtra("Key", qr);
                intent.putExtra("Key2", pay);
                startActivity(intent);
            }
        });

        //нажатие на второй текст для оплаты
        textViewNoCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QrCode.this, NoCashActivity.class);
                intent.putExtra("Key", qr);
                intent.putExtra("Key2", pay);
                startActivity(intent);
            }
        });

        //нажатие на третий текст для оплаты
        textViewEquaring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QrCode.this, EquaringActivity.class);
                startActivity(intent);
            }
        });




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