package com.example.hyliann.firewatchdesarrollo;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.hyliann.firewatchdesarrollo.modelo.Aplicacion;
import com.example.hyliann.firewatchdesarrollo.modelo.Arduino;
import com.example.hyliann.firewatchdesarrollo.modelo.ArduinoDAO;
import com.example.hyliann.firewatchdesarrollo.modelo.Sector;
import com.example.hyliann.firewatchdesarrollo.modelo.adiciones.Bateria;
import com.example.hyliann.firewatchdesarrollo.modelo.adiciones.Sensor;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BT = 1;
    ArrayList<Arduino> arduinos;//arduinos creados
    ListView lv_arduinos; // item de interfaz
    BluetoothAdapter mBluetoothAdapter;
    ArrayAdapter<Arduino> dispositivos;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); /// orientacion fija
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //arduinos =Aplicacion.getDispositivos();
        arduinos = new ArduinoDAO(this).getArduinos();//obtener dispositivos de la BD
        Aplicacion.setDispositivos(arduinos);//guardarlos en memoria de la app
        lv_arduinos = (ListView) findViewById(R.id.lv_arduinos);
        //if(arduinos.isEmpty())
        //AgregarDispositivos();
        ListarDipositivos();
        //IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        //registerReceiver(mReceiver, filter);//-
        /*
        int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
        permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
        if (permissionCheck != 0) {

            this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
        }
        */
    }

    @Override
    protected void onResume() {
        super.onResume();
        //new ArduinoDAO(this).registrar(new Arduino("0","test"));
        ActualizarLista();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void Sincronizar(View view) {//llama al cuadro de alerta
        VerificarPermisos();
        VerificarBT();
        //dispositivos.clear();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {//detectar compatibilidad bluetooth
            Toast.makeText(this, "NO COMPATIBLE!",Toast.LENGTH_SHORT);
        }else{
            if (!mBluetoothAdapter.isEnabled()) {//                         //Activar BT //*hacer en un metedo bool
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
            else{
                AlertSincronizar().show();

            }
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {//Aceptar activacion de BT
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_ENABLE_BT){
            if(resultCode==RESULT_OK){
                AlertSincronizar().show();
            }else if(resultCode==RESULT_CANCELED){
                //NO ACTIVE o ERROR AL ACTIVAR
            }
        }
    }

    // Create a BroadcastReceiver for ACTION_FOUND
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array dispositivos to show in a ListView
                dispositivos.add(new Arduino(device.getName()));

            }
        }
    };


    public AlertDialog AlertSincronizar() {//...agregar style en dialog_sicronizar.xml

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_sincronizar, null);
        builder.setView(v);
        Button aceptar = (Button) v.findViewById(R.id.bt_aceptar);
        Button cancelar = (Button) v.findViewById(R.id.bt_cancelar);//-
        AlertDialog alertDialog=builder.create();
        aceptar.setOnClickListener(//------------------------------------------------ACEPTAR
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                         // Don't forget to unregister during onDestroy
                        //mBluetoothAdapter.startDiscovery();
                        //Intent intent = new Intent(MainActivity.this, RegistroActivity.class);
                        Intent intent = new Intent(MainActivity.this, SincronizarActivity.class);
                        startActivity(intent);
                        alertDialog.dismiss();
                    }
                }
        );
        cancelar.setOnClickListener(//------------------------------------------------CANCELAR
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {//Cancelar
                        //unregisterReceiver(mReceiver);        //CRASH!!!!!!!!!!!!!!
                        //dispositivos.clear();
                        //mBluetoothAdapter.cancelDiscovery();

                        alertDialog.dismiss();
                    }
                }

        );

        return alertDialog;
    }
    //simulacion
    void AgregarDispositivos(){
        ArrayList<Sensor> sensores=new ArrayList<>();
        sensores.add(new Sensor("Humo","0.8%",true));
        sensores.add(new Sensor("Humedad","30%",true));
        sensores.add(new Sensor("Temperatura","25º",true));
        ArrayList<Sector> sectores=new ArrayList<>();
        int cantidad=(int) (Math.random() * 50) + 1; // sectores
        for(int i=0;i<cantidad;i++){
            sectores.add(new Sector(i,"Sector"));
        }
        arduinos.add(new Arduino("Arduino1",new Bateria(((int) (Math.random() * 100) + 1)),sensores,sectores));
        arduinos.add(new Arduino("Arduino2",new Bateria(((int) (Math.random() * 100) + 1)),sensores,sectores));
        arduinos.add(new Arduino("Arduino3",new Bateria(((int) (Math.random() * 100) + 1)),sensores,sectores));
        arduinos.add(new Arduino("Arduino7",new Bateria(((int) (Math.random() * 100) + 1)),sensores,sectores));
        arduinos.add(new Arduino("Rasperri3.14",new Bateria(((int) (Math.random() * 100) + 1)),sensores,sectores));
        arduinos.add(new Arduino("Unknown",new Bateria(((int) (Math.random() * 100) + 1)),sensores,sectores));
        arduinos.add(new Arduino("Glados",new Bateria(((int) (Math.random() * 100) + 1)),sensores,sectores));
        arduinos.add(new Arduino("Talos",new Bateria(((int) (Math.random() * 100) + 1)),sensores,sectores));

        Aplicacion.setDispositivos(arduinos);


    }

    void ListarDipositivos(){
        arduinos =Aplicacion.getDispositivos();
        dispositivos = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arduinos);
        lv_arduinos.setAdapter(dispositivos);
        lv_arduinos.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Arduino arduino = (Arduino) lv_arduinos.getItemAtPosition(position);
                Intent intent=new Intent(MainActivity.this,Activity_Home.class);
                intent.putExtra("dispositivo",arduino);
                startActivity(intent);
            }
        });

        lv_arduinos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Arduino arduino = (Arduino) lv_arduinos.getItemAtPosition(position);
                AlertSeleccion(arduino).show();
                return true;
            }
        });
    }

    public AlertDialog AlertSeleccion(Arduino a) {//...agregar style en dialog_sicronizar.xml
        //AlertDialog alertDialog = builder.create();
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                long resultado = new ArduinoDAO(MainActivity.this).eliminar(a.getMac());
                if (resultado != -1) {
                    Toast.makeText(MainActivity.this, "Eliminado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
                ActualizarLista();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                dialog.cancel();
            }
        });
        builder.setNeutralButton("Editar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //crear activiuti update
                Intent intent = new Intent(MainActivity.this, RegistroActivity.class);
                intent.putExtra("dispositivo", a);
                startActivity(intent);
                //ActualizarLista();
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        return dialog;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    void VerificarPermisos(){
        int permissionCheck = MainActivity.this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
        permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
        if (permissionCheck != 0) {

            this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
        }
    }

    void ActualizarLista() {
        arduinos = new ArduinoDAO(this).getArduinos();
        Aplicacion.setDispositivos(arduinos);
        dispositivos.clear();
        dispositivos.addAll(arduinos);
    }

    private void VerificarBT() {

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {//detectar compatibilidad bluetooth
            Toast.makeText(this, "NO COMPATIBLE!",Toast.LENGTH_SHORT);
        }else {
            if (!mBluetoothAdapter.isEnabled()) {//                         //Activar BT //*hacer en un metedo bool
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }

}
