package com.example.hyliann.firewatchdesarrollo.modelo;

import com.example.hyliann.firewatchdesarrollo.MainActivity;

import java.io.Serializable;
import java.util.ArrayList;

public class Aplicacion implements Serializable {
    private static Arduino dispositivo_actual;
    private static ArrayList<Arduino> dispositivos;

    public Aplicacion() {
    }

    public static ArrayList<Arduino> getDispositivos() {
        return dispositivos;
    }

    public static void setDispositivos(ArrayList<Arduino> dispositivos) {
        Aplicacion.dispositivos = dispositivos;
    }

    public static Arduino getDispositivo_actual() {
        return dispositivo_actual;
    }

    public static void setDispositivo_actual(Arduino dispositivo_actual) {
        Aplicacion.dispositivo_actual = dispositivo_actual;
    }

    public static boolean AgregarDispositivo(Arduino a){
        if(!dispositivos.equals(a)){
            dispositivos.add(a);
            return true;
        }
        return false;
    }

    public static boolean ActualizarDispositivo(Arduino a){

        if(dispositivos.equals(a)){
            dispositivos.set(dispositivos.indexOf(a),a);
            return true;
        }
        return false;
    }

    public static boolean EliminarDispositivo(Arduino a){
        if(dispositivos.equals(a)){
            dispositivos.remove(a);
            return true;
        }
        return false;
    }
}
