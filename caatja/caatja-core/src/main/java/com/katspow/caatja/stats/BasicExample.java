package com.katspow.caatja.stats;

//import com.google.gwt.user.client.DOM;
//import com.google.gwt.user.client.Timer;
//import com.google.gwt.user.client.ui.RootPanel;
import com.katspow.caatja.core.Caatja;
import com.katspow.caatja.core.canvas.CaatjaCanvas;
import com.katspow.caatja.core.canvas.CaatjaContext2d;

public class BasicExample {
    
    public void init() {
        
        final Stats stats = new Stats();
        stats.setMode(1);
        
//        DOM.appendChild(RootPanel.getBodyElement(), stats.domElement);
        
        CaatjaCanvas canvas = Caatja.createCanvas();;
        canvas.setCoordinateSpaceWidth(512);
        canvas.setCoordinateSpaceHeight(512);
        
        Caatja.addCanvas(canvas);
        
        final CaatjaContext2d context = canvas.getContext2d();
        
        context.setFillStyle("rgba(127,0,255,0.05)");
        
//        Timer t = new Timer() {
//            @Override
//            public void run() {
//                double time = Caatja.getTime() * 0.001;
//
//                context.clearRect( 0, 0, 512, 512 );
//
//                stats.begin();
//
//                for ( int i = 0; i < 2000; i ++ ) {
//
//                    double x = Math.cos( time + i * 0.01 ) * 196 + 256;
//                    double y = Math.sin( time + i * 0.01234 ) * 196 + 256;
//
//                    context.beginPath();
//                    context.arc( x, y, 10, 0, Math.PI * 2, true );
//                    context.fill();
//
//                }
//
//                stats.end();
//                
//            }
//        };
//        
//        t.scheduleRepeating(60);
        
    }

}
