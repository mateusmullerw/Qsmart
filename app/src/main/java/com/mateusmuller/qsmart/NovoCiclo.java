package com.mateusmuller.qsmart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.goodiebag.carouselpicker.CarouselPicker;

public class NovoCiclo extends AppCompatActivity {

    Context context = this;

    EditText nomeciclo;
    Button salvar;

    TextView tTipo;
    RadioGroup rgTipo;
    RadioButton tipoDelicado;
    RadioButton tipoDiaDia;
    RadioButton tipoPesado;

    TextView tMolho;
    RadioGroup rgMolho;
    RadioButton molhoCurto;
    RadioButton molhoMedio;
    RadioButton molhoLongo;

    Switch Centrifugar;
    RadioGroup rgCentrifuga;
    RadioButton rpm1;
    RadioButton rpm2;
    RadioButton rpm3;

    Switch Secar;
    RadioGroup rgTempoSecar;
    RadioGroup rgTemperatura;
    RadioButton seca20;
    RadioButton seca40;
    RadioButton seca60;
    RadioButton tempBaixa;
    RadioButton tempMedia;
    RadioButton tempAlta;

    TextView tNivel;
    Switch nivelAutomatico;
    RadioGroup rgnivelAgua;
    RadioButton nivelBaixo;
    RadioButton nivelMedio;
    RadioButton nivelAlto;

    //ANIMAÇÕES
    Animation Animopen, Animclose;


