package com.katspow.caatja.behavior;

import com.katspow.caatja.foundation.actor.Actor;

public class ScaleBehavior extends BaseBehavior {

    /**
     * ScaleBehavior applies scale affine transforms in both axis.
     * StartScale and EndScale must be supplied for each axis. This method takes care of a FF bug in which if a Scale is
     * set to 0, the animation will fail playing.
     * 
     * This behavior specifies anchors in values ranges 0..1
     *
     * @constructor
     * @extends CAAT.Behavior
     *
     */
    public ScaleBehavior() {
        super();
    }

    /**
     * Start X scale value.
     * @private
     * @type {number}
     */
    public double startScaleX = 1;
    
    /**
     * End X scale value.
     * @private
     * @type {number}
     */
    public double endScaleX = 1;
    
    /**
     * Start Y scale value.
     * @private
     * @type {number}
     */
    public double startScaleY = 1;
    
    /**
     * End Y scale value.
     * @private
     * @type {number}
     */
    public double endScaleY = 1;
    
    /**
     * Scale X anchor value.
     * @private
     * @type {number}
     */
    public double anchorX =        .50;
    
    /**
     * Scale Y anchor value.
     * @private
     * @type {number}
     */
    public double anchorY =        .50;
    
    // TODO
    public void parse(Actor obj ) {
        super.parse(obj);
//        this.startScaleX= (obj.scaleX && obj.scaleX.start) || 0;
//        this.endScaleX= (obj.scaleX && obj.scaleX.end) || 0;
//        this.startScaleY= (obj.scaleY && obj.scaleY.start) || 0;
//        this.endScaleY= (obj.scaleY && obj.scaleY.end) || 0;
//        this.anchorX= (typeof obj.anchorX!=="undefined" ? parseInt(obj.anchorX) : 0.5);
//        this.anchorY= (typeof obj.anchorY!=="undefined" ? parseInt(obj.anchorY) : 0.5);
    }
    
    @Override
    public String getPropertyName() {
        return "scale";
    }

    public void apply(double time, Actor actor) {
        if (this.isBehaviorInTime(time, actor)) {
            time = this.normalizeTime(time);
            this.setForTime(time, actor);
        }
    }

    /**
     * Applies corresponding scale values for a given time.
     * 
     * @param time the time to apply the scale for.
     * @param actor the target actor to Scale.
     * @return {object} an object of the form <code>{ scaleX: {float}, scaleY: {float}}</code>
     */
    public SetForTimeReturnValue setForTime(double time, Actor actor) {

        double scaleX = this.startScaleX + time * (this.endScaleX - this.startScaleX);
        double scaleY = this.startScaleY + time * (this.endScaleY - this.startScaleY);
        
        // Firefox 3.x & 4, will crash animation if either scaleX or scaleY equals 0.
        if (0==scaleX ) {
            scaleX=.01;
        }
        if (0==scaleY ) {
            scaleY=.01;
        }

        actor.setScaleAnchored( scaleX, scaleY, this.anchorX, this.anchorY );
        
        SetForTimeReturnValue returnValue = new SetForTimeReturnValue();
        returnValue.scaleX = scaleX;
        returnValue.scaleY = scaleY;
        return returnValue;
//        return { scaleX: scaleX, scaleY: scaleY };
    }
    
    /**
     * Define this scale behaviors values.
     *
     * Be aware the anchor values are supplied in <b>RELATIVE PERCENT</b> to
     * actor's size.
     *
     * @param startX {number} initial X axis scale value.
     * @param endX {number} final X axis scale value.
     * @param startY {number} initial Y axis scale value.
     * @param endY {number} final Y axis scale value.
     * @param anchorx {float} the percent position for anchorX
     * @param anchory {float} the percent position for anchorY
     *
     * @return this.
     */
    public ScaleBehavior setValues(double startX, double endX, double startY, double endY, double anchorx, double anchory) {
		this.startScaleX = startX;
		this.endScaleX = endX;
		this.startScaleY = startY;
		this.endScaleY = endY;

		this.anchorX = anchorx;
		this.anchorY = anchory;

		return this;
    }
    
    // Add by me
    public ScaleBehavior setValues(double startX, double endX, double startY, double endY) {
    	this.startScaleX = startX;
        this.endScaleX = endX;
        this.startScaleY = startY;
        this.endScaleY = endY;
        return this;
    }
    
    /**
     * Set an exact position scale anchor. Use this method when it is hard to
     * set a thorough anchor position expressed in percentage.
     * @param actor
     * @param x
     * @param y
     */
    public ScaleBehavior setAnchor  (Actor actor, double x, double y) {
    	this.anchorX= x/actor.width;
        this.anchorY= y/actor.height;
        return this;
    }
    
    public String calculateKeyFrameData(double time ) {
        double scaleX;
        double scaleY;

        time= this.interpolator.getPosition(time).y;
        scaleX= this.startScaleX + time*(this.endScaleX-this.startScaleX);
        scaleY= this.startScaleY + time*(this.endScaleY-this.startScaleY);

        return "scale(" + scaleX +"," + scaleY + ")";
    };
    
    // TODO Check return type
    public SetForTimeReturnValue getKeyFrameDataValues(double time) {
        time = this.interpolator.getPosition(time).y;
        SetForTimeReturnValue value = new SetForTimeReturnValue();
        value.scaleX = this.startScaleX + time * (this.endScaleX - this.startScaleX);
        value.scaleY = this.startScaleY + time * (this.endScaleY - this.startScaleY);
        return value;
    }

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
    public ScaleBehavior setAnchor  (Actor actor) {
    	return setAnchor(actor, 0, 0);
    }

    @Override
    public ScaleBehavior setFrameTime(double startTime, double duration) {
        return (ScaleBehavior) super.setFrameTime(startTime, duration);
    }

    @Override
    public ScaleBehavior setPingPong() {
        return (ScaleBehavior) super.setPingPong();
    }

    @Override
    public ScaleBehavior setCycle(boolean bool) {
        return (ScaleBehavior) super.setCycle(bool);
    }

    @Override
    public ScaleBehavior setInterpolator(Interpolator interpolator) {
        return (ScaleBehavior) super.setInterpolator(interpolator);
    }

    @Override
    public ScaleBehavior setId(String id) {
        return (ScaleBehavior) super.setId(id);
    }

    @Override
    public ScaleBehavior setValueApplication(boolean apply) {
        return (ScaleBehavior) super.setValueApplication(apply);
    }
    
}
