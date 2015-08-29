package com.katspow.caatja.behavior.listener;

import com.katspow.caatja.behavior.BaseBehavior;
import com.katspow.caatja.behavior.SetForTimeReturnValue;
import com.katspow.caatja.foundation.actor.Actor;

public interface BehaviorAppliedListener {

    void onApplied(BaseBehavior behavior, double time, double normalizeTime, Actor actor,
            SetForTimeReturnValue value) throws Exception;

}
