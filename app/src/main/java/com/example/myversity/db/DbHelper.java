package com.example.myversity.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.myversity.entidades.Asignaturas;
import com.example.myversity.entidades.CondAsignatura;
import com.example.myversity.entidades.Evaluaciones;
import com.example.myversity.entidades.Notas;
import com.example.myversity.entidades.TipoPromedio;
import com.example.myversity.entidades.TiposPenalizacion;
import com.example.myversity.entidades.tiposPromedios.MediaAritmetica;
import com.example.myversity.entidades.tiposPromedios.MediaCuadratica;
import com.example.myversity.entidades.tiposPromedios.MediaGeometrica;
import com.example.myversity.entidades.tiposPromedios.MediaPonderada;
import com.example.myversity.entidades.tiposPromedios.MediaSoloSuma;

import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NOMBRE = "myversityDB";

    public static final String TABLE_CONFIG_INICIAL = "configInicial";
    public static final String TABLE_ASIGNATURAS = "asignaturas";
    public static final String TABLE_EVALUACIONES = "evaluaciones";
    public static final String TABLE_NOTAS = "notas";
    public static final String TABLE_COND_ASIGNATURAS = "condAsignaturas";
    public static final String TABLE_TIPOS_PENALIZACION = "tiposPenalizacion";
    public static final String TABLE_TIPO_PROMEDIO = "tipoPromedio";

    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NOMBRE, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // orientacion ascendente -> true: menor a mayor
        //                           false: mayor a menor
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_CONFIG_INICIAL + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nota_minima TEXT NOT NULL," +
                "nota_maxima TEXT NOT NULL," +
                "nota_aprobacion TEXT NOT NULL," +
                "decimal BOOLEAN NOT NULL," +
                "orientacion_asc BOOLEAN NOT NULL)"
        );

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_ASIGNATURAS + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "id_configInicial INTEGER NOT NULL," +
                "id_tipoPromedio INTEGER NOT NULL," +
                "nombre TEXT NOT NULL," +
                "nota_final TEXT," +
                "FOREIGN KEY(id_configInicial) REFERENCES " + TABLE_CONFIG_INICIAL + "(id)," +
                "FOREIGN KEY(id_tipoPromedio) REFERENCES " + TABLE_TIPO_PROMEDIO + "(id))"
        );

        // peso -> el porcentaje que podria valer del total, segun el tipo de promedio de la tabla de asignaturas
        // cond y nota_cond -> es el caso en el que existe una condicion al promedio de este tipo de evaluacion
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_EVALUACIONES + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "id_asignaturas INTEGER NOT NULL," +
                "id_tipoPromedio INTEGER NOT NULL," +
                "tipo TEXT NOT NULL," +
                "cantidad INTEGER NOT NULL," +
                "nota_evaluacion TEXT," +
                "cond BOOLEAN NOT NULL," +
                "nota_cond TEXT," +
                "peso TEXT," +
                "FOREIGN KEY(id_asignaturas) REFERENCES " + TABLE_ASIGNATURAS + "(id)," +
                "FOREIGN KEY(id_tipoPromedio) REFERENCES " + TABLE_TIPO_PROMEDIO + "(id))"
        );

        // peso -> el porcentaje que podria valer del total, segun el tipo de promedio de la tabla de evaluaciones
        // cond y nota_cond -> es el caso en el que existe una condicion a la nota
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NOTAS + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "id_evaluaciones INTEGER NOT NULL," +
                "nota TEXT," +
                "cond BOOLEAN NOT NULL," +
                "nota_cond TEXT," +
                "peso TEXT," +
                "FOREIGN KEY(id_evaluaciones) REFERENCES " + TABLE_EVALUACIONES + "(id))"
        );

        // condicion -> nombre de la condicion (podria ser asistencia o responder encuesta)
        // chequeado -> si se cumple o no (la condicion)
        // valor -> es el valor de penalizacion (positiva o negativa) que podria existir al cumplir la condicion
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_COND_ASIGNATURAS + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "id_asignaturas INTEGER NOT NULL," +
                "id_tiposPenalizacion INTEGER NOT NULL," +
                "condicion TEXT NOT NULL," +
                "chequeado BOOLEAN NOT NULL," +
                "valor TEXT," +
                "FOREIGN KEY(id_asignaturas) REFERENCES " + TABLE_ASIGNATURAS + "(id)," +
                "FOREIGN KEY(id_tiposPenalizacion) REFERENCES " + TABLE_TIPOS_PENALIZACION + "(id))"
        );

        // nombre -> es el tipo de penalizacion (reprobacion, % nota final, descuento a puntaje final)
        // cuando_penaliza -> false: penaliza cuando la condicion no se cumple (false = false) -> [ NO cumplir asistencia => reprobar ]
        //                 -> true: penaliza cuando la condicion se cumple (true = true)       -> [ SI respondes encuesta => puntaje extra ]
        // requiere_valor -> indica si se debe llenar el "valor" en la tabla de condAsignatura (por ejemplo 30%, +5 ptos) (en reprobar, no se requiere valor)
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_TIPOS_PENALIZACION + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT NOT NULL," +
                "cuando_penaliza BOOLEAN NOT NULL," +
                "requiere_valor BOOLEAN NOT NULL)"
        );

        // usa_peso -> indica si el promedio especifico requiere llenar el atributo "peso" en las tablas de Asignaturas y Evaluaciones
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_TIPO_PROMEDIO + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT NOT NULL," +
                "usa_peso BOOLEAN NOT NULL)"
        );

        //------------------------------------------------------------------------//
        // AHORA POBLAMOS TABLE_TIPOS_PENALIZACION y TABLE_TIPO_PROMEDIO
        List<TiposPenalizacion> tp = new ArrayList<>();
        tp.add(new TiposPenalizacion("Reprobación", false, false));
        tp.add(new TiposPenalizacion("Descuento porcentaje nota final", false, true));
        tp.add(new TiposPenalizacion("Descuento puntaje nota final", false, true));
        tp.add(new TiposPenalizacion("Adición puntaje nota final", true, true));

        ContentValues values;
        for(TiposPenalizacion t : tp){
            values = new ContentValues();
            values.put("nombre", t.getNombre());
            values.put("cuando_penaliza", t.getCuando_penaliza());
            values.put("requiere_valor", t.getRequiere_valor());
            sqLiteDatabase.insert(TABLE_TIPOS_PENALIZACION, null, values);
        }

        List<TipoPromedio> tpr = new ArrayList<>();
        tpr.add(new MediaAritmetica());
        tpr.add(new MediaPonderada());
        tpr.add(new MediaGeometrica());
        tpr.add(new MediaCuadratica());
        tpr.add(new MediaSoloSuma());
        for(TipoPromedio t : tpr){
            values = new ContentValues();
            values.put("nombre", t.getNombre());
            values.put("usa_peso", t.getUsa_peso());
            sqLiteDatabase.insert(TABLE_TIPO_PROMEDIO, null, values);
        }

        //------------------------------------------------------------------------//
        // DATOS DUMMY PARA TESTEO DE EXPORT E IMPORT
        /*values = new ContentValues();
        values.put("nota_minima", "0");
        values.put("nota_maxima", "100");
        values.put("nota_aprobacion", "55");
        values.put("decimal", false);
        values.put("orientacion_asc", true);
        sqLiteDatabase.insert(TABLE_CONFIG_INICIAL, null, values);

        List<Asignaturas> asignaturas = new ArrayList<>();
        asignaturas.add(new Asignaturas(1, 5, "Aplicaciones Moviles"));
        asignaturas.add(new Asignaturas(1, 2, "Bases de datos avanzadas"));
        for(Asignaturas asig : asignaturas){
            values = new ContentValues();
            values.put("id_configInicial", asig.getId_configInicial());
            values.put("id_tipoPromedio", asig.getId_tipoPromedio());
            values.put("nombre", asig.getNombre());
            sqLiteDatabase.insert(TABLE_ASIGNATURAS, null, values);
        }

        List<CondAsignatura> ca = new ArrayList<>();
        ca.add(new CondAsignatura(1, 1, "Asistencia", false, null));
        for(CondAsignatura c : ca){
            values = new ContentValues();
            values.put("id_asignaturas", c.getId_asignaturas());
            values.put("id_tiposPenalizacion", c.getId_tiposPenalizacion());
            values.put("condicion", c.getCondicion());
            values.put("chequeado", c.getChequeado());
            values.put("valor", c.getValor());
            sqLiteDatabase.insert(TABLE_COND_ASIGNATURAS, null, values);
        }

        List<Evaluaciones> ev = new ArrayList<>();
        ev.add(new Evaluaciones(1, 2, "Presentacion", 3, false, null, null));
        ev.add(new Evaluaciones(2, 1, "Laboratorio", 3, false, null, "0.45"));
        ev.add(new Evaluaciones(2, 1, "Lectura", 3, false, null, "0.3"));
        ev.add(new Evaluaciones(2, 1, "Actividad", 5, false, null, "0.25"));
        for(Evaluaciones eval : ev){
            values = new ContentValues();
            values.put("id_asignaturas", eval.getId_asignaturas());
            values.put("id_tipoPromedio", eval.getId_tipoPromedio());
            values.put("tipo", eval.getTipo());
            values.put("cantidad", eval.getCantidad());
            values.put("cond", eval.getCond());
            values.put("nota_cond", eval.getNota_cond());
//            if(eval.getId_asignaturas() == 2){
                values.put("peso", eval.getPeso());
//            }
            sqLiteDatabase.insert(TABLE_EVALUACIONES, null, values);
        }

        List<Notas> nota = new ArrayList<>();
        nota.add(new Notas(1, false, null, "0.2"));
        nota.add(new Notas(1, false, null, "0.3"));
        nota.add(new Notas(1, false, null, "0.5"));
        nota.add(new Notas(2, false, null, null));
        nota.add(new Notas(2, false, null, null));
        nota.add(new Notas(2, false, null, null));
        nota.add(new Notas(3, false, null, null));
        nota.add(new Notas(3, false, null, null));
        nota.add(new Notas(3, false, null, null));
        nota.add(new Notas(4, false, null, null));
        nota.add(new Notas(4, false, null, null));
        nota.add(new Notas(4, false, null, null));
        nota.add(new Notas(4, false, null, null));
        nota.add(new Notas(4, false, null, null));
        for(Notas n : nota){
            values = new ContentValues();
            values.put("id_evaluaciones", n.getId_evaluaciones());
            values.put("cond", n.getCond());
            values.put("nota_cond", n.getNota_cond());
//            if(n.getId_evaluaciones() == 1){
                values.put("peso", n.getPeso());
//            }
            sqLiteDatabase.insert(TABLE_NOTAS, null, values);
        }*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE " + TABLE_CONFIG_INICIAL);
        sqLiteDatabase.execSQL("DROP TABLE " + TABLE_ASIGNATURAS);
        sqLiteDatabase.execSQL("DROP TABLE " + TABLE_EVALUACIONES);
        sqLiteDatabase.execSQL("DROP TABLE " + TABLE_NOTAS);
        sqLiteDatabase.execSQL("DROP TABLE " + TABLE_COND_ASIGNATURAS);
        sqLiteDatabase.execSQL("DROP TABLE " + TABLE_TIPOS_PENALIZACION);
        sqLiteDatabase.execSQL("DROP TABLE " + TABLE_TIPO_PROMEDIO);
        onCreate(sqLiteDatabase);
    }
}
