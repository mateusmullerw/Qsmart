package com.mateusmuller.qsmart;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;

public class MinhaConta extends AppCompatActivity {

    Switch login;
    Switch backlight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minha_conta);

        login = findViewById(R.id.backlight);
        backlight = findViewById(R.id.login);

        login.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences mSharedPreferences = getSharedPreferences("Login", MODE_PRIVATE);
                SharedPreferences.Editor mEditor = mSharedPreferences.edit();
                mEditor.putInt("loginFlag", 0);
                mEditor.apply();
            }
        });

        backlight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Network.sendRecieve("192.168.4.1/=_luz_fim=");
            }
        });

    }
}
