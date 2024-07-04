package com.example.login_php;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;


public class Servicio_del_boton extends Service {


    private static final String TAG = "BackgroundSoundService";
    MediaPlayer reproductor;
    @Override
    public void onCreate(){
        super.onCreate();
        reproductor = MediaPlayer.create(this,R.raw.alertadefi);
        Log.i( TAG, "onCreate()");

    }
    @Override
    public int onStartCommand(Intent i, int flags, int idArranque){
        Log.i( TAG, "Servicio reiniciado");

            reproductor.start();
        return Service.START_STICKY;
    }
    @Override
    public void onDestroy(){
        Toast.makeText(this, "Alerta detenida", Toast.LENGTH_SHORT).show();
        reproductor.stop();
        Log.i( TAG, "Servicio destruido");
        super.onDestroy();
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.i(TAG, "onBind()" );
        return null;

    }
}