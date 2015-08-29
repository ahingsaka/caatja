package com.katspow.caatja.behavior;

import com.katspow.caatja.behavior.listener.BehaviorAppliedListener;
import com.katspow.caatja.behavior.listener.BehaviorExpiredListener;
import com.katspow.caatja.behavior.listener.BehaviorStartedListener;

// Add by me
public class BehaviorListener {
    
    private BehaviorAppliedListener behaviorAppliedListener;
    private BehaviorExpiredListener behaviorExpiredListener;
    private BehaviorStartedListener behaviorStartedListener;
    
    public static BehaviorListener valueOf(BehaviorAppliedListener behaviorAppliedListener, BehaviorExpiredListener behaviorExpiredListener, BehaviorStartedListener behaviorStartedListener) {
        BehaviorListener behaviorListener = new BehaviorListener();
        behaviorListener.behaviorAppliedListener = behaviorAppliedListener;
        behaviorListener.behaviorExpiredListener = behaviorExpiredListener;
        behaviorListener.behaviorStartedListener = behaviorStartedListener;
        return behaviorListener;
    }
    
    public static BehaviorListener valueOfExpired(BehaviorExpiredListener behaviorExpiredListener) {
        BehaviorListener behaviorListener = new BehaviorListener();
        behaviorListener.behaviorExpiredListener = behaviorExpiredListener;
        return behaviorListener;
    }
    
    public static BehaviorListener valueOfExpiredAndApplied(BehaviorExpiredListener behaviorExpiredListener, BehaviorAppliedListener behaviorAppliedListener) {
        BehaviorListener behaviorListener = new BehaviorListener();
        behaviorListener.behaviorAppliedListener = behaviorAppliedListener;
        behaviorListener.behaviorExpiredListener = behaviorExpiredListener;
        return behaviorListener;
    }

    public BehaviorAppliedListener getBehaviorAppliedListener() {
        return behaviorAppliedListener;
    }

    public void setBehaviorAppliedListener(BehaviorAppliedListener behaviorAppliedListener) {
        this.behaviorAppliedListener = behaviorAppliedListener;
    }

    public BehaviorExpiredListener getBehaviorExpiredListener() {
        return behaviorExpiredListener;
    }

    public void setBehaviorExpiredListener(BehaviorExpiredListener behaviorExpiredListener) {
        this.behaviorExpiredListener = behaviorExpiredListener;
    }

    public BehaviorStartedListener getBehaviorStartedListener() {
        return behaviorStartedListener;
    }

    public void setBehaviorStartedListener(BehaviorStartedListener behaviorStartedListener) {
        this.behaviorStartedListener = behaviorStartedListener;
    }
    
//    void behaviorExpired(BaseBehavior behavior, double time, Actor actor);
//    
//    // TODO change value handling. 
//    void behaviorApplied(BaseBehavior behavior, double time, double normalizeTime, Actor actor, SetForTimeReturnValue value) throws Exception;
//
//    void behaviorStarted(BaseBehavior behavior, double time, Actor actor);

}
