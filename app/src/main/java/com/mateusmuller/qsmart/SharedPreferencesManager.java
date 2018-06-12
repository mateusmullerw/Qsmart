package com.mateusmuller.qsmart;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class SharedPreferencesManager {
    public static String[] getCiclo(int thisCiclo, Context context) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences("ciclos", context.MODE_PRIVATE);
        String ciclo = "ciclo" + Integer.toString(thisCiclo);
        String propriedades = mSharedPreferences.getString(ciclo, "nope");
        String proprilines[] = propriedades.split("\n");
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
}
