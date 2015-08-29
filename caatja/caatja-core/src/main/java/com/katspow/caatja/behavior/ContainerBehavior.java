package com.katspow.caatja.behavior;

import java.util.ArrayList;

import com.katspow.caatja.behavior.listener.BehaviorAppliedListener;
import com.katspow.caatja.behavior.listener.BehaviorExpiredListener;
import com.katspow.caatja.foundation.actor.Actor;

public class ContainerBehavior extends BaseBehavior { //implements BehaviorListener {
    
    // TODO
    public void parse(Object obj ) {
//        if ( obj.behaviors && obj.behaviors.length ) {
//            for( int i=0; i<obj.behaviors.length; i+=1 ) {
//                this.addBehavior( CAAT.Behavior.BaseBehavior.parse( obj.behaviors[i] ) );
//            }
//        }
//        
//        super.parse(obj);
    }
    
    /**
     * <p>
     * A ContainerBehavior is a holder to sum up different behaviors.
     * <p>
     * It imposes some constraints to contained Behaviors:
     * <ul>
     * <li>The time of every contained behavior will be zero based, so the frame time set for each behavior will
     * be referred to the container's behaviorStartTime and not scene time as usual.
     * <li>Cycling a ContainerBehavior means cycling every contained behavior.
     * <li>The container will not impose any Interpolator, so calling the method <code>setInterpolator(CAAT.Interpolator)
     * </code> will be useless.
     * <li>The Behavior application time will be bounded to the Container's frame time. I.E. if we set a container duration
     * to 10 seconds, setting a contained behavior's duration to 15 seconds will be useless since the container will stop
     * applying the behavior after 10 seconds have elapsed.
     * <li>Every ContainerBehavior adds itself as an observer for its contained Behaviors. The main reason is because
     * ContainerBehaviors modify cycling properties of its contained Behaviors. When a contained
     * Behavior is expired, if the Container has isCycle=true, will unexpire the contained Behavior, otherwise, it won't be
     * applied in the next frame. It is left up to the developer to manage correctly the logic of other posible contained
     * behaviors observers.
     * </ul>
     *
     * <p>
     * A ContainerBehavior can contain other ContainerBehaviors at will.
     * <p>
     * A ContainerBehavior will not apply any CAAT.Actor property change by itself, but will instrument its contained
     * Behaviors to do so.
     *
     * @constructor
     * @extends CAAT.Behavior
     */
    public ContainerBehavior() {
        super();
        this.behaviors = new ArrayList<BaseBehavior>();
        
        // Add by me
        behaviorListener = new BehaviorListener();
        
        BehaviorExpiredListener behaviorExpiredListener = new BehaviorExpiredListener() {
            public void onExpired(BaseBehavior behavior, double time, Actor actor) {
                if (cycleBehavior) {
                    behavior.setStatus(Status.STARTED);
                }
            }
        };
        
        BehaviorAppliedListener behaviorAppliedListener = new BehaviorAppliedListener() {
            public void onApplied(BaseBehavior behavior, double scenetime, double time, Actor actor,
                    SetForTimeReturnValue value) throws Exception {
                fireBehaviorAppliedEvent(actor, scenetime, time, value);
            }
        };
        
        behaviorListener.setBehaviorExpiredListener(behaviorExpiredListener);
        behaviorListener.setBehaviorAppliedListener(behaviorAppliedListener);
    }
    
    public ContainerBehavior(boolean conforming) {
        super();
        this.behaviors = new ArrayList<BaseBehavior>();
        if ( conforming ) {
            this.conforming= true;
        }
    }

    /**
     * A collection of behaviors.
     * @type {Array.<CAAT.Behavior.BaseBehavior>}
     */
    private ArrayList<BaseBehavior> behaviors; // contained behaviors array
    private boolean recursiveCycleBehavior = false;
    private boolean conforming = false;
    
    // Add by me
    private double duration;
    private BehaviorListener behaviorListener;
    
    /**
     * Proportionally change this container duration to its children.
     * @param duration {number} new duration in ms.
     * @return this;
     */
    public ContainerBehavior conformToDuration(double duration ) {
        this.duration= duration;
        
        double f= duration/this.duration;
        
        for (BaseBehavior behavior : this.behaviors) {
            behavior.setFrameTime(behavior.getStartTime()*f, behavior.getDuration() * f);
        }
        
        return this;
    }
    
