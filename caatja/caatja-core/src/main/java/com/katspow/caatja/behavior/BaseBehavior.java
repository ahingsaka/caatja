package com.katspow.caatja.behavior;

import java.util.ArrayList;

import com.katspow.caatja.behavior.listener.BehaviorAppliedListener;
import com.katspow.caatja.behavior.listener.BehaviorExpiredListener;
import com.katspow.caatja.behavior.listener.BehaviorStartedListener;
import com.katspow.caatja.foundation.actor.Actor;

/** Behaviors are keyframing elements.
* By using a BehaviorContainer, you can specify different actions on any animation Actor.
* An undefined number of Behaviors can be defined for each Actor.
*
* There're the following Behaviors:
*  + AlphaBehavior:   controls container/actor global alpha.
*  + RotateBehavior:  takes control of rotation affine transform.
*  + ScaleBehavior:   takes control of scaling on x/y axis affine transform.
*  + PathBehavior:    takes control of translating an Actor/ActorContainer across a path [ie. pathSegment collection].
*  + GenericBehavior: applies a behavior to any given target object's property, or notifies a callback.
*
**/

public class BaseBehavior {

    /**
     * Behavior base class.
     *
     * <p>
     * A behavior is defined by a frame time (behavior duration) and a behavior application function called interpolator.
     * In its default form, a behaviour is applied linearly, that is, the same amount of behavior is applied every same
     * time interval.
     * <p>
     * A concrete Behavior, a rotateBehavior in example, will change a concrete Actor's rotationAngle during the specified
     * period.
     * <p>
     * A behavior is guaranteed to notify (if any observer is registered) on behavior expiration.
     * <p>
     * A behavior can keep an unlimited observers. Observers are objects of the form:
     * <p>
     * <code>
     * {
     *      behaviorExpired : function( behavior, time, actor);
     *      behaviorApplied : function( behavior, time, normalizedTime, actor, value);
     * }
     * </code>
     * <p>
     * <strong>behaviorExpired</strong>: function( behavior, time, actor). This method will be called for any registered observer when
     * the scene time is greater than behavior's startTime+duration. This method will be called regardless of the time
     * granurality.
     * <p>
     * <strong>behaviorApplied</strong> : function( behavior, time, normalizedTime, actor, value). This method will be called once per
     * frame while the behavior is not expired and is in frame time (behavior startTime>=scene time). This method can be
     * called multiple times.
     * <p>
     * Every behavior is applied to a concrete Actor.
     * Every actor must at least define an start and end value. The behavior will set start-value at behaviorStartTime and
     * is guaranteed to apply end-value when scene time= behaviorStartTime+behaviorDuration.
     * <p>
     * You can set behaviors to apply forever that is cyclically. When a behavior is cycle=true, won't notify
     * behaviorExpired to its registered observers.
     * <p>
     * Other Behaviors simply must supply with the method <code>setForTime(time, actor)</code> overriden.
     *
     * @constructor
     */
    public BaseBehavior() {
        this.lifecycleListenerList = new ArrayList<BehaviorListener>();
        
        // Add by me
//        this.behaviorAppliedListenersList = new ArrayList<BehaviorAppliedListener>();
//        this.behaviorExpiredListenersList = new ArrayList<BehaviorExpiredListener>();
//        this.behaviorStartedListenersList = new ArrayList<BehaviorStartedListener>();
        
        this.setDefaultInterpolator();
    }
    
    public enum Status {
        NOT_STARTED(0), STARTED(1), EXPIRED(2);
        
        private int val;

        private Status(int val) {
            this.val = val;
        }
        
        public int getVal() {
            return val;
        }
    }
    
    Interpolator DefaultInterpolator=    Interpolator.createLinearInterpolator(false, null);
    Interpolator DefaultPPInterpolator=  Interpolator.createLinearInterpolator(true, null);

    /**
     * Behavior lifecycle observer list.
     * @private
     */
    public ArrayList<BehaviorListener> lifecycleListenerList;
    
