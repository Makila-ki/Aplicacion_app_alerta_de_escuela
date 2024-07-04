package com.example.login_php;

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
import android.media.AudioManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class home_estu extends AppCompatActivity {

    RequestQueue requestQueue;

    private LocationManager ubicacionn;
    private AudioManager audioManagerr;

    int volumenMaximoo;

    TextView logg, altt;

    Button botongrandee, botonchiquitoo, botonpdf;
    ImageButton btonciee;

    String deviceiidd, nombrete, matriculin, lat, lon;
    Double slogg, saltt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        deviceiidd = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        setContentView(R.layout.activity_home_estu);
        botongrandee = findViewById(R.id.BtAccion);
        botonchiquitoo = findViewById(R.id.Bt2Accion);
        botonpdf = findViewById(R.id.BtRepor);
        btonciee=findViewById(R.id.Salidota);
        logg = findViewById(R.id.TxtLongg);
        altt = findViewById(R.id.TxtAltt);
        Bundle revo = getIntent().getExtras();
        nombrete=revo.getString("santi");
        matriculin=revo.getString("mati");
        altt.setText("");
        logg.setText("");
        localizacion();
        registro();
        audioManagerr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        volumenMaximoo = audioManagerr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        audioManagerr.setStreamVolume(AudioManager.STREAM_MUSIC, volumenMaximoo, 0);
        btonciee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cierree();
                Intent intent=new Intent(home_estu.this,login.class);
                startActivity(intent);
                finish();
            }
        });
        botongrandee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                audioManagerr.setStreamVolume(AudioManager.STREAM_MUSIC, volumenMaximoo, 0);
                startService(new Intent(home_estu.this, Servicio_del_boton.class));
                Envioalee();
            }
        });
        botonchiquitoo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(new Intent(home_estu.this, Servicio_del_boton.class));
            }
        });
        botonpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void localizacion() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
            }, 10);
        }
        ubicacionn = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location loc = ubicacionn.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (ubicacionn != null) {
            //log.setText(String.valueOf(loc.getLongitude()));
            //alt.setText(String.valueOf(loc.getLatitude()));
        }
    }

    private void registro() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        ubicacionn = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        ubicacionn.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, new home_estu.miloca());

    }
    private class miloca implements LocationListener {

        @Override
        public void onLocationChanged(@NonNull Location location) {
            lat = ""+location.getLatitude();
            lon= ""+location.getLongitude();
            slogg=location.getLongitude();
            saltt=location.getLatitude();

            logg.setText(lon);
            altt.setText(lat);
        }

        @Override
        public void onLocationChanged(@NonNull List<Location> locations) {
            LocationListener.super.onLocationChanged(locations);
        }

        @Override
        public void onFlushComplete(int requestCode) {
            LocationListener.super.onFlushComplete(requestCode);
        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {
            LocationListener.super.onProviderEnabled(provider);
        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {
            LocationListener.super.onProviderDisabled(provider);
        }
    }

    public void Cierree(){
        final String Clavvee = deviceiidd;
        StringRequest request = new StringRequest(Request.Method.POST, "https://conexteso.000webhostapp.com/eliminar.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.equalsIgnoreCase("Datos insertados")){

                            Toast.makeText(home_estu.this, "Sesion Cerrada", Toast.LENGTH_SHORT).show();

                        }
                        else{
                            Toast.makeText(home_estu.this, "No se puede cerrar sesion", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(home_estu.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<String,String>();

                params.put("cclavee",Clavvee);

                return params;
            }
        };


        requestQueue = Volley.newRequestQueue(home_estu.this);
        requestQueue.add(request);
    }
    public void Envioalee(){
        StringRequest request = new StringRequest(Request.Method.POST, "https://conexteso.000webhostapp.com/insertar_aler.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.equalsIgnoreCase("Datos insertados")) {

                        }
                        else{
                            Toast.makeText(home_estu.this, "Datos insertados", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(home_estu.this, "No se puede hacer este comando", Toast.LENGTH_SHORT).show();
            }
        }

        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<String,String>();

                params.put("nomb",nombrete);
                params.put("matri",matriculin);
                params.put("clav", deviceiidd);
                params.put("logg",lat);
                params.put("altt", lon);

                return params;
            }
        };


        requestQueue = Volley.newRequestQueue(home_estu.this);
        requestQueue.add(request);

    }
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}