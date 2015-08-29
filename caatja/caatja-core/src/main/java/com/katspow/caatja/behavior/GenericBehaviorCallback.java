package com.katspow.caatja.behavior;

import com.katspow.caatja.foundation.actor.Actor;

public interface GenericBehaviorCallback {
    
    SetForTimeReturnValue call(double time, Actor target, Actor actor);

}
