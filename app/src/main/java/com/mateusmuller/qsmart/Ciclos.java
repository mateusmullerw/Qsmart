package com.mateusmuller.qsmart;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

public class Ciclos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ciclos);
        Context context = this;

        List<DataCiclo> data = PropiedadesCiclo.ciclo_data(context);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        MyAdapter adapter = new MyAdapter(data, getApplication());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

}
