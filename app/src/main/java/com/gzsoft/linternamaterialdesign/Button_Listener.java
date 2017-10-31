package com.gzsoft.linternamaterialdesign;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.view.View;

public class Button_Listener extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(10);

        Camera camara = Camera.open();
        Camera.Parameters parametrosCamara = camara.getParameters();
        parametrosCamara.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        camara.setParameters(parametrosCamara);
        camara.stopPreview();

        Principal.Rayo.setImageResource(R.drawable.rayo_apagado);
    }
}
