package com.gzsoft.linternamaterialdesign;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

public class Principal extends AppCompatActivity {

    Camera camara;
    Camera.Parameters parametrosCamara;
    ImageView Switch;

    NotificationManager notiManager;
    RemoteViews remoteV;
    NotificationCompat.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actv_principal);
        Configuraciones.EsconderBarra(findViewById(R.id.view_Principal)); //Elimino la barra de navegacion
        VerificarPermisoCamara(); //Verifico si el permiso esta activado
        PrepararCamara(); // Verifica si tiene camara y flash
        LeerDatos(); //Lee los datos guardados
        PrenderNotificacion();
    }

    boolean primeraVez = true;

    @Override
    protected void onResume() {
        super.onResume();
        Configuraciones.EsconderBarra(findViewById(R.id.view_Principal)); //Elimino la barra de navegacion
        if (!encendido && primeraVez) {
            EncenderIniciar();//Prende la linterna al iniciar si esta activado
            primeraVez = false;
        }
    }

    @Override
    protected void onDestroy() {
        if (notiManager != null) {
            notiManager.cancel(10);
        }
        super.onDestroy();
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

            if (camara == null) {
                try {
                    camara = Camera.open();
                    parametrosCamara = camara.getParameters();
                } catch (Exception ex) {
                    conProblemas = true;
                }
            }
        }
        if (conProblemas) { // si hubo problemas, desabilito los botones de flash de camara
            Toast.makeText(this, "Hubo problemas con la camara", Toast.LENGTH_LONG);
            Switch.setImageResource(R.drawable.switch_flash_off);
            flash = false;
        }
    }

    boolean encendido = false;
    ImageView Rayo;

    public void Encender(View v) {
        if (flash) {
            try {
                if (camara != null) {
                    Rayo = (ImageView) findViewById(R.id.Rayo);
                    encendido = !encendido;
                    prendioConFlash = !prendioConFlash;
                    if (encendido) { //Enciendo
                        Rayo.setImageResource(R.drawable.rayo_prendido);

                        parametrosCamara.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                        camara.setParameters(parametrosCamara);
                        camara.startPreview();

                        notiManager.notify(10, builder.build());
                    } else { //Apago
                        Rayo.setImageResource(R.drawable.rayo_apagado);

                        parametrosCamara.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                        camara.setParameters(parametrosCamara);
                        camara.stopPreview();

                        notiManager.cancel(10);
                        if (Timer != null) {
                            Timer.cancel();
                            tvTimer.setText("MM:SS");
                            Timer = null;
                        }
                    }
                }
            } catch (Exception ex) {
                camara = null;
                PrepararCamara();
            }
        } else {
            Intent intent = new Intent(findViewById(R.id.view_Principal).getContext(), LinternaPantalla.class);
            intent.putExtra("apagar", ((minutos * 60 + segundos) * 1000));
            startActivity(intent);

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
    //-------------------------------------------------------------------------------------FUNCIONES
    public void AbrirConfiguracion(View v) {
        Intent intent = new Intent(v.getContext(), Configuraciones.class);
        startActivityForResult(intent, 0);
    }

    public void LeerDatos() {
        SharedPreferences preferencias = getSharedPreferences("Configuracion", 0);
        Configuraciones.color = preferencias.getInt("color", -1);
        Configuraciones.brillo = preferencias.getFloat("brillo", 100.0F);
        Configuraciones.tiempoColor = preferencias.getFloat("tiempoColor", 0.0F);
        Configuraciones.colorAleatorio = preferencias.getBoolean("colorAleatorio", true);
        Configuraciones.cambiarBrillo = preferencias.getBoolean("cambiarBrillo", true);
        Configuraciones.EncenderIniciarPantalla = preferencias.getBoolean("EncenderIniciarPantalla", false);
        Configuraciones.EncenderIniciarFlash = preferencias.getBoolean("EncenderIniciarFlash", false);
    }

    public void EncenderIniciar() {
        if (Configuraciones.EncenderIniciarFlash) {
            Encender(null);
        }
        if (Configuraciones.EncenderIniciarPantalla) {
            Switch.setImageResource(R.drawable.switch_flash_off);
            flash = false;
            Encender(null);
        }
    }

    NumberPicker npMinutos;
    NumberPicker npSegundos;
    int minutos = 0;
    int segundos = 0;
    AlertDialog.Builder mBuild;
    AlertDialog dx;
    View TimerPersonalizado;
    TextView tvTimer;
    CountDownTimer Timer;
    boolean prendioConFlash = false;

    public void TimerFlash(View v) {
        tvTimer = (TextView) findViewById(R.id.tvTimer);
        mBuild = new AlertDialog.Builder(Principal.this);
        TimerPersonalizado = getLayoutInflater().inflate(R.layout.timer_personalizado, null);
        npMinutos = TimerPersonalizado.findViewById(R.id.npMinute);
        npSegundos = TimerPersonalizado.findViewById(R.id.npSecond);

        npMinutos.setMaxValue(14);
        npSegundos.setMaxValue(59);
        npMinutos.setMinValue(00);
        npSegundos.setMinValue(00);

        npMinutos.setWrapSelectorWheel(false);
        npSegundos.setWrapSelectorWheel(false);
        Configuraciones.EsconderBarra(TimerPersonalizado);
        mBuild.setTitle("Temporizador")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        minutos = npMinutos.getValue();
                        segundos = npSegundos.getValue();
                        if (segundos < 10) {
                            tvTimer.setText(minutos + ":0" + segundos);
                        } else {
                            tvTimer.setText(minutos + ":" + segundos);
                        }
                        Configuraciones.EsconderBarra(findViewById(R.id.view_Principal));
                        if (Timer != null) {
                            Timer.cancel();
                        }
                        Timer = null;
                        int tiempo = (minutos * 60 + segundos) * 1000;
                        if (!encendido && tiempo > 0) {
                            Encender(null);
                        }
                        if (tiempo > 0) {
                            Timer = new CountDownTimer(tiempo, 1000) {

                                public void onTick(long millisUntilFinished) {
                                    if (segundos == 0) {
                                        if (minutos > 0) {
                                            minutos--;
                                        }
                                        segundos = 59;
                                    }
                                    segundos--;

                                    if (segundos < 10) {
                                        tvTimer.setText(minutos + ":0" + segundos);
                                    } else {
                                        tvTimer.setText(minutos + ":" + segundos);
                                    }
                                }

                                public void onFinish() {
                                    tvTimer.setText("MM:SS");
                                    minutos = 0;
                                    segundos = 0;
                                    if (prendioConFlash) {
                                        flash = true;
                                        Encender(null);
                                        flash = false;
                                    }
                                }
                            }.start();
                        } else {
                            tvTimer.setText("MM:SS");
                        }
                    }
                })
                .setCancelable(false)
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Configuraciones.EsconderBarra(findViewById(R.id.view_Principal));
                    }
                }).setView(TimerPersonalizado);
        dx = mBuild.create();
        dx.show();
    }

    public void PrenderNotificacion() {
        notiManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        remoteV = new RemoteViews(getPackageName(), R.layout.notificacion);
        remoteV.setImageViewResource(R.id.noti_icon, R.drawable.rayo_notif);
        remoteV.setTextViewText(R.id.txtTitle, "Linterna Activa");
        //-----------------------------------------------------------------------------------------------------------------------------
        final int notification_id = (int) System.currentTimeMillis();
        Intent button_intent = new Intent("button_click");
        button_intent.putExtra("dato_a_pasar", 10);
        PendingIntent p_intent = PendingIntent.getBroadcast(this, 123, button_intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteV.setOnClickPendingIntent(R.id.btnDesactivar, p_intent);
        //----------------------------------------------------------------------------------------------------------------------------
        Intent notification_1 = new Intent(getApplicationContext(), Principal.class);
        PendingIntent pending = PendingIntent.getActivity(getApplicationContext(), 0, notification_1, 0);
        builder = new NotificationCompat.Builder(getApplicationContext());
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(false)
                .setCustomBigContentView(remoteV)
                .setContentIntent(pending);
    }

}



