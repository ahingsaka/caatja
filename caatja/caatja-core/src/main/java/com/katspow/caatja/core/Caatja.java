package com.katspow.caatja.core;

import com.katspow.caatja.core.canvas.CaatjaCanvas;
import com.katspow.caatja.core.canvas.CaatjaService;
import com.katspow.caatja.core.event.CaatjaEventManager;
import com.katspow.caatja.core.image.CaatjaImageLoader;
import com.katspow.caatja.core.image.CaatjaPreloader;
import com.katspow.caatja.core.interfaces.CaatjaDate;
import com.katspow.caatja.core.interfaces.CaatjaNavigator;
import com.katspow.caatja.core.interfaces.CaatjaRootPanel;
import com.katspow.caatja.core.interfaces.CaatjaStorage;
import com.katspow.caatja.core.interfaces.CaatjaWindow;
import com.katspow.caatja.foundation.Director;
import com.katspow.caatja.modules.data.CaatjaHighScores;
import com.katspow.caatja.modules.data.CaatjaScoreLoader;
import com.katspow.caatja.modules.data.CaatjaScoreLoaderCallback;
import com.katspow.caatja.modules.loading.CaatjaDefaultLoading;
import com.katspow.caatja.modules.loading.CaatjaLoading;

/**
 *  Main class of the project, objects should be injected within constructor.
 */
public class Caatja {

    private static CaatjaDate caatjaDate;
    private static CaatjaNavigator caatjaNavigator;
    private static CaatjaWindow caatjaWindow;
    private static CaatjaRootPanel caatjaRootPanel;
    private static CaatjaService caatjaService;
    private static CaatjaEventManager caatjaEventManager;
    private static CaatjaImageLoader caatjaImageLoader;
    private static CaatjaPreloader caatjaPreloader;
    private static CaatjaStorage caatjaStorage;
    private static CaatjaScoreLoader caatjaScoreLoader;
    private static CAAT caat;
    
    private static CaatjaLoading caatjaLoading;

    public Caatja(CaatjaDate date, CaatjaNavigator navigator, CaatjaWindow window, CaatjaRootPanel rootPanel,
            CaatjaService service, CaatjaEventManager manager, CaatjaImageLoader loader, CaatjaPreloader preloader, 
            CaatjaStorage storage, CaatjaScoreLoader scoreLoader, CAAT thecaat) {
        
        caatjaDate = date;
        caatjaNavigator = navigator;
        caatjaWindow = window;
        caatjaRootPanel = rootPanel;
        caatjaService = service;
        caatjaEventManager = manager;
        caatjaImageLoader = loader;
        caatjaPreloader = preloader;
        caatjaStorage = storage;
        caatjaScoreLoader = scoreLoader;
        caat = thecaat;
        
        // Creates a default loading effect
        caatjaLoading = new CaatjaDefaultLoading();
    }

    public static double getTime() {
        return caatjaDate.getTime();
    }

    // NAVIGATOR METHODS
    public static String getUserAgent() {
        return caatjaNavigator.getUserAgent();
    }

    public static String getPlatform() {
        return caatjaNavigator.getPlatform();
    }

    public static String getAppVersion() {
        return caatjaNavigator.getAppVersion();
    }

    // WINDOW METHODS
    public static void alert(String text) {
        caatjaWindow.alert(text);
    }
    
    public static int getClientHeight() {
        return caatjaWindow.getClientHeight();
    }

    public static int getClientWidth() {
        return caatjaWindow.getClientWidth();
    }
    
    public static void setFullScreen(boolean fullScreen) {
    	caat.setFullScreen(fullScreen);
    }
    
    public static boolean confirm(String msg) {
        return caatjaWindow.confirm(msg);
    }
    
    public static void redirect(String url) {
        caatjaWindow.redirect(url);
    }

    // ROOTPANEL METHODS
    public static void addCanvas(CaatjaCanvas canvas) {
        caatjaRootPanel.addCanvas(canvas);
    }
    
    public static void addCanvas(CaatjaCanvas canvas, int x, int y) {
        caatjaRootPanel.addCanvas(canvas, x, y);
    }
    
    // SERVICE
    public static CaatjaCanvas createCanvas() {
    	return caatjaService.createCanvas();
    }
    
    public static CaatjaCanvas createCanvas(int width, int height) {
    	return caatjaService.createCanvas(width, height);
    }
    
    public static CaatjaEventManager getCaatjaEventManager() {
        return caatjaEventManager;
    }
    
    // IMAGE LOADING
    public static CaatjaImageLoader getCaatjaImageLoader() {
        return caatjaImageLoader;
    }
    
    public static CaatjaPreloader getCaatjaImagePreloader() {
        return caatjaPreloader;
    }
    
    // STORAGE
    public static CaatjaStorage getStorage() {
    	return caatjaStorage;
    }
    
    public static CAAT getCaat() {
    	return caat;
    }

	public static void loop(int fps) throws Exception {
		caat.loop(fps);
	}

	public static void setCursor(String value) {
		caat.setCursor(value);
	}

	public static void renderFrame(Double value) throws Exception {
		caat.renderFrame(value);
	}

	public static void registerDirector(Director director) {
		caat.registerDirector(director);
	}
	
	public static int randomInt(int maxValue) {
	    return caat.randomInt(maxValue);
	}
	
	public static void setLoading(CaatjaLoading caatjaLoading) {
        Caatja.caatjaLoading = caatjaLoading;
    }
	
	public static CaatjaLoading getLoading() {
	    return Caatja.caatjaLoading;
	}
	
	/**
	 * By passing scores, we update scores
	 * @param scores
	 * @param callback
	 */
	public static void getHighScores(CaatjaHighScores scores, CaatjaScoreLoaderCallback callback) {
	    caatjaScoreLoader.loadScores(scores, callback);
	}
	
}