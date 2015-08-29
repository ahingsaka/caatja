package com.katspow.caatja.foundation.actor;

import com.katspow.caatja.foundation.actor.Actor.EventType;

public interface ActorListener {
    
    public void actorLifeCycleEvent(Actor actor, EventType eventType, double time);

}
