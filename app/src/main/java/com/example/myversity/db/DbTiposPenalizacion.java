package com.example.myversity.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.example.myversity.entidades.TipoPromedio;
import com.example.myversity.entidades.TiposPenalizacion;

import java.util.ArrayList;

public class DbTiposPenalizacion extends DbHelper{
    Context context;

    public DbTiposPenalizacion(@Nullable Context context) {
        super(context);
        // Asignamos el contexto del constructor para usarlo mas adelante
        this.context = context;
    }

    public ArrayList<TiposPenalizacion> buscarTiposPenalizacion(){
        ArrayList<TiposPenalizacion> listaTipos = new ArrayList<>();
        TiposPenalizacion tp = null;
        Cursor cursor = null;
        Integer id = null;
        String nombre = null;
        Boolean cuando_penaliza = null;
        Boolean requiere_valor = null;
        try {
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            cursor = db.rawQuery("SELECT * FROM " + TABLE_TIPOS_PENALIZACION, null);
            if(cursor.moveToFirst()){
                do{
                    id = cursor.getInt(0);
                    nombre = cursor.getString(1);
                    cuando_penaliza = cursor.getInt(2) != 0;
                    requiere_valor = cursor.getInt(3) != 0;

                    tp = new TiposPenalizacion(nombre, cuando_penaliza, requiere_valor);
                    tp.setId(id);

                    listaTipos.add(tp);
                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();

        } catch (Exception e){
            e.toString();
        }

        return listaTipos;
    }

    public TiposPenalizacion buscarTipoPenalizacion(Integer idNuevo){
        TiposPenalizacion tp = null;
        Cursor cursor = null;
        Integer id = null;
        String nombre = null;
        Boolean cuando_penaliza = null;
        Boolean requiere_valor = null;
        try {
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            cursor = db.rawQuery("SELECT * FROM " + TABLE_TIPOS_PENALIZACION + " WHERE id=" + idNuevo.toString() + " LIMIT 1", null);
            if(cursor.moveToFirst()){
                id = cursor.getInt(0);
                nombre = cursor.getString(1);
                cuando_penaliza = cursor.getInt(2) != 0;
                requiere_valor = cursor.getInt(3) != 0;

                tp = new TiposPenalizacion(nombre, cuando_penaliza, requiere_valor);
                tp.setId(id);
            }

            cursor.close();
            db.close();

        } catch (Exception e){
            e.toString();
        }

        return tp;
    }
}
