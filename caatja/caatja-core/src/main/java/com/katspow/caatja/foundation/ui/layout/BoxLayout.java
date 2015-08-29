package com.katspow.caatja.foundation.ui.layout;

import com.katspow.caatja.core.CAAT;
import com.katspow.caatja.foundation.actor.Actor;
import com.katspow.caatja.foundation.actor.ActorContainer;
import com.katspow.caatja.math.Dimension;

public class BoxLayout extends LayoutManager {

    /**
     * @name BoxLayout
     * @memberOf CAAT.Foundation.UI.Layout
     * @extends CAAT.Foundation.UI.Layout.LayoutManager
     * @constructor
     */
    public BoxLayout() {
        super();
    }
    
    public enum AXIS {
        X (0),
        Y (1);
        
        private int val;

        private AXIS(int val) {
            this.val = val;
        }
        
        public int getVal() {
            return val;
        }
        
    }
    
    public enum ALIGNMENT {
        LEFT (  0),
        RIGHT(  1),
        CENTER( 2),
        TOP(    3),
        BOTTOM( 4);
        
        private int val;

        private ALIGNMENT(int val) {
            this.val = val;
        }
        
        public int getVal() {
            return val;
        }
        
    }
    
    /**
     * Stack elements in this axis.
     * @type {CAAT.Foundation.UI.Layout.LayoutManager}
     */
    AXIS axis    = AXIS.Y;
    
    /**
     * Vertical alignment.
     * @type {CAAT.Foundation.UI.Layout.LayoutManager.ALIGNMENT}
     */
    ALIGNMENT valign  = ALIGNMENT.CENTER;
    
    /**
     * Horizontal alignment.
     * @type {CAAT.Foundation.UI.Layout.LayoutManager.ALIGNMENT}
     */
    ALIGNMENT halign  = ALIGNMENT.CENTER;

    public BoxLayout setAxis (AXIS axis ) {
        this.axis= axis;
        this.invalidateLayout(null);
        return this;
    }

    public BoxLayout setHorizontalAlignment (ALIGNMENT align ) {
        this.halign= align;
        this.invalidateLayout(null);
        return this;
    }

    public BoxLayout setVerticalAlignment (ALIGNMENT align ) {
        this.valign= align;
        this.invalidateLayout(null);
        return this;
    }

    public void doLayout (ActorContainer container ) {

        if ( this.axis==AXIS.Y ) {
            this.doLayoutVertical( container );
        } else {
            this.doLayoutHorizontal( container );
        }

        super.doLayout(container);
    }

    public void doLayoutHorizontal (ActorContainer container ) {

        double computedW= 0, computedH=0;
        double yoffset= 0, xoffset;
        int i, l;
        Actor actor;

        // calculamos ancho y alto de los elementos.
        for( i= 0, l=container.getNumChildren(); i<l; i+=1 ) {

            actor= container.getChildAt(i);
            if (!actor.preventLayout && actor.isVisible() && actor.isInAnimationFrame( CAAT.getCurrentSceneTime() ) ) {
                if ( computedH < actor.height ) {
                    computedH= actor.height;
                }
    
                computedW += actor.width;
                if ( i>0 ) {
                    computedW+= this.hgap;
                }
            }
        }

        switch( this.halign ) {
            case LEFT:
                xoffset= this.padding.left;
                break;
            case RIGHT:
                xoffset= container.width - computedW - this.padding.right;
                break;
            default:
                xoffset= (container.width - computedW) / 2;
        }

        for( i= 0, l=container.getNumChildren(); i<l; i+=1 ) {
            actor= container.getChildAt(i);
            if (!actor.preventLayout && actor.isVisible() && actor.isInAnimationFrame( CAAT.getCurrentSceneTime() ) ) {
                switch( this.valign ) {
                    case TOP:
                        yoffset= this.padding.top;
                        break;
                    case BOTTOM:
                        yoffset= container.height - this.padding.bottom - actor.height;
                        break;
                    default:
                        yoffset= (container.height - actor.height) / 2;
                }
    
                this.__setActorPosition( actor, xoffset, yoffset );
    
                xoffset += actor.width + this.hgap;
            }
        }

    }

