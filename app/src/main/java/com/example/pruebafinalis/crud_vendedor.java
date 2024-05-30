package com.example.pruebafinalis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class crud_vendedor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crud_vendedor);

    }
    public void solicitudes_ven(View vista){
        Intent solicitado = new Intent(this, solicitudes_ven.class);
        startActivity(solicitado);
    }
    public void Usuario(View vista){
        Intent salir = new Intent(this, Usuario.class);
        startActivity(salir);
    }
    public void buscar(View vista){
        Intent buscar = new Intent(this, Buscar.class);
        startActivity(buscar);
    }
    public void delete_sol(View vista){
        Intent delete_sol = new Intent(this,Delete_sol.class);
        startActivity(delete_sol);
    }
}