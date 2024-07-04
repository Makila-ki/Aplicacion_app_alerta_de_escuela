package com.example.login_php;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class home extends AppCompatActivity {

    RequestQueue requestQueue;
    Button bton;
    ImageButton btoncie;

    EditText Pa, Pe,Pi;

    ArrayList<Alusu> listaUsuarios= new ArrayList<>();
    long ahora = System.currentTimeMillis();
    String info, nom, deviceiid, otnomb, otmatri, pdfnom, pdfmatri;
    Handler handler = new Handler();

    int papaleta=0;

    private static  final String CHANNEL_ID="canal";
    private PendingIntent pendingIntent;

    Date fe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        deviceiid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        setContentView(R.layout.activity_home);
        bton=findViewById(R.id.BtnPDF);
        btoncie=findViewById(R.id.Salidita);
        Pa=findViewById(R.id.EditRe);
        Pe = findViewById(R.id.Matri);
        Pi = findViewById(R.id.Nvato);
        Bundle resi = getIntent().getExtras();
        fe= new Date(ahora);
        info=resi.getString("mati");
        nom=resi.getString("santi");
        Pe.setText(info);
        Pi.setText(nom);
        Pi.setFocusable(false);
        Pi.setClickable(false);
        Pe.setFocusable(false);
        Pe.setClickable(false);
        ejecutarTarea();


        btoncie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cierre();

                home.super.onDestroy();
                SystemClock.sleep(1000);
                Intent intent=new Intent(home.this,login.class);
                startActivity(intent);
                onBackPressed();

            }
        });

        bton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generarPdf();
            }
        });
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){

            ShowNotification();
        }else{
            ShowNewNotification();
        }
    }

    private void ShowNotification() {
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "NEW", NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);
        ShowNewNotification();
    }

    private void ShowNewNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Existe Una Alerta")
                .setContentText("En el area: , El alumno: "+otnomb+ ", Con Matricula:"+otmatri+", Solicita una ayuda")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent);
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getApplicationContext());
        managerCompat.notify(1, builder.build());
    }

    private void setPendingIntent(Class<?> homeClass) {
        Intent intent = new Intent(this, homeClass);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(homeClass);
        stackBuilder.addNextIntent(intent);
        pendingIntent = stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void generarPdf() {
        try {
            Bitmap bitmap, bitmapEscala;
            String carpeta = "/archivospdf";
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + carpeta;

            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
                Toast.makeText(this, "CARPETA CREADA", Toast.LENGTH_SHORT).show();
            }

            File archivo = new File(dir, "usuarios.pdf");
            FileOutputStream fos = new FileOutputStream(archivo);

            Document documento = new Document();
            PdfWriter.getInstance(documento, fos);
            String descripcionText = Pa.getText().toString();
            documento.open();

            Paragraph titulo = new Paragraph(
                    "Reporte de Emergencia\n",
                    FontFactory.getFont("arial", 22, Font.BOLD, BaseColor.BLUE)
            );
            Paragraph matricula= new Paragraph(
                    "Matricula del Encargado: "+info+"\n",
                    FontFactory.getFont("arial", 15, Font.BOLD, BaseColor.RED)
            );

            Paragraph nombre= new Paragraph(
                    "Nombre del encargado: "+nom+"\n\n\n",
                    FontFactory.getFont("arial", 15, Font.BOLD, BaseColor.RED)
            );
            Paragraph nomest= new Paragraph(
                    "Nombre del Estudiante: "+pdfnom+"\n\n\n",
                    FontFactory.getFont("arial", 15, Font.BOLD, BaseColor.RED)
            );
            Paragraph matrienca= new Paragraph(
                    "Matricula del afectado: "+pdfmatri+"\n\n\n",
                    FontFactory.getFont("arial", 15, Font.BOLD, BaseColor.RED)
            );

            Paragraph cuerpo = new Paragraph(
                    ""+descripcionText+"\n\n",
                    FontFactory.getFont("arial", 12, Font.BOLD, BaseColor.BLACK)
            );

            Paragraph fecha = new Paragraph(
                    "Generado: "+fe+"\n\n",
                    FontFactory.getFont("arial", 12, Font.BOLD, BaseColor.BLACK)
            );

            documento.add(titulo);
            documento.add(matricula);
            documento.add(nombre);
            documento.add(nomest);
            documento.add(matrienca);
            documento.add(cuerpo);
            documento.add(fecha);

            documento.close();
            Toast.makeText(this, "PDF Generado Correctamente", Toast.LENGTH_SHORT).show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch ( DocumentException e) {
            e.printStackTrace();
        }
    }

    public void Cierre(){
        final String Clavve = deviceiid;
        StringRequest request = new StringRequest(Request.Method.POST, "https://conexteso.000webhostapp.com/eliminar.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.equalsIgnoreCase("Datos insertados")){

                            Toast.makeText(home.this, "No se puede cerrar sesion", Toast.LENGTH_SHORT).show();

                        }
                        else{
                            Toast.makeText(home.this, "Sesion Cerrada", Toast.LENGTH_SHORT).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(home.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<String,String>();

                params.put("cclavee",Clavve);

                return params;
            }
        };


        requestQueue = Volley.newRequestQueue(home.this);
        requestQueue.add(request);
    }
    private final int TIEMPO = 3000;

    public void ejecutarTarea() {
        handler.postDelayed(new Runnable() {
            public void run() {

                // función a ejecutar
                analisis(); // función para refrescar la ubicación del conductor, creada en otra línea de código
                if (papaleta==1){
                    createNotificationChannel();
                    Pe.setText(otmatri);
                    Pi.setText(otnomb);
                    pdfnom=otnomb;
                    pdfmatri=otmatri;
                    SystemClock.sleep(1000);
                    aleo();
                    papaleta=0;
                }else{
                    Toast.makeText(getApplicationContext(), "No hay ninguna alerta", Toast.LENGTH_SHORT).show();

                }

                handler.postDelayed(this, TIEMPO);

            }

        }, TIEMPO);

    }

    public void analisis(){
        String url = "https://conexteso.000webhostapp.com/verifi_alert.php?estad="+1+"";


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        String gat, per, ard;
                        Double mamut, tig;
                        gat=jsonObject.getString("nombre");
                        per=jsonObject.getString("matricula");
                        ard=jsonObject.getString("clave");
                        mamut=jsonObject.getDouble("loguitud");
                        tig = jsonObject.getDouble("latitud");
                        papaleta=jsonObject.getInt("estado");

                        listaUsuarios.add(new Alusu(""+gat+"", ""+per+"",mamut,tig, ""+ard+""));
                        otnomb=listaUsuarios.get(0).nombr;
                        otmatri=listaUsuarios.get(0).matricul;



                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    public void aleo(){
        final String esta = "0";
        StringRequest request = new StringRequest(Request.Method.POST, "https://conexteso.000webhostapp.com/actuali.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.equalsIgnoreCase("Datos insertados")){

                            Toast.makeText(home.this, "Sesion Cerrada", Toast.LENGTH_SHORT).show();

                        }
                        else{
                            Toast.makeText(home.this, "Alerta en proceso", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(home.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<String,String>();

                params.put("estadito",esta);

                return params;
            }
        };


        requestQueue = Volley.newRequestQueue(home.this);
        requestQueue.add(request);
    }


    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}