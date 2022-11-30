package com.example.examenfinal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.examenfinal.entities.Image;
import com.example.examenfinal.entities.ImageResponse;
import com.example.examenfinal.entities.Movimiento;
import com.example.examenfinal.services.ImagenService;
import com.example.examenfinal.services.MovimientoService;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistrarMovimientoActivity extends AppCompatActivity {

    EditText etTipo, etMonto, etMotivo;
    ImageView ivBoucher;

    TextView tvLatitud, tvLongitud;
    Double latitud = 0d;
    Double longitud = 0d;

    String encoded = "";
    String link = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_movimiento);

        etTipo = findViewById(R.id.etCrearTipoMovimiento);
        etMonto = findViewById(R.id.etCrearMonto);
        etMotivo = findViewById(R.id.etCrearMotivo);
        ivBoucher = findViewById(R.id.ivCrearBoucher);

        tvLatitud = findViewById(R.id.tvCrearLatitud);
        tvLongitud = findViewById(R.id.tvCrearLongitud);

    }

    public void tomarFotoCrear(View view){
        if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){ //verificamos los permisos
            abrirCamara(); //abrimos la cámara sí hay permisos
        }else{
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);//solicitar permisos
        }
    }

    //metodo abrir cámara
    public void abrirCamara(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1000);
    }

    //guardar la foto
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000 && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();//obtenemos la imagen
            Bitmap imageBitMap = (Bitmap) extras.get("data");

            //seteamos la imagen en la ivFotoPel
            ivBoucher.setImageBitmap(imageBitMap);

            //convertir a base64
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            imageBitMap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();

            encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
            Log.i("MAIN_APP", encoded);

            obtenerLink();
        }
    }
    //método para obtener link
    public void obtenerLink(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.imgur.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Image image = new Image();
        image.image = encoded;

        ImagenService service = retrofit.create(ImagenService.class);
        service.create(image).enqueue(new Callback<ImageResponse>() {
            @Override
            public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                Log.i("MAIN_APP", String.valueOf(response.code()));
                ImageResponse data = response.body();
                link = data.data.link; //obtenemos el link
                Log.i("MAIN_APP", new Gson().toJson(data));
            }

            @Override
            public void onFailure(Call<ImageResponse> call, Throwable t) {
                Log.i("MAIN_APP", "NO SE OBTUVO ENLACE");
            }
        });
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

    public void obtenerUbicacion(View view){
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1001);
        }else{
            locationStart();
        }
    }

    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Localizacion Local = new Localizacion();
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

        RegistrarMovimientoActivity registrarMovimientoActivity;
        public RegistrarMovimientoActivity getMainActivity() {
            return registrarMovimientoActivity;
        }

        public void setMainActivity(RegistrarMovimientoActivity mainActivity) {
            this.registrarMovimientoActivity = mainActivity;
        }

        @Override
        public void onLocationChanged(Location loc) {
            loc.getLatitude();
            loc.getLongitude();
            latitud = loc.getLatitude();
            longitud = loc.getLongitude();
            tvLatitud.setText("Latitud: " + latitud);
            tvLongitud.setText("Longitud: " + longitud);
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

    //métodoRegistrar
    public void registrarMovimiento(View view){
        String tipoM = etTipo.getText().toString();
        String montoM = etMonto.getText().toString();
        String motivoM = etMotivo.getText().toString();

        Movimiento movimiento = new Movimiento();
        movimiento.tipo = tipoM;
        movimiento.monto = montoM;
        movimiento.motivo = motivoM;
        movimiento.imagenURL = link;
        movimiento.latitud = latitud;
        movimiento.longitud = longitud;

        //llamamos al retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://635765f82712d01e1407281a.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovimientoService service = retrofit.create(MovimientoService.class);
        service.create(movimiento).enqueue(new Callback<Movimiento>() {
            @Override
            public void onResponse(Call<Movimiento> call, Response<Movimiento> response) {
                Log.i("MAIN_APP", String.valueOf(response.code()));
                Intent intent = new Intent(getApplicationContext(), ListarMovimientos.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<Movimiento> call, Throwable t) {
                Log.i("MAIN_APP", "Fallo al Guardar");
            }
        });
    }
}