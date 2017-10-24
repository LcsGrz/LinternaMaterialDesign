package com.gzsoft.linternamaterialdesign;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class Principal extends AppCompatActivity {

    Camera camara;
    Camera.Parameters parametrosCamara;
    ImageView Switch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actv_principal);
        Configuraciones.EsconderBarra(findViewById(R.id.view_Principal)); //Elimino la barra de navegacion
        VerificarPermisoCamara(); //Verifico si el permiso esta activado
        PrepararCamara(); // Verifica si tiene camara y flash
        LeerDatos(); //Lee los datos guardados
    }

    @Override
    protected void onResume() {
        super.onResume();
        Configuraciones.EsconderBarra(findViewById(R.id.view_Principal)); //Elimino la barra de navegacion
    }

    //----------------------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------VERIFICAR PERMISO
    private void VerificarPermisoCamara() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 123);
            }
        }
    }

    boolean aceptoTarde = false;
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (123 == requestCode) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Por favor activa el permiso", Toast.LENGTH_LONG).show();
                VerificarPermisoCamara();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        Switch.setImageResource(R.drawable.switch_flash_on);
        flash = true;
        PrepararCamara();
    }

    //----------------------------------------------------------------------------------------------
    //------------------------------------------------------------COMPORTAMIENTO Y ENCENDER LINTERNA
    Boolean conProblemas = true;
    public void PrepararCamara() {
        Switch = (ImageView) findViewById(R.id.Switch_Linterna);

        if (getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            conProblemas = false;

            if(camara == null) {
                try {
                    camara = Camera.open();
                    parametrosCamara = camara.getParameters();
                }
                catch (Exception ex){
                    conProblemas = true;
                }
            }
        }
        if (conProblemas) { // si hubo problemas, desabilito los botones de flash de camara
            Toast.makeText(this,"Hubo problemas con la camara",Toast.LENGTH_LONG);
            Switch.setImageResource(R.drawable.switch_flash_off);
            flash = false;
        }
    }

    boolean encendido = false;
    ImageView Rayo;

    public void Encender(View v) {
        if (flash) {
            if(camara != null) {
                Rayo = (ImageView) findViewById(R.id.Rayo);
                encendido = !encendido;
                if (encendido) { //Enciendo
                    Rayo.setImageResource(R.drawable.rayo_prendido);

                    parametrosCamara.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    camara.setParameters(parametrosCamara);
                    camara.startPreview();
                } else { //Apago
                    Rayo.setImageResource(R.drawable.rayo_apagado);

                    parametrosCamara.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    camara.setParameters(parametrosCamara);
                    camara.stopPreview();
                }
            }
        } else {
            Intent intent = new Intent(v.getContext(), LinternaPantalla.class);
            startActivityForResult(intent, 0);
        }
    }

    boolean flash = true;

    public void CambiarModo(View v) {
        if (!conProblemas) { //Si no hubo problemas con la camara, me permite cambiar los tipos de linterna
            flash = !flash;
            if (flash) {
                Switch.setImageResource(R.drawable.switch_flash_on);
            } else {
                Switch.setImageResource(R.drawable.switch_flash_off);
            }
        }
    }
    //----------------------------------------------------------------------------------------------
    //------------------------------------------------------------COMPORTAMIENTO Y ENCENDER LINTERNA
    public void AbrirConfiguracion(View v){
        Intent intent = new Intent(v.getContext(), Configuraciones.class);
        startActivityForResult(intent, 0);
    }
    public void LeerDatos() {
        SharedPreferences preferencias = getSharedPreferences("Configuracion",0);
        Configuraciones.brillo = preferencias.getFloat("brillo", 100.0F);
    }
}



