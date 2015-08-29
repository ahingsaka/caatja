package com.katspow.caatja;


public class KeyModifiers {
    
    public KeyModifiers() {
        
    }
    
    public KeyModifiers(boolean alt, boolean control, boolean shift) {
        super();
        this.alt = alt;
        this.control = control;
        this.shift = shift;
    }
    
    public boolean alt = false;
    public boolean control = false;
    public boolean shift = false;
    
}
