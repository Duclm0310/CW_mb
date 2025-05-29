package com.example.cw_comp1786;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Login_Screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_screen);

        Button btnLogin = findViewById(R.id.btnLogin);
        EditText editEmailAddress = findViewById(R.id.editEmailAddress2);
        EditText editPassword = findViewById(R.id.editPassword);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = editEmailAddress.getText().toString().trim();
                String password = editPassword.getText().toString().trim();

                if (username.equals("admin") && password.equals("123456")) {
                    Intent intent = new Intent(Login_Screen.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(Login_Screen.this, "Please!! Try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}