package com.katspow.caatja.core;

import java.util.ArrayList;
import java.util.List;

import com.katspow.caatja.CAATKeyListener;
import com.katspow.caatja.KeyModifiers;
import com.katspow.caatja.foundation.Director;
import com.katspow.caatja.foundation.ResizeListener;
import com.katspow.caatja.foundation.Scene;
import com.katspow.caatja.math.matrix.Matrix;

/**
 * TODO
 * Input.js
 * AnimationLoop.js
 * Constants.js
 *
 */
public abstract class CAAT {
	
	// Add by me
    public static final boolean NO_PERF = false;
    
    /**
     * Use RAF shim instead of setInterval. Set to != 0 to use setInterval.
     * @type {Number}
     */
    public static boolean NO_RAF = false;
    
//    public static native Canvas createCanvas(CanvasElement element) 
//    /*-{
//        @com.google.gwt.canvas.client.Canvas::new(Lcom/google/gwt/dom/client/CanvasElement;)(element);
//    }-*/;
    
    /**
     * // do not clamp coordinates. speeds things up in older browsers.
     * @type {Boolean}
     * @private
     */
    public static boolean CLAMP= false;
    
    public static void setCoordinateClamp(boolean clamp) {
        CLAMP = clamp;
        Matrix.setCoordinateClamping(clamp);
    }
    
    /**
     * Control how CAAT.Font and CAAT.TextActor control font ascent/descent values.
     * 0 means it will guess values from a font height
     * 1 means it will try to use css to get accurate ascent/descent values and fall back to the previous method
     *   in case it couldn't.
     *
     * @type {Number}
     */
    public static final boolean CSS_TEXT_METRICS=      false;
    
    // TODO Enum ?
    /**
     * Constant to set touch behavior as single touch, compatible with mouse.
     * @type {Number}
     * @constant
     */
    public static final int TOUCH_AS_MOUSE=        1;
    
    /**
     * Constant to set CAAT touch behavior as multitouch.
     * @type {Number}
     * @contant
     */
    public static final int TOUCH_AS_MULTITOUCH=   2;

    /**
     * Set CAAT touch behavior as single or multi touch.
     * @type {Number}
     */
    public static int TOUCH_BEHAVIOR= TOUCH_AS_MOUSE;
    
    /**
     * Box2D point meter conversion ratio.
     */
    public static int PMR = 64;
    
    /**
     * is GLRendering enabled.
     * @type {Boolean}
     */
    public static boolean GLRENDER= false;

    /**
     * set this variable before building CAAT.Director intances to enable debug panel.
     */
    public static boolean DEBUG = false;
    
    /**
     * show Bounding Boxes
     * @type {Boolean}
     */
    public static boolean DEBUGBB = false;
    
    /**
     * Bounding Boxes color.
     * @type {String}
     */
    public static String DEBUGBBCOLOR = "#00f";
    
    /**
     * debug axis aligned bounding boxes.
     * @type {Boolean}
     */
    public static boolean DEBUGAABB = false; // debug bounding boxes.
    
    /**
     * Bounding boxes color.
     * @type {String}
     */
    public static String DEBUGAABBCOLOR = "#f00";
    
    /**
     * if CAAT.Director.setClear uses CLEAR_DIRTY_RECTS, this will show them on screen.
     * @type {Boolean}
     */
    public static boolean DEBUG_DIRTYRECTS = false;
    
    /**
     * time to process one frame.
     * @type {Number}
     */
    public static double FRAME_TIME = 0;

    /**
     * Flag to signal whether events are enabled for CAAT.
     */
    public static boolean globalEventsEnabled = false;

    /**
     * Generic eventing attributes.
     */

    public static Director targetDirector = null;

    /**
     * Accelerometer related data.
     */
    // TODO Accel disabled
    // prevOnDeviceMotion= null; // previous accelerometer callback function.
    // onDeviceMotion= null; // current accelerometer callback set for CAAT.

    // TODO Accel disabled
    // accelerationIncludingGravity= { x:0, y:0, z:0 }; // acceleration data.
    // rotationRate= { alpha: 0, beta:0, gamma: 0 }; // angles data.

    /**
     * Do not consider mouse drag gesture at least until you have dragged
     * DRAG_THRESHOLD_X and DRAG_THRESHOLD_Y pixels.
     * This is suitable for tablets, where just by touching, drag events are delivered.
     */
    public static int DRAG_THRESHOLD_X = 5;
    public static int DRAG_THRESHOLD_Y = 5;
    
    /**
     * When switching scenes, cache exiting scene or not. Set before building director instance.
     * @type {Boolean}
     */
    public static final boolean CACHE_SCENE_ON_CHANGE= true;   // cache scenes on change. set before building director instance.

