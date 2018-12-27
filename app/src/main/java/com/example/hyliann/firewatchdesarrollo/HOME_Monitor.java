package com.example.hyliann.firewatchdesarrollo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

import com.example.hyliann.firewatchdesarrollo.adapters.HumedadAdapter;
import com.example.hyliann.firewatchdesarrollo.adapters.HumoAdapter;
import com.example.hyliann.firewatchdesarrollo.adapters.TemperaturaAdapter;
import com.example.hyliann.firewatchdesarrollo.modelo.Aplicacion;
import com.example.hyliann.firewatchdesarrollo.modelo.Arduino;
import com.example.hyliann.firewatchdesarrollo.modelo.Sector;

public class HOME_Monitor extends AppCompatActivity {
    Arduino arduino=new Arduino();
    String mipantalla;
    ArrayList<Sector> sectores;
    TextView tv_valor_temperatura;
    TextView tv_valor_humedad;
    TextView tv_valor_humo;

    Handler bluetoothIn;
    final int handlerState = 0;        				 //used to identify handler message
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;

    private StringBuilder recDataString = new StringBuilder();

    private ConnectedThread mConnectedThread;

    // SPP UUID service - this should work for most devices
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // String for MAC address
    private static String address = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_monitor);
        mipantalla = this.getClass().getSimpleName().toString();
        tv_valor_temperatura = (TextView) findViewById(R.id.tv_valor_temperatura);
        tv_valor_humedad = (TextView) findViewById(R.id.tv_valor_humedad);
        tv_valor_humo = (TextView) findViewById(R.id.tv_valor_humo);
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            arduino = (Arduino) intent.getSerializableExtra("dispositivo");
            Aplicacion.setDispositivo_actual(arduino);
        }
        getSupportActionBar().setTitle("Monitorizar");

        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == handlerState) {		//if message is what we want
                    String readMessage = (String) msg.obj;     // msg.arg1 = bytes from connect thread
                    recDataString.append(readMessage);     //keep appending to string until ~
                    int endOfLineIndex = recDataString.indexOf("~");                    // determine the end-of-line
                    if (endOfLineIndex > 0) {                                           // make sure there data before ~
                        String dataInPrint = recDataString.substring(0, endOfLineIndex);    // extract string
                        //txtString.setText("Datos recibidos = " + dataInPrint);
                        int dataLength = dataInPrint.length();							//get length of data received
                        //txtStringLength.setText("Tamaño del String = " + String.valueOf(dataLength));
                        //sensorView0.setText(recDataString);
                        if (recDataString.charAt(0) == '#')								//if it starts with # we know it is what we are looking for
                        {
                            String sensor0 = recDataString.substring(1, 5);             //get sensor value from string between indices 1-5
                            // Impresion de T°
                            tv_valor_temperatura.setText(sensor0+"°C");
                        }
                        else if (recDataString.charAt(0) == 'h')								//if it starts with # we know it is what we are looking for
                        {
                            String sensor1 = recDataString.substring(1, 5);             //get sensor value from string between indices 1-5
                            // Impresion de Humedad°
                            tv_valor_humedad.setText(sensor1+"%");
                        }
                        else if (recDataString.charAt(0) == 'H')								//if it starts with # we know it is what we are looking for
                        {
                            String sensor2 = recDataString.substring(1, 5);             //get sensor value from string between indices 1-5
                            // Impresion de Humo°
                            tv_valor_humo.setText(sensor2);

                        }
                        recDataString.delete(0, recDataString.length()); 					//clear all string data
                        // strIncom =" ";
                        dataInPrint = " ";
                    }
                }
            }
        };

        btAdapter = BluetoothAdapter.getDefaultAdapter();       // get Bluetooth adapter
        checkBTState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.buscar_item, menu);//set_xml
        MenuItem menuItem = menu.findItem(R.id.item_buscar);//get desde xml
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Buscar");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {//agregar get busqeda al aceptar
                //Toast.makeText(getApplicationContext(), "Buscando...",Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {//Agregar logica de busqeda al escribir
                //Toast.makeText(getApplicationContext(), newText,Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
    //item selected ir home

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.item_home:
                intent = new Intent(this, Activity_Home.class);//intento a home
                intent.putExtra("dispositivo", arduino);
                startActivity(intent);
                break;
            case R.id.item_Opciones:
                intent = new Intent(this, OpcionesActivity.class);//intento a home
                //intent.putExtra("dispositivo",arduino);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private ArrayList<Sector> AgregarSectores() {//cambiar por sectores de cada tipo de lista para filtrar...
        ArrayList<Sector> sectoreslist = new ArrayList<>();
        //for(int i=0;i<10;i++){
        //    sectoreslist.add(new Sector(i,"Sector"));
        //}
        sectoreslist = arduino.getSectores();
        return sectoreslist;
    }


    private void SetVista(RecyclerView lista) {
        if (lista.getLayoutManager() instanceof GridLayoutManager) {
            lista.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        } else {
            lista.setLayoutManager(new GridLayoutManager(this, 2));
        }

    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {

        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
        //creates secure outgoing connecetion with BT device using UUID
    }

    @Override
    public void onResume() {
        super.onResume();

        //Get MAC address from DeviceListActivity via intent
        arduino = Aplicacion.getDispositivo_actual();
        //Get the MAC address from the DeviceListActivty via EXTRA
        address = arduino.getMac();
        //address = intent.getStringExtra("dispositivo");
        //create device and set the MAC address
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "La creacción del Socket fallo", Toast.LENGTH_LONG).show();
        }
        // Establish the Bluetooth socket connection.
        try
        {
            btSocket.connect();
        } catch (IOException e) {
            try
            {
                btSocket.close();
            } catch (IOException e2)
            {
                //insert code to deal with this
            }
        }
        mConnectedThread = new ConnectedThread(btSocket);
        mConnectedThread.start();

        //I send a character when resuming.beginning transmission to check device is connected
        //If it is not an exception will be thrown in the write method and finish() will be called
        mConnectedThread.write("x");
    }

    @Override
    public void onPause()
    {
        super.onPause();
        try
        {
            //Don't leave Bluetooth sockets open when leaving activity
            btSocket.close();
        } catch (IOException e2) {
            //insert code to deal with this
        }
    }

    //Checks that the Android device Bluetooth is available and prompts to be turned on if off
    private void checkBTState() {

        if(btAdapter==null) {
            Toast.makeText(getBaseContext(), "El dispositivo no soporta bluetooth", Toast.LENGTH_LONG).show();
        } else {
            if (btAdapter.isEnabled()) {
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    //create new class for connect thread
    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        //creation of the connect thread
        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                //Create I/O streams for connection
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }


        public void run() {
            byte[] buffer = new byte[256];
            int bytes;

            // Keep looping to listen for received messages
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);            //read bytes from input buffer
                    String readMessage = new String(buffer, 0, bytes);
                    // Send the obtained bytes to the UI Activity via handler
                    //sensorView0.setText(readMessage);
                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }

        //write method
        public void write(String input) {
            byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(msgBuffer);                //write bytes over BT connection via outstream
                //sensorView0.setText(msgBuffer.toString());
            } catch (IOException e) {
                //if you cannot write, close the application
                Toast.makeText(getBaseContext(), "La Conexión fallo", Toast.LENGTH_LONG).show();
                finish();

            }
        }

        public void ChangeView(View view) {
        }
    }
}
