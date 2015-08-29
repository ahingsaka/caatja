package com.katspow.caatja.event;

import com.katspow.caatja.core.Caatja;
import com.katspow.caatja.foundation.actor.Actor;

public class TouchInfo {
    
    public String identifier;
    public int clientX;
    public int pageX;
    public int clientY;
    public int pageY;
    public Actor target;
    public double time;

    /**
     * Constructor delegate.
     * @param id {number}
     * @param x {number}
     * @param y {number}
     * @param target {DOMElement}
     * @private
     */
    public TouchInfo(String id, int x, int y, Actor target) {
        this.identifier= id;
        this.clientX= x;
        this.pageX= x;
        this.clientY= y;
        this.pageY= y;
        this.target= target;
        this.time = Caatja.getTime();
    }

}
