package com.deneme.tuna.yuztanima;

import android.os.Parcel;
import android.os.Parcelable;

//Acitivityler arası geçirilebilir class
public class AnalizSonuclari implements Parcelable {
    private String yuzTipi;
    private String dudakTipi;
    private String kasTipi;
    private String burunTipi;
    private String gozTipi;
    private String mutlulukTipi;
    private String ceneTipi;


    AnalizSonuclari(){

    }

    protected AnalizSonuclari(Parcel in) {
        yuzTipi = in.readString();
        dudakTipi = in.readString();
        kasTipi = in.readString();
        burunTipi = in.readString();
        gozTipi = in.readString();
        mutlulukTipi = in.readString();
        ceneTipi = in.readString();
    }

    public static final Creator<AnalizSonuclari> CREATOR = new Creator<AnalizSonuclari>() {
        @Override
        public AnalizSonuclari createFromParcel(Parcel in) {
            return new AnalizSonuclari(in);
        }

        @Override
        public AnalizSonuclari[] newArray(int size) {
            return new AnalizSonuclari[size];
        }
    };

    public String getYuzTipi() {
        return yuzTipi;
    }

    public void setYuzTipi(String yuzTipi) {
        this.yuzTipi = yuzTipi;
    }

    public String getDudakTipi() {
        return dudakTipi;
    }

    public void setDudakTipi(String dudakTipi) {
        this.dudakTipi = dudakTipi;
    }

    public String getKasTipi() {
        return kasTipi;
    }

    public void setKasTipi(String kasTipi) {
        this.kasTipi = kasTipi;
    }

    public String getBurunTipi() {
        return burunTipi;
    }

    public void setBurunTipi(String burunTipi) {
        this.burunTipi = burunTipi;
    }

    public String getGozTipi() {
        return gozTipi;
    }

    public void setGozTipi(String gozTipi) {
        this.gozTipi = gozTipi;
    }

    public String getMutlulukTipi() {
        return mutlulukTipi;
    }

    public void setMutlulukTipi(String mutlulukTipi) {
        this.mutlulukTipi = mutlulukTipi;
    }

    public String getCeneTipi() {
        return ceneTipi;
    }

    public void setCeneTipi(String ceneTipi) {
        this.ceneTipi = ceneTipi;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(yuzTipi);
        dest.writeString(dudakTipi);
        dest.writeString(kasTipi);
        dest.writeString(burunTipi);
        dest.writeString(gozTipi);
        dest.writeString(mutlulukTipi);
        dest.writeString(ceneTipi);
    }



}
