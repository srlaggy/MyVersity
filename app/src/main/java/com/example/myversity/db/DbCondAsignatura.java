package com.example.myversity.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.example.myversity.entidades.Asignaturas;
import com.example.myversity.entidades.CondAsignatura;
import com.example.myversity.entidades.ConfiguracionInicial;
import com.example.myversity.entidades.TipoPromedio;
import com.example.myversity.entidades.TiposPenalizacion;

import java.util.ArrayList;

public class DbCondAsignatura extends DbHelper{
    Context context;

    public DbCondAsignatura(@Nullable Context context){
        super(context);
        // Asignamos el contexto del constructor para usarlo mas adelante
        this.context = context;
    }

    // CondAsignatura debe contener todos sus items completos
    // menos el id y los extras
    public long insertarCondAsignatura(CondAsignatura ca){
        long id = 0;

        try{
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("id_asignaturas", ca.getId_asignaturas());
            values.put("id_tiposPenalizacion", ca.getId_tiposPenalizacion());
            values.put("condicion", ca.getCondicion());
            values.put("chequeado", ca.getChequeado());
            values.put("valor", ca.getValor());

            id = db.insert(TABLE_COND_ASIGNATURAS, null, values);
            db.close();
        } catch (Exception e){
            e.toString();
        }

        return id;
    }

    public ArrayList<CondAsignatura> buscarCondAsignaturas(){
        ArrayList<CondAsignatura> listaCondAsignaturas = new ArrayList<>();
        CondAsignatura ca = null;
        Cursor cursor = null;
        TiposPenalizacion tp = null;
        try {
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            cursor = db.rawQuery("SELECT * FROM " + TABLE_COND_ASIGNATURAS, null);
            if (cursor.moveToFirst()) {
                do {
                    ca = new CondAsignatura();
                    ca.setId(cursor.getInt(0));
                    ca.setId_asignaturas(cursor.getInt(1));
                    ca.setId_tiposPenalizacion(cursor.getInt(2));
                    ca.setCondicion(cursor.getString(3));
                    ca.setChequeado(cursor.getInt(4) != 0);
                    ca.setValor(cursor.getString(5));

                    DbTiposPenalizacion dbTiposPenalizacion = new DbTiposPenalizacion(context);
                    tp = dbTiposPenalizacion.buscarTipoPenalizacion(ca.getId_tiposPenalizacion());
                    dbTiposPenalizacion.close();
                    ca.setTp(tp);

                    listaCondAsignaturas.add(ca);

                } while (cursor.moveToNext()) ;
            }

            cursor.close();
            db.close();

        } catch (Exception e){
            e.toString();
        }
        return listaCondAsignaturas;
    }

    public CondAsignatura buscarCondAsignaturaPorId(Integer id){
        CondAsignatura ca = null;
        Cursor cursor = null;
        TiposPenalizacion tp = null;
        try {
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            cursor = db.rawQuery("SELECT * FROM " + TABLE_COND_ASIGNATURAS + " WHERE id=" + id.toString() + " LIMIT 1", null);
            if (cursor.moveToFirst()) {
                ca = new CondAsignatura();
                ca.setId(cursor.getInt(0));
                ca.setId_asignaturas(cursor.getInt(1));
                ca.setId_tiposPenalizacion(cursor.getInt(2));
                ca.setCondicion(cursor.getString(3));
                ca.setChequeado(cursor.getInt(4) != 0);
                ca.setValor(cursor.getString(5));

                DbTiposPenalizacion dbTiposPenalizacion = new DbTiposPenalizacion(context);
                tp = dbTiposPenalizacion.buscarTipoPenalizacion(ca.getId_tiposPenalizacion());
                dbTiposPenalizacion.close();
                ca.setTp(tp);
            }

            cursor.close();
            db.close();

        } catch (Exception e){
            e.toString();
        }
        return ca;
    }

    public ArrayList<CondAsignatura> buscarCondAsignaturasPorIdAsignatura(Integer idAsignatura){
        ArrayList<CondAsignatura> listaCondAsignaturas = new ArrayList<>();
        CondAsignatura ca = null;
        Cursor cursor = null;
        TiposPenalizacion tp = null;
        try {
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            cursor = db.rawQuery("SELECT * FROM " + TABLE_COND_ASIGNATURAS + " WHERE id_asignaturas=" + idAsignatura.toString(), null);
            if (cursor.moveToFirst()) {
                do {
                    ca = new CondAsignatura();
                    ca.setId(cursor.getInt(0));
                    ca.setId_asignaturas(cursor.getInt(1));
                    ca.setId_tiposPenalizacion(cursor.getInt(2));
                    ca.setCondicion(cursor.getString(3));
                    ca.setChequeado(cursor.getInt(4) != 0);
                    ca.setValor(cursor.getString(5));

                    DbTiposPenalizacion dbTiposPenalizacion = new DbTiposPenalizacion(context);
                    tp = dbTiposPenalizacion.buscarTipoPenalizacion(ca.getId_tiposPenalizacion());
                    dbTiposPenalizacion.close();
                    ca.setTp(tp);

                    listaCondAsignaturas.add(ca);

                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();

        } catch (Exception e){
            e.toString();
        }
        return listaCondAsignaturas;
    }

    public Long actualizarIdTiposPenalizacion(Integer id, Integer idTipoPenalizacion){
        Long estado = 0L;
        String where = "id = " + id.toString();

        try{
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("id_tiposPenalizacion", idTipoPenalizacion);

            db.update(TABLE_COND_ASIGNATURAS, values, where, null);
            estado = 1L;
            db.close();
        } catch (Exception e){
            e.toString();
        }
        return estado;
    }

    public Long actualizarCondicionNombre(Integer id, String condicion){
        Long estado = 0L;
        String where = "id = " + id.toString();

        try{
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("condicion", condicion);

            db.update(TABLE_COND_ASIGNATURAS, values, where, null);
            estado = 1L;
            db.close();
        } catch (Exception e){
            e.toString();
        }
        return estado;
    }

    public Long actualizarChequeado(Integer id, Boolean chequeado){
        Long estado = 0L;
        String where = "id = " + id.toString();

        try{
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("chequeado", chequeado);

            db.update(TABLE_COND_ASIGNATURAS, values, where, null);
            estado = 1L;
            db.close();
        } catch (Exception e){
            e.toString();
        }
        return estado;
    }

    public Long actualizarValorPenalizacion(Integer id, String valor){
        Long estado = 0L;
        String where = "id = " + id.toString();

        try{
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("valor", valor);

            db.update(TABLE_COND_ASIGNATURAS, values, where, null);
            estado = 1L;
            db.close();
        } catch (Exception e){
            e.toString();
        }
        return estado;
    }

    public Long borrarCondAsignatura(Integer id){
        Long estado = 0L;
        String whereClause = "_id=?";
        String[] whereArgs = new String[] {id.toString()};

        try {
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete(TABLE_COND_ASIGNATURAS, whereClause, whereArgs);

            estado = 1L;
            db.close();
        } catch (Exception e){
            e.toString();
        }

        return estado;
    }

    public Long borrarCondAsignaturaPorIdAsignatura(Integer id){
        Long estado = 0L;
        String whereClause = "id_asignaturas=?";
        String[] whereArgs = new String[] {id.toString()};

        try {
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete(TABLE_COND_ASIGNATURAS, whereClause, whereArgs);

            estado = 1L;
            db.close();
        } catch (Exception e){
            e.toString();
        }

        return estado;
    }
}
