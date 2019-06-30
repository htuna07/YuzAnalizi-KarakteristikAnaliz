package com.deneme.tuna.yuztanima.Organlar;

import java.util.ArrayList;
import java.util.List;

import com.deneme.tuna.yuztanima.Nokta;

public class Burun {
    private List<Nokta> kemer = new ArrayList<>();
    private List<Nokta> alt = new ArrayList<>();

    public void addKemerNoktasi(Nokta nokta){
        this.getKemer().add(nokta);
    }

    public void addAltNokta(Nokta nokta){
        this.getAlt().add(nokta);
    }



    public List<Nokta> getKemer() {
        return kemer;
    }

    public void setKemer(List<Nokta> kemer) {
        this.kemer = kemer;
    }

    public List<Nokta> getAlt() {
        return alt;
    }

    public void setAlt(List<Nokta> alt) {
        this.alt = alt;
    }
}
