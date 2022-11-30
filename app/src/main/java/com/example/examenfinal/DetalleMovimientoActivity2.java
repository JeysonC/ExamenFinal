package com.example.examenfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.examenfinal.entities.Movimiento;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class DetalleMovimientoActivity2 extends AppCompatActivity {

    TextView tvTipo, tvMonto, tvMotivo, tvlatitud, tvlongitud;
    Double latitud = 0d;
    Double longitud = 0d;
    ImageView ivDetalleBouchers;
    Movimiento movimiento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_movimiento2);

        tvTipo = findViewById(R.id.tvDetalleTipos);
        tvMonto = findViewById(R.id.tvDetalleMontos);
        tvMotivo = findViewById(R.id.tvDetalleMotivos);
        tvlatitud = findViewById(R.id.tvDetalleLatituds);
        tvlongitud = findViewById(R.id.tvDetalleLongituds);
        ivDetalleBouchers = findViewById(R.id.ivDetalleBouchers);

        Intent intent = getIntent();
        String motivimientoJson = intent.getStringExtra("DATOS_MOVIMIENTO");

        if (motivimientoJson != null){
            movimiento = new Gson().fromJson(motivimientoJson, Movimiento.class);

            tvTipo.setText(movimiento.tipo);
            tvMonto.setText(movimiento.monto);
            tvMotivo.setText(movimiento.motivo);
            Picasso.get().load(movimiento.imagenURL).into(ivDetalleBouchers);
            tvlatitud.setText(movimiento.latitud.toString());
            tvlongitud.setText(movimiento.longitud.toString());

        }
        if (motivimientoJson == null){
            return;
        }
    }

    public void verUbicacion(View view){
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        intent.putExtra("COORDENADAS", new Gson().toJson(movimiento));
        startActivity(intent);
    }

    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        DetalleMovimientoActivity2.Localizacion Local = new DetalleMovimientoActivity2.Localizacion();
        Local.setMainActivity(this);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local);
    }

    //Clase Localizacion
    public class Localizacion implements LocationListener {

        DetalleMovimientoActivity2 detalleMovimientoActivity2;
        public DetalleMovimientoActivity2 getMainActivity() {
            return detalleMovimientoActivity2;
        }

        public void setMainActivity(DetalleMovimientoActivity2 mainActivity) {
            this.detalleMovimientoActivity2 = mainActivity;
        }

        @Override
        public void onLocationChanged(Location loc) {
            loc.getLatitude();
            loc.getLongitude();
            latitud = loc.getLatitude();
            longitud = loc.getLongitude();
            tvlatitud.setText("Latitud: " + latitud);
            tvlongitud.setText("Longitud: " + longitud);
        }

        @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado
            Toast.makeText(getApplicationContext(), "GPS Desactivado", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es activado
            Toast.makeText(getApplicationContext(), "GPS Activado", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("debug", "LocationProvider.AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }
    }
    //Permisos de localización
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1001){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                locationStart();
                return;
            }
        }
    }
}