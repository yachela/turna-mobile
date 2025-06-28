package com.example.turna;

import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.turna.database.AppDatabase;
import com.example.turna.model.Turno;

import java.util.Calendar;

public class AgregarTurnoActivity extends AppCompatActivity {

    private EditText editTextNombre, editTextMotivo;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private Button btnGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_turno);

        editTextNombre = findViewById(R.id.editTextNombre);
        editTextMotivo = findViewById(R.id.editTextMotivo);
        datePicker = findViewById(R.id.datePicker);
        timePicker = findViewById(R.id.timePicker);
        btnGuardar = findViewById(R.id.btnGuardar);

        timePicker.setIs24HourView(true);

        btnGuardar.setOnClickListener(v -> {
            String nombre = editTextNombre.getText().toString().trim();
            String motivo = editTextMotivo.getText().toString().trim();

            if (nombre.isEmpty() || motivo.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth() + 1;
            int year = datePicker.getYear();
            String fecha = String.format("%02d/%02d/%04d", day, month, year);

            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();
            String hora = String.format("%02d:%02d", hour, minute);

            Turno nuevoTurno = new Turno(nombre,motivo,fecha, hora);

            new Thread(() -> {
                AppDatabase.getInstance(this).turnoDao().insert(nuevoTurno);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Turno guardado", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }).start();
        });
    }
}