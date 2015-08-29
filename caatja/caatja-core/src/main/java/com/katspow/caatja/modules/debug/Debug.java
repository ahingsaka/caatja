package com.katspow.caatja.modules.debug;

import com.katspow.caatja.core.CAAT;
import com.katspow.caatja.core.canvas.CaatjaCanvas;
import com.katspow.caatja.core.canvas.CaatjaContext2d;
import com.katspow.caatja.foundation.Framerate;
import com.katspow.caatja.foundation.Statistics;
//import com.google.gwt.dom.client.CanvasElement;
//import com.google.gwt.dom.client.Document;
//import com.google.gwt.dom.client.Element;
//import com.google.gwt.user.client.DOM;

/*
* Get realtime Debug information of CAAT's activity.
* Set CAAT.DEBUG=1 before any CAAT.Director object creation.
* This class creates a DOM node called 'caat-debug' and associated styles
* The debug panel is minimized by default and shows short information. It can be expanded and minimized again by clicking on it
* 
* FIXME Not up to date with source !
*
*/
public class Debug {

    public int width = 0;
    public int height = 0;
    public CaatjaCanvas canvas = null;
    public CaatjaContext2d ctx = null;
    public Statistics statistics = null;
    public Framerate framerate = null;
    
//    public Element textContainer=      null;
//    public Element textFPS=            null;
//    public Element textEntitiesTotal=  null;
//    public Element textEntitiesActive= null;
//    public Element textDraws=          null;
//    public Element textDrawTime=       null;
//    public Element textRAFTime=        null;

    public int frameTimeAcc =      0;
    public int frameRAFAcc =       0;

    public boolean canDebug=           false;

    public int SCALE = 60;
    
    private String debugTpl =  
        "<style type='text/css'>"+
            "#caat-debug {"+
                "z-index: 10000;"+
                "position:fixed; "+
                "bottom:0; "+
                "left:0; "+
                "width:100%;"+
                "background-color: rgba(0,0,0,0.8);"+
                "height: 120px;"+
                "margin-bottom: -104px;"+
            "}"+
            "#caat-debug.caat_debug_max {"+
                "margin-bottom: 0px;"+
            "}"+
            ".caat_debug_bullet {"+
                "display:inline-block;"+
                "background-color:#f00;"+
                "width:6px;"+
                "height:10px;"+
                "margin-left:4px;"+
                "margin-right:4px;"+
            "}"+
            ".caat_debug_description {"+
                "font-size:11px;"+
                "font-family: helvetica, arial;"+
                "color: #aaa;"+
                "display: inline-block;"+
            "}"+
            ".caat_debug_value {"+
                "font-size:11px;"+
                "font-family: helvetica, arial;"+
                "color: #fff;"+
                "width:25px;"+
                "text-align: right;"+
                "display: inline-block;"+
                "margin-right: .3em;"+
            "}"+
            ".caat_debug_menu {"+
                "font-family: helvetica, arial;"+
                "font-size: 12px;"+
                "font-weight: bold;"+
                "color: #888;"+
                "padding: 2px;"+
            "}"+
        "</style>"+
        "<div id='caat-debug' onCLick='javascript: var debug = document.getElementById(\'caat-debug\');if (debug.className == \'\') {debug.className = \'caat_debug_max\'} else {debug.className = \'\'}'>"+
            "<div style='width:100%;'>"+
                "<div class='caat_debug_menu'>CAAT Performance "+
                    "<span>"+
                        "<span class='caat_debug_bullet' style='background-color:#f00;'></span>"+
                        "<span class='caat_debug_value' id='textFPSShort'>48</span>"+
                    "</span>"+
                    "<span>"+
                        "<span class='caat_debug_bullet' style='background-color:#0f0;'></span>"+
                        "<span class='caat_debug_value' id='textDrawTimeShort'>5.46</span>"+
                        "<span class='caat_debug_description'>ms.</span>"+
                    "</span>"+
                    "<span>"+
                        "<span class='caat_debug_bullet' style='background-color:#0f0;'></span>"+
                        "<span class='caat_debug_value' id='textRAFTimeShort'>20.76</span>"+
                        "<span class='caat_debug_description'>ms.</span>"+
                    "</span>"+
                    "<span>"+
                        "<span class='caat_debug_bullet' style='background-color:#0ff;'></span>"+
                        "<span class='caat_debug_value' id='textEntitiesTotalShort'>41</span>"+
                    "</span>"+
                    "<span>"+
                        "<span class='caat_debug_bullet' style='background-color:#0ff;'></span>"+
                        "<span class='caat_debug_value' id='textEntitiesActiveShort'>37</span>"+
                    "</span>"+
                    "<span>"+
                        "<span class='caat_debug_bullet' style='background-color:#00f;'></span>"+
                        "<span class='caat_debug_value' id='textDrawsShort'>0</span>"+
                    "</span>"+
                "</div>"+
            "</div>"+
            "<div id='caat-debug-performance'>"+
                "<canvas id='caat-debug-canvas' height='60'></canvas>"+
                "<div>"+
                    "<span>"+
                        "<span class='caat_debug_bullet' style='background-color:#f00;'></span>"+
                        "<span class='caat_debug_description'>FPS: </span>"+
                        "<span class='caat_debug_value' id='textFPS'>48</span>"+
                    "</span>"+
                    "<span>"+
                        "<span class='caat_debug_bullet' style='background-color:#0f0;'></span>"+
                        "<span class='caat_debug_description'>Draw Time: </span>"+
                        "<span class='caat_debug_value' id='textDrawTime'>5.46</span>"+
                        "<span class='caat_debug_description'>ms.</span>"+
                    "</span>"+
                    "<span>"+
                        "<span class='caat_debug_bullet' style='background-color:#0f0;'></span>"+
                        "<span class='caat_debug_description'>RAF Time:</span>"+
                        "<span class='caat_debug_value' id='textRAFTime'>20.76</span>"+
                        "<span class='caat_debug_description'>ms.</span>"+
                    "</span>"+
                    "<span>"+
                        "<span class='caat_debug_bullet' style='background-color:#0ff;'></span>"+
                        "<span class='caat_debug_description'>Entities Total: </span>"+
                        "<span class='caat_debug_value' id='textEntitiesTotal'>41</span>"+
                    "</span>"+
                    "<span>"+
                        "<span class='caat_debug_bullet' style='background-color:#0ff;'></span>"+
                        "<span class='caat_debug_description'>Entities Active: </span>"+
                        "<span class='caat_debug_value' id='textEntitiesActive'>37</span>"+
                    "</span>"+
                    "<span>"+
                        "<span class='caat_debug_bullet' style='background-color:#00f;'></span>"+
                        "<span class='caat_debug_description'>Draws: </span>"+
                        "<span class='caat_debug_value' id='textDraws'>0</span>"+
                    "</span>"+
                "</div>"+
            "</div>"+
        "</div>";
    
