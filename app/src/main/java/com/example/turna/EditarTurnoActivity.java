package com.example.turna;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import com.example.turna.database.AppDatabase;
import com.example.turna.model.Turno;
import com.example.turna.database.TurnoDao;
import java.util.concurrent.Executors;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.ArrayAdapter;
import android.widget.Toast;

public class EditarTurnoActivity extends AppCompatActivity {

    private Spinner spinnerProfesional;
    private EditText editTextFecha, editTextHora;
    private Button btnGuardar;
    private int turnoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_turno);

        Intent intent = getIntent();
        turnoId = intent.getIntExtra("id", -1);
        String profesional = intent.getStringExtra("profesional");
        String fecha = intent.getStringExtra("fecha");
        String hora = intent.getStringExtra("hora");

        spinnerProfesional = findViewById(R.id.spinnerProfesional);
        editTextFecha = findViewById(R.id.editTextFecha);
        editTextHora = findViewById(R.id.editTextHora);
        btnGuardar = findViewById(R.id.btnGuardarTurno);

        String[] profesionales = {"Dra. López", "Lic. García", "Dr. Pérez"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, profesionales);
        spinnerProfesional.setAdapter(adapter);

        editTextFecha.setText(fecha != null ? fecha : "");
        editTextHora.setText(hora != null ? hora : "");

        if (profesional != null) {
            for (int i = 0; i < profesionales.length; i++) {
                if (profesionales[i].equals(profesional)) {
                    spinnerProfesional.setSelection(i);
                    break;
                }
            }
        }

        btnGuardar.setOnClickListener(view -> guardarCambios());
    }

    private void guardarCambios() {
        String nuevoProfesional = spinnerProfesional.getSelectedItem().toString();
        String nuevaFecha = editTextFecha.getText().toString().trim();
        String nuevaHora = editTextHora.getText().toString().trim();

        // Validaciones
        if (nuevaFecha.isEmpty() || nuevaHora.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (turnoId == -1) {
            Toast.makeText(this, "Error: ID de turno inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        AppDatabase db = AppDatabase.getInstance(this);
        TurnoDao turnoDao = db.turnoDao();

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Turno turno = turnoDao.buscarPorId(turnoId);
                if (turno != null) {
                    turno.setNombreProfesional(nuevoProfesional);
                    turno.setFecha(nuevaFecha);
                    turno.setHora(nuevaHora);

                    turnoDao.actualizar(turno);

                    runOnUiThread(() -> {
                        Toast.makeText(this, "Turno actualizado exitosamente", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                } else {
                    runOnUiThread(() ->
                            Toast.makeText(this, "Error: No se encontró el turno", Toast.LENGTH_SHORT).show()
                    );
                }
            } catch (Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(this, "Error al actualizar: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
            }
        });
    }
}