    /**
     * Get a behavior by mathing its id.
     * @param id {object}
     */
    public BaseBehavior getBehaviorById(String id) {
        
        for (BaseBehavior behavior : this.behaviors) {
            if (behavior.id.equals(id)) {
                return behavior;
            }
        }
        
        return null;
    }
    
    public ContainerBehavior setCycle(boolean cycle, boolean recurse ) {
        super.setCycle(cycle);

		if (recurse) {
			for (BaseBehavior behavior : this.behaviors) {
				behavior.setCycle(cycle);
			}
		}

        this.recursiveCycleBehavior= recurse;

        return this;
    }
    
    /**
     * Add a new behavior to the container.
     * @param behavior {CAAT.Behavior.BaseBehavior}
     */
    public ContainerBehavior addBehavior(BaseBehavior behavior) {
        this.behaviors.add(behavior);
        
        // TODO Change by me
        behavior.addListener(behaviorListener);
//        behavior.addListener(this);
        
        if ( this.conforming ) {
            double len= behavior.behaviorDuration + behavior.behaviorStartTime;
            if ( this.behaviorDuration < len ) {
                this.behaviorDuration= len;
                this.behaviorStartTime= 0;
            }
        }
        
        if ( this.recursiveCycleBehavior ) {
            behavior.setCycle( this.isCycle() );
        }
        
        return this;
    }
    
    /**
     * Applies every contained Behaviors.
     * The application time the contained behaviors will receive will be ContainerBehavior related and not the
     * received time.
     * @param time an integer indicating the time to apply the contained behaviors at.
     * @param actor a CAAT.Actor instance indicating the actor to apply the behaviors for.
     * @throws Exception 
     */
    public void apply (double time,Actor actor) throws Exception {
        
        if ( !this.solved ) {
            this.behaviorStartTime+= time;
            this.solved= true;
        }
        
        time+= this.timeOffset*this.behaviorDuration;
        
        if ( this.isBehaviorInTime(time,actor) )   {
            time-= this.behaviorStartTime;
            if ( this.cycleBehavior ){
                time%= this.behaviorDuration;
            }
            
            for (BaseBehavior behaviour : this.behaviors) {
                behaviour.apply(time, actor);
            }
        }
    }
    
//    /**
//     * This method does nothing for containers, and hence has an empty implementation.
//     * @param interpolator a CAAt.Interpolator instance.
//     * 
//     * TODO No return ?
//     */
//    public void setInterpolator (Path path) {
//        // return this;
//    }
    
    /**
     * This method is the observer implementation for every contained behavior.
     * If a container is Cycle=true, won't allow its contained behaviors to be expired.
     * @param behavior a CAAT.Behavior instance which has been expired.
     * @param time an integer indicating the time at which has become expired.
     * @param actor a CAAT.Actor the expired behavior is being applied to.
     */
//    @Override
//    public void behaviorExpired(BaseBehavior behavior, double time, Actor actor) {
//        if (this.cycleBehavior) {
//            behavior.setStatus(Status.STARTED);
//        }
//    }
    
    /**
     * Implementation method of the behavior.
     * Just call implementation method for its contained behaviors.
     * @param time an intenger indicating the time the behavior is being applied at.
     * @param actor a CAAT.Actor the behavior is being applied to.
     */
    public SetForTimeReturnValue setForTime (double time, Actor actor) {
        SetForTimeReturnValue retValue= null;
        for (BaseBehavior behavior : this.behaviors) {
            retValue = behavior.setForTime(time, actor);
        }
        
        return retValue;
    }
    
    /**
     * Expire this behavior and the children applied at the parameter time.
     * @param actor {CAAT.Foundation.Actor}
     * @param time {number}
     * @return {*}
     */
    @Override
    public void setExpired(Actor actor, double time) {
        super.setExpired(actor, time);
     // set for final interpolator value.
        for (BaseBehavior behavior : this.behaviors) {
            if (behavior.status != Status.EXPIRED) {
                behavior.setExpired(actor, time-this.behaviorStartTime);
            }
        }
        
        /**
         * moved here from the beggining of the method.
         * allow for expiration observers to reset container behavior and its sub-behaviors
         * to redeem.
         */
        super.setExpired(actor, time);
        
        // TODO NO return ?
//        return this;
    }
    
