package com.example.myversity.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.example.myversity.entidades.Asignaturas;
import com.example.myversity.entidades.CondAsignatura;
import com.example.myversity.entidades.ConfiguracionInicial;
import com.example.myversity.entidades.Evaluaciones;
import com.example.myversity.entidades.Notas;
import com.example.myversity.entidades.TipoPromedio;

import java.util.ArrayList;
import java.util.List;

public class DbEvaluaciones extends DbHelper{
    Context context;

    public DbEvaluaciones(@Nullable Context context) {
        super(context);
        // Asignamos el contexto del constructor para usarlo mas adelante
        this.context = context;
    }

    // INSERTAR EVALUACIONES NO CONTIENE NI NOTA_EVALUACION, NI CONDICION, NI NOTA_COND
    // EL PESO SE DEBE AGREGAR, AUNQUE SEA NULO (DEPENDE DE SI LA ASIGNATURA PADRE USA PESO)
    public long insertarEvaluaciones(Integer id_asignaturas, Integer id_tipoPromedio, String tipo, Integer cantidad, String peso){
        long id = 0;

        try{
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("id_asignaturas", id_asignaturas);
            values.put("id_tipoPromedio", id_tipoPromedio);
            values.put("tipo", tipo);
            values.put("cantidad", cantidad);
            if(peso != null){
                values.put("peso", peso);
            }

            id = db.insert(TABLE_EVALUACIONES, null, values);
            db.close();
        } catch (Exception e){
            e.toString();
        }

        return id;
    }

    public long insertarEvaluacionesFull(Integer id_asignaturas, Integer id_tipoPromedio, String tipo, Integer cantidad, Boolean cond, String nota_cond, String peso){
        long id = 0;

        try{
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("id_asignaturas", id_asignaturas);
            values.put("id_tipoPromedio", id_tipoPromedio);
            values.put("tipo", tipo);
            values.put("cantidad", cantidad);
            values.put("cond", cond);
            if(cond == true){
                values.put("nota_cond", nota_cond);
            }
            if(peso != null){
                values.put("peso", peso);
            }

            id = db.insert(TABLE_EVALUACIONES, null, values);
            db.close();
        } catch (Exception e){
            e.toString();
        }

        return id;
    }

    public ArrayList<Evaluaciones> buscarEvaluaciones(){
        ArrayList<Evaluaciones> evaluaciones = new ArrayList<>();
        Evaluaciones eval = null;
        Cursor cursor = null;
        List<Notas> listaNotas = new ArrayList<>();
        TipoPromedio tipoPromedio = null;
        try {
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            cursor = db.rawQuery("SELECT * FROM " + TABLE_EVALUACIONES, null);
            if (cursor.moveToFirst()) {
                do {
                    eval = new Evaluaciones();
                    eval.setId(cursor.getInt(0));
                    eval.setId_asignaturas(cursor.getInt(1));
                    eval.setId_tipoPromedio(cursor.getInt(2));
                    eval.setTipo(cursor.getString(3));
                    eval.setCantidad(cursor.getInt(4));
                    eval.setNota_evaluacion(cursor.getString(5));
                    eval.setCond(cursor.getInt(6) != 0);
                    eval.setNota_cond(cursor.getString(7));
                    eval.setPeso(cursor.getString(8));

                    DbTipoPromedio dbTipoPromedio = new DbTipoPromedio(context);
                    tipoPromedio = dbTipoPromedio.buscarTipoPromedio(eval.getId_tipoPromedio());
                    dbTipoPromedio.close();
                    eval.setTp(tipoPromedio);

                    DbNotas dbNotas = new DbNotas(context);
                    listaNotas = dbNotas.buscarNotasPorIdEvaluacion(eval.getId());
                    dbNotas.close();
                    eval.setNotas(listaNotas);

                    evaluaciones.add(eval);

                } while (cursor.moveToNext()) ;
            }

            cursor.close();
            db.close();

        } catch (Exception e){
            e.toString();
        }
        return evaluaciones;
    }

    public ArrayList<Evaluaciones> buscarEvaluacionesPorIdAsignatura(Integer idAsignatura){
        ArrayList<Evaluaciones> evaluaciones = new ArrayList<>();
        Evaluaciones eval = null;
        Cursor cursor = null;
        List<Notas> listaNotas = new ArrayList<>();
        TipoPromedio tipoPromedio = null;
        try {
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            cursor = db.rawQuery("SELECT * FROM " + TABLE_EVALUACIONES + " WHERE id_asignaturas=" + idAsignatura.toString(), null);
            if (cursor.moveToFirst()) {
                do {
                    eval = new Evaluaciones();
                    eval.setId(cursor.getInt(0));
                    eval.setId_asignaturas(cursor.getInt(1));
                    eval.setId_tipoPromedio(cursor.getInt(2));
                    eval.setTipo(cursor.getString(3));
                    eval.setCantidad(cursor.getInt(4));
                    eval.setNota_evaluacion(cursor.getString(5));
                    eval.setCond(cursor.getInt(6) != 0);
                    eval.setNota_cond(cursor.getString(7));
                    eval.setPeso(cursor.getString(8));

                    DbTipoPromedio dbTipoPromedio = new DbTipoPromedio(context);
                    tipoPromedio = dbTipoPromedio.buscarTipoPromedio(eval.getId_tipoPromedio());
                    dbTipoPromedio.close();
                    eval.setTp(tipoPromedio);

                    DbNotas dbNotas = new DbNotas(context);
                    listaNotas = dbNotas.buscarNotasPorIdEvaluacion(eval.getId());
                    dbNotas.close();
                    eval.setNotas(listaNotas);

                    evaluaciones.add(eval);

                } while (cursor.moveToNext()) ;
            }

            cursor.close();
            db.close();

        } catch (Exception e){
            e.toString();
        }
        return evaluaciones;
    }

