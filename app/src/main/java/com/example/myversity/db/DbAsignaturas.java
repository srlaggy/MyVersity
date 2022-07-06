package com.example.myversity.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.myversity.entidades.Asignaturas;
import com.example.myversity.entidades.CondAsignatura;
import com.example.myversity.entidades.ConfiguracionInicial;
import com.example.myversity.entidades.Evaluaciones;
import com.example.myversity.entidades.TipoPromedio;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

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
            db.close();
        } catch (Exception e){
            e.toString();
        }

        return id;
    }

    public ArrayList<Asignaturas> buscarAsignaturas(){
        ArrayList<Asignaturas> asignaturas = new ArrayList<>();
        Asignaturas asig = null;
        Cursor cursor = null;
        ConfiguracionInicial config = null;
        TipoPromedio tipoPromedio = null;
        List<CondAsignatura> listCondAsig = new ArrayList<>();
        List<Evaluaciones> listaEvaluaciones = new ArrayList<>();
        try {
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            cursor = db.rawQuery("SELECT * FROM " + TABLE_ASIGNATURAS, null);
            if (cursor.moveToFirst()) {
                do {
                    asig = new Asignaturas();
                    asig.setId(cursor.getInt(0));
                    asig.setId_configInicial(cursor.getInt(1));
                    asig.setId_tipoPromedio(cursor.getInt(2));
                    asig.setNombre(cursor.getString(3));
                    asig.setNota_final(cursor.getString(4));

                    DbConfigInicial dbConfigInicial = new DbConfigInicial(context);
                    config = dbConfigInicial.buscarRegistro(asig.getId_configInicial());
                    dbConfigInicial.close();
                    asig.setConfig(config);

                    DbTipoPromedio dbTipoPromedio = new DbTipoPromedio(context);
                    tipoPromedio = dbTipoPromedio.buscarTipoPromedio(asig.getId_tipoPromedio());
                    dbTipoPromedio.close();
                    asig.setTp(tipoPromedio);

                    DbCondAsignatura dbCondAsignatura = new DbCondAsignatura(context);
                    listCondAsig = dbCondAsignatura.buscarCondAsignaturasPorIdAsignatura(asig.getId());
                    dbCondAsignatura.close();
                    asig.setCa(listCondAsig);

                    DbEvaluaciones dbEvaluaciones = new DbEvaluaciones(context);
                    listaEvaluaciones = dbEvaluaciones.buscarEvaluacionesPorIdAsignatura(asig.getId());
                    dbEvaluaciones.close();
                    asig.setEv(listaEvaluaciones);

                    asignaturas.add(asig);

                } while (cursor.moveToNext()) ;
            }

            cursor.close();
            db.close();

        } catch (Exception e){
            e.toString();
        }
        return asignaturas;
    }

    public Asignaturas buscarAsignaturaPorId(Integer id){
        Asignaturas asig = null;
        Cursor cursor = null;
        ConfiguracionInicial config = null;
        TipoPromedio tipoPromedio = null;
        List<CondAsignatura> listCondAsig = new ArrayList<>();
        List<Evaluaciones> listaEvaluaciones = new ArrayList<>();
        try {
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            cursor = db.rawQuery("SELECT * FROM " + TABLE_ASIGNATURAS + " WHERE id=" + id.toString() + " LIMIT 1", null);
            if (cursor.moveToFirst()) {
                asig = new Asignaturas();
                asig.setId(cursor.getInt(0));
                asig.setId_configInicial(cursor.getInt(1));
                asig.setId_tipoPromedio(cursor.getInt(2));
                asig.setNombre(cursor.getString(3));
                asig.setNota_final(cursor.getString(4));

                DbConfigInicial dbConfigInicial = new DbConfigInicial(context);
                config = dbConfigInicial.buscarRegistro(asig.getId_configInicial());
                dbConfigInicial.close();
                asig.setConfig(config);

                DbTipoPromedio dbTipoPromedio = new DbTipoPromedio(context);
                tipoPromedio = dbTipoPromedio.buscarTipoPromedio(asig.getId_tipoPromedio());
                dbTipoPromedio.close();
                asig.setTp(tipoPromedio);

                DbCondAsignatura dbCondAsignatura = new DbCondAsignatura(context);
                listCondAsig = dbCondAsignatura.buscarCondAsignaturasPorIdAsignatura(asig.getId());
                dbCondAsignatura.close();
                asig.setCa(listCondAsig);

                DbEvaluaciones dbEvaluaciones = new DbEvaluaciones(context);
                listaEvaluaciones = dbEvaluaciones.buscarEvaluacionesPorIdAsignatura(asig.getId());
                dbEvaluaciones.close();
                asig.setEv(listaEvaluaciones);
            }

            cursor.close();
            db.close();

        } catch (Exception e){
            e.toString();
        }
        return asig;
    }

    public Long actualizarNotaAsignatura(Integer id, String nota){
        Long estado = 0L;
        String where = "id = " + id.toString();

        try{
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("nota_final", nota);

            db.update(TABLE_ASIGNATURAS, values, where, null);
            estado = 1L;
            db.close();
        } catch (Exception e){
            e.toString();
        }
        return estado;
    }

    public Long actualizarNombre(Integer id, String nombre){
        Long estado = 0L;
        String where = "id = " + id.toString();

        try{
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("nombre", nombre);

            db.update(TABLE_ASIGNATURAS, values, where, null);
            estado = 1L;
            db.close();
        } catch (Exception e){
            e.toString();
        }
        return estado;
    }

    public Long actualizarIdTipoPromedioAsignatura(Integer id, Integer idTipoPromedio){
        Long estado = 0L;
        String where = "id = " + id.toString();

        try{
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("id_tipoPromedio", idTipoPromedio);

            db.update(TABLE_ASIGNATURAS, values, where, null);
            estado = 1L;
            db.close();
        } catch (Exception e){
            e.toString();
        }
        return estado;
    }

    public Long actualizarConfigInicial(Integer idAsignatura, Integer idConfigInicial_FK, ConfiguracionInicial ci){
        Long idRetorno = 0L;
        String where = "id = " + idAsignatura.toString();

        try{
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            DbConfigInicial dbConfigInicial = new DbConfigInicial(context);

            // ESTADO DE CONFIGURACION POR DEFECTO
            if(idConfigInicial_FK == 1){
                // SE DEBE CREAR UNA NUEVA CONFIGURACION DISTINTA A LA POR DEFECTO
                idRetorno = dbConfigInicial.insertarConfigInicial(ci.getMin(), ci.getMax(), ci.getNotaAprobacion(), ci.getDecimal(), ci.getOrientacionAsc());

                // Y SETEAR LA REFERENCIA NUEVA
                ContentValues values = new ContentValues();
                values.put("id_configInicial", idRetorno);
                db.update(TABLE_ASIGNATURAS, values, where, null);
            } else if (idConfigInicial_FK > 1){
                // SE MANTIENE LA MISMA REFERENCIA, POR LO QUE SOLO SE ACTUALIZA EL CORRESPONDIENTE
                idRetorno = dbConfigInicial.actualizarRegistro(idConfigInicial_FK, ci);
            }

            dbConfigInicial.close();
            db.close();

        } catch (Exception e){
            e.toString();
        }
        return idRetorno;
    }

    public Long borrarAsignatura(Integer idAsignatura, Integer idConfigInicial){
        Long estado = 0L;
        String whereClause = "id=?";
        String[] whereArgs = new String[] {idAsignatura.toString()};

        try{
            // SI TIENE SU PROPIA CONFIGURACION INICIAL, POR LO TANTO HAY QUE BORRARLA
            if(idConfigInicial > 1){
                DbConfigInicial dbConfigInicial = new DbConfigInicial(context);
                dbConfigInicial.borrarRegistro(idConfigInicial);
                dbConfigInicial.close();
            }

            // AHORA SE BORRAN LAS CONDICIONES DE ASIGNATURA
            DbCondAsignatura dbCondAsignatura = new DbCondAsignatura(context);
            dbCondAsignatura.borrarCondAsignaturaPorIdAsignatura(idAsignatura);
            dbCondAsignatura.close();

            // AHORA SE BORRAN LAS EVALUACIONES (POR DENTRO BORRARA LAS NOTAS)
            DbEvaluaciones dbEvaluaciones = new DbEvaluaciones(context);
            dbEvaluaciones.borrarEvaluacionesPorIdAsignatura(idAsignatura);
            dbEvaluaciones.close();

            // AHORA SE BORRA LA ASIGNATURA
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Log.e("test", String.valueOf(idConfigInicial));
            db.delete(TABLE_ASIGNATURAS, whereClause, whereArgs);

            estado = 1L;
            db.close();
        } catch (Exception e){
            e.toString();
        }

        return estado;
    }
}
