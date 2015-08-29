package com.katspow.caatja.behavior;

import com.katspow.caatja.foundation.actor.Actor;

public class AlphaBehavior extends BaseBehavior {

    /**
     * AlphaBehavior modifies alpha composition property for an actor.
     *
     * @constructor
     * @extends CAAT.Behavior
     */
    public AlphaBehavior() {
        super();
    }

    /**
     * Starting alpha transparency value. Between 0 and 1.
     * @type {number}
     * @private
     */
    public double startAlpha = 0;
    
    /**
     * Ending alpha transparency value. Between 0 and 1.
     * @type {number}
     * @private
     */
    public double endAlpha = 0;
    
    // TODO
    public void parse(Object obj ) {
//        super.parse(obj);
//        this.startAlpha= obj.start || 0;
//        this.endAlpha= obj.end || 0;
    }
    
    @Override
    public String getPropertyName() {
        return "opacity";
    }

    public void apply(double time, Actor actor) {
        if (this.isBehaviorInTime(time, actor)) {
            time = this.normalizeTime(time);
            this.setForTime(time, actor);
        }
    }

    /**
     * Applies corresponding alpha transparency value for a given time.
     *
     * @param time the time to apply the scale for.
     * @param actor the target actor to set transparency for.
     * @return {number} the alpha value set. Normalized from 0 (total transparency) to 1 (total opacity)
     */
    public SetForTimeReturnValue setForTime(double time, Actor actor) {
        double alpha = (this.startAlpha + time * (this.endAlpha - this.startAlpha));
        actor.setAlpha(alpha);
        
        SetForTimeReturnValue returnValue = new SetForTimeReturnValue();
        returnValue.angle = alpha;
        return returnValue;
//        return alpha;
    }

    /**
     * Set alpha transparency minimum and maximum value.
     * This value can be coerced by Actor's property isGloblAlpha.
     *
     * @param start {number} a float indicating the starting alpha value.
     * @param end {number} a float indicating the ending alpha value.
     */
    public AlphaBehavior setValues(double start, double end) {
        this.startAlpha = start;
        this.endAlpha = end;
        return this;
    }
    
    public String calculateKeyFrameData(double time ) {
//        time= this.interpolator.getPosition(time).y;
//        return  (this.startAlpha+time*(this.endAlpha-this.startAlpha));
        return "";
    }
    
    // TODO Check return type
    public SetForTimeReturnValue getKeyFrameDataValues(double time) {
        time = this.interpolator.getPosition(time).y;
        
        // FIXME
//        return {
//            alpha : this.startAlpha + time * (this.endAlpha - this.startAlpha)
//        };
        
        return null;
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
//        for( i=0; i<=keyframessize; i++ )    {
//            kfr= "" +
//                (i/keyframessize*100) + "%" + // percentage
//                "{" +
//                     "opacity: " + this.calculateKeyFrameData( i / keyframessize ) +
//                "}";
//
//            kfd+= kfr;
//        }
//
//        kfd+="}";

//        return kfd;
        return "";
    }
    
    // Add by me
    public AlphaBehavior setFrameTime(double startTime, double duration) {
        return (AlphaBehavior) super.setFrameTime(startTime, duration);
    }

    @Override
    public AlphaBehavior setPingPong() {
        return (AlphaBehavior) super.setPingPong();
    }

    @Override
    public AlphaBehavior setCycle(boolean bool) {
        return (AlphaBehavior) super.setCycle(bool);
    }

    @Override
    public AlphaBehavior addListener(BehaviorListener behaviorListener) {
        return (AlphaBehavior) super.addListener(behaviorListener);
    }
    
    

}
