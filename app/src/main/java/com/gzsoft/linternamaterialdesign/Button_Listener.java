package com.gzsoft.linternamaterialdesign;

import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

public class Button_Listener extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        /*NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(10);

        Camera camara = Camera.open();
        Camera.Parameters parametrosCamara = camara.getParameters();
        parametrosCamara.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        camara.setParameters(parametrosCamara);
        camara.stopPreview();

        Principal.Rayo.setImageResource(R.drawable.rayo_apagado);*/
        if (Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction())) {
            KeyEvent event = (KeyEvent)intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
            if (KeyEvent.KEYCODE_VOLUME_DOWN == event.getKeyCode()) {
                Toast.makeText(context, "Holi", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
