package com.example.pruebafinalis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.List;

public class Buscar extends AppCompatActivity {
    private SolicitudesService service;
    private ArrayAdapter<Solicitud> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_buscar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ListView listView = findViewById(R.id.ListView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api-production-c57e.up.railway.app/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(SolicitudesService.class);

        Button botonBuscar = findViewById(R.id.btnBuscar);
        EditText editTextId = findViewById(R.id.etBuscar);
        botonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = Integer.parseInt(editTextId.getText().toString());

                // Para obtener una solicitud específica
                Call<Solicitud> call = service.getSolicitud(id);
                call.enqueue(new Callback<Solicitud>() {
                    @Override
                    public void onResponse(Call<Solicitud> call, Response<Solicitud> response) {
                        if (!response.isSuccessful()) {
                            // Manejar error
                            return;
                        }

                        Solicitud solicitud = response.body();
                        // Actualizar tu UI con los detalles de la solicitud
                    }

                    @Override
                    public void onFailure(Call<Solicitud> call, Throwable t) {
                        // Manejar error
                    }
                });
            }
        });

        Button botonListar = findViewById(R.id.btnMostrarTodo);
        botonListar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Para obtener todas las solicitudes
                Call<List<Solicitud>> call = service.getSolicitudes();
                call.enqueue(new Callback<List<Solicitud>>() {
                    @Override
                    public void onResponse(Call<List<Solicitud>> call, Response<List<Solicitud>> response) {
                        if (!response.isSuccessful()) {
                            // Manejar error
                            return;
                        }

                        List<Solicitud> solicitudes = response.body();
                        // Actualizar tu UI con la lista de solicitudes
                    }

                    @Override
                    public void onFailure(Call<List<Solicitud>> call, Throwable t) {
                        // Manejar error
                    }
                });
            }
        });
    }

    public void crud_vendedor(View vista){
        Intent salir = new Intent(this, crud_vendedor.class);
        startActivity(salir);
    }
}
