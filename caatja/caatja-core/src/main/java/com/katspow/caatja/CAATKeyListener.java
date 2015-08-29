package com.katspow.caatja;

import com.katspow.caatja.event.CAATKeyEvent;


public abstract class CAATKeyListener {
    
    // public abstract void call(int key, String action, KeyModifiers keyModifiers, KeyEvent event);
    
    public abstract void call(CAATKeyEvent keyEvent);
}
