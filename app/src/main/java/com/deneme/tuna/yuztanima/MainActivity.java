package com.deneme.tuna.yuztanima;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.Toast;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {

    private Button fotocekButton;
    private String[] cinsiyetler;
    private String secimOnay;
    private String secimVazgec;

    private String cinsiyetSecimi = "";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor ;

    private ImageView backButton ;

    private InterstitialAd mInterstitialAd;
    private AdView mAdView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Kullanıcı internet kontrolü
        if (!internetKontrolu()){
            Toast.makeText(getApplicationContext(),"Lütfen internete bağlı olduğunuzdan emin olup yeniden deneyiniz.",Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        //Kullanıcı analiz işlemi için cinsiyet seçecek ve seçilen cinsiyet bu yöntemle saklanacak.
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        //Admob reklamları için gerekli ön işlem
        MobileAds.initialize(this, getResources().getString(R.string.admob_uygulama_id));

        //Geçiş reklamı işlemleri
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.admob_gecis_reklam_id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
               super.onAdLoaded();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
            }

        });


        //Banner reklamı
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener(){
            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
            }
        });

        //Geri butonu
        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.click_effect));
                onBackPressed();
            }
        });

        //Seçilebilecek cinsiyetler,onayla ve vazgeç seçenekleri
        cinsiyetler = getResources().getStringArray(R.array.cinsiyetler);
        secimOnay = getResources().getString(R.string.onayla);
        secimVazgec = getResources().getString(R.string.vazgec);


        fotocekButton = (Button) findViewById(R.id.fotoCekButton);
        fotocekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //geçiş reklamı yüklendiyse göster
                if (mInterstitialAd.isLoaded()){
                    mInterstitialAd.show();
                }
                //cinsiyet seçimi
                alertdialogGoster();

            }
        });


    }

    public void alertdialogGoster(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        mBuilder.setTitle("Cinsiyetiniz");

        mBuilder.setItems(cinsiyetler, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                     cinsiyetSecimi = cinsiyetler[which];
                     dialog.dismiss();
                     fotoCekmeIslemi();
                     cinsiyetiKaydet(cinsiyetSecimi);

            }
        });
        AlertDialog alertDialog = mBuilder.create();
        alertDialog.show();

    }

    private void cinsiyetiKaydet(String cinsiyetSecimi){
        editor = sharedPreferences.edit();
        editor.putString("cinsiyet",cinsiyetSecimi);
        editor.apply();
    }

    private void fotoCekmeIslemi() {
        //CameraActivity'i başlat
        Intent cameraIntent = new Intent(getApplicationContext(),CameraActivity.class);
        startActivity(cameraIntent);


    }

    //Internet kontrolü yapan metod
    public boolean internetKontrolu(){
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
    }
        return connected;
    }

    //Geri tuşuna basılınca yapılacak işlemler
   @Override
    public void onBackPressed() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        mBuilder.setMessage("Uygulamadan çıkmak istediğinize emin misiniz?");

        mBuilder.setPositiveButton(secimOnay, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        mBuilder.setNegativeButton(secimVazgec, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = mBuilder.create();
        alertDialog.show();
    }
}






