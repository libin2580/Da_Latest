package com.meridian.dateout.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.meridian.dateout.R;
import com.meridian.dateout.login.FrameLayoutActivity;

import org.json.JSONException;
import org.json.JSONObject;

import me.leolin.shortcutbadger.ShortcutBadger;

//import android.support.v4.content.LocalBroadcastManager;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    public static final int MESSAGE_NOTIFICATION_ID = 435345;
    private NotificationUtils notificationUtils;
    int counts=0;
    String deal_id;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void handleNotification(String message) {
        createNotification("DATE OUT NOTIFICATION", message);
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
//            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
//            pushNotification.putExtra("message", message);


            SharedPreferences sharedPreferencesns1 =getSharedPreferences("notification",MODE_PRIVATE);
            counts=  sharedPreferencesns1.getInt("notifcnt",0);
            counts=counts+1;
            System.out.println("countss1........."+counts);
            if(counts>0)
            {
                ShortcutBadger.applyCount(getApplicationContext(),counts);
                SharedPreferences preferences= getApplicationContext().getSharedPreferences("notification", MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putInt("notifcnt",counts);
                System.out.println("countss2"+counts);
                editor.commit();
            }
          //  LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
//            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
//            notificationUtils.playNotificationSound();
        }
        else {


            SharedPreferences sharedPreferencesns1 =getSharedPreferences("notification",MODE_PRIVATE);
            counts=  sharedPreferencesns1.getInt("notifcnt",0);
            counts=counts+1;
            System.out.println("countss2............"+counts);
            if(counts>0)
            {
                ShortcutBadger.applyCount(getApplicationContext(),counts);
                SharedPreferences preferences= getApplicationContext().getSharedPreferences("notification", MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putInt("notifcnt",counts);
                System.out.println("countss2"+counts);
                editor.commit();
            }
            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");

            String title = data.getString("title");
            String message = data.getString("message");
            boolean isBackground = data.getBoolean("is_background");
            String imageUrl = data.getString("image");
           deal_id = data.getString("deal_id");
            String timestamp = data.getString("timestamp");
            JSONObject payload = data.getJSONObject("payload");

            Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + message);
            Log.e(TAG, "isBackground: " + isBackground);
            Log.e(TAG, "payload: " + payload.toString());
            Log.e(TAG, "imageUrl: " + imageUrl);
            Log.e(TAG, "timestamp: " + timestamp);


            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
//                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
//                pushNotification.putExtra("message", message);

                createNotification("DATE OUT NOTIFICATION", message);
                SharedPreferences sharedPreferencesns1 =getSharedPreferences("notification",MODE_PRIVATE);
                counts=  sharedPreferencesns1.getInt("notifcnt",0);
                counts=counts+1;
                System.out.println("countss1"+counts);
                if(counts>0)
                {
                    ShortcutBadger.applyCount(getApplicationContext(),counts);
                    SharedPreferences preferences= getApplicationContext().getSharedPreferences("notification", MODE_PRIVATE);
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putInt("notifcnt",counts);
                    System.out.println("countss2"+counts);
                    editor.commit();
                }
//
//                SharedPreferences sharedPreferencesns1 =getSharedPreferences("notification",MODE_PRIVATE);
//                counts=  sharedPreferencesns1.getInt("notifcnt",0);
//                counts=counts+1;
//                System.out.println("countss1"+counts);
//                if(counts>0)
//                {
//                    ShortcutBadger.applyCount(getApplicationContext(),counts);
//                    SharedPreferences preferences= getApplicationContext().getSharedPreferences("notification", MODE_PRIVATE);
//                    SharedPreferences.Editor editor=preferences.edit();
//                    editor.putInt("notifcnt",counts);
//                    System.out.println("countss2"+counts);
//                    editor.commit();
//                }

               // LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
//                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
//                notificationUtils.playNotificationSound();
//            } else {
//                // app is in background, show the notification in notification tray
//                Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
//                resultIntent.putExtra("message", message);
//
//                // check for image attachment
//                if (TextUtils.isEmpty(imageUrl)) {
//                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
//                } else {
//                    // image is present, show notification with image
//                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
//                }
        }
        else {
                createNotification("DATE OUT NOTIFICATION", message);

                SharedPreferences sharedPreferencesns1 =getSharedPreferences("notification",MODE_PRIVATE);
                counts=  sharedPreferencesns1.getInt("notifcnt",0);
                counts=counts+1;
                System.out.println("countss1"+counts);
                if(counts>0)
                {
                    ShortcutBadger.applyCount(getApplicationContext(),counts);
                    SharedPreferences preferences= getApplicationContext().getSharedPreferences("notification", MODE_PRIVATE);
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putInt("notifcnt",counts);
                    System.out.println("countss2"+counts);
                    editor.commit();
                }

            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }

    // Creates notification based on title and body received

    private void createNotification(String title, String body) {


        /*Intent resultIntent = new Intent(this, FrameLayoutActivity.class);
        Intent broadcast = new Intent("broadcaster");
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcast);
// Because clicking the notification opens a new ("special") activity, there's
// no need to create an artificial back stack.
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );*/




        Intent notificationIntent = new Intent(this, FrameLayoutActivity.class);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        notificationIntent.putExtra("open", "account");
        notificationIntent.putExtra("deal_id", deal_id);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP |Intent.FLAG_ACTIVITY_NEW_TASK );
        NotificationManager nMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        PendingIntent intent2 = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);




        Context context = getBaseContext();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.dateout_icon)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setContentIntent(intent2)
                .setDefaults(NotificationCompat.DEFAULT_SOUND|NotificationCompat.DEFAULT_LIGHTS|NotificationCompat.DEFAULT_VIBRATE);;
        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        nMgr.notify(MESSAGE_NOTIFICATION_ID, mBuilder.build());
    }
}