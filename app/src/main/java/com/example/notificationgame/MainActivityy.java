package com.example.notificationgame;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

public class MainActivityy extends Activity {

	private SingletonScore scoreObject = SingletonScore.getInstance();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	 @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public void createNotification(View view) {
		 
		 	scoreObject.score = 0;
			scoreObject.colorIcon = "";
			scoreObject.colorName = "";
			
		    // Prepare intent which is triggered if the
		    // notification is selected
		    Intent intent = new Intent(this, NotificationReceiverService.class);
		    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);	
		    PendingIntent pIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);		    

		    // Build notification
		    Notification noti = new Notification.Builder(this)
		        .setContentTitle("Notification Game")
		        .setContentText("To begin the game, press Start.").setSmallIcon(R.drawable.ng_logo)
		        .setContentIntent(pIntent)
		        .addAction(R.drawable.ng_logo, "Start", pIntent).build();
		    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		    
		    // hide the notification after its selected
		    noti.flags |= Notification.FLAG_AUTO_CANCEL;

		    notificationManager.notify(0, noti);

		  }
}
