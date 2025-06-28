package com.example.turna;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MenuPrincipalActivity extends AppCompatActivity {

    private Button btnReservar, btnVerTurnos, btnEditarPerfil, btnCerrarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        btnReservar = findViewById(R.id.btnReservar);
        btnVerTurnos = findViewById(R.id.btnVerTurnos);
        btnEditarPerfil = findViewById(R.id.btnEditarPerfil);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        btnReservar.setOnClickListener(v -> {
            Intent intent = new Intent(this, ReservarTurnoActivity.class);
            startActivity(intent);
        });

        btnVerTurnos.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListaTurnosActivity.class);
            startActivity(intent);
        });

       /* btnEditarPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditarPerfilActivity.class);
            startActivity(intent);
        })
        */

        btnCerrarSesion.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}