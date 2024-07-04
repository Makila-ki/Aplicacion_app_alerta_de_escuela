package com.example.login_php;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
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
import java.util.Map;

public class Login_pa extends AppCompatActivity {

    RequestQueue requestQueue;
    ArrayList<Usuario> listaUsuarios= new ArrayList<>();

    EditText matri, contra;

    String str_mat,str_contra, pan, ton, yin, deviceIdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_pa);
        deviceIdd = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        matri = findViewById(R.id.etmatricula);
        contra = findViewById(R.id.etcontraseña);
    }
    public void Login(View view) {

        if(matri.getText().toString().equals("")){
            Toast.makeText(this, "Ingresa matricula", Toast.LENGTH_SHORT).show();
        }
        else if(contra.getText().toString().equals("")){
            Toast.makeText(this, "Ingresa contraseña", Toast.LENGTH_SHORT).show();
        }
        else{


            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Iniciando Sesion...");

            progressDialog.show();

            str_mat = matri.getText().toString().trim();
            str_contra = contra.getText().toString().trim();

            String url = "https://conexteso.000webhostapp.com/login.php?matricula="+str_mat+"&contrasena="+str_contra+"";


            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    progressDialog.dismiss();
                    JSONObject jsonObject = null;
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            jsonObject = response.getJSONObject(i);
                            String papa, mama, hijo, nieto;
                            papa=jsonObject.getString("matricula");
                            mama=jsonObject.getString("contrasena");
                            hijo=jsonObject.getString("nombre");
                            nieto=jsonObject.getString("tipo");
                            listaUsuarios.add(new Usuario(""+hijo+"", ""+papa+"",""+mama+"", ""+nieto+""));
                            pan=listaUsuarios.get(0).Matricula;
                            ton=listaUsuarios.get(0).Nombre;
                            yin=listaUsuarios.get(0).Tipo;
                            insertData();
                            if(yin.equals("Encargado")){
                                Bundle envio= new Bundle();
                                envio.putString("mati",pan);
                                envio.putString("santi",ton);
                                Intent intent = new Intent(Login_pa.this, home.class);
                                intent.putExtras(envio);
                                startActivity(intent);
                            }else {
                                if(yin.equals("Estudiante")){
                                    Bundle envio= new Bundle();
                                    envio.putString("mati",pan);
                                    envio.putString("santi",ton);
                                    Intent intent = new Intent(Login_pa.this, home_estu.class);
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
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Matricula y/o Contraseña incorrectos", Toast.LENGTH_SHORT).show();
                }
            });
            requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsonArrayRequest);
        }
    }

    public void moveToRegistration(View view) {
        startActivity(new Intent(getApplicationContext(), registrar.class));
        finish();
    }

    private void insertData() {

        final String nombree = ton;
        final String matriculaa = pan;
        final String clavee = deviceIdd;
        final String tipoo = yin;

        StringRequest request = new StringRequest(Request.Method.POST, "https://conexteso.000webhostapp.com/insert_ini.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if(response.equalsIgnoreCase("Datos insertados")){

                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Login_pa.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String,String> params = new HashMap<String,String>();

                    params.put("nombree",nombree);
                    params.put("matriculaa",matriculaa);
                    params.put("clavee",clavee);
                    params.put("tipoo", tipoo);
                    return params;
                }
            };

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }


}