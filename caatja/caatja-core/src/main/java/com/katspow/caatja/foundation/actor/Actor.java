package com.katspow.caatja.foundation.actor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.List;

import com.katspow.caatja.behavior.BaseBehavior;
import com.katspow.caatja.behavior.BehaviorListener;
import com.katspow.caatja.behavior.Interpolator;
import com.katspow.caatja.behavior.PathBehavior;
import com.katspow.caatja.behavior.RotateBehavior;
import com.katspow.caatja.behavior.Scale1Behavior;
import com.katspow.caatja.behavior.ScaleBehavior;
import com.katspow.caatja.core.CAAT;
import com.katspow.caatja.core.Caatja;
import com.katspow.caatja.core.canvas.CaatjaCanvas;
import com.katspow.caatja.core.canvas.CaatjaColor;
import com.katspow.caatja.core.canvas.CaatjaContext2d;
import com.katspow.caatja.core.canvas.CaatjaImage;
import com.katspow.caatja.event.CAATMouseEvent;
import com.katspow.caatja.event.CAATTouchEvent;
import com.katspow.caatja.event.MouseListener;
import com.katspow.caatja.foundation.Director;
import com.katspow.caatja.foundation.image.SpriteImage;
import com.katspow.caatja.foundation.image.SpriteImage.Tr;
import com.katspow.caatja.foundation.image.SpriteImageAnimationCallback;
import com.katspow.caatja.math.AABB;
import com.katspow.caatja.math.Dimension;
import com.katspow.caatja.math.Pt;
import com.katspow.caatja.math.Rectangle;
import com.katspow.caatja.math.matrix.Matrix;
import com.katspow.caatja.modules.texturepacker.TexturePage;
import com.katspow.caatja.pathutil.LinearPath;
import com.katspow.caatja.pathutil.Path;

//import elemental.html.Float32Array;

/**
*
* CAAT.Foundation.Actor is the base animable element. It is the base object for Director, Scene and
* Container.
*    <p>CAAT.Actor is the simplest object instance CAAT manages. Every on-screen element is an Actor instance.
*        An Actor has entity, it has a size, position and can have input sent to it. Everything that has a
*        visual representation is an Actor, including Director and Scene objects.</p>
*    <p>This object has functionality for:</p>
*    <ol>
*        <li>Set location and size on screen. Actors are always rectangular shapes, but not needed to be AABB.</li>
*        <li>Set affine transforms (rotation, scale and translation).</li>
*        <li>Define life cycle.</li>
*        <li>Manage alpha transparency.</li>
*        <li>Manage and keep track of applied Behaviors. Behaviors apply transformations via key-framing.</li>
*        <li>Compose transformations. A container Actor will transform its children before they apply their own transformation.</li>
*        <li>Clipping capabilities. Either rectangular or arbitrary shapes.</li>
*        <li>The API is developed to allow method chaining when possible.</li>
*        <li>Handle input (either mouse events, touch, multitouch, keys and accelerometer).</li>
*        <li>Show an image.</li>
*        <li>Show some image animations.</li>
*        <li>etc.</li>
*    </ol>
*
* @name Actor
* @memberOf CAAT.Foundation
* @constructor
*
*/
public class Actor extends AABB {
    
    public static int __index = 0;
    
    // Add by me
    public String name;
    public ActorRender onRenderStart;
    public ActorRender onRenderEnd;
    
    public enum EventType {
        EXPIRED, DESTROYED
    }

//    private Float32Array __uv;
//    private Float32Array __vv;

    /**
     * This class is the base for all animable entities in CAAT.
     * It defines an entity able to:
     *
     * <ul>
     * <li>Position itself on screen.
     * <li>Able to modify its presentation aspect via affine transforms.
     * <li>Take control of parent/child relationship.
     * <li>Take track of behaviors (@see CAAT.Behavior).
     * <li>Define a region on screen.
     * <li>Define alpha composition scope.
     * <li>Expose lifecycle.
     * <li>Manage itself in/out scene time.
     * <li>etc.
     * </ul>
     *
     * @constructor
     */
    public Actor() {
        this.behaviorList = new ArrayList<BaseBehavior>();
        this.lifecycleListenerList = new ArrayList<ActorListener>();
        this.AABB = new Rectangle();
        this.viewVertices = new ArrayList<Pt>() {{
           add(new Pt(0, 0, 0));
           add(new Pt(0, 0, 0));
           add(new Pt(0, 0, 0));
           add(new Pt(0, 0, 0));
        }};

        this.scaleAnchor = Anchor.CENTER;
        
        this.modelViewMatrix = new Matrix();
        this.modelViewMatrixI = new Matrix();
        this.worldModelViewMatrix = new Matrix();
        this.worldModelViewMatrixI = new Matrix();
        
        this.resetTransform();
        this.setScale(1, 1);
        this.setRotation(0);
        
        this.id= String.valueOf(__index++);
        
        // ADD by me
        disableDrag();
    }
    
    /**
     * A collection of this Actors lifecycle observers.
     * @type { Array.<{actorLifeCycleEvent : function( CAAT.Foundation.Actor, string, number ) }> }
     */
    public List<ActorListener> lifecycleListenerList =  null;
    
    /**
     * A collection of behaviors to modify this actor�s properties.
     * @type { Array.<CAAT.Behavior.Behavior> }
     */
    public ArrayList<BaseBehavior> behaviorList;
    
    /**
     * This actor's parent container.
     * @type { CAAT.Foundation.ActorContainer }
     */
    public ActorContainer parent;
    
    /**
     * x position on parent. In parent's local coord. system.
     * @type {number}
     */
    public double x;
    
    /**
     * y position on parent. In parent's local coord. system.
     * @type {number}
     */
    public double y;
    
    /**
     * Actor's width. In parent's local coord. system.
     * @type {number}
     */
    public double width;
    
    /**
     * Actor's height. In parent's local coord. system.
     * @type {number}
     */
    public double height;
    
    /**
     * actor�s layout preferred size.
     * @type {CAAT.Math.Dimension}
     */
    public Dimension preferredSize =         null;
    
    /**
     * actor's layout minimum size.
     * @type {CAAT.Math.Dimension}
     */
    public Dimension minimumSize =           null;
    
    /**
     * Marks since when this actor, relative to scene time, is going to be animated/drawn.
     * @type {number}
     */
    public double start_time;
    
    /**
     * Marks from the time this actor is going to be animated, during how much time.
     * Forever by default.
     * @type {number}
     */
    private double duration = Double.MAX_VALUE;
    
    /**
     * Will this actor be clipped before being drawn on screen ?
     * @type {boolean}
     */
    public boolean clip = false;
    
    // TODO Check type
    /**
     * If this.clip and this.clipPath===null, a rectangle will be used as clip area. Otherwise,
     * clipPath contains a reference to a CAAT.PathUtil.Path object.
     * @type {CAAT.PathUtil.Path}
     */
    public Path clipPath;
    
    /**
     * Translation x anchor. 0..1
     * @type {number}
     */
    public double tAnchorX            =   0;
    
    /**
     * Translation y anchor. 0..1
     * @type {number}
     */
    public double tAnchorY            =   0;

    /**
     * ScaleX value.
     * @type {number}
     */
    public Double scaleX = 1d; // transformation. width scale parameter
    
    /**
     * ScaleY value.
     * @type {number}
     */
    public Double scaleY = 1d; // transformation. height scale parameter
    
    /**
     * Scale Anchor X. Value 0-1
     * @type {number}
     */
    public Double scaleTX = .50; // transformation. scale anchor x position
    
    /**
     * Scale Anchor Y. Value 0-1
     * @type {number}
     */
    public Double scaleTY = .50; // transformation. scale anchor y position
    
    /**
     * A value that corresponds to any CAAT.Foundation.Actor.ANCHOR_* value.
     * @type {CAAT.Foundation.Actor.ANCHOR_*}
     */
    private Anchor scaleAnchor = Anchor.CENTER; // transformation. scale anchor
    
    /**
     * This actor�s rotation angle in radians.
     * @type {number}
     */
    public Double rotationAngle = 0d; // transformation. rotation angle in radians
    
    /**
     * Rotation Anchor X. CAAT uses different Anchors for position, rotation and scale. Value 0-1.
     * @type {number}
     */
    public double rotationY = .50; // transformation. rotation center y
    
    /**
     * Rotation Anchor Y. CAAT uses different Anchors for position, rotation and scale. Value 0-1.
     * @type {number}
     */
    public double rotationX = .50; // transformation. rotation center x
    
    /**
     * Transparency value. 0 is totally transparent, 1 is totally opaque.
     * @type {number}
     */
    public double alpha = 1; // alpha transparency value
    
    /**
     * true to make all children transparent, false, only this actor/container will be transparent.
     * @type {boolean}
     */
    public boolean isGlobalAlpha = false; // is this a global alpha
    
    /**
     * @type {number}
     * @private
     */
    public double frameAlpha = 1; // hierarchically calculated alpha for this Actor.
    
    /**
     * Mark this actor as expired, or out of the scene time.
     * @type {boolean}
     */
    public boolean expired = false; // set when the actor has been expired
    
    /**
     * Mark this actor as discardable. If an actor is expired and mark as discardable, if will be
     * removed from its parent.
     * @type {boolean}
     */
    public boolean discardable = false; // set when you want this actor to be removed if expired
    
    /**
     * @type {boolean}
     */
    public boolean pointed = false; // is the mouse pointer inside this actor
    
    /**
     * Enable or disable input on this actor. By default, all actors receive input.
     * See also priority lists.
     * see demo4 for an example of input and priority lists.
     * @type {boolean}
     */
    public boolean mouseEnabled = true; // events enabled ?
    
    /**
     * Make this actor visible or not.
     * An invisible actor avoids making any calculation, applying any behavior on it.
     * @type {boolean}
     */
    public boolean visible = true;

    // constant values to determine different affine transform anchors
    public enum Anchor {
        CENTER(0), TOP(1), BOTTOM(2), LEFT(3), RIGHT(4), TOP_LEFT(5), TOP_RIGHT(6), BOTTOM_LEFT(7), BOTTOM_RIGHT(8), CUSTOM(9);

        private int value;

