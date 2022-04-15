package in.calibrage.teluguchurches.notificationfirebase;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class NotificationDismissedReceiver extends BroadcastReceiver {

    // This method is called when this BroadcastReceiver receives an Intent broadcast
    @Override
    public void onReceive(Context context, Intent intent) {

        //to store the notification ID
        int notificationId = intent.getExtras().getInt(".notificationId");

        //check here that notificationID is received or not?
        Log.e("notificationId", "notificationId : " + notificationId);
        /* Your code to handle the event here */
    }
}