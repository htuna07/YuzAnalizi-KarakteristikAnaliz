package com.deneme.tuna.yuztanima;

//Analiz işlemi için referans alınan sınır değerleri ve işlemleri
public class SinirDegerleri {
    private String cinsiyet;
    private double YUZ_ORANI_SINIRI ;
    private final double BURUN_ORANI_SINIRI = 70 ;
    private final double UZGUN_ORANI_SINIRI = 10;
    private final double NORMAL_MUTLULUK_ORANI_SINIRI = 35;
    private final double TEBESSUM_ORANI_SINIRI = 80;
    private final double MUTLU_ORANI_SINIRI = 99.4;

    private final double KUCUK_GOZ_SINIRI = 25;
    private final double NORMAL_GOZ_SINIRI = 32;

    private final double CENE_SINIR_DEGERI = 27;


    private final double INCE_DUDAK_SINIR_DEGERI = 25;
    private final double NORMAL_DUDAK_SINIR_DEGERI = 30;


    SinirDegerleri(String gelenCinsiyet){
        cinsiyet = gelenCinsiyet;

        switch (cinsiyet){
            case "Erkek":
                YUZ_ORANI_SINIRI = 85;
                break;
            case "Kadın":
                YUZ_ORANI_SINIRI = 80;
                break;
        }



    }


    public double getYUZ_ORANI_SINIRI() {
        return YUZ_ORANI_SINIRI;
    }

    public double getBURUN_ORANI_SINIRI() {
        return BURUN_ORANI_SINIRI;
    }

    public double getUZGUN_ORANI_SINIRI() {
        return UZGUN_ORANI_SINIRI;
    }

    public double getNORMAL_MUTLULUK_ORANI_SINIRI() {
        return NORMAL_MUTLULUK_ORANI_SINIRI;
    }

    public double getTEBESSUM_ORANI_SINIRI() {
        return TEBESSUM_ORANI_SINIRI;
    }

    public double getMUTLU_ORANI_SINIRI() {
        return MUTLU_ORANI_SINIRI;
    }


    public double getKUCUK_GOZ_SINIRI() {
        return KUCUK_GOZ_SINIRI;
    }

    public double getNORMAL_GOZ_SINIRI() {
        return NORMAL_GOZ_SINIRI;
    }

    public double getCENE_SINIR_DEGERI() {
        return CENE_SINIR_DEGERI;
    }

    public double getINCE_DUDAK_SINIR_DEGERI() {
        return INCE_DUDAK_SINIR_DEGERI;
    }

    public double getNORMAL_DUDAK_SINIR_DEGERI() {
        return NORMAL_DUDAK_SINIR_DEGERI;
    }
}
