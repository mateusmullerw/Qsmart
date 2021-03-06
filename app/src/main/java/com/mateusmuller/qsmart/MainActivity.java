package com.mateusmuller.qsmart;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.List;

import in.goodiebag.carouselpicker.CarouselPicker;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    //Context
    Context context = this;

    //Animações
    Animation FABopen, FABclose;


    //Interface
    TextView textociclo;
    ImageButton menu;

    TextView tempo;
    ProgressBar barraprogresso;
    ImageView wifiOff;

    FloatingActionButton agendar;
    FloatingActionButton novociclo;
    TextView min;

    Button iniciar;
    CarouselPicker carouselPicker;
    List<CarouselPicker.PickerItem> imageItems = new ArrayList<>();
    ImageView Iicone;

    //Ciclo Pré Salvos
    String ciclo0 = "1_Pesado_3_3_2_3_2_0_145000";
    String ciclo1 = "0_Dia-a-dia_2_2_2_2_2_0_110000";
    String ciclo2 = "2_Delicado_1_2_1_2_1_0_110000";

    //Variáveis
    String proprilines[];
    int position;
    int onoff = 0;
    boolean wifion = false;
    int tempoTotal;
    String request;
    final String url = "http://192.168.4.1/";


    //NETWORK
    private ConnectivityManager mConnMgr;
    public NetworkReceiver mReceiver;


    //timer - substituir
    CountDownTimer timer = new CountDownTimer(tempoTotal,1000) {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onTick(long millisUntilFinished) {

        }

        public void onFinish() {

        }
    };


    // ON CREATE
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //DRAWER
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Primeiro Login
        SharedPreferences mSharedPreferences = getSharedPreferences("Login", MODE_PRIVATE);
        int loginFlag = mSharedPreferences.getInt("loginFlag", 0);
        if (loginFlag == 0){
            PropiedadesCiclo.salvaCiclo(ciclo0, 0, context);
            PropiedadesCiclo.salvaCiclo(ciclo1, 1,context);
            PropiedadesCiclo.salvaCiclo(ciclo2, 2,context);
            PropiedadesCiclo.salvaPosition(1,context);
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
        }
        //NETWORK
        mConnMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        mReceiver = new NetworkReceiver();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReceiver, filter);





        //Animações
        FABopen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_show);
        FABclose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_close);

        //Interface
        textociclo = findViewById(R.id.textociclo);
        Iicone = findViewById(R.id.icone);

        tempo = findViewById(R.id.tempo);
        barraprogresso = findViewById(R.id.barraprogresso);
        wifiOff = findViewById(R.id.wifiOff);

        agendar = findViewById(R.id.FABagendar);
        novociclo = findViewById(R.id.FABnovociclo);
        min = findViewById(R.id.min);

        iniciar = findViewById(R.id.iniciar);

        //Carrossel
        carouselPicker = findViewById(R.id.carousel);
        List<CarouselPicker.PickerItem> imageItems = new ArrayList<>();
        mSharedPreferences = getSharedPreferences("ciclos", MODE_PRIVATE);
        int numCiclo = mSharedPreferences.getInt("numCiclo", 0);
        String icone;
        for (int x = 0; x <= numCiclo; x ++) {
            proprilines = PropiedadesCiclo.getCiclo(x, context);
            icone = "p" + proprilines[0];
            int id = getDrawable(this, icone);
            imageItems.add(new CarouselPicker.DrawableItem(id));
        }
        CarouselPicker.CarouselViewAdapter imageAdapter = new CarouselPicker.CarouselViewAdapter(this, imageItems, 0);
        carouselPicker.setAdapter(imageAdapter);
        restoreCarousel();
        request = url + "=_start_"+proprilines[2]+"_"
                                +proprilines[3]+"_"
                                +proprilines[4]+"_"
                                +proprilines[5]+"_"
                                +proprilines[6]+"_"
                                +proprilines[7]+"_fim=";


        carouselPicker.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                String cicone;
                proprilines = PropiedadesCiclo.getCiclo(position, context);
                request = url + "=_start_"+proprilines[2]+"_"
                                            +proprilines[3]+"_"
                                            +proprilines[4]+"_"
                                            +proprilines[5]+"_"
                                            +proprilines[6]+"_"
                                            +proprilines[7]+"_fim=";
                cicone = "p" + proprilines[0];
                Iicone.setImageResource(getDrawable(context,cicone));
                textociclo.setText(proprilines[1]);
                tempoTotal  = Integer.parseInt(proprilines[8]);

                mostraTempo();
                PropiedadesCiclo.salvaPosition(position,context);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        //CLICK LISTENERS
        iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onoff == 0 && wifion) {
                    Network.sendRecieve(request);
                    timerStart();
                    iniciar.setText("Parar");
                    novociclo.startAnimation(FABclose);
                    agendar.startAnimation(FABclose);
                    novociclo.setVisibility(View.INVISIBLE);
                    agendar.setVisibility(View.INVISIBLE);
                    carouselPicker.setVisibility(View.INVISIBLE);
                    Iicone.setVisibility(View.VISIBLE);
                    onoff = 1;

                }
                else if (onoff == 1 && wifion) {
                    Network.sendRecieve(url + "=_stop_fim=");
                    restoreCarousel();
                    timer.cancel();
                    progressAnim( 0, 0);
                    iniciar.setText("Iniciar");
                    novociclo.startAnimation(FABopen);
                    agendar.startAnimation(FABopen);
                    novociclo.setVisibility(View.VISIBLE);
                    agendar.setVisibility(View.VISIBLE);
                    carouselPicker.setVisibility(View.VISIBLE);
                    Iicone.setVisibility(View.INVISIBLE);
                    onoff = 0;
                }

            }
        });

        novociclo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NovoCiclo.class);
                startActivity(intent);
            }
        });
        agendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AgendaCiclo.class);
                startActivity(intent);
            }
        });
    }


    //DRAWER
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button_round, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Intent intent = new Intent();
        switch (id){
            case R.id.ciclos:
                intent.setClass(getApplicationContext(), Ciclos.class);
                break;
            case R.id.limpeza:
                intent.setClass(getApplicationContext(), Limpeza.class);
                break;
            case R.id.consumo:
                intent.setClass(getApplicationContext(), Consumo.class);
                break;
            case R.id.manual:
                intent.setClass(getApplicationContext(), Manual.class);
                break;
            case R.id.manutencao:
                intent.setClass(getApplicationContext(), Manutencao.class);
                break;
            case R.id.minhaconta:
                intent.setClass(getApplicationContext(), MinhaConta.class);
                break;
        }

        startActivity(intent);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static int getDrawable(Context context, String name)
    {
        Assert.assertNotNull(context);
        Assert.assertNotNull(name);

        return context.getResources().getIdentifier(name,
                "drawable", context.getPackageName());
    }
    //Carrossel
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void restoreCarousel(){
        position = PropiedadesCiclo.getPosition(context);
        proprilines = PropiedadesCiclo.getCiclo(position, context);
        carouselPicker.setCurrentItem(position);
        textociclo.setText(proprilines[1]);
        tempoTotal = Integer.parseInt(proprilines[8]);
        mostraTempo();
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
            restoreCarousel();
            barraprogresso.setProgress(0);
        }

    }
    public void timerMetodo(int totalMilli) {
        final int tempomilli = totalMilli;
        if (timer != null) {
            timer.cancel();
        }
        timer = new CountDownTimer(tempomilli+2000, 1000) {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTick(long millisUntilFinished) {
                mostraTempo();
                tempoTotal = tempoTotal-1000;

            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            public void onFinish() {
                restoreCarousel();
                progressAnim( 0, 0);
                iniciar.setText("Iniciar");
                novociclo.startAnimation(FABopen);
                agendar.startAnimation(FABopen);
                novociclo.setVisibility(View.VISIBLE);
                agendar.setVisibility(View.VISIBLE);
                carouselPicker.setVisibility(View.VISIBLE);
                Iicone.setVisibility(View.INVISIBLE);
                onoff = 0;


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
    //NETWORK
    public class NetworkReceiver extends BroadcastReceiver {

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onReceive(Context context, Intent intent) {

            NetworkInfo networkInfo = mConnMgr.getActiveNetworkInfo();

            if(networkInfo != null){

                boolean isWiFiAvailable = mConnMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
                boolean isGSMAvailable = mConnMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected();
                if (isWiFiAvailable){
                    //Toast.makeText(context, "Conectado", Toast.LENGTH_SHORT).show();
                    barraprogresso.setBackground(getDrawable(R.drawable.circle));
                    wifiOff.startAnimation(FABclose);
                    wifiOff.setVisibility(View.INVISIBLE);
                    wifion = true;


                }else if (isGSMAvailable){
                    //Toast.makeText(context, "Conectado", Toast.LENGTH_SHORT).show();
                    barraprogresso.setBackground(getDrawable(R.drawable.circle));
                    wifiOff.startAnimation(FABclose);
                    wifiOff.setVisibility(View.INVISIBLE);
                    wifion = true;
                }else{
                    //Toast.makeText(context, "Desconectado", Toast.LENGTH_SHORT).show();
                    barraprogresso.setBackground(getDrawable(R.drawable.circulo_vermelho));
                    wifiOff.startAnimation(FABopen);
                    wifiOff.setVisibility(View.VISIBLE);
                    wifion = false;
                }
            }else{
                //Toast.makeText(context, "Desconectado", Toast.LENGTH_SHORT).show();
                barraprogresso.setBackground(getDrawable(R.drawable.circulo_vermelho));
                wifiOff.startAnimation(FABopen);
                wifiOff.setVisibility(View.VISIBLE);
                wifion = false;
            }
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mReceiver != null){
            unregisterReceiver(mReceiver);
        }
    }

}
