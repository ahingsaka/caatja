package com.katspow.caatja.behavior;

import com.katspow.caatja.foundation.actor.Actor;
import com.katspow.caatja.foundation.actor.SpriteActor;
import com.katspow.caatja.foundation.image.SpriteImage;
import com.katspow.caatja.math.Pt;
import com.katspow.caatja.pathutil.Path;

public class PathBehavior extends BaseBehavior {

    /**
     * CAAT.PathBehavior modifies the position of a CAAT.Actor along the path represented by an
     * instance of <code>CAAT.Path</code>.
     *
     * @constructor
     * @extends CAAT.Behavior
     *
     */
    public PathBehavior() {
        super();
    }
    
    public enum AutoRotate {
        LEFT_TO_RIGHT(0),          // fix left_to_right direction
        RIGHT_TO_LEFT(1),          // fix right_to_left
        FREE(2); // do not apply correction
        
        private int value;
        
        private AutoRotate(int value) {
            this.value = value;
        }
        
        public int getValue() {
            return value;
        }
    }

    /**
     * A path to traverse.
     * @type {CAAT.PathUtil.Path}
     * @private
     */
    public Path path = null;
    
    /**
     * Whether to set rotation angle while traversing the path.
     * @private
     */
    public boolean autoRotate = false;
    private double prevX = -1; // private, do not use.
    private double prevY = -1; // private, do not use.
    
    /**
     * Autorotation hint.
     * @type {CAAT.Behavior.PathBehavior.autorotate}
     * @private
     */
    public AutoRotate autoRotateOp = AutoRotate.FREE;
    
    public boolean isOpenContour = false;
    
    private double relativeX = 0;
    private double relativeY = 0;

    public PathBehavior setOpenContour(boolean b) {
        this.isOpenContour= b;
        return this;
    }
    
    // TODO
    public void parse(Object obj ) {
        super.parse(obj);

//        if ( obj.SVG ) {
//            SVGPath parser= new SVGPath();
//            Path path=parser.parsePath( obj.SVG );
//            this.setValues(path);
//        }
//        
//        if ( obj.autoRotate ) {
//            this.autoRotate= obj.autoRotate;
//        }
    }
    
    @Override
    public String getPropertyName() {
        return "translate";
    }
    
    public PathBehavior setRelativeValues(double x, double y ) {
        this.relativeX= x;
        this.relativeY= y;
        this.isRelative= true;
        return this;
    }
    
    // Add by me
    private boolean right_to_left;
    
    /**
     * Sets an actor rotation to be heading from past to current path's point.
     * Take into account that this will be incompatible with rotation Behaviors
     * since they will set their own rotation configuration.
     * @param autorotate {boolean}
     * @param autorotateOp {CAAT.PathBehavior.autorotate} whether the sprite is drawn heading to the right.
     * @return this.
     */
    public PathBehavior setAutoRotate(boolean autoRotate, AutoRotate autorotateOp) {
        this.autoRotate = autoRotate;
        if (autorotateOp != null) {
            this.autoRotateOp= autorotateOp;
        }
        return this;
    }
    
    // Add by me
    public PathBehavior setAutoRotate(boolean autoRotate) {
        this.autoRotate = autoRotate;
        return this;
    }

    /**
     * Set the behavior path. The path can be any length, and will take
     * behaviorDuration time to be traversed.
     * 
     * @param {CAAT.Path}
     * 
     * @deprecated
     */
    @Deprecated
    public PathBehavior setPath(Path path) {
        this.path = path;
        return this;
    }
    
    /**
     * Set the behavior path.
     * The path can be any length, and will take behaviorDuration time to be traversed.
     * @param {CAAT.Path}
     * @return this
     */
    public PathBehavior setValues(Path path) {
        return this.setPath(path);
    }

    @Override
    public PathBehavior setFrameTime(double startTime, double duration) {
        super.setFrameTime(startTime, duration);
        this.prevX = -1;
        this.prevY = -1;
        return this;
    }
    
    /**
     * @see Acotr.setPositionAcchor
     * @deprecated
     * @param tx a float with xoffset.
     * @param ty a float with yoffset.
     */
    public PathBehavior setTranslation(double tx, double ty ) {
        return this;
    }
    
    @Override
    public String calculateKeyFrameData(double time ) {
        time= this.interpolator.getPosition(time).y;
        Pt point= this.path.getPosition(time);
        return "translateX("+point.x+"px) translateY("+point.y+"px)" ;
    }

    @Override
    public String calculateKeyFramesData(String prefix, String name, int keyframessize) {

//        if ( typeof keyframessize==='undefined' ) {
//            keyframessize= 100;
//        }
//        keyframessize>>=0;
//
//        var i;
//        var kfr;
//        var time;
//        var kfd= "@-"+prefix+"-keyframes "+name+" {";
//
//        for( i=0; i<=keyframessize; i++ )    {
//            kfr= "" +
//                (i/keyframessize*100) + "%" + // percentage
//                "{" +
//                    "-"+prefix+"-transform:" + this.calculateKeyFrameData(i/keyframessize) +
//                "}";
//
//            kfd+= kfr;
//        }
//
//        kfd+="}";
//
//        return kfd;
        
        return "";
    }
    
