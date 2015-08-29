package com.katspow.caatja.core.event;

import com.katspow.caatja.core.canvas.CaatjaCanvas;
import com.katspow.caatja.foundation.Director;

public interface CaatjaEventManager {
    
    void addMouseHandlers(Director director, CaatjaCanvas canvas);
    
    void addTouchHandlers(Director director, CaatjaCanvas canvas);

    void addMultiTouchHandlers(Director director, CaatjaCanvas canvas);
    
}