    /**
     * Boolean flag to determine if CAAT.loop has already been called.
     * @type {Boolean}
     */
    public static boolean renderEnabled = false;
    
    /**
     * expected FPS when using setInterval animation.
     * @type {Number}
     */
    public static int FPS = 60;

    /**
     * Array of window resize listeners.
     * @type {Array}
     */
    public static List<ResizeListener> windowResizeListeners = new ArrayList<ResizeListener>();

    /**
     * Register a function callback as window resize listener.
     * @param f
     */
    public static void registerResizeListener(ResizeListener f) {
        windowResizeListeners.add(f);
    }

    /**
     * Remove a function callback as window resize listener.
     * @param director
     */
    public static void unregisterResizeListener(Director director) {
        windowResizeListeners.remove(director);
    }
    
    public static double getCurrentSceneTime() {
        return CAAT.currentDirector.getCurrentScene().time;
    }

    /**
     * Array of Key listeners.
     */
    public static List<CAATKeyListener> keyListeners = new ArrayList<CAATKeyListener>();

    /**
     * Register a function callback as key listener.
     * @param f
     */
    public static void registerKeyListener(CAATKeyListener f) {
        keyListeners.add(f);
    }
    
    // TODO Externaliser dans une autre classe (cf KeyEvent.js)
    public enum Keys {
        ENTER(13),
        BACKSPACE(8),
        TAB(9),
        SHIFT(16),
        CTRL(17),
        ALT(18),
        PAUSE(19),
        CAPSLOCK(20),
        ESCAPE(27),
        // SPACE(32),
        PAGEUP(33),
        PAGEDOWN(34),
        END(35),
        HOME(36),
        LEFT(37),
        UP(38),
        RIGHT(39),
        DOWN(40),
        INSERT(45),
        DELETE(46),
        zero(48),
        one(49),
        two(50),
        three(51),
        four(52),
        five(53),
        six(54),
        seven(55),
        eight(56),
        nine(57),
        a(65),
        b(66),
        c(67),
        d(68),
        e(69),
        f(70),
        g(71),
        h(72),
        i(73),
        j(74),
        k(75),
        l(76),
        m(77),
        n(78),
        o(79),
        p(80),
        q(81),
        r(82),
        s(83),
        t(84),
        u(85),
        v(86),
        w(87),
        x(88),
        y(89),
        z(90),
        SELECT(93),
        NUMPAD0(96),
        NUMPAD1(97),
        NUMPAD2(98),
        NUMPAD3(99),
        NUMPAD4(100),
        NUMPAD5(101),
        NUMPAD6(102),
        NUMPAD7(103),
        NUMPAD8(104),
        NUMPAD9(105),
        MULTIPLY(106),
        ADD(107),
        SUBTRACT(109),
        DECIMALPOINT(110),
        DIVIDE(111),
        F1(112),
        F2(113),
        F3(114),
        F4(115),
        F5(116),
        F6(117),
        F7(118),
        F8(119),
        F9(120),
        F10(121),
        F11(122),
        F12(123),
        NUMLOCK(144),
        SCROLLLOCK(145),
        SEMICOLON(186),
        EQUALSIGN(187),
        COMMA(188),
        DASH(189),
        PERIOD(190),
        FORWARDSLASH(191),
        GRAVEACCENT(192),
        OPENBRACKET(219),
        BACKSLASH(220),
        CLOSEBRAKET(221),
        SINGLEQUOTE(222);
        
        private int val;

        Keys(int val) {
            this.val = val;
        }
        
        public int getValue() {
            return val;
        }
    }
    
    public static final int SHIFT_KEY=    16;
    public static final int CONTROL_KEY=  17;
    public static final int ALT_KEY=      18;
    public static final int ENTER_KEY=    13;
    
    /**
     * Event modifiers.
     */
    public static KeyModifiers KEY_MODIFIERS = new KeyModifiers();

    /**
     * Registered director objects.
     * @type {Array}
     */
    public static List<Director> director = null;
    
    public static final boolean RETINA_DISPLAY_ENABLED = false;

    public static void log(String msg) {
        consoleLog(msg);
    }

    static native void consoleLog(String message) /*-{
		console.log(message);
    }-*/;
    
