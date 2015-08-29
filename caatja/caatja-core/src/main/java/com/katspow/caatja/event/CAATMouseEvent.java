package com.katspow.caatja.event;

import java.util.ArrayList;
import java.util.List;

import com.katspow.caatja.foundation.actor.Actor;
import com.katspow.caatja.math.Pt;

/**
 * This function creates a mouse event that represents a touch or mouse event.
 * @constructor
 */
public class CAATMouseEvent {

    public CAATMouseEvent() {
        this.point = new Pt(0,0,0);
        this.screenPoint = new Pt(0,0,0);
        this.touches = new ArrayList<TouchInfo>();
    }

    /**
     * Original mouse/touch screen coord
     */
    public Pt screenPoint;
    
    /**
     * Transformed in-actor coordinate
     */
    public Pt point;
    
    /**
     * scene time when the event was triggered.
     */
    private double time = 0;
    
    /**
     * Actor the event was produced in.
     */
    public Actor source = null;

    /**
     * Was shift pressed ?
     */
    public boolean shift = false;
    
    /**
     * Was control pressed ?
     */
    public boolean control = false;
    
    /**
     * was alt pressed ?
     */
    public boolean alt = false;
    
    /**
     * was Meta key pressed ?
     */
    public boolean meta = false;
    
    /**
     * Original mouse/touch event
     */
//    public HumanInputEvent sourceEvent = null;
    
    public List<TouchInfo> touches = null;
    
    // Add by me
    // TODO Check type
    public double x;
    public double y;

    public CAATMouseEvent init(double x, double y, Actor source, Pt screenPoint, double time) {
        this.point.set(x, y);
        this.source = source;
        this.screenPoint = screenPoint;

        // FIXME keyboard events ...
        // this.alt = sourceEvent.altKey;
        // this.control = sourceEvent.ctrlKey;
        // this.shift = sourceEvent.shiftKey;
        // this.meta = sourceEvent.metaKey;

//        this.sourceEvent = sourceEvent;
        this.x=             x;
        this.y=             y;
        this.time = time;
        return this;
    }

    public boolean isAltDown() {
        return this.alt;
    }

    public boolean isControlDown() {
        return this.control;
    }

    public boolean isShiftDown() {
        return this.meta;
    }
    
    public boolean isMetaDown() {
        return this.meta;
    }
    
    public double getTime() {
        return time;
    }
    
//    public HumanInputEvent getSourceEvent() {
//        return this.sourceEvent;
//    }

}
