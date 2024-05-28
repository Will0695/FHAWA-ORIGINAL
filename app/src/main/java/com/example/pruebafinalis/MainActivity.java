package com.example.pruebafinalis;

import android.content.Intent;
import android.text.InputType;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;


import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private TextInputEditText passwordEditText;
    private ProgressBar progressBar;
    private TextView errorTextView;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        progressBar = findViewById(R.id.progressBar);
        errorTextView = findViewById(R.id.errorTextView);

        sessionManager = new SessionManager(this);

        // Verificar si hay una sesión activa
        String token = sessionManager.getAuthToken();
        if (token != null && !token.isEmpty()) {
            // Iniciar la actividad Usuario directamente
            startActivity(new Intent(MainActivity.this, Usuario.class));
            finish();
        }
    }

    public void onLoginClicked(View view) {
        progressBar.setVisibility(View.VISIBLE); // Mostrar el ProgressBar al iniciar la solicitud de inicio de sesión
        loginPost();
    }

    public void loginPost() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String postUrl = "https://api-production-c57e.up.railway.app/api/login";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Validar campos de entrada
        if (username.isEmpty() || password.isEmpty()) {
            progressBar.setVisibility(View.GONE);
            errorTextView.setText("Por favor, complete todos los campos");
            errorTextView.setVisibility(View.VISIBLE);
            return;
        }

        JSONObject postData = new JSONObject();
        try {
            postData.put("nombre_usuario", username);
            postData.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
            progressBar.setVisibility(View.GONE);
            errorTextView.setText("Error al crear la solicitud de inicio de sesión");
            errorTextView.setVisibility(View.VISIBLE);
            return;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, postData,
                response -> {
                    progressBar.setVisibility(View.GONE);
                    try {
                        String token = response.getString("token");
                        sessionManager.saveAuthToken(token);

                        startActivity(new Intent(MainActivity.this, Usuario.class));
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        errorTextView.setText("Error al analizar el token");
                        errorTextView.setVisibility(View.VISIBLE);
                    }
                },
                error -> {
                    progressBar.setVisibility(View.GONE);
                    errorTextView.setVisibility(View.VISIBLE);
                    errorTextView.setText("Error al iniciar sesión. Verifique sus credenciales e intente nuevamente.");
                    usernameEditText.setText("");
                    passwordEditText.setText("");
                    usernameEditText.requestFocus();
                    error.printStackTrace();
                }
        );

        requestQueue.add(jsonObjectRequest);
    }
}