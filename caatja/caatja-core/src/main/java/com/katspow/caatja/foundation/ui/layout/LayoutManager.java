package com.katspow.caatja.foundation.ui.layout;

import java.util.ArrayList;
import java.util.List;

import com.katspow.caatja.behavior.Interpolator;
import com.katspow.caatja.foundation.actor.Actor;
import com.katspow.caatja.foundation.actor.ActorContainer;
import com.katspow.caatja.math.Dimension;

public class LayoutManager {
    
    public LayoutManager() {
        this.newChildren= new ArrayList<Actor>();
        this.padding= new Padding();
    }
    
    /**
     * If animation enabled, new element interpolator.
     */
    public static Interpolator newElementInterpolator = Interpolator.createElasticOutInterpolator(1.1, .7, false);
    
    /**
     * If animation enabled, relayout elements interpolator.
     */
    public static Interpolator  moveElementInterpolator = Interpolator.createExponentialOutInterpolator(2, false);
    
    /**
     * Defines insets:
     * @type {{ left, right, top, botton }}
     */
    public Padding padding;
    
    /**
     * Needs relayout ??
     */
    public boolean invalid = true;

    /**
     * Horizontal gap between children.
     */
    public int hgap        = 2;
    
    /**
     * Vertical gap between children.
     */
    public int vgap        = 2;
    
    /**
     * Animate on adding/removing elements.
     */
    public boolean animated    = false;
    
    /**
     * pending to be laid-out actors.
     */
    public List<Actor> newChildren = null;
    
    public LayoutManager setAnimated (boolean animate ) {
        this.animated= animate;
        return this;
    }

    public LayoutManager setHGap (int gap ) {
        this.hgap= gap;
        this.invalidateLayout(null);
        return this;
    }

    public LayoutManager setVGap (int gap ) {
        this.vgap= gap;
        this.invalidateLayout(null);
        return this;
    }

    public LayoutManager setAllPadding (int s ) {
        this.padding.left= s;
        this.padding.right= s;
        this.padding.top= s;
        this.padding.bottom= s;
        this.invalidateLayout(null);
        return this;
    }

    public LayoutManager setPadding (int l,int r, int t,int b ) {
        this.padding.left= l;
        this.padding.right= r;
        this.padding.top= t;
        this.padding.bottom= b;
        this.invalidateLayout(null);
        return this;
    }

    public void addChild (Actor child, String constraint ) {
        this.newChildren.add( child );
    }

    public void removeChild (Actor child ) {

    }

    public void doLayout (ActorContainer container ) {
        this.newChildren= new ArrayList<Actor>();
        this.invalid= false;
    }

    public void invalidateLayout (ActorContainer container ) {
        this.invalid= true;
    }

    public Dimension getMinimumLayoutSize (ActorContainer container ) {
        return null;
    }

    public Dimension getPreferredLayoutSize (ActorContainer container ) {
        return null;
    }

    public boolean isValid () {
        return !this.invalid;
    }

    public boolean isInvalidated () {
        return this.invalid;
    }
    
}
