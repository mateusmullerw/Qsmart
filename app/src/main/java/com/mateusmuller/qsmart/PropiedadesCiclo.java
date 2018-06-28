package com.mateusmuller.qsmart;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;
import static com.mateusmuller.qsmart.MainActivity.getDrawable;

public class PropiedadesCiclo {

    public static List<DataCiclo> ciclo_data (Context context) {

        int id = 1;
        String nome = "";
        String tempoMin = "";

        List<DataCiclo> data = new ArrayList<>();

        SharedPreferences mSharedPreferences = context.getSharedPreferences("ciclos", MODE_PRIVATE);
        int numCiclo = mSharedPreferences.getInt("numCiclo", 0);
        for (int x = 0; x <= numCiclo; x ++) {
            String proprilines[] = getCiclo(x, context);
            String icone = "s" + proprilines[0];
            id = getDrawable( context, icone);
            nome = proprilines[1];
            tempoMin = milliFormat(Integer.parseInt(proprilines[8]));

            data.add(new DataCiclo(nome, tempoMin, id));
        }

        return data;
    }

    public static String[] getCiclo(int thisCiclo, Context context) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences("ciclos", context.MODE_PRIVATE);
        String ciclo = "ciclo" + Integer.toString(thisCiclo);
        String propriedades = mSharedPreferences.getString(ciclo, "nope");
        String proprilines[] = propriedades.split("_");
        return proprilines;
    }
    public static void salvaCiclo(String propriedades, int countCiclos, Context context){
        final String ciclo = "ciclo"+Integer.toString(countCiclos);
        SharedPreferences mSharedPreferences = context.getSharedPreferences("ciclos", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(ciclo, propriedades);
        mEditor.putInt("numCiclo", countCiclos);
        mEditor.putInt("position", countCiclos);
        mEditor.apply();
        return;

    }
    public static void salvaPosition(int position, Context context) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences("ciclos", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putInt("position", position);
        mEditor.apply();
        return;
    }
    public static String milliFormat (int tempo_total) {
        if(tempo_total >= 60000) {

            String tempoMostrador = String.format("%01d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(tempo_total) % TimeUnit.HOURS.toMinutes(1),
                    TimeUnit.MILLISECONDS.toSeconds(tempo_total) % TimeUnit.MINUTES.toSeconds(1));
            return tempoMostrador;
        }else{
            String tempoMostrador = String.format("%02d",
                    //TimeUnit.MILLISECONDS.toMinutes(tempo_total) % TimeUnit.HOURS.toMinutes(1),
                    TimeUnit.MILLISECONDS.toSeconds(tempo_total) % TimeUnit.MINUTES.toSeconds(1));
            return tempoMostrador;
        }
    }
}