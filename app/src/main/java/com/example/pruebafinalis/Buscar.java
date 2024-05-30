package com.example.pruebafinalis;

import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Buscar extends AppCompatActivity {

    Button btnBuscar, btnMostrarTodo;
    EditText etBuscar;
    ListView lV_sol;
    ArrayAdapter<String> adapter;

    private RequestQueue requestQueue;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_buscar);

        // Inicialización de los objetos
        btnBuscar = findViewById(R.id.btnBuscar);
       // btnMostrarTodo = findViewById(R.id.btnMostrarTodo);
        etBuscar = findViewById(R.id.etBuscar);
        lV_sol = findViewById(R.id.lV_sol);
        //listView = findViewById(R.id.ListView);

        // Inicializar RequestQueue
        requestQueue = Volley.newRequestQueue(this);
        // Inicializar SessionManager
       sessionManager = new SessionManager(this);

       //final String token = "token";

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = etBuscar.getText().toString();
                String url = "https://api-production-c57e.up.railway.app/api/solicitudes-busca/" + id;
                String token = sessionManager.getAuthToken();

                if (token == null || token.isEmpty()) {
                    Toast.makeText(Buscar.this, "Token no encontrado. Por favor, inicie sesión nuevamente.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Buscar.this, MainActivity.class));
                    finish();
                    return;
                }

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                        response -> {
                            try {
                                // Imprime la respuesta completa para depuración
                                Log.d("Response", response.toString());

                                // Verifica si la clave "userData" existe en el objeto JSON
                                if (response.has("userData")) {
                                    JSONObject userData = response.getJSONObject("userData");

                                    String nombre = userData.getString("nombre");
                                    String apellido = userData.getString("apellido");
                                    long dpi = userData.getLong("dpi");
                                    long telefono = userData.getLong("telefono");
                                    String direccion = userData.getString("direccion");
                                    int ingresos = userData.getInt("ingresos");
                                    String paquete = userData.getString("paquete");
                                    int userId = userData.getInt("user_id");

                                    // Crear una lista de mapas para los datos
                                    List<Map<String, String>> data = new ArrayList<>();
                                    Map<String, String> datum = new HashMap<>();
                                    datum.put("title", "Nombre");
                                    datum.put("value", nombre);
                                    data.add(datum);

                                    datum = new HashMap<>();
                                    datum.put("title", "Apellido");
                                    datum.put("value", apellido);
                                    data.add(datum);

                                    datum = new HashMap<>();
                                    datum.put("title", "DPI");
                                    datum.put("value", String.valueOf(dpi));
                                    data.add(datum);

                                    datum = new HashMap<>();
                                    datum.put("title", "Teléfono");
                                    datum.put("value", String.valueOf(telefono));
                                    data.add(datum);

                                    datum = new HashMap<>();
                                    datum.put("title", "Dirección");
                                    datum.put("value", direccion);
                                    data.add(datum);

                                    datum = new HashMap<>();
                                    datum.put("title", "Ingresos");
                                    datum.put("value", String.valueOf(ingresos));
                                    data.add(datum);

                                    datum = new HashMap<>();
                                    datum.put("title", "Paquete");
                                    datum.put("value", paquete);
                                    data.add(datum);

                                    datum = new HashMap<>();
                                    datum.put("title", "User ID");
                                    datum.put("value", String.valueOf(userId));
                                    data.add(datum);

                                    // Crear un adaptador
                                    SimpleAdapter adapter = new SimpleAdapter(
                                            Buscar.this,
                                            data,
                                            android.R.layout.simple_list_item_2,
                                            new String[]{"title", "value"},
                                            new int[]{android.R.id.text1, android.R.id.text2});

                                    // Establecer el adaptador en el ListView
                                    runOnUiThread(() -> lV_sol.setAdapter(adapter));

                                } else {
                                    Log.e("Error", "La clave 'userData' no existe en la respuesta JSON.");
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        },
                        error -> {
                            // Manejar errores de la solicitud HTTP
                            Log.e("Error", "Error en la solicitud HTTP: " + error.toString());
                            Toast.makeText(Buscar.this, "Error al buscar datos", Toast.LENGTH_SHORT).show();
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
        });

    }

    public void crud_vendedor(View vista){
        Intent salir = new Intent(this, crud_vendedor.class);
        startActivity(salir);
    }
}