    @Override
    public ContainerBehavior setFrameTime(double startTime, double duration) {
        super.setFrameTime(startTime, duration);
        
        for (BaseBehavior behavior : this.behaviors) {
            behavior.setStatus(Status.NOT_STARTED);
        }
        return this;
    }
    
    @Override
    public ContainerBehavior setDelayTime(double start, double duration )  {
        super.setDelayTime(start,duration);

        for (BaseBehavior behavior : this.behaviors) {
            behavior.setStatus(Status.NOT_STARTED);
        }
        return this;
    }
    
    public SetForTimeReturnValue getKeyFrameDataValues(double referenceTime) {

        //var i, bh, time;
        double time;
        SetForTimeReturnValue keyFrameData = new SetForTimeReturnValue();
        keyFrameData.angle = 0d;
        keyFrameData.scaleX = 1d;
        keyFrameData.scaleY = 1d;
        keyFrameData.x = 0d;
        keyFrameData.y = 0d;
        
        for (int i = 0; i < this.behaviors.size(); i++) {
            BaseBehavior bh = this.behaviors.get(i);
            if (bh.status != Status.EXPIRED && !(bh instanceof GenericBehavior)) {

                // ajustar tiempos:
                //  time es tiempo normalizado a duracion de comportamiento contenedor.
                //      1.- desnormalizar
                time = referenceTime * this.behaviorDuration;

                //      2.- calcular tiempo relativo de comportamiento respecto a contenedor
                if (bh.behaviorStartTime <= time && bh.behaviorStartTime + bh.behaviorDuration >= time) {
                    //      3.- renormalizar tiempo reltivo a comportamiento.
                    time = (time - bh.behaviorStartTime) / bh.behaviorDuration;

                    //      4.- obtener valor de comportamiento para tiempo normalizado relativo a contenedor
                    // FIXME
//                    var obj= bh.getKeyFrameDataValues(time);
//                    for( var pr in obj ) {
//                        keyFrameData[pr]= obj[pr];
//                    }
                }
            }
        }

        return keyFrameData;
    }
    
//    calculateKeyFrameData(referenceTime, prefix, prevValues )  {
//
//        var i;
//        var bh;
//
//        var retValue= {};
//        var time;
//        var cssRuleValue;
//        var cssProperty;
//        var property;
//
//        for( i=0; i<this.behaviors.length; i++ ) {
//            bh= this.behaviors[i];
//            if ( /*!bh.expired*/ bh.status!==CAAT.Behavior.Status.EXPIRED && !(bh instanceof CAAT.GenericBehavior) ) {
//
//                // ajustar tiempos:
//                //  time es tiempo normalizado a duracion de comportamiento contenedor.
//                //      1.- desnormalizar
//                time= referenceTime * this.behaviorDuration;
//
//                //      2.- calcular tiempo relativo de comportamiento respecto a contenedor
//                if ( bh.behaviorStartTime<=time && bh.behaviorStartTime+bh.behaviorDuration>=time ) {
//                    //      3.- renormalizar tiempo reltivo a comportamiento.
//                    time= (time-bh.behaviorStartTime)/bh.behaviorDuration;
//
//                    //      4.- obtener valor de comportamiento para tiempo normalizado relativo a contenedor
//                    cssRuleValue= bh.calculateKeyFrameData(time);
//                    cssProperty= bh.getPropertyName(prefix);
//
//                    if ( typeof retValue[cssProperty] ==='undefined' ) {
//                        retValue[cssProperty]= "";
//                    }
//
//                    //      5.- asignar a objeto, par de propiedad/valor css
//                    retValue[cssProperty]+= cssRuleValue+" ";
//                }
//
//            }
//        }
//
//
//        var tr="";
//        var pv;
//        function xx(pr) {
//            if ( retValue[pr] ) {
//                tr+= retValue[pr];
//            } else {
//                if ( prevValues ) {
//                    pv= prevValues[pr];
//                    if ( pv ) {
//                        tr+= pv;
//                        retValue[pr]= pv;
//                    }
//                }
//            }
//
//        }
//
//        xx('translate');
//        xx('rotate');
//        xx('scale');
//
//        var keyFrameRule= "";
//
//        if ( tr ) {
//            keyFrameRule='-'+prefix+'-transform: '+tr+';';
//        }
//
//        tr="";
//        xx('opacity');
//        if( tr ) {
//            keyFrameRule+= ' opacity: '+tr+';';
//        }
    
//    keyFrameRule+=" -webkit-transform-origin: 0% 0%";
//
//        return {
//            rules: keyFrameRule,
//            ret: retValue
//        };
//
//    }

