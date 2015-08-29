package com.katspow.caatja.foundation.timer;

import com.katspow.caatja.foundation.actor.ActorContainer;

/**
 * This class defines a timer action which is constrained to Scene time, so every Scene has the
 * abbility to create its own TimerTask objects. They must not be created by calling scene's
 * createTime method.
 *
 * <p>
 * A TimerTask is defined at least by:
 * <ul>
 *  <li>startTime: since when the timer will be active
 *  <li>duration:  from startTime to startTime+duration, the timerTask will be notifying (if set) the callback callback_tick.
 * </ul>
 * <p>
 * Upon TimerTask expiration, the TimerTask will notify (if set) the callback function callback_timeout.
 * Upon a call to the method cancel, the timer will be set expired, and (if set) the callback to callback_cancel will be
 * invoked.
 * <p>
 * Timer notifications will be performed <strong>BEFORE<strong> scene loop.
 *
 * @constructor
 *
 */
public class TimerTask {

    /**
     * Timer start time. Relative to Scene or Director time, depending who owns this TimerTask.
     */
    public double startTime = 0;
    
    /**
     * Timer duration.
     */
    public double duration = 0;
    
    /**
     * This callback will be called only once, when the timer expires.
     */
    public CallbackTimeout callback_timeout = null;
    
    /**
     * This callback will be called whenever the timer is checked in time.
     */
    public CallbackTick callback_tick = null;
    
    /**
     * This callback will be called when the timer is cancelled.
     */
    public CallbackCancel callback_cancel = null;

    /**
     * What TimerManager instance owns this task.
     */
    public TimerManager owner = null;
    
    /**
     * Scene or director instance that owns this TimerTask owner.
     */
    public ActorContainer scene = null;
    
    /**
     * An arbitrry id.
     */
    public int taskId = 0;
    
    /**
     * Remove this timer task on expiration/cancellation ?
     */
    public boolean remove = false;
    
    // Add by me
    public double sceneTime;
    
    // When set to TRUE, timertask is inactive
    public boolean suspended = false;

    /**
     * Create a TimerTask.
     * The taskId will be set by the scene.
     * @param startTime {number} an integer indicating TimerTask enable time.
     * @param duration {number} an integer indicating TimerTask duration.
     * @param callback_timeout {function( sceneTime {number}, timertaskTime{number}, timertask {CAAT.TimerTask} )} on timeout callback function.
     * @param callback_tick {function( sceneTime {number}, timertaskTime{number}, timertask {CAAT.TimerTask} )} on tick callback function.
     * @param callback_cancel {function( sceneTime {number}, timertaskTime{number}, timertask {CAAT.TimerTask} )} on cancel callback function.
     *
     * @return this
     */
    public TimerTask create(double startTime, double duration, CallbackTimeout callback_timeout, CallbackTick callback_tick, CallbackCancel callback_cancel) {
        this.startTime = startTime;
        this.duration = duration;
        this.callback_timeout = callback_timeout;
        this.callback_tick = callback_tick;
        this.callback_cancel = callback_cancel;
        return this;
    }

    /**
     * Performs TimerTask operation. The task will check whether it is in frame time, and will
     * either notify callback_timeout or callback_tick.
     *
     * @param time {number} an integer indicating scene time.
     * @return this
     *
     * @protected
     *
     */
    public TimerTask checkTask(double time) {
        double ttime = time;
        ttime -= this.startTime;
        if (ttime >= this.duration) {
            this.remove = true;
            if (callback_timeout != null) {
                this.callback_timeout.call(time, ttime, this);
            }
        } else {
            if (callback_tick != null) {
                this.callback_tick.call(time, ttime, this);
            }
        }
        return this;
    }
    
    public double remainingTime() {
        return this.duration - (this.scene.time-this.startTime);
    }

    /**
     * Reschedules this TimerTask by changing its startTime to current scene's time.
     * @param time {number} an integer indicating scene time.
     * @return this
     */
    public TimerTask reset(double time) {
        this.remove = false;
        this.startTime = time;
        this.owner.ensureTimerTask(this);
        return this;
    }
    
    /**
     * Cancels this timer by removing it on scene's next frame. The function callback_cancel will
     * be called.
     * @return this
     */
    public TimerTask cancel() {
        this.remove= true;
        if ( null!=this.callback_cancel ) {
            this.callback_cancel.call( this.scene.time, this.scene.time-this.startTime, this );
        }
        
        return this;
    }
    
    public TimerTask addTime(double time) {
        this.duration += time;
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + taskId;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TimerTask other = (TimerTask) obj;
        if (taskId != other.taskId)
            return false;
        return true;
    }

}
