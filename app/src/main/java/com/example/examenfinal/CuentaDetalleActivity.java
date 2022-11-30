package com.example.examenfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.examenfinal.entities.Cuenta;
import com.google.gson.Gson;

public class CuentaDetalleActivity extends AppCompatActivity {

    TextView tvNombreCuenta;

    Cuenta cuenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuenta_detalle);

        tvNombreCuenta = findViewById(R.id.tvDetalleNombreCuent);

        Intent intent = getIntent();
        String cuentaJson = intent.getStringExtra("CUENTA_NOMBRE");

        if(cuentaJson != null){
            cuenta = new Gson().fromJson(cuentaJson, Cuenta.class);
            tvNombreCuenta.setText(cuenta.nombre);
        }
        if(cuentaJson == null ){
            return;
        }

    }



    public void registrarMovimiento(View view){
        Intent intent = new Intent(getApplicationContext(),RegistrarMovimientoActivity.class);
        startActivity(intent);
    }

    public void verMovimientos(View view){
        Intent intent = new Intent(getApplicationContext(), ListarMovimientos.class);
        startActivity(intent);
    }

    public void sincronizar(View view){

    }
}