    /**
     *
     * @param prefix
     * @param name
     * @param keyframessize
     * @throws Exception 
     */
//    calculateKeyFramesData(prefix, name, keyframessize) {
//
//        if ( this.duration===Number.MAX_VALUE ) {
//            return "";
//        }
//
//        if ( typeof keyframessize==='undefined' ) {
//            keyframessize=100;
//        }
//
//        var i;
//        var prevValues= null;
//        var kfd= "@-"+prefix+"-keyframes "+name+" {";
//        var ret;
//        var time;
//        var kfr;
//
//        for( i=0; i<=keyframessize; i++ )    {
//            time= this.interpolator.getPosition(i/keyframessize).y;
//            ret= this.calculateKeyFrameData(time, prefix, prevValues);
//            kfr= "" +
//                (i/keyframessize*100) + "%" + // percentage
//                "{" + ret.rules + "}\n";
//
//            prevValues= ret.ret;
//            kfd+= kfr;
//        }
//
//        kfd+= "}";
//
//        return kfd;
//    }
    
//    calculateKeyFramesData:function (prefix, name, keyframessize, anchorX, anchorY) {
//
//        function toKeyFrame(obj, prevKF) {
//
//            for( var i in prevKF ) {
//                if ( !obj[i] ) {
//                    obj[i]= prevKF[i];
//                }
//            }
//
//            var ret= "-" + prefix + "-transform:";
//
//            if ( obj.x || obj.y ) {
//                var x= obj.x || 0;
//                var y= obj.y || 0;
//                ret+= "translate("+x+"px,"+y+"px)";
//            }
//
//            if ( obj.angle ) {
//                ret+= " rotate("+obj.angle+"rad)";
//            }
//
//            if ( obj.scaleX!==1 || obj.scaleY!==1 ) {
//                ret+= " scale("+(obj.scaleX)+","+(obj.scaleY)+")";
//            }
//
//            ret+=";";
//
//            if ( obj.alpha ) {
//                ret+= " opacity: "+obj.alpha+";";
//            }
//
//            if ( anchorX!==.5 || anchorY!==.5) {
//                ret+= " -" + prefix + "-transform-origin:"+ (anchorX*100) + "% " + (anchorY*100) + "%;";
//            }
//
//            return ret;
//        }
//
//        if (this.duration === Number.MAX_VALUE) {
//            return "";
//        }
//
//        if (typeof anchorX==="undefined") {
//            anchorX= .5;
//        }
//
//        if (typeof anchorY==="undefined") {
//            anchorY= .5;
//        }
//
//        if (typeof keyframessize === 'undefined') {
//            keyframessize = 100;
//        }
//
//        var i;
//        var kfd = "@-" + prefix + "-keyframes " + name + " {";
//        var time;
//        var prevKF= {};
//
//        for (i = 0; i <= keyframessize; i++) {
//            time = this.interpolator.getPosition(i / keyframessize).y;
//
//            var obj = this.getKeyFrameDataValues(time);
//
//            kfd += "" +
//                (i / keyframessize * 100) + "%" + // percentage
//                "{" + toKeyFrame(obj, prevKF) + "}\n";
//
//            prevKF= obj;
//
//        }
//
//        kfd += "}\n";
//
//        return kfd;
//    }

//    @Override
//    public void behaviorApplied(BaseBehavior behavior, double scenetime, double time, Actor actor, SetForTimeReturnValue value) throws Exception {
//        this.fireBehaviorAppliedEvent(actor, scenetime, time, value);
//    }
    
//    @Override
//    public void behaviorStarted(BaseBehavior behavior, double time, Actor actor) {
//        
//    }

    // Add by me
    @Override
    public ContainerBehavior setCycle(boolean bool) {
        return (ContainerBehavior) super.setCycle(bool);
    }

    @Override
    public ContainerBehavior addListener(BehaviorListener behaviorListener) {
        return (ContainerBehavior) super.addListener(behaviorListener);
    }

    @Override
    public ContainerBehavior setId(String id) {
        return (ContainerBehavior) super.setId(id);
    }
    
}
