package com.example.examenfinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.examenfinal.adapters.CuentasAdapter;
import com.example.examenfinal.adapters.MovimientoAdapter;
import com.example.examenfinal.entities.Movimiento;
import com.example.examenfinal.services.MovimientoService;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListarMovimientos extends AppCompatActivity {

    RecyclerView rvListaMovimieto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_movimientos);

        rvListaMovimieto = findViewById(R.id.rvListaMovimientos);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://635765f82712d01e1407281a.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovimientoService service = retrofit.create(MovimientoService.class);
        service.getAll().enqueue(new Callback<List<Movimiento>>() {
            @Override
            public void onResponse(Call<List<Movimiento>> call, Response<List<Movimiento>> response) {
                List<Movimiento> data = response.body();
                rvListaMovimieto.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                rvListaMovimieto.setAdapter(new MovimientoAdapter(data));
                Log.i("MAIN_APP", "Response: "+response.body().size());
                Log.i("MAIN_APP", new Gson().toJson(data));
            }

            @Override
            public void onFailure(Call<List<Movimiento>> call, Throwable t) {
                Log.i("MAIN_APP", "NO SE PUDO OBTENER LA DATA");
            }
        });
    }
}