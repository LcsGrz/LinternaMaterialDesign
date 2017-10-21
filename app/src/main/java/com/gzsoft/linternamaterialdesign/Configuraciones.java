package com.gzsoft.linternamaterialdesign;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

public class Configuraciones {
    public static SharedPreferences sharedPref;
    public static boolean linterna = true;
    public static boolean colorAleatorio = true;
    public static float brillo = 1.0F;
    public static int color = Color.WHITE;

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