    // Add by me
    private int scale;
//    private int size_total;
//    private int size_active;
//    private Element textFPSShort;
//    private Element textDrawTimeShort;
//    private Element textRAFTimeShort;
//    private Element textEntitiesTotalShort;
//    private Element textEntitiesActiveShort;
//    private Element textDrawsShort;
    
    public Debug setScale (int s) {
        this.scale= s;
        return this;
    }

    public Debug initialize(double w, double h) {
        
//        // TODO Check
////        w= window.innerWidth;
//        
//        this.width= (int) w;
//        this.height= (int) h;
//        
//        this.framerate = new Framerate();
//        
//        if (CAAT.FPS_REFRESH > 0) {
//            this.framerate.refreshInterval = CAAT.FPS_REFRESH;
//        } else {
//            this.framerate.refreshInterval = 500;
//        }
//        
//        this.framerate.frames = 0;
//        this.framerate.timeLastRefresh = 0;
//        this.framerate.fps = 0;
//        this.framerate.prevFps = -1;
//        this.framerate.fpsMin = 1000;
//        this.framerate.fpsMax = 0;
//        
//        Element debugContainer= Document.get().getElementById("caat-debug");
//        if (debugContainer != null) {
//            com.google.gwt.user.client.Element wrap = DOM.createDiv();
//            wrap.setInnerHTML(this.debugTpl);
//            Document.get().appendChild(wrap);
////            console.log(wrap);
//        }
//        
//        Element elementFound = Document.get().getElementById("caat-debug-canvas");
//        
//        if (elementFound == null) {
//            this.canDebug = false;
//            return null;
//        }
//        
//        this.canvas = CAAT.createCanvas((CanvasElement) elementFound);
////        this.canvas= Canvas.createIfSupported();
//        
//        this.canvas.setCoordinateSpaceWidth((int) w);
//        this.canvas.setCoordinateSpaceHeight((int) h);
//        this.ctx= this.canvas.getContext2d();
//
//        this.ctx.setFillStyle("#000");
//        this.ctx.fillRect(0,0,this.width,this.height);
//        
//        this.textFPS = Document.get().getElementById("textFPS");
//        this.textDrawTime = Document.get().getElementById("textDrawTime");
//        this.textRAFTime = Document.get().getElementById("textRAFTime");
//        this.textEntitiesTotal = Document.get().getElementById("textEntitiesTotal");
//        this.textEntitiesActive = Document.get().getElementById("textEntitiesActive");
//        this.textDraws = Document.get().getElementById("textDraws");
//        
//        this.textFPSShort= Document.get().getElementById("textFPSShort");
//        this.textDrawTimeShort= Document.get().getElementById("textDrawTimeShort");
//        this.textRAFTimeShort= Document.get().getElementById("textRAFTimeShort");
//        this.textEntitiesTotalShort= Document.get().getElementById("textEntitiesTotalShort");
//        this.textEntitiesActiveShort= Document.get().getElementById("textEntitiesActiveShort");
//        this.textDrawsShort= Document.get().getElementById("textDrawsShort");
//
//        this.canDebug= true;

        return this;
    }

