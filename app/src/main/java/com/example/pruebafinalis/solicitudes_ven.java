package com.example.pruebafinalis;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class solicitudes_ven extends AppCompatActivity {
    private EditText no_dpi, nombre_cli, apellido_cli, telefo, ingresoso_t, direccion_cli, paqueteria;
    private Button agregar, regresar;
    private RequestQueue requestQueue;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_solicitudes_ven);

        // Inicio de vistas
        no_dpi = findViewById(R.id.dpi_edEditText);
        nombre_cli = findViewById(R.id.nombre_cli);
        apellido_cli = findViewById(R.id.apellido_cli);
        telefo = findViewById(R.id.telefo);
        ingresoso_t = findViewById(R.id.ingresoso_t);
        direccion_cli = findViewById(R.id.direccion_cli);
        paqueteria = findViewById(R.id.paqueteria);
        agregar = findViewById(R.id.agregar);
        regresar = findViewById(R.id.regresar_v);

        // Inicializar RequestQueue
        requestQueue = Volley.newRequestQueue(this);

        // Inicializar SessionManager
        sessionManager = new SessionManager(this);

        // Configurar el botón de crear usuario
        agregar.setOnClickListener(v -> {
            String nombre = nombre_cli.getText().toString();
            String apellido = apellido_cli.getText().toString();
            String direccion = direccion_cli.getText().toString();
            String paquete = paqueteria.getText().toString();

            if (nombre.isEmpty() || apellido.isEmpty() || direccion.isEmpty() || paquete.isEmpty()) {
                Toast.makeText(solicitudes_ven.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                Integer dpi = Integer.parseInt(no_dpi.getText().toString());
                Integer telefono = Integer.parseInt(telefo.getText().toString());
                Float ingresos = Float.parseFloat(ingresoso_t.getText().toString());

                JSONObject newSolicitud = new JSONObject();
                newSolicitud.put("nombre", nombre);
                newSolicitud.put("apellido", apellido);
                newSolicitud.put("dpi", dpi);
                newSolicitud.put("telefono", telefono);
                newSolicitud.put("direccion", direccion);
                newSolicitud.put("ingresos", ingresos);
                newSolicitud.put("paquete", paquete);

                // Enviar la solicitud HTTP para guardar el nuevo usuario
                guardar_nueva_solicitud(newSolicitud);
            } catch (JSONException | NumberFormatException e) {
                e.printStackTrace();
                Toast.makeText(solicitudes_ven.this, "Error al crear la solicitud", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void guardar_nueva_solicitud(JSONObject newSolicitud) {
        String url = "https://api-production-c57e.up.railway.app/api/solicitudes-create";
        String token = sessionManager.getAuthToken();

        if (token == null || token.isEmpty()) {
            Toast.makeText(solicitudes_ven.this, "Token no encontrado. Por favor, inicie sesión nuevamente.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(solicitudes_ven.this, MainActivity.class));
            finish();
            return;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, newSolicitud,
                response -> {
                    Toast.makeText(solicitudes_ven.this, "Solicitud creada exitosamente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(solicitudes_ven.this, Usuario.class);
                    startActivity(intent);
                    finish();
                },
                error -> {
                    Toast.makeText(solicitudes_ven.this, "Error al guardar la solicitud", Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
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
    }

    public void crud_vendedor(View vista) {
        Intent salir = new Intent(this, crud_vendedor.class);
        startActivity(salir);
    }
}