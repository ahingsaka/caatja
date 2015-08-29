package com.katspow.caatja.event;

import com.katspow.caatja.foundation.actor.Actor;

public class TouchInfoData {
    public TouchInfoData(Actor actor, TouchInfo touch) {
        this.actor = actor;
        this.touch = touch;
    }
    public Actor actor;
    public TouchInfo touch;
}
