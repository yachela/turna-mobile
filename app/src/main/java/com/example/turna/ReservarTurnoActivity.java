package com.example.turna;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import com.google.firebase.auth.FirebaseAuth;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.turna.database.TurnoDbHelper;
import com.example.turna.model.Turno;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.util.Calendar;

public class ReservarTurnoActivity extends AppCompatActivity {

    private Spinner spinnerProfesional;
    private EditText editTextFecha, editTextHora;
    private Button btnGuardar;
    private int turnoId;
    private Button btnEliminar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        setContentView(R.layout.activity_reservar_turno);

        spinnerProfesional = findViewById(R.id.spinnerProfesional);
        btnGuardar = findViewById(R.id.btnGuardarTurno);
        Button btnFecha = findViewById(R.id.btnFecha);
        Button btnHora = findViewById(R.id.btnHora);
        editTextFecha = findViewById(R.id.editTextFecha);
        editTextHora = findViewById(R.id.editTextHora);
        btnEliminar = findViewById(R.id.btnEliminarTurno);

        final Calendar calendario = Calendar.getInstance();

        // Setup spinner
        String[] profesionales = {"Dra. López", "Lic. García", "Dr. Pérez"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, profesionales);
        spinnerProfesional.setAdapter(adapter);

        // Leer datos si es edición
        Intent intent = getIntent();
        turnoId = intent.getIntExtra("turnoId", -1);
        if (turnoId != -1) {
            TurnoDbHelper dbHelper = new TurnoDbHelper(this);
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            String[] projection = {
                    TurnoDbHelper.COLUMN_PROFESIONAL,
                    TurnoDbHelper.COLUMN_FECHA,
                    TurnoDbHelper.COLUMN_HORA
            };

            String selection = TurnoDbHelper.COLUMN_ID + " = ?";
            String[] selectionArgs = {String.valueOf(turnoId)};

            try (android.database.Cursor cursor = db.query(
                    TurnoDbHelper.TABLE_TURNOS,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            )) {
                if (cursor.moveToFirst()) {
                    String profesional = cursor.getString(cursor.getColumnIndexOrThrow(TurnoDbHelper.COLUMN_PROFESIONAL));
                    String fecha = cursor.getString(cursor.getColumnIndexOrThrow(TurnoDbHelper.COLUMN_FECHA));
                    String hora = cursor.getString(cursor.getColumnIndexOrThrow(TurnoDbHelper.COLUMN_HORA));

                    int spinnerPosition = adapter.getPosition(profesional);
                    spinnerProfesional.setSelection(spinnerPosition);
                    editTextFecha.setText(fecha);
                    editTextHora.setText(hora);
                    btnFecha.setText(fecha);
                    btnHora.setText(hora);
                }
            }
            db.close();
            btnEliminar.setVisibility(View.VISIBLE);
        } else {
            btnEliminar.setVisibility(View.GONE);
        }

        // Configuración de botones
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

        // Guardado de turno
        btnGuardar.setOnClickListener(view -> {
            String profesional = spinnerProfesional.getSelectedItem().toString();
            String fecha = editTextFecha.getText().toString().trim();
            String hora = editTextHora.getText().toString().trim();

            if (fecha.isEmpty() || hora.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            TurnoDbHelper dbHelper = new TurnoDbHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(TurnoDbHelper.COLUMN_PROFESIONAL, profesional);
            values.put(TurnoDbHelper.COLUMN_FECHA, fecha);
            values.put(TurnoDbHelper.COLUMN_HORA, hora);

            long result;
            if (turnoId == -1) {
                result = db.insert(TurnoDbHelper.TABLE_TURNOS, null, values);
            } else {
                String selection = TurnoDbHelper.COLUMN_ID + " = ?";
                String[] selectionArgs = {String.valueOf(turnoId)};
                result = db.update(TurnoDbHelper.TABLE_TURNOS, values, selection, selectionArgs);
            }

            if (result != -1) {
                Toast.makeText(this, "Turno guardado", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error al guardar el turno", Toast.LENGTH_SHORT).show();
            }

            db.close();
        });

        // Eliminación de turno
        btnEliminar.setOnClickListener(view -> {
            if (turnoId == -1) {
                Toast.makeText(this, "No hay turno seleccionado", Toast.LENGTH_SHORT).show();
                return;
            }
            TurnoDbHelper dbHelper = new TurnoDbHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            String selection = TurnoDbHelper.COLUMN_ID + " = ?";
            String[] selectionArgs = {String.valueOf(turnoId)};

            int filasEliminadas = db.delete(TurnoDbHelper.TABLE_TURNOS, selection, selectionArgs);

            db.close();

            if (filasEliminadas > 0) {
                Toast.makeText(this, "Turno eliminado", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
