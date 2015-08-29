package com.katspow.caatja.foundation.timer;

import java.util.ArrayList;
import java.util.List;
import com.katspow.caatja.foundation.actor.ActorContainer;

public class TimerManager {
    
    public TimerManager() {
        this.timerList = new ArrayList<TimerTask>();
    }

    /**
     * Collection of registered timers.
     * @type {CAAT.Foundation.Timer.TimerManager}
     * @private
     */
    private List<TimerTask> timerList;
    
    /**
     * Index sequence to idenfity registered timers.
     * @private
     */
    private int timerSequence = 0;
    
    /**
     * Check and apply timers in frame time.
     * 
     * Add by me : if timertask is suspended, do not check task.
     * 
     * @param time {number} the current Scene time.
     */
    public void checkTimers (double time) {
        List<TimerTask> tl = this.timerList;
        int i = tl.size() - 1;
        while (i >= 0) {
            TimerTask timerTask = tl.get(i);
            if (!timerTask.remove && !timerTask.suspended) {
                timerTask.checkTask(time);
            }
            i--;
        }
    }
    /**
     * Make sure the timertask is contained in the timer task list by adding it to the list in case it
     * is not contained.
     * @param timertask {CAAT.TimerTask} a CAAT.TimerTask object.
     * @return this
     */
    public TimerManager ensureTimerTask (TimerTask timertask ) {
        if ( !this.hasTimer(timertask) ) {
            this.timerList.add(timertask);
        }
        return this;
    }
    /**
     * Check whether the timertask is in this scene's timer task list.
     * @param timertask {CAAT.TimerTask} a CAAT.TimerTask object.
     * @return {boolean} a boolean indicating whether the timertask is in this scene or not.
     */
    public boolean hasTimer (TimerTask timertask ) {
        return this.timerList.contains(timertask);
    }
    /**
     * Creates a timer task. Timertask object live and are related to scene's time, so when an Scene
     * is taken out of the Director the timer task is paused, and resumed on Scene restoration.
     *
     * @param startTime {number} an integer indicating the scene time this task must start executing at.
     * @param duration {number} an integer indicating the timerTask duration.
     * @param callback_timeout {function} timer on timeout callback function.
     * @param callback_tick {function} timer on tick callback function.
     * @param callback_cancel {function} timer on cancel callback function.
     *
     * @return {CAAT.TimerTask} a CAAT.TimerTask class instance.
     */
    public TimerTask createTimer (double startTime, double duration, CallbackTimeout callback_timeout, CallbackTick callback_tick, CallbackCancel callback_cancel, ActorContainer scene) {

        TimerTask tt= new TimerTask().create(
                    startTime,
                    duration,
                    callback_timeout,
                    callback_tick,
                    callback_cancel );

        tt.taskId= this.timerSequence++;
        tt.sceneTime = scene.time;
        tt.owner = this;
        tt.scene = scene;

        this.timerList.add( tt );

        return tt;
    }
    
    /**
     * Removes expired timers. This method must not be called directly.
     */
    public void removeExpiredTimers () {
        int i;
        List<TimerTask> tl = this.timerList;
        for( i=0; i<tl.size(); i++ ) {
            if ( tl.get(i).remove ) {
                // TODO Check
                tl.remove(i);
            }
        }
    }
}
