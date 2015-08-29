package com.katspow.caatjagwt.client;

import com.google.gwt.animation.client.AnimationScheduler;
import com.google.gwt.animation.client.AnimationScheduler.AnimationCallback;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.RootPanel;
import com.katspow.caatja.CAATKeyListener;
import com.katspow.caatja.KeyModifiers;
import com.katspow.caatja.core.CAAT;
import com.katspow.caatja.core.Caatja;
import com.katspow.caatja.core.image.CaatjaImageLoader;
import com.katspow.caatja.event.CAATKeyEvent;
import com.katspow.caatja.foundation.Director;
import com.katspow.caatja.foundation.ResizeListener;
import com.katspow.caatja.modules.data.CaatjaScoreLoader;

public class CaatjaGwt extends CAAT {
    
    private CaatjaGwt() {
        
    }
    
    public static void init() {
        new Caatja(new CaatjaGwtDate(), new CaatjaGwtNavigator(), new CaatjaGwtWindow(), new CaatjaGwtRootPanel(),
                new CaatjaGwtService(), new CaatjaGwtEventManager(), new CaatjaGwtImageLoader(),
                new CaatjaGwtPreloader(), new CaatjaGwtStorage(), null, new CaatjaGwt());
    }
    
    public static void init(CaatjaImageLoader caatjaImageLoader) {
        new Caatja(new CaatjaGwtDate(), new CaatjaGwtNavigator(), new CaatjaGwtWindow(), new CaatjaGwtRootPanel(),
                new CaatjaGwtService(), new CaatjaGwtEventManager(), caatjaImageLoader,
                new CaatjaGwtPreloader(), new CaatjaGwtStorage(), null, new CaatjaGwt());
    }
    
    public static void init(CaatjaImageLoader caatjaImageLoader, CaatjaScoreLoader scoreLoader) {
        new Caatja(new CaatjaGwtDate(), new CaatjaGwtNavigator(), new CaatjaGwtWindow(), new CaatjaGwtRootPanel(),
                new CaatjaGwtService(), new CaatjaGwtEventManager(), caatjaImageLoader,
                new CaatjaGwtPreloader(), new CaatjaGwtStorage(), scoreLoader, new CaatjaGwt());
    }

	/**
	 * if setInterval, this value holds CAAT.setInterval return value.
	 * 
	 * @type {null}
	 */
	public static Timer INTERVAL_ID = null;

	@Override
	public void endLoop() {
		if (CAAT.NO_RAF) {
			if (INTERVAL_ID != null) {
				// clearInterval(INTERVAL_ID);
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

		// Specific init jsni data
		initJsniData();

		if (CAAT.NO_RAF) {

			INTERVAL_ID = new Timer() {
				@Override
				public void run() {

					double t = Caatja.getTime();

					for (Director d : director) {
						try {
							d.renderFrame();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					FRAME_TIME = t - SET_INTERVAL;

					if (CAAT.RAF != null) {
						CAAT.REQUEST_ANIMATION_FRAME_TIME = Caatja.getTime() - CAAT.RAF;
					}
					CAAT.RAF = Caatja.getTime();

					SET_INTERVAL = t;

				}
			};

			INTERVAL_ID.scheduleRepeating(1000 / CAAT.FPS);

		} else {
			Caatja.renderFrame(null);
		}

	}

	private static native void initJsniData()
	/*-{
		$wnd.requestAnimFrame = (function() {
			return window.requestAnimationFrame
					|| window.webkitRequestAnimationFrame
					|| window.mozRequestAnimationFrame
					|| window.oRequestAnimationFrame
					|| window.msRequestAnimationFrame
					|| function raf(callback, element) {
						$wnd.setTimeout(callback,
								1000 / @com.katspow.caatja.core.CAAT::FPS);
					};
		})();
	}-*/;

	@Override
	protected void requestAnimationFrame() {
		
		AnimationScheduler.get().requestAnimationFrame(new AnimationCallback() {
			@Override
			public void execute(double timestamp) {
				try {
					renderFrame();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
//		requestAF();
	}
	
	private static native void requestAF()
	/*-{
		$wnd.requestAnimFrame(this.@com.katspow.caatja.core.CAAT::renderFrame());
	}-*/;

	@Override
	protected void setCursor(String value) {
		setC(value);
	}

	private static native void setC(String value)
	/*-{
	if (navigator.browser !== 'iOS') {
	  $doc.body.style.cursor = value;
	}
	}-*/;

	@Override
	public void globalEnableEvents() {
		if (globalEventsEnabled) {
			return;
		}

		globalEventsEnabled = true;

		RootPanel.get().addDomHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				int key = event.getNativeKeyCode();
				
				// Add by me
				event.preventDefault();

				if (key == SHIFT_KEY) {
					KEY_MODIFIERS.shift = true;
				} else if (key == CONTROL_KEY) {
					KEY_MODIFIERS.control = true;
				} else if (key == ALT_KEY) {
					KEY_MODIFIERS.alt = true;
				} else {
					for (CAATKeyListener keyListener : keyListeners) {
						keyListener.call(new CAATKeyEvent(key, "down", new KeyModifiers(KEY_MODIFIERS.alt,
								KEY_MODIFIERS.control, KEY_MODIFIERS.shift)));
						// keyListener.call(key, "down", new
						// KeyModifiers(KEY_MODIFIERS.alt,
						// KEY_MODIFIERS.control, KEY_MODIFIERS.shift), event);
					}

				}

			}
		}, KeyDownEvent.getType());

		RootPanel.get().addDomHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				int key = event.getNativeKeyCode();
				
				// Add by me
				event.preventDefault();

				if (key == SHIFT_KEY) {
					KEY_MODIFIERS.shift = false;
				} else if (key == CONTROL_KEY) {
					KEY_MODIFIERS.control = false;
				} else if (key == ALT_KEY) {
					CAAT.KEY_MODIFIERS.alt = false;
				} else {
					for (CAATKeyListener keyListener : keyListeners) {
						keyListener.call(new CAATKeyEvent(key, "up", new KeyModifiers(KEY_MODIFIERS.alt,
								KEY_MODIFIERS.control, KEY_MODIFIERS.shift)));

						// keyListener.call(key, "up", new
						// KeyModifiers(KEY_MODIFIERS.alt,
						// KEY_MODIFIERS.control, KEY_MODIFIERS.shift), event);
					}
				}

			}
		}, KeyUpEvent.getType());

		RootPanel.get().addHandler(new ResizeHandler() {
			@Override
			public void onResize(ResizeEvent event) {
				for (ResizeListener resizeListener : windowResizeListeners) {
					resizeListener.windowResized(Caatja.getClientWidth(), Caatja.getClientHeight());
				}
			}
		}, ResizeEvent.getType());
	}

    @Override
    protected int randomInt(int maxValue) {
        return Random.nextInt(maxValue);
    }

	@Override
	public void setFullScreen(boolean fullScreen) {
		// TODO Auto-generated method stub
	}

}
