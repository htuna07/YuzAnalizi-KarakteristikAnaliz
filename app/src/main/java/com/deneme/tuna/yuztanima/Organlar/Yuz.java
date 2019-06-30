package com.deneme.tuna.yuztanima.Organlar;

import com.deneme.tuna.yuztanima.Nokta;

import java.util.ArrayList;
import java.util.List;

public class Yuz {
    private List<Nokta> cevre = new ArrayList<>();

    public void addYuzCevresiNoktasi(Nokta nokta){
        this.getCevre().add(nokta);
    }


    public List<Nokta> getCevre() {
        return cevre;
    }

    public void setCevre(List<Nokta> cevre) {
        this.cevre = cevre;
    }
}
