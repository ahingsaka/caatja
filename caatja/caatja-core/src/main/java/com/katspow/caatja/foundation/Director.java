package com.katspow.caatja.foundation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.katspow.caatja.behavior.Interpolator;
import com.katspow.caatja.core.CAAT;
import com.katspow.caatja.core.Caatja;
import com.katspow.caatja.core.canvas.CaatjaCanvas;
import com.katspow.caatja.core.canvas.CaatjaContext2d;
import com.katspow.caatja.core.canvas.CaatjaImage;
import com.katspow.caatja.event.CAATMouseEvent;
import com.katspow.caatja.event.TouchInfoData;
import com.katspow.caatja.foundation.Scene.Ease;
import com.katspow.caatja.foundation.actor.Actor;
import com.katspow.caatja.foundation.actor.ActorContainer;
import com.katspow.caatja.foundation.actor.ImageActor;
import com.katspow.caatja.foundation.timer.CallbackCancel;
import com.katspow.caatja.foundation.timer.CallbackTick;
import com.katspow.caatja.foundation.timer.CallbackTimeout;
import com.katspow.caatja.foundation.timer.TimerManager;
import com.katspow.caatja.foundation.timer.TimerTask;
import com.katspow.caatja.math.Pt;
import com.katspow.caatja.math.Rectangle;
import com.katspow.caatja.math.matrix.Matrix;
import com.katspow.caatja.math.matrix.Matrix3;
import com.katspow.caatja.modules.audio.AudioManager;
import com.katspow.caatja.modules.debug.Debug;
import com.katspow.caatja.modules.runtime.BrowserInfo;
import com.katspow.caatja.modules.texturepacker.TexturePage;
import com.katspow.caatja.modules.texturepacker.TexturePageManager;
import com.katspow.caatja.webgl.program.ColorProgram;
import com.katspow.caatja.webgl.program.TextureProgram;

//import elemental.html.Float32Array;
//import elemental.html.WebGLRenderingContext;

public class Director extends ActorContainer implements ResizeListener {

    /**
     * flag indicating debug mode. It will draw affedted screen areas.
     * @type {boolean}
     */
    public boolean debug = false;
    
    /**
     * Set CAAT render mode. Right now, this takes no effect.
     */
    public RenderMode renderMode = RenderMode.CONTINOUS;
    
    /**
     * This method will be called before rendering any director scene.
     * Use this method to calculate your physics for example.
     * @private
     */
	public DirectorCallback onRenderStart = null;
	
	/**
     * This method will be called after rendering any director scene.
     * Use this method to clean your physics forces for example.
     * @private
     */
	private DirectorCallback onRenderEnd = null;
    
    // input related variables
	/**
     * mouse coordinate related to canvas 0,0 coord.
     * @private
     */
    public  Pt mousePoint = null;
    
    /**
     * previous mouse position cache. Needed for drag events.
     * @private
     */
    public  Pt prevMousePoint = null;
    
    /**
     * screen mouse coordinates.
     * @private
     */
    public  Pt screenMousePoint = null;
    
    /**
     * is the left mouse button pressed ?.
     * Needed to handle dragging.
     */
    public  boolean isMouseDown = false;
    
    /**
     * director's last actor receiving input.
     * Needed to set capture for dragging events.
     */
    public  Actor lastSelectedActor = null;
    
    /**
     * is input in drag mode ?
     */
    public  boolean dragging = false;
    
    // other attributes
    /**
     * This director scene collection.
     * @type {Array.<CAAT.Foundation.Scene>}
     */
    public List<Scene> scenes;
    
    /**
     * The current Scene. This and only this will receive events.
     */
    public Scene currentScene;
    
    /**
     * The canvas the Director draws on.
     * @private
     */
    public CaatjaCanvas canvas;
    
    /**
     * This director's canvas rendering context.
     */
    public CaatjaContext2d ctx;
    
    /**
     * director time.
     * @private
     */
    public Double time = 0d;
    
    /**
     * global director timeline.
     * @private
     */
    public double timeline = 0;
    
    /**
     * An array of JSON elements of the form { id:string, image:Image }
     */
    public Map<String, CaatjaImage> imagesCache = null;
    
    /**
     * this director's audio manager.
     * @private
     */
    public AudioManager audioManager = null;
    
    /**
     * Clear screen strategy:
     * CAAT.Foundation.Director.CLEAR_NONE : director won't clear the background.
     * CAAT.Foundation.Director.CLEAR_DIRTY_RECTS : clear only affected actors screen area.
     * CAAT.Foundation.Director.CLEAR_ALL : clear the whole canvas object.
     * 
     * FIXME Change type !!
     */
    public boolean clear = true;
    
    /**
     * if CAAT.CACHE_SCENE_ON_CHANGE is set, this scene will hold a cached copy of the exiting scene.
     * @private
     */
    public Scene transitionScene = null;
    
    /**
     * Some browser related information.
     */
    private BrowserInfo browserInfo;
    
    /**
     * 3d context
     * @private
     */
//    public WebGLRenderingContext gl=                 null;
    
    /**
     * is WebGL enabled as renderer ?
     * @private
     */
    public boolean glEnabled=          false;
    
    
    /**
     * if webGL is on, CAAT will texture pack all images transparently.
     * @private
     */
//    glTextureManager:null,
    
    /**
     * The only GLSL program for webGL
     * @private
     */
    public TextureProgram glTextureProgram = null;
    public ColorProgram glColorProgram=     null;
    
    /**
     * webGL projection matrix
     * @private
     */
    public Matrix3 pMatrix=            null;
    
    /**
     * webGL vertex array
     * @private
     */
//    public Float32Array coords=             null;
    
    /**
     * webGL vertex indices.
     * @private
     */
    public Double coordsIndex=        0d;
    
    /**
     * webGL uv texture indices
     * @private
     */
//    public Float32Array uv=                 null;
    public int uvIndex=            0;
    
    /**
     * draw tris front_to_back or back_to_front ?
     * @private
     */
    public boolean front_to_back=      false;
    
    /**
     * statistics object
     */
    public Statistics statistics = new Statistics();
    
    private TexturePageManager glTextureManager;
    
    /**
     * webGL current texture page. This minimizes webGL context changes.
     * @private
     */
    public TexturePage currentTexturePage= null;
    
    /**
     * webGL current shader opacity.
     * BUGBUG: change this by vertex colors.
     * @private
     */
    public double currentOpacity=     1d;

    /**
     * if CAAT.NO_RAF is set (no request animation frame), this value is the setInterval returned
     * id.
     * @private
     * TODO Change type !
     */
    public Object intervalId=         null;

    /**
     * Rendered frames counter.
     */
    public int frameCounter=       0;
    
    public enum Resize {
        NONE(1), WIDTH(2), HEIGHT(4), BOTH(8), PROPORTIONAL(16);

        private int value;

