package com.example.hyliann.firewatchdesarrollo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.hyliann.firewatchdesarrollo.modelo.Arduino;

import java.util.ArrayList;
import java.util.Set;

public class SincronizarActivity extends AppCompatActivity {
    BluetoothAdapter mBluetoothAdapter;
    ListView lv_dispositivos;
    ArrayList<Arduino> dispositivos = new ArrayList<>();
    ArrayAdapter<Arduino> list_dispositivos;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sincronizar);
        lv_dispositivos = (ListView) findViewById(R.id.lv_dispositivos);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        ListarDipositivos();

        //obtener permisos
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
        //Toast.makeText(this, "Resume",Toast.LENGTH_SHORT).show();
        ListarDipositivos();
        super.onResume();
    }

    @Override
    protected void onPause() {
        //Toast.makeText(this, "Pause",Toast.LENGTH_SHORT).show();
        super.onPause();
        mBluetoothAdapter.cancelDiscovery();
        //unregisterReceiver(mReceiver);
    }

    @Override
    protected void onStop() {
        //Toast.makeText(this, "Stop",Toast.LENGTH_SHORT).show();
        //mBluetoothAdapter.cancelDiscovery();
        //unregisterReceiver(mReceiver);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        //Toast.makeText(this, "Destroy",Toast.LENGTH_SHORT).show();
        mBluetoothAdapter.cancelDiscovery();
        unregisterReceiver(mReceiver);
        super.onDestroy();

    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {//agregar boton scanear
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array dispositivos to show in a ListView
                Arduino dispositivo = new Arduino(device.getAddress(), device.getName());
                list_dispositivos.add(dispositivo);
            }
        }
    };

    void Scan() {
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);
        mBluetoothAdapter.startDiscovery();
    }

    void GetVinculados() {
        list_dispositivos.clear();//limpiar lista
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array dispositivos to show in a ListView
                Arduino dispositivo = new Arduino(device.getAddress(), device.getName());
                list_dispositivos.add(dispositivo);
            }
        }
    }

    void ListarDipositivos() {
        //Scan();
        list_dispositivos = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dispositivos);
        lv_dispositivos.setAdapter(list_dispositivos);
        lv_dispositivos.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Arduino arduino = (Arduino) lv_dispositivos.getItemAtPosition(position);
                Intent intent = new Intent(SincronizarActivity.this, RegistroActivity.class);
                intent.putExtra("dispositivo", arduino);
                startActivity(intent);
            }
        });

        GetVinculados();
        Scan();
    }

    public void Actualizar(View view) {
        ListarDipositivos();
    }
    // Create a BroadcastReceiver for ACTION_FOUND.

}
