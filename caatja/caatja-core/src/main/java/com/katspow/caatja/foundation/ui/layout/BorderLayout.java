package com.katspow.caatja.foundation.ui.layout;

import com.katspow.caatja.foundation.actor.Actor;
import com.katspow.caatja.foundation.actor.ActorContainer;
import com.katspow.caatja.math.Dimension;

public class BorderLayout extends LayoutManager {
    
    /**
     * @name BorderLayout
     * @memberOf CAAT.Foundation.UI.Layout
     * @extends CAAT.Foundation.UI.Layout.LayoutManager
     * @constructor
     */
    public BorderLayout() {
        super();
    }
    
    /**
     * An actor to position left.
     */
    public Actor left    = null;
    
    /**
     * An actor to position right.
     */
    public Actor right   = null;
    
    /**
     * An actor to position top.
     */
    public Actor top     = null;
    
    /**
     * An actor to position botton.
     */
    public Actor bottom  = null;
    
    /**
     * An actor to position center.
     */
    public Actor center  = null;
    
    public void addChild (Actor child, String constraint ) {
        super.addChild( child, constraint );

        if ( constraint .equals( "center") ) {
            this.center= child;
        } else if ( constraint.equals("left") ) {
            this.left= child;
        } else if ( constraint.equals("right") ) {
            this.right= child;
        } else if ( constraint.equals("top") ) {
            this.top= child;
        } else if ( constraint.equals("bottom") ) {
            this.bottom= child;
        }
    }

    public void removeChild (Actor child ) {
        if ( this.center.equals(child )) {
            this.center=null;
        } else if ( this.left.equals(child )) {
            this.left= null;
        } else if ( this.right.equals(child )) {
            this.right= null;
        } else if ( this.top.equals(child )) {
            this.top= null;
        } else if ( this.bottom.equals(child )) {
            this.bottom= null;
        }
    }

    public Actor __getChild (String constraint ) {
        if ( constraint.equals("center") ) {
            return this.center;
        } else if ( constraint.equals("left" )) {
            return this.left;
        } else if ( constraint.equals("right") ) {
            return this.right;
        } else if ( constraint.equals("top" )) {
            return this.top;
        } else if ( constraint.equals("bottom" )) {
            return this.bottom;
        }
        
        // Add by me
        else {
            return null;
        }
    }

    public Dimension getMinimumLayoutSize (ActorContainer container ) {
        Actor c;
        Dimension d;
        Dimension dim= new Dimension();

        if ((c=this.__getChild("right")) != null) {
            d = c.getMinimumSize();
            dim.width += d.width + this.hgap;
            dim.height = Math.max(d.height, dim.height);
        }
        if ((c=this.__getChild("left")) != null) {
            d = c.getMinimumSize();
            dim.width += d.width + this.hgap;
            dim.height = Math.max(d.height, dim.height);
        }
        if ((c=this.__getChild("center")) != null) {
            d = c.getMinimumSize();
            dim.width += d.width;
            dim.height = Math.max(d.height, dim.height);
        }
        if ((c=this.__getChild("top")) != null) {
            d = c.getMinimumSize();
            dim.width = Math.max(d.width, dim.width);
            dim.height += d.height + this.vgap;
        }
        if ((c=this.__getChild("bottom")) != null) {
            d = c.getMinimumSize();
            dim.width = Math.max(d.width, dim.width);
            dim.height += d.height + this.vgap;
        }

        dim.width += this.padding.left + this.padding.right;
        dim.height += this.padding.top + this.padding.bottom;

        return dim;
    }

    public Dimension getPreferredLayoutSize (ActorContainer container ) {
        Actor c;
        Dimension d;
        Dimension dim= new Dimension();

        if ((c=this.__getChild("left")) != null) {
            d = c.getPreferredSize();
            dim.width += d.width + this.hgap;
            dim.height = Math.max(d.height, dim.height);
        }
        if ((c=this.__getChild("right")) != null) {
            d = c.getPreferredSize();
            dim.width += d.width + this.hgap;
            dim.height = Math.max(d.height, dim.height);
        }
        if ((c=this.__getChild("center")) != null) {
            d = c.getPreferredSize();
            dim.width += d.width;
            dim.height = Math.max(d.height, dim.height);
        }
        if ((c=this.__getChild("top")) != null) {
            d = c.getPreferredSize();
            dim.width = Math.max(d.width, dim.width);
            dim.height += d.height + this.vgap;
        }
        if ((c=this.__getChild("bottom")) != null) {
            d = c.getPreferredSize();
            dim.width = Math.max(d.width, dim.width);
            dim.height += d.height + this.vgap;
        }

        dim.width += this.padding.left + this.padding.right;
        dim.height += this.padding.top + this.padding.bottom;

        return dim;
    }

    public void doLayout (ActorContainer container ) {

        double top = this.padding.top;
        double bottom = container.height - this.padding.bottom;
        double left = this.padding.left;
        double right = container.width - this.padding.right;
        Actor c;
        Dimension d;

        if ((c=this.__getChild("top")) != null) {
            c.setSize(right - left, c.height);
            d = c.getPreferredSize();
            c.setBounds(left, top, right - left, d.height);
            top += d.height + this.vgap;
        }
        if ((c=this.__getChild("bottom")) != null) {
            c.setSize(right - left, c.height);
            d = c.getPreferredSize();
            c.setBounds(left, bottom - d.height, right - left, d.height);
            bottom -= d.height + this.vgap;
        }
        if ((c=this.__getChild("right")) != null) {
            c.setSize(c.width, bottom - top);
            d = c.getPreferredSize();
            c.setBounds(right - d.width, top, d.width, bottom - top);
            right -= d.width + this.hgap;
        }
        if ((c=this.__getChild("left")) != null) {
            c.setSize(c.width, bottom - top);
            d = c.getPreferredSize();
            c.setBounds(left, top, d.width, bottom - top);
            left += d.width + this.hgap;
        }
        if ((c=this.__getChild("center")) != null) {
            c.setBounds(left, top, right - left, bottom - top);
        }

        super.doLayout(container);
    }

}
