package com.example.turna;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.turna.database.AppDatabase;
import com.example.turna.model.Turno;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ListaTurnosActivity extends AppCompatActivity {

    private ListView listViewTurnos;
    private AppDatabase db;
    private ArrayList<Turno> turnos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_turnos);
        Button btnVolverMenu = findViewById(R.id.btnVolverMenu);
        btnVolverMenu.setOnClickListener(v -> {
            Intent intent = new Intent(ListaTurnosActivity.this, MenuPrincipalActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        if (!isUserLoggedIn()) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return;
        }

        db = AppDatabase.getInstance(this);
        listViewTurnos = findViewById(R.id.listViewTurnos);


        /*
        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            getSharedPreferences("TurnaPrefs", MODE_PRIVATE).edit().clear().apply();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
        */

        cargarTurnos();

        listViewTurnos.setOnItemClickListener((parent, view, position, id) -> {
            Turno turnoSeleccionado = turnos.get(position);

            Intent intent = new Intent(ListaTurnosActivity.this, EditarTurnoActivity.class);
            intent.putExtra("id", turnoSeleccionado.getId());
            intent.putExtra("profesional", turnoSeleccionado.getNombreProfesional());
            intent.putExtra("fecha", turnoSeleccionado.getFecha());
            intent.putExtra("hora", turnoSeleccionado.getHora());
            intent.putExtra("nombre", turnoSeleccionado.getNombre());
            intent.putExtra("motivo", turnoSeleccionado.getMotivo());
            startActivity(intent);
        });

        listViewTurnos.setOnItemLongClickListener((parent, view, position, id) -> {
            Turno turnoSeleccionado = turnos.get(position);

            new AlertDialog.Builder(ListaTurnosActivity.this)
                    .setTitle("Eliminar turno")
                    .setMessage("¿Querés eliminar este turno?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        db.turnoDao().delete(turnoSeleccionado);
                        cargarTurnos();
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();

            return true;
        });
    }

    private boolean isUserLoggedIn() {
        return getSharedPreferences("TurnaPrefs", MODE_PRIVATE)
                .getBoolean("logueado", false);
    }

    private void cargarTurnos() {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            ArrayList<Turno> turnosDb = new ArrayList<>(db.turnoDao().getAll());
            ArrayList<String> lista = new ArrayList<>();
            for (Turno turno : turnosDb) {
                lista.add(turno.getNombreProfesional() + " - " + turno.getFecha() + " - " + turno.getHora());
            }

            runOnUiThread(() -> {
                turnos.clear();
                turnos.addAll(turnosDb);

                ArrayAdapter<String> adapter;
                if (lista.isEmpty()) {
                    ArrayList<String> vacio = new ArrayList<>();
                    vacio.add("No hay turnos");
                    adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, vacio);
                    listViewTurnos.setAdapter(adapter);
                    listViewTurnos.setOnItemClickListener(null); // Desactiva eventos
                    listViewTurnos.setOnItemLongClickListener(null);
                } else {
                    adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lista);
                    listViewTurnos.setAdapter(adapter);
                }
            });
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarTurnos();
    }
}