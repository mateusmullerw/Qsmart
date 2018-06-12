package com.mateusmuller.qsmart;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class Limpeza extends AppCompatActivity {

    public Button iniciar;
    public int onoff = 0;
    public ProgressBar barraprogresso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_limpeza);

        iniciar = findViewById(R.id.iniciar2);
        barraprogresso = findViewById(R.id.barraprogresso2);

        iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onoff == 0) {
                    barraprogresso.setVisibility(View.VISIBLE);
                    iniciar.setText("Pausar");
                    onoff = 1;
                } else if (onoff == 1) {
                    barraprogresso.setVisibility(View.INVISIBLE);
                    iniciar.setText("Iniciar");
                    onoff = 0;
                }

            }
        });
    }
}