        private Resize(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * Window resize strategy.
     * see CAAT.Foundation.Director.RESIZE_* constants.
     * @private
     */
    public Resize resize = Resize.NONE;
    
    /**
     * Callback when the window is resized.
     */
    public DirectorResizeCallback onResizeCallback = null;
    
    /**
     * Calculated gesture event scale.
     * @private
     */
    public int __gestureScale =    0;
    
    /**
     * Calculated gesture event rotation.
     * @private
     */
    public double __gestureRotation =  0;

    /**
     * Dirty rects cache.
     * An array of CAAT.Math.Rectangle object.
     * @private
     */
    private List<Rectangle> dirtyRects;
    
    /**
     * Currently used dirty rects.
     * @private
     */
    private List<Rectangle> sDirtyRects;
    
    /**
     * Number of currently allocated dirty rects.
     * @private
     */
    private List<Rectangle> cDirtyRects;
    
    /**
     * Number of currently allocated dirty rects.
     * @private
     */
    private int dirtyRectsIndex;
    
    /**
     * Dirty rects enabled ??
     * @private
     */
    public boolean dirtyRectsEnabled = false;
    
    /**
     * Number of dirty rects.
     * @private
     */
    public int nDirtyRects = 0;
    
    /**
     * Dirty rects count debug info.
     * @private
     */
    public int drDiscarded = 0;
    
    /**
     * Is this director stopped ?
     */
    public boolean stopped             = false;  
    
    /**
     * currently unused.
     * Intended to run caat in evented mode.
     * @private
     */
    public boolean needsRepaint        = false;   
    
    /**
     * Touches information. Associate touch.id with an actor and original touch info.
     * @private
     */
    public Map<String, TouchInfoData> touches; 
    
    /**
     * Director�s timer manager.
     * Each scene has a timerManager as well.
     * The difference is the scope. Director�s timers will always be checked whereas scene� timers
     * will only be scheduled/checked when the scene is director� current scene.
     * @private
     */
    public TimerManager timerManager;
    
    /**
     * Retina display deicePixels/backingStorePixels ratio
     * @private
     */
    public static double SCREEN_RATIO = 1;
    
    private HashMap<Object, Object> __map;

    // Add by me
	private double referenceHeight;
	private double referenceWidth;
    public Debug debugInfo;
    public boolean gesturing = false;
    public double __gestureSX;
    public double __gestureSY;
    public boolean inValidation;
    
    /**
     * Director is the animator scene graph manager.
     * <p>
     * The director elements is an ActorContainer itself with the main responsibility of managing
     * different Scenes.
     * <p>
     * It is responsible for:
     * <ul>
     * <li>scene changes.
     * <li>route input to the appropriate scene graph actor.
     * <li>be the central point for resource caching.
     * <li>manage the timeline.
     * <li>manage frame rate.
     * <li>etc.
     * </ul>
     *
     * <p>
     * One document can contain different CAAT.Director instances which will be kept together in CAAT
     * function.
     *
     * @constructor
     * @extends CAAT.ActorContainer
     */
    public Director() {
        super();
        this.browserInfo = new BrowserInfo();
        this.audioManager = new AudioManager().initialize(8);
        this.scenes = new ArrayList<Scene>();
        this.imagesCache = new HashMap<String, CaatjaImage>();
        
        // input related variables initialization
        this.mousePoint=        new Pt(0,0,0);
        this.prevMousePoint=    new Pt(0,0,0);
        this.screenMousePoint=  new Pt(0,0,0);
        this.isMouseDown=       false;
        this.lastSelectedActor= null;
        this.dragging=          false;
        
        this.cDirtyRects= new ArrayList<Rectangle>();
        
        this.sDirtyRects= new ArrayList<Rectangle>();
        this.dirtyRects= new ArrayList<Rectangle>();
        for( int i=0; i<64; i++ ) {
            this.dirtyRects.add( new Rectangle() );
        }
        this.dirtyRectsIndex=   0;
        this.touches = new HashMap<String, TouchInfoData>();
        
        this.timerManager= new TimerManager();
        this.__map= new HashMap<Object, Object>();
    }
    
    public enum RenderMode {
        CONTINOUS(1), // redraw every frame
        DIRTY(2); // suitable for evented CAAT.
        
        private int val;

        private RenderMode(int val) {
            this.val = val;
        }
        
        public int getVal() {
            return val;
        }
        
    }
    
    public static final boolean CLEAR_DIRTY_RECTS = true;
    public static final boolean CLEAR_ALL = true;
    public static final boolean CLEAR_NONE = false;
    
    // Add by me, lightweight constructor
    public Director(boolean activation) {
        
    }
    
    public Director clean() {
        this.scenes= null;
        this.currentScene= null;
        this.imagesCache= null;
        this.audioManager= null;
        this.isMouseDown= false;
        this.lastSelectedActor=  null;
        this.dragging= false;
        this.__gestureScale= 0;
        this.__gestureRotation= 0;
        this.dirty= true;
        this.dirtyRects=null;
        this.cDirtyRects=null;
        this.dirtyRectsIndex=  0;
        this.dirtyRectsEnabled= false;
        this.nDirtyRects= 0;
        this.onResizeCallback= null;
        return this;
    }
    
    public AudioManager cancelPlay(String id) {
        return this.audioManager.cancelPlay(id);
    }

    // TODO Audio
//    public AudioManager cancelPlayByChannel(AudioElement audioObject) {
//        return this.audioManager.cancelPlayByChannel(audioObject);
//    }

    // TODO Audio
    public Director setAudioFormatExtensions(List<String> extensions) {
//        this.audioManager.setAudioFormatExtensions(extensions);
        return this;
    }
    
    public Director setValueForKey(Object key, Object value) {
        this.__map.put(key, value);
        return this;
    }
    
    public Object getValueForKey(Object key ) {
        return this.__map.get(key);
    }
    
    public TimerTask createTimer(double startTime,double duration, CallbackTimeout callback_timeout, CallbackTick callback_tick, CallbackCancel callback_cancel ) {
        return this.timerManager.createTimer( startTime, duration, callback_timeout, callback_tick, callback_cancel, this );
    }
    
    public void requestRepaint() {
        this.needsRepaint= true;
    }
    
    public Scene getCurrentScene() {
        return this.currentScene;
    }

    public Director setRenderMode(RenderMode mode ) {
        if ( mode==Director.RenderMode.CONTINOUS || mode== Director.RenderMode.DIRTY ) {
            this.renderMode= mode;
        }
        return this;
    }
    
    public void checkDebug() {
        // TODO ???
//        !navigator.isCocoonJS
        if ( CAAT.DEBUG ) {
            // TODO Check
            this.debugInfo = new Debug().initialize( this.width, 60 );
//            this.debugInfo= dd.debugInfo.bind(dd);
        }
    }
    
    public String getRenderType() {
        return this.glEnabled ? "WEBGL" : "CANVAS";
    }
    
    public void windowResized(int w, int h) {
        switch (this.resize) {
        case WIDTH:
            this.setBounds(0, 0, w, this.height);
            break;
        case HEIGHT:
            this.setBounds(0, 0, this.width, h);
            break;
        case BOTH:
            this.setBounds(0, 0, w, h);
            break;
        case PROPORTIONAL:
            this.setScaleProportional(w, h);
            break;
        }
        
        if ( this.glEnabled ) {
            this.glReset();
        }
        
        if ( this.onResizeCallback != null)    {
            this.onResizeCallback.call( this, w, h );
        }
    }
    
    public void setScaleProportional(int w, int h) {
        double factor = Math.min(w / this.referenceWidth, h / this.referenceHeight);

        this.canvas.setCoordinateSpaceWidth((int) (this.referenceWidth * factor));
        this.canvas.setCoordinateSpaceHeight((int) (this.referenceHeight * factor));
        // FIXME TODO No webgl for the moment
        // this.ctx = this.canvas.getContext(this.glEnabled ? "experimental-webgl" : "2d");
        this.ctx = this.canvas.getContext2d();
        
        this.__setupRetina();

        this.setScaleAnchored(factor * this.scaleX, factor * this.scaleY, 0, 0);
//        this.setScaleAnchored(factor, factor, 0, 0);
        
        if ( this.glEnabled ) {
            this.glReset();
        }
    }

    /**
     * Enable window resize events and set redimension policy. A callback functio could be supplied
     * to be notified on a Director redimension event. This is necessary in the case you set a redim
     * policy not equal to RESIZE_PROPORTIONAL. In those redimension modes, director"s area and their
     * children scenes are resized to fit the new area. But scenes content is not resized, and have
     * no option of knowing so uless an onResizeCallback function is supplied.
     *
     * @param mode {number}  RESIZE_BOTH, RESIZE_WIDTH, RESIZE_HEIGHT, RESIZE_NONE.
     * @param onResizeCallback {function(director{CAAT.Director}, width{integer}, height{integer})} a callback
     * to notify on canvas resize.
     */
    public Director enableResizeEvents(Resize mode, DirectorResizeCallback onResizeCallback) {
        if (mode == Resize.BOTH || mode == Resize.WIDTH || mode == Resize.HEIGHT || mode == Resize.PROPORTIONAL) {
			this.referenceWidth = this.width;
			this.referenceHeight = this.height;
            this.resize = mode;
            CAAT.registerResizeListener(this);
            this.onResizeCallback= onResizeCallback;
            
            // TODO Check
            this.windowResized( Caatja.getClientWidth(),  Caatja.getClientHeight() );
        } else {
            CAAT.unregisterResizeListener(this);
            this.onResizeCallback= null;
        }
        
        return this;
    }
    
    private void __setupRetina() {

        if ( CAAT.RETINA_DISPLAY_ENABLED ) {

            // The world is full of opensource awesomeness.
            //
            // Source: http://www.html5rocks.com/en/tutorials/canvas/hidpi/
            //
            double devicePixelRatio= BrowserInfo.devicePixelRatio;
            
            // TODO ???
            double backingStoreRatio = 1;
//            double backingStoreRatio = this.ctx.webkitBackingStorePixelRatio || /* maybe more prefixes to come...
//                                    this.ctx.mozBackingStorePixelRatio ||
//                                    this.ctx.msBackingStorePixelRatio ||
//                                    this.ctx.oBackingStorePixelRatio ||
//                                    this.ctx.backingStorePixelRatio || */
//                                    1;

            double ratio = devicePixelRatio / backingStoreRatio;

            if (devicePixelRatio != backingStoreRatio) {

                double oldWidth = this.canvas.getCoordinateSpaceWidth();
                double oldHeight = this.canvas.getCoordinateSpaceHeight();

                this.canvas.setCoordinateSpaceWidth((int) (oldWidth * ratio));
                this.canvas.setCoordinateSpaceHeight((int) (oldHeight * ratio));

                // FIXME todo
//                DOM.setStyleAttribute(this.canvas.getElement(), "width", oldWidth + "px");
//                DOM.setStyleAttribute(this.canvas.getElement(), "height", oldHeight + "px");

                this.setScaleAnchored( ratio, ratio, 0, 0 );
            } else {
                this.setScaleAnchored( 1, 1, 0, 0 );
            }

            this.SCREEN_RATIO= ratio;
        } else {
            this.setScaleAnchored( 1, 1, 0, 0 );
        }
        
        for (Scene scene : this.scenes) {
            scene.setBounds(0, 0, this.width, this.height);
        }

    }

    /**
     * Set this director"s bounds as well as its contained scenes.
     * 
     * @param x
     *            {number} ignored, will be 0.
     * @param y
     *            {number} ignored, will be 0.
     * @param w
     *            {number} director width.
     * @param h
     *            {number} director height.
     * 
     * @return this
     */
    public Director setBounds(double x, double y, double w, double h) {
        super.setBounds(x, y, w, h);
        
        if ( this.canvas.getCoordinateSpaceWidth()!=w ) {
            this.canvas.setCoordinateSpaceWidth((int) w);
        }
        
        if ( this.canvas.getCoordinateSpaceHeight() != h ) {
            this.canvas.setCoordinateSpaceHeight((int) h);
        }
        
        // TODO No webgl for the moment 
//        this.ctx = this.canvas.getContext(this.glEnabled ? "experimental-webgl" : "2d");
        this.ctx = this.canvas.getContext2d();
        
        this.__setupRetina();

        if (this.glEnabled) {
            this.glReset();
        }

        return this;
    }

    /**
     * This method performs Director initialization. Must be called once.
     * If the canvas parameter is not set, it will create a Canvas itself,
     * and the developer must explicitly add the canvas to the desired DOM position.
     * This method will also set the Canvas dimension to the specified values
     * by width and height parameters.
     *
     * @param width {number} a canvas width
     * @param height {number} a canvas height
     * @param canvas {HTMLCanvasElement=} An optional Canvas object.
     * @param proxy {HTMLElement} this object can be an event proxy in case you'd like to layer different elements
     *              and want events delivered to the correct element.
     *
     * @return this
     * @throws Exception 
     */
    // Add by me
    public Director initialize(int width, int height, CaatjaCanvas canvas) throws Exception {
        
        if (canvas == null) {
            canvas = Caatja.createCanvas();
            Caatja.addCanvas(canvas);
        }
        
        this.canvas= canvas;
        
        // FIXME caatja
//        if (proxy == null) {
//            proxy = canvas;
//        }

        this.setBounds(0, 0, width, height);
        
     // FIXME caatja
        this.enableEvents(canvas);

        this.timeline = Caatja.getTime();
        
        // transition scene
        if (CAAT.CACHE_SCENE_ON_CHANGE) {
            this.transitionScene= new Scene().setBounds(0,0,width,height);
            
            CaatjaCanvas transitionCanvas= Caatja.createCanvas();
            // TODO Should I add this to RootPanel ??
            
            transitionCanvas.setCoordinateSpaceWidth(width);
            transitionCanvas.setCoordinateSpaceHeight(height);
            
            ImageActor transitionImageActor= new ImageActor().setCanvas(transitionCanvas);
            
            // TODO No background image for the moment
            //ImageActor transitionImageActor = new Actor().setBackgroundImage(transitionCanvas);
            
            this.transitionScene.ctx = transitionCanvas.getContext2d();
            this.transitionScene.addChildImmediately(transitionImageActor);
            this.transitionScene.setEaseListener(this);
        }
        
        this.checkDebug();
        
        return this;
    }
    
    // Add by me
    public Director initialize(int width, int height) throws Exception {
        return initialize(width, height, null);
    }
    
    // TODO
    public void glReset() {
//         this.pMatrix = CAAT.WebGL.GLU.makeOrtho(0, this.referenceWidth, this.referenceHeight, 0, -1, 1);
//        this.gl.viewport(0,0,this.canvas.width,this.canvas.height);
//        this.glColorProgram.setMatrixUniform(this.pMatrix);
//        this.glTextureProgram.setMatrixUniform(this.pMatrix);
//        this.gl.viewportWidth = this.canvas.width;
//        this.gl.viewportHeight = this.canvas.height;
    }
    
    /**
     * Experimental.
     * Initialize a gl enabled director.
     * @param width
     * @param height
     * @param canvas
     * @throws Exception 
     */
    // Add by me
    public Director initializeGL(int width,int height, CaatjaCanvas canvas ) throws Exception {

        if (canvas == null) {
            canvas = Caatja.createCanvas();
        }
        canvas.setCoordinateSpaceWidth(width);
        canvas.setCoordinateSpaceHeight(height);
        
        // Caatja no proxy
//        if (proxy == null) {
//            proxy = canvas;
//        }
        
        this.referenceWidth= width;
        this.referenceHeight=height;
        
        int i;

        // FIXME TODO
//        try {
//            
//            this.gl = (WebGLRenderingContext) canvas.getContext("experimental-webgl");
//            this.gl.viewport(0, 0, canvas.getCoordinateSpaceWidth(), canvas.getCoordinateSpaceHeight());
//        } catch(Exception e) {
//        }

//        if (this.gl != null) {
//            this.canvas= canvas;
        // this.setBounds(0, 0, width, height);
//            this.crc= this.ctx;
//            this.enableEvents(canvas);
//            this.timeline= Caatja.getTime();;
//
//            this.glColorProgram= (ColorProgram) new ColorProgram(this.gl).create().initialize();
//            this.glTextureProgram= (TextureProgram) new TextureProgram(this.gl).create().initialize();
//            this.glTextureProgram.useProgram();
//            this.glReset();
//
//            int maxTris=512;
//            this.coords= WebGLUtils.createArrayOfFloat32(maxTris*12);
//            this.uv= WebGLUtils.createArrayOfFloat32(maxTris*8);
//
//
//            this.gl.clearColor(0, 0, 0, 255);
//
//            if ( this.front_to_back ) {
//                this.gl.clearDepth((float) 1.0);
//                this.gl.enable(this.gl.DEPTH_TEST);
//                this.gl.depthFunc(this.gl.LESS);
//            } else {
//                this.gl.disable(this.gl.DEPTH_TEST);
//            }
//
//            this.gl.enable(this.gl.BLEND);
//            // Fix FF  this.gl.blendFunc(this.gl.SRC_ALPHA, this.gl.ONE_MINUS_SRC_ALPHA);
//        this.gl.blendFunc(this.gl.ONE, this.gl.ONE_MINUS_SRC_ALPHA);
//            this.glEnabled= true;
        
//            this.checkDebug();
//        } else {
            // fallback to non gl enabled canvas.
//            return this.initialize(width,height,canvas);    
//        }

        return this;
    }
    
    /**
     * Creates an initializes a Scene object.
     * @return {CAAT.Scene}
     */
    public Scene createScene() {
        Scene scene= new Scene();
        this.addScene(scene);
        return scene;
    }
    
    // TODO Update with source
    public void setImagesCache (Map<String, CaatjaImage> imagesCache, Integer tpW, Integer tpH) {
        
        if (imagesCache == null || imagesCache.isEmpty() ) {
            return;
        }

		if (null != this.glTextureManager) {
			this.glTextureManager.deletePages();
			this.glTextureManager = null;
		}

		// FIXME TODO
		// this.imagesCache= imagesCache;

		if (tpW == null) {
			tpW = 2048;
		}

		if (tpH == null) {
			tpH = 2048;
		}

		this.updateGLPages();

    }
    
    public void updateGLPages() {
        if (this.glEnabled) {

            this.glTextureManager= new TexturePageManager();
            
            // FIXME replace imagesCache with this.imagesCache
            // FIXME TODO
//            this.glTextureManager.createPages(this.gl,tpW,tpH, imagesCache);

            this.currentTexturePage= this.glTextureManager.pages.get(0);
            
            // FIXME
//            this.glTextureProgram.setTexture(this.currentTexturePage.texture);
        }
    }
    
    public Director setGLTexturePage(TexturePage tp ) {
        this.currentTexturePage = tp;
        // FIXME
//        this.glTextureProgram.setTexture(tp.texture);
        return this;
    }
    
    /**
     * Add a new image to
     * director"s image cache. If gl is enabled and the "noUpdateGL" is not set
     * to true this function will try to recreate the whole GL texture pages. If
     * many handcrafted images are to be added to the director, some performance
     * can be achieved by calling <code>director.addImage(id,image,false)</code>
     * many times and a final call with
     * <code>director.addImage(id,image,true)</code> to finally command the
     * director to create texture pages.
     * 
     * @param id
     *            {string|object} an identitifier to retrieve the image with
     * @param image
     *            {Image|HTMLCanvasElement} image to add to cache
     * @param noUpdateGL
     *            {!boolean} unless otherwise stated, the director will try to
     *            recreate the texture pages.
     */
    // TODO Check
    public void addImage(String id, CaatjaImage image, boolean noUpdateGL) {
        if ( this.getImage(id) != null) {
        	this.imagesCache.put(id, image);
//            this.imagesCache[ id ]= image;
        } else {
            this.imagesCache.put(id, image);
//            this.imagesCache[id]= image;
        }
        
        if (!!!noUpdateGL) {
            this.updateGLPages();
        }
    }
    
    public void deleteImage(String id, boolean noUpdateGL ) {
        
        this.imagesCache.remove(id);
        
        // TODO Check
//        for (var i = 0; i < this.imagesCache.length; i++) {
//            if (this.imagesCache[i].id === id) {
//                delete this.imagesCache[id];
//                this.imagesCache.splice(i,1);
//                break;
//            }
//        }
        
        if (!!! noUpdateGL ) {
            this.updateGLPages();
        }
    }

    public void setGLCurrentOpacity (double opacity) {
        this.currentOpacity= opacity;
        this.glTextureProgram.setAlpha(opacity);
    }
    /**
     * Render buffered elements.
     * @param vertex
     * @param coordsIndex
     * @param uv
     */
    // FIXME TODO
//    public void  glRender (Float32Array vertex, Double coordsIndex, Float32Array uv) {
//
//        if (vertex == null) {
//            vertex = this.coords;
//        }
//        
//        if (uv == null) {
//            uv= this.uv;
//        }
//        
//        if (coordsIndex == null) {
//            coordsIndex= this.coordsIndex;
//        }
//
//        WebGLRenderingContext gl= this.gl;
//
//        Double numTris= new Double(coordsIndex/12*2);
//        double numVertices= coordsIndex/3;
//
//        this.glTextureProgram.updateVertexBuffer( vertex );
//        this.glTextureProgram.updateUVBuffer( uv );
//
//        gl.drawElements(gl.TRIANGLES, 3*numTris.intValue(), gl.UNSIGNED_SHORT, 0d);
//
//    }
//    
//    public void glFlush () {
//        if ( this.coordsIndex!=0 ) {
//            this.glRender(this.coords, this.coordsIndex, this.uv);
//        }
//        this.coordsIndex= 0d;
//        this.uvIndex= 0;
//    this.statistics.draws++;
//    }
    
    public Actor findActorAtPosition(Pt point) {

        // z-order
        List<Actor> cl= this.childrenList;
        for( int i=cl.size()-1; i>=0; i-- ) {
            Actor child= this.childrenList.get(i);

            Pt np= new Pt( point.x, point.y, 0 );
            Actor contained= child.findActorAtPosition( np );
            if ( null!=contained ) {
                return contained;
            }
        }

        return this;
    }
    
    /**
    *
    * Reset statistics information.
    *
    * @private
    */
    private void resetStats() {
        this.statistics.size_total= 0;
        this.statistics.size_active=0;
        this.statistics.draws=      0;
        this.statistics.size_discarded_by_dirty_rects= 0;
    }
    
    /**
     * This is the entry point for the animation system of the Director.
     * The director is fed with the elapsed time value to maintain a virtual timeline.
     * This virtual timeline will provide each Scene with its own virtual timeline, and will only
     * feed time when the Scene is the current Scene, or is being switched.
     *
     * If dirty rectangles are enabled and canvas is used for rendering, the dirty rectangles will be
     * set up as a single clip area.
     *
     * @param time {number} integer indicating the elapsed time between two consecutive frames of the
     * Director.
     * @throws Exception 
     */
    public void render(double time) throws Exception {
        
        if ( this.currentScene != null && this.currentScene.isPaused() ) {
            return;
        }
        
        this.time+= time;
        
        for (int i = 0, l = this.childrenList.size(); i < l; i++) {
            Scene c = (Scene) this.childrenList.get(i);
            if (c.isInAnimationFrame(this.time) && !c.isPaused()) {
                double tt = c.time - c.start_time;
                c.timerManager.checkTimers(tt);
                c.timerManager.removeExpiredTimers();
            }
        }

        this.animate(this, this.time);
        
        // TODO (!navigator.isCocoonJS
        if ( CAAT.DEBUG ) {
            this.resetStats();
        }

        /**
         * draw director active scenes.
         */
        int ne= this.childrenList.size();
        Scene c = null;
        CaatjaContext2d ctx = this.ctx;
        
        if ( this.glEnabled ) {

            // FIXME TODO
//            this.gl.clear(this.gl.COLOR_BUFFER_BIT | this.gl.DEPTH_BUFFER_BIT);
//            this.coordsIndex= 0d;
//            this.uvIndex= 0;
            
//            
//            for(int i=0; i<ne; i++ ) {
//                Actor c= this.childrenList.get(i);
//               if (c.isInAnimationFrame(this.time)) {
//            tt = c.time - c.start_time;
//            if ( c.onRenderStart ) {
//                c.onRenderStart(tt);
//            }
//            c.paintActorGL(this, tt);
//            if ( c.onRenderEnd ) {
//                c.onRenderEnd(tt);
//            }
//               if ( !c.isPaused() ) {
//                  c.time += time;
//              }
//            if (!navigator.isCocoonJS && CAAT.DEBUG ) {
//            this.statistics.size_total+= c.size_total;
//            this.statistics.size_active+= c.size_active;
//        }
//        }
//            }
//
//            this.glFlush();

        } else {
            ctx.setGlobalAlpha(1);
            ctx.setGlobalCompositeOperation("source-over");

            ctx.save();
            
            if (this.dirtyRectsEnabled) {
                this.modelViewMatrix.transformRenderingContext( ctx );
                
                if ( !CAAT.DEBUG_DIRTYRECTS ) {
                    ctx.beginPath();
                    this.nDirtyRects=0;
                    List<Rectangle> dr = this.cDirtyRects;
                    for (Rectangle drr : dr) {
                        if (!drr.isEmpty()) {
                            ctx.rect((int) drr.x|0, (int)drr.y|0, 1+((int)drr.width|0), 1+((int)drr.height|0) );
                            this.nDirtyRects++;
                        }
                    }
                    ctx.clip();
                } else {
                    ctx.clearRect(0, 0, this.canvas.getCoordinateSpaceWidth(), this.canvas.getCoordinateSpaceHeight());
                }
                
            } else if (this.clear==true ) {
                ctx.clearRect(0, 0, this.canvas.getCoordinateSpaceWidth(), this.canvas.getCoordinateSpaceHeight());
            }

            for( int i=0; i<ne; i++ ) {
                // TODO Check ?
                c = (Scene) this.childrenList.get(i);
				if (c.isInAnimationFrame(this.time)) {
                    double tt = c.time - c.start_time;
                    ctx.save();
                    
                    if ( c.onRenderStart != null) {
                        c.onRenderStart.call(tt);
                    }
                    
                    if ( !CAAT.DEBUG_DIRTYRECTS && this.dirtyRectsEnabled ) {
                        // TODO Check cond
                        if ( this.nDirtyRects > 0) {
                            c.paintActor(this, tt);
                        }
                    } else {
                        c.paintActor(this, tt);
                    }
                    
                    if ( c.onRenderEnd != null) {
                        c.onRenderEnd.call(tt);
                    }
                    ctx.restore();
                    
                    if (CAAT.DEBUGAABB) {
                        ctx.setGlobalAlpha(1);
                        ctx.setGlobalCompositeOperation("source-over");
                        this.modelViewMatrix.transformRenderingContextSet(ctx);
                        c.drawScreenBoundingBox(this, tt);
                    }

                    if ( !c.isPaused() ) {
                        c.time+= time;
                    }
                    
                    // TODO (!navigator.isCocoonJS
                    if ( CAAT.DEBUG ) {
                        this.statistics.size_total+= c.size_total;
                        this.statistics.size_active+= c.size_active;
                        this.statistics.size_dirtyRects= this.nDirtyRects;
                    }
                }
            }
            
            // TODO !navigator.isCocoonJS
            if (this.nDirtyRects>0 && CAAT.DEBUG && CAAT.DEBUG_DIRTYRECTS ) {
                ctx.beginPath();
                this.nDirtyRects=0;
                List<Rectangle> dr= this.cDirtyRects;
                
                for (Rectangle drr : dr) {
                    if ( !drr.isEmpty() ) {
                        ctx.rect( (int)drr.x|0, (int)drr.y|0, 1+((int)drr.width|0), 1+((int)drr.height|0) );
                        this.nDirtyRects++;
                    }
                }
                
                ctx.clip();
                ctx.setFillStyle("rgba(160,255,150,.4)");
                ctx.fillRect(0, 0, this.canvas.getCoordinateSpaceWidth(), this.canvas.getCoordinateSpaceHeight());
            }
            
            ctx.restore();
        }

        this.frameCounter++;
    }
    
    public boolean inDirtyRect(Actor actor ) {

        if ( !this.dirtyRectsEnabled || CAAT.DEBUG_DIRTYRECTS ) {
            return true;
        }

         List<Rectangle> dr = this.cDirtyRects;
        int i;
        Rectangle aabb= actor.AABB;

        for( i=0; i<dr.size(); i++ ) {
            if ( dr.get(i).intersects( aabb ) ) {
                return true;
            }
        }

        this.statistics.size_discarded_by_dirty_rects+= actor.size_total;
        return false;
    }
    
    /**
     * A director is a very special kind of actor.
     * Its animation routine simple sets its modelViewMatrix in case some transformation"s been
     * applied.
     * No behaviors are allowed for Director instances.
     * @param director {CAAT.Director} redundant reference to CAAT.Director itself
     * @param time {number} director time.
     * @throws Exception 
     */
    @Override
	public boolean animate(Director director, double time) throws Exception {
        
        this.timerManager.checkTimers(time);
        
        this.setModelViewMatrix();
        this.modelViewMatrix.getInverse(this.modelViewMatrixI);
        this.setScreenBounds();
        
        this.dirty= false;
        this.invalid= false;
        this.dirtyRectsIndex= -1;
        this.cDirtyRects.clear();
        int i, l;
        
        if ( this.dirtyRectsEnabled ) {
            List<Rectangle> sdr = this.sDirtyRects;
            if ( sdr.size() > 0) {
                for( i= 0,l=sdr.size(); i<l; i++ ) {
                    this.addDirtyRect( sdr.get(i));
                }
                this.sDirtyRects.clear();
            }
        }

        for (Actor child : this.childrenList) {
            double tt = child.time - child.start_time;
            child.animate(this, tt);
        }
        
        this.timerManager.removeExpiredTimers();
        
        return true;
	}
    
    /**
     * This method is used when asynchronous operations must produce some dirty rectangle painting.
     * This means that every operation out of the regular CAAT loop must add dirty rect operations
     * by calling this method.
     * For example setVisible() and remove.
     * @param rectangle
     */
    public void scheduleDirtyRect(Rectangle rectangle ) {
        this.sDirtyRects.add( rectangle );
    }
    
    /**
     * Add a rectangle to the list of dirty screen areas which should be redrawn.
     * This is the opposite method to clear the whole screen and repaint everything again.
     * Despite i'm not very fond of dirty rectangles because it needs some extra calculations, this
     * procedure has shown to be speeding things up under certain situations. Nevertheless it doesn't or
     * even lowers performance under others, so it is a developer choice to activate them via a call to
     * setClear( CAAT.Director.CLEAR_DIRTY_RECTS ).
     *
     * This function, not only tracks a list of dirty rectangles, but tries to optimize the list. Overlapping
     * rectangles will be removed and intersecting ones will be unioned.
     *
     * Before calling this method, check if this.dirtyRectsEnabled is true.
     *
     * @param rectangle {CAAT.Rectangle}
     */
    public void addDirtyRect(Rectangle rectangle ) {

        if ( rectangle.isEmpty() ) {
            return;
        }

        int i, j;
        List<Rectangle> cdr = this.cDirtyRects;
        for( i=0; i<cdr.size(); i++ ) {
            Rectangle dr = cdr.get(i);
            if (!dr.isEmpty() && dr.intersects( rectangle ) ) {
                boolean intersected= true;
                while (intersected) {
                    dr.unionRectangle( rectangle );
    
                    for( j=0; j<cdr.size(); j++ ) {
                        if ( j!=i ) {
                            Rectangle drj = cdr.get(j);
                            if (!drj.isEmpty() && drj.intersects( dr ) ) {
                                dr.unionRectangle( drj );
                                drj.setEmpty();
                                break;
                            }
                        }
                    }
                    
                    if ( j==cdr.size() ) {
                        intersected= false;
                    }
                }

                for( j=0; j<cdr.size(); j++ ) {
                    if ( cdr.get(j).isEmpty() ) {
                        // TODO Check
                        cdr.remove(j);
                    }
                }

                return;
            }
        }

        this.dirtyRectsIndex++;

        if ( this.dirtyRectsIndex>=this.dirtyRects.size() ) {
            for( i=0; i<32; i++ ) {
                this.dirtyRects.add( new Rectangle() );
            }
        }

        Rectangle r= this.dirtyRects.get( this.dirtyRectsIndex );

        r.x= rectangle.x;
        r.y= rectangle.y;
        r.x1= rectangle.x1;
        r.y1= rectangle.y1;
        r.width= rectangle.width;
        r.height= rectangle.height;

        this.cDirtyRects.add( r );

    }

	/**
     * This method draws an Scene to an offscreen canvas. This offscreen canvas is also a child of
     * another Scene (transitionScene). So instead of drawing two scenes while transitioning from one to another,
     * first of all an scene is drawn to offscreen, and that image is translated.
     * <p>
     * Until the creation of this method, both scenes where drawn while transitioning with its performance
     * penalty since drawing two scenes could be twice as expensive than drawing only one.
     * <p>
     * Though a high performance increase, we should keep an eye on memory consumption.
     *
     * @param ctx a <code>canvas.getContext("2d")</code> instnce.
     * @param scene {CAAT.Scene} the scene to draw offscreen.
	 * @throws Exception 
     */
    public void renderToContext(CaatjaContext2d ctx, Scene scene ) throws Exception {
        /**
         * draw actors on scene.
         */
        if (scene.isInAnimationFrame(this.time)) {
            ctx.setTransform(1,0,0,1, 0,0);
            
            ctx.setGlobalAlpha(1);
            ctx.setGlobalCompositeOperation("source-over");
            ctx.clearRect(0,0,this.width,this.height);

            CaatjaContext2d octx= this.ctx;
            
            this.ctx = ctx;
            
            ctx.save();
            
            /**
             * to draw an scene to an offscreen canvas, we have to:
             *   1.- save diector"s world model view matrix
             *   2.- set no transformation on director since we want the offscreen to
             *       be drawn 1:1.
             *   3.- set world dirty flag, so that the scene will recalculate its matrices
             *   4.- animate the scene
             *   5.- paint the scene
             *   6.- restore world model view matrix.
             */
            Matrix matmv= this.modelViewMatrix;
            Matrix matwmv=  this.worldModelViewMatrix;
            this.worldModelViewMatrix= new Matrix();
            this.modelViewMatrix= this.worldModelViewMatrix;
            this.wdirty= true;
                scene.animate(this, scene.time);
                if ( scene.onRenderStart != null) {
                    scene.onRenderStart.call(scene.time);
                }
                scene.paintActor(this, scene.time);
                if ( scene.onRenderEnd != null) {
                    scene.onRenderEnd.call(scene.time);
                }
            this.worldModelViewMatrix = matwmv;
            this.modelViewMatrix= matmv;
            
            ctx.restore();
            
            this.ctx= octx;
            
        }

    }
    
    /**
     * Add a new Scene to Director"s Scene list. By adding a Scene to the Director
     * does not mean it will be immediately visible, you should explicitly call either
     * <ul>
     *  <li>easeIn
     *  <li>easeInOut
     *  <li>easeInOutRandom
     *  <li>setScene
     *  <li>or any of the scene switching methods
     * </ul>
     *
     * @param scene {CAAT.Scene} an CAAT.Scene object.
     */
    public void addScene (Scene scene ) {
        scene.setBounds(0,0,this.width,this.height);
        this.scenes.add(scene);
        scene.setEaseListener(this);
        if ( null==this.currentScene ) {
            this.setScene(0);
        }
    }
    
    /**
     * Private
     * Gets a contained Scene index on this Director.
     *
     * @param scene a CAAT.Foundation.Scene object instance.
     *
     * @return {number}
     */
    private int findScene(Scene scene) {
        return this.scenes.indexOf(scene);
    }
    
    /**
     * Private
     * Removes a scene from this director.
     *
     * @param scene a CAAT.Foundation.Scene object instance or scene index.
     *
     * @return {number}
     */
    private void removeScene(Scene scene) {
       this.scenes.remove(scene);
    }
    
    // Add by me
    private void removeScene(int scene) {
        this.scenes.remove(scene);
    }
    
    /**
     * Get the number of scenes contained in the Director.
     * @return {number} the number of scenes contained in the Director.
     */
    public int getNumScenes () {
        return this.scenes.size();
    }
    
    /**
     * This method offers full control over the process of switching between any given two Scenes.
     * To apply this method, you must specify the type of transition to apply for each Scene and
     * the anchor to keep the Scene pinned at.
     * <p>
     * The type of transition will be one of the following values defined in CAAT.Scene.prototype:
     * <ul>
     *  <li>EASE_ROTATION
     *  <li>EASE_SCALE
     *  <li>EASE_TRANSLATION
     * </ul>
     *
     * <p>
     * The anchor will be any of these values defined in CAAT.Actor.prototype:
     * <ul>
     *  <li>ANCHOR_CENTER
     *  <li>ANCHOR_TOP
     *  <li>ANCHOR_BOTTOM
     *  <li>ANCHOR_LEFT
     *  <li>ANCHOR_RIGHT
     *  <li>ANCHOR_TOP_LEFT
     *  <li>ANCHOR_TOP_RIGHT
     *  <li>ANCHOR_BOTTOM_LEFT
     *  <li>ANCHOR_BOTTOM_RIGHT
     * </ul>
     *
     * <p>
     * In example, for an entering scene performing a EASE_SCALE transition, the anchor is the
     * point by which the scene will scaled.
     *
     * @param inSceneIndex integer indicating the Scene index to bring in to the Director.
     * @param typein integer indicating the type of transition to apply to the bringing in Scene.
     * @param anchorin integer indicating the anchor of the bringing in Scene.
     * @param outSceneIndex integer indicating the Scene index to take away from the Director.
     * @param typeout integer indicating the type of transition to apply to the taking away in Scene.
     * @param anchorout integer indicating the anchor of the taking away Scene.
     * @param time inteter indicating the time to perform the process of switchihg between Scene object
     * in milliseconds.
     * @param alpha boolean boolean indicating whether alpha transparency fading will be applied to
     * the scenes.
     * @param interpolatorIn CAAT.Interpolator object to apply to entering scene.
     * @param interpolatorOut CAAT.Interpolator object to apply to exiting scene.
     * @throws Exception 
     */
    public void easeInOut (int inSceneIndex, Ease typein, Anchor anchorin, int outSceneIndex,Ease typeout,Anchor anchorout,double time,boolean alpha, Interpolator interpolatorIn, Interpolator interpolatorOut ) throws Exception {

        if ( inSceneIndex==this.getCurrentSceneIndex() ) {
            return;
        }

        Scene ssin=this.scenes.get( inSceneIndex );
        Scene sout=this.scenes.get( outSceneIndex );
        
        // TODO Check css ?
        if (CAAT.CACHE_SCENE_ON_CHANGE) {
            this.renderToContext( this.transitionScene.ctx, sout );
            sout=this.transitionScene;
        }
        
        ssin.setExpired(false);
        sout.setExpired(false);

        ssin.mouseEnabled= false;
        sout.mouseEnabled= false;

        ssin.resetTransform();
        sout.resetTransform();

        ssin.setLocation(0,0);
        sout.setLocation(0,0);

        ssin.alpha = 1;
        sout.alpha = 1;
        
        if (typein==Scene.Ease.ROTATION) {
            ssin.easeRotationIn(time, alpha, anchorin, interpolatorIn );
        } else if (typein==Scene.Ease.SCALE) {
            ssin.easeScaleIn(0,time, alpha, anchorin, interpolatorIn );
        } else {
            ssin.easeTranslationIn(time,alpha,anchorin,interpolatorIn );
        }
        
        if (typeout==Scene.Ease.ROTATION) {
            sout.easeRotationOut(time, alpha, anchorout, interpolatorOut );
        } else if (typeout==Scene.Ease.SCALE) {
            sout.easeScaleOut(0,time, alpha, anchorout, interpolatorOut );
        } else {
            sout.easeTranslationOut(time,alpha,anchorout, interpolatorOut);
        }
        
        this.childrenList.clear();
        
        sout.goOut(ssin);
        ssin.getIn(sout);

        this.addChild(sout);
        this.addChild(ssin);
    }

    /**
     * This method will switch between two given Scene indexes (ie, take away scene number 2,
     * and bring in scene number 5).
     * <p>
     * It will randomly choose for each Scene the type of transition to apply and the anchor
     * point of each transition type.
     * <p>
     * It will also set for different kind of transitions the following interpolators:
     * <ul>
     * <li>EASE_ROTATION    -> ExponentialInOutInterpolator, exponent 4.
     * <li>EASE_SCALE       -> ElasticOutInterpolator, 1.1 and .4
     * <li>EASE_TRANSLATION -> BounceOutInterpolator
     * </ul>
     *
     * <p>
     * These are the default values, and could not be changed by now.
     * This method in final instance delegates the process to easeInOutMethod.
     *
     * @see easeInOutMethod.
     *
     * @param inIndex integer indicating the entering scene index.
     * @param outIndex integer indicating the exiting scene index.
     * @param time integer indicating the time to take for the process of Scene in/out in milliseconds.
     * @param alpha boolean indicating whether alpha transparency fading should be applied to transitions.
     * @throws Exception 
     */
    public void easeInOutRandom(int inIndex, int outIndex, double time, boolean alpha) throws Exception {

        double pin = Math.random();
        double pout = Math.random();

        Ease typeIn;
        Interpolator interpolatorIn;

        if (pin < .33) {
            typeIn = Scene.Ease.ROTATION;
            interpolatorIn = Interpolator.createExponentialInOutInterpolator(4, false);
        } else if (pin < .66) {
            typeIn = Scene.Ease.SCALE;
            interpolatorIn = Interpolator.createElasticOutInterpolator(1.1, .4, false);
        } else {
            typeIn = Scene.Ease.TRANSLATE;
            interpolatorIn = Interpolator.createBounceOutInterpolator(false);
        }

        Ease typeOut;
        Interpolator interpolatorOut;

        if (pout < .33) {
            typeOut = Scene.Ease.ROTATION;
            interpolatorOut = Interpolator.createExponentialInOutInterpolator(4, false);
        } else if (pout < .66) {
            typeOut = Scene.Ease.SCALE;
            interpolatorOut = Interpolator.createExponentialOutInterpolator(4, false);
        } else {
            typeOut = Scene.Ease.TRANSLATE;
            interpolatorOut = Interpolator.createBounceOutInterpolator(false);
        }

        // Add by me
        int i = (int) (Math.random() * 8.99) >> 0;
        Anchor anchorIn = Anchor.CENTER;
        switch (i) {
        case 0:
            anchorIn = Anchor.CENTER;
            break;
        case 1:
            anchorIn = Anchor.TOP;
            break;
        case 2:
            anchorIn = Anchor.BOTTOM;
            break;
        case 3:
            anchorIn = Anchor.LEFT;
            break;
        case 4:
            anchorIn = Anchor.RIGHT;
            break;
        case 5:
            anchorIn = Anchor.TOP_LEFT;
            break;
        case 6:
            anchorIn = Anchor.TOP_RIGHT;
            break;
        case 7:
            anchorIn = Anchor.BOTTOM_LEFT;
            break;
        case 8:
            anchorIn = Anchor.BOTTOM_RIGHT;
            break;
        }

        int j = (int) (Math.random() * 8.99) >> 0;

        Anchor anchorOut = Anchor.CENTER;
        switch (j) {
        case 0:
            anchorOut = Anchor.CENTER;
            break;
        case 1:
            anchorOut = Anchor.TOP;
            break;
        case 2:
            anchorOut = Anchor.BOTTOM;
            break;
        case 3:
            anchorOut = Anchor.LEFT;
            break;
        case 4:
            anchorOut = Anchor.RIGHT;
            break;
        case 5:
            anchorOut = Anchor.TOP_LEFT;
            break;
        case 6:
            anchorOut = Anchor.TOP_RIGHT;
            break;
        case 7:
            anchorOut = Anchor.BOTTOM_LEFT;
            break;
        case 8:
            anchorOut = Anchor.BOTTOM_RIGHT;
            break;
        }

        this.easeInOut(inIndex, typeIn, anchorIn,

        outIndex, typeOut, anchorOut,

        time, alpha,

        interpolatorIn, interpolatorOut);
    }

    /**
     * This method changes Director"s current Scene to the scene index indicated by
     * inSceneIndex parameter. The Scene running in the director won"t be eased out.
     *
     * @see {CAAT.Interpolator}
     * @see {CAAT.Actor}
     * @see {CAAT.Scene}
     *
     * @param inSceneIndex integer indicating the new Scene to set as current.
     * @param type integer indicating the type of transition to apply to bring the new current
     * Scene to the Director. The values will be one of: CAAT.Scene.prototype.EASE_ROTATION,
     * CAAT.Scene.prototype.EASE_SCALE, CAAT.Scene.prototype.EASE_TRANSLATION.
     * @param time integer indicating how much time in milliseconds the Scene entrance will take.
     * @param alpha boolean indicating whether alpha transparency fading will be applied to the
     * entereing Scene.
     * @param anchor integer indicating the anchor to fix for Scene transition. It will be any of
     * CAAT.Actor.prototype.ANCHOR_* values.
     * @param interpolator an CAAT.Interpolator object indicating the interpolation function to
     * apply.
     */
    public void easeIn(int inSceneIndex, Ease type, double time, boolean alpha, Anchor anchor, Interpolator interpolator) {
        Scene sin = this.scenes.get(inSceneIndex);
        
        if (type == Scene.Ease.ROTATION) {
            sin.easeRotationIn(time, alpha, anchor, interpolator);
        } else if (type == Scene.Ease.SCALE) {
            sin.easeScaleIn(0, time, alpha, anchor, interpolator);
        } else {
            sin.easeTranslationIn(time, alpha, anchor, interpolator);
        }
        
        this.childrenList.clear();
        this.addChild(sin);

        sin.resetTransform();
        sin.setLocation(0, 0);
        sin.alpha = 1;
        sin.mouseEnabled = false;
        sin.setExpired(false);
    }
    
    /**
     * Changes (or sets) the current Director scene to the index
     * parameter. There will be no transition on scene change.
     * @param sceneIndex {number} an integer indicating the index of the target Scene
     * to be shown.
     */
    public void setScene (int sceneIndex ) {
        Scene sin= this.scenes.get(sceneIndex);
        this.childrenList.clear();
        this.addChild(sin);
        this.currentScene= sin;
        
        sin.setExpired(false);
        sin.mouseEnabled= true;
        sin.resetTransform();
        sin.setLocation(0,0);
        sin.alpha = 1;
        
        // TODO Check
        sin.getIn(null);
        sin.activated();
    }
    
    // Add by me
    public void setScene(Scene sin) {
        this.childrenList.clear();
        this.addChild(sin);
        this.currentScene= sin;
        
        sin.setExpired(false);
        sin.mouseEnabled= true;
        sin.resetTransform();
        sin.setLocation(0,0);
        sin.alpha = 1;
        
        // TODO Check
        sin.getIn(null);
        sin.activated();
    }
    
    /**
     * This method will change the current Scene by the Scene indicated as parameter.
     * It will apply random values for anchor and transition type.
     * @see easeInOutRandom
     *
     * @param iNewSceneIndex {number} an integer indicating the index of the new scene to run on the Director.
     * @param time {number} an integer indicating the time the Scene transition will take.
     * @param alpha {boolean} a boolean indicating whether Scene transition should be fading.
     * @param transition {boolean} a boolean indicating whether the scene change must smoothly animated.
     * @throws Exception 
     */
    public void switchToScene (int iNewSceneIndex,double time,boolean alpha, boolean transition ) throws Exception {
        int currentSceneIndex= this.getSceneIndex(this.currentScene);
        
        if (!transition) {
            this.setScene(iNewSceneIndex);
        }
        else {
            this.easeInOutRandom( iNewSceneIndex, currentSceneIndex, time, alpha );
        }           
    }
    
    /**
     * Sets the previous Scene in sequence as the current Scene.
     * @see switchToScene.
     *
     * @param time {number} integer indicating the time the Scene transition will take.
     * @param alpha {boolean} a boolean indicating whether Scene transition should be fading.
     * @param transition {boolean} a boolean indicating whether the scene change must smoothly animated.
     * @throws Exception 
     */
    public void switchToPrevScene (double time,boolean  alpha,boolean transition) throws Exception {
        
        int currentSceneIndex= this.getSceneIndex(this.currentScene);
        
        if ( this.getNumScenes()<=1 || currentSceneIndex==0 ) {
            return;
        }
        
        if (!transition) {
            this.setScene(currentSceneIndex-1);
        }
        else {
            this.easeInOutRandom( currentSceneIndex-1, currentSceneIndex, time, alpha );
        }
    }
    
    /**
     * Sets the previous Scene in sequence as the current Scene.
     * @see switchToScene.
     *
     * @param time {number} integer indicating the time the Scene transition will take.
     * @param alpha {boolean} a boolean indicating whether Scene transition should be fading.
     * @param transition {boolean} a boolean indicating whether the scene change must smoothly animated.
     * @throws Exception 
     */
    public void switchToNextScene(double time,boolean  alpha,boolean transition) throws Exception {
        
        int currentSceneIndex= this.getSceneIndex(this.currentScene);
        
        if ( this.getNumScenes()<=1 || currentSceneIndex==this.getNumScenes()-1 ) {
            return;
        }

        if (!transition) {
            this.setScene(currentSceneIndex+1);
        }
        else {
            this.easeInOutRandom( currentSceneIndex+1, currentSceneIndex, time, alpha );
        }
    }

    public void mouseEnter(CAATMouseEvent mouseEvent) {
    }

    public void mouseExit(CAATMouseEvent mouseEvent) {
    }

    public void mouseMove(CAATMouseEvent mouseEvent) {
    }

    public void mouseDown(CAATMouseEvent mouseEvent) {
    }

    public void mouseUp(CAATMouseEvent mouseEvent) {
    }

    public void mouseDrag(CAATMouseEvent mouseEvent) {
    }
    
    /**
     * Scene easing listener. Notifies scenes when they"re about to be activated (set as current
     * director"s scene).
     *
     * @param scene {CAAT.Scene} the scene that has just been brought in or taken out of the director.
     * @param b_easeIn {boolean} scene enters or exits ?
     */
    public void easeEnd (Scene scene, boolean b_easeIn ) {
        // scene is going out
        if (!b_easeIn) {
            scene.setExpired(true);
        } else {
            this.currentScene = scene;
            this.currentScene.activated();
        }

        scene.mouseEnabled = true;
        scene.emptyBehaviorList();
    }
    
    /**
     * Return the index for a given Scene object contained in the Director.
     * @param scene {CAAT.Scene}
     */
    public int getSceneIndex(Scene scene) {
        return this.scenes.indexOf(scene);
    }
    
    /**
     * Get a concrete director"s scene.
     * @param index {number} an integer indicating the scene index.
     * @return {CAAT.Scene} a CAAT.Scene object instance or null if the index is oob.
     */
    public Scene getScene(int index ) {
        return this.scenes.get(index);
    }
    
    public Scene getSceneById(String id) {
        for (Scene scene : this.scenes) {
            if (scene.id.equals(id)) {
                return scene;
            }
        }
        return null;
    }
    
    /**
     * Return the index of the current scene in the Director"s scene list.
     * @return {number} the current scene"s index.
     */
    public int getCurrentSceneIndex () {
        return this.getSceneIndex(this.currentScene);
    }
    
    /**
     * Return the running browser name.
     * @return {string} the browser name.
     */
    public String getBrowserName () {
        return browserInfo.browser;
    }
    
    /**
     * Return the running browser version.
     * @return {string} the browser version.
     */
    public String getBrowserVersion () {
        return browserInfo.version;
    }
    
    /**
     * Return the operating system name.
     * @return {string} the os name.
     */
    public String getOSName () {
        return browserInfo.OS;
    }
    
    /**
     * Gets the resource with the specified resource name.
     * The Director holds a collection called <code>imagesCache</code>
     * where you can store a JSON of the form
     *  <code>[ { id: imageId, image: imageObject } ]</code>.
     * This structure will be used as a resources cache.
     * There"s a CAAT.ImagePreloader class to preload resources and
     * generate this structure on loading finalization.
     *
     * @param sId {object} an String identifying a resource.
     */
    public CaatjaImage getImage (String sId) {
        return imagesCache.get(sId);
    }
    
    public AudioManager musicPlay(String id) {
        return this.audioManager.playMusic(id);
    }
    
    public void musicStop() {
        this.audioManager.stopMusic();
    }
    
    /**
     * Adds an audio to the cache.
     *
     * @see CAAT.AudioManager.addAudio
     * @return this
     */
    public Director addAudio(String id, String url) { 
    // FIXME call
//        this.audioManager.addAudio(id, url, null);
        return this;
    }
    
    /**
     * Plays the audio instance identified by the id.
     * @param id {object} the object used to store a sound in the audioCache.
     * @return 
     */
    public AudioManager audioPlay(String id) {
        return this.audioManager.play(id);
    }

    /**
     * Loops an audio instance identified by the id.
     * @param id {object} the object used to store a sound in the audioCache.
     * @return 
     *
     * @return {HTMLElement|null} the value from audioManager.loop
     * 
     * TODO Audio
     */
//    public AudioElement audioLoop(String id) {
//        return this.audioManager.loop(id);
//    }
    
    public AudioManager endSound() {
        return this.audioManager.endSound();
    }
    
    public AudioManager setSoundEffectsEnabled (boolean enabled) {
        return this.audioManager.setSoundEffectsEnabled(enabled);
    }
    
    public AudioManager setMusicEnabled (boolean enabled) {
        return this.audioManager.setMusicEnabled(enabled);
    }
    
    public boolean isMusicEnabled () {
        return this.audioManager.isMusicEnabled();
    }
    
    public boolean isSoundEffectsEnabled () {
        return this.audioManager.isSoundEffectsEnabled();
    }
    
    public AudioManager setVolume(String id, double volume ) {
        return this.audioManager.setVolume( id, volume );
    }
    
    /**
     * Removes Director"s scenes.
     * FIXME empty current scene, clear children list !!! 
     */
    public void emptyScenes () {
        this.scenes.clear();
    }
    
    /**
     * Adds an scene to this Director.
     * @param scene {CAAT.Scene} a scene object.
     */
    public void addChild(Scene scene) {
        scene.parent= this;
        this.childrenList.add(scene);
    };
    
    /**
     * @Deprecated use CAAT.loop instead.
     * @param fps
     * @param callback
     * @param callback2
     * @throws Exception 
     */
    @Deprecated
	public void loop(int fps, final DirectorCallback callback,
			DirectorCallback callback2) throws Exception {
		if (callback2 != null) {
			this.onRenderStart = callback;
			this.onRenderEnd = callback2;
		} else if (callback != null) {
			this.onRenderEnd = callback;
		}
		
		Caatja.loop(fps);
	}

    /**
     * Starts the director animation.If no scene is explicitly selected, the
     * current Scene will be the first scene added to the Director.
     * <p>
     * The fps parameter will set the animation quality. Higher values, means
     * CAAT will try to render more frames in the same second (at the expense of
     * cpu power at least until hardware accelerated canvas rendering context
     * are available). A value of 60 is a high frame rate and should not be
     * exceeded.
     * 
     * @param fps
     *            {number} integer value indicating the target frames per second
     *            to run the animation at.
     * @throws Exception 
     */
    
    public void renderFrame() throws Exception {
        
        CAAT.currentDirector = this;
        
        if (this.stopped) {
            return;
        }

        double t = Caatja.getTime(), delta = t - this.timeline;
        
        /*
        check for massive frame time. if for example the current browser tab is minified or taken out of
        foreground, the system will account for a bit time interval. minify that impact by lowering down
        the elapsed time (virtual timelines FTW)
         */
        if ( delta > 500 ) {
            delta= 500;
        }
        
        if ( this.onRenderStart != null) {
            this.onRenderStart.call(this, delta);
        }

        this.render(delta);
        
        if (this.debugInfo != null) {
            // TODO Different from source
            this.debugInfo.debugInfo(this.statistics);
        }
        
        this.timeline = t;

        if (this.onRenderEnd != null) {
            this.onRenderEnd.call(this, delta);
        }
        
        this.needsRepaint= false;
    }
    
    /**
     * If the director has renderingMode: DIRTY, the timeline must be reset to register accurate frame measurement.
     */
    public void resetTimeline() {
        this.timeline= Caatja.getTime();
    }
    
    /**
     * @Deprecated use CAAT.loop instead.
     * @param fps
     * @throws Exception 
     */
    @Deprecated
    public void loop(int fps) throws Exception {
        loop(fps, null, null);
    }
    
    public void endLoop() {
    }
    
    /**
     * This method states whether the director must clear background before rendering
     * each frame.
     *
     * The clearing method could be:
     *  + CAAT.Director.CLEAR_ALL. previous to draw anything on screen the canvas will have clearRect called on it.
     *  + CAAT.Director.CLEAR_DIRTY_RECTS. Actors marked as invalid, or which have been moved, rotated or scaled
     *    will have their areas redrawn.
     *  + CAAT.Director.CLEAR_NONE. clears nothing.
     *
     * @param clear {CAAT.Director.CLEAR_ALL |�CAAT.Director.CLEAR_NONE | CAAT.Director.CLEAR_DIRTY_RECTS}
     * @return this.
     */
    public Director setClear(boolean clear) {
        this.clear= clear;
        if ( this.clear == CLEAR_DIRTY_RECTS ) {
            this.dirtyRectsEnabled= true;
        }
        return this;
    }
    
    /**
     * Get this Director"s AudioManager instance.
     * @return {CAAT.AudioManager} the AudioManager instance.
     */
    public AudioManager getAudioManager() {
        return audioManager;
    }
    
    // Add by me
    private class OffsetResult {
        int x;
        int y;
        String style;

        public OffsetResult(int x, int y, String style) {
            this.x = x;
            this.y = y;
            this.style = style;
        }
    }
    
    /**
     * Acculumate dom elements position to properly offset on-screen mouse/touch events.
     * @param node
     * 
     * FIXME Node  value ?
     */
//    public OffsetResult cumulateOffset(Node node, String parent, String prop) {
//        String left= prop+"Left";
//        String top= prop+"Top";
//        int x=0, y=0;
//        String style = "";

        // FIXME TODO and not up to date
//        while(!navigator.browser.equals("iOS") && node && node.style ) {
//            if ( node.currentStyle ) {
//                style= node.currentStyle["position"];
//            } else {
//                style= (node.ownerDocument.defaultView || node.ownerDocument.parentWindow).getComputedStyle(node, null);
//                style= style ? style.getPropertyValue("position") : null;
//            }
//
//            // if (!/^(relative|absolute|fixed)$/.test(style)) {
//         if (!/^(fixed)$/.test(style)) {
//                x+= node[left];
//                y+= node[top];
//                node = node[parent];
//            } else {
//                break;
//            }
//        }
//        
//        return new OffsetResult(x, y, style);
//
//    }
    
    /**
     * 
     * FIXME Object value
     * 
     * @param node
     * @return
     */
//    public Pt getOffset(Node node ) {
//        OffsetResult res= this.cumulateOffset(node, "offsetParent", "offset");
//        if ( res.style.equals("fixed")) {
//            OffsetResult res2= this.cumulateOffset(node, node.getParentNode() != null ? "parentNode" : "parentElement", "scroll");
//            return new Pt(res.x + res2.x, res.y + res2.y);
//        }
//
//        return new Pt(res.x, res.y);
//    }
    
    /**
     * Enable canvas input events.
     * Events sent to any CAAT.Actor instance will be as follows:
     *
     * MouseEnter
     *  if (press)
     *      MouseDown
     *      if (drag)
     *          MouseDrag
     *  if (release)
     *      MouseUp
     *      MouseClicked
     *  MouseExit
     *
     */
    public boolean in_ = false; // Moved by me

//    private void __enableEvents() {
//        CAAT.registerDirector(this);
//        
//        Canvas canvas = this.canvas;
//        in_ = false;
//        
//        canvas.addDomHandler(new MouseUpHandler() {
//            @Override
//            public void onMouseUp(MouseUpEvent event) {
//                
//                event.preventDefault();
//                
//                isMouseDown = false;
//                getCanvasCoord(mousePoint, event);
//                
//                Pt pos= null;
//                
//                Actor lactor = lastSelectedActor;
//                
//                if (null != lactor) {
//                    
//                    pos = lactor.viewToModel(new Pt(mousePoint.x, mousePoint.y, 0));
//                    
//                    // FIXME Remove comment when asButton works in Actor
////                    if ( lactor.actionPerformed && lastSelectedActor.contains(pos.x, pos.y) ) {
////                        lactor.actionPerformed(event);
////                    }
//                    
//                    lactor.mouseUp(
//                            new CAATMouseEvent().init(
//                                    pos.x,
//                                    pos.y,
//                                    event,
//                                    lactor,
//                                    screenMousePoint));
//                }
//
//                    if (!dragging && null != lactor) {
//                        // Add by me
//                        pos = lactor.viewToModel(new Pt(mousePoint.x, mousePoint.y, 0));
//                        if (lactor.contains(pos.x, pos.y)) {
//                            
//                            try {
//                                lactor.mouseClick(
//                                        new CAATMouseEvent().init(
//                                                pos.x,
//                                                pos.y,
//                                                event,
//                                                lactor,
//                                                screenMousePoint));
//                            } catch (Exception e) {
//                                // TODO Auto-generated catch block
//                                e.printStackTrace();
//                            }
//                            
//                            dragging = false;
//                            in_= false;
//                            CAAT.setCursor("default");
//                            
//                        }
//                        
//                    }
//
//            }
//        }, MouseUpEvent.getType());
//
//        
//        canvas.addDomHandler(new MouseDownHandler() {
//            @Override
//            public void onMouseDown(MouseDownEvent event) {
//                
//                event.preventDefault();
//                
//                getCanvasCoord(mousePoint, event);
//                
//                isMouseDown = true;
//                Actor lactor = findActorAtPosition(mousePoint);
////                double px= mousePoint.x;
////                double py= mousePoint.y;
//                
//                if (null != lactor) {
//                    
//                    Pt pos = lactor.viewToModel(mousePoint );
//                    
//                    // to calculate mouse drag threshold
////                    prevMousePoint.x= px;
////                    prevMousePoint.y= py;
//                    lactor.mouseDown(
//                            new CAATMouseEvent().init(
//                                    pos.x,
//                                    pos.y,
//                                    event,
//                                    lactor,
//                                    new Pt(screenMousePoint.x, screenMousePoint.y)));
//                }
//                
//                lastSelectedActor = lactor;
//            }
//        }, MouseDownEvent.getType());
//        
//        canvas.addDomHandler(new MouseOverHandler() {
//            @Override
//            public void onMouseOver(MouseOverEvent event) {
//                
//                event.preventDefault();
//                getCanvasCoord(mousePoint, event);
//                
//                Actor lactor = findActorAtPosition(mousePoint);
//                Pt pos = null;
//                
//                if (null != lactor) {
//                    
//                    pos= lactor.viewToModel(
//                            new Pt(screenMousePoint.x, screenMousePoint.y, 0));
//                    
//                    lactor.mouseEnter(
//                            new CAATMouseEvent().init(
//                                    pos.x,
//                                    pos.y,
//                                    event,
//                                    lactor,
//                                    screenMousePoint));
//                }
//                
//                lastSelectedActor= lactor;
//            }
//        }, MouseOverEvent.getType());
//        
//        canvas.addDomHandler(new MouseOutHandler() {
//            @Override
//            public void onMouseOut(MouseOutEvent event) {
//                
//                event.preventDefault();
//                
//                if (null != lastSelectedActor) {
//                    getCanvasCoord(mousePoint, event);
//                    Pt pos= new Pt( mousePoint.x, mousePoint.y, 0 );
//                    lastSelectedActor.viewToModel(pos);
//
//                    lastSelectedActor.mouseExit(
//                            new CAATMouseEvent().init(
//                                    pos.x,
//                                    pos.y,
//                                    event,
//                                    lastSelectedActor,
//                                    screenMousePoint));
//                    lastSelectedActor = null;
//                }
//                isMouseDown = false;
//                in_ = false;
//            }
//        }, MouseOutEvent.getType());
//
//        canvas.addDomHandler(new MouseMoveHandler() {
//            @Override
//            public void onMouseMove(MouseMoveEvent event) {
//                
//                event.preventDefault();
//                
//                getCanvasCoord(mousePoint, event);
//                
//                Actor lactor;
//                Pt pos;
//                
//                // drag
//                if (isMouseDown && null != lastSelectedActor) {
//                    
////                    // check for mouse move threshold.
////                    if ( !dragging ) {
////                        if ( Math.abs(prevMousePoint.x-mousePoint.x)< CAAT.DRAG_THRESHOLD_X &&
////                             Math.abs(prevMousePoint.y-mousePoint.y)< CAAT.DRAG_THRESHOLD_Y ) {
////                            return;
////                        }
////                    }
//                    
//                    lactor = lastSelectedActor;
//                    
//                    pos = lactor.viewToModel(
//                            new Pt(screenMousePoint.x, screenMousePoint.y, 0));
//                    
//                    dragging = true;
//                    
//                    double px= lactor.x;
//                    double py= lactor.y;
//                    
////                    Pt p= new Pt(mousePoint.x, mousePoint.y, 0);
////                    
////                    if (null != lastSelectedActor.parent) {
////                        lastSelectedActor.parent.viewToModel(mousePoint);
////                    }
////                    
////                    double px= lastSelectedActor.x;
////                    double py= lastSelectedActor.y;
//                    lactor.mouseDrag(
//                            new CAATMouseEvent().init(
//                                    pos.x,
//                                    pos.y,
//                                    event,
//                                    lactor,
//                                    new Pt(screenMousePoint.x, screenMousePoint.y)));
//                    
//                    prevMousePoint.x= pos.x;
//                    prevMousePoint.y= pos.y;
//                    
//                    /**
//                     * Element has not moved after drag, so treat it as a button.
//                     * 
//                     */
//                    if ( px==lactor.x && py==lactor.y )   {
//                        
//                        boolean contains= lactor.contains(pos.x, pos.y);
////                        lastSelectedActor.viewToModel( p );
//
//                        if (in_ && !contains) {
//                            lactor.mouseExit(
//                                new CAATMouseEvent().init(
//                                    pos.x,
//                                    pos.y,
//                                    event,
//                                    lactor,
//                                    screenMousePoint));
//                            in_ = false;
//                        }
//
//                        if (!in_ && contains) {
//                            lactor.mouseEnter(
//                                new CAATMouseEvent().init(
//                                    pos.x,
//                                    pos.y,
//                                    event,
//                                    lactor,
//                                    screenMousePoint));
//                            in_ = true;
//                        }
//                    }
//                    
//                    
//                    return;
//                }
//                
//                in_= true;
//
//                lactor = findActorAtPosition(mousePoint);
//                
//                // Add by me
//                if (lactor == null) {
//                    return;
//                }
//                
//                // cambiamos de actor.
//                if (lactor != lastSelectedActor) {
//                    if (null != lastSelectedActor) {
//                        
//                        pos = lastSelectedActor.viewToModel(
//                                new Pt(screenMousePoint.x, screenMousePoint.y, 0));
//                        
//                        lastSelectedActor.mouseExit(
//                                new CAATMouseEvent().init(
//                                        pos.x,
//                                        pos.y,
//                                        event,
//                                        lastSelectedActor,
//                                        screenMousePoint));
//                    }
//                    if (null != lactor) {
//                        
//                        pos = lactor.viewToModel(
//                                new Pt(screenMousePoint.x, screenMousePoint.y, 0));
//                        
//                        lactor.mouseEnter(
//                                new CAATMouseEvent().init(
//                                        pos.x,
//                                        pos.y,
//                                        event,
//                                        lactor,
//                                        screenMousePoint));
//                    }
//                }
//                
////                lastSelectedActor = lactor;
//                
//                pos = lactor.viewToModel(new Pt(screenMousePoint.x, screenMousePoint.y, 0));
//                
//                if (null != lactor) {
//                    try {
//                        lactor.mouseMove(
//                                new CAATMouseEvent().init(
//                                        pos.x,
//                                        pos.y,
//                                        event,
//                                        lactor,
//                                        screenMousePoint));
//                    } catch (Exception e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                    
//                    lastSelectedActor = lactor;
//                    
//                }
//            }
//        }, MouseMoveEvent.getType());
//        
//        canvas.addDomHandler(new DoubleClickHandler() {
//            @Override
//            public void onDoubleClick(DoubleClickEvent event) {
//                
//                event.preventDefault();
//                
//                getCanvasCoord(mousePoint, event);
//                if (null != lastSelectedActor) {
//                    
//                    Pt pos = lastSelectedActor.viewToModel(
//                            new Pt(screenMousePoint.x, screenMousePoint.y, 0));
//                    
//                    //lastSelectedActor.viewToModel(new Pt(mousePoint.x, mousePoint.y));
//                    
//                    try {
//                        lastSelectedActor.mouseDblClick(
//                                new CAATMouseEvent().init(
//                                        mousePoint.x,
//                                        mousePoint.y,
//                                        event,
//                                        lastSelectedActor,
//                                        screenMousePoint));
//                    } catch (Exception e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }, DoubleClickEvent.getType());
//        
//
//        canvas.addDomHandler(new TouchStartHandler() {
//            @Override
//            public void onTouchStart(TouchStartEvent event) {
//                event.getChangedTouches();
//            }
//        }, TouchStartEvent.getType());
//
//        canvas.addDomHandler(new TouchStartHandler() {
//            @Override
//            public void onTouchStart(TouchStartEvent event) {
//                touchHandler(event);
//            }
//        }, TouchStartEvent.getType());
//        
//        canvas.addDomHandler(new TouchMoveHandler() {
//            @Override
//            public void onTouchMove(TouchMoveEvent event) {
//                touchHandler(event);
//            }
//        }, TouchMoveEvent.getType());
//        
//        canvas.addDomHandler(new TouchEndHandler() {
//            
//            @Override
//            public void onTouchEnd(TouchEndEvent event) {
//                touchHandler(event);
//            }
//        }, TouchEndEvent.getType());
//        
//        canvas.addDomHandler(new TouchCancelHandler() {
//            
//            @Override
//            public void onTouchCancel(TouchCancelEvent event) {
//                touchHandler(event);
//            }
//        }, TouchCancelEvent.getType());
//        
//    }
//    
//    private static void touchHandler(TouchEvent event) {
//        JsArray<Touch> touches = event.getChangedTouches();
//        Touch first = touches.get(0);
//            String type = "";
//            
//        if (event instanceof TouchStartEvent) {
//            type = "mousedown";
//        } else if (event instanceof TouchMoveEvent) {
//            type = "mousemove";
//        } else if (event instanceof TouchEndEvent) {
//            type = "mouseup";
//        } else {
//            return;
//        }
//
//        DomEvent.fireNativeEvent(Document.get().createMouseEvent(type,
//                true,
//                true,
//                1,
//                first.getScreenX(),
//                first.getScreenY(),
//                first.getClientX(),
//                first.getClientY(),
//                false,
//                false,
//                false,
//                false,
//                0/*left*/,
//                null), RootPanel.get());
//        
//        event.preventDefault();
//    }
    
    

    // Add by me
    public boolean touching = false;

    public void __gestureStart (double scale, double rotation ) {
        this.gesturing= true;
        this.__gestureRotation= this.lastSelectedActor.rotationAngle;
        this.__gestureSX= this.lastSelectedActor.scaleX - 1;
        this.__gestureSY= this.lastSelectedActor.scaleY - 1;
    }

    public void __gestureChange (Double scale, Double rotation ) {
        // TODO Check 0 values ?
        if ( scale== null || rotation== null) {
            return;
        }

        if ( this.lastSelectedActor!=null && this.lastSelectedActor.isGestureEnabled() ) {
            this.lastSelectedActor.setRotation( rotation*Math.PI/180 + this.__gestureRotation );

            this.lastSelectedActor.setScale(
                this.__gestureSX + scale,
                this.__gestureSY + scale );
        }

    }

    public void __gestureEnd (double scale, double rotation ) {
        this.gesturing= false;
        this.__gestureRotation= 0;
        this.__gestureScale= 0;
    }
    
    // Caatja remove
//    public void __touchEndHandlerMT(TouchEvent e) {
//        
//        e.preventDefault();
//     // TODO
//        //e.returnValue = false;
//
//        int i,j;
//        List<Integer> recent= new ArrayList<Integer>();
//
//        /**
//         * extrae actores afectados, y coordenadas relativas para ellos.
//         * crear una coleccion touch-id : { actor, touch-event }
//         */
//        JsArray<Touch> touches = e.getChangedTouches();
//        for( i=0; i< touches.length(); i++ ) {
//            Touch _touch= touches.get(i);
//            int id= _touch.getIdentifier();
//            recent.add( id );
//        }
//
//        /**
//         * para los touch identificados, extraer que actores se han afectado.
//         * crear eventos con la info de touch para cada uno.
//         */
//
//        Map<String, TouchEventData> actors= new HashMap<String, TouchEventData>();
//        for( i=0; i<recent.size(); i++ ) {
//            int touchId= recent.get( i );
//            if ( this.touches.get( touchId ) != null) {
//                Actor actor= this.touches.get(touchId).actor;
//
//                if ( actors.get(actor.id) == null) {
//                    actors.put(actor.id,new TouchEventData( 
//                        actor,
//                        new CAATTouchEvent().init( e, actor, this.currentScene.time )));
//                }
//
//                CAATTouchEvent ev= actors.get( actor.id ).touch;
//                ev.addChangedTouch( this.touches.get( touchId ).touch );
//            }
//        }
//
//        /**
//         * remove ended touch info.
//         */
//        for( i=0; i< touches.length(); i++ ) {
//            Touch touch= touches.get(i);
//            int id= touch.getIdentifier();
//            this.touches.remove(id);
//        }
//
//        /**
//         * notificar a todos los actores.
//         */
//        for( String pr : actors.keySet() ) {
//            TouchEventData data= actors.get(pr);
//            Actor actor= data.actor;
//            CAATTouchEvent touch= data.touch;
//
//            for( String actorId : this.touches.keySet() ) {
//                TouchInfoData tt= this.touches.get(actorId);
//                if ( tt.actor.id.equals(actor.id)) {
//                    touch.addTouch( tt.touch );
//                }
//            }
//
//            actor.touchEnd( touch );
//        }
//    }

    // Caatja remove
//    public void __touchMoveHandlerMT(TouchEvent e) {
//        e.preventDefault();
//     // TODO
//        //e.returnValue = false;
//
//        int i;
//        List<Integer> recent= new ArrayList<Integer>();
//
//        /**
//         * extrae actores afectados, y coordenadas relativas para ellos.
//         * crear una coleccion touch-id : { actor, touch-event }
//         */
//        JsArray<Touch> touches = e.getChangedTouches();
//        for( i=0; i< touches.length(); i++ ) {
//            Touch touch= touches.get(i);
//            int id= touch.getIdentifier();
//            
//            if (this.touches.get(String.valueOf(id)) != null) {
//                Pt mp = this.mousePoint;
//                // FIXME
////                this.getCanvasCoord(mp, touch);
//                
//                Actor actor= this.touches.get( id ).actor;
//                mp= actor.viewToModel(mp);
//                
//                this.touches.put(String.valueOf(id), new TouchInfoData(actor, new TouchInfo(String.valueOf(id), (int) mp.x, (int) mp.y, actor )));
//
//                recent.add( id );
//            }
//
//
//
//        }
//
//        /**
//         * para los touch identificados, extraer que actores se han afectado.
//         * crear eventos con la info de touch para cada uno.
//         */
//
//        Map<String, TouchEventData> actors= new HashMap<String, TouchEventData>();
//        for( i=0; i<recent.size(); i++ ) {
//            int touchId= recent.get(i);
//            Actor actor= this.touches.get( touchId).actor;
//
//            if ( actors.get(actor.id) == null) {
//                actors.put(actor.id, new TouchEventData(
//                    actor,
//                    new CAATTouchEvent().init( e, actor, this.currentScene.time )
//                ));
//            }
//
//            CAATTouchEvent ev= actors.get(actor.id).touch;
//            ev.addTouch( this.touches.get( touchId ).touch );
//            ev.addChangedTouch( this.touches.get( touchId ).touch );
//        }
//
//        /**
//         * notificar a todos los actores.
//         */
//        for( String pr : actors.keySet() ) {
//            TouchEventData data= actors.get(pr);
//            Actor actor= data.actor;
//            CAATTouchEvent touch= data.touch;
//
//            for( String actorId : this.touches.keySet() ) {
//                TouchInfoData tt= this.touches.get(actorId);
//                if ( tt.actor.id.equals(actor.id )){
//                    touch.addTouch( tt.touch );
//                }
//            }
//
//            actor.touchMove( touch );
//        }
//    }

    // Caatja remove
//    public void __touchCancelHandleMT(TouchEvent e) {
//        this.__touchEndHandlerMT(e);
//    }

    // Caatja remove
//    public void __touchStartHandlerMT(TouchEvent e) {
//        e.preventDefault();
//     // TODO
//        //e.returnValue = false;
//
//        int i;
//        List<Integer> recent= new ArrayList<Integer>();
//        boolean allInCanvas= true;
//
//        /**
//         * extrae actores afectados, y coordenadas relativas para ellos.
//         * crear una coleccion touch-id : { actor, touch-event }
//         */
//        JsArray<Touch> touches = e.getChangedTouches();
//        for( i=0; i< touches.length(); i++ ) {
//            Touch touch= touches.get(i);
//            int id= touch.getIdentifier();
//            Pt mp= this.mousePoint;
//            // FIXME
////            this.getCanvasCoord(mp, touch);
//            if ( mp.x<0 || mp.y<0 || mp.x>=this.width || mp.y>=this.height ) {
//                allInCanvas = false;
//                continue;
//            }
//
//            Actor actor= this.findActorAtPosition(mp);
//            if ( actor!=null ) {
//                mp= actor.viewToModel(mp);
//
//                if ( this.touches.get(id) == null) {
//
//                    this.touches.put(String.valueOf(id), new TouchInfoData( 
//                        actor,
//                        new TouchInfo(String.valueOf(id), (int)mp.x, (int)mp.y, actor )
//                    ));
//
//                    recent.add( id );
//                }
//            }
//        }
//        
//        /**
//         * para los touch identificados, extraer que actores se han afectado.
//         * crear eventos con la info de touch para cada uno.
//         */
//
//        Map<String, TouchEventData> actors= new HashMap<String, TouchEventData>();
//        for( i=0; i<recent.size(); i++ ) {
//            Integer touchId= recent.get(i);
//            Actor actor= this.touches.get( touchId ).actor;
//
//            if ( actors.get(actor.id) == null) {
//                actors.put(actor.id, new TouchEventData(
//                    actor,
//                    new CAATTouchEvent().init( e, actor, this.currentScene.time )
//                ));
//            }
//
//            CAATTouchEvent ev= actors.get( actor.id ).touch;
//            ev.addTouch( this.touches.get( touchId).touch );
//            ev.addChangedTouch( this.touches.get( touchId ).touch );
//        }
//
//        /**
//         * notificar a todos los actores.
//         */
//        for( String pr : actors.keySet() ) {
//            TouchEventData data= actors.get(pr);
//            Actor actor= data.actor;
//            CAATTouchEvent touch= data.touch;
//
//            for( String actorId : this.touches.keySet() ) {
//                TouchInfoData tt= this.touches.get(actorId);
//                if ( tt.actor.id.equals(actor.id)) {
//                    touch.addTouch( tt.touch );
//                }
//            }
//
//            actor.touchStart( touch );
//        }
//    }
    
    public Actor __findTouchFirstActor () {

        double t= Double.MAX_VALUE;
        Actor actor= null;
        for( String pr : this.touches.keySet() ) {

            TouchInfoData touch= this.touches.get(pr);

            // TODO Check condition
            if ( touch.touch.time != 0 && touch.touch.time < t && touch.actor.isGestureEnabled() ) {
                actor= touch.actor;
                t= touch.touch.time;
            }
        }
        return actor;
    }

    public Actor __gesturedActor = null; 

    public void addHandlers(final CaatjaCanvas canvas) {
        
        Caatja.getCaatjaEventManager().addMouseHandlers(this, canvas);
        
        if (CAAT.TOUCH_BEHAVIOR == CAAT.TOUCH_AS_MOUSE) {
            Caatja.getCaatjaEventManager().addTouchHandlers(this, canvas);
            
        } else if (CAAT.TOUCH_BEHAVIOR == CAAT.TOUCH_AS_MULTITOUCH) {
            Caatja.getCaatjaEventManager().addMultiTouchHandlers(this, canvas);
        }
        
    }

    public void enableEvents(CaatjaCanvas onElement) {
        Caatja.registerDirector(this);
        this.in_ = false;
        this.createEventHandler(onElement);
    }

    public void createEventHandler(CaatjaCanvas onElement) {
//        Canvas canvas= this.canvas;
        this.in_ = false;
//        this.addHandlers(canvas);
        this.addHandlers(onElement);
    }
    

}
