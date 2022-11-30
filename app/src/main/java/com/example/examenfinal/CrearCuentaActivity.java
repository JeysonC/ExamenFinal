package com.example.examenfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.examenfinal.entities.Cuenta;
import com.example.examenfinal.services.CuentaService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CrearCuentaActivity extends AppCompatActivity {

    EditText etNombreCuenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cuenta);

        etNombreCuenta = findViewById(R.id.etCrearNombreCuenta);
    }

    public void guardarCuenta(View view){
        Cuenta nombreCuenta = new Cuenta();
        nombreCuenta.nombre = etNombreCuenta.getText().toString();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://635765f82712d01e1407281a.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CuentaService service = retrofit.create(CuentaService.class);
        service.create(nombreCuenta).enqueue(new Callback<Cuenta>() {
            @Override
            public void onResponse(Call<Cuenta> call, Response<Cuenta> response) {
                Log.i("MAIN_APP", String.valueOf(response.code()));

                Intent intent = new Intent(getApplicationContext(), ListarCuentasActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<Cuenta> call, Throwable t) {
                Log.i("MAIN_APP", "Fallo al guardar");
            }
        });
    }

}