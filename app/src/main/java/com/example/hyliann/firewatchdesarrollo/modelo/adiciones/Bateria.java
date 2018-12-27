package com.example.hyliann.firewatchdesarrollo.modelo.adiciones;

import java.io.Serializable;

public class Bateria implements Serializable {

    private int nivelDeCarga; // el estado de la bateria actual deberia capturarse del arduinO


    //constructor
    public Bateria(int nivelDeCarga) {
        this.nivelDeCarga = nivelDeCarga;
    }

    ///geters y setters
    public int getNivelDeCarga() {
        return nivelDeCarga;
    }

    public void setNivelDeCarga(int nivelDeCarga) {
        this.nivelDeCarga = nivelDeCarga;
    }
}
