package com.example.cw_comp1786;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cw_comp1786.db.DatabaseHelper;
import com.example.cw_comp1786.db.databasehepler.YogaclassTableHelper;

public class Splash_Screen extends AppCompatActivity {

    private static final int SPLASH_DELAY = 2000; // 2 seconds delay
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);

        
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Splash_Screen.this, Start_Screen.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_DELAY);

        }

}
