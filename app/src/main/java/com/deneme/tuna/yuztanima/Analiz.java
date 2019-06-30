package com.deneme.tuna.yuztanima;

import android.widget.Toast;

import com.deneme.tuna.yuztanima.Organlar.Surat;


//Organların büyük,küçük,kısa,uzun,geniş,dar gibi şekillerine karar veren class
public class Analiz {



    private Surat islenecekSurat;
    private AnalizSonuclari sonuclar;
    private SinirDegerleri sinirDegerleri;


    Analiz(Surat gelenSurat,String cinsiyet){
        islenecekSurat = gelenSurat;
        sonuclar = new AnalizSonuclari();
        sinirDegerleri = new SinirDegerleri(cinsiyet);

    }


    public AnalizSonuclari analiziBaslat(){

        //mutluluk kararı ver

        sonuclar.setMutlulukTipi(mutlulukKarariVer());


        //Yuz kararı ver

        sonuclar.setYuzTipi(yuzKarariVer());


        //Burun kararı ver

        sonuclar.setBurunTipi(burunKarariVer());

        //Kaş kararı ver



        //Çene Kararı Ver

        sonuclar.setCeneTipi(ceneKarariVer());


        //Göz kararı ver

        sonuclar.setGozTipi(gozKarariVer());

        //Dudak Kararı ver

        sonuclar.setDudakTipi(dudakKarariVer());

        return sonuclar;

    }

    private String mutlulukKarariVer(){
        String karar = "";


        double mutlulukOrani = islenecekSurat.getMutlulukOrani();

        if (mutlulukOrani < sinirDegerleri.getUZGUN_ORANI_SINIRI()){
            karar = "üzgün";
        }
        else if(mutlulukOrani < sinirDegerleri.getNORMAL_MUTLULUK_ORANI_SINIRI()
                && mutlulukOrani >= sinirDegerleri.getUZGUN_ORANI_SINIRI()){
            karar = "normal";
        }
        else if(mutlulukOrani < sinirDegerleri.getTEBESSUM_ORANI_SINIRI()
                && mutlulukOrani >= sinirDegerleri.getNORMAL_MUTLULUK_ORANI_SINIRI()){
            karar = "tebessüm";
        }
        else if(mutlulukOrani < sinirDegerleri.getMUTLU_ORANI_SINIRI()
                && mutlulukOrani >= sinirDegerleri.getTEBESSUM_ORANI_SINIRI()){
            karar = "mutlu";
        }
        else if(mutlulukOrani >= sinirDegerleri.getMUTLU_ORANI_SINIRI()){
            karar = "aşırı mutlu";
        }
        return karar;
    }


    private String yuzKarariVer(){
        String karar = "";
        double yuzOran =  Math.abs(
                (islenecekSurat.getBurun().getKemer().get(0).getY() - islenecekSurat.getYuz().getCevre().get(18).getY())
                        /
                        (islenecekSurat.getYuz().getCevre().get(8).getX() - islenecekSurat.getYuz().getCevre().get(28).getX())
                        * 100);

        if (yuzOran >= sinirDegerleri.getYUZ_ORANI_SINIRI()){
            karar = "uzun";
        }
        else if(yuzOran < sinirDegerleri.getYUZ_ORANI_SINIRI()){
            karar = "geniş";
        }

        return karar;

    }

    private String burunKarariVer(){
        String karar = "";
        double burunOran = Math.abs((
                (islenecekSurat.getBurun().getAlt().get(2).getX() - islenecekSurat.getBurun().getAlt().get(0).getX())
                        /
                        (islenecekSurat.getBurun().getKemer().get(0).getY() - islenecekSurat.getBurun().getAlt().get(1).getY()))
                *
                100);
        if (burunOran >= sinirDegerleri.getBURUN_ORANI_SINIRI()){
            karar = "büyük";
        }
        else if (burunOran < sinirDegerleri.getBURUN_ORANI_SINIRI()){
            karar = "küçük";
        }

        return karar;

    }

