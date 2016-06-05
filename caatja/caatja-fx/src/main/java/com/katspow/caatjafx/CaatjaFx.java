package com.katspow.caatjafx;

import java.util.Random;
import java.util.Timer;

import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

import com.katspow.caatja.CAATKeyListener;
import com.katspow.caatja.KeyModifiers;
import com.katspow.caatja.core.CAAT;
import com.katspow.caatja.core.Caatja;
import com.katspow.caatja.core.image.CaatjaImageLoader;
import com.katspow.caatja.event.CAATKeyEvent;
import com.katspow.caatja.foundation.Director;
import com.katspow.caatja.modules.data.CaatjaScoreLoader;

public class CaatjaFx extends CAAT {

	public static Timer INTERVAL_ID = null;
	private static Scene fxScene;
    private static Pane fxPane;
    private static Stage fxStage;

	private CaatjaFx(CaatjaFxRootPanel caatjaFxRootPanel) {
		CAAT.NO_RAF = true;
		fxScene = caatjaFxRootPanel.getFxScene();
		fxPane = caatjaFxRootPanel.getFxPane();
	}

	public static void init(int width, int height, Stage primaryStage) {
		CaatjaFxRootPanel rootPanel = new CaatjaFxRootPanel(width, height);
		CaatjaFxWindow window = new CaatjaFxWindow();
		
        new Caatja(new CaatjaFxDate(), new CaatjaFxNavigator(),
				window, rootPanel, new CaatjaFxService(),
				new CaatjaFxEventManager(rootPanel), new CaatjaFxImageLoader(),
				new CaatjaFxPreloader(), new CaatjaFxStorage(), null, new CaatjaFx(rootPanel));

		primaryStage.setScene(rootPanel.getFxScene());
		show(primaryStage);
		
		fxStage = primaryStage;
		window.setOwner(primaryStage);
	}
	
	public static void init(int width, int height, Stage primaryStage, CaatjaImageLoader imageLoader) {
		
		CaatjaFxRootPanel rootPanel = new CaatjaFxRootPanel(width, height);
		CaatjaFxWindow window = new CaatjaFxWindow();
		
		new Caatja(new CaatjaFxDate(), new CaatjaFxNavigator(),
				window, rootPanel, new CaatjaFxService(),
				new CaatjaFxEventManager(rootPanel), imageLoader,
				new CaatjaFxPreloader(), new CaatjaFxStorage(), null, new CaatjaFx(rootPanel));

		primaryStage.setScene(rootPanel.getFxScene());
		show(primaryStage);
		
		fxStage = primaryStage;
		window.setOwner(primaryStage);
	}
	
	public static void init(int width, int height, Stage primaryStage, CaatjaImageLoader imageLoader, CaatjaScoreLoader scoreLoader) {
	    
	    CaatjaFxRootPanel rootPanel = new CaatjaFxRootPanel(width, height);
        CaatjaFxWindow window = new CaatjaFxWindow();
        
        new Caatja(new CaatjaFxDate(), new CaatjaFxNavigator(),
                window, rootPanel, new CaatjaFxService(),
                new CaatjaFxEventManager(rootPanel), imageLoader,
                new CaatjaFxPreloader(), new CaatjaFxStorage(), scoreLoader, new CaatjaFx(rootPanel));

        primaryStage.setScene(rootPanel.getFxScene());
        show(primaryStage);
        
        fxStage = primaryStage;
        window.setOwner(primaryStage);
	    
	}
	
	private static void show(Stage stage) {
		stage.show();
		System.out.println(stage.getHeight());
		System.out.println(stage.getWidth());
	}
	
	public static Pane getFxPane() {
	    return fxPane;
	}
	
	public static Stage getFxStage() {
        return fxStage;
    }

	@Override
	public void endLoop() {
		if (CAAT.NO_RAF) {
			if (INTERVAL_ID != null) {
				INTERVAL_ID.cancel();
			}
		} else {
			CAAT.ENDRAF = true;
		}

		CAAT.renderEnabled = false;

	}