    int tempoTotal;
    int icone;
    String propriedades = "";
    int numCiclo;
    boolean emBranco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_ciclo);
        propriedades = "";

        nomeciclo = findViewById(R.id.nomenovociclo);

        tTipo = findViewById(R.id.tipoText);
        rgTipo = findViewById(R.id.tipoDeRoupa);
        tipoDelicado = findViewById(R.id.rbDelicado);
        tipoDiaDia = findViewById(R.id.rbDiaDia);
        tipoPesado = findViewById(R.id.rbPesado);

        tMolho = findViewById(R.id.molhoText);
        rgMolho = findViewById(R.id.tempoDeMolho);
        molhoCurto = findViewById(R.id.rbCurto);
        molhoMedio = findViewById(R.id.rbMedio);
        molhoLongo = findViewById(R.id.rbLongo);

        Centrifugar = findViewById(R.id.swCentrifugar);
        rgCentrifuga = findViewById(R.id.rgCentrifuga);
        rpm1 = findViewById(R.id.rbrpm1);
        rpm2 = findViewById(R.id.rbrpm2);
        rpm3 = findViewById(R.id.rbrpm3);

        Secar = findViewById(R.id.swSecar);
        rgTempoSecar = findViewById(R.id.tempoSecar);
        rgTemperatura = findViewById(R.id.temperatura);
        seca20 = findViewById(R.id.rb20min);
        seca40 = findViewById(R.id.rb40min);
        seca60 = findViewById(R.id.rb60min);
        tempBaixa = findViewById(R.id.rbtemp50);
        tempMedia = findViewById(R.id.rbtemp60);
        tempAlta = findViewById(R.id.rbtemp70);

        tNivel = findViewById(R.id.nivelText);
        nivelAutomatico = findViewById(R.id.swAutomatico);
        rgnivelAgua = findViewById(R.id.nivelAgua);
        nivelBaixo = findViewById(R.id.rbnivelBaixo);
        nivelMedio = findViewById(R.id.rbnivelMedio);
        nivelAlto = findViewById(R.id.rbnivelAlto);

        //Animações
        Animopen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rgopen);
        Animclose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rgclose);

        SharedPreferences mSharedPreferences = getSharedPreferences("ciclos", MODE_PRIVATE);
        numCiclo = mSharedPreferences.getInt("numCiclo", 0);

        //Carrossel
        CarouselPicker carouselPicker = (CarouselPicker) findViewById(R.id.carousel);

        List<CarouselPicker.PickerItem> imageItems = new ArrayList<>();
        int x;
        for (x = 0; x<=31; x++){
            String icone = "p" + Integer.toString(x);
            int id = getResources().getIdentifier(icone, "drawable", getPackageName());
            imageItems.add(new CarouselPicker.DrawableItem(id));
        }

        CarouselPicker.CarouselViewAdapter imageAdapter = new CarouselPicker.CarouselViewAdapter(this, imageItems, 0);
        carouselPicker.setAdapter(imageAdapter);

        Centrifugar.setEnabled(false);

        salvar = findViewById(R.id.salvar);

        //TEMPOS

        // Tipo             1= 20; 2= 30; 3= 40
        // Molho            1= 10; 2= 15; 3= 20
        // Centrifugar      1= 35; 2= 25; 3= 15
        // Secar            1= 20; 2= 40; 3= 60


        // Salvar
        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tempoTotal = 0;
                emBranco = false;
                propriedades = Integer.toString(icone)+ "_";
                propriedades = propriedades+nomeciclo.getText()+"_";
            int checkedRadioButtonId = rgTipo.getCheckedRadioButtonId();
            if (checkedRadioButtonId == -1) {
                tTipo.setTextColor(getResources().getColor(R.color.colorTextAccent));
                tipoDelicado.setTextColor(getResources().getColor(R.color.colorTextAccent));
                tipoDiaDia.setTextColor(getResources().getColor(R.color.colorTextAccent));
                tipoPesado.setTextColor(getResources().getColor(R.color.colorTextAccent));
                emBranco = true;
            }
            else{
                switch (checkedRadioButtonId){
                    case R.id.rbDelicado:
                        propriedades = propriedades+"1_";
                        tempoTotal = tempoTotal + 20;
                        break;
                    case R.id.rbDiaDia:
                        propriedades = propriedades+"2_";
                        tempoTotal = tempoTotal + 30;
                        break;
                    case R.id.rbPesado:
                        propriedades = propriedades+"3_";
                        tempoTotal = tempoTotal + 40;
                        break;
                }
            }
            checkedRadioButtonId = rgMolho.getCheckedRadioButtonId();
            if (checkedRadioButtonId == -1) {
                tMolho.setTextColor(getResources().getColor(R.color.colorTextAccent));
                molhoCurto.setTextColor(getResources().getColor(R.color.colorTextAccent));
                molhoMedio.setTextColor(getResources().getColor(R.color.colorTextAccent));
                molhoLongo.setTextColor(getResources().getColor(R.color.colorTextAccent));
                emBranco = true;
            }
            else{
                switch (checkedRadioButtonId){
                    case R.id.rbCurto:
                        propriedades = propriedades+"1_";
                        tempoTotal = tempoTotal + 10;
                        break;
                    case R.id.rbMedio:
                        propriedades = propriedades+"2_";
                        tempoTotal = tempoTotal + 15;
                        break;
                    case R.id.rbLongo:
                        propriedades = propriedades+"3_";
                        tempoTotal = tempoTotal + 20;
                        break;
                }
            }
            if (Centrifugar.isChecked()) {
                checkedRadioButtonId = rgCentrifuga.getCheckedRadioButtonId();
                if (checkedRadioButtonId == -1) {
                    Centrifugar.setTextColor(getResources().getColor(R.color.colorTextAccent));
                    rpm1.setTextColor(getResources().getColor(R.color.colorTextAccent));
                    rpm2.setTextColor(getResources().getColor(R.color.colorTextAccent));
                    rpm3.setTextColor(getResources().getColor(R.color.colorTextAccent));
                    emBranco = true;
                } else {
                    switch (checkedRadioButtonId) {
                        case R.id.rbrpm1:
                            propriedades = propriedades + "1_";
                            tempoTotal = tempoTotal + 35;
                            break;
                        case R.id.rbrpm2:
                            propriedades = propriedades + "2_";
                            tempoTotal = tempoTotal + 25;
                            break;
                        case R.id.rbrpm3:
                            propriedades = propriedades + "3_";
                            tempoTotal = tempoTotal + 15;
                            break;
                    }
                }
            }
            else{
                propriedades = propriedades + "0_";
            }
            if (Secar.isChecked()) {
                checkedRadioButtonId = rgTempoSecar.getCheckedRadioButtonId();
                if (checkedRadioButtonId == -1) {
                    seca20.setTextColor(getResources().getColor(R.color.colorTextAccent));
                    seca40.setTextColor(getResources().getColor(R.color.colorTextAccent));
                    seca60.setTextColor(getResources().getColor(R.color.colorTextAccent));
                    emBranco = true;
                } else {
                    switch (checkedRadioButtonId) {
                        case R.id.rb20min:
                            propriedades = propriedades + "1_";
                            tempoTotal = tempoTotal + 20;
                            break;
                        case R.id.rb40min:
                            propriedades = propriedades + "2_";
                            tempoTotal = tempoTotal + 40;
                            break;
                        case R.id.rb60min:
                            propriedades = propriedades + "3_";
                            tempoTotal = tempoTotal + 60;
                            break;
                    }
                }
                checkedRadioButtonId = rgTemperatura.getCheckedRadioButtonId();
                if (checkedRadioButtonId == -1) {
                    tempBaixa.setTextColor(getResources().getColor(R.color.colorTextAccent));
                    tempMedia.setTextColor(getResources().getColor(R.color.colorTextAccent));
                    tempAlta.setTextColor(getResources().getColor(R.color.colorTextAccent));
                    emBranco = true;
                } else {
                    switch (checkedRadioButtonId) {
                        case R.id.rbtemp50:
                            propriedades = propriedades + "1_";
                            break;
                        case R.id.rbtemp60:
                            propriedades = propriedades + "2_";
                            break;
                        case R.id.rbtemp70:
                            propriedades = propriedades + "3_";
                            break;
                    }
                }
            }
            else{
                propriedades = propriedades + "0_0_";
            }
            if (!nivelAutomatico.isChecked()) {
                checkedRadioButtonId = rgnivelAgua.getCheckedRadioButtonId();
                if (checkedRadioButtonId == -1) {
                    tNivel.setTextColor(getResources().getColor(R.color.colorTextAccent));
                    nivelBaixo.setTextColor(getResources().getColor(R.color.colorTextAccent));
                    nivelMedio.setTextColor(getResources().getColor(R.color.colorTextAccent));
                    nivelAlto.setTextColor(getResources().getColor(R.color.colorTextAccent));
                    emBranco = true;
                } else {
                    switch (checkedRadioButtonId) {
                        case R.id.rbnivelBaixo:
                            propriedades = propriedades + "1_";
                            break;
                        case R.id.rbnivelMedio:
                            propriedades = propriedades + "2_";
                            break;
                        case R.id.rbnivelAlto:
                            propriedades = propriedades + "3_";
                            break;
                    }
                }
            }
            else{
                propriedades = propriedades + "0_";
            }
            if(emBranco){
                emBranco = false;
                return;
            }
            propriedades = propriedades + Integer.toString(tempoTotal * 1000);
            numCiclo++;
            PropiedadesCiclo.salvaCiclo(propriedades, numCiclo, context);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            }
        });

        //ClickListeners
        Centrifugar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == false) {
                    rgCentrifuga.startAnimation(Animclose);
                    rgCentrifuga.setVisibility(View.GONE);
                }
                else {
                    rgCentrifuga.startAnimation(Animopen);
                    rgCentrifuga.setVisibility(View.VISIBLE);
                }

            }
        });

        Secar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == false) {
                    Centrifugar.setEnabled(true);
                    rgTempoSecar.setVisibility(View.GONE);
                    rgTemperatura.setVisibility(View.GONE);
                }
                else {
                    Centrifugar.setChecked(true);
                    Centrifugar.setEnabled(false);
                    rgTempoSecar.setVisibility(View.VISIBLE);
                    rgTemperatura.setVisibility(View.VISIBLE);
                }

            }
        });

        nivelAutomatico.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    rgnivelAgua.setVisibility(View.GONE);
                }
                else {
                    rgnivelAgua.setVisibility(View.VISIBLE);
                }

            }
        });

        carouselPicker.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
               icone = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }


}

