package com.example.turna;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.turna.database.AppDatabase;
import com.example.turna.database.Usuario;
import com.example.turna.database.UsuarioDao;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RegistroActivity extends AppCompatActivity {

    private EditText editTextEmailRegistro, editTextPasswordRegistro;
    private Button btnRegistrar;
    private UsuarioDao usuarioDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        editTextEmailRegistro = findViewById(R.id.editTextEmailRegistro);
        editTextPasswordRegistro = findViewById(R.id.editTextPasswordRegistro);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        usuarioDao = AppDatabase.getInstance(this).usuarioDao();

        btnRegistrar.setOnClickListener(v -> {
            String email = editTextEmailRegistro.getText().toString().trim();
            String password = editTextPasswordRegistro.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                Usuario existente = usuarioDao.findByEmail(email);
                if (existente != null) {
                    runOnUiThread(() ->
                            Toast.makeText(this, "Ya existe un usuario con ese email", Toast.LENGTH_SHORT).show()
                    );
                } else {
                    Usuario nuevo = new Usuario(email, password);
                    usuarioDao.insert(nuevo);

                    runOnUiThread(() -> {
                        Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, LoginActivity.class));
                        finish();
                    });
                }
            });
        });
    }
}