package com.katspow.caatja.foundation.actor;

import com.katspow.caatja.behavior.BaseBehavior;
import com.katspow.caatja.foundation.Director;

public class CSSActor extends ActorContainer {
    
    /**
     * This class aims to instrument Dom elements as if were Canvas elements.
     * <p>
     * It will create and add a div elemento to the dom which will be transformed and presented by CSS.
     * The parent/child relationship inherent to every CAAT animable element will be held by containing
     * div elements inside of other div elements.
     *
     * <p>
     * Experimental form.
     * 
     * FIXME ???
     *
     * @constructor
     * @extends CAAT.ActorContainer
     */
    public CSSActor() {
        super();
        this.setFillStyle(null);
        this.setStringStrokeStyle(null);
//        this.DOMParent = RootPanel.getBodyElement();
    }
    
//    Element domElement = null;

    public boolean dirty = true;
    public double oldX = -1;
    public double oldY = -1;
//    public Element DOMParent;

    /**
     * Set CSS div's inner HTML.
     * @param innerHTML {string} a valid html block.
     * @return this;
     */
    public CSSActor setInnerHTML(String innerHTML) {
//        this.domElement.setInnerHTML(innerHTML);
        return this;
    }

    public CSSActor create () {
//        this.domElement= DOM.createElement("div");
//        this.DOMParent.appendChild(this.domElement);
//        
//        DOM.setStyleAttribute(this.domElement, "position", "absolute");
//        DOM.setStyleAttribute(this.domElement, "webkitTransition", "all 0s linear");
        
        return this;
    }
    
//    public CSSActor setDOMParent(Element dom) {
//        this.DOMParent= dom;
//        return this;
//    }
    
    @Override
    public CSSActor setLocation (double x, double y ) {
        super.setLocation(x, y);
//        DOM.setStyleAttribute(this.domElement, "left", x + "px");
//        DOM.setStyleAttribute(this.domElement, "top", y + "px");
        return this;
    }
    
    @Override
    public CSSActor setSize (double w, double h ) {
        super.setSize(w, h);
//        DOM.setStyleAttribute(this.domElement, "width", ""+w+"px");
//        DOM.setStyleAttribute(this.domElement, "height", ""+h+"px");
        return this;
    }
    
    @Override
    public CSSActor setBounds (double x,double y,double w,double h ) {
        this.setLocation(x,y);
        this.setSize(w,h);
        return this;
    }
    
    public CSSActor setBackground (String backgroundImage_Local_URL ) {
//        DOM.setStyleAttribute(this.domElement, "background", "url("+backgroundImage_Local_URL+")");
        return this;
    }
    
    public void setOpacity () {
//        DOM.setStyleAttribute(this.domElement, "filter", "alpha(opacity="+((int)(this.alpha*100)>>0)+")");
//        DOM.setStyleAttribute(this.domElement, "-moz-opacity", String.valueOf(this.alpha));
//        DOM.setStyleAttribute(this.domElement, "-khtml-opacity", String.valueOf(this.alpha));
//        DOM.setStyleAttribute(this.domElement, "-opacity", String.valueOf(this.alpha));
    }
    
    @Override
    public CSSActor addChild (Actor actor ) throws Exception {
        if (actor instanceof CSSActor) {
            CSSActor cssActor = (CSSActor) actor;
//            this.domElement.appendChild(cssActor.domElement);
            return (CSSActor) super.addChild(actor);
        }

        return null;

    }
    
    @Override
    public boolean paintActor (Director director, double time) {
        if ( !this.isInAnimationFrame(time) ) {
            this.inFrame= false;
            return false;
        }

        if ( (this.oldX!=this.x) || (this.oldY!=this.y) ) {
//            DOM.setStyleAttribute(this.domElement, "top", this.y + "px");
//            DOM.setStyleAttribute(this.domElement, "left", this.x + "px");
            this.oldX= this.x;
            this.oldY= this.y;
        }

        if ( this.dirty ) {

            String strMatrix = "translate3d(0px)";
            if ( this.rotationAngle!=0 ) {
                strMatrix = strMatrix + " rotate(" + this.rotationAngle + "rad)";
            }
            if ( this.scaleX!=1 ) {
                strMatrix = strMatrix + " scale(" + this.scaleX + ")";
            }

//            DOM.setStyleAttribute(this.domElement, "-webkit-transform", strMatrix);
//            DOM.setStyleAttribute(this.domElement, "-o-transform", strMatrix);
//            DOM.setStyleAttribute(this.domElement, "-moz-transform", strMatrix);

            this.dirty= false;
        }

        for (Actor child : this.childrenList) {
            child.paintActor(director, time);
        }

        this.inFrame= true;

        return true;
    }

    @Override
    public void paint(Director director, double time) {
    }

    // Add by me
    @Override
    public CSSActor enableEvents(boolean enable) {
        return (CSSActor) super.enableEvents(enable);
    }

    @Override
    public CSSActor addBehavior(BaseBehavior behaviour) {
        return (CSSActor) super.addBehavior(behaviour);
    }
    
    

}
