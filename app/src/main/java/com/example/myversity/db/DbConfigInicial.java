package com.example.myversity.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import androidx.annotation.Nullable;

import com.example.myversity.entidades.ConfiguracionInicial;

import java.util.ArrayList;

public class DbConfigInicial extends DbHelper{
    Context context;

    public DbConfigInicial(@Nullable Context context) {
        super(context);
        // Asignamos el contexto del constructor para usarlo mas adelante
        this.context = context;
    }

    public long insertarConfigInicial(String nota_minima, String nota_maxima, String nota_aprobacion, Boolean decimal, Boolean orientacion_asc){
        long id = 0;

        try{
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("nota_minima", nota_minima);
            values.put("nota_maxima", nota_maxima);
            values.put("nota_aprobacion", nota_aprobacion);
            values.put("decimal", decimal);
            values.put("orientacion_asc", orientacion_asc);

            id = db.insert(TABLE_CONFIG_INICIAL, null, values);
        } catch (Exception e){
            e.toString();
        }

        return id;
    }

    public ArrayList<ConfiguracionInicial> buscarConfiguraciones(){
        ArrayList<ConfiguracionInicial> listaConfig = new ArrayList<>();
        ConfiguracionInicial config = null;
        Cursor cursor = null;
        try {
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            cursor = db.rawQuery("SELECT * FROM " + TABLE_CONFIG_INICIAL, null);
            if(cursor.moveToFirst()){
                do{
                    config = new ConfiguracionInicial();
                    config.setId(cursor.getInt(0));
                    config.setMin(cursor.getString(1));
                    config.setMax(cursor.getString(2));
                    config.setNotaAprobacion(cursor.getString(3));
                    config.setDecimal(cursor.getInt(4) != 0);
                    config.setOrientacionAsc(cursor.getInt(5) != 0);
                    listaConfig.add(config);
                } while (cursor.moveToNext());
            }

            cursor.close();

        } catch (Exception e){
            e.toString();
        }

        return listaConfig;
    }

    public ConfiguracionInicial buscarUltimaConfiguracion(){
        ConfiguracionInicial config = null;
        Cursor cursor = null;

        try {
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            cursor = db.rawQuery("SELECT * FROM " + TABLE_CONFIG_INICIAL + " ORDER BY id DESC LIMIT 1", null);
            if(cursor.moveToFirst()){
                config = new ConfiguracionInicial();
                config.setId(cursor.getInt(0));
                config.setMin(cursor.getString(1));
                config.setMax(cursor.getString(2));
                config.setNotaAprobacion(cursor.getString(3));
                config.setDecimal(cursor.getInt(4) != 0);
                config.setOrientacionAsc(cursor.getInt(5) != 0);
            }

            cursor.close();

        } catch (Exception e){
            e.toString();
        }

        return config;
    }
}