	@Override
	public void loop(int fps) throws Exception {
		if (CAAT.renderEnabled) {
			return;
		}

		for (int i = 0, l = CAAT.director.size(); i < l; i++) {
			CAAT.director.get(i).timeline = Caatja.getTime();
		}

		if (fps <= 0) {
			CAAT.FPS = 60;
		}

		CAAT.renderEnabled = true;

		if (CAAT.NO_RAF) {

			// Timeline timeLine = new Timeline();
			// timeLine.setCycleCount(Timeline.INDEFINITE);
			// timeLine.getKeyFrames().add(
			// new KeyFrame(Duration.millis(500),
			// new EventHandler<ActionEvent>() {
			//
			// @Override
			// public void handle(ActionEvent evt) {
			// // context2.clearRect(0, 0, animation.getWidth(),
			// animation.getHeight());
			// // final int nextIndex = rnd.nextInt(images.size());
			// //// Image kajak = images.get(nextIndex);
			// // System.out.println("Draw image with index "+nextIndex);
			// // int rndX = rnd.nextInt(xRange);
			// // int rndY = rnd.nextInt(yRange);
			// // context2.drawImage(kajak, rndX, rndY); // TODO location
			//
			// double t = Caatja.getTime();
			//
			// for (Director d : director) {
			// try {
			// d.renderFrame();
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
			// }
			//
			// FRAME_TIME = t - SET_INTERVAL;
			//
			// if (CAAT.RAF != null) {
			// CAAT.REQUEST_ANIMATION_FRAME_TIME = Caatja.getTime() - CAAT.RAF;
			// }
			// CAAT.RAF = Caatja.getTime();
			//
			// SET_INTERVAL = t;
			//
			//
			// }
			// },
			// new KeyValue[0]) // don't use binding
			// );
			// timeLine.playFromStart();

			new AnimationTimer() {
				@Override
				public void handle(long now) {
					// double t = Caatja.getTime();
					double t = now;

					for (Director d : director) {
						try {
							d.renderFrame();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					FRAME_TIME = t - SET_INTERVAL;

					if (CAAT.RAF != null) {
						CAAT.REQUEST_ANIMATION_FRAME_TIME = Caatja.getTime()
								- CAAT.RAF;
					}
					CAAT.RAF = Caatja.getTime();

					SET_INTERVAL = t;
				}
			}.start();

		} else {
			Caatja.renderFrame(null);
		}

	}

	@Override
	public void globalEnableEvents() {
		// TODO Auto-generated method stub
		if (globalEventsEnabled) {
			return;
		}

		globalEventsEnabled = true;
		
		
		fxScene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
    	    @Override
    	    public void handle(KeyEvent event) {
    	    	
    	    	if (event.isShiftDown()) {
    	    		KEY_MODIFIERS.shift = true;
    	    	} else if (event.isControlDown()) {
    	    		KEY_MODIFIERS.control = true;
    	    	} else if (event.isAltDown()) {
    	    		KEY_MODIFIERS.alt = true;
    	    	} else {
    	    		
    	    		KeyCode code = event.getCode();
    	    		int key = code.impl_getCode();
    	    		
    	    		for (CAATKeyListener keyListener : keyListeners) {
						keyListener.call(new CAATKeyEvent(key, "down", new KeyModifiers(KEY_MODIFIERS.alt,
								KEY_MODIFIERS.control, KEY_MODIFIERS.shift)));
    	    		}
    	    	
    	    	}
    	    }
    	});
		
		fxScene.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
    	    @Override
    	    public void handle(KeyEvent event) {
    	    	
    	    	if (event.isShiftDown()) {
    	    		KEY_MODIFIERS.shift = false;
    	    	} else if (event.isControlDown()) {
    	    		KEY_MODIFIERS.control = false;
    	    	} else if (event.isAltDown()) {
    	    		KEY_MODIFIERS.alt = false;
    	    	} else {
    	    		
    	    		String character = event.getCharacter();
    	    		char charAt = character.charAt(0);
    	    		int key = (int) charAt;
    	    		
    	    		for (CAATKeyListener keyListener : keyListeners) {
						keyListener.call(new CAATKeyEvent(key, "up", new KeyModifiers(KEY_MODIFIERS.alt,
								KEY_MODIFIERS.control, KEY_MODIFIERS.shift)));
    	    		}
    	    	
    	    	}
    	    }
    	});
		
		// TODO Add resize event
		
		

	}

	@Override
	protected void requestAnimationFrame() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setCursor(String arg0) {
		// TODO Auto-generated method stub
	}

    @Override
    protected int randomInt(int arg0) {
        return new Random().nextInt(arg0);
    }

	@Override
	public void setFullScreen(boolean fullScreen) {
		letterbox(fxScene, fxPane);
		fxStage.setFullScreen(fullScreen);
	}
	
	private void letterbox(final Scene scene, final Pane contentPane) {
	    final double initWidth  = scene.getWidth();
	    final double initHeight = scene.getHeight();
	    final double ratio      = initWidth / initHeight;

	    SceneSizeChangeListener sizeListener = new SceneSizeChangeListener(scene, ratio, initHeight, initWidth, contentPane);
	    scene.widthProperty().addListener(sizeListener);
	    scene.heightProperty().addListener(sizeListener);
    }
	
	private static class SceneSizeChangeListener implements ChangeListener<Number> {
	    private final Scene scene;
	    private final double ratio;
	    private final double initHeight;
	    private final double initWidth;
	    private final Pane contentPane;

	    public SceneSizeChangeListener(Scene scene, double ratio, double initHeight, double initWidth, Pane contentPane) {
	      this.scene = scene;
	      this.ratio = ratio;
	      this.initHeight = initHeight;
	      this.initWidth = initWidth;
	      this.contentPane = contentPane;
	    }

	    @Override
	    public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
	      final double newWidth  = scene.getWidth();
	      final double newHeight = scene.getHeight();

	      double scaleFactor =
	          newWidth / newHeight > ratio
	              ? newHeight / initHeight
	              : newWidth / initWidth;

	      if (scaleFactor >= 1) {
	        Scale scale = new Scale(scaleFactor, scaleFactor);
	        scale.setPivotX(0);
	        scale.setPivotY(0);
	        scene.getRoot().getTransforms().setAll(scale);

	        contentPane.setPrefWidth (newWidth  / scaleFactor);
	        contentPane.setPrefHeight(newHeight / scaleFactor);
	        
	        
	      } else {
	        contentPane.setPrefWidth (Math.max(initWidth,  newWidth));
	        contentPane.setPrefHeight(Math.max(initHeight, newHeight));
	      }
	    }
	}

}
