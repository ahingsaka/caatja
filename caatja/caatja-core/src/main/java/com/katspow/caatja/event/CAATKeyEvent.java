package com.katspow.caatja.event;

import com.katspow.caatja.KeyModifiers;

/**
 * Define a key event.
 * @constructor
 * @param keyCode
 * @param up_or_down
 * @param modifiers
 * @param originalEvent
 */
public class CAATKeyEvent {
    
    public int keyCode;
    private KeyModifiers modifiers;
//    private KeyCodeEvent sourceEvent;
    public String action;

    /**
     * Define a key event.
     * @param keyCode
     * @param up_or_down
     * @param modifiers
     * @param originalEvent
     */
    public CAATKeyEvent(int keyCode, String up_or_down, KeyModifiers modifiers) {
        this.keyCode= keyCode;
        this.action=  up_or_down;
        this.modifiers= modifiers;
//        this.sourceEvent= originalEvent;
    }
    
//    public void preventDefault() {
//        this.sourceEvent.preventDefault();
//    }
    
    public int getKeyCode() {
        return this.keyCode;
    }

    public String getAction() {
        return this.action;
    }

    public KeyModifiers modifiers() {
        return this.modifiers;
    }

    public boolean isShiftPressed() {
        return this.modifiers.shift;
    }

    public boolean isControlPressed() {
        return this.modifiers.control;
    }

    public boolean isAltPressed() {
        return this.modifiers.alt;
    }

//    public KeyCodeEvent getSourceEvent() {
//        return this.sourceEvent;
//    }

}
