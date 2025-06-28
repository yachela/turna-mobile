package com.example.turna;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.turna.database.AppDatabase;
import com.example.turna.database.Usuario;
import com.example.turna.database.UsuarioDao;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private Button btnLogin;
    private UsuarioDao usuarioDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.btnLogin);
        TextView linkRegistro = findViewById(R.id.linkRegistro);

        usuarioDao = AppDatabase.getInstance(this).usuarioDao();

        btnLogin.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                Usuario usuario = usuarioDao.login(email, password);
                runOnUiThread(() -> {
                    if (usuario != null) {
                        getSharedPreferences("TurnaPrefs", MODE_PRIVATE)
                                .edit()
                                .putBoolean("logueado", true)
                                .apply();
                        Intent intent = new Intent(LoginActivity.this, MenuPrincipalActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });

        linkRegistro.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
            startActivity(intent);
        });
    }
}