package com.gzsoft.linternamaterialdesign;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.LinearLayout;

import java.util.Random;

public class LinternaPantalla extends AppCompatActivity implements GestureDetector.OnGestureListener {
    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------ATRIBUTOS GLOBALES
    WindowManager.LayoutParams parametroDiseño;
    GestureDetector detectorGestos;
    LinearLayout pantalla;
    CountDownTimer timer;
    CountDownTimer timerApagado;
    //----------------------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------CONSTRUCTOR
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actv_linterna_pantalla);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //No permito a la pantalla apagarse
        Configuraciones.EsconderBarra(findViewById(R.id.LinternaPantalla)); //Elimino la barra de navegacion
        //---------------Seteo brillo default de la configuracion
        parametroDiseño = getWindow().getAttributes();
        parametroDiseño.screenBrightness = Configuraciones.brillo / 100;
        getWindow().setAttributes(parametroDiseño);
        //-------------------------------------------------------
        detectorGestos = new GestureDetector(LinternaPantalla.this, LinternaPantalla.this);
        //---------------Seteo color default de la configuracion
        pantalla = (LinearLayout) findViewById(R.id.LinternaPantalla);
        pantalla.setBackgroundColor(Configuraciones.color);
        //-------------------------------------------------------
        if (Configuraciones.tiempoColor > 0){iniciarTimer();}
        if(getIntent().getExtras().getInt("apagar")>0){iniciarTimerApagado();}
    }

    @Override
    protected void onDestroy() {
        if(timer != null){
            timer.cancel();
        }
        super.onDestroy();
    }

    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------GESTOS SWIPE
    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        return detectorGestos.onTouchEvent(motionEvent);
    }

    //------------------------------MANTENER CLICK PARA SALIR---------------------------------------
    @Override
    public void onLongPress(MotionEvent motionEvent) {
        finish();
    }

    //--------------------------------------BRILLO - COLOR------------------------------------------
    @Override
    public boolean onFling(MotionEvent motionEvent1, MotionEvent motionEvent2, float v, float v1) {
        if ((motionEvent1.getY() - motionEvent2.getY() > 50) && Configuraciones.cambiarBrillo) {
            if ((motionEvent1.getY() - motionEvent2.getY() > 0) && (motionEvent1.getY() - motionEvent2.getY() < 1000)) {
                float brillo = motionEvent1.getY() - motionEvent2.getY();
                parametroDiseño.screenBrightness = brillo / 1000.0F;
            }
        } else if ((motionEvent2.getY() - motionEvent1.getY() > 50) && Configuraciones.cambiarBrillo) {
            if ((motionEvent2.getY() - motionEvent1.getY() > 0) && (motionEvent2.getY() - motionEvent1.getY() < 1000)) {
                float brillo = motionEvent2.getY() - motionEvent1.getY();
                parametroDiseño.screenBrightness = (1000 - brillo) / 1000.0F;
            }
        } else {
            if (Configuraciones.colorAleatorio) {
                Random aleatorio = new Random();
                pantalla.setBackgroundColor(Color.argb(255, aleatorio.nextInt(255), aleatorio.nextInt(255), aleatorio.nextInt(255)));
            }
        }
        getWindow().setAttributes(parametroDiseño);
        return true;
    }
    //----------------------------------------------------------------------------------------------
    public void iniciarTimer(){
        timer = new CountDownTimer((long)(Configuraciones.tiempoColor*1000.0F), 1) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                Random aleatorio = new Random();
                pantalla.setBackgroundColor(Color.argb(255, aleatorio.nextInt(255), aleatorio.nextInt(255), aleatorio.nextInt(255)));
                iniciarTimer();
            }
        }.start();
    }
    public void iniciarTimerApagado(){
        timerApagado = new CountDownTimer(getIntent().getExtras().getInt("apagar"), 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
             finish();
            }
        }.start();
    }
}