    // Add by me (change, we have one list for each behavioral procedure)
//    private List<BehaviorAppliedListener> behaviorAppliedListenersList;
//    private List<BehaviorExpiredListener> behaviorExpiredListenersList;
//    private List<BehaviorStartedListener> behaviorStartedListenersList;
    
    /**
     * Behavior application start time related to scene time.
     * @private
     */
    public double behaviorStartTime = -1;
    
    /**
     * Behavior application duration time related to scene time.
     * @private
     */
    protected double behaviorDuration = -1;
    
    /**
     * Will this behavior apply for ever in a loop ?
     * @private
     */
    public boolean cycleBehavior = false;
    
//    public boolean expired = true; // indicates whether the behavior is expired.
    
    /**
     * behavior status.
     * @private
     */
    public Status status = Status.NOT_STARTED;
    
    /**
     * An interpolator object to apply behaviors using easing functions, etc.
     * Unless otherwise specified, it will be linearly applied.
     * @type {CAAT.Behavior.Interpolator}
     * @private
     */
    public Interpolator interpolator = null;
    
    /**
     * The actor this behavior will be applied to.
     * @type {CAAT.Foundation.Actor}
     * @private
     */
    public Actor actor = null;
    
    /**
     * An id to identify this behavior.
     */
    public String id; // an integer id suitable to identify this behavior by number.
    
    /**
     * Initial offset to apply this behavior the first time.
     * @type {number}
     * @private
     */
    public double timeOffset = 0;
    
    /**
     * Apply the behavior, or just calculate the values ?
     * @type {boolean}
     */
    public boolean doValueApplication = true;
    
    /**
     * Is this behavior solved ? When called setDelayTime, this flag identifies whether the behavior
     * is in time relative to the scene.
     * @type {boolean}
     * @private
     */
    public boolean solved = true;
    
    /**
     * if true, this behavior will be removed from the this.actor instance when it expires.
     * @type {boolean}
     * @private
     */
    public boolean discardable = false;
    
    /**
     * does this behavior apply relative values ??
     */
    public boolean isRelative = false;
    
    /**
     * Set this behavior as relative value application to some other measures.
     * Each Behavior will define its own.
     * @param bool
     * @returns {*}
     */
    public BaseBehavior setRelative(boolean bool ) {
        this.isRelative= bool;
        return this;
    }

    public BaseBehavior setRelativeValues() {
        this.isRelative= true;
        return this;
    }
    
    /**
     * Parse a behavior of this type.
     * @param obj {object} an object with a behavior definition.
     */
    public void parse(Object obj ) {
//        if ( obj.pingpong ) {
//            this.setPingPong();
//        }
//        if ( obj.cycle ) {
//            this.setCycle(true);
//        }
//        double delay= obj.delay || 0;
//        double duration= obj.duration || 1000;
//
//        this.setDelayTime( delay, duration );
//
//        if ( obj.interpolator ) {
//            this.setInterpolator( Interpolator.parse(obj.interpolator) );
//        }
    }

    /**
     * Set whether this behavior will apply behavior values to a reference Actor instance.
     * @param apply {boolean}
     * @return {*}
     */
    public BaseBehavior setValueApplication(boolean apply ) {
        this.doValueApplication= apply;
        return this;
    }
    
    /**
     * Set this behavior offset time.
     * This method is intended to make a behavior start applying (the first) time from a different
     * start time.
     * @param offset {number} between 0 and 1
     * @return {*}
     */
    public BaseBehavior setTimeOffset(double timeOffset) {
        this.timeOffset = timeOffset;
        return this;
    }
    
    /**
     * Set this behavior status
     * @param st {CAAT.Behavior.BaseBehavior.Status}
     * @return {*}
     * @private
     */
    public void setStatus(Status status) {
        this.status = status;
    }
    
