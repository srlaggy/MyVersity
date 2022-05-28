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

    // METODO PARA INSERTAR UNA FILA EN LA TABLA (CON ID AUTOINCREMENTAL)
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
            db.close();
        } catch (Exception e){
            e.toString();
        }

        return id;
    }

    public long insertarConfigInicialConId(Long id, String nota_minima, String nota_maxima, String nota_aprobacion, Boolean decimal, Boolean orientacion_asc){
        long idN = 0;

        try{
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("id", id);
            values.put("nota_minima", nota_minima);
            values.put("nota_maxima", nota_maxima);
            values.put("nota_aprobacion", nota_aprobacion);
            values.put("decimal", decimal);
            values.put("orientacion_asc", orientacion_asc);

            idN = db.insert(TABLE_CONFIG_INICIAL, null, values);
            db.close();
        } catch (Exception e){
            e.toString();
        }

        return idN;
    }

    // METODO PARA OBTENER UNA LISTA DE TODOS LOS ELEMENTOS DE LA TABLA
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
            db.close();

        } catch (Exception e){
            e.toString();
        }

        return listaConfig;
    }

    // METODO PARA BUSCAR LA ULTIMA INSERCION EN LA TABLA
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
            db.close();

        } catch (Exception e){
            e.toString();
        }

        return config;
    }

    // METODO PARA BUSCAR LA PRIMERA INSERCION DE LA TABLA (ID 1 => CONFIGURACION POR DEFECTO)
    public ConfiguracionInicial buscarPrimeraConfiguracion(){
        ConfiguracionInicial config = null;
        Cursor cursor = null;

        try {
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            cursor = db.rawQuery("SELECT * FROM " + TABLE_CONFIG_INICIAL + " WHERE id=1 LIMIT 1", null);
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
            db.close();

        } catch (Exception e){
            e.toString();
        }

        return config;
    }

    // METODO PARA BUSCAR UN REGISTRO CUALQUIERA DE LA TABLA (SEGUN EL ID QUE SE LE ENTREGA)
    public ConfiguracionInicial buscarRegistro(Integer id){
        ConfiguracionInicial config = null;
        Cursor cursor = null;

        try {
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            cursor = db.rawQuery("SELECT * FROM " + TABLE_CONFIG_INICIAL + " WHERE id=" + id.toString() + " LIMIT 1", null);
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
            db.close();

        } catch (Exception e){
            e.toString();
        }

        return config;
    }

    // METODO PARA ACTUALIZAR UN REGISTRO SEGUN SU ID
    public Long actualizarRegistro(Integer id, ConfiguracionInicial ci){
        Long estado = 0L;
        String where = "id = " + id.toString();

        try{
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("nota_minima", ci.getMin());
            values.put("nota_maxima", ci.getMax());
            values.put("nota_aprobacion", ci.getNotaAprobacion());
            values.put("decimal", ci.getDecimal());
            values.put("orientacion_asc", ci.getOrientacionAsc());

            db.update(TABLE_CONFIG_INICIAL, values, where, null);
            estado = 1L;
            db.close();
        } catch (Exception e){
            e.toString();
        }
        return estado;
    }

    // METODO PARA BORRAR UN REGISTRO DE SER NECESARIO (EVITAR BORRAR EL ID 1)
    public Long borrarRegistro(Integer id){
        Long estado = 0L;
        String whereClause = "_id=?";
        String[] whereArgs = new String[] {id.toString()};

        try {
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete(TABLE_CONFIG_INICIAL, whereClause, whereArgs);

            estado = 1L;
            db.close();
        } catch (Exception e){
            e.toString();
        }

        return estado;
    }
}