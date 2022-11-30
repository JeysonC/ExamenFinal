package com.example.examenfinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.examenfinal.adapters.CuentasAdapter;
import com.example.examenfinal.entities.Cuenta;
import com.example.examenfinal.services.CuentaService;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListarCuentasActivity extends AppCompatActivity {

    RecyclerView rvCuentas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_cuentas);

        rvCuentas = findViewById(R.id.rvListarCuentas);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://635765f82712d01e1407281a.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CuentaService service = retrofit.create(CuentaService.class);
        service.getAll().enqueue(new Callback<List<Cuenta>>() {
            @Override
            public void onResponse(Call<List<Cuenta>> call, Response<List<Cuenta>> response) {
                List<Cuenta> data = response.body();
                rvCuentas.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                rvCuentas.setAdapter(new CuentasAdapter(data));
                Log.i("MAIN_APP", "Response: "+response.body().size());
                Log.i("MAIN_APP", new Gson().toJson(data));
            }

            @Override
            public void onFailure(Call<List<Cuenta>> call, Throwable t) {
                Log.i("MAIN_APP", "NO SE PUDO OBTENER LA DATA");
            }
        });

    }
}