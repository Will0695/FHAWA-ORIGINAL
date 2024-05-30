package com.example.pruebafinalis;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import android.content.Intent;
import android.view.View;

public class Delete_sol extends AppCompatActivity{

    private EditText etBuscarEliminar;
    private Button btnBuscarEliminar, btnEliminar;
    private TextView tvNombreEliminar, tvApellidoEliminar, tvDpiEliminar;
    private RequestQueue requestQueue;
    private SessionManager sessionManager;
    private int solicitudId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_sol);

        etBuscarEliminar = findViewById(R.id.etBuscarEliminar);
        btnBuscarEliminar = findViewById(R.id.btnBuscarEliminar);
        btnEliminar = findViewById(R.id.btnEliminar);
        tvNombreEliminar = findViewById(R.id.tvNombreEliminar);
        tvApellidoEliminar = findViewById(R.id.tvApellidoEliminar);
        tvDpiEliminar = findViewById(R.id.tvDpiEliminar);
        requestQueue = Volley.newRequestQueue(this);
        sessionManager = new SessionManager(this);

        btnBuscarEliminar.setOnClickListener(view -> {
            String id = etBuscarEliminar.getText().toString();
            String url = "https://api-production-c57e.up.railway.app/api/solicitudes-busca/" + id;
            String token = sessionManager.getAuthToken();

            if (token == null || token.isEmpty()) {
                Toast.makeText(Delete_sol.this, "Token no encontrado. Por favor, inicie sesión nuevamente.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Delete_sol.this, MainActivity.class));
                finish();
                return;
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    response -> {
                        try {
                            Log.d("Response", response.toString());
                            if (response.has("userData")) {
                                JSONObject userData = response.getJSONObject("userData");

                                String nombre = userData.getString("nombre");
                                String apellido = userData.getString("apellido");
                                long dpi = userData.getLong("dpi");
                                solicitudId = userData.getInt("id");

                                tvNombreEliminar.setText("Nombre: " + nombre);
                                tvApellidoEliminar.setText("Apellido: " + apellido);
                                tvDpiEliminar.setText("DPI: " + dpi);

                                btnEliminar.setVisibility(View.VISIBLE);
                            } else {
                                Log.e("Error", "La clave 'userData' no existe en la respuesta JSON.");
                                Toast.makeText(Delete_sol.this, "Solicitud no encontrada.", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> {
                        Log.e("Error", "Error en la solicitud HTTP: " + error.toString());
                        Toast.makeText(Delete_sol.this, "Error al buscar datos", Toast.LENGTH_SHORT).show();
                    }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + token);
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };

            requestQueue.add(jsonObjectRequest);
        });

        btnEliminar.setOnClickListener(view -> {
            String url = "https://api-production-c57e.up.railway.app/api/solicitudes-destroy/" + solicitudId;
            String token = sessionManager.getAuthToken();

            if (token == null || token.isEmpty()) {
                Toast.makeText(Delete_sol.this, "Token no encontrado. Por favor, inicie sesión nuevamente.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Delete_sol.this, MainActivity.class));
                finish();
                return;
            }

            StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,
                    response -> {
                        Toast.makeText(Delete_sol.this, "Solicitud eliminada con éxito.", Toast.LENGTH_SHORT).show();
                        tvNombreEliminar.setText("Nombre: ");
                        tvApellidoEliminar.setText("Apellido: ");
                        tvDpiEliminar.setText("DPI: ");
                        btnEliminar.setVisibility(View.GONE);
                    },
                    error -> {
                        Log.e("Error", "Error en la solicitud HTTP: " + error.toString());
                        Toast.makeText(Delete_sol.this, "Error al eliminar la solicitud.", Toast.LENGTH_SHORT).show();
                    }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + token);
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };

            requestQueue.add(stringRequest);
        });
    }

    public void crud_vendedor(View vista){
        Intent salir = new Intent(this, crud_vendedor.class);
        startActivity(salir);
    }
}

