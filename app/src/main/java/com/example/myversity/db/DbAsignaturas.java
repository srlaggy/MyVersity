package com.example.myversity.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.example.myversity.entidades.Asignaturas;
import com.example.myversity.entidades.ConfiguracionInicial;

import java.util.ArrayList;

public class DbAsignaturas extends DbHelper{
    Context context;

    public DbAsignaturas(@Nullable Context context){
        super(context);
        // Asignamos el contexto del constructor para usarlo mas adelante
        this.context = context;
    }

    // INSERTAR DATOS EN LA TABLA ASIGNATURA
    public long crearAsignatura(Integer id_configInicial, Integer id_tipoPromedio, String nombre){
        long id = 0;

        try{
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("id_configInicial", id_configInicial);
            values.put("nombre", nombre);
            values.put("id_tipoPromedio", id_tipoPromedio);

            id = db.insert(TABLE_ASIGNATURAS, null, values);
        } catch (Exception e){
            e.toString();
        }

        return id;
    }

    // OBTENER LISTA DE OBJETOS DE TODAS LAS ASIGNATURAS
    public ArrayList<Asignaturas> buscarAsignaturas(){
        ArrayList<Asignaturas> listaAsignaturas = new ArrayList<>();
        Asignaturas asign = null;
        Cursor cursor = null;
        try {
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            cursor = db.rawQuery("SELECT * FROM " + TABLE_ASIGNATURAS, null);
            if(cursor.moveToFirst()){
                do{
                    asign = new Asignaturas();
                    asign.setId(cursor.getInt(0));
                    asign.setId_configInicial(cursor.getInt(1));
                    asign.setId_tipoPromedio(cursor.getInt(2));
                    asign.setNombre(cursor.getString(3));
                    asign.setNota_final(cursor.getString(4));
                    listaAsignaturas.add(asign);
                } while (cursor.moveToNext());
            }

            cursor.close();

        } catch (Exception e){
            e.toString();
        }

        return listaAsignaturas;
    }
}
