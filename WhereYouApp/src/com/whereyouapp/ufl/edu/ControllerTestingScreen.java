//This class wil be used to test controller functions, it will consist of a screen with buttons that do various functions
package com.whereyouapp.ufl.edu;


import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.view.View;
import android.widget.Button;

public class ControllerTestingScreen extends Activity{

public static Context c;
protected void onCreate(Bundle savedInstanceState) {

    c=this;
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_controller_test_screen);

    Button button = (Button) findViewById(R.id.testbutton);
    button.getBackground().setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);
    OnClickListener buttonListener = new View.OnClickListener(){
        public void onClick(View arg0) {
            Toast.makeText(getBaseContext(), "inside the onclick", Toast.LENGTH_LONG).show();
            Controller.setServiceAlarm(getBaseContext(), true);
        }

    };
    button.setOnClickListener(buttonListener);

    Button notibutton = (Button) findViewById(R.id.notificationbutton);
    notibutton.getBackground().setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);
    OnClickListener nbuttonListener = new View.OnClickListener(){
        public void onClick(View arg0){
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getBaseContext());
            mBuilder.setSmallIcon(R.drawable.ic_launcher);
            mBuilder.setAutoCancel(true);
            mBuilder.setContentTitle("test");
            mBuilder.setContentText("test2");
            Intent resultIntent = new Intent (getBaseContext(), ControllerTestingScreen.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(getBaseContext());
            stackBuilder.addParentStack(ControllerTestingScreen.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mBuilder.setDefaults(Notification.DEFAULT_ALL);
            mNotificationManager.notify(0, mBuilder.build());
        }
    };
    notibutton.setOnClickListener(nbuttonListener);
}
}