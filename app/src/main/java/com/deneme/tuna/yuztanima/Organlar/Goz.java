package com.deneme.tuna.yuztanima.Organlar;

import java.util.ArrayList;
import java.util.List;

import com.deneme.tuna.yuztanima.Nokta;

public class Goz {
    private List<Nokta> cevre = new ArrayList<>();

    public void addGozCevresiNoktasi(Nokta nokta){
        this.getCevre().add(nokta);
    }


    public List<Nokta> getCevre() {
        return cevre;
    }

    public void setCevre(List<Nokta> cevre) {
        this.cevre = cevre;
    }
}
