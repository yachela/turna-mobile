package com.example.turna.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.turna.database.Usuario;

@Dao
public interface UsuarioDao {
    @Insert
    void insert(Usuario usuario);

    @Query("SELECT * FROM usuarios WHERE email = :email AND password = :password")
    Usuario login(String email, String password);

    @Query("SELECT * FROM usuarios WHERE email = :email")
    Usuario findByEmail(String email);}