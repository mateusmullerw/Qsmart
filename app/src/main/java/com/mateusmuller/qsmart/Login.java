package com.mateusmuller.qsmart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity {

    Button login;
    String user = "Qsmart";
    String senha = "12345678";
    EditText tuser;
    EditText tsenha;
    TextView buser;
    TextView bsenha;
    TextView aviso;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.backlight);
        tuser = findViewById(R.id.tuser);
        tsenha = findViewById(R.id.tsenha);
        buser = findViewById(R.id.buser);
        bsenha = findViewById(R.id.bsenha);
        aviso = findViewById(R.id.aviso);

        login.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (user.equals(tuser.getText().toString()) && senha.equals(tsenha.getText().toString())) {

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
                }else{
                    buser.setTextColor(getColor(R.color.vermelho));
                    bsenha.setTextColor(getColor(R.color.vermelho));
                    aviso.setVisibility(View.VISIBLE);

                }
            }
        });

    }
}
