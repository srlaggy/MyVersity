package com.example.myversity.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

public class DbAsignaturas extends DbHelper{
    Context context;

    public DbAsignaturas(@Nullable Context context){
        super(context);
        // Asignamos el contexto del constructor para usarlo mas adelante
        this.context = context;
    }

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
}
