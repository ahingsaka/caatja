package com.katspow.caatja.foundation.timer;


public interface Callback {
    void call(double sceneTime, double timerTime, TimerTask timerTask);
}