    public void __setActorPosition (Actor actor, double xoffset, double yoffset ) {
        if ( this.animated ) {
            if ( this.newChildren.indexOf( actor )!=-1 ) {
                actor.setPosition( xoffset, yoffset );
                actor.setScale(0,0);
                actor.scaleTo( 1d,1d, 500d, 0d,.5,.5, LayoutManager.newElementInterpolator );
            } else {
                actor.moveTo( xoffset, yoffset, 500d, 0d, LayoutManager.moveElementInterpolator );
            }
        } else {
            actor.setPosition( xoffset, yoffset );
        }
    }

    public void doLayoutVertical (ActorContainer container ) {

        double computedW= 0, computedH=0;
        double yoffset, xoffset;
        int i, l;
        Actor actor;

        // calculamos ancho y alto de los elementos.
        for( i= 0, l=container.getNumChildren(); i<l; i+=1 ) {

            actor= container.getChildAt(i);
            if (!actor.preventLayout && actor.isVisible() && actor.isInAnimationFrame( CAAT.getCurrentSceneTime() ) ) {
                if ( computedW < actor.width ) {
                    computedW= actor.width;
                }
    
                computedH += actor.height;
                if ( i>0 ) {
                    computedH+= this.vgap;
                }
            }
        }

        switch( this.valign ) {
            case TOP:
                yoffset= this.padding.top;
                break;
            case BOTTOM:
                yoffset= container.height - computedH - this.padding.bottom;
                break;
            default:
                yoffset= (container.height - computedH) / 2;
        }

        for( i= 0, l=container.getNumChildren(); i<l; i+=1 ) {
            actor= container.getChildAt(i);
            if (!actor.preventLayout && actor.isVisible() && actor.isInAnimationFrame( CAAT.getCurrentSceneTime() ) ) {
                switch( this.halign ) {
                    case LEFT:
                        xoffset= this.padding.left;
                        break;
                    case RIGHT:
                        xoffset= container.width - this.padding.right - actor.width;
                        break;
                    default:
                        xoffset= (container.width - actor.width) / 2;
                }
    
                this.__setActorPosition( actor, xoffset, yoffset );
    
                yoffset += actor.height + this.vgap;
            }
        }
    }

    public Dimension getPreferredLayoutSize (ActorContainer container ) {

        Dimension dim= new Dimension();
        double computedW= 0, computedH=0;
        int i, l;

        // calculamos ancho y alto de los elementos.
        for( i= 0, l=container.getNumChildren(); i<l; i+=1 ) {

            Actor actor= container.getChildAt(i);
            if (!actor.preventLayout && actor.isVisible() && actor.isInAnimationFrame( CAAT.getCurrentSceneTime() ) ) {
                Dimension ps= actor.getPreferredSize();
    
                if ( computedH < ps.height ) {
                    computedH= ps.height;
                }
                computedW += ps.width;
            }
        }

        dim.width= computedW;
        dim.height= computedH;

        return dim;
    }

    public Dimension getMinimumLayoutSize (ActorContainer container ) {
        Dimension dim= new Dimension();
        double computedW= 0, computedH=0;
        int i, l;

        // calculamos ancho y alto de los elementos.
        for( i= 0, l=container.getNumChildren(); i<l; i+=1 ) {

            Actor actor= container.getChildAt(i);
            if (!actor.preventLayout && actor.isVisible() && actor.isInAnimationFrame( CAAT.getCurrentSceneTime() ) ) {
                Dimension ps= actor.getMinimumSize();
    
                if ( computedH < ps.height ) {
                    computedH= ps.height;
                }
                
                computedW += ps.width;
            }
        }

        dim.width= computedW;
        dim.height= computedH;

        return dim;
    }
}