    /**
     * Sets this behavior id.
     * @param id {object}
     *
     */
    public BaseBehavior setId(String id) {
        this.id = id;
        return this;
    }

    /**
     * Sets the default interpolator to a linear ramp, that is, behavior will be applied linearly.
     * @return this
     */
    public BaseBehavior setDefaultInterpolator() {
        this.interpolator = DefaultInterpolator;
        return this;
    }

    /**
     * Sets default interpolator to be linear from 0..1 and from 1..0.
     * @return this
     */
    public BaseBehavior setPingPong() {
        this.interpolator = DefaultPPInterpolator;
        return this;
    }
    
    /**
     * Sets behavior start time and duration. Start time is set absolutely relative to scene time.
     * @param startTime {number} an integer indicating behavior start time in scene time in ms..
     * @param duration {number} an integer indicating behavior duration in ms.
     */
    public BaseBehavior setFrameTime(double startTime, double duration) {
        this.behaviorStartTime = startTime;
        this.behaviorDuration = duration;
        this.setStatus(Status.NOT_STARTED);
        return this;
    }
    
    /**
     * Sets behavior start time and duration. Start time is relative to scene time.
     *
     * a call to
     *   setFrameTime( scene.time, duration ) is equivalent to
     *   setDelayTime( 0, duration )
     * @param delay {number}
     * @param duration {number}
     */
    public BaseBehavior setDelayTime(double delay, double duration ) {
        this.behaviorStartTime= delay;
        this.behaviorDuration=  duration;
        this.setStatus( BaseBehavior.Status.NOT_STARTED );
        this.solved= false;
        // TODO Check possible ?
//        this.expired = false;

        return this;

    }

    /**
     * Make this behavior not applicable.
     * @return {*}
     */
    public BaseBehavior setOutOfFrameTime() {
        this.setStatus(Status.EXPIRED);
        this.behaviorStartTime = Double.MAX_VALUE;
        this.behaviorDuration = 0;
        return this;
    }

    /**
     * Changes behavior default interpolator to another instance of CAAT.Interpolator.
     * If the behavior is not defined by CAAT.Interpolator factory methods, the interpolation function must return
     * its values in the range 0..1. The behavior will only apply for such value range.
     * @param interpolator a CAAT.Interpolator instance.
     */
    public BaseBehavior setInterpolator(Interpolator interpolator) {
        this.interpolator = interpolator;
        return this;
    }

    /**
     * This method must no be called directly.
     * The director loop will call this method in orther to apply actor behaviors.
     * @param time the scene time the behaviro is being applied at.
     * @param actor a CAAT.Actor instance the behavior is being applied to.
     * @throws Exception 
     */
    public void apply(double time, Actor actor) throws Exception {
        
        if ( !this.solved ) {
            this.behaviorStartTime+= time;
            this.solved= true;
        }
        
        time+= this.timeOffset*this.behaviorDuration;
        
        double orgTime= time;
        if ( this.isBehaviorInTime(time,actor) )    {
            time= this.normalizeTime(time);
            this.fireBehaviorAppliedEvent(
                    actor,
                    orgTime,
                    time,
                    this.setForTime( time, actor ) );
        }
    }

    /**
     * Sets the behavior to cycle, ie apply forever.
     * @param bool a boolean indicating whether the behavior is cycle.
     */
    public BaseBehavior setCycle(boolean bool) {
        this.cycleBehavior = bool;
        return this;
    }
    
    public boolean isCycle() {
		return cycleBehavior;
	}

    /**
     * Adds an observer to this behavior.
     * @param behaviorListener an observer instance.
     */
    public BaseBehavior addListener(BehaviorListener behaviorListener) {
        this.lifecycleListenerList.add(behaviorListener);
        return this;
    }
    
