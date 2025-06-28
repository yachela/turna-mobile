package com.example.turna.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.turna.model.Turno;

import java.util.List;

@Dao
public interface TurnoDao {

    @Query("SELECT * FROM turnos")
    List<Turno> getAll();

    @Insert
    void insert(Turno turno);


    @Delete
    void delete(Turno turno);

    @Update
    void actualizar(Turno turno);

    @Query("SELECT * FROM turnos WHERE id = :turnoId LIMIT 1")
    Turno buscarPorId(int turnoId);
}