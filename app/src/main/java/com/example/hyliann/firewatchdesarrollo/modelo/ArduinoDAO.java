package com.example.hyliann.firewatchdesarrollo.modelo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class ArduinoDAO {

    private ConexionHelper helper;

    public ArduinoDAO(Context context) {
        this.helper = new ConexionHelper(context);
    }

    //public int registrar(String email, String clave){
    public long registrar(Arduino a) {
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            ContentValues c = new ContentValues();
            c.put("mac", a.getMac());
            c.put("nombre", a.getNombreDelDispositivoDESIGNADO());
            c.put("codigo", a.getCodigoBlueDESIGNADO());
            c.put("fono", a.getNumeroAlertaDESIGNADO());
            long resultado = db.insert("Arduino", null, c);
            return resultado;
        } catch (Exception e) {
            return -1;
        } finally {
            db.close();
        }
    }

    public long actualizar(Arduino a) {
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            ContentValues c = new ContentValues();
            c.put("nombre", a.getNombreDelDispositivoDESIGNADO());
            c.put("codigo", a.getCodigoBlueDESIGNADO());
            c.put("fono", a.getNumeroAlertaDESIGNADO());
            long resultado = db.update("Arduino", c, "mac=?", new String[]{a.getMac()});
            return resultado;
        } catch (Exception e) {
            return -1;
        } finally {
            db.close();
        }
    }

    public long eliminar(String mac) {
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            long resultado = db.delete("Arduino", "mac=?", new String[]{mac});
            return resultado;
        } catch (Exception e) {
            return -1;
        } finally {
            db.close();
        }
    }

    public Arduino buscar(String mac) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Arduino a = null;
        try {
            //ArrayList<Arduino> listado = new ArrayList<>();
            Cursor c = db.rawQuery("SELECT * FROM Arduino WHERE mac =" + mac, null);
            while (c.moveToNext()) {
                a = new Arduino();
                a.setMac(c.getString(0));
                a.setNombreDelDispositivoDESIGNADO(c.getString(1));
                a.setCodigoBlueDESIGNADO(c.getString(2));
                a.setNumeroAlertaDESIGNADO(c.getString(3));
            }
            return a;
        } catch (Exception e) {
            return null;
        } finally {
            db.close();
        }
    }

    public Arduino buscar_nombre(String nombre) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Arduino a = null;
        try {
            Cursor c = db.rawQuery("SELECT * FROM Arduino WHERE nombre =" + nombre, null);
            while (c.moveToNext()) {
                a = new Arduino();
                a.setMac(c.getString(0));
                a.setNombreDelDispositivoDESIGNADO(c.getString(1));
                a.setCodigoBlueDESIGNADO(c.getString(2));
                a.setNumeroAlertaDESIGNADO(c.getString(3));
            }
            return a;
        } catch (Exception e) {
            return null;
        } finally {
            db.close();
        }
    }

    public ArrayList<Arduino> getArduinos() {
        SQLiteDatabase db = helper.getReadableDatabase();
        try {
            ArrayList<Arduino> listado = new ArrayList<>();
            Cursor c = db.rawQuery("SELECT * FROM Arduino", null);
            while (c.moveToNext()) {
                Arduino a = new Arduino();
                a.setMac(c.getString(0));
                a.setNombreDelDispositivoDESIGNADO(c.getString(1));
                a.setCodigoBlueDESIGNADO(c.getString(2));
                a.setNumeroAlertaDESIGNADO(c.getString(3));
                listado.add(a);
            }
            return listado;
        } catch (Exception e) {
            return null;
        } finally {
            db.close();
        }
    }

}
