package com.example.myversity.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.example.myversity.entidades.TipoPromedio;
import com.example.myversity.entidades.tiposPromedios.MediaAritmetica;
import com.example.myversity.entidades.tiposPromedios.MediaCuadratica;
import com.example.myversity.entidades.tiposPromedios.MediaGeometrica;
import com.example.myversity.entidades.tiposPromedios.MediaPonderada;
import com.example.myversity.entidades.tiposPromedios.MediaSoloSuma;

import java.util.ArrayList;

public class DbTipoPromedio extends DbHelper{
    Context context;

    public DbTipoPromedio(@Nullable Context context) {
        super(context);
        // Asignamos el contexto del constructor para usarlo mas adelante
        this.context = context;
    }

    public ArrayList<TipoPromedio> buscarTiposPromedios(){
        ArrayList<TipoPromedio> listaTipos = new ArrayList<>();
        TipoPromedio tp = null;
        Cursor cursor = null;
        Integer id = null;
        String nombre = null;
        Boolean usa_peso = null;
        try {
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            cursor = db.rawQuery("SELECT * FROM " + TABLE_TIPO_PROMEDIO, null);
            if(cursor.moveToFirst()){
                do{
                    id = cursor.getInt(0);
                    nombre = cursor.getString(1);
                    usa_peso = cursor.getInt(2) != 0;

                    tp = hijoTipoPromedio(nombre);
                    if(tp != null){
                        tp.setId(id);
                    }

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

    public TipoPromedio buscarTipoPromedio(Integer idNuevo){
        TipoPromedio tp = null;
        Cursor cursor = null;
        Integer id = null;
        String nombre = null;
        Boolean usa_peso = null;
        try {
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            cursor = db.rawQuery("SELECT * FROM " + TABLE_TIPO_PROMEDIO + " WHERE id=" + idNuevo.toString() + " LIMIT 1", null);
            if(cursor.moveToFirst()){
                id = cursor.getInt(0);
                nombre = cursor.getString(1);
                usa_peso = cursor.getInt(2) != 0;

                tp = hijoTipoPromedio(nombre);
                if(tp != null){
                    tp.setId(id);
                }
            }

            cursor.close();
            db.close();

        } catch (Exception e){
            e.toString();
        }

        return tp;
    }

    // AUXILIAR PARA ENTREGAR EL TIPO CORRECTO DE PROMEDIO
    // NO USAR EN CRUD
    private TipoPromedio hijoTipoPromedio(String nombre){
        TipoPromedio tp = null;
        switch (nombre){
            case "Media aritmética":
                tp = new MediaAritmetica();
                break;
            case "Media Cuadrática":
                tp = new MediaCuadratica();
                break;
            case "Media geométrica":
                tp = new MediaGeometrica();
                break;
            case "Media ponderada":
                tp = new MediaPonderada();
                break;
            case "Suma":
                tp = new MediaSoloSuma();
                break;
        }
        return tp;
    }
}
