package ussrfantom.com.example.myscanner.Screens.MenuPrice;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureActivity;

import ussrfantom.com.example.myscanner.R;
import ussrfantom.com.example.myscanner.Screens.BasketScreen.BasketScreen;
import ussrfantom.com.example.myscanner.Screens.CaptureScreen.CaptureScreen;
import ussrfantom.com.example.myscanner.Screens.Greeting.GreetingActivity;

public class MenuPrice extends AppCompatActivity implements View.OnClickListener {

    Button buttonStart;
    Button buttonStart2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_price);
        buttonStart = findViewById(R.id.button_start);
        buttonStart2 = findViewById(R.id.buttonStart2);

        buttonStart.setOnClickListener(this);
        buttonStart2.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        scanCode();
    }


    private void scanCode(){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(CaptureScreen.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("НАЧИНАЕМ СКАНИРОВАТЬ");
        integrator.initiateScan();
    }

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null){
            if (result.getContents() != null){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(result.getContents());//<<&&
                builder.setTitle("Сканирование Успешно!");
                builder.setPositiveButton("Продолжить сканирование", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        scanCode();
                    }
                }).setNegativeButton("Выполнено! К отчету!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            else {
                Toast.makeText(this, "Готово!", Toast.LENGTH_LONG).show();
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MenuPrice.this, GreetingActivity.class);
        startActivity(intent);
        finish();
    }
}