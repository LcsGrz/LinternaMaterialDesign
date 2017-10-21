package com.gzsoft.linternamaterialdesign;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

public class Principal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actv_principal);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //Elimino la barra de navegacion
        VerificarPermisoCamara(); //Verifico si el permiso esta activado

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
    }

    //----------------------------------------------------------------------------------------------
    public void Encender(View v) {
        if (flash) {

        } else {
            Intent intent = new Intent(v.getContext(), LinternaPantalla.class);
            startActivityForResult(intent, 0);
        }
    }

    boolean flash = true;
    ImageView Switch;

    public void CambiarModo(View v) {
        Switch = (ImageView) findViewById(R.id.Switch_Linterna);
        flash = !flash;
        if (flash) {
            Switch.setImageResource(R.drawable.switch_flash_on);
        } else {
            Switch.setImageResource(R.drawable.switch_flash_off);
        }
    }
}
