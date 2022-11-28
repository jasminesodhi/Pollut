package com.jasminesodhi.randomhacksofkindness;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import mehdi.sakout.fancybuttons.FancyButton;

public class MainActivity extends AppCompatActivity {

    FancyButton userLogin, uberLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userLogin = (FancyButton) findViewById(R.id.user_login);
        uberLogin = (FancyButton) findViewById(R.id.uber_login);

        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.putExtra("Type", "car");
                startActivity(intent);
            }
        });

        uberLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.putExtra("Type", "uber");
                startActivity(intent);
            }
        });
    }
}