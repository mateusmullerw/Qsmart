package com.mateusmuller.qsmart;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static android.support.v7.widget.DividerItemDecoration.HORIZONTAL;
import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class Ciclos extends AppCompatActivity {

    FloatingActionButton novociclo;
    private int mExpandedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ciclos);
        Context context = this;


        novociclo = findViewById(R.id.FABnovociclo);

        List<DataCiclo> data = PropiedadesCiclo.ciclo_data(context);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        DividerItemDecoration itemDecor = new DividerItemDecoration(context, VERTICAL);
        recyclerView.addItemDecoration(itemDecor);
        MyAdapter adapter = new MyAdapter(data, getApplication());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        novociclo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NovoCiclo.class);
                startActivity(intent);
            }
        });

    }

}
