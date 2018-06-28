package com.mateusmuller.qsmart;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Limpeza extends AppCompatActivity {

    Button iniciar;
    int onoff = 0;
    ProgressBar barraprogresso;
    TextView tempo;

    int tempoTotal = 15000;

    Context context = this;

    //timer - substituir
    CountDownTimer timer = new CountDownTimer(tempoTotal,1000) {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onTick(long millisUntilFinished) {

        }

        public void onFinish() {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_limpeza);



        iniciar = findViewById(R.id.iniciar2);
        barraprogresso = findViewById(R.id.barraprogresso2);
        tempo = findViewById(R.id.tempo2);

        iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onoff == 0) {
                    Network.sendRecieve("192.168.4.1/=_limpa_fim=");
                    tempoTotal = 15000;
                    timerStart();
                    iniciar.setText("Parar");
                    onoff = 1;
                } else if (onoff == 1) {
                    Network.sendRecieve("192.168.4.1/=_limpa_fim=");
                    timer.cancel();
                    progressAnim(0, 0);
                    tempo.setText("15");
                    iniciar.setText("Iniciar");
                    onoff = 0;
                } else if (onoff == 2){
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }

            }
        });

    }
    //TIMER
    public void timerStart () {
        timerMetodo(tempoTotal);
        progressAnim( 0, 1000);
        timer.start();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void mostraTempo(){
        tempo.setText(PropiedadesCiclo.milliFormat(tempoTotal));
        if (tempoTotal == 0){
            timer.cancel();

            barraprogresso.setProgress(0);
        }

    }
    public void timerMetodo(int totalMilli) {
        final int tempomilli = totalMilli;
        if (timer != null) {
            timer.cancel();
        }
        timer = new CountDownTimer(tempomilli+1000, 1000) {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTick(long millisUntilFinished) {
                mostraTempo();
                tempoTotal = tempoTotal-1000;

            }

            public void onFinish() {
                iniciar.setText("Voltar");
                onoff = 2;

            }
        };
    }
    //ANIMATION
    public void progressAnim( int from, int to){
        ProgressBarAnimation anim = new ProgressBarAnimation(barraprogresso, from, to);

        anim.setInterpolator(new LinearInterpolator());
        anim.setDuration(tempoTotal+1800);
        barraprogresso.startAnimation(anim);


    }

}

