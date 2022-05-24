package com.example.myversity.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NOMBRE = "myversityDB";
    public static final String TABLE_CONFIG_INICIAL = "configInicial";

    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NOMBRE, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_CONFIG_INICIAL + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nota_minima TEXT NOT NULL," +
                "nota_maxima TEXT NOT NULL," +
                "nota_aprobacion TEXT NOT NULL," +
                "decimal BOOLEAN NOT NULL," +
                "orientacion_asc BOOLEAN NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE " + TABLE_CONFIG_INICIAL);
        onCreate(sqLiteDatabase);
    }
}
