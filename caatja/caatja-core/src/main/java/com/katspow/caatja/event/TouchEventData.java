package com.katspow.caatja.event;

import com.katspow.caatja.foundation.actor.Actor;

public class TouchEventData {
    public TouchEventData(Actor actor, CAATTouchEvent touch) {
        this.actor = actor;
        this.touch = touch;
    }
    public Actor actor;
    public CAATTouchEvent touch;
}