    // TODO Check return type
    public SetForTimeReturnValue getKeyFrameDataValues(double time) {
        time = this.interpolator.getPosition(time).y;
        Pt point = this.path.getPosition(time);
        
        SetForTimeReturnValue obj = new SetForTimeReturnValue();
        obj.x = point.x;
        obj.y = point.y;

        if ( this.autoRotate ) {

            Pt point2= time==0 ? point : this.path.getPosition(time -.001);
            double ax = point.x - point2.x;
            double ay = point.y - point2.y;
            double angle = Math.atan2(ay, ax);

            obj.angle= angle;
        }

        return obj;
    }

    /**
     * Translates the Actor to the corresponding time path position.
     * If autoRotate=true, the actor is rotated as well. The rotation anchor will (if set) always be ANCHOR_CENTER.
     * @param time an integer indicating the time the behavior is being applied at.
     * @param actor a CAAT.Actor instance to be translated.
     * @return {object} an object of the form <code>{ x: {float}, y: {float}�}</code>.
     */
    public SetForTimeReturnValue setForTime(double time, Actor actor) {

        if (this.path == null) {
            SetForTimeReturnValue returnValue = new SetForTimeReturnValue();
            returnValue.x = actor.x;
            returnValue.y = actor.y;
            return returnValue;
        }

        // TODO Check source ???
        Pt point = this.path.getPosition(time);
        
        if (this.isRelative ) {
            point.x+= this.relativeX;
            point.y+= this.relativeY;
        }
        
        if (this.autoRotate) {

            if (-1 == this.prevX && -1 == this.prevY) {
                this.prevX = point.x;
                this.prevY = point.y;
            }

            double ax = point.x - this.prevX;
            double ay = point.y - this.prevY;

            if (ax == 0 && ay == 0) {
                actor.setLocation(point.x, point.y);
                SetForTimeReturnValue returnValue = new SetForTimeReturnValue();
                returnValue.x = actor.x;
                returnValue.y = actor.y;
                return returnValue;
            }

            double angle = Math.atan2(ay, ax);

         // actor is heading left to right
            // TODO Check
            if ( this.autoRotateOp== AutoRotate.LEFT_TO_RIGHT ) {
                if (this.prevX <= point.x) {
                    // Add by me
                    if (actor instanceof SpriteActor)
                        ((SpriteActor) actor).setImageTransformation(SpriteImage.Tr.NONE);
                    
                } else if (this.autoRotateOp==AutoRotate.RIGHT_TO_LEFT) {
                    // Add by me
                    if (actor instanceof SpriteActor)
                        ((SpriteActor) actor).setImageTransformation(SpriteImage.Tr.FLIP_HORIZONTAL);
                     angle += Math.PI;
                }
                
            } else {
                
                if (this.prevX <= point.x) {
                    // Add by me
                    if (actor instanceof SpriteActor)
                        ((SpriteActor) actor).setImageTransformation(SpriteImage.Tr.FLIP_HORIZONTAL);
                } else {
                    // Add by me
                    if (actor instanceof SpriteActor)
                        ((SpriteActor) actor).setImageTransformation(SpriteImage.Tr.NONE);
                     angle += Math.PI;
                }
                
                
            }

            actor.setRotation(angle);

            this.prevX = point.x;
            this.prevY = point.y;

            double modulo = Math.sqrt(ax * ax + ay * ay);
            ax /= modulo;
            ay /= modulo;

        }
        
        SetForTimeReturnValue returnValue = new SetForTimeReturnValue();
        if ( this.doValueApplication ) {
            actor.setLocation(point.x, point.y);
            returnValue.x = actor.x;
            returnValue.y = actor.y;
            return returnValue;
             
        } else {
            returnValue.x = point.x;
            returnValue.y = point.y;
            return returnValue;
        }

    }

    /**
     * Get a point on the path.
     * If the time to get the point at is in behaviors frame time, a point on the path will be returned, otherwise
     * a default {x:-1, y:-1} point will be returned.
     *
     * @param time {number} the time at which the point will be taken from the path.
     * @return {object} an object of the form {x:float y:float}
     */
    public Pt positionOnTime(double time) {
        if (this.isBehaviorInTime(time, null)) {
            time = this.normalizeTime(time);
            return this.path.getPosition(time);
        }

        return new Pt(-1, -1);

    }

    // Add by me
    @Override
    public PathBehavior setCycle(boolean bool) {
        return (PathBehavior) super.setCycle(bool);
    }

    @Override
    public PathBehavior setInterpolator(Interpolator interpolator) {
        return (PathBehavior) super.setInterpolator(interpolator);
    }

    @Override
    public PathBehavior addListener(BehaviorListener behaviorListener) {
        return (PathBehavior) super.addListener(behaviorListener);
    }

    @Override
    public PathBehavior setId(String id) {
        return (PathBehavior) super.setId(id);
    }

    @Override
    public PathBehavior setValueApplication(boolean apply) {
        return (PathBehavior) super.setValueApplication(apply);
    }
    
}
