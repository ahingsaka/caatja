package com.katspow.caatja.event;

import java.util.ArrayList;
import java.util.List;

import com.katspow.caatja.foundation.actor.Actor;

/**
 * This function creates a mouse event that represents a touch or mouse event.
 * @constructor
 */
public class CAATTouchEvent {
    
    public CAATTouchEvent() {
        this.touches= new ArrayList<TouchInfo>();
        this.changedTouches= new ArrayList<TouchInfo>();
    }
    
    /**
     * Time the touch event was triggered at.
     */
    public double time=           0;
    
    /**
     * Source Actor the event happened in.
     */
    public Actor source=         null;
    
    // TODO Check type
    /**
     * Original touch event.
     */
//    public HumanInputEvent sourceEvent=    null;

    /**
     * Was shift pressed ?
     */
    public boolean shift=          false;
    
    /**
     * Was control pressed ?
     */
    public boolean control=        false;
    
    /**
     * Was alt pressed ?
     */
    public boolean alt=            false;
    
    /**
     * Was meta pressed ?
     */
    public boolean meta=           false;

    /**
     * touches collection
     */
    public List<TouchInfo> touches         = null;
    
    /**
     * changed touches collection
     */
    public List<TouchInfo> changedTouches  = null;
    
    public CAATTouchEvent init(Actor source, double time) {

        this.source = source;
        
        // FIXME keyboard events ...
        // this.alt = sourceEvent.altKey;
        // this.control = sourceEvent.ctrlKey;
        // this.shift = sourceEvent.shiftKey;
        // this.meta = sourceEvent.metaKey;
        // this.sourceEvent= sourceEvent;
        this.time = time;

        return this;
    }
    /**
    *
    * @param touchInfo
    *  <{
    *      id : <number>,
    *      point : {
    *          x: <number>,
    *          y: <number> }
    *  }>
    * @return {*}
    */
    public CAATTouchEvent addTouch (TouchInfo touchInfo ) {
        if ( -1==this.touches.indexOf( touchInfo ) ) {
            this.touches.add( touchInfo );
        }
        return this;
    }
    
    public CAATTouchEvent addChangedTouch (TouchInfo touchInfo ) {
        if ( -1==this.changedTouches.indexOf( touchInfo ) ) {
            this.changedTouches.add( touchInfo );
        }
        return this;
    }
    
    public boolean isAltDown () {
        return this.alt;
    }
    public boolean isControlDown () {
        return this.control;
    }
    public boolean isShiftDown () {
        return this.shift;
    }
    public boolean isMetaDown() {
        return this.meta;
    }
//    public HumanInputEvent getSourceEvent () {
//        return this.sourceEvent;
//    }

}
