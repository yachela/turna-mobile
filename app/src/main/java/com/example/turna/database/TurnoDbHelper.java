package com.example.turna.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TurnoDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "turna.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_TURNOS = "turnos";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_PROFESIONAL = "profesional";
    public static final String COLUMN_FECHA = "fecha";
    public static final String COLUMN_HORA = "hora";

    public static final String TABLE_USUARIOS = "usuarios";
    public static final String COLUMN_USUARIO_ID = "id";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_TURNOS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_PROFESIONAL + " TEXT, " +
                    COLUMN_FECHA + " TEXT, " +
                    COLUMN_HORA + " TEXT);";

    private static final String SQL_CREATE_USUARIOS =
            "CREATE TABLE " + TABLE_USUARIOS + " (" +
                    COLUMN_USUARIO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_EMAIL + " TEXT UNIQUE NOT NULL, " +
                    COLUMN_PASSWORD + " TEXT NOT NULL)";
    public TurnoDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        db.execSQL(SQL_CREATE_USUARIOS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TURNOS);
        onCreate(db);
    }
}