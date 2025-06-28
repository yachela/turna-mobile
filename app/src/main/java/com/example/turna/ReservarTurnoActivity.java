package com.example.turna;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.turna.database.AppDatabase;
import com.example.turna.database.TurnoDao;
import com.example.turna.model.Turno;

import java.util.Calendar;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ReservarTurnoActivity extends AppCompatActivity {

    private Spinner spinnerProfesional;
    private EditText editTextFecha, editTextHora;
    private Button btnGuardar;
    private int turnoId;
    private Button btnEliminar;
    private TurnoDao turnoDao;
    private Executor executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservar_turno);

        spinnerProfesional = findViewById(R.id.spinnerProfesional);
        btnGuardar = findViewById(R.id.btnGuardarTurno);
        Button btnFecha = findViewById(R.id.btnFecha);
        Button btnHora = findViewById(R.id.btnHora);
        editTextFecha = findViewById(R.id.editTextFecha);
        editTextHora = findViewById(R.id.editTextHora);
        btnEliminar = findViewById(R.id.btnEliminarTurno);

        turnoDao = AppDatabase.getInstance(this).turnoDao();

        final Calendar calendario = Calendar.getInstance();

        String[] profesionales = {"Dra. López", "Lic. García", "Dr. Pérez"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, profesionales);
        spinnerProfesional.setAdapter(adapter);

        Intent intent = getIntent();
        turnoId = intent.getIntExtra("turnoId", -1);

        if (turnoId != -1) {
            executor.execute(() -> {
                Turno turno = turnoDao.buscarPorId(turnoId);
                if (turno != null) {
                    runOnUiThread(() -> {
                        int spinnerPosition = adapter.getPosition(turno.getNombreProfesional());
                        spinnerProfesional.setSelection(spinnerPosition);
                        editTextFecha.setText(turno.getFecha());
                        editTextHora.setText(turno.getHora());
                        btnFecha.setText(turno.getFecha());
                        btnHora.setText(turno.getHora());
                        btnEliminar.setVisibility(Button.VISIBLE);
                    });
                }
            });
        } else {
            btnEliminar.setVisibility(Button.GONE);
        }

        btnFecha.setOnClickListener(v -> {
            int año = calendario.get(Calendar.YEAR);
            int mes = calendario.get(Calendar.MONTH);
            int día = calendario.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    ReservarTurnoActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        String fechaSeleccionada = dayOfMonth + "/" + (month + 1) + "/" + year;
                        btnFecha.setText(fechaSeleccionada);
                        editTextFecha.setText(fechaSeleccionada);
                    },
                    año, mes, día
            );
            datePickerDialog.show();
        });

        btnHora.setOnClickListener(h -> {
            int hora = calendario.get(Calendar.HOUR_OF_DAY);
            int minuto = calendario.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    ReservarTurnoActivity.this,
                    (view, hourOfDay, minute) -> {
                        String horaSeleccionada = String.format("%02d:%02d", hourOfDay, minute);
                        btnHora.setText(horaSeleccionada);
                        editTextHora.setText(horaSeleccionada);
                    },
                    hora, minuto, true
            );
            timePickerDialog.show();
        });

        btnGuardar.setOnClickListener(view -> {
            String profesional = spinnerProfesional.getSelectedItem().toString();
            String fecha = editTextFecha.getText().toString().trim();
            String hora = editTextHora.getText().toString().trim();

            if (fecha.isEmpty() || hora.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            executor.execute(() -> {
                if (turnoId == -1) {
                    Turno nuevoTurno = new Turno("Paciente", "Motivo", fecha, hora, profesional); // Ajusta si quieres guardar nombre y motivo real
                    turnoDao.insert(nuevoTurno);
                } else {
                    Turno turnoEditado = turnoDao.buscarPorId(turnoId);
                    if (turnoEditado != null) {
                        turnoEditado.setNombreProfesional(profesional);
                        turnoEditado.setFecha(fecha);
                        turnoEditado.setHora(hora);
                        turnoDao.actualizar(turnoEditado);
                    }
                }
                runOnUiThread(() -> {
                    Toast.makeText(this, "Turno guardado", Toast.LENGTH_SHORT).show();
                    finish();
                });
            });
        });

        btnEliminar.setOnClickListener(view -> {
            if (turnoId == -1) {
                Toast.makeText(this, "No hay turno seleccionado", Toast.LENGTH_SHORT).show();
                return;
            }
            executor.execute(() -> {
                Turno turnoAEliminar = turnoDao.buscarPorId(turnoId);
                if (turnoAEliminar != null) {
                    turnoDao.delete(turnoAEliminar);
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Turno eliminado", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show());
                }
            });
        });

    }
}