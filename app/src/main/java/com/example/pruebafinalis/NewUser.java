package com.example.pruebafinalis;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

public class NewUser extends AppCompatActivity {

    private EditText usernameEditText, apellidoEditText, nombre_usuarioEditText, correoEditText, passwordEditText, confirpasswordEditText;
    private Spinner roleSpinner;
    private Button crearButton;
    private RequestQueue requestQueue;
    private ProgressBar crearUsuarioProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        // Inicializar vistas
        usernameEditText = findViewById(R.id.usernameEditText);
        apellidoEditText = findViewById(R.id.apellidoEditText);
        nombre_usuarioEditText = findViewById(R.id.nombre_usuarioEditText);
        correoEditText = findViewById(R.id.correoEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirpasswordEditText = findViewById(R.id.confirpasswordEditText);
        roleSpinner = findViewById(R.id.roleSpinner);
        crearButton = findViewById(R.id.crearButton);



        // Inicializar RequestQueue
        requestQueue = Volley.newRequestQueue(this);

        // Configurar el Spinner de roles
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.roles_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);

        // Configurar el botón de crear usuario
        crearButton.setOnClickListener(v -> {
            // Obtener los valores ingresados por el usuario
            String nombre = usernameEditText.getText().toString();
            String apellido = apellidoEditText.getText().toString();
            String nombreUsuario = nombre_usuarioEditText.getText().toString();
            String correo = correoEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String confirmPassword = confirpasswordEditText.getText().toString();
            String rol = roleSpinner.getSelectedItem().toString();

            // Verificar si se han ingresado todos los campos
            if (nombre.isEmpty() || apellido.isEmpty() || nombreUsuario.isEmpty() || correo.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(NewUser.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Verificar si las contraseñas coinciden
            if (!password.equals(confirmPassword)) {
                Toast.makeText(NewUser.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                return;
            }

            // Crear un objeto JSON con los datos del nuevo usuario
            JSONObject newUser = new JSONObject();
            try {
                newUser.put("nombre", nombre);
                newUser.put("apellido", apellido);
                newUser.put("nombre_usuario", nombreUsuario);
                newUser.put("tipo_usuario", rol); // Agregar el rol del usuario
                newUser.put("email", correo);
                newUser.put("password", password);
                newUser.put("password_confirmation", confirmPassword);
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(NewUser.this, "Error al crear el nuevo usuario", Toast.LENGTH_SHORT).show();
                return;
            }

            // Enviar la solicitud HTTP para guardar el nuevo usuario
            guardarNuevoUsuario(newUser);
        });

        // Establecer filtros de entrada para aceptar solo letras
        setInputFilters();
    }

    // Método para establecer filtros de entrada en los campos de texto
    private void setInputFilters() {
        usernameEditText.setFilters(new InputFilter[]{new InputFilterLetters()});
        apellidoEditText.setFilters(new InputFilter[]{new InputFilterLetters()});
        nombre_usuarioEditText.setFilters(new InputFilter[]{new InputFilterLetters()});
    }

    private void guardarNuevoUsuario(JSONObject newUser) {
        // URL de la API para guardar el nuevo usuario
        String url = "https://api-production-c57e.up.railway.app/api/register"; // Usar HTTPS

        // Crear la solicitud HTTP POST
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, newUser,
                response -> {
                    // Manejar la respuesta del servidor (por ejemplo, mostrar un mensaje de éxito)
                    Toast.makeText(NewUser.this, "Usuario creado exitosamente", Toast.LENGTH_SHORT).show();
                    // Dirigir al usuario a la actividad Usuario
                    Intent intent = new Intent(NewUser.this, Usuario.class);
                    startActivity(intent);
                    finish(); // Opcional: para cerrar la actividad actual después de iniciar la actividad Usuario
                },
                error -> {
                    // Manejar la respuesta de error del servidor (por ejemplo, mostrar un mensaje de error)
                    Toast.makeText(NewUser.this, "Error al guardar el usuario", Toast.LENGTH_SHORT).show();

                });

        // Agregar la solicitud a la cola de solicitudes
        requestQueue.add(jsonObjectRequest);
    }


}