        private Anchor(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
    
    public enum Cache {
        NO(0), SIMPLE(1), DEEP(2);
        
        private int value;
        
        private Cache(int value) {
            this.value = value;
        }
        
        public int getValue() {
            return value;
        }
        
    }

    /**
     * any canvas rendering valid fill style.
     * @type {string}
     */
    public CaatjaColor fillStyle;
    
    /**
     * any canvas rendering valid stroke style.
     * @type {string}
     */
    public CaatjaColor strokeStyle;
    
    /**
     * This actor�s scene time.
     * @type {number}
     */
    public double time;
    
    // TODO Check
    /**
     * This rectangle keeps the axis aligned bounding box in screen coords of this actor.
     * In can be used, among other uses, to realize whether two given actors collide regardless
     * the affine transformation is being applied on them.
     * @type {CAAT.Math.Rectangle}
     */
//    public Rectangle AABB; // CAAT.Rectangle
    
    /**
     * These 4 CAAT.Math.Point objects are the vertices of this actor�s non axis aligned bounding
     * box. If the actor is not rotated, viewVertices and AABB define the same bounding box.
     * @type {Array.<CAAT.Math.Point>}
     */
    private List<Pt> viewVertices;
    
    /**
     * Is this actor processed in the last frame ?
     * @type {boolean}
     */
    public boolean inFrame = false;

    /**
     * Local matrix dirtyness flag.
     * @type {boolean}
     * @private
     */
    public boolean dirty = true;
    
    /**
     * Global matrix dirtyness flag.
     * @type {boolean}
     * @private
     */
    public boolean wdirty=                 true;   // world model view is dirty ?
    
    /**
     * @type {number}
     * @private
     */
    public double oldX=                   -1;
    
    /**
     * @type {number}
     * @private
     */
    public double oldY=                   -1;
    
    /**
     * This actor�s affine transformation matrix.
     * @type {CAAT.Math.Matrix}
     */
    public Matrix modelViewMatrix=        null;   // model view matrix.
    
    /**
     * This actor�s world affine transformation matrix.
     * @type {CAAT.Math.Matrix}
     */
    public Matrix worldModelViewMatrix=   null;   // world model view matrix.
    
    /**
     * @type {CAAT.Math.Matrix}
     */
    public Matrix modelViewMatrixI=       null;   // model view matrix.
    
    /**
     * @type {CAAT.Math.Matrix}
     */
    public Matrix worldModelViewMatrixI=  null;   // world model view matrix.
    
    /**
     * Is this actor enabled on WebGL ?
     * @type {boolean}
     */
    public boolean glEnabled = false;
    
    /**
     * Define this actor�s background image.
     * See SpriteImage object.
     * @type {CAAT.Foundation.SpriteImage}
     */
    public SpriteImage backgroundImage =        null;
    
    /**
     * Set this actor� id so that it can be later identified easily.
     * @type {object}
     */
    public String id = null;
    
    /**
     * debug info.
     * @type {number}
     */
    public int size_active =          1;      // number of animated children
    
    /**
     * debug info.
     * @type {number}
     */
    public int size_total =           1;
    
    private double __d_ax = -1; // for drag-enabled actors.
    private double __d_ay = -1;
    
    /**
     * Is gesture recognition enabled on this actor ??
     * @type {boolean}
     */
    public boolean gestureEnabled = false;
        
    /**
     * If dirty rects are enabled, this flag indicates the rendering engine to invalidate this
     * actor�s screen area.
     * @type {boolean}
     */
    public boolean invalid             = true;
    
    /**
     * Caching as bitmap strategy. Suitable to cache very complex actors.
     *
     * 0 : no cache.
     * CACHE_SIMPLE : if a container, only cache the container.
     * CACHE_DEEP : if a container, cache the container and recursively all of its children.
     *
     * @type {number}
     */
    public Cache cached              = Cache.NO;  // 0 no, CACHE_SIMPLE | CACHE_DEEP
    
    /**
     * Exclude this actor from automatic layout on its parent.
     * @type {boolean}
     */
    public boolean preventLayout = false;
    
    /**
     * is this actor/container Axis aligned ? if so, much faster inverse matrices can be calculated.
     * @type {boolean}
     * @private
     */
    public boolean isAA                =   true; 
    
    /**
     * if this actor is cached, when destroy is called, it does not call 'clean' method, which clears some
     * internal properties.
     */
    public boolean isCachedActor = false;
    
    public boolean collides            = false;
    public boolean collidesAsRect      = true;
    
    public Actor setCachedActor(boolean cached) {
        this.isCachedActor= cached;
        return this;
    }
    
    /**
    *
    * Make this actor not be laid out.
    *
    * @memberof CAAT.Foundation.Actor
    * @function
    */
    public Actor setPreventLayout(boolean b) {
        this.preventLayout= b;
        return this;
    }
    
    public Actor invalidateLayout () {
        if ( this.parent != null && !this.parent.layoutInvalidated ) {
            this.parent.invalidateLayout();
        }

        return this;
    }

    public void __validateLayout () {

    }

    /**
     * Set this actors preferred layout size.
     *
     * @param pw {number}
     * @param ph {number}
     * @return {*}
     */
    public Actor setPreferredSize (double pw, double ph ) {
        if ( this.preferredSize == null) {
            this.preferredSize= new Dimension();
        }
        this.preferredSize.width= pw;
        this.preferredSize.height= ph;
        return this;
    }

    public Dimension getPreferredSize () {
        return this.preferredSize != null ? this.preferredSize :
                    this.getMinimumSize();
    }

    /**
     * Set this actors minimum layout size.
     *
     * @param pw {number}
     * @param ph {number}
     * @return {*}
     */
    public Actor setMinimumSize (double pw,double ph ) {
        if ( this.minimumSize == null) {
            this.minimumSize= new Dimension();
        }

        this.minimumSize.width= pw;
        this.minimumSize.height= ph;
        return this;
    }

    public Dimension getMinimumSize () {
        return this.minimumSize != null ? this.minimumSize :
                    new Dimension(this.width, this.height);
    }
    
    /**
     * Move this actor to a position.
     * It creates and adds a new PathBehavior.
     * @param x {number} new x position
     * @param y {number} new y position
     * @param duration {number} time to take to get to new position
     * @param delay {=number} time to wait before start moving
     * @param interpolator {=CAAT.Interpolator} a CAAT.Interpolator instance
     */
    public Actor moveTo (double x, double y, double duration, Double delay, Interpolator interpolator, BehaviorListener callback ) {
        
        if ( x==this.x && y==this.y ) {
            // TODO Check return this
            return this;
        }
        
        String id= "__moveTo";
        BaseBehavior b= this.getBehavior( id );
        if ( b == null) {
            b= new PathBehavior().
                setId( id).
                setValues( new LinearPath() );
            this.addBehavior(b);
        }

        ((LinearPath) ((PathBehavior) b).path).setInitialPosition( this.x, this.y ).setFinalPosition( x, y);
        b.setDelayTime( delay != null ? delay : 0, duration);
        if ( interpolator != null) {
            b.setInterpolator( interpolator );
        }
        
        if ( callback != null) {
            b.lifecycleListenerList = new ArrayList<BehaviorListener>();
            b.addListener(callback);
        }

        return this;
    }
    
    // add by me
    public Actor moveTo (double x, double y, double duration, Double delay, Interpolator interpolator) {
        return moveTo(x, y, duration, delay, interpolator, null);
    }

    /**
    *
    * @param angle {number} new rotation angle
    * @param duration {number} time to rotate
    * @param delay {number=} millis to start rotation
    * @param anchorX {number=} rotation anchor x
    * @param anchorY {number=} rotation anchor y
    * @param interpolator {CAAT.Interpolator=}
    * @return {*}
    */
    public Actor rotateTo (double angle, double duration, Double delay, double anchorX, double anchorY, Interpolator interpolator ) {
        
        if ( angle==this.rotationAngle ) {
            return this;
        }
        
        String id= "__rotateTo";
        BaseBehavior b= this.getBehavior( id );
        if ( b == null) {
            b= new RotateBehavior().
                setId( id).
                setValues( 0, 0, .5,.5 );
            this.addBehavior(b);
        }

        ((RotateBehavior) b).setValues( this.rotationAngle, angle, anchorX, anchorY ).
            setDelayTime( delay != null ? delay : 0, duration);

        if ( interpolator != null) {
            b.setInterpolator( interpolator );
        }

        return this;
    }

    /**
     *
     * @param scaleX {number} new X scale
     * @param scaleY {number} new Y scale
     * @param duration {number} time to rotate
     * @param delay {=number} millis to start rotation
     * @param anchorX {=number} rotation anchor x
     * @param anchorY {=number} rotation anchor y
     * @param interpolator {=CAAT.Interpolator}
     * @return {*}
     */
    public Actor scaleTo (double scaleX, double scaleY, double duration, Double delay, double anchorX, double anchorY, Interpolator interpolator ) {
        
        if ( this.scaleX==scaleX && this.scaleY==scaleY ) {
            return this;
        }
        
        String id= "__scaleTo";
        BaseBehavior b= this.getBehavior( id );
        if ( b == null) {
            b= new ScaleBehavior().
                setId( id).
                setValues( 1,1,1,1, .5,.5 );
            this.addBehavior(b);
        }

        ((ScaleBehavior) b).setValues( this.scaleX, scaleX, this.scaleY, scaleY, anchorX, anchorY ).
            setDelayTime( delay != null ? delay : 0, duration);

        if ( interpolator != null) {
            b.setInterpolator( interpolator );
        }

        return this;
    }

    /**
     *
     * @param scaleX {number} new X scale
     * @param duration {number} time to rotate
     * @param delay {=number} millis to start rotation
     * @param anchorX {=number} rotation anchor x
     * @param anchorY {=number} rotation anchor y
     * @param interpolator {=CAAT.Interpolator}
     * @return {*}
     */
     public Actor  scaleXTo (double scaleX, double duration, double delay, int anchorX, int anchorY, Interpolator interpolator ) {
        return this.__scale1To(
            Scale1Behavior.AXIS_X,
            scaleX,
            duration,
            delay,
            anchorX,
            anchorY,
            interpolator
        );
    }

    /**
     *
     * @param scaleY {number} new Y scale
     * @param duration {number} time to rotate
     * @param delay {=number} millis to start rotation
     * @param anchorX {=number} rotation anchor x
     * @param anchorY {=number} rotation anchor y
     * @param interpolator {=CAAT.Interpolator}
     * @return {*}
     */
     public Actor scaleYTo (double scaleY, double duration, double delay, int anchorX, int anchorY, Interpolator interpolator ) {
        return this.__scale1To(
            Scale1Behavior.AXIS_Y,
            scaleY,
            duration,
            delay,
            anchorX,
            anchorY,
            interpolator
        );
    }

    /**
     * @param axis {CAAT.Scale1Behavior.AXIS_X|CAAT.Scale1Behavior.AXIS_Y} scale application axis
     * @param scale {number} new Y scale
     * @param duration {number} time to rotate
     * @param delay {=number} millis to start rotation
     * @param anchorX {=number} rotation anchor x
     * @param anchorY {=number} rotation anchor y
     * @param interpolator {=CAAT.Interpolator}
     * @return {*}
     */
     public Actor __scale1To (Integer axis, double scale, double duration, Double delay, double anchorX, double anchorY, Interpolator interpolator ) {
         
         if (( axis == Scale1Behavior.AXIS_X && scale==this.scaleX) ||
                 ( axis == Scale1Behavior.AXIS_Y && scale==this.scaleY)) {

                     return this;
        }
         
        String id= "__scaleXTo";
        BaseBehavior b= this.getBehavior( id );
        if ( b == null) {
            b= new Scale1Behavior().
                setId( id).
                setValues( 1,1, axis==Scale1Behavior.AXIS_X, .5,.5 );
            this.addBehavior(b);
        }

        ((RotateBehavior) b).setValues(
                axis != null ? this.scaleX : this.scaleY,
                scale,
                anchorX,
                anchorY ).
            setDelayTime( delay != null ? delay : 0, duration);

        if ( interpolator != null) {
            b.setInterpolator( interpolator );
        }

        return this;
    }
    
    /**
     * Touch Start only received when CAAT.TOUCH_BEHAVIOR= CAAT.TOUCH_AS_MULTITOUCH
     * @param e <CAAT.TouchEvent>
     */
    public void touchStart(CAATTouchEvent e) {
    }
    
    public void touchMove(CAATTouchEvent e) {
    }
    
    public void touchEnd(CAATTouchEvent e) {
    }
    
    public void gestureStart (double rotation, double scaleX, double scaleY) {
    }
    
    public Actor gestureChange (double rotation, double scaleX, double scaleY ) {
        if ( this.gestureEnabled ) {
            this.setRotation( rotation );
            this.setScale( scaleX, scaleY );
        }
        return this;
    }
    
    public void gestureEnd (double rotation, double scaleX, double scaleY ) {
    }
    
    public boolean isVisible() {
        return this.visible;
    }

    public Actor invalidate() {
        this.invalid= true;
        return this;
    }
    
    public Actor setGestureEnabled(boolean enable ) {
        this.gestureEnabled= !!enable;
        return this;
    }
    
    public boolean isGestureEnabled() {
        return this.gestureEnabled;
    }
    
    public String getId() {
        return id;
    }
    
    public Actor setId(String id) {
        this.id = id;
        return this;    
    }

    // Add by me
    private Anchor rotateAnchor;
    
    /**
     * Set this actor's parent.
     * @param parent {CAAT.ActorContainer}
     * @return this
     */
    public Actor setParent(ActorContainer parent) {
        this.parent = parent;
        return this;
    }
    
    /**
     * Set this actor's background image.
     * The need of a background image is to kept compatibility with the new CSSDirector class.
     * The image parameter can be either an Image/Canvas or a CAAT.SpriteImage instance. If an image
     * is supplied, it will be wrapped into a CAAT.SriteImage instance of 1 row by 1 column.
     * If the actor has set an image in the background, the paint method will draw the image, otherwise
     * and if set, will fill its background with a solid color.
     * If adjust_size_to_image is true, the host actor will be redimensioned to the size of one
     * single image from the SpriteImage (either supplied or generated because of passing an Image or
     * Canvas to the function). That means the size will be set to [width:SpriteImage.singleWidth,
     * height:singleHeight].
     *
     * WARN: if using a CSS renderer, the image supplied MUST be a HTMLImageElement instance.
     *
     * @see CAAT.SpriteImage
     *
     * @param image {Image|HTMLCanvasElement|CAAT.SpriteImage}
     * @param adjust_size_to_image {boolean} whether to set this actor's size based on image parameter.
     *
     * @return this
     * 
     *FIXME Canvas can be set instead of image
     */
    public Actor setBackgroundImage(SpriteImage image, Boolean adjust_size_to_image ) {
        if ( image != null) {
            // TODO
//            if (!(image instanceof CAAT.Foundation.SpriteImage)) {
//                if ( isString(image) ) {
//                    image = new CAAT.Foundation.SpriteImage().initialize(CAAT.currentDirector.getImage(image), 1, 1);
//                } else {
//                    image = new CAAT.Foundation.SpriteImage().initialize(image, 1, 1);
//                }
//            } else {
//                image= image.getRef();
//            }

            image.setOwner(this);
            this.backgroundImage= image;
            if (adjust_size_to_image == null || adjust_size_to_image ) {
                this.width= image.getWidth();
                this.height= image.getHeight();
            }

            this.glEnabled= true;
            
            this.invalidate();
            
        } else {
            this.backgroundImage = null;
        }
        
        return this;
    }
    
    // Add by me
    public Actor setBackgroundImage(CaatjaImage image, boolean adjust_size_to_image) {
    	if (image != null) {
    		SpriteImage spriteImage = new SpriteImage().initialize(image, 1, 1);
    		return setBackgroundImage(spriteImage, adjust_size_to_image);
    	} else {
    		return this;
    	}
    }
    
    /**
     * Set the actor's SpriteImage index from animation sheet.
     * @see CAAT.SpriteImage
     * @param index {number}
     *
     * @return this
     */
    public Actor setSpriteIndex(int index) {
        if ( this.backgroundImage != null ) {
            this.backgroundImage.setSpriteIndex(index);
            this.invalidate();
        }

        return this;

    }
    
    /**
     * Set this actor's background SpriteImage offset displacement.
     * The values can be either positive or negative meaning the texture space of this background
     * image does not start at (0,0) but at the desired position.
     * @see CAAT.SpriteImage
     * @param ox {number} horizontal offset
     * @param oy {number} vertical offset
     *
     * @return this
     */
    public Actor setBackgroundImageOffset (int ox, int oy ) {
        if ( this.backgroundImage != null) {
            this.backgroundImage.setOffset(ox,oy);
        }

        return this;
    }
    /**
     * Set this actor's background SpriteImage its animation sequence.
     * In its simplet's form a SpriteImage treats a given image as an array of rows by columns
     * subimages. If you define d Sprite Image of 2x2, you'll be able to draw any of the 4 subimages.
     * This method defines the animation sequence so that it could be set [0,2,1,3,2,1] as the
     * animation sequence
     * @param ii {Array<number>} an array of integers.
     */
    public Actor setAnimationImageIndex (int[] ii ) {
        if ( this.backgroundImage != null) {
            this.backgroundImage.resetAnimationTime();
            this.backgroundImage.setAnimationImageIndex(ii);
            this.invalidate();
        }
        return this;
    }
    
    public Actor addAnimation (String name, int[] array, int time, SpriteImageAnimationCallback callback ) {
        if (this.backgroundImage != null) {
            this.backgroundImage.addAnimation(name, array, time, callback);
        }
        return this;
    }

    public Actor playAnimation (String name) {
        if (this.backgroundImage != null) {
            this.backgroundImage.playAnimation(name);
        }
        return this;
    }

    public Actor setAnimationEndCallback (SpriteImageAnimationCallback f) {
        if (this.backgroundImage != null) {
            this.backgroundImage.setAnimationEndCallback(f);
        }
        
        return this;
    }
    
    public Actor resetAnimationTime() {
        if ( this.backgroundImage != null) {
            this.backgroundImage.resetAnimationTime();
            this.invalidate();
        }
        return this;
    }
    
    public Actor setChangeFPS(int time) {
        if ( this.backgroundImage != null) {
            this.backgroundImage.setChangeFPS(time);
        }
        return this;

    }
    
    /**
     * Set this background image transformation.
     * If GL is enabled, this parameter has no effect.
     * @param it any value from CAAT.SpriteImage.TR_*
     * @return this
     */
    public Actor setImageTransformation (Tr it ) {
        if ( this.backgroundImage != null) {
            this.backgroundImage.setSpriteTransformation(it);
        }
        return this;
    }
    /**
     * Center this actor at position (x,y).
     * @param x {number} x position
     * @param y {number} y position
     *
     * @return this
     * @deprecated
     */
    @Deprecated
    public Actor centerOn (double x, double y ) {
        this.setPosition( x-this.width/2, y-this.height/2 );
        return this;
    }
    
    /**
     * Center this actor at position (x,y).
     * @param x {number} x position
     * @param y {number} y position
     *
     * @return this
     */
    public Actor centerAt(double x, double y) {
        this.setPosition(
                x - this.width * (.5 - this.tAnchorX ),
                y - this.height * (.5 - this.tAnchorY ) );
        return this;
    }
    
    /*
     */
    /**
     * If GL is enables, get this background image's texture page, otherwise it will fail.
     * @return {CAAT.GLTexturePage}
     */
    public TexturePage getTextureGLPage () {
        return this.backgroundImage.image.__texturePage;            
    }
    
    /**
     * Set this actor invisible.
     * The actor is animated but not visible.
     * A container won't show any of its children if set visible to false.
     *
     * @param visible {boolean} set this actor visible or not.
     * @return this
     */
    public Actor setVisible(boolean visible) {
        this.invalidate();
     // si estoy visible y quiero hacerme no visible
        if ( CAAT.currentDirector != null && CAAT.currentDirector.dirtyRectsEnabled && !visible && this.visible ) {
            // if dirty rects, add this actor
            CAAT.currentDirector.scheduleDirtyRect( this.AABB );
        }
        
        if ( visible && !this.visible) {
            this.dirty= true;
        }
        
        this.visible= visible;
        return this;
    }
    
    /**
     * Puts an Actor out of time line, that is, won't be transformed nor rendered.
     * @return this
     */
    public Actor setOutOfFrameTime() {
        this.setFrameTime(-1,0);
        return this;
    }
    
    /**
     * Adds an Actor's life cycle listener.
     * The developer must ensure the actorListener is not already a listener, otherwise
     * it will notified more than once.
     * @param actorListener {object} an object with at least a method of the form:
     * <code>actorLyfeCycleEvent( actor, string_event_type, long_time )</code>
     */
    public Actor addListener (ActorListener actorListener ) {
        this.lifecycleListenerList.add(actorListener);
        return this;
    }
    
    /**
     * Removes an Actor's life cycle listener.
     * It will only remove the first occurrence of the given actorListener.
     * @param actorListener {object} an Actor's life cycle listener.
     */
    public void removeListener (ActorListener actorListener ) {
        this.lifecycleListenerList.remove(actorListener);
    }
    
    /**
     * Set alpha composition scope. global will mean this alpha value will be its children maximum.
     * If set to false, only this actor will have this alpha value.
     * @param global {boolean} whether the alpha value should be propagated to children.
     */
    public Actor setGlobalAlpha (boolean global ) {
        this.isGlobalAlpha= global;
        return this;
    }
    
    /**
     * Notifies the registeres Actor's life cycle listener about some event.
     * @param sEventType an string indicating the type of event being notified.
     * @param time an integer indicating the time related to Scene's timeline when the event
     * is being notified.
     * 
     * TODO String should be enum
     */
    public void fireEvent (EventType sEventType, double time)  {
        for (ActorListener actorListener : lifecycleListenerList) {
            actorListener.actorLifeCycleEvent(this, sEventType, time);
        }
    }
    
    /**
     * Sets this Actor as Expired.
     * If this is a Container, all the contained Actors won't be nor drawn nor will receive
     * any event. That is, expiring an Actor means totally taking it out the Scene's timeline.
     * @param time {number} an integer indicating the time the Actor was expired at.
     * @return this.
     */
    public Actor setExpired(double time) {
        this.expired= true;
        this.fireEvent(EventType.EXPIRED,time);
        return this;
    }
    
    // KEEP THIS ???
    public Actor setExpired(boolean expired) {
        if (expired) {
            this.expired = true;
            this.duration = 0;
            this.start_time = -1;
        }
        return this;
    }
    
    /**
     * Enable or disable the event bubbling for this Actor.
     * @param enable {boolean} a boolean indicating whether the event bubbling is enabled.
     * @return this
     */
    public Actor enableEvents(boolean enable) {
        this.mouseEnabled = enable;
        return this;
    };

    /**
     * Removes all behaviors from an Actor.
     * @return this
     */
    public Actor emptyBehaviorList() {
        this.behaviorList.clear();
        return this;
    }

    /**
     * Caches a fillStyle in the Actor.
     * @param style a valid Canvas rendering context fillStyle.
     * @return this
     */
    public Actor setFillStyle(String fillStyle) {
        this.fillStyle =  CaatjaColor.valueOf(fillStyle); //CssColor.make(fillStyle);
        this.invalidate();
        return this;
    }
    
    // Add by me
    public Actor setFillStrokeStyle(CaatjaColor style) {
        this.fillStyle= style;
        this.invalidate();
        return this;
    }
    
    /**
     * Caches a stroke style in the Actor.
     * @param style a valid canvas rendering context stroke style.
     * @return this
     */
    public Actor setStrokeStyle(CaatjaColor style) {
        this.strokeStyle = style;
        this.invalidate();
        return this;
    };
    
    // Add by me
    public Actor setStringStrokeStyle(String strokeStyle) {
        this.strokeStyle = CaatjaColor.valueOf(strokeStyle); // CssColor.make(strokeStyle);
        this.invalidate();
        return this;
    }
    
    /**
     * 
     * @param paint
     * @return
     */
    @Deprecated
    public Actor setPaint(CaatjaColor paint) {
        return this.setFillStrokeStyle(paint);
    }
    
    /**
     * Stablishes the Alpha transparency for the Actor.
     * If it globalAlpha enabled, this alpha will the maximum alpha for every contained actors.
     * The alpha must be between 0 and 1.
     * @param alpha a float indicating the alpha value.
     * @return this
     */
    public Actor setAlpha(double alpha) {
        this.alpha = alpha;
        this.invalidate();
        return this;
    }
    
    /**
     * Remove all transformation values for the Actor.
     * @return this
     */
    public Actor resetTransform() {
        this.rotationAngle = 0d;
        this.rotationX = .5;
        this.rotationY = .5;
        this.scaleX = 1d;
        this.scaleY = 1d;
        this.scaleTX = .5;
        this.scaleTY = .5;
        this.scaleAnchor = Anchor.CENTER;
        this.oldX=-1;
        this.oldY=-1;
        this.dirty = true;

        return this;
    }
    
    /**
     * Sets the time life cycle for an Actor.
     * These values are related to Scene time.
     * @param startTime an integer indicating the time until which the Actor won't be visible on the Scene.
     * @param duration an integer indicating how much the Actor will last once visible.
     * @return this
     */
    public Actor setFrameTime(double startTime, double duration) {
        this.start_time = startTime;
        this.duration = duration;
        this.expired = false;
        this.dirty = true;

        return this;
    }
    
    /**
     * This method should me overriden by every custom Actor.
     * It will be the drawing routine called by the Director to show every Actor.
     * @param director the CAAT.Director instance that contains the Scene the Actor is in.
     * @param time an integer indicating the Scene time in which the drawing is performed.
     */
    public void paint(Director director, double time) {
    	
    	if (this.backgroundImage != null) {
    		this.backgroundImage.paint(director,time,0,0);
    	} else if ( this.fillStyle != null) {
    	    CaatjaContext2d ctx= director.ctx;
            ctx.setFillStyle(this.fillStyle);
            ctx.fillRect(0,0,this.width,this.height );
        }

//        Context2d ctx = director.ctx;
//
//        if (null != this.fillStyle) {
//            ctx.setFillStyle(this.pointed ? CssColor.make("orange") : (this.fillStyle != null ? this.fillStyle
//                    : CssColor.make("white"))); // 'white';
//            ctx.fillRect(0, 0, this.width, this.height);
//        }

    }
    
    /**
     * A helper method to setScaleAnchored with an anchor of ANCHOR_CENTER
     *
     * @see setScaleAnchored
     *
     * @param sx a float indicating a width size multiplier.
     * @param sy a float indicating a height size multiplier.
     * @return this
     */
    public Actor setScale(double sx, double sy) {
        this.scaleX=sx;
        this.scaleY=sy;
        this.dirty = true;
        return this;
    }
    
    public Pt getAnchorPercent(int anchor ) {

        double[] anchors= new double[]{
                .50,.50,   .50,0,  .50,1.00,
                0,.50,   1.00,.50, 0,0,
                1.00,0,  0,1.00,  1.00,1.00
        };

        return new Pt(anchors[anchor*2],anchors[anchor*2+1]);
    }
    
    /**
     * Private.
     * Gets a given anchor position referred to the Actor.
     * @param anchor
     * @return an object of the form { x: float, y: float }
     */
    public Pt getAnchor(Anchor anchor) {
        double tx = 0, ty = 0;

        switch (anchor) {
        case CENTER:
            tx= .5;
            ty= .5;
            break;
        case TOP:
            tx= .5;
            ty = 0;
            break;
        case BOTTOM:
            tx= .5;
            ty= 1;
            break;
        case LEFT:
            tx= 0;
            ty= .5;
            break;
        case RIGHT:
            tx= 1;
            ty= .5;
            break;
        case TOP_RIGHT:
            tx= 1;
            ty= 0;
            break;
        case BOTTOM_LEFT:
            tx = 0;
            ty= 1;
            break;
        case BOTTOM_RIGHT:
            tx= 1;
            ty= 1;
            break;
        case TOP_LEFT:
            tx = 0;
            ty = 0;
            break;
        }

        Pt p = new Pt();
        p.set(tx, ty);
        return p;
    }
    
    public Actor setGlobalAnchor (double ax,double ay ) {
        this.tAnchorX=  ax;
        this.rotationX= ax;
        this.scaleTX=   ax;

        this.tAnchorY=  ay;
        this.rotationY= ay;
        this.scaleTY=   ay;

        this.dirty= true;
        return this;
    }

    public Actor setScaleAnchor (double sax,double say ) {
        this.scaleTX= sax;
        this.scaleTY= say;
        this.dirty= true;
        return this;
    }
    
    /**
     * Modify the dimensions on an Actor.
     * The dimension will not affect the local coordinates system in opposition
     * to setSize or setBounds.
     *
     * @param sx {number} width scale.
     * @param sy {number} height scale.
     * @param anchorx {number} x anchor to perform the Scale operation.
     * @param anchory {number} y anchor to perform the Scale operation.
     *
     * @return this;
     */
    public Actor setScaleAnchored(double sx, double sy, double anchorx, double anchory) {
        this.scaleTX = anchorx;
        this.scaleTY = anchory;

        this.scaleX = sx;
        this.scaleY = sy;
        
        this.dirty = true;

        return this;
    }
    
    public Actor setRotationAnchor(double rax, double ray ) {
        this.rotationX= ray;
        this.rotationY= rax;
        this.dirty= true;
        return this;
    }

    /**
     * A helper method for setRotationAnchored. This methods stablishes the center
     * of rotation to be the center of the Actor.
     *
     * @param angle a float indicating the angle in radians to rotate the Actor.
     * @return this
     */
    public Actor setRotation(double angle) {
        this.rotationAngle= angle;
        this.dirty= true;
        return this;
    }

    /**
     * This method sets Actor rotation around a given position.
     * @param angle {number} indicating the angle in radians to rotate the Actor.
     * @param rx {number} value in the range 0..1
     * @param ry {number} value in the range 0..1
     * @return this;
     */
    public Actor setRotationAnchored(double angle, Double rx, Double ry) {
        this.rotationAngle = angle;
        this.rotationX = rx != null ? rx : 0;
        this.rotationY = ry != null ? ry : 0;
        this.dirty = true;
        return this;
    }

    /**
     * Sets an Actor's dimension
     * @param w a float indicating Actor's width.
     * @param h a float indicating Actor's height.
     * @return this
     */
    public Actor setSize(double w, double h) {
        this.width = w;
        this.height = w;
        this.dirty= true;
        
        return this;
    }

    /**
     * Set location and dimension of an Actor at once.
     * TODO double or int ?
     *
     * @param x{number} a float indicating Actor's x position.
     * @param y{number} a float indicating Actor's y position
     * @param w{number} a float indicating Actor's width
     * @param h{number} a float indicating Actor's height
     * @return this
     */
    public Actor setBounds(double x, double y, double w, double h) {
        
        this.x= x;
        this.y= y;
        this.width= w;
        this.height= h;
        
        this.dirty= true;
        
        return this;
    }

    /**
     * This method sets the position of an Actor inside its parent.
     * 
     * TODO double or int ?
     *
     * @param x{number} a float indicating Actor's x position
     * @param y{number} a float indicating Actor's y position
     * @return this
     * 
     * @deprecated
     */
    @Deprecated
    public Actor setLocation(double x, double y) {
        this.x= x;
        this.y= y;
        this.oldX= x;
        this.oldY= y;

        this.dirty= true;
        
        return this;
    }
    
    public Actor setPosition (double x,double y ) {
        return this.setLocation( x,y );
    }

    public Actor setPositionAnchor (double pax,double pay ) {
        this.tAnchorX=  pax;
        this.tAnchorY=  pay;
        return this;
    }

    public Actor setPositionAnchored (double x,double y,double pax,double pay ) {
        this.setLocation( x,y );
        this.tAnchorX=  pax;
        this.tAnchorY=  pay;
        return this;
    }

    /**
     * This method is called by the Director to know whether the actor is on Scene time.
     * In case it was necessary, this method will notify any life cycle behaviors about
     * an Actor expiration.
     * @param time {number} time indicating the Scene time.
     *
     * @private
     *
     */
    public boolean isInAnimationFrame(double time) {
        
        if (this.expired) {
            return false;
        }

        if (this.duration == Double.MAX_VALUE) {
            return this.start_time <= time;
        }
        
        if (time >= this.start_time + this.duration) {
            if (!this.expired) {
                this.setExpired(time);
            }

            return false;
        }
        
        return this.start_time <= time && time < this.start_time + this.duration;
    }

    /**
     * Checks whether a coordinate is inside the Actor's bounding box.
     * @param x {number} a float
     * @param y {number} a float
     *
     * @return boolean indicating whether it is inside.
     */
    public boolean contains(double x, double y) {
        return x >= 0 && y >= 0 && x < this.width && y < this.height;
    }

    /**
     * Add a Behavior to the Actor.
     * An Actor accepts an undefined number of Behaviors.
     *
     * @param behavior {CAAT.Behavior} a CAAT.Behavior instance
     * @return this
     */
    public Actor addBehavior(BaseBehavior behaviour) {
        this.behaviorList.add(behaviour);
        return this;
    }
    
    /**
     * Remove a Behavior from the Actor.
     * If the Behavior is not present at the actor behavior collection nothing happends.
     *
     * @param behavior {CAAT.Behavior} a CAAT.Behavior instance.
     */
    public Actor removeBehaviour(BaseBehavior behavior ) {
        this.behaviorList.remove(behavior);
        return this;
    };
    
    /**
     * Remove a Behavior with id param as behavior identifier from this actor.
     * This function will remove ALL behavior instances with the given id.
     *
     * @param id {number} an integer.
     * return this;
     */
    public Actor removeBehaviorById(String id ) {
        ArrayList<BaseBehavior> c = this.behaviorList;
        for( int n=0; n<c.size(); n++ ) {
            if ( c.get(n).id.equals(id)) {
                c.remove(n);
            }
        }

        return this;

    }
    
    public BaseBehavior getBehavior(String id)  {
        for (BaseBehavior behavior : this.behaviorList) {
            if (behavior.id.equals(id)) {
                return behavior;
            }
        }

        return null;
    }
    
    /**
     * Set discardable property. If an actor is discardable, upon expiration will be removed from
     * scene graph and hence deleted.
     * @param discardable {boolean} a boolean indicating whether the Actor is discardable.
     * @return this
     */
    public Actor setDiscardable(boolean discardable) {
        this.discardable= discardable;
        return this;
    };

    /**
     * This method will be called internally by CAAT when an Actor is expired, and at the
     * same time, is flagged as discardable.
     * It notifies the Actor life cycle listeners about the destruction event.
     *
     * @param time an integer indicating the time at wich the Actor has been destroyed.
     *
     * @private
     *
     */
    public void destroy(double time) {
        if (this.parent != null) {
            this.parent.removeChild(this);
        }
        
        this.fireEvent(EventType.DESTROYED, time);
        if ( !this.isCachedActor ) {
            this.clean();
        }
        
    }
    
    private void clean() {
    	this.backgroundImage= null;
        this.emptyBehaviorList();
        this.lifecycleListenerList = new ArrayList<ActorListener>();
    }
    
    /**
     * Transform a point or array of points in model space to view space.
     *
     * @param point {CAAT.Point|Array} an object of the form {x : float, y: float}
     *
     * @return the source transformed elements.
     *
     * @private
     *
     */
    public List<Pt> modelToView(List<Pt> point) {
        
        double x, y;
        
        if ( this.dirty ) {
            this.setModelViewMatrix();
        }
        
        double[] tm = this.worldModelViewMatrix.matrix;
        
        for (Pt pt : point) {
//            this.worldModelViewMatrix.transformCoord(pt);
            x= pt.x;
            y= pt.y;
            pt.x= x*tm[0] + y*tm[1] + tm[2];
            pt.y= x*tm[3] + y*tm[4] + tm[5];
        }
        
        return point;
    }
    
    /**
     * Transform a local coordinate point on this Actor's coordinate system into
     * another point in otherActor's coordinate system.
     * @param point {CAAT.Point}
     * @param otherActor {CAAT.Actor}
     */
    public Pt modelToModel(Pt point, Actor otherActor )   {
        if ( this.dirty ) {
            this.setModelViewMatrix();
        }
        
        // TODO Check
        return otherActor.viewToModel( this.modelToView(Arrays.asList(point)).get(0));
    }
    
    /**
     * Transform a point from model to view space.
     * <p>
     * WARNING: every call to this method calculates
     * actor's world model view matrix.
     *
     * @param point {CAAT.Point} a point in screen space to be transformed to model space.
     *
     * @return the source point object
     *
     *
     */
    public Pt viewToModel(Pt point) {
        if ( this.dirty ) {
            this.setModelViewMatrix();
        }
        this.worldModelViewMatrix.getInverse(this.worldModelViewMatrixI);
        this.worldModelViewMatrixI.transformCoord(point);
        return point;
    }

    
    /**
     * Private
     * This method does the needed point transformations across an Actor hierarchy to devise
     * whether the parameter point coordinate lies inside the Actor.
     * @param point {CAAT.Point}
     *
     * @return null if the point is not inside the Actor. The Actor otherwise.
     */
    public Actor findActorAtPosition(Pt point) {
        
        if (this.scaleX==0 || this.scaleY==0) {
            return null;
        }
        
        if ( !this.visible || !this.mouseEnabled || !this.isInAnimationFrame(this.time) ) {
            return null;
        }

        this.modelViewMatrix.getInverse(this.modelViewMatrixI);
        this.modelViewMatrixI.transformCoord(point);
        return this.contains(point.x, point.y) ? this :null;
    }

    // Add by me
    private double ax; // Used ?
    private double ay; // Used ?
    private double __d_asx;
    private double __d_asy;
    private double __d_ara;
    private double __d_screenx;
    private double __d_screeny;
    
    public double mx;
    public double my;
    private double asx;
    private double asy;
    private double ara;
    private double screenx;
    private double screeny;

    public boolean enableDrag = false;

    /**
     * Enables a default dragging routine for the Actor.
     * This default dragging routine allows to:
     *  <li>scale the Actor by pressing shift+drag
     *  <li>rotate the Actor by pressing control+drag
     *  <li>scale non uniformly by pressing alt+shift+drag
     *
     * @return this
     */
    public void enableDrag() {

        this.ax = 0;
        this.ay = 0;
        this.asx = 1;
        this.asy = 1;
        this.ara = 0;
        this.screenx = 0;
        this.screeny = 0;

        this.enableDrag = true;
        
        if (mouseDragListener == null) {
            setMouseDragListener(new DefaultDragListener());
        }
    }
    
    public Actor disableDrag() {
        
        this.enableDrag = false;
        
        setMouseDragListener(null);

        // FIXME remove everything ???
//        this.mouseEnter= function(mouseEvent) {};
//        this.mouseExit = function(mouseEvent) {};
//        this.mouseMove = function(mouseEvent) {};
//        this.mouseUp = function(mouseEvent) {};
//        this.mouseDrag = function(mouseEvent) {};

        return this;
    }
    
    
    protected MouseListener mouseClickListener;
    protected MouseListener mouseDblClickListener;
    protected MouseListener mouseDownListener;
    protected MouseListener mouseOutListener;
    protected MouseListener mouseOverListener;
    
    protected MouseListener mouseEnterListener = new MouseListener() {
        public void call(CAATMouseEvent e) throws Exception {
            if (enableDrag) {
                // this.ax = -1;
                // this.ay = -1;
                Actor.this.__d_ax = -1;
                Actor.this.__d_ay = -1;
                Actor.this.pointed = true;
                Caatja.setCursor("move");
            } else {
                Actor.this.pointed = true;
            }
        }
    };
    
    protected MouseListener mouseExitListener = new MouseListener() {
        public void call(CAATMouseEvent e) throws Exception {
            if (enableDrag) {
//              this.ax = -1;
//              this.ay = -1;
              Actor.this.__d_ax= -1;
              Actor.this.__d_ay= -1;
              Actor.this.pointed = false;
              Caatja.setCursor("default");
          } else {
              Actor.this.pointed= false;
          }            
        }
    };
    
    // TODO What is the point for the method here ?
    protected MouseListener mouseMoveListener = new MouseListener() {
        public void call(CAATMouseEvent e) throws Exception {
            if (enableDrag) {
                // this.mx = mouseEvent.point.x;
                // this.my = mouseEvent.point.y;
            }
        }
    };
    
    protected MouseListener mouseUpListener = new MouseListener() {
        public void call(CAATMouseEvent e) throws Exception {
            if (enableDrag) {
                // this.ax = -1;
                // this.ay = -1;
                __d_ax = -1;
                __d_ay = -1;
            }
        }
    };
    
    private class DefaultDragListener implements MouseListener {
        @Override
        public void call(CAATMouseEvent e) throws Exception {
            List<Pt> pt = Arrays.asList(new Pt(e.x, e.y )) ;
            Pt point = pt.get(0);

            pt= modelToView(pt);
            parent.viewToModel( point );
            
            if (__d_ax == -1 || __d_ay == -1) {
                __d_ax = point.x;
                __d_ay = point.y;
                __d_asx = scaleX;
                __d_asy = scaleY;
                __d_ara = rotationAngle;
                __d_screenx = e.screenPoint.x;
                __d_screeny = e.screenPoint.y;
            }

//            if (ax == -1 || ay == -1) {
//                ax = e.point.x;
//                ay = e.point.y;
//                asx = scaleX;
//                asy = scaleY;
//                ara = rotationAngle;
//                screenx = e.screenPoint.x;
//                screeny = e.screenPoint.y;
//            }

            if (e.isShiftDown()) {
                double scx = (e.screenPoint.x - __d_screenx) / 100;
                double scy = (e.screenPoint.y - __d_screeny) / 100;
                if (!e.isAltDown()) {
                    double sc = Math.max(scx, scy);
                    scx = sc;
                    scy = sc;
                }
                setScale(scx + asx, scy + asy);

            } else if (e.isControlDown()) {
                double vx = e.screenPoint.x - __d_screenx;
                double vy = e.screenPoint.y - __d_screeny;
                setRotation(-Math.atan2(vx, vy) + ara);
            } else {
                x += (point.x - __d_ax);
                y += (point.y - __d_ay);
//                ax = e.point.x;
//                ay = e.point.y;
            }
            
            __d_ax= point.x;
            __d_ay= point.y;
            
        }
    }
    
    protected MouseListener mouseDragListener = null;
    
    /**
     * Default mouseClick handler.
     * Mouse click events are received after a call to mouseUp method if no dragging was in progress.
     *
     * @param mouseEvent {CAAT.MouseEvent}
     */
    public void mouseClick(CAATMouseEvent mouseEvent) throws Exception {
        if (mouseClickListener != null) {
            mouseClickListener.call(mouseEvent);
        }
    }
    
    // TODO Throw Exception if enable events to false ?
    public Actor setMouseClickListener(MouseListener mouseListener) {
        this.mouseClickListener = mouseListener;
        return this;
    }
    
    public Actor setMouseDblClickListener(MouseListener mouseListener) {
        this.mouseDblClickListener = mouseListener;
        return this;
    }
    
    public Actor setMouseEnterListener(MouseListener mouseEnterListener) {
        this.mouseEnterListener = mouseEnterListener;
        return this;
    }
    
    public void setMouseExitListener(MouseListener mouseExitListener) {
        this.mouseExitListener = mouseExitListener;
    }
    
    public void setMouseMoveListener(MouseListener mouseMoveListener) {
        this.mouseMoveListener = mouseMoveListener;
    }
    
    public void setMouseDownListener(MouseListener mouseDownListener) {
        this.mouseDownListener = mouseDownListener;
    }
    
    public void setMouseUpListener(MouseListener mouseUpListener) {
        this.mouseUpListener = mouseUpListener;
    }
    
    public void setMouseOutListener(MouseListener mouseOutListener) {
        this.mouseOutListener = mouseOutListener;
    }
    
    public void setMouseOverListener(MouseListener mouseOverListener) {
        this.mouseOverListener = mouseOverListener;
    }
    
    public void setMouseDragListener(MouseListener mouseDragListener) {
        this.mouseDragListener = mouseDragListener;
    }

    /**
     * Default double click handler
     *
     * @param mouseEvent {CAAT.MouseEvent}
     */
    public void mouseDblClick(CAATMouseEvent mouseEvent) throws Exception {
        if (mouseDblClickListener != null) {
            mouseDblClickListener.call(mouseEvent);
        }
    }
    
    /**
     * Default mouse enter on Actor handler.
     * @param mouseEvent a CAAT.MouseEvent object instance.
     * 
     *  @ignore
     */
    public void mouseEnter(CAATMouseEvent mouseEvent) throws Exception {
        if (mouseEnterListener != null) {
            mouseEnterListener.call(mouseEvent);
        }
    }

    /**
     * Default mouse exit on Actor handler.
     *
     * @param mouseEvent a CAAT.MouseEvent object instance.
     * @throws Exception 
     * 
     *  @ignore
     */
    public void mouseExit(CAATMouseEvent mouseEvent) throws Exception {
        if (mouseExitListener != null) {
            mouseExitListener.call(mouseEvent);
        }
    }

    /**
     * Default mouse move inside Actor handler.
     *
     * @param mouseEvent a CAAT.MouseEvent object instance.
     * @throws Exception 
     * 
     *  @ignore
     */
    public void mouseMove(CAATMouseEvent mouseEvent) throws Exception {
        if (mouseMoveListener != null) {
            mouseMoveListener.call(mouseEvent);
        }
    }

    /**
     * default mouse press in Actor handler.
     *
     * @param mouseEvent a CAAT.MouseEvent object instance.
     * @throws Exception 
     */
    public void mouseDown(CAATMouseEvent mouseEvent) throws Exception {
        if (mouseDownListener != null) {
            mouseDownListener.call(mouseEvent);
        }
    }

    /**
     * default mouse release in Actor handler.
     *
     * @param mouseEvent a CAAT.MouseEvent object instance.
     * @throws Exception 
     * 
     *  @ignore
     */
    public void mouseUp(CAATMouseEvent mouseEvent) throws Exception {
        if (mouseUpListener != null) {
            mouseUpListener.call(mouseEvent);
        }
    }
    
    public void mouseOut(CAATMouseEvent mouseEvent) throws Exception {
        if (mouseOutListener != null) {
            mouseOutListener.call(mouseEvent);
        }
    }
    
    public void mouseOver(CAATMouseEvent mouseEvent) throws Exception {
        if (mouseOverListener != null) {
            mouseOverListener.call(mouseEvent);
        }
    }

    /**
     * default Actor mouse drag handler.
     *
     * @param mouseEvent a CAAT.MouseEvent object instance.
     * @throws Exception 
     * 
     *  @ignore
     */
    public void mouseDrag(CAATMouseEvent mouseEvent) throws Exception {
        if (mouseDragListener != null) {
            mouseDragListener.call(mouseEvent);
        }
    }
    
    /**
     * Draw a bounding box with on-screen coordinates regardless of the transformations
     * applied to the Actor.
     *
     * @param director {CAAT.Director} object instance that contains the Scene the Actor is in.
     * @param time {number} integer indicating the Scene time when the bounding box is to be drawn.
     */
    public void drawScreenBoundingBox(Director director, double time ) {
        if (null!=this.AABB && this.inFrame) {
            Rectangle s= this.AABB;
            //director.ctx.strokeRect( (int) s.x|0, (int) s.y|0, (int) s.width|0, (int) s.height|0 );
            CaatjaContext2d ctx= director.ctx;
            ctx.setStrokeStyle(CAAT.DEBUGAABBCOLOR);
            ctx.strokeRect( .5+((int)s.x|0), .5+((int)s.y|0), (int)s.width|0, (int)s.height|0 );
            if ( CAAT.DEBUGBB ) {
                List<Pt> vv= this.viewVertices;
                ctx.beginPath(  );
                ctx.lineTo( vv.get(0).x, vv.get(0).y );
                ctx.lineTo( vv.get(1).x, vv.get(1).y );
                ctx.lineTo( vv.get(2).x, vv.get(2).y );
                ctx.lineTo( vv.get(3).x, vv.get(3).y );
                ctx.closePath();
                ctx.setStrokeStyle(CAAT.DEBUGBBCOLOR);
                ctx.stroke();
            }
        }
    }
    
    /**
     * Private
     * This method is called by the Director instance.
     * It applies the list of behaviors the Actor has registered.
     *
     * @param director the CAAT.Director object instance that contains the Scene the Actor is in.
     * @param time an integer indicating the Scene time when the bounding box is to be drawn.
     * @throws Exception 
     */
    public boolean animate(Director director, double time) throws Exception {
        
        if ( !this.visible ) {
            return false;
        }
        
		if (!this.isInAnimationFrame(time)) {
			this.inFrame = false;
			this.dirty = true;
			return false;
		}

		if (this.x != this.oldX || this.y != this.oldY) {
			this.dirty = true;
			this.oldX = this.x;
			this.oldY = this.y;
		}

		// TODO Handle concurrent modification exception
		try {
			for (BaseBehavior behaviour : behaviorList) {
				behaviour.apply(time, this);
			}
		} catch (ConcurrentModificationException e) {

		}
		
        if ( this.clipPath != null) {
            this.clipPath.applyBehaviors(time);
        }

        // transformation stuff.
		this.setModelViewMatrix();
		
		if ( this.dirty || this.wdirty || this.invalid ) {
            if ( director.dirtyRectsEnabled ) {
                director.addDirtyRect( this.AABB );
            }
            this.setScreenBounds();
            if ( director.dirtyRectsEnabled ) {
                director.addDirtyRect( this.AABB );
            }
        }
        this.dirty= false;
        this.invalid= false;
		
		this.inFrame = true;
		
		if ( this.backgroundImage != null) {
            this.backgroundImage.setSpriteIndexAtTime(time);
        }
		
		return this.AABB.intersects( director.AABB );

//		return true;
        
    }
    
    /**
     * Set this model view matrix if the actor is Dirty.
     * 
     *  mm[2]+= this.x;
             mm[5]+= this.y;
             if ( this.rotationAngle ) {
                 this.modelViewMatrix.multiply( m.setTranslate( this.rotationX, this.rotationY) );
                 this.modelViewMatrix.multiply( m.setRotation( this.rotationAngle ) );
                 this.modelViewMatrix.multiply( m.setTranslate( -this.rotationX, -this.rotationY) );                    c= Math.cos( this.rotationAngle );
             }
             if ( this.scaleX!=1 || this.scaleY!=1 && (this.scaleTX || this.scaleTY )) {
                 this.modelViewMatrix.multiply( m.setTranslate( this.scaleTX , this.scaleTY ) );
                 this.modelViewMatrix.multiply( m.setScale( this.scaleX, this.scaleY ) );
                 this.modelViewMatrix.multiply( m.setTranslate( -this.scaleTX , -this.scaleTY ) );
             }
     * @return this
     */
    public Actor setModelViewMatrix() {
        
        double c,s,_m00,_m01,_m10,_m11;
        double mm0, mm1, mm2, mm3, mm4, mm5;
        double[] mm;
        
        this.wdirty= false;
        mm = this.modelViewMatrix.matrix;
        
        if ( this.dirty ) {
            
            mm0= 1;
            mm1= 0;
//            mm2= mm[2];
            mm3= 0;
            mm4= 1;
//            mm5= mm[5];

            mm2= this.x - this.tAnchorX * this.width ;
            mm5= this.y - this.tAnchorY * this.height;

            if ( this.rotationAngle != null && this.rotationAngle != 0) {
                
                double rx= this.rotationX*this.width;
                double ry= this.rotationY*this.height;

                mm2+= mm0*rx + mm1*ry;
                mm5+= mm3*rx + mm4*ry;

                c= Math.cos( this.rotationAngle );
                s= Math.sin( this.rotationAngle );
                _m00= mm0;
                _m01= mm1;
                _m10= mm3;
                _m11= mm4;
                mm0=  _m00*c + _m01*s;
                mm1= -_m00*s + _m01*c;
                mm3=  _m10*c + _m11*s;
                mm4= -_m10*s + _m11*c;

                mm2+= -mm0*rx - mm1*ry;
                mm5+= -mm3*rx - mm4*ry;
            }
            if ( this.scaleX!=1 || this.scaleY!=1) {
                
                double sx= this.scaleTX*this.width;
                double sy= this.scaleTY*this.height;

                mm2+= mm0*sx + mm1*sy;
                mm5+= mm3*sx + mm4*sy;

                mm0= mm0*this.scaleX;
                mm1= mm1*this.scaleY;
                mm3= mm3*this.scaleX;
                mm4= mm4*this.scaleY;

                mm2+= -mm0*sx - mm1*sy;
                mm5+= -mm3*sx - mm4*sy;
            }

            mm[0]= mm0;
            mm[1]= mm1;
            mm[2]= mm2;
            mm[3]= mm3;
            mm[4]= mm4;
            mm[5]= mm5;

        }

        if ( this.parent != null) {
            
            this.isAA= this.rotationAngle==0 && this.scaleX==1 && this.scaleY==1 && this.parent.isAA;
            
            if ( this.dirty || this.parent.wdirty ) {
                this.worldModelViewMatrix.copy( this.parent.worldModelViewMatrix );
                if ( this.isAA ) {
                    double[] mmm= this.worldModelViewMatrix.matrix;
                    mmm[2]+= mm[2];
                    mmm[5]+= mm[5];
                } else {
                    this.worldModelViewMatrix.multiply( this.modelViewMatrix );
                }
                this.wdirty= true;
            }
        } else {
        	if ( this.dirty ) {
        	    this.wdirty= true;
            }
        	
        	this.worldModelViewMatrix.identity();
        	this.isAA= this.rotationAngle==0 && this.scaleX==1 && this.scaleY==1;
        }
        
//        if ( (CAAT.DEBUGAABB || glEnabled) && (this.dirty || this.wdirty) ) {
        // screen bounding boxes will always be calculated.
//        if ( this.dirty || this.wdirty || this.invalid ) {
//            if ( director.dirtyRectsEnabled ) {
//                director.addDirtyRect( this.AABB );
//            }
//            this.setScreenBounds();
//            if ( director.dirtyRectsEnabled ) {
//                director.addDirtyRect( this.AABB );
//            }
//        }
//            
//        this.dirty= false;
//        this.invalid = false;
        
        return this;
    }
    
    /**
     * Calculates the 2D bounding box in canvas coordinates of the Actor.
     * This bounding box takes into account the transformations applied hierarchically for
     * each Scene Actor.
     * 
     *  @private
     */
    public void setScreenBounds() {

        Rectangle AABB= this.AABB;
        List<Pt> vv= this.viewVertices;
        Pt vvv;
        
        if ( this.isAA ) {
            double[] m= this.worldModelViewMatrix.matrix;
            double x= m[2];
            double y= m[5];
            double w= this.width;
            double h= this.height;
            AABB.x= x;
            AABB.y= y;
            AABB.x1= x + w;
            AABB.y1= y + h;
            AABB.width= w;
            AABB.height= h;

            if ( CAAT.GLRENDER ) {
                vvv= vv.get(0);
                vvv.x=x;
                vvv.y=y;
                vvv= vv.get(1);
                vvv.x=x+w;
                vvv.y=y;
                vvv= vv.get(2);
                vvv.x=x+w;
                vvv.y=y+h;
                vvv= vv.get(3);
                vvv.x=x;
                vvv.y=y+h;
            }
            
            return;
        }
        
        vvv= vv.get(0);
        vvv.x=0;
        vvv.y=0;
        vvv= vv.get(1);
        vvv.x=this.width;
        vvv.y=0;
        vvv= vv.get(2);
        vvv.x=this.width;
        vvv.y=this.height;
        vvv= vv.get(3);
        vvv.x=0;
        vvv.y=this.height;

        this.modelToView( this.viewVertices );

        double xmin= Double.MAX_VALUE, xmax=-Double.MIN_VALUE;
        double ymin= Double.MAX_VALUE, ymax=-Double.MIN_VALUE;
        
        vvv= vv.get(0);
        if ( vvv.x < xmin ) {
            xmin=vvv.x;
        }
        if ( vvv.x > xmax ) {
            xmax=vvv.x;
        }
        if ( vvv.y < ymin ) {
            ymin=vvv.y;
        }
        if ( vvv.y > ymax ) {
            ymax=vvv.y;
        }
         vvv= vv.get(1);
        if ( vvv.x < xmin ) {
            xmin=vvv.x;
        }
        if ( vvv.x > xmax ) {
            xmax=vvv.x;
        }
        if ( vvv.y < ymin ) {
            ymin=vvv.y;
        }
        if ( vvv.y > ymax ) {
            ymax=vvv.y;
        }
         vvv= vv.get(2);
        if ( vvv.x < xmin ) {
            xmin=vvv.x;
        }
        if ( vvv.x > xmax ) {
            xmax=vvv.x;
        }
        if ( vvv.y < ymin ) {
            ymin=vvv.y;
        }
        if ( vvv.y > ymax ) {
            ymax=vvv.y;
        }
         vvv= vv.get(3);
        if ( vvv.x < xmin ) {
            xmin=vvv.x;
        }
        if ( vvv.x > xmax ) {
            xmax=vvv.x;
        }
        if ( vvv.y < ymin ) {
            ymin=vvv.y;
        }
        if ( vvv.y > ymax ) {
            ymax=vvv.y;
        }

        AABB.x = xmin;
        AABB.y = ymin;
        AABB.x1 = xmax;
        AABB.y1 = ymax;
        AABB.width = (xmax - xmin);
        AABB.height = (ymax - ymin);

    }
    
    /**
     * Private.
     * This method will be called by the Director to set the whole Actor pre-render process.
     *
     * @param director the CAAT.Director object instance that contains the Scene the Actor is in.
     * @param time an integer indicating the Scene time when the bounding box is to be drawn.
     *
     * @return boolean indicating whether the Actor isInFrameTime
     */
    public boolean paintActor(Director director, double time) {
        
        // TODO No way to do this in Java
        if (CAAT.NO_PERF) {
            return __paintActor(director, time);

        } else {

            if (!this.visible || !director.inDirtyRect(this) ) {
                return true;
            }

            CaatjaContext2d ctx = director.ctx;

            if (this.parent != null) {
                this.frameAlpha = this.parent.frameAlpha * this.alpha;
            } else {
                this.frameAlpha = 1;
            }

            ctx.setGlobalAlpha(this.frameAlpha);
            
            director.modelViewMatrix.transformRenderingContextSet( ctx );
            this.worldModelViewMatrix.transformRenderingContext(ctx);

            if (this.clip) {
                ctx.beginPath();
                if (this.clipPath == null) {
                    ctx.rect(0,0,this.width,this.height);
                } else {
                    this.clipPath.applyAsPath(director);
                }
                ctx.clip();
            }

            this.paint(director, time);
            
            return true;

        }
    }

    /**
     * for js2native
     * 
     * @param director
     * @param time
     * 
     * TODO Check
     */
    public boolean __paintActor(Director director, double time) {
        if (!this.visible) {
            return true;
        }
        CaatjaContext2d ctx = director.ctx;
        
        // global opt:
        // set alpha as owns alpha, not take globalAlpha procedure.
        this.frameAlpha= this.alpha;
        
        double[] m = this.worldModelViewMatrix.matrix;
        ctx.setTransform(m[0], m[3], m[1], m[4], m[2], m[5]);
        this.paint(director, time);
        return true;
    }

    /**
     * Set coordinates and uv values for this actor. This function uses
     * Director's coords and indexCoords values.
     * 
     * @param director
     * @param time
     */
    public boolean paintActorGL (Director director, double time) {

        this.frameAlpha= this.parent.frameAlpha*this.alpha;

        if ( !this.glEnabled || !this.visible) {
            
            // TODO Check return type
            return true;
        }

        if ( this.glNeedsFlush(director) ) {
            // FIXME TODO
//            director.glFlush();
            this.glSetShader(director);

            // FIXME TODO
//            if ( this.__uv == null) {
//                this.__uv= WebGLUtils.createArrayOfFloat32(8);
//            }
//            if ( this.__vv == null ) {
//                this.__vv= WebGLUtils.createArrayOfFloat32(12);
//            }
//
//            this.setGLCoords( this.__vv, 0 );
//            this.setUV( this.__uv, 0 );
//            director.glRender(this.__vv, 12d, this.__uv);

            // TODO Check return type
            return true;
        }

        // FIXME TODO
//        Float32Array glCoords=       director.coords;
        Double glCoordsIndex=  director.coordsIndex;

        ////////////////// XYZ
//        this.setGLCoords(glCoords, glCoordsIndex);
        director.coordsIndex= glCoordsIndex+12;

        ////////////////// UV
        // FIXME TODO
//        this.setUV( director.uv, director.uvIndex );
        director.uvIndex+= 8;
        
        // TODO Check return type
        return true;
    }
    
    // FIXME TODO
    /**
     * TODO: set GLcoords for different image transformations.
     * @param glCoords
     * @param glCoordsIndex
     */
//    public void setGLCoords (Float32Array glCoords, int glCoordsIndex) {

//        List<Pt> vv=             this.viewVertices;
        
//        glCoords.set(glCoordsIndex++, (float) vv.get(0).x);
//        glCoords.set(glCoordsIndex++, (float) vv.get(0).y);
//        glCoords.set(glCoordsIndex++, 0);
//
//        glCoords.set(glCoordsIndex++, (float) vv.get(1).x);
//        glCoords.set(glCoordsIndex++, (float) vv.get(1).y);
//        glCoords.set(glCoordsIndex++, 0);
//
//        glCoords.set(glCoordsIndex++, (float) vv.get(2).x);
//        glCoords.set(glCoordsIndex++, (float) vv.get(2).y);
//        glCoords.set(glCoordsIndex++, 0);
//
//        glCoords.set(glCoordsIndex++, (float) vv.get(3).x);
//        glCoords.set(glCoordsIndex++, (float) vv.get(3).y);
//        glCoords.set(glCoordsIndex, 0);

//    }
    /**
    *Set UV for this actor's quad.
    * @param uvBuffer {Float32Array}
    * @param uvIndex {number}
     * FIXME TODO
     */
//    public void setUV (Float32Array uvBuffer, int uvIndex ) {
//		this.backgroundImage.setUV(uvBuffer, uvIndex);
//    }
    
    /**
     * Test for compulsory gl flushing:
     *  1.- opacity has changed.
     *  2.- texture page has changed.
     *
     */
    public boolean glNeedsFlush (Director director) {
    	 if ( this.getTextureGLPage()!=director.currentTexturePage ) {
             return true;
         }
         if ( this.frameAlpha!=director.currentOpacity ) {
             return true;
         }
         return false;
    }
    
    /**
     * Change texture shader program parameters.
     * @param director
     */
    public void glSetShader (Director director) {
        
        TexturePage tp = this.getTextureGLPage();
        if ( tp!=director.currentTexturePage ) {
            director.setGLTexturePage(tp);
        }
        
        if ( this.frameAlpha!=director.currentOpacity ) {
            director.setGLCurrentOpacity(this.frameAlpha);
        }
    }
    
    /**
     * @private.
     * This method is called after the Director has transformed and drawn a whole frame.
     *
     * @param director the CAAT.Director object instance that contains the Scene the Actor is in.
     * @param time an integer indicating the Scene time when the bounding box is to be drawn.
     * @return this
     * 
     * @deprecated
     */
    @Deprecated
    protected void endAnimate(Director director, double time) {
    }
    
    public void initialize() {
    }
    
    /**
     * Enable or disable the clipping process for this Actor.
     *
     * @param clip a boolean indicating whether clip is enabled.
     * @param clipPath {CAAT.Path=} An optional path to apply clip with. If enabled and clipPath is not set,
     *  a rectangle will be used.
     * @return this
     */
    public Actor setClip(boolean clip, Path clipPath) {
        this.clip = clip;
        this.clipPath = clipPath;
        return this;
    }
    
    public void stopCacheAsBitmap() {
        if ( this.cached.getValue() > 0 ) {
            this.backgroundImage= null;
            this.cached= Cache.NO;
        }
    }
    
    /**
    *
    * @param time {Number=}
    *  @param stragegy {CAAT.Actor.CACHE_SIMPLE | CAAT.Actor.CACHE_DEEP}
    *  
    * @return this
    */
   public CaatjaCanvas cacheAsBitmap(Double time, Cache strategy) {
       
       // FIXME cannot return this
       if (this.width<=0 || this.height<=0 ) {
           return null;
       }
       
       if (time == null || time == 0d) {
           time = 0d;
       }
       
       CaatjaCanvas canvas = Caatja.createCanvas();;
       canvas.setCoordinateSpaceWidth((int)this.width);
       canvas.setCoordinateSpaceHeight((int)this.height);
       
       CaatjaContext2d ctx= canvas.getContext2d();
       
       Director director = new Director(false);
       director.ctx = ctx;
       
       director.modelViewMatrix = new Matrix();
       director.worldModelViewMatrix = new Matrix();
       director.dirtyRectsEnabled = false;
       // TODO No need to do this ?
//       inDirtyRect : function() { return true; }
       
       Matrix pmv = this.modelViewMatrix;
       Matrix pwmv = this.worldModelViewMatrix;

       this.modelViewMatrix =  new Matrix();
       this.worldModelViewMatrix =  new Matrix();
       
       this.cached= Cache.NO;
       this.paintActor(director,time);
       // FIXME TODO Cannot set canvas as background	 !
//       this.setBackgroundImage(canvas);
       
       this.cached= strategy != null ? strategy : Actor.Cache.SIMPLE;;
       
       this.modelViewMatrix =  pmv;
       this.worldModelViewMatrix =  pwmv;
       
       // FIXME cannot return this
       return canvas;
   }
   
   public Actor resetAsButton() {
       // TODO
//       this.actionPerformed= null;
//       this.mouseEnter=    function() {};
//       this.mouseExit=     function() {};
//       this.mouseDown=     function() {};
//       this.mouseUp=       function() {};
//       this.mouseClick=    function() {};
//       this.mouseDrag=     function() {};
       return this;
   }
   
   /**
    * Set this actor behavior as if it were a Button. The actor size will be set as SpriteImage's
    * single size.
    * 
    * @param buttonImage {CAAT.SpriteImage} sprite image with button's state images.
    * @param iNormal {number} button's normal state image index
    * @param iOver {number} button's mouse over state image index
    * @param iPress {number} button's pressed state image index
    * @param iDisabled {number} button's disabled state image index
    * @param fn {function(button{CAAT.Actor})} callback function
    */
   public Actor setAsButton(CaatjaImage buttonImage, int iNormal, int iOver, int iPress, int iDisabled, ActorCallback fn ) {

	   // FIXME TODO
       this.setBackgroundImage(buttonImage, true);

//       this.iNormal=       iNormal || 0;
//       this.iOver=         iOver || this.iNormal;
//       this.iPress=        iPress || this.iNormal;
//       this.iDisabled=     iDisabled || this.iNormal;
//       this.fnOnClick=     fn;
//       this.enabled=       true;
//
//       this.setSpriteIndex( iNormal );
//
//       /**
//        * Enable or disable the button.
//        * @param enabled {boolean}
//        * @ignore
//        */
//       this.setEnabled= function( enabled ) {
//           this.enabled= enabled;
//       this.setSpriteIndex( this.enabled ? this.iNormal : this.iDisabled );
//       return this;
//       };
//
//       /**
//        * This method will be called by CAAT *before* the mouseUp event is fired.
//        * @param event {CAAT.MouseEvent}
//        * @ignore
//        */
//       this.actionPerformed= function(event) {
//           if (this.enabled && this.fnOnClick) {
//               this.fnOnClick(this);
//           }
//       };
//
//       /**
//        * Button's mouse enter handler. It makes the button provide visual feedback
//        * @param mouseEvent {CAAT.MouseEvent}
//        * @ignore
//        */
//       this.mouseEnter= function(mouseEvent) {
       
//       if ( !this.enabled ) {
//           return;
//       }
       
//           if ( this.dragging ) {
//               this.setSpriteIndex( this.iPress );
//           } else {
//               this.setSpriteIndex( this.iOver );
//           }
//           CAAT.setCursor('pointer');
//       };
//
//       /**
//        * Button's mouse exit handler. Release visual apperance.
//        * @param mouseEvent {CAAT.MouseEvent}
//        * @ignore
//        */
//       this.mouseExit= function(mouseEvent) {
       
//       if ( !this.enabled ) {
//           return;
//       }
       
//           this.setSpriteIndex( this.iNormal );
//           CAAT.setCursor('default');
//       };
//
//       /**
//        * Button's mouse down handler.
//        * @param mouseEvent {CAAT.MouseEvent}
//        * @ignore
//        */
//       this.mouseDown= function(mouseEvent) {
       
//       if ( !this.enabled ) {
//           return;
//       }
       
//           this.setSpriteIndex( this.iPress );
//       };
//
//       /**
//        * Button's mouse up handler.
//        * @param mouseEvent {CAAT.MouseEvent}
//        * @ignore
//        */
//       this.mouseUp= function(mouseEvent) {

//       if ( !this.enabled ) {
//           return;
//       }
//           this.setSpriteIndex( this.iNormal );
//           this.dragging= false;
//       };
//
//       /**
//        * Button's mouse click handler. Do nothing by default. This event handler will be
//        * called ONLY if it has not been drag on the button.
//        * @param mouseEvent {CAAT.MouseEvent}
//        * @ignore
//        */
//       this.mouseClick= function(mouseEvent) {
//       };
//
//       /**
//        * Button's mouse drag handler.
//        * @param mouseEvent {CAAT.MouseEvent}
//        * @ignore
//        */
//       this.mouseDrag= function(mouseEvent)  {
       
//       if ( !this.enabled ) {
//           return;
//       }
       
//           this.dragging= true;
//       };
//       
//       this.setButtonImageIndex= function(_normal, _over, _press, _disabled ) {
//       this.iNormal=    _normal || 0;
//       this.iOver=      _over || this.iNormal;
//       this.iPress=     _press || this.iNormal;
//       this.iDisabled=  _disabled || this.iNormal;
//           this.setSpriteIndex(this.iNormal );
//           return this;
//       };

       return this;
   }
   
   public Actor findActorById(String id) {
       return this.id.equals(id) ? this : null;
   }

    // Add by me
    public CaatjaCanvas cacheAsBitmap() {
        return cacheAsBitmap(null, null);
    }
    
    // TODO Check this
    /*
    if ( CAAT.NO_PERF ) {
        CAAT.Actor.prototype.paintActor= CAAT.Actor.prototype.__paintActor;
    }
*/

}
