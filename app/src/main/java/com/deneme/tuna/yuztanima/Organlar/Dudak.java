package com.deneme.tuna.yuztanima.Organlar;

import java.util.ArrayList;
import java.util.List;

import com.deneme.tuna.yuztanima.Nokta;

public class Dudak {
    private List<Nokta> alt = new ArrayList<>();
    private List<Nokta> ust = new ArrayList<>();
    private List<Nokta> cevre = new ArrayList<>();

    public void addAltNokta(Nokta nokta){
        this.getAlt().add(nokta);
    }

    public void addUstNokta(Nokta nokta){
        this.getUst().add(nokta);
    }


    public List<Nokta> getAlt() {
        return alt;
    }

    public void setAlt(List<Nokta> alt) {
        this.alt = alt;
    }

    public List<Nokta> getUst() {
        return ust;
    }

    public void setUst(List<Nokta> ust) {
        this.ust = ust;
    }

    public List<Nokta> getCevre() {
        return cevre;
    }

    public void setCevre(List<Nokta> cevre) {
        this.cevre = cevre;
    }

    public void cevreOlustur(){
        List<Nokta> olacakCevre = new ArrayList<>();
        olacakCevre.addAll(getAlt());
        olacakCevre.addAll(getUst());
        setCevre(olacakCevre);

    }
}
