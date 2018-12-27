package com.example.hyliann.firewatchdesarrollo.modelo;

import com.example.hyliann.firewatchdesarrollo.modelo.adiciones.Sensor;

import java.io.Serializable;
import java.util.ArrayList;

public class Sector implements Serializable {

    private int id;
    private String nombre;
    private ArrayList<Sensor> sensores;

    public Sector() {
    }


    public Sector(String nombre) {
        this.nombre = nombre;
    }

    public Sector(String nombre, ArrayList<Sensor> sensores) {
        this.nombre = nombre;
        this.sensores = sensores;
    }

    public Sector(int id) {
        this.id = id;
    }

    public Sector(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return this.nombre+"#"+this.id;
    }
}
