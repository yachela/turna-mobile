package com.example.turna.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import com.example.turna.model.Turno;
import com.example.turna.database.Usuario;

@Database(entities = {Turno.class, Usuario.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract TurnoDao turnoDao();

    public abstract UsuarioDao usuarioDao();

    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "turno_database")
                            .fallbackToDestructiveMigration() // Para desarrollo
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}