    public void debugInfo (Statistics statistics) {
        
        this.statistics = statistics;
        
        this.frameTimeAcc+= CAAT.FRAME_TIME;
        this.frameRAFAcc+= CAAT.REQUEST_ANIMATION_FRAME_TIME;
        
        /* Update the framerate counter */
        this.framerate.frames++;
        if ( CAAT.RAF > this.framerate.timeLastRefresh + this.framerate.refreshInterval ) {
//            this.framerate.fps = (int) Math.round( ( this.framerate.frames * 1000 ) / ( CAAT.RAF - this.framerate.timeLastRefresh ) );
//            this.framerate.fpsMin = this.framerate.frames > 0 ? Math.min( this.framerate.fpsMin, this.framerate.fps ) : this.framerate.fpsMin;
//            this.framerate.fpsMax = Math.max( this.framerate.fpsMax, this.framerate.fps );
//
//            this.textFPS.setInnerHTML(this.framerate.fps + "");
//            this.textFPSShort.setInnerHTML(this.framerate.fps + "");
//            
//            double value= ((this.frameTimeAcc*100/this.framerate.frames)|0)/100;
//            this.frameTimeAcc=0;
//            this.textDrawTime.setInnerHTML(value + "");
//            this.textDrawTimeShort.setInnerHTML(value + "");
//
//            double value2= ((this.frameRAFAcc*100/this.framerate.frames)|0)/100;
//            this.frameRAFAcc=0;
//            this.textRAFTime.setInnerHTML(value2 + "");
//            this.textRAFTimeShort.setInnerHTML(value2 + "");
//            
//            this.framerate.timeLastRefresh = CAAT.RAF;
//            this.framerate.frames = 0;
            
//            this.paint(value2);
        }
        
//        this.textEntitiesTotal.setInnerHTML(this.statistics.size_total + "");
//        this.textEntitiesTotalShort.setInnerHTML(this.statistics.size_total + "");
//        
//        this.textEntitiesActive.setInnerHTML(this.statistics.size_active + "");
//        this.textEntitiesActiveShort.setInnerHTML(this.statistics.size_active + "");
//        
//        this.textDraws.setInnerHTML(this.statistics.draws + "");
//        this.textDrawsShort.setInnerHTML(this.statistics.draws + "");
        
    }
    
    double prevRAF = -1;

    public void paint (double rafValue) {
        CaatjaContext2d ctx= this.ctx;
        double t=0;

//        ctx.drawImage(
//            this.canvas.getCanvasElement(),
//            1, 0, this.width-1, this.height,
//            0, 0, this.width-1, this.height );

        ctx.setStrokeStyle("black");
        ctx.beginPath();
        ctx.moveTo( this.width-.5, 0 );
        ctx.lineTo( this.width-.5, this.height );
        ctx.stroke();

        ctx.setStrokeStyle("#a22");
        ctx.beginPath();
        t= this.height-((20/this.SCALE*this.height)>>0)-.5;
        ctx.moveTo( .5, t );
        ctx.lineTo( this.width + .5, t );
        ctx.stroke();

        ctx.setStrokeStyle("#aa2");
        ctx.beginPath();
        t= this.height-((30/this.SCALE*this.height)>>0)-.5;
        ctx.moveTo(.5, t);
        ctx.lineTo(this.width + .5, t);
        ctx.stroke();
        
        int fps = Math.min( this.height-(this.framerate.fps/this.SCALE*this.height), 60 );
        if (-1==this.framerate.prevFps) {
            this.framerate.prevFps= fps|0;
        }

        //ctx.setStrokeStyle(CAAT.FRAME_TIME < 16 ? "green" : CAAT.FRAME_TIME < 25 ? "yellow" : "red");
        ctx.setStrokeStyle("#0ff");
        ctx.beginPath();
        ctx.moveTo( this.width, (fps|0)-.5 );
        ctx.lineTo( this.width, this.framerate.prevFps-.5 );
        ctx.stroke();
        
        this.framerate.prevFps= fps;
        
        
        double t1=  ((int)(this.height-(rafValue/this.SCALE*this.height))>>0)-.5;
        ctx.setStrokeStyle("#ff0");
        ctx.beginPath();
        ctx.moveTo( this.width, t1 );
        ctx.lineTo( this.width, t1 );
        ctx.stroke();
        
//        this.prevRAF= t1;
//
//        ctx.setFillStyle("rgba(255,0,0,.75");
//        ctx.fillRect( 0,0,300,15);
//        ctx.setFillStyle("white");
//        ctx.fillText(
//                "  Total: "+this.statistics.size_total+
//                "  Active: "+this.statistics.size_active+
//                "  Draws: "+this.statistics.draws+
//                "  Framerate: "+this.framerate.fps+" (min:"+this.framerate.fpsMin+", max:"+this.framerate.fpsMax+")",
//                0,
//                12 );
    }

}