    public Evaluaciones buscarEvaluacionPorId(Integer id){
        Evaluaciones eval = null;
        Cursor cursor = null;
        List<Notas> listaNotas = new ArrayList<>();
        TipoPromedio tipoPromedio = null;
        try {
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            cursor = db.rawQuery("SELECT * FROM " + TABLE_EVALUACIONES + " WHERE id=" + id.toString(), null);
            if (cursor.moveToFirst()) {
                eval = new Evaluaciones();
                eval.setId(cursor.getInt(0));
                eval.setId_asignaturas(cursor.getInt(1));
                eval.setId_tipoPromedio(cursor.getInt(2));
                eval.setTipo(cursor.getString(3));
                eval.setCantidad(cursor.getInt(4));
                eval.setNota_evaluacion(cursor.getString(5));
                eval.setCond(cursor.getInt(6) != 0);
                eval.setNota_cond(cursor.getString(7));
                eval.setPeso(cursor.getString(8));

                DbTipoPromedio dbTipoPromedio = new DbTipoPromedio(context);
                tipoPromedio = dbTipoPromedio.buscarTipoPromedio(eval.getId_tipoPromedio());
                dbTipoPromedio.close();
                eval.setTp(tipoPromedio);

                DbNotas dbNotas = new DbNotas(context);
                listaNotas = dbNotas.buscarNotasPorIdEvaluacion(eval.getId());
                dbNotas.close();
                eval.setNotas(listaNotas);
            }

            cursor.close();
            db.close();

        } catch (Exception e){
            e.toString();
        }
        return eval;
    }

    public Long actualizarNotaEvaluacion(Integer id, String nota){
        Long estado = 0L;
        String where = "id = " + id.toString();

        try{
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("nota_evaluacion", nota);

            db.update(TABLE_EVALUACIONES, values, where, null);
            estado = 1L;
            db.close();
        } catch (Exception e){
            e.toString();
        }
        return estado;
    }

    public Long actualizarIdTipoPromedioEvaluacion(Integer id, Integer idTipoPromedio){
        Long estado = 0L;
        String where = "id = " + id.toString();

        try{
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("id_tipoPromedio", idTipoPromedio);

            db.update(TABLE_EVALUACIONES, values, where, null);
            estado = 1L;
            db.close();
        } catch (Exception e){
            e.toString();
        }
        return estado;
    }

    public Long actualizarTipoEvaluacion(Integer id, String tipo){
        Long estado = 0L;
        String where = "id = " + id.toString();

        try{
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("tipo", tipo);

            db.update(TABLE_EVALUACIONES, values, where, null);
            estado = 1L;
            db.close();
        } catch (Exception e){
            e.toString();
        }
        return estado;
    }

    public Long actualizarCantidadNotas(Integer id, Integer cantidad){
        Long estado = 0L;
        String where = "id = " + id.toString();

        try{
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("cantidad", cantidad);

            db.update(TABLE_EVALUACIONES, values, where, null);
            estado = 1L;
            db.close();
        } catch (Exception e){
            e.toString();
        }
        return estado;
    }

    public Long actualizarCondicion(Integer id, Boolean cond){
        Long estado = 0L;
        String where = "id = " + id.toString();

        try{
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("cond", cond);

            db.update(TABLE_EVALUACIONES, values, where, null);
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

            db.update(TABLE_EVALUACIONES, values, where, null);
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

            db.update(TABLE_EVALUACIONES, values, where, null);
            estado = 1L;
            db.close();
        } catch (Exception e){
            e.toString();
        }
        return estado;
    }

    public Long borrarEvaluacion(Integer id){
        Long estado = 0L;
        String whereClause = "_id=?";
        String[] whereArgs = new String[] {id.toString()};

        try{
            // BORRAR NOTAS ASOCIADAS A ESTA EVALUACION
            DbNotas dbNotas = new DbNotas(context);
            dbNotas.borrarNotasPorIdEvaluaciones(id);
            dbNotas.close();

            // AHORA SE BORRA LA EVALUACION
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete(TABLE_EVALUACIONES, whereClause, whereArgs);

            estado = 1L;
            db.close();
        } catch (Exception e){
            e.toString();
        }

        return estado;
    }

    public Long borrarEvaluacionesPorIdAsignatura(Integer idAsignatura){
        Long estado = 0L;
        String whereClause = "id_asignaturas=?";
        String[] whereArgs = new String[] {idAsignatura.toString()};

        try{
            // PRIMERO BUSCAMOS LAS EVALUACIONES QUE SE BORRARAN
            // ASI TENEMOS HISTORIAL DE QUE NOTAS ESTAN ASOCIADAS, Y POR TANTO, QUE NOTAS BORRAR
            // SOLO BORRANDO LAS EVALUACIONES, NO SE BORRAN LAS ASIGNATURAS
            DbNotas dbNotas = new DbNotas(context);
            List<Evaluaciones> eval = buscarEvaluacionesPorIdAsignatura(idAsignatura);
            for(Evaluaciones ev : eval){
                dbNotas.borrarNotasPorIdEvaluaciones(ev.getId());
            }
            dbNotas.close();

            // AHORA BORRAMOS LAS EVALUACIONES
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete(TABLE_EVALUACIONES, whereClause, whereArgs);

            estado = 1L;
            db.close();
        } catch (Exception e){
            e.toString();
        }

        return estado;
    }
}
