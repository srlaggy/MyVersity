package com.example.myversity.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.example.myversity.entidades.Notas;
import com.example.myversity.entidades.TiposPenalizacion;

import java.util.ArrayList;

public class DbNotas extends DbHelper{
    Context context;

    public DbNotas(@Nullable Context context) {
        super(context);
        // Asignamos el contexto del constructor para usarlo mas adelante
        this.context = context;
    }

    // INSERTAR NOTAS NO CONTIENE NI NOTA, NI CONDICION, NI NOTA_COND
    // EL PESO SE DEBE AGREGAR, AUNQUE SEA NULO (DEPENDE DE SI LA EVALUACION PADRE USA PESO)
    public long insertarNota(Integer id_evaluaciones, String peso){
        long id = 0;

        try{
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("id_evaluaciones", id_evaluaciones);
            if(peso != null){
                values.put("peso", peso);
            }

            id = db.insert(TABLE_NOTAS, null, values);
            db.close();
        } catch (Exception e){
            e.toString();
        }

        return id;
    }

    public long insertarNotaFull(Integer id_evaluaciones, Boolean cond, String nota_cond, String peso){
        long id = 0;

        try{
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("id_evaluaciones", id_evaluaciones);
            values.put("cond", cond);
            if(cond == true){
                values.put("nota_cond", nota_cond);
            }
            if(peso != null){
                values.put("peso", peso);
            }

            id = db.insert(TABLE_NOTAS, null, values);
            db.close();
        } catch (Exception e){
            e.toString();
        }

        return id;
    }

    public ArrayList<Notas> buscarNotas(){
        ArrayList<Notas> listaNotas = new ArrayList<>();
        Notas notas = null;
        Cursor cursor = null;
        Integer id = null;
        Integer id_evaluaciones = null;
        String nota = null;
        Boolean cond = null;
        String nota_cond = null;
        String peso = null;
        try {
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            cursor = db.rawQuery("SELECT * FROM " + TABLE_NOTAS, null);
            if(cursor.moveToFirst()){
                do{
                    id = cursor.getInt(0);
                    id_evaluaciones = cursor.getInt(1);
                    nota = cursor.getString(2);
                    cond = cursor.getInt(3) != 0;
                    nota_cond = cursor.getString(4);
                    peso = cursor.getString(5);

                    notas = new Notas(id, id_evaluaciones, nota, cond, nota_cond, peso);
                    listaNotas.add(notas);
                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();
        } catch (Exception e){
            e.toString();
        }

        return listaNotas;
    }

    public ArrayList<Notas> buscarNotasPorIdEvaluacion(Integer idEvaluacion){
        ArrayList<Notas> listaNotas = new ArrayList<>();
        Notas notas = null;
        Cursor cursor = null;
        Integer id = null;
        Integer id_evaluaciones = null;
        String nota = null;
        Boolean cond = null;
        String nota_cond = null;
        String peso = null;
        try {
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            cursor = db.rawQuery("SELECT * FROM " + TABLE_NOTAS + " WHERE id_evaluaciones=" + idEvaluacion.toString(), null);
            if(cursor.moveToFirst()){
                do{
                    id = cursor.getInt(0);
                    id_evaluaciones = cursor.getInt(1);
                    nota = cursor.getString(2);
                    cond = cursor.getInt(3) != 0;
                    nota_cond = cursor.getString(4);
                    peso = cursor.getString(5);

                    notas = new Notas(id, id_evaluaciones, nota, cond, nota_cond, peso);
                    listaNotas.add(notas);
                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();

        } catch (Exception e){
            e.toString();
        }

        return listaNotas;
    }

    public Notas buscarNotaPorId(Integer idNota){
        Notas notas = null;
        Cursor cursor = null;
        Integer id = null;
        Integer id_evaluaciones = null;
        String nota = null;
        Boolean cond = null;
        String nota_cond = null;
        String peso = null;
        try {
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            cursor = db.rawQuery("SELECT * FROM " + TABLE_NOTAS + " WHERE id=" + idNota.toString(), null);
            if(cursor.moveToFirst()){
                id = cursor.getInt(0);
                id_evaluaciones = cursor.getInt(1);
                nota = cursor.getString(2);
                cond = cursor.getInt(3) != 0;
                nota_cond = cursor.getString(4);
                peso = cursor.getString(5);

                notas = new Notas(id, id_evaluaciones, nota, cond, nota_cond, peso);
            }

            cursor.close();
            db.close();

        } catch (Exception e){
            e.toString();
        }

        return notas;
    }

    public Long actualizarNota(Integer id, String nota){
        Long estado = 0L;
        String where = "id = " + id.toString();

        try{
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("nota", nota);

            db.update(TABLE_NOTAS, values, where, null);
            estado = 1L;
            db.close();
        } catch (Exception e){
            e.toString();
        }
        return estado;
    }

    public Long actualizarCond(Integer id, Boolean cond){
        Long estado = 0L;
        String where = "id = " + id.toString();

        try{
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("cond", cond);

            db.update(TABLE_NOTAS, values, where, null);
            estado = 1L;
            db.close();
        } catch (Exception e){
            e.toString();
        }
        return estado;
    }

    public Long actualizarNotaCond(Integer id, String nota_cond){
        Long estado = 0L;
        String where = "id = " + id.toString();

        try{
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("nota_cond", nota_cond);

            db.update(TABLE_NOTAS, values, where, null);
            estado = 1L;
            db.close();
        } catch (Exception e){
            e.toString();
        }
        return estado;
    }

    public Long actualizarPeso(Integer id, String peso){
        Long estado = 0L;
        String where = "id = " + id.toString();

        try{
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("peso", peso);

            db.update(TABLE_NOTAS, values, where, null);
            estado = 1L;
            db.close();
        } catch (Exception e){
            e.toString();
        }
        return estado;
    }

    public Long borrarNota(Integer id){
        Long estado = 0L;
        String whereClause = "id=?";
        String[] whereArgs = new String[] {id.toString()};

        try {
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete(TABLE_NOTAS, whereClause, whereArgs);

            estado = 1L;
            db.close();
        } catch (Exception e){
            e.toString();
        }

        return estado;
    }

    public Long borrarNotasPorIdEvaluaciones(Integer id){
        Long estado = 0L;
        String whereClause = "id_evaluaciones=?";
        String[] whereArgs = new String[] {id.toString()};

        try {
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete(TABLE_NOTAS, whereClause, whereArgs);

            estado = 1L;
            db.close();
        } catch (Exception e){
            e.toString();
        }

        return estado;
    }
}
