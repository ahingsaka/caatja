package com.katspow.caatja.core.image;

public abstract class CaatjaImageLoaderCallback {
    
    public abstract void onFinishedLoading() throws Exception;
    
    public void onError(Exception e) {
    	e.printStackTrace();
    };

}
