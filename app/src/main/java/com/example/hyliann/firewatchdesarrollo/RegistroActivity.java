package com.example.hyliann.firewatchdesarrollo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hyliann.firewatchdesarrollo.modelo.Aplicacion;
import com.example.hyliann.firewatchdesarrollo.modelo.Arduino;
import com.example.hyliann.firewatchdesarrollo.modelo.ArduinoDAO;
import com.example.hyliann.firewatchdesarrollo.modelo.Sector;
import com.example.hyliann.firewatchdesarrollo.modelo.adiciones.Bateria;
import com.example.hyliann.firewatchdesarrollo.modelo.adiciones.Sensor;

import java.util.ArrayList;

/**
 * A login screen that offers login via email/password.
 */
public class RegistroActivity extends AppCompatActivity {




    // UI references.
    private EditText et_nombre;
    private EditText et_codigo;
    private View mProgressView;
    private View mLoginFormView;
    Arduino arduino;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        setTitle("Dispositivo Encontrado");
        // Set up the login form.
        et_nombre = (EditText) findViewById(R.id.et_nombre);
        et_codigo = (EditText) findViewById(R.id.et_fono);

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            arduino = (Arduino) intent.getSerializableExtra("dispositivo");
            //Aplicacion.setDispositivo_actual(arduino);
            et_nombre.setText(arduino.getNombreDelDispositivoDESIGNADO());
            //et_codigo.setText(arduino.getMac().toString());
        }

        Button mEmailSignInButton = (Button) findViewById(R.id.sincronizando);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //attemptLogin();
                //Arduino dispositivo =new Arduino();
                String nombre=et_nombre.getText().toString();
                //String codigo=et_codigo.getText().toString();
                String fono = et_codigo.getText().toString();
                if (!nombre.trim().equals("") && !fono.trim().equals("")) {
                    arduino.setNombreDelDispositivoDESIGNADO(nombre);
                    arduino.setNumeroAlertaDESIGNADO(fono);
                    if (AgregarNuevoDispositivo(arduino)) {
                        Intent intent= new Intent(RegistroActivity.this,Activity_Home.class);
                        intent.putExtra("dispositivo", arduino);
                        startActivity(intent);
                        Toast.makeText(RegistroActivity.this, "Sincronizando...", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(RegistroActivity.this, "Intente nuevamente...", Toast.LENGTH_SHORT).show();
                        //et_codigo.setText(dispositivo.getMac().toString());
                    }
                }
                else {
                    Toast.makeText(RegistroActivity.this,"Debe ingresar todos los datos!",Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    public boolean AgregarNuevoDispositivo(Arduino arduino){

        if (new ArduinoDAO(this).buscar(arduino.getMac()) != null) {
            Toast.makeText(RegistroActivity.this, "El dispostivo ya esta registrado...", Toast.LENGTH_SHORT).show();
            return false;
        } else if (new ArduinoDAO(this).buscar_nombre(arduino.getNombreDelDispositivoDESIGNADO()) != null) {
            Toast.makeText(RegistroActivity.this, "Ingrese un nuevo nombre!", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            long resultado = new ArduinoDAO(this).registrar(arduino);
            if (resultado != -1) {
                return true;
            }
            Toast.makeText(RegistroActivity.this, "Error al registrar", Toast.LENGTH_SHORT).show();
            return false;

        }
    }
}

