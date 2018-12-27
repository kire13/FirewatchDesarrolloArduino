package com.example.hyliann.firewatchdesarrollo.modelo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ConexionHelper extends SQLiteOpenHelper {
    private static String DATABASE_NAME = "db_arduinos.db";
    private static int DATABASE_VERSION = 2;

    public ConexionHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE Arduino(" +
                "mac varchar(100) primary key not null," +
                "nombre varchar(100) unique not null," +
                "codigo varchar(16) ," +
                "fono varchar(100)  " +
                ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE Arduino");
        String query = "CREATE TABLE Arduino(" +
                "mac varchar(100) primary key not null," +
                "nombre varchar(100) unique not null," +
                "codigo varchar(16) ," +
                "fono varchar(100)  " +
                ");";
        db.execSQL(query);
    }
}