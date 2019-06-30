package com.deneme.tuna.yuztanima;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.deneme.tuna.yuztanima.Tipler.BurnaGore.BuyukBurun;
import com.deneme.tuna.yuztanima.Tipler.BurnaGore.KucukBurun;
import com.deneme.tuna.yuztanima.Tipler.CeneyeGore.DuzCene;
import com.deneme.tuna.yuztanima.Tipler.CeneyeGore.SivriCene;
import com.deneme.tuna.yuztanima.Tipler.DudagaGore.InceDudak;
import com.deneme.tuna.yuztanima.Tipler.DudagaGore.KalinDudak;
import com.deneme.tuna.yuztanima.Tipler.DudagaGore.NormalDudak;
import com.deneme.tuna.yuztanima.Tipler.GozeGore.BuyukGoz;
import com.deneme.tuna.yuztanima.Tipler.GozeGore.KucukGoz;
import com.deneme.tuna.yuztanima.Tipler.GozeGore.NormalGoz;
import com.deneme.tuna.yuztanima.Tipler.MutlulugaGore.AsiriMutlu;
import com.deneme.tuna.yuztanima.Tipler.MutlulugaGore.Mutlu;
import com.deneme.tuna.yuztanima.Tipler.MutlulugaGore.NormalMutluluk;
import com.deneme.tuna.yuztanima.Tipler.MutlulugaGore.Tebessum;
import com.deneme.tuna.yuztanima.Tipler.MutlulugaGore.Uzgun;
import com.deneme.tuna.yuztanima.Tipler.YuzeGore.GenisYuz;
import com.deneme.tuna.yuztanima.Tipler.YuzeGore.UzunYuz;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class ShowResultsActivity extends AppCompatActivity {

    private TextView yuzSonucuBaslik;
    private TextView yuzSonucuIcerik;

    private TextView burunSonucuBaslik;
    private TextView burunSonucuIcerik;

    private TextView mutlulukSonucuBaslik;
    private TextView mutlulukSonucuIcerik;

    private TextView gozSonucuBaslik;
    private TextView gozSonucuIcerik;

    private TextView ceneSonucuBaslik;
    private TextView ceneSonucuIcerik;

    private TextView dudakSonucuBaslik;
    private TextView dudakSonucuIcerik;

    private GenisYuz genisYuz ;
    private UzunYuz uzunYuz ;

    private KucukBurun kucukBurun ;
    private BuyukBurun buyukBurun ;

    private BuyukGoz buyukGoz;
    private KucukGoz kucukGoz;
    private NormalGoz normalGoz;

    private SivriCene sivriCene;
    private DuzCene duzCene;

    private InceDudak inceDudak;
    private NormalDudak normalDudak;
    private KalinDudak kalinDudak;


    private AsiriMutlu asiriMutlu;
    private Mutlu mutlu;
    private NormalMutluluk normalMutluluk;
    private Tebessum tebessum;
    private Uzgun uzgun;

    private ImageView backButton ;

    private AdView mAdView;


    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_results);

        MobileAds.initialize(this, getResources().getString(R.string.admob_uygulama_id));

        MobileAds.initialize(this, getResources().getString(R.string.admob_uygulama_id));

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.admob_gecis_reklam_id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                mInterstitialAd.show();
                super.onAdLoaded();

            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
            }

        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener(){
            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
            }
        });





        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.click_effect));
                onBackPressed();
            }
        });

        yuzSonucuBaslik = findViewById(R.id.yuzBaslik);
        yuzSonucuIcerik = findViewById(R.id.yuzIcerik);
        burunSonucuBaslik = findViewById(R.id.burunBaslik);
        burunSonucuIcerik = findViewById(R.id.burunIcerik);
        mutlulukSonucuBaslik = findViewById(R.id.mutlulukBaslik);
        mutlulukSonucuIcerik = findViewById(R.id.mutlulukIcerik);
        gozSonucuBaslik = findViewById(R.id.gozBaslik);
        gozSonucuIcerik = findViewById(R.id.gozIcerik);
        ceneSonucuBaslik = findViewById(R.id.ceneBaslik);
        ceneSonucuIcerik = findViewById(R.id.ceneIcerik);
        dudakSonucuBaslik = findViewById(R.id.dudakBaslik);
        dudakSonucuIcerik = findViewById(R.id.dudakIcerik);




        sonuclariGoster();


    }

    private void sonuclariGoster(){
        //Analiz sonuçlarını al
        AnalizSonuclari sonuclar = getIntent().getParcelableExtra("analizsonuclari");
        //Analiz sonuçlarına göre sonuç ekranını tasarla

        //Mutluluk sonuçları
        switch (sonuclar.getMutlulukTipi()){
            case "aşırı mutlu":
                asiriMutlu = new AsiriMutlu();
                mutlulukSonucuBaslik.setText(asiriMutlu.getBASLIK());
                mutlulukSonucuIcerik.setText(asiriMutlu.getICERIK());
                break;
            case "mutlu":
                mutlu = new Mutlu();
                mutlulukSonucuBaslik.setText(mutlu.getBASLIK());
                mutlulukSonucuIcerik.setText(mutlu.getICERIK());
                break;
            case "tebessüm":
                tebessum = new Tebessum();
                mutlulukSonucuBaslik.setText(tebessum.getBASLIK());
                mutlulukSonucuIcerik.setText(tebessum.getICERIK());
                break;
            case "normal":
                normalMutluluk = new NormalMutluluk();
                mutlulukSonucuBaslik.setText(normalMutluluk.getBASLIK());
                mutlulukSonucuIcerik.setText(normalMutluluk.getICERIK());
                break;
            case "üzgün":
                uzgun = new Uzgun();
                mutlulukSonucuBaslik.setText(uzgun.getBASLIK());
                mutlulukSonucuIcerik.setText(uzgun.getICERIK());
                break;


        }

        //Yüz sonuçları
        switch (sonuclar.getYuzTipi()){
            case "geniş":
                genisYuz = new GenisYuz();
                yuzSonucuBaslik.setText(genisYuz.getBASLIK());
                yuzSonucuIcerik.setText(genisYuz.getICERIK());
                break;
            case "uzun":
                uzunYuz = new UzunYuz();
                yuzSonucuBaslik.setText(uzunYuz.getBASLIK());
                yuzSonucuIcerik.setText(uzunYuz.getICERIK());
                break;
        }

        //Burun sonuçları

        switch (sonuclar.getBurunTipi()){
            case "büyük":
                buyukBurun = new BuyukBurun();
                burunSonucuBaslik.setText(buyukBurun.getBASLIK());
                burunSonucuIcerik.setText(buyukBurun.getICERIK());
                break;
            case "küçük":
                kucukBurun = new KucukBurun();
                burunSonucuBaslik.setText(kucukBurun.getBASLIK());
                burunSonucuIcerik.setText(kucukBurun.getICERIK());
                break;
        }

        //Goz sonuçları

        switch (sonuclar.getGozTipi()){
            case "büyük":
                buyukGoz = new BuyukGoz();
                gozSonucuBaslik.setText(buyukGoz.getBASLIK());
                gozSonucuIcerik.setText(buyukGoz.getICERIK());
                break;
            case "normal":
                normalGoz = new NormalGoz();
                gozSonucuBaslik.setText(normalGoz.getBASLIK());
                gozSonucuIcerik.setText(normalGoz.getICERIK());
                break;
            case "küçük":
                kucukGoz = new KucukGoz();
                gozSonucuBaslik.setText(kucukGoz.getBASLIK());
                gozSonucuIcerik.setText(kucukGoz.getICERIK());
                break;
        }

        //Cene Sonuclari

        switch (sonuclar.getCeneTipi()){
            case "sivri":
                sivriCene = new SivriCene();
                ceneSonucuBaslik.setText(sivriCene.getBASLIK());
                ceneSonucuIcerik.setText(sivriCene.getICERIK());
                break;
            case "düz":
                duzCene = new DuzCene();
                ceneSonucuBaslik.setText(duzCene.getBASLIK());
                ceneSonucuIcerik.setText(duzCene.getICERIK());
                break;
        }

        //Dudak Sonucları
        switch (sonuclar.getDudakTipi()){
            case "ince":
                inceDudak = new InceDudak();
                dudakSonucuBaslik.setText(inceDudak.getBASLIK());
                dudakSonucuIcerik.setText(inceDudak.getICERIK());
                break;
            case "normal":
                normalDudak = new NormalDudak();
                dudakSonucuBaslik.setText(normalDudak.getBASLIK());
                dudakSonucuIcerik.setText(normalDudak.getICERIK());
                break;
            case "kalın":
                kalinDudak = new KalinDudak();
                dudakSonucuBaslik.setText(kalinDudak.getBASLIK());
                dudakSonucuIcerik.setText(kalinDudak.getICERIK());
                break;
        }

        //



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
