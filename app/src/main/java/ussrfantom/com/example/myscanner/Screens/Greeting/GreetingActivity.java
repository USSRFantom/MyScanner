package ussrfantom.com.example.myscanner.Screens.Greeting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ussrfantom.com.example.myscanner.R;
import ussrfantom.com.example.myscanner.Screens.BasketScreen.BasketScreen;
import ussrfantom.com.example.myscanner.Screens.SplashScreen.SplashScreen;

public class GreetingActivity extends AppCompatActivity {
    Button buttonStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greeting);
        buttonStart = findViewById(R.id.buttonStart);

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GreetingActivity.this, BasketScreen.class);
                startActivity(intent);
                finish();
            }

        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(GreetingActivity.this, SplashScreen.class);
        startActivity(intent);
        finish();
    }
}