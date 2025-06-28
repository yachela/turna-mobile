package com.example.turna.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

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
                            .fallbackToDestructiveMigration().addCallback(new RoomDatabase.Callback() {
                                @Override
                                public void onCreate(@androidx.annotation.NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    new Thread(() -> {
                                        AppDatabase database = getInstance(context);
                                        TurnoDao turnoDao = database.turnoDao();
                                        UsuarioDao usuarioDao = database.usuarioDao();
                                        turnoDao.insert(new Turno("Turno 1", "Consulta", "2025-07-01", "09:00", "Dr. Prueba"));
                                        turnoDao.insert(new Turno("Turno 2", "Control", "2025-07-02", "11:00", "Lic. Ejemplo"));
                                        usuarioDao.insert(new Usuario("prueba@turna.com", "123456"));

                                    }).start();
                                }
                            })
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}