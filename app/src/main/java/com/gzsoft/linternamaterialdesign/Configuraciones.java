package com.gzsoft.linternamaterialdesign;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

public class Configuraciones extends AppCompatActivity {
    public static SharedPreferences sharedPref;
    public static boolean linterna = true;
    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------VARIABLES GLOBALES
    public static boolean colorAleatorio = true;
    public static float brillo = 1.0F;
    public static int color = Color.WHITE;
    //----------------------------------------------------------------------------------------------
    //--------------------------------------------------------------------------------------CREACION
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actv_configuracion);

       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //Elimino la barra de navegacion
    }
    //----------------------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------------FUNCIONES
    public static void Reset(){

    }
    public static void setActivity(Activity actividad){
        sharedPref = actividad.getPreferences(Context.MODE_PRIVATE);
    }
    public static void Leer(){
        linterna = Boolean.parseBoolean(sharedPref.getString("linterna","true"));
    }
    public static void Guardar(){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("linterna","true");
        editor.commit();
    }
}
