package com.katspow.caatja.behavior.listener;

import com.katspow.caatja.behavior.BaseBehavior;
import com.katspow.caatja.foundation.actor.Actor;

public interface BehaviorStartedListener {
    
    void onStarted(BaseBehavior behavior, double time, Actor actor);

}