    /**
     * Enable window level input events, keys and redimension.
     */
    public abstract void globalEnableEvents();
//    {
//        if (globalEventsEnabled) {
//            return;
//        }
//
//        globalEventsEnabled = true;
//
//        RootPanel.get().addDomHandler(new KeyDownHandler() {
//            @Override
//            public void onKeyDown(KeyDownEvent event) {
//                int key = event.getNativeKeyCode();
//                
//                if ( key== SHIFT_KEY ) {
//                    KEY_MODIFIERS.shift= true;
//                } else if ( key== CONTROL_KEY ) {
//                    KEY_MODIFIERS.control= true;
//                } else if ( key== ALT_KEY ) {
//                    KEY_MODIFIERS.alt= true;
//                } else {
//                    for (CAATKeyListener keyListener : keyListeners) {
//                        keyListener.call(new CAATKeyEvent(
//                                key,
//                                "down",
//                                new KeyModifiers(KEY_MODIFIERS.alt, KEY_MODIFIERS.control, KEY_MODIFIERS.shift),
//                                event
//                                ));
//                        //keyListener.call(key, "down", new KeyModifiers(KEY_MODIFIERS.alt, KEY_MODIFIERS.control, KEY_MODIFIERS.shift), event);
//                    }
//                    
//                }
//                
//            }
//        }, KeyDownEvent.getType());
//
//        RootPanel.get().addDomHandler(new KeyUpHandler() {
//            @Override
//            public void onKeyUp(KeyUpEvent event) {
//                int key = event.getNativeKeyCode();
//                
//                if ( key==SHIFT_KEY ) {
//                    KEY_MODIFIERS.shift= false;
//                } else if ( key==CONTROL_KEY ) {
//                    KEY_MODIFIERS.control= false;
//                } else if ( key==ALT_KEY ) {
//                    CAAT.KEY_MODIFIERS.alt= false;
//                } else {
//                    for (CAATKeyListener keyListener : keyListeners) {
//                        keyListener.call(new CAATKeyEvent(
//                                key,
//                                "up",
//                                new KeyModifiers(KEY_MODIFIERS.alt, KEY_MODIFIERS.control, KEY_MODIFIERS.shift),
//                                event
//                                ));
//                        
//                        //keyListener.call(key, "up", new KeyModifiers(KEY_MODIFIERS.alt, KEY_MODIFIERS.control, KEY_MODIFIERS.shift), event);
//                    }
//                }
//                
//                
//
//            }
//        }, KeyUpEvent.getType());
//
//        RootPanel.get().addHandler(new ResizeHandler() {
//            @Override
//            public void onResize(ResizeEvent event) {
//                for (ResizeListener resizeListener : windowResizeListeners) {
//                    resizeListener.windowResized(Caatja.getClientWidth(), Caatja.getClientHeight());
//                }
//            }
//        }, ResizeEvent.getType());
//
//    }
//
//    /**
//     * Polyfill for requestAnimationFrame.
//     */
//    private static native void initJsniData()
//    /*-{
//		$wnd.requestAnimFrame = (function() {
//			return window.requestAnimationFrame
//					|| window.webkitRequestAnimationFrame
//					|| window.mozRequestAnimationFrame
//					|| window.oRequestAnimationFrame
//					|| window.msRequestAnimationFrame
//					|| function raf(callback, element) {
//						$wnd.setTimeout(callback,
//								1000 / @com.katspow.gwat.client.core.CAAT::FPS);
//					};
//		})();
//    }-*/;
    
    /**
     * time between two consecutive setInterval calls.
     * @type {Number}
     */
    public static double SET_INTERVAL = 0;
    
    // TODO Caatja -> replace clearInterval method call
    public abstract void endLoop();
    //{
//        if ( CAAT.NO_RAF ) {
//            if ( CAAT.INTERVAL_ID!=null ) {
//                clearInterval( CAAT.INTERVAL_ID );
//            }
//        } else {
//            CAAT.ENDRAF= true;
//        }
//
//        CAAT.renderEnabled= false;
//    }
    
    /**
     * if RAF, this value signals end of RAF.
     * @type {Boolean}
     */
    public static boolean ENDRAF= false; 
    
    /**
     * if setInterval, this value holds CAAT.setInterval return value.
     * @type {null}
     */
    // TODO Caatja
//    public static Timer INTERVAL_ID= null;

