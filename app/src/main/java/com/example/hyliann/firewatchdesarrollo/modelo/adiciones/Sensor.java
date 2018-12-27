package com.example.hyliann.firewatchdesarrollo.modelo.adiciones;

import java.io.Serializable;

public class Sensor implements Serializable {

    //1 arduino - N sensores
    //sensores dentro del aparato arduino
    String nombre;  //calor , humedad,distancia
    String valorDato; // grados u otro
    Boolean estadoFuncional;// 1  ,0  si esta activo o no el sensor


    //constructor  usados para tipos de sensores
    public Sensor() {
    }

    public Sensor(String nombre, Boolean estadoFuncional) {
        this.nombre = nombre;
        this.estadoFuncional = estadoFuncional;
    }

    public Sensor(String nombre, String valorDato, Boolean estadoFuncional) {
        this.nombre = nombre;
        this.valorDato = valorDato;
        this.estadoFuncional = estadoFuncional;
    }

    //get sett
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getValorDato() {
        return valorDato;
    }

    public void setValorDato(String valorDato) {
        this.valorDato = valorDato;
    }

    public Boolean getEstadoFuncional() {
        return estadoFuncional;
    }

    public void setEstadoFuncional(Boolean estadoFuncional) {
        this.estadoFuncional = estadoFuncional;
    }

    @Override
    public String toString() {
        return this.nombre;
    }
    //

    //    public void metodoCantidadSensores(){
//    }


    ///getters y setters

}

