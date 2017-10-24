package com.gzsoft.linternamaterialdesign;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

public class Configuraciones extends AppCompatActivity {
    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------VARIABLES GLOBALES
    public static boolean colorAleatorio = true;
    public static float brillo = 100.0F;
    public static int color = Color.WHITE;
    //----------------------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------COMPONENTES
    SeekBar skBrillo;
    TextView procentajeBrillo;

    //----------------------------------------------------------------------------------------------
    //--------------------------------------------------------------------------------------CREACION
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actv_configuracion);
        LeerDatos();
        setComponentes();
        skBrillo_Listener();
    }
    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------INICIALIZACIONES & COMPONENTES

    public void skBrillo_Listener() {
        final TextView procentajeBrillo = (TextView) findViewById(R.id.tvPorcentajeBrillo);
        skBrillo = (SeekBar) findViewById(R.id.sbBrillo);
        skBrillo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                GuardarDatos();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                brillo = progress;
                procentajeBrillo.setText(Integer.toString(progress) + '%');
            }
        });
    }

    public void CambiarColor(View v) {

    }

    //----------------------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------------FUNCIONES
    public static void Reset() {

    }

    public void LeerDatos() {
        SharedPreferences preferencias = getSharedPreferences("Configuracion", 0);
        brillo = preferencias.getFloat("brillo", 100.0F);

    }

    private void setComponentes() {
        //------------------------------------------------------------------------
        procentajeBrillo = (TextView) findViewById(R.id.tvPorcentajeBrillo);
        procentajeBrillo.setText(Float.toString((int) brillo) + '%');

        skBrillo = (SeekBar) findViewById(R.id.sbBrillo);
        skBrillo.setProgress((int) brillo);
        //------------------------------------------------------------------------
    }

    private void GuardarDatos() {
        SharedPreferences.Editor editor = getSharedPreferences("Configuracion", 0).edit();
        editor.putFloat("brillo", brillo);
        editor.commit();
    }

    public static void EsconderBarra(View v) {
        v.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }
}
