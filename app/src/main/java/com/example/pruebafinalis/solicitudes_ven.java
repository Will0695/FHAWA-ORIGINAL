package com.example.pruebafinalis;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class solicitudes_ven extends AppCompatActivity {
    private EditText no_dpi,nombre_cli,apellido_cli,telefo,ingresoso_t ,direccion_cli, paqueteria;
    private Button agregar, regresar;
    private RequestQueue requestQueue;

    @Override
    protected void  onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_solicitudes_ven);
        // inicio de vistas
        no_dpi = findViewById(R.id.no_dpi);
        nombre_cli = findViewById(R.id.nombre_cli);
        apellido_cli = findViewById(R.id.apellido_cli);
        telefo = findViewById(R.id.telefo);
        ingresoso_t = findViewById(R.id.ingresoso_t);
        direccion_cli = findViewById(R.id.direccion_cli);
        paqueteria = findViewById(R.id.paqueteria);
        agregar = findViewById(R.id.agregar);
        regresar = findViewById(R.id.regresar);

        // Inicializar RequestQueue
        requestQueue = Volley.newRequestQueue(this);

        // Configurar el botón de crear usuario
        agregar.setOnClickListener(v -> {
            String nombre = nombre_cli.getText().toString();
            String dpi = no_dpi.getText().toString();
            String apellido = apellido_cli.getText().toString();
            String telefono = telefo.getText().toString();
            String ingresos = ingresoso_t.getText().toString();
            String direccion = direccion_cli.getText().toString();
            String paquete = paqueteria.toString().toString();

            // Verificar si se han ingresado todos los campos
            if (nombre.isEmpty() || apellido.isEmpty() || dpi.isEmpty() || telefono.isEmpty() || ingresos.isEmpty() || direccion.isEmpty() || paquete.isEmpty()) {
                Toast.makeText(solicitudes_ven.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
               }
            // Crear un objeto JSON con los datos del nuevo usuario
            JSONObject solicitudes_vem = new JSONObject();
            try {
                solicitudes_vem.put("nombre", nombre);
                solicitudes_vem.put("apellido", apellido);
                solicitudes_vem.put("dpi", dpi);
                solicitudes_vem.put("telefono", telefono); // Agregar el rol del usuario
                solicitudes_vem.put("ingresos", ingresos);
                solicitudes_vem.put("direccion", direccion);
                solicitudes_vem.put("paquete", paquete);
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(solicitudes_ven.this, "Error al crear la solicitud", Toast.LENGTH_SHORT).show();
                return;
            }

            // Enviar la solicitud HTTP para guardar el nuevo usuario
            guardar_nueva_solicitud(solicitudes_vem);
        });
        // Establecer filtros de entrada para aceptar solo letras
        setInputFilters();

    }
    private void setInputFilters() {
        nombre_cli.setFilters(new InputFilter[]{new InputFilterLetters()});
        apellido_cli.setFilters(new InputFilter[]{new InputFilterLetters()});
    }
    private void guardar_nueva_solicitud(JSONObject solicit) {
        // URL de la API
        String url = "https://api-production-c57e.up.railway.app/api/solicitudes-create"; // Usar HTTPS

        // Crear la solicitud HTTP POST
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, solicit,
                response -> {
                    // Manejar la respuesta del servidor (por ejemplo, mostrar un mensaje de éxito)
                    Toast.makeText(solicitudes_ven.this, "solicitud  creada exitosamente", Toast.LENGTH_SHORT).show();
                    // Dirigir al usuario a la actividad Usuario
                    Intent intent = new Intent(solicitudes_ven.this, Usuario.class);
                    startActivity(intent);
                    finish(); // Opcional: para cerrar la actividad actual después de iniciar la actividad Usuario
                },
                error -> {
                    // Manejar la respuesta de error del servidor (por ejemplo, mostrar un mensaje de error)
                    Toast.makeText(solicitudes_ven.this, "Error al guardar la solicitud", Toast.LENGTH_SHORT).show();

                });

        // Agregar la solicitud a la cola de solicitudes
        requestQueue.add(jsonObjectRequest);
    }
    public void crud_vendedor(View vista){
        Intent salir = new Intent(this, crud_vendedor.class);
        startActivity(salir);
    }
}