    private String kasKarariVer(){
        String karar = "";
        double kasOran = Math.abs((
                (islenecekSurat.getSagKas().getUst().get(2).getY() - islenecekSurat.getSagKas().getAlt().get(2).getY())
                        /
                        (islenecekSurat.getSagKas().getAlt().get(4).getX() - islenecekSurat.getSagKas().getUst().get(0).getX())

        )
                *
                100);

        return karar;
    }

    private String gozKarariVer(){
        String karar = "";
        double gozOraniSag = Math.abs(
                ((islenecekSurat.getSagGoz().getCevre().get(4).getY() - islenecekSurat.getSagGoz().getCevre().get(12).getY())
                        /
                        (islenecekSurat.getSagGoz().getCevre().get(8).getX() - islenecekSurat.getSagGoz().getCevre().get(0).getX()))
                        *100
        );

        double gozOraniSol = Math.abs(
                ((islenecekSurat.getSolGoz().getCevre().get(4).getY() - islenecekSurat.getSolGoz().getCevre().get(12).getY())
                        /
                        (islenecekSurat.getSolGoz().getCevre().get(8).getX() - islenecekSurat.getSolGoz().getCevre().get(0).getX()))
                        *100
        );

        double gozOraniOrt = ((gozOraniSag) + (gozOraniSol)) / 2;

        if(gozOraniOrt <= sinirDegerleri.getKUCUK_GOZ_SINIRI()){
            karar = "küçük";
        }
        else if (gozOraniOrt > sinirDegerleri.getKUCUK_GOZ_SINIRI()
                && gozOraniOrt < sinirDegerleri.getNORMAL_GOZ_SINIRI()){
            karar = "normal";
        }
        else if(gozOraniOrt >= sinirDegerleri.getNORMAL_GOZ_SINIRI()){
            karar = "büyük";
        }

        return karar;
    }

    private String ceneKarariVer(){

        String karar = "";

        double ceneSagOran = Math.abs(
                ((islenecekSurat.getYuz().getCevre().get(20).getY() - islenecekSurat.getYuz().getCevre().get(18).getY())
                        /
                        (islenecekSurat.getYuz().getCevre().get(18).getX() - islenecekSurat.getYuz().getCevre().get(20).getX()))
                        *100
        );

        double ceneSolOran =  Math.abs(
                ((islenecekSurat.getYuz().getCevre().get(16).getY() - islenecekSurat.getYuz().getCevre().get(18).getY())
                        /
                        (islenecekSurat.getYuz().getCevre().get(16).getX() - islenecekSurat.getYuz().getCevre().get(18).getX()))
                        *100
        );

        double ceneOraniOrt = ((ceneSagOran) + (ceneSolOran)) / 2;

        if (ceneOraniOrt >= sinirDegerleri.getCENE_SINIR_DEGERI()){
            karar = "sivri";
        }
        else{
            karar = "düz";
        }

        return karar;
    }

    private String dudakKarariVer(){
        String karar = "";

        double ustDudakOran = Math.abs(
                ((islenecekSurat.getUstDudak().getUst().get(4).getY() - islenecekSurat.getUstDudak().getAlt().get(3).getY())
                        /
                        (islenecekSurat.getUstDudak().getUst().get(10).getX() - islenecekSurat.getUstDudak().getUst().get(0).getX()))
                        *100
        );

        double altDudakOran = Math.abs(
                ((islenecekSurat.getAltDudak().getUst().get(5).getY() - islenecekSurat.getAltDudak().getAlt().get(5).getY())
                        /
                        (islenecekSurat.getAltDudak().getAlt().get(0).getX() - islenecekSurat.getAltDudak().getAlt().get(8).getX()))
                        *100
        );

        double dudakOranOrt = (ustDudakOran) + (altDudakOran);



        if (dudakOranOrt <= sinirDegerleri.getINCE_DUDAK_SINIR_DEGERI()){
            karar = "ince";
        }
        else if(dudakOranOrt > sinirDegerleri.getINCE_DUDAK_SINIR_DEGERI() && dudakOranOrt <= sinirDegerleri.getNORMAL_DUDAK_SINIR_DEGERI()){
            karar = "normal";
        }
        else if(dudakOranOrt > sinirDegerleri.getNORMAL_DUDAK_SINIR_DEGERI()){
            karar = "kalın";
        }

        return karar;
    }


}
