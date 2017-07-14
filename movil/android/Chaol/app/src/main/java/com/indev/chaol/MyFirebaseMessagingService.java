package com.indev.chaol;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.indev.chaol.utils.DateTimeUtils;

/**
 * Created by jvier on 05/07/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getNotification() != null) {

            String titulo = remoteMessage.getNotification().getTitle();
            String texto = remoteMessage.getNotification().getBody();

            Log.d(TAG, "NOTIFICACION RECIBIDA");
            Log.d(TAG, "Título: " + titulo);
            Log.d(TAG, "Texto: " + texto);

            //Opcional: mostramos la notificación en la barra de estado
            sendNotification(titulo, texto);
        }
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param title FCM message title received.
     * @param message message body received.
     */
    private void sendNotification(String title, String message) {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_local_shipping_white_48dp)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.launcher))
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Long ID = DateTimeUtils.getTimeStamp();

        notificationManager.notify(ID.intValue(), notificationBuilder.build());
    }
}
