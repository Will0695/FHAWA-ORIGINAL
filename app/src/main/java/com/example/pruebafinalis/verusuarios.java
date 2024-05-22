package com.example.pruebafinalis;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class verusuarios extends AppCompatActivity {
    private TextView usuariosTextView;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verusuarios);

        // Inicializar TextView para mostrar usuarios
        usuariosTextView = findViewById(R.id.usuariosTextView);

        // Inicializar la cola de solicitudes de Volley
        requestQueue = Volley.newRequestQueue(this);

        // Realizar solicitud GET a la API
        String url = "https://api-production-c57e.up.railway.app/api/users";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Procesar la respuesta JSON
                        mostrarUsuarios(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar errores de la solicitud
                        Toast.makeText(verusuarios.this, "Error al obtener la lista de usuarios", Toast.LENGTH_SHORT).show();
                    }
                });

        // Agregar la solicitud a la cola de solicitudes
        requestQueue.add(jsonArrayRequest);
    }

    private void mostrarUsuarios(JSONArray usuarios) {
        // Construir una cadena con la información de los usuarios
        StringBuilder usuariosStr = new StringBuilder();
        for (int i = 0; i < usuarios.length(); i++) {
            try {
                JSONObject usuario = usuarios.getJSONObject(i);
                String nombre = usuario.getString("nombre");
                String apellido = usuario.getString("apellido");
                String nombreUsuario = usuario.getString("nombre_usuario");
                String rol = usuario.getString("rol");
                String correo = usuario.getString("email");

                // Agregar la información del usuario a la cadena
                usuariosStr.append("Nombre: ").append(nombre).append("\n");
                usuariosStr.append("Apellido: ").append(apellido).append("\n");
                usuariosStr.append("Nombre de usuario: ").append(nombreUsuario).append("\n");
                usuariosStr.append("Rol: ").append(rol).append("\n");
                usuariosStr.append("Correo electrónico: ").append(correo).append("\n\n");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // Mostrar la cadena en el TextView
        usuariosTextView.setText(usuariosStr.toString());
    }
}
