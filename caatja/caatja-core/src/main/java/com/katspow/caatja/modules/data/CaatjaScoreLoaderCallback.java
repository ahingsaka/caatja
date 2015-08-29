package com.katspow.caatja.modules.data;

public abstract class CaatjaScoreLoaderCallback {
	
    public abstract void onFinishedLoading(CaatjaHighScores highScores);
    
    public void onError(Exception e) {
    	e.printStackTrace();
    };
    
}
