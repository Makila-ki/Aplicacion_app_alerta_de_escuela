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
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class login extends AppCompatActivity {

    private LocationManager ubicacion;
    TextView log, alt, imeitxt;

    String pann, tonn, yinn, deviceId, stlog, stalt, nomme ="", mattri="";
    Button botongrande, botonchiquito, botonregis;

    private AudioManager audioManager;

    int volumenMaximo;

    double loog, aalt;

    ArrayList<Usuario> listaUsuarioss= new ArrayList<>();

    RequestQueue requestQueue;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        analisis();
        setContentView(R.layout.activity_main);
        botongrande = findViewById(R.id.Btn1Accion);
        botonchiquito = findViewById(R.id.Btn2Accion);
        botonregis = findViewById(R.id.BtnRepor);
        log = findViewById(R.id.TxtLong);
        alt = findViewById(R.id.TxtAlt);
        alt.setText("");
        log.setText("");

        localizacion();
        registro();





        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        volumenMaximo = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volumenMaximo, 0);

        botongrande.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volumenMaximo, 0);
                startService(new Intent(login.this, Servicio_del_boton.class));
                Envioale();
            }
        });
        botonchiquito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(new Intent(login.this, Servicio_del_boton.class));
            }
        });
        botonregis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login.this, Login_pa.class);
                startActivity(intent);
            }
        });
    }

    private void localizacion() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
            }, 10);
        }
        ubicacion = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location loc = ubicacion.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (ubicacion != null) {
            //log.setText(String.valueOf(loc.getLongitude()));
            //alt.setText(String.valueOf(loc.getLatitude()));
        }
    }

    private void registro() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        ubicacion = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        ubicacion.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, new miloca());

    }
    private class miloca implements LocationListener{

        @Override
        public void onLocationChanged(@NonNull Location location) {

            loog = location.getLongitude();
            aalt= location.getLatitude();
            stalt= ""+aalt;
            stlog= ""+loog;
            log.setText(stalt);
            alt.setText(stalt);
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

    public void analisis(){
        String url = "https://conexteso.000webhostapp.com/comproba.php?cclave="+deviceId+"";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        String pa, ma, hi, nie;
                        pa=jsonObject.getString("Nombre");
                        ma=jsonObject.getString("Matricula");
                        hi=jsonObject.getString("clave");
                        nie=jsonObject.getString("tipo");
                        listaUsuarioss.add(new Usuario(""+pa+"", ""+ma+"",""+hi+"",""+nie+""));
                        pann=listaUsuarioss.get(0).Matricula;
                        tonn=listaUsuarioss.get(0).Nombre;
                        yinn=listaUsuarioss.get(0).Tipo;
                        String mdmd="Encargado";
                        if(yinn.equals(mdmd)){
                            Bundle envio= new Bundle();
                            envio.putString("mati",pann);
                            envio.putString("santi",tonn);
                            Intent intent = new Intent(login.this, home.class);
                            intent.putExtras(envio);
                            startActivity(intent);
                        }else{
                           if(yinn.equals("Estudiante")){
                               Bundle envio= new Bundle();
                               envio.putString("mati",pann);
                               envio.putString("santi",tonn);
                               Intent intent = new Intent(login.this, home_estu.class);
                               intent.putExtras(envio);
                               startActivity(intent);

                           }else{
                               Toast.makeText(getApplicationContext(), "No hay esa opcion", Toast.LENGTH_SHORT).show();
                           }
                        }


                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "No hay un inicio de sesion previo", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue = Volley.newRequestQueue(login.this);
        requestQueue.add(jsonArrayRequest);
    }

    public void Envioale(){
        StringRequest request = new StringRequest(Request.Method.POST, "https://conexteso.000webhostapp.com/insertar_aler.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.equalsIgnoreCase("Datos insertados")) {

                        }
                        else{
                            Toast.makeText(login.this, "Datos insertados", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(login.this, "No se puede hacer este comando", Toast.LENGTH_SHORT).show();
            }
        }

        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<String,String>();

                params.put("nomb",nomme);
                params.put("matri", mattri);
                params.put("clav", deviceId);
                params.put("logg",stlog);
                params.put("altt", stalt);

                return params;
            }
        };


        requestQueue = Volley.newRequestQueue(login.this);
        requestQueue.add(request);

    }




}