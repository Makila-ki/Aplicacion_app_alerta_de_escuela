package com.example.login_php;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import java.util.HashMap;
import java.util.Map;

public class registrar extends AppCompatActivity {
    RequestQueue requestQueue;
    EditText txtName,txtmat,pass;
    Button btn_insert;

    String mamama, matmat, papaa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        txtName     = findViewById(R.id.ednombre);
        txtmat    = findViewById(R.id.etmatricula);
        pass = findViewById(R.id.etcontraseña);
        btn_insert = findViewById(R.id.btn_register);


        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                veri();
                insertData();
            }
        });
    }

    private void insertData() {

        final String nombre = txtName.getText().toString().trim();
        final String matricula = txtmat.getText().toString().trim();
        final String contrasena = pass.getText().toString().trim();
        final String tipo = papaa;



        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("cargando...");

        if(nombre.isEmpty()){


            txtName.setError("complete los campos");
            return;
        }
        else if(matricula.isEmpty()){

            txtmat.setError("complete los campos");
            return;
        }


        else{
            progressDialog.show();
            StringRequest request = new StringRequest(Request.Method.POST, "https://conexteso.000webhostapp.com/insertar.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if(response.equalsIgnoreCase("Datos insertados")){

                                Toast.makeText(registrar.this, "Datos insertados", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(registrar.this,Login_pa.class);
                                startActivity(intent);
                                progressDialog.dismiss();


                            }
                            else{
                                progressDialog.dismiss();
                                Toast.makeText(registrar.this, "Datos insertados", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(registrar.this, "No se puede hacer este comando", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String,String> params = new HashMap<String,String>();

                    params.put("nombre",nombre);
                    params.put("matricula",matricula);
                    params.put("contrasena",contrasena);
                    params.put("tipo",tipo);

                    return params;
                }
            };


            requestQueue = Volley.newRequestQueue(registrar.this);
            requestQueue.add(request);



        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public  void  login(View v){
        startActivity(new Intent(getApplicationContext(), Login_pa.class));
        finish();
    }

    public void veri(){
         mamama = txtName.getText().toString().trim();
         matmat = txtmat.getText().toString().trim();

        String url = "https://conexteso.000webhostapp.com/verifi.php?nnombre="+mamama+"&mmatricula="+matmat+"";


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        papaa=jsonObject.getString("Tipo");

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "No guarde papa", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "No te encuentras en la lista oficial, checa y verifica tus datos", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }


}