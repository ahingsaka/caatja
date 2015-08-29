package com.katspow.caatja.behavior.listener;

import com.katspow.caatja.behavior.BaseBehavior;
import com.katspow.caatja.foundation.actor.Actor;

public interface BehaviorExpiredListener {

    void onExpired(BaseBehavior behavior, double time, Actor actor);

}
