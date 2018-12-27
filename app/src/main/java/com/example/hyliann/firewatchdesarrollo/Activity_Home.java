package com.example.hyliann.firewatchdesarrollo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hyliann.firewatchdesarrollo.modelo.Aplicacion;
import com.example.hyliann.firewatchdesarrollo.modelo.Arduino;
import com.example.hyliann.firewatchdesarrollo.modelo.adiciones.Bateria;
import com.example.hyliann.firewatchdesarrollo.modelo.adiciones.Sensor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

public class Activity_Home extends AppCompatActivity {
    String mipantalla;
    TextView tv_nombre;
    TextView tv_cantidadsens;
    TextView tv_tiposens;
    TextView tv_sensoresconproblemas;
    TextView tv_sensoressinproblemas;
    TextView tv_energiabateria;
    final int handlerState = 0;                         //used to identify handler message
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;

    private StringBuilder recDataString = new StringBuilder();


    Handler bluetoothIn;
    // SPP UUID service - this should work for most devices
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // String for MAC address
    private static String address = null;
    Arduino arduino=new Arduino();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);/// orientacion fija
        super.onCreate(savedInstanceState);
        mipantalla = this.getClass().getSimpleName();
        setContentView(R.layout.activity_home);
        tv_nombre = (TextView) findViewById(R.id.tv_NombredeDdispositivo);
        tv_cantidadsens = (TextView) findViewById(R.id.tv_cantidadSensores);
        tv_tiposens = (TextView) findViewById(R.id.tv_tiposensores);
        tv_sensoresconproblemas = (TextView) findViewById(R.id.tv_sensoresconproblemas);
        tv_sensoressinproblemas = (TextView) findViewById(R.id.tv_sensoressinproblemas);
        tv_energiabateria = (TextView) findViewById(R.id.tv_bateria);
        Intent intent=getIntent();
        if(intent.getExtras()!=null){
            arduino = (Arduino) intent.getSerializableExtra("dispositivo");
            Aplicacion.setDispositivo_actual(arduino);
            //SetInfo(arduino);
            setTitle(arduino.getNombreDelDispositivoDESIGNADO());//set titulo con el name del dispositivo
        }
        setTitleColor(Color.BLACK);

    }
    // llenado de HOME

    @Override
    protected void onStart() {
        super.onStart();
        ArrayList<Sensor> sensores = arduino.getSensores();
        ////nombre dispositivo
        tv_nombre.setText(arduino.getNombreDelDispositivoDESIGNADO());
        //
        String estadoBateria;

        Bateria b = arduino.getBateriaDaparato();
        if (b != null) {
            if (arduino.getBateriaDaparato().getNivelDeCarga() > 20) {
                estadoBateria = "bajo";
                tv_energiabateria.setText("" + estadoBateria);
            }
            if (arduino.getBateriaDaparato().getNivelDeCarga() > 70) {
                estadoBateria = "medio";
                tv_energiabateria.setText("" + estadoBateria);
            }
            if (arduino.getBateriaDaparato().getNivelDeCarga() > 80) {
                estadoBateria = "casi llena";
                tv_energiabateria.setText("" + estadoBateria);
            }
            if (arduino.getBateriaDaparato().getNivelDeCarga() == 100) {
                estadoBateria = "completa";
                tv_energiabateria.setText("" + estadoBateria);
            }
        } else {
            tv_energiabateria.setText("No disponible");
        }
        ///cantidad de sensores
        int totalSensores = 0; // comeinza en 0
        if (sensores != null) {
            for (int i = 0; i < sensores.size(); i++) { // se necesita numero........
                totalSensores++;
            }
        }

        tv_cantidadsens.setText("" + totalSensores);
        ///cantidad de sensores
        //
        //tipos de sensores
        //dispositivo actual
        tv_tiposens.setText("" + "----");//no asignado aun
        // realizar contador de Nombre de sensores dentro del objeto sensor del array0
        boolean revisaBool;
        Integer matcherNoFuncionales = 0;
        Integer matcherSiFuncionales = 0;
        //tipos de sensores
        //
        //sensores funcionales y no funcionales   o con problemas
        if (sensores != null && !sensores.isEmpty()) {
            ///////////
            for (int i = 0; i < sensores.size(); i++) {
                revisaBool = sensores.get(i).getEstadoFuncional();
                if (revisaBool) {
                    matcherSiFuncionales++;
                } else {
                    matcherNoFuncionales++;
                }
            }

        }

        tv_sensoresconproblemas.setText(matcherNoFuncionales.toString());
        tv_sensoressinproblemas.setText(matcherSiFuncionales.toString());

    }//fin on create

    @Override//Barra
    public boolean onCreateOptionsMenu(Menu menu) { //0
        MenuInflater inflater = getMenuInflater();//1
        inflater.inflate(R.menu.barra_item, menu); // 2
        menu.findItem(R.id.item_home).setVisible(false);//ocultar boton home en home
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item_Opciones:
                Intent intent = new Intent(this, OpcionesActivity.class);//intento a home
                //intent.putExtra("dispositivo",arduino);
                startActivity(intent);//intento a home
        }
        return super.onOptionsItemSelected(item);
    }

    //fin Barra
    ///animacion y BOTONES

    public void Controlar(View view) {/// PANTALLA  CONTROLAR
        Intent intent = new Intent(this, HOME_Seguimiento.class);
        intent.putExtra("dispositivo",arduino);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);// mov  animacion
    }

    public void Seguir(View view) {/// PANTALLA  SEGUIMIENTO
        Intent intent = new Intent(this, HOME_Controlar.class);
        intent.putExtra("dispositivo",arduino);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);// mov  animacion
    }

    public void Monitorear(View view) {/// PANTALLA  Monitor
//        Intent intent = new Intent(this, HOME_Monitorizar.class);
        Intent intent = new Intent(this, HOME_Monitor.class);
        intent.putExtra("dispositivo",arduino);
        startActivity(intent);
       overridePendingTransition(R.anim.slide_out_left, R.anim.slide_out_right);// mov  animacion
    }

    private void checkBTState() {

        if (btAdapter == null) {
            Toast.makeText(getBaseContext(), "El dispositivo no soporta bluetooth", Toast.LENGTH_LONG).show();
        } else {
            if (btAdapter.isEnabled()) {
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    public void SetInfo(Arduino arduino){
        setTitle(arduino.getNombreDelDispositivoDESIGNADO());
        //setear toda informacion del dispostivo con respecto al Arduino recibido...

    }

}
