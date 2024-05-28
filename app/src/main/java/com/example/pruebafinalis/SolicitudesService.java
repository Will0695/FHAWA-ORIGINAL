package com.example.pruebafinalis;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import java.util.List;

public interface SolicitudesService {
    @GET("solicitudes-lista")
    Call<List<Solicitud>> getSolicitudes();

    @GET("solicitudes-busca/{id}")
    Call<Solicitud> getSolicitud(@Path("id") int id);
}


