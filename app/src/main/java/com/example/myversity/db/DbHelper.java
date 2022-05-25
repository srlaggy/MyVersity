package com.example.myversity.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.myversity.entidades.TipoPromedio;
import com.example.myversity.entidades.TiposPenalizacion;

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
                "peso REAL," +
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
                "peso REAL," +
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
        tpr.add(new TipoPromedio("Media aritmética", false));
        tpr.add(new TipoPromedio("Media ponderada", true));
        tpr.add(new TipoPromedio("Media geométrica", false));
        tpr.add(new TipoPromedio("Media Cuadrática", false));
        tpr.add(new TipoPromedio("Suma", false));
        for(TipoPromedio t : tpr){
            values = new ContentValues();
            values.put("nombre", t.getNombre());
            values.put("usa_peso", t.getUsa_peso());
            sqLiteDatabase.insert(TABLE_TIPO_PROMEDIO, null, values);
        }

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
