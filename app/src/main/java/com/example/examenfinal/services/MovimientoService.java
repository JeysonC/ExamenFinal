package com.example.examenfinal.services;

import com.example.examenfinal.entities.Cuenta;
import com.example.examenfinal.entities.Movimiento;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface MovimientoService {
    @GET("movimientos")
    Call<List<Movimiento>> getAll();

    @POST("movimientos")
    Call<Movimiento> create(@Body Movimiento movimiento);
}
