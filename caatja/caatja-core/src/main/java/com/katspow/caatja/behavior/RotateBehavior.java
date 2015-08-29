package com.katspow.caatja.behavior;

import com.katspow.caatja.foundation.actor.Actor;

public class RotateBehavior extends BaseBehavior {

    /**
     * This class applies a rotation to a CAAt.Actor instance.
     * StartAngle, EndAngle must be supplied. Angles are in radians.
     * The RotationAnchor, if not supplied, will be ANCHOR_CENTER.
     *
     * An example os use will be
     *
     * var rb= new CAAT.RotateBehavior().
     *      setValues(0,2*Math.PI).
     *      setFrameTime(0,2500);
     *
     * @see CAAT.Actor.
     *
     * @constructor
     * @extends CAAT.Behavior
     *
     */
    public RotateBehavior() {
        super();
    }

    /**
     * Start rotation angle.
     * @type {number}
     * @private
     */
    public double startAngle = 0;
    
    /**
     * End rotation angle.
     * @type {number}
     * @private
     */
    public double endAngle = 0;
    
    /**
     * Rotation X anchor.
     * @type {number}
     * @private
     */
    public double anchorX =         0.50;
    
    /**
     * Rotation Y anchor.
     * @type {number}
     * @private
     */
    public double anchorY =         0.50;
    
    public double rotationRelative = 0;

    public RotateBehavior setRelativeValues(double r) {
        this.rotationRelative= r;
        this.isRelative= true;
        return this;
    }
    
    // TODO
    public void parse(BaseBehavior obj ) {
        super.parse(obj);
//        this.startAngle= obj.start || 0;
//        this.endAngle= obj.end || 0;
//        this.anchorX= (typeof obj.anchorX!=="undefined" ? parseInt(obj.anchorX) : 0.5);
//        this.anchorY= (typeof obj.anchorY!=="undefined" ? parseInt(obj.anchorY) : 0.5);
    }

    @Override
    public String getPropertyName() {
        return "rotate";
    }

    /**
     * Behavior application function.
     * Do not call directly.
     * @param time an integer indicating the application time.
     * @param actor a CAAT.Actor the behavior will be applied to.
     * @return the set angle.
     */
    public SetForTimeReturnValue setForTime(double time, Actor actor) {
        double angle = this.startAngle + time * (this.endAngle - this.startAngle);
        
        if ( this.isRelative ) {
            angle+= this.rotationRelative;
            if (angle>=Math.PI) {
                angle= (angle-2*Math.PI);
            }
            if ( angle<-2*Math.PI) {
                angle= (angle+2*Math.PI);
            }
        }
        
        if (this.doValueApplication) {
            actor.setRotationAnchored(angle, this.anchorX, this.anchorY);
        }

        SetForTimeReturnValue returnValue = new SetForTimeReturnValue();
        returnValue.angle = angle;
        return returnValue;
//        return angle;
    }
    
    /**
     * Set behavior bound values.
     * if no anchorx,anchory values are supplied, the behavior will assume
     * 50% for both values, that is, the actor's center.
     *
     * Be aware the anchor values are supplied in <b>RELATIVE PERCENT</b> to
     * actor's size.
     *
     * @param startAngle {float} indicating the starting angle.
     * @param endAngle {float} indicating the ending angle.
     * @param anchorx {float} the percent position for anchorX
     * @param anchory {float} the percent position for anchorY
     */
	public RotateBehavior setValues(double startAngle, double endAngle,
			double anchorx, double anchory) {
		this.startAngle = startAngle;
		this.endAngle = endAngle;
		this.anchorX = anchorx;
		this.anchorY = anchory;
		return this;
	}
    
    // Add by me
    public RotateBehavior setValues(double startAngle, double endAngle) {
		this.startAngle = startAngle;
		this.endAngle = endAngle;
		return this;
    }
    
    
    /**
     * @deprecated
     * Use setValues instead
     * @param start
     * @param end
     */
    @Deprecated
    public RotateBehavior setAngles(double start, double end) {
        return this.setValues(start,end);
    }

    /**
     * Set the behavior rotation anchor. Use this method when setting an exact percent
     * by calling setValues is complicated.
     * @see CAAT.Actor
     *
     * These parameters are to set a custom rotation anchor point. if <code>anchor==CAAT.Actor.ANCHOR_CUSTOM
     * </code> the custom rotation point is set.
     * @param actor
     * @param rx
     * @param ry
     *
     */
    public RotateBehavior setAnchor(Actor actor, double rx, double ry) {
    	this.anchorX= rx/actor.width;
        this.anchorY= ry/actor.height;
        return this;
    }
    
    public String calculateKeyFrameData(double time ) {
        time= this.interpolator.getPosition(time).y;
        return "rotate(" + (this.startAngle + time*(this.endAngle-this.startAngle)) +"rad)";
    }
    
    // TODO Check return type
    public SetForTimeReturnValue getKeyFrameDataValues(double time) {
        time = this.interpolator.getPosition(time).y;
        SetForTimeReturnValue obj = new SetForTimeReturnValue();
        obj.angle = this.startAngle + time * (this.endAngle - this.startAngle);
        return obj;
    }

    /**
     * @param prefix {string} browser vendor prefix
     * @param name {string} keyframes animation name
     * @param keyframessize {integer} number of keyframes to generate
     * @override
     */
    public String calculateKeyFramesData(String prefix, String name, int keyframessize) {

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
//            kfr = "" +
//                (i / keyframessize * 100) + "%" + // percentage
//                "{" +
//                "-" + prefix + "-transform:" + this.calculateKeyFrameData(i / keyframessize) +
//                "; -" + prefix + "-transform-origin:" + (this.anchorX*100) + "% " + (this.anchorY*100) + "% " +
//                "}\n";
//
//            kfd += kfr;
//        }
//
//        kfd += "}\n";
//
//        return kfd;
        
        return "";
    }
    
    // Add by me
    public RotateBehavior setAnchor(Actor actor) {
        return setAnchor(actor, 0, 0);
    }
    
    @Override
    public RotateBehavior setFrameTime(double startTime, double duration) {
        return (RotateBehavior) super.setFrameTime(startTime, duration);
    }

    @Override
    public RotateBehavior setCycle(boolean bool) {
        return (RotateBehavior) super.setCycle(bool);
    }

    @Override
    public RotateBehavior setInterpolator(Interpolator interpolator) {
        return (RotateBehavior) super.setInterpolator(interpolator);
    }

    @Override
    public RotateBehavior setId(String id) {
        return (RotateBehavior) super.setId(id);
    }

    @Override
    public RotateBehavior setValueApplication(boolean apply) {
        return (RotateBehavior) super.setValueApplication(apply);
    }
    
    
    
}
