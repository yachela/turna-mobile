package com.example.turna;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnNuevo = findViewById(R.id.btnNuevoTurno);
        Button btnLista = findViewById(R.id.btnVerTurnos);

        btnNuevo.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ReservarTurnoActivity.class);
            startActivity(intent);
        });

        btnLista.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ListaTurnosActivity.class);
            startActivity(intent);
        });
    }
}