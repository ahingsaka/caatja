package com.katspow.caatja.behavior;

import com.katspow.caatja.foundation.actor.Actor;

/**
 * 
 * Scale only X or Y axis, instead both at the same time as ScaleBehavior.
 * 
 * @constructor
 */
public class Scale1Behavior extends BaseBehavior {

    public Scale1Behavior() {
        super();
        // TODO Used ?
//        this.anchor = Actor.Anchor.CENTER;
    }
    
    // TODO Check with source
    public static final int AXIS_X= 0;
    public static final int AXIS_Y= 1;
    
    /**
     * Start scale value.
     * @private
     */
    public double startScale = 1;
    
    /**
     * End scale value.
     * @private
     */
    public double endScale = 1;
    
    /**
     * Scale X anchor.
     * @private
     */
    public double anchorX = .50;
    
    /**
     * Scale Y anchor.
     * @private
     */
    public double anchorY = .50;

    public int sx = 1;
    public int sy = 1;

    /**
     * Apply on Axis X or Y ?
     */
    public boolean applyOnX = true;
    
    // TODO
    public void parse(Object obj ) {
        super.parse(obj);
//        this.startScale= obj.start || 0;
//        this.endScale= obj.end || 0;
//        this.anchorX= (typeof obj.anchorX!=="undefined" ? parseInt(obj.anchorX) : 0.5);
//        this.anchorY= (typeof obj.anchorY!=="undefined" ? parseInt(obj.anchorY) : 0.5);
//        this.applyOnX= obj.axis ? obj.axis.toLowerCase()==="x" : true;
    }
    
    public void applyOnAxis(int axis ) {
        if ( axis == AXIS_Y ) {
            this.applyOnX= false;
        } else {
            this.applyOnX= true;
        }
    }
    
    public String getPropertyName () {
        return "scale";
    }

    /**
     * Applies corresponding scale values for a given time.
     *
     * @param time the time to apply the scale for.
     * @param actor the target actor to Scale.
     * @return {object} an object of the form <code>{ scaleX: {float} scaleY: {float}Ê}</code>
     */
    public SetForTimeReturnValue setForTime (double time, Actor actor) {

        double scale= this.startScale + time*(this.endScale-this.startScale);

        // Firefox 3.x & 4, will crash animation if either scaleX or scaleY equals 0.
        if (0==scale ) {
            scale=0.01;
        }

        if ( this.doValueApplication ) {
            if ( this.applyOnX ) {
                actor.setScaleAnchored( scale, actor.scaleY, this.anchorX, this.anchorY );
            } else {
                actor.setScaleAnchored( actor.scaleX, scale, this.anchorX, this.anchorY );
            }
        }

        SetForTimeReturnValue returnValue = new SetForTimeReturnValue();
        returnValue.scale = scale;
        return returnValue;
//        return scale;
    }
    /**
     * Define this scale behaviors values.
     *
     * Be aware the anchor values are supplied in <b>RELATIVE PERCENT</b> to
     * actor's size.
     *
     * @param start {number} initial X axis scale value.
     * @param end {number} final X axis scale value.
     * @param anchorx {float} the percent position for anchorX
     * @param anchory {float} the percent position for anchorY
     *
     * @return this.
     */
    public Scale1Behavior setValues (double start,double end, boolean applyOnX, double anchorx, double anchory ) {
		this.startScale = start;
		this.endScale = end;
		this.applyOnX = !!applyOnX;

		this.anchorX = anchorx;
		this.anchorY = anchory;

		return this;
    }
    
    // Add by me
	public Scale1Behavior setValues(double start, double end, boolean applyOnX) {
		this.startScale = start;
		this.endScale = end;
		this.applyOnX = !!applyOnX;
		return this;
	}
    
    
    /**
     * Set an exact position scale anchor. Use this method when it is hard to
     * set a thorough anchor position expressed in percentage.
     * @param actor
     * @param x
     * @param y
     */
    public Scale1Behavior setAnchor (Actor actor,double x,double y ) {
        this.anchorX= x/actor.width;
        this.anchorY= y/actor.height;

        return this;
    }
    
    // TODO Check return type
    public SetForTimeReturnValue getKeyFrameDataValues(double time) {
        time = this.interpolator.getPosition(time).y;
        SetForTimeReturnValue obj = new SetForTimeReturnValue();
        
        if (this.applyOnX) {
            obj.scaleX = this.startScale + time * (this.endScale - this.startScale);
        } else {
            obj.scaleY = this.startScale + time * (this.endScale - this.startScale);
        }
//        obj[ this.applyOnX ? "scaleX" : "scaleY" ]= this.startScale + time * (this.endScale - this.startScale);

        return obj;
    }

//    calculateKeyFrameData ( time ) {
//        var scale;
//
//        time= this.interpolator.getPosition(time).y;
//        scale= this.startScale + time*(this.endScale-this.startScale);
//
//        return this.applyOnX ? "scaleX("+scale+")" : "scaleY("+scale+")";
//    }
//
//    calculateKeyFramesData (prefix, name, keyframessize) {
//
//        if ( typeof keyframessize==='undefined' ) {
//            keyframessize= 100;
//        }
//        keyframessize>>=0;
//
//        var i;
//        var kfr;
//        var kfd= "@-"+prefix+"-keyframes "+name+" {";
//
//        for (i = 0; i <= keyframessize; i++) {
//    kfr = "" +
//            (i / keyframessize * 100) + "%" + // percentage
//            "{" +
//            "-" + prefix + "-transform:" + this.calculateKeyFrameData(i / keyframessize) +
//            "; -" + prefix + "-transform-origin:" + (this.anchorX*100) + "% " + (this.anchorY*100) + "% " +
//            "}\n";
//
//        kfd += kfr;
//    }
//
//    kfd += "}\n";
//
//        return kfd;
//    }
    
    // Add by me
    
    @Override
    public Scale1Behavior setId(String id) {
        return (Scale1Behavior) super.setId(id);
    }
    

}
