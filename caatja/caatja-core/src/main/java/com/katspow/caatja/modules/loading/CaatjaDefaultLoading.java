package com.katspow.caatja.modules.loading;

import com.katspow.caatja.behavior.AlphaBehavior;
import com.katspow.caatja.foundation.actor.Actor;
import com.katspow.caatja.foundation.ui.ShapeActor;
import com.katspow.caatja.foundation.ui.ShapeActor.Shape;

public class CaatjaDefaultLoading implements CaatjaLoading {

    @Override
    public Actor getLoadingActor() {
        
        ShapeActor shapeActor = new ShapeActor();
        shapeActor.setShape(Shape.RECTANGLE);
        shapeActor.setPosition(5, 5);
        shapeActor.setAlpha(0);
        shapeActor.setSize(20, 20);
        shapeActor.setFillStyle("white");

        AlphaBehavior ab = new AlphaBehavior();
        ab.setValues(0, 1);
        ab.setFrameTime(0, 1000);
        ab.setPingPong();
        ab.setCycle(true);

        shapeActor.addBehavior(ab);

        return shapeActor;
    }

}
