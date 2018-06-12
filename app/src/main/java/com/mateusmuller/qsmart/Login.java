package com.mateusmuller.qsmart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Login extends AppCompatActivity {

    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences mSharedPreferences = getSharedPreferences("Login", MODE_PRIVATE);
                SharedPreferences.Editor mEditor = mSharedPreferences.edit();
                mEditor.putInt("loginFlag", 1);
                mEditor.apply();
                mSharedPreferences = getSharedPreferences("ciclos", MODE_PRIVATE);
                if (mSharedPreferences.getInt("numCiclo", 0) == 0) {
                    mEditor = mSharedPreferences.edit();
                    mEditor.putInt("numCiclo", 2);
                    mEditor.apply();
                }
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

}
