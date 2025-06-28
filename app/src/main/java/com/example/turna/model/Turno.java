package com.example.turna.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "turnos")
public class Turno {
    @PrimaryKey(autoGenerate = true)
    private int id;

    // Otros campos
    private String nombre;
    private String motivo;
    private String fecha;
    private String hora;
    private String nombreProfesional;

    public Turno() {}

    public Turno(String nombre, String motivo, String fecha, String hora) {
        this.nombre = nombre;
        this.motivo = motivo;
        this.fecha = fecha;
        this.hora = hora;
        this.nombreProfesional = "";
    }

    public Turno(String nombre, String motivo, String fecha, String hora, String nombreProfesional) {
        this.nombre = nombre;
        this.motivo = motivo;
        this.fecha = fecha;
        this.hora = hora;
        this.nombreProfesional = nombreProfesional;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getNombreProfesional() {
        return nombreProfesional;
    }

    public void setNombreProfesional(String nombreProfesional) {
        this.nombreProfesional = nombreProfesional;
    }
}