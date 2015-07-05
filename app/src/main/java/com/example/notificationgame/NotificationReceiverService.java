package com.example.notificationgame;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class NotificationReceiverService extends Service{
	
	private final int MAX_TIMEOUT = 10;
	
	private NotificationReceiverService instance = this;
	private Boolean firstTime = true;
	private HashMap<String, Integer> colorsMap = new HashMap<String, Integer>();
	private LinkedList<String> colorsList = new LinkedList<String>();
	private Random rand = null;
	private final int LOW = 0;
	private final int HIGH = 6;
	private SingletonScore scoreObject = SingletonScore.getInstance();
	private Boolean endOfGame = false;
	private Handler handler = new Handler();	
	private Runnable runnable = new Runnable() {
		   @Override
		   public void run() {
			   Log.e("Msg","Time's up");
			   scoreObject.newTimerSetup = true;
			   endOfGame = true;
			  	Intent service = new Intent(instance, NotificationReceiverService.class);
				service.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			    startService(service);
			    
		   }
		};
	
	  @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	  @Override
	   public int onStartCommand(Intent intent, int flags, int startId) {		  
		  Intent timeService =null;
		  rand = new Random();
		  
		  if(endOfGame){
			  Log.e("Msg","end of game");
			  
			   endOfGame = false;
			   if(scoreObject.score>scoreObject.highScore){
				   scoreObject.highScore = scoreObject.score;
			   }
			    Intent intentNotify = new Intent(this, NotificationReceiverService.class);
			    intentNotify.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);	
			    PendingIntent pIntent = PendingIntent.getService(this, 0, intentNotify, PendingIntent.FLAG_CANCEL_CURRENT);		    
			    
			    Notification noti = new Notification.Builder(this)
		        .setContentTitle("Oops!! You ran out of time")
		        .setContentText("Score: "+scoreObject.score+"\nHigh Score: "+scoreObject.highScore).setSmallIcon(R.drawable.ng_logo)
		        .setContentIntent(pIntent)
		        .addAction(R.drawable.ng_logo, "Play Again", pIntent).build();
		    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			    // hide the notification after its selected
			    noti.flags |= Notification.FLAG_AUTO_CANCEL ;

			    notificationManager.notify(0, noti);
			    scoreObject.score = 0;
				scoreObject.colorIcon = "";
				scoreObject.colorName = "";
				scoreObject.attemptNumber=0;
			    return START_STICKY;
		  }
		  
		  if(firstTime){
			  initialSetUp();			  
			  firstTime=false;
		  }
		   	
		  //timeout = 10 seconds
		  if(scoreObject.newTimerSetup){
			handler.postDelayed(runnable, MAX_TIMEOUT*1000);
			scoreObject.newTimerSetup = false;
		  }
		  
		  //rig every Nth attempt
		  if(scoreObject.attemptNumber<1){
			  scoreObject.attemptNumber=rand.nextInt(scoreObject.largestAttemptNumber-LOW+1)+LOW;
			  Log.e("Msg","MAX "+scoreObject.attemptNumber);
		 }
		  Log.e("Msg","CURR "+scoreObject.attemptNumber);
		  
		  String colorIcon = colorsList.get(rand.nextInt(HIGH-LOW)+LOW);
		  int icon = colorsMap.get(colorIcon);
		  
		  String colorName = "";
		  
		  if(scoreObject.attemptNumber==1){
			  colorName = colorIcon;                        
		  }else{
			  colorName = colorsList.get(rand.nextInt(HIGH-LOW)+LOW);	 
		  }
		  scoreObject.attemptNumber--;
		  scoreObject.colorIcon = colorIcon;
		  scoreObject.colorName = colorName;	  
		  
		   
			Intent yesIntent = new Intent(this, YesService.class);	
			yesIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);	
			PendingIntent yesPIntent = PendingIntent.getService(this, 0, yesIntent, PendingIntent.FLAG_CANCEL_CURRENT);
			
			Intent noIntent = new Intent(this, NoService.class);
			noIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);	
			PendingIntent noPIntent = PendingIntent.getService(this, 0, noIntent, PendingIntent.FLAG_CANCEL_CURRENT);
		    
		    Notification noti = new Notification.Builder(this)
		        .setContentTitle(colorName)
		        .setContentText("").setSmallIcon(icon)
		        .setContentIntent(null)
		        .addAction(R.drawable.yes_mark, "Yes", yesPIntent)
		        .addAction(R.drawable.no_mark, "No", noPIntent).build();
		    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		    // hide the notification after its selected
		    noti.flags |= Notification.FLAG_AUTO_CANCEL ;

		    notificationManager.notify(0, noti);
		  
	      return START_STICKY;
	   }
	private void initialSetUp() {
		Log.e("Msg","Initial SetUp");
		colorsMap.put("Black", R.drawable.black);
		colorsMap.put("Blue",R.drawable.blue);
		colorsMap.put("Red", R.drawable.red);
		colorsMap.put("Green",R.drawable.green);
		colorsMap.put("White", R.drawable.white);
		colorsMap.put("Yellow",R.drawable.yellow);
		
		colorsList.add("Black");
		colorsList.add("Blue");
		colorsList.add("Red");
		colorsList.add("Green");
		colorsList.add("White");
		colorsList.add("Yellow");		
		
	}
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
