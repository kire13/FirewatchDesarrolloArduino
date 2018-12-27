package com.example.hyliann.firewatchdesarrollo.modelo;

import android.widget.SectionIndexer;

import com.example.hyliann.firewatchdesarrollo.modelo.adiciones.Bateria;
import com.example.hyliann.firewatchdesarrollo.modelo.adiciones.Sensor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Arduino implements Serializable {
//una clase que se podria llamar en otras activitys
private String mac;//direccion mac
    private String nombreDelDispositivoDESIGNADO;  //se genera automaticamente
    private String codigoBlueDESIGNADO;  //se captura con un metodo y se guarda
    private int velocidadDESIGNADO; // inician en 0 todos
    private String numeroAlertaDESIGNADO;  //se rellena por el usuario
    private Bateria bateriaDaparato;   // un objeto BATERIA que es parte de clase "arduino"                -CLASE objeto
    private ArrayList<Sensor> sensores;//un arraylist SENSORES que es parte de clase"arduino"     -CLASE objeto array
    private ArrayList<Sector> sectores;
///pantalla de setter usuario
    //nombre asignado al arduino
    //codigo bluethot asignado pero no se ense;a ssolo se avisa
    //numero de alerta para llamar al celular
    //los sensores detectados y setear sus rangos de deteccion
    //constructores
    public Arduino() {

    }

    public Arduino(String mac) {
        this.mac = mac;
    }

    /*
        public Arduino(String mac, String nombreDelDispositivoDESIGNADO) {
            this.mac = mac;
            this.nombreDelDispositivoDESIGNADO = nombreDelDispositivoDESIGNADO;
        }
    */
    public Arduino(String mac, String nombreDelDispositivoDESIGNADO) {
        this.mac = mac;
        this.nombreDelDispositivoDESIGNADO = nombreDelDispositivoDESIGNADO;
        this.codigoBlueDESIGNADO = "888888888";
        this.velocidadDESIGNADO = 0; // detenidos todos los dispositivos por default
        this.numeroAlertaDESIGNADO = "+958020188";
        this.bateriaDaparato = new Bateria(100);
        ArrayList<Sensor> sensores = new ArrayList<>();
        sensores.add(new Sensor("Temperatura", true));
        sensores.add(new Sensor("Humedad", true));
        sensores.add(new Sensor("Humo", true));
        this.sensores = sensores;
        ArrayList<Sector> sectores = new ArrayList<>();
        sectores.add(new Sector("Sector de Prueba", sensores));
        this.sectores = sectores;
    }

    public Arduino(Bateria bateriaDaparato, ArrayList<Sensor> sensores) {
        this.nombreDelDispositivoDESIGNADO = "DISPOSITIVO #1";
        this.codigoBlueDESIGNADO = "888888888";
        this.velocidadDESIGNADO = 0; // detenidos todos los dispositivos por default
        this.numeroAlertaDESIGNADO = "+958020188";  //
        this.bateriaDaparato = bateriaDaparato;
        this.sensores = sensores;
    }

    public Arduino(String nombreDelDispositivoDESIGNADO, Bateria bateriaDaparato, ArrayList<Sensor> sensores, ArrayList<Sector> sectores) {
        this.nombreDelDispositivoDESIGNADO = nombreDelDispositivoDESIGNADO;
        this.bateriaDaparato = bateriaDaparato;
        this.sensores = sensores;
        this.sectores = sectores;
    }

    public Arduino(String nombreDelDispositivoDESIGNADO, String codigoBlueDESIGNADO, int velocidadDESIGNADO, String numeroAlertaDESIGNADO, Bateria bateriaDaparato, ArrayList<Sensor> sensores) {
        this.nombreDelDispositivoDESIGNADO = "DISPOSITIVO #1";
        this.codigoBlueDESIGNADO = "888888888";
        this.velocidadDESIGNADO = 0; // detenidos todos los dispositivos por default
        this.numeroAlertaDESIGNADO = "+958020188";  //
        this.bateriaDaparato = bateriaDaparato; //
        this.sensores = sensores; //

    }

    public Arduino(String nombreDelDispositivoDESIGNADO, String codigoBlueDESIGNADO, int velocidadDESIGNADO, String numeroAlertaDESIGNADO, Bateria bateriaDaparato, ArrayList<Sensor> sensores, ArrayList<Sector> sectores) {
        this.nombreDelDispositivoDESIGNADO = nombreDelDispositivoDESIGNADO;
        this.codigoBlueDESIGNADO = codigoBlueDESIGNADO;
        this.velocidadDESIGNADO = velocidadDESIGNADO;
        this.numeroAlertaDESIGNADO = numeroAlertaDESIGNADO;
        this.bateriaDaparato = bateriaDaparato;
        this.sensores = sensores;
        this.sectores = sectores;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getNombreDelDispositivoDESIGNADO() {
        return nombreDelDispositivoDESIGNADO;
    }

    public void setNombreDelDispositivoDESIGNADO(String nombreDelDispositivoDESIGNADO) {
        this.nombreDelDispositivoDESIGNADO = nombreDelDispositivoDESIGNADO;
    }

    public String getCodigoBlueDESIGNADO() {
        return codigoBlueDESIGNADO;
    }

    public void setCodigoBlueDESIGNADO(String codigoBlueDESIGNADO) {
        this.codigoBlueDESIGNADO = codigoBlueDESIGNADO;
    }

    public int getVelocidadDESIGNADO() {
        return velocidadDESIGNADO;
    }

    public void setVelocidadDESIGNADO(int velocidadDESIGNADO) {
        this.velocidadDESIGNADO = velocidadDESIGNADO;
    }

    public String getNumeroAlertaDESIGNADO() {
        return numeroAlertaDESIGNADO;
    }

    public void setNumeroAlertaDESIGNADO(String numeroAlertaDESIGNADO) {
        this.numeroAlertaDESIGNADO = numeroAlertaDESIGNADO;
    }

    public Bateria getBateriaDaparato() {
        return bateriaDaparato;
    }

    public void setBateriaDaparato(Bateria bateriaDaparato) {
        this.bateriaDaparato = bateriaDaparato;
    }

    public ArrayList<Sensor> getSensores() {
        return sensores;
    }

    public void setSensores(ArrayList<Sensor> sensores) {
        this.sensores = sensores;
    }

    public ArrayList<Sector> getSectores() {
        return sectores;
    }

    public void setSectores(ArrayList<Sector> sectores) {
        this.sectores = sectores;
    }

    @Override
    public String toString() {
        return this.getNombreDelDispositivoDESIGNADO();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Arduino)) return false;
        Arduino arduino = (Arduino) o;
        return Objects.equals(getMac(), arduino.getMac());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMac());
    }
}
