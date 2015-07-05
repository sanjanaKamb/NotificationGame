package com.example.notificationgame;

public class SingletonScore {

	   private static SingletonScore instance = new SingletonScore();
	   public int highScore = 0;
	   public int score =0;
	   public String colorName = "";
	   public String colorIcon = "";
	   public Boolean newTimerSetup = true;
	   public int attemptNumber = 0;
	   public final int largestAttemptNumber =4;
	   
	   private SingletonScore(){}

	   public static SingletonScore getInstance(){
	      return instance;
	   }	  
	   
	   
}