    // Add by me
//    public BaseBehavior addAppliedListener(BehaviorAppliedListener behaviorAppliedListener) {
//        this.behaviorAppliedListenersList.add(behaviorAppliedListener);
//        return this;
//    }
//    
//    public BaseBehavior addExpiredListener(BehaviorExpiredListener behaviorExpiredListener) {
//        this.behaviorExpiredListenersList.add(behaviorExpiredListener);
//        return this;
//    }
//    
//    public BaseBehavior addStartedListener(BehaviorStartedListener behaviorStartedListener) {
//        this.behaviorStartedListenersList.add(behaviorStartedListener);
//        return this;
//    }
    
    /**
     * Remove all registered listeners to the behavior.
     */
    public BaseBehavior emptyListenerList() {
        this.lifecycleListenerList.clear();
        return this;
    }
    
    // Add by me
//    public BaseBehavior emptyAppliedListenerList(BehaviorAppliedListener behaviorAppliedListener) {
//        this.behaviorAppliedListenersList.clear();
//        return this;
//    }
//    
//    public BaseBehavior emptyExpiredListenerList(BehaviorExpiredListener behaviorExpiredListener) {
//        this.behaviorExpiredListenersList.clear();
//        return this;
//    }
//    
//    public BaseBehavior emptyStartedListenerList(BehaviorStartedListener behaviorStartedListener) {
//        this.behaviorStartedListenersList.clear();
//        return this;
//    }

    /**
     * @return an integer indicating the behavior start time in ms..
     */
    public double getStartTime() {
        return this.behaviorStartTime;
    }

    /**
     * @return an integer indicating the behavior duration time in ms.
     */
    public double getDuration() {
        return this.behaviorDuration;

    }

    /**
     * Chekcs whether the behaviour is in scene time.
     * In case it gets out of scene time, and has not been tagged as expired, the behavior is expired and observers
     * are notified about that fact.
     * @param time the scene time to check the behavior against.
     * @param actor the actor the behavior is being applied to.
     * @return a boolean indicating whether the behavior is in scene time.
     */
    public boolean isBehaviorInTime(double time, Actor actor) {
        
        if (this.status == Status.EXPIRED || this.behaviorStartTime < 0) {
            return false;
        }

        if (this.cycleBehavior) {
            if (time >= this.behaviorStartTime) {
                time = (time - this.behaviorStartTime) % this.behaviorDuration + this.behaviorStartTime;
            }
        }

        if (time > this.behaviorStartTime + this.behaviorDuration) {
            if (this.status != Status.EXPIRED) {
                this.setExpired(actor, time);
            }

            return false;
        }
        
        if ( this.status==Status.NOT_STARTED ) {
            this.status=Status.STARTED;
            this.fireBehaviorStartedEvent(actor,time);
        }

        return this.behaviorStartTime <= time; // && time < this.behaviorStartTime + this.behaviorDuration;
    }
    
    /**
     * Notify observers the first time the behavior is applied.
     * @param actor
     * @param time
     * @private
     */
    public void fireBehaviorStartedEvent(Actor actor, double time) {
        
        for (BehaviorListener behaviorListener : this.lifecycleListenerList) {
            // TODO Check
            BehaviorStartedListener behaviorStartedListener = behaviorListener.getBehaviorStartedListener();
            if (behaviorStartedListener != null) {
                behaviorStartedListener.onStarted(this, time, actor);
//                behaviorListener.behaviorStarted(this, time, actor);
            }
        }
        
        // Add by me
//        for (BehaviorStartedListener behaviorStartedListener : behaviorStartedListenersList) {
//            behaviorStartedListener.call(this, time, actor);
//        }
        
    }

    /**
     * Notify observers about expiration event.
     * @param actor a CAAT.Actor instance
     * @param time an integer with the scene time the behavior was expired at.
     * @private
     */
    public void fireBehaviorExpiredEvent(Actor actor, double time) {
        for (BehaviorListener b : this.lifecycleListenerList) {
            // TODO Make expired as object ?
            BehaviorExpiredListener behaviorExpiredListener = b.getBehaviorExpiredListener();
            if (behaviorExpiredListener != null) {
                behaviorExpiredListener.onExpired(this, time, actor);
//                b.behaviorExpired(this, time, actor);
            }
        }
        
        // Add by me
//        for (BehaviorExpiredListener behaviorExpiredListener : behaviorExpiredListenersList) {
//            behaviorExpiredListener.call(this, time, actor);
//        }

    }
    
    /**
     * Notify observers about behavior being applied.
     * @param actor a CAAT.Actor instance the behavior is being applied to.
     * @param time the scene time of behavior application.
     * @param normalizedTime the normalized time (0..1) considering 0 behavior start time and 1
     * behaviorStartTime+behaviorDuration.
     * @param value the value being set for actor properties. each behavior will supply with its own value version.
     * @private
     */
    // FIXME Object value
    public void fireBehaviorAppliedEvent(Actor actor, double time, double normalizedTime, SetForTimeReturnValue value) throws Exception    {
        for (BehaviorListener b : this.lifecycleListenerList) {
         // TODO Make applied as object ?
            BehaviorAppliedListener behaviorAppliedListener = b.getBehaviorAppliedListener();
            if (behaviorAppliedListener != null) {
                behaviorAppliedListener.onApplied(this,time,normalizedTime,actor,value);
//                b.behaviorApplied(this,time,normalizedTime,actor,value);
            }
        }
        
        // Add by me
//        for (BehaviorAppliedListener behaviorAppliedListener : behaviorAppliedListenersList) {
//            behaviorAppliedListener.call(this, time, normalizedTime, actor, value);
//        }

    }

    /**
     * Convert scene time into something more manageable for the behavior.
     * behaviorStartTime will be 0 and behaviorStartTime+behaviorDuration will be 1.
     * the time parameter will be proportional to those values.
     * @param time the scene time to be normalized. an integer.
     * @private
     */
    public double normalizeTime(double time) {
        time = time - this.behaviorStartTime;
        if (this.cycleBehavior) {
            time %= this.behaviorDuration;
        }
        return this.interpolator.getPosition(time / this.behaviorDuration).y;
    }

    /**
     * Sets the behavior as expired.
     * This method must not be called directly. It is an auxiliary method to isBehaviorInTime method.
     * @param actor {CAAT.Actor}
     * @param time {integer} the scene time.
     * @private
     */
    public void setExpired(Actor actor, double time) {
        // set for final interpolator value.
        this.status = Status.EXPIRED;
        this.setForTime(this.interpolator.getPosition(1).y, actor);
        this.fireBehaviorExpiredEvent(actor, time);
        
        if ( this.discardable ) {
            this.actor.removeBehaviour( this );
        }
    }

    /**
     * This method must be overriden for every Behavior breed.
     * Must not be called directly.
     * @param actor {CAAT.Actor} a CAAT.Actor instance.
     * @param time {number} an integer with the scene time.
     * @private
     */
    // TODO Check good return type ??? No choice actually
    public SetForTimeReturnValue setForTime(double time, Actor actor) {
        return null;
    }
    
    /**
     * Get this behaviors CSS property name application.
     * @return {String}
     */
    public String getPropertyName() {
        return "";
    }
    
    /**
     * Calculate a CSS3 @key-frame for this behavior at the given time.
     * @param time {number}
     */
    public String calculateKeyFrameData(double time) {
        return null;
    }

    /**
     * Calculate a CSS3 @key-frame data values instead of building a CSS3 @key-frame value.
     * @param time {number}
     */
    public SetForTimeReturnValue getKeyFrameDataValues(double time) {
        return null;
    }

    /**
     * Calculate a complete CSS3 @key-frame set for this behavior.
     * @param prefix {string} browser vendor prefix
     * @param name {string} keyframes animation name
     * @param keyframessize {number} number of keyframes to generate
     */
    public String calculateKeyFramesData(String prefix, String name, int keyframessize) {
        return null;
    }


}
