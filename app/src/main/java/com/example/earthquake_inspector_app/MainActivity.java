package com.example.earthquake_inspector_app;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button log_in_btn;
    Button sign_up_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Cast views
        log_in_btn = findViewById(R.id.to_login_view);
        sign_up_btn = findViewById(R.id.to_signup_view);

        //Listeners
        log_in_btn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, LoginActivity.class)));
        sign_up_btn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, SignupActivity.class)));
    }
}