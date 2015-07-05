package com.example.notificationgame;
import java.util.ArrayList;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
public class NoService extends Service{
	
	private SingletonScore scoreObject = SingletonScore.getInstance();
	public ArrayList<Double> scoreTracker = null;
	
	@SuppressWarnings("unchecked")
	@Override
	   public int onStartCommand(Intent intent, int flags, int startId) {
		Log.e("Msg","NO");			
		
		Intent notificationService = new Intent(this, NotificationReceiverService.class);
		notificationService.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		String colorIcon = scoreObject.colorIcon;
		String colorName = scoreObject.colorName;
		
		if(colorIcon.equalsIgnoreCase(colorName)){
			scoreObject.score -= 100;
		}else{
			scoreObject.score += 100;
		}		
		
		Log.e("Msg",Integer.toString(scoreObject.score));
		startService(notificationService);
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
