package com.example.pruebafinalis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class modificar_ve extends AppCompatActivity {

    private EditText dpi_edEditText,nombre_edEditText,apellido_edEditText,telefo_edEditText,dir_edEditText,ing_edEditText,paq_idEditText;
private Button regresar_v,busqueda_v,editar_v,eliminar_v;
    private RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_modificar_ve);

        // Inicializar vistas
        dpi_edEditText = findViewById(R.id.dpi_edEditText);
        nombre_edEditText = findViewById(R.id.nombre_edEditText);
        apellido_edEditText = findViewById(R.id.apellido_edEditText);
        telefo_edEditText = findViewById(R.id.telefo_edEditText);
        dir_edEditText = findViewById(R.id.dir_edEditText);
        ing_edEditText = findViewById(R.id.ing_edEditText);
        paq_idEditText = findViewById(R.id.paq_idEditText);
        busqueda_v = findViewById(R.id.busqueda_v);

        // Inicializar RequestQueue
        requestQueue = Volley.newRequestQueue(this);

        // Configurar el listener del botón de búsqueda
        busqueda_v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el número de DPI ingresado
                String dpi = dpi_edEditText.getText().toString();

                // Realizar la lógica de búsqueda de datos asociados al DPI
                buscarDatosPorDPI(dpi);
            }
        });

        // Configurar el listener del botón de editar
        editar_v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener los nuevos datos ingresados
                // Aquí debes obtener los datos de los EditText y crear un JSONObject con los datos actualizados
                JSONObject newData = new JSONObject();
                try {
                    newData.put("nombre", nombre_edEditText.getText().toString());
                    newData.put("apellido", apellido_edEditText.getText().toString());
                    // Agregar otros campos de datos según sea necesario
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Llamar al método para actualizar datos en la API
                actualizarDatos(newData);
            }
        });

        // Configurar el listener del botón de eliminar
        eliminar_v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Llamar al método para eliminar datos en la API
                eliminarDatos();
            }
        });
    }

    // Método para buscar datos asociados a un DPI
    private void buscarDatosPorDPI(String dpi) {
        // Construir la URL para la búsqueda
        String url = "https://api-production-c57e.up.railway.app/api/solicitudes-busca/" + dpi;

        // Realizar la solicitud HTTP GET
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    // Procesar la respuesta del servidor
                    try {
                        // Obtener los datos del JSON de respuesta
                        String nombre = response.getString("nombre");
                        String apellido = response.getString("apellido");
                        // Obtener otros datos según sea necesario

                        // Actualizar los campos de texto con los datos obtenidos
                        nombre_edEditText.setText(nombre);
                        apellido_edEditText.setText(apellido);
                        // Actualizar otros campos de texto según sea necesario
                    } catch (JSONException e) {
                        e.printStackTrace();
                        // Manejar errores de procesamiento de JSON
                    }
                },
                error -> {
                    // Manejar errores de la solicitud HTTP
                    Toast.makeText(modificar_ve.this, "Error al buscar datos", Toast.LENGTH_SHORT).show();
                });

        // Agregar la solicitud a la cola de solicitudes
        requestQueue.add(jsonObjectRequest);
    }

    // Método para actualizar datos en la API
    private void actualizarDatos(JSONObject newData) {
        // Construir la URL para la actualización
        String url = "https://api-production-c57e.up.railway.app/api/solicitudes-update/id"; // Reemplaza "id" con el ID del registro que quieres actualizar

        // Realizar la solicitud HTTP PUT o POST
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, newData,
                response -> {
                    // Manejar la respuesta del servidor (por ejemplo, mostrar un mensaje de éxito)
                    Toast.makeText(modificar_ve.this, "Datos actualizados correctamente", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    // Manejar errores de la solicitud HTTP
                    Toast.makeText(modificar_ve.this, "Error al actualizar datos", Toast.LENGTH_SHORT).show();
                });

        // Agregar la solicitud a la cola de solicitudes
        requestQueue.add(jsonObjectRequest);
    }

    // Método para eliminar datos en la API
    private void eliminarDatos() {
        // Construir la URL para la eliminación
        String url = "https://api-production-c57e.up.railway.app/api/solicitudes-destroy/id"; // Reemplaza "id" con el ID del registro que quieres eliminar

        // Realizar la solicitud HTTP DELETE
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,
                response -> {
                    // Manejar la respuesta del servidor (por ejemplo, mostrar un mensaje de éxito)
                    Toast.makeText(modificar_ve.this, "Datos eliminados correctamente", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    // Manejar errores de la solicitud HTTP
                    Toast.makeText(modificar_ve.this, "Error al eliminar datos", Toast.LENGTH_SHORT).show();
                });

        // Agregar la solicitud a la cola de solicitudes
        requestQueue.add(stringRequest);
    }

    public void crud_vendedor(View vista){
        Intent salir = new Intent(this, crud_vendedor.class);
        startActivity(salir);
    }

}