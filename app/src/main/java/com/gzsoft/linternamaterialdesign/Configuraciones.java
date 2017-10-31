package com.gzsoft.linternamaterialdesign;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

public class Configuraciones extends AppCompatActivity {
    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------VARIABLES GLOBALES
    public static boolean colorAleatorio = true;
    public static boolean cambiarBrillo = true;
    public static boolean EncenderIniciarPantalla = false;
    public static boolean EncenderIniciarFlash = false;
    public static float brillo = 100.0F;
    public static float tiempoColor = 0.0F;
    public static int color = Color.WHITE;
    public static int intermitencia= 0;
    //----------------------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------COMPONENTES
    SeekBar skBrillo;
    SeekBar skTiempoColor;

    TextView tvProcentajeBrillo;
    TextView tvTiempoColor;

    CheckBox cbColorAleatorio;
    CheckBox cbCambiarBrillo;

    CheckBox cbEncenderIniciarPantalla;
    CheckBox cbEncenderIniciarFlash;

    LinearLayout llCuadraditoColor;

    //----------------------------------------------------------------------------------------------
    //--------------------------------------------------------------------------------------CREACION
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actv_configuracion);
        LeerDatos();
        setComponentes();
        skBrillo_Listener();
        skTiempoColor_Listener();
    }

    @Override
    protected void onDestroy() {
        GuardarDatos();
        super.onDestroy();
    }
    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------INICIALIZACIONES & COMPONENTES

    public void skBrillo_Listener() {
        skBrillo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                brillo = progress;
                tvProcentajeBrillo.setText(Integer.toString(progress) + '%');
            }
        });
    }

    public void skTiempoColor_Listener() {
        skTiempoColor.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tiempoColor = progress / 1000.0f;
                tvTiempoColor.setText(Float.toString(tiempoColor) + " Segs");
            }
        });
    }

    public void CambiarColor(View v) {
        ColorPickerDialogBuilder
                .with(this)
                .setTitle("Elije un color")
                .initialColor(0xffffffff)
                .showAlphaSlider(false)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(8)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                    }
                }).setPositiveButton("Acpetar", new ColorPickerClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                color = selectedColor;
                llCuadraditoColor.setBackgroundColor(selectedColor);
            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).build().show();
    }

    public void setCbCambiarBrillo(View v) {
        cambiarBrillo = !cambiarBrillo;
    }

    public void setCbColorAleatorio(View v) {
        colorAleatorio = !colorAleatorio;
    }

    public void setCbEncenderIniciarPantalla(View v) {
        EncenderIniciarPantalla = !EncenderIniciarPantalla;
    }

    public void setCbEncenderIniciarFlash(View v) {
        EncenderIniciarFlash = !EncenderIniciarFlash;
    }

    //----------------------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------------FUNCIONES
    public void Reset() {

        GuardarDatos();
        setComponentes();
    }

    public void LeerDatos() {
        SharedPreferences preferencias = getSharedPreferences("Configuracion", 0);
        color = preferencias.getInt("color", -1);
        brillo = preferencias.getFloat("brillo", 100.0F);
        tiempoColor = preferencias.getFloat("tiempoColor", 0.0F);
        colorAleatorio = preferencias.getBoolean("colorAleatorio", true);
        cambiarBrillo = preferencias.getBoolean("cambiarBrillo", true);
        EncenderIniciarPantalla = preferencias.getBoolean("EncenderIniciarPantalla", false);
        EncenderIniciarFlash = preferencias.getBoolean("EncenderIniciarFlash", false);
    }

    private void setComponentes() {
        //------------------------------------------------------------------------
        tvProcentajeBrillo = (TextView) findViewById(R.id.tvPorcentajeBrillo);
        tvProcentajeBrillo.setText(Float.toString((int) brillo) + '%');

        skBrillo = (SeekBar) findViewById(R.id.sbBrillo);
        skBrillo.setProgress((int) brillo);
        //------------------------------------------------------------------------
        tvTiempoColor = (TextView) findViewById(R.id.tvTiempoColor);
        tvTiempoColor.setText(Float.toString(tiempoColor) + " Segs");

        skTiempoColor = (SeekBar) findViewById(R.id.skTiempoColor);
        skTiempoColor.setProgress((int) (tiempoColor * 1000.0F));
        //------------------------------------------------------------------------
        cbColorAleatorio = (CheckBox) findViewById(R.id.cbColorAleatorio);
        cbColorAleatorio.setChecked(colorAleatorio);
        //------------------------------------------------------------------------
        cbCambiarBrillo = (CheckBox) findViewById(R.id.cbCambiarBrillo);
        cbCambiarBrillo.setChecked(cambiarBrillo);
        //------------------------------------------------------------------------
        cbEncenderIniciarPantalla = (CheckBox) findViewById(R.id.cbEncenderIniciarPantalla);
        cbEncenderIniciarPantalla.setChecked(EncenderIniciarPantalla);
        //------------------------------------------------------------------------
        cbEncenderIniciarFlash = (CheckBox) findViewById(R.id.cbEncenderIniciarFlash);
        cbEncenderIniciarFlash.setChecked(EncenderIniciarFlash);
        //------------------------------------------------------------------------
        llCuadraditoColor = (LinearLayout) findViewById(R.id.cuadraditoColor);
        llCuadraditoColor.setBackgroundColor(color);
    }

    private void GuardarDatos() {
        SharedPreferences.Editor editor = getSharedPreferences("Configuracion", 0).edit();
        editor.putInt("color", color);
        editor.putFloat("brillo", brillo);
        editor.putFloat("tiempoColor", tiempoColor);
        editor.putBoolean("colorAleatorio", colorAleatorio);
        editor.putBoolean("cambiarBrillo", cambiarBrillo);
        editor.putBoolean("EncenderIniciarPantalla", EncenderIniciarPantalla);
        editor.putBoolean("EncenderIniciarFlash", EncenderIniciarFlash);
        editor.commit();
    }

    public static void EsconderBarra(View v) {
        v.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

}