    /**
     * Main animation loop entry point.
     * @param fps {number} desired fps. This parameter makes no sense unless requestAnimationFrame function
     * is not present in the system.
     */
 // TODO Caatja
    public abstract void loop(int fps) throws Exception; 
    //{
//        if (CAAT.renderEnabled) {
//            return;
//        }
//        
//        for (int i = 0, l = CAAT.director.size(); i < l; i++) {
//            CAAT.director.get(i).timeline= Caatja.getTime();
//        }
//
//        if (fps <= 0) {
//            CAAT.FPS = 60;
//        }
//
//        CAAT.renderEnabled = true;
//        
//        // Specific init jsni data
//        initJsniData();
//        
//        if (CAAT.NO_RAF) {
//            
//            CAAT.INTERVAL_ID = new Timer() {
//				@Override
//				public void run() {
//				    
//				    double t = Caatja.getTime();
//				    
//					for (Director d : director) {
//						try {
//                            d.renderFrame();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//					}
//					
//					FRAME_TIME = t - SET_INTERVAL;
//					
//					if (CAAT.RAF != null)   {
//                        CAAT.REQUEST_ANIMATION_FRAME_TIME= Caatja.getTime()-CAAT.RAF;
//                    }
//                    CAAT.RAF= Caatja.getTime();
//					
//					SET_INTERVAL = t;
//					
//				}
//        	};
//        	
//        	CAAT.INTERVAL_ID.scheduleRepeating(1000 / CAAT.FPS);
//        	
//        } else {
//        	CAAT.renderFrame(null);
//        }
//
//    }
    
    /**
     * Current animated director.
     * @type {CAAT.Foundation.Director}
     */
    public static Director currentDirector = null;
    
    public static final Scene getCurrentScene() {
        return currentDirector.getCurrentScene();
    }
    
    /**
     * debug panel update time.
     * @type {Number}
     */
    public static int FPS_REFRESH = 500;
    
    /**
     * requestAnimationFrame time reference.
     * @type {Number}
     */
    public static Double RAF = null;
    
    /**
     * time between two consecutive RAF. usually bigger than FRAME_TIME
     * @type {Number}
     */
    public static double REQUEST_ANIMATION_FRAME_TIME = 0;

    /**
     * Make a frame for each director instance present in the system.
     * @throws Exception 
     */
    public void renderFrame() throws Exception {
        renderFrame(null);
    }
    
    public void renderFrame(Double now) throws Exception {
        
        if ( CAAT.ENDRAF ) {
            CAAT.ENDRAF= false;
            return;
        }
        
        if ((now == null) || (now == 0)) now = Caatja.getTime();

        double t= Caatja.getTime();
        REQUEST_ANIMATION_FRAME_TIME = RAF != null ? now - RAF : 0.16;
        
        for (Director dr : director) {
            if ( dr.renderMode== Director.RenderMode.CONTINOUS || dr.needsRepaint ) {
                dr.renderFrame();
            }
        }
        
        RAF = now;
        FRAME_TIME = Caatja.getTime() - t;
        
//        t = Caatja.getTime() - t;
//        CAAT.FRAME_TIME = t;
//        
//        if (RAF != null)   {
//            REQUEST_ANIMATION_FRAME_TIME= Caatja.getTime()- RAF;
//        }
//        
//        RAF= Caatja.getTime();

        requestAnimationFrame();
    }
    
    /**
     * Set browser cursor. The preferred method for cursor change is this method.
     * @param cursor
     */
    protected abstract void setCursor(String value);
    
//    public static native void setCursor(String value) /*-{
//        if (navigator.browser !== 'iOS') {
//            $doc.body.style.cursor = value;
//        }
//    }-*/;
    
    protected abstract void requestAnimationFrame();

//    private static native void requestAnimationFrame()
//    /*-{
//		$wnd.requestAnimFrame(@com.katspow.gwat.client.core.CAAT::renderFrame());
//    }-*/;
    
    // TODO Check and Caatja ?? Remove since used only by endLoop() ?
//    private static void clearInterval(Timer timer) {
//        timer.cancel();
//    }
    
    /**
     * Register and keep track of every CAAT.Director instance in the document.
     */
    public void registerDirector(Director director) {
        if (CAAT.director == null) {
            CAAT.director = new ArrayList<Director>();
            CAAT.currentDirector= director;
        }

        CAAT.director.add(director);
        globalEnableEvents();
    }
    
    protected abstract int randomInt(int maxValue);

	public abstract void setFullScreen(boolean fullScreen);

    /**
     * Enable at window level accelerometer events.
     * 
     */

    // TODO Rotation Disabled
    // function tilt(data) {
    // CAAT.rotationRate= {
    // alpha : 0,
    // beta : data[0],
    // gamma : data[1]
    // }
    // }
    //
    // if (window.DeviceOrientationEvent) {
    // window.addEventListener("deviceorientation", function (event) {
    // tilt([event.beta, event.gamma]);
    // }, true);
    // } else if (window.DeviceMotionEvent) {
    // window.addEventListener('devicemotion', function (event) {
    // tilt([event.acceleration.x * 2, event.acceleration.y * 2]);
    // }, true);
    // } else {
    // window.addEventListener("MozOrientation", function (event) {
    // tilt([-event.y * 45, event.x * 45]);
    // }, true);
    // }
    //

}
