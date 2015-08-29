package com.katspow.caatja.foundation.ui.document;

import com.katspow.caatja.core.canvas.CaatjaContext2d;
import com.katspow.caatja.foundation.ui.render.RenderContextStyle;

/**
 * Abstract document element.
 * The document contains a collection of DocumentElementText and DocumentElementImage.
 * @param anchor
 * @param style
 * @return {*}
 * @constructor
 */
public class DocumentElement {
    
    public DocumentElement(Object anchor, RenderContextStyle style ) {
        this.link= anchor;
        this.style= style;
    }
    
    public double x;
    public double y;
    public double width = 0;
    public double height = 0;

    public RenderContextStyle style = null;

    // TODO Change type
    public Object link    = null;

    public Object isLink () {
        return this.link;
    }

    public DocumentElement setLink (Object link ) {
        this.link= link;
        return this;
    }

    public Object getLink () {
        return this.link;
    }

    public boolean contains (double x, double y) {
        return false;
    }
    
    // Add by me
    public void setYPosition (double baseline ) {
        
    }
    
    // TODO Check
    public double getHeight() {
        return height;
    }
    
    public void paint (CaatjaContext2d ctx ) {
        
    }
    
//    public DocumentElement() {
//        
//    }
//    
//    public DocumentElement(String text, double x,double y,double width,double height, RenderContextStyle style) {
//        this.x=         x;
//        this.y=         y;
//        this.width=     width;
//        this.height=    height;
//        this.text=      text;
//        this.style=     style;
//
//    }
//    
//    public double x       ;
//    public double y       ;
//    public String text    = null;
//    public RenderContextStyle style   = null;
//    public double width   = 0;
//    public double height  = 0;
//
//    public void paint (Context2d ctx ) {
//        this.style.text( ctx,this.text, this.x, this.y );
//    }
//
//    public int getHeight() {
//        return this.style.fontSize;
//    }

}
