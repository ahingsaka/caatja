package com.katspow.caatja.foundation.ui.layout;

import java.util.ArrayList;
import java.util.List;

import com.katspow.caatja.core.CAAT;
import com.katspow.caatja.foundation.actor.Actor;
import com.katspow.caatja.foundation.actor.ActorContainer;
import com.katspow.caatja.math.Dimension;

/**
*
* Layouts a container children in equal sized cells organized in rows by columns.
*
* @param rows {number=} number of initial rows, defaults to 2.
* @param columns {number=} number of initial columns, defaults to 2.
* @return {*}
* @constructor
*/
public class GridLayout extends LayoutManager {
    
    /**
     * @name GridLayout
     * @memberOf CAAT.Foundation.UI.Layout
     * @extends CAAT.Foundation.UI.Layout.LayoutManager
     * @constructor
     */
    public GridLayout(int rows, int columns) {
        super();
        this.rows= rows;
        this.columns= columns;
    }
    
    /**
     * Layout elements using this number of rows.
     */
    public int rows    = 0;
    
    /**
     * Layout elements using this number of columns.
     */
    public int columns = 2;
    
    public void doLayout (ActorContainer container ) {
        
        List<Actor> actors= new ArrayList<Actor>();

        for( int i=0; i<container.getNumChildren(); i++ ) {
            Actor child= container.getChildAt(i);
            if (!child.preventLayout && child.isVisible() && child.isInAnimationFrame( CAAT.getCurrentSceneTime()) ) {
                actors.add(child);
            }
        }

        int nactors = actors.size();
        
        if (nactors == 0) {
            return;
        }

        int nrows = this.rows;
        int ncols = this.columns;

        if (nrows > 0) {
            ncols = (int) Math.floor( (nactors + nrows - 1) / nrows );
        } else {
            nrows = (int) Math.floor( (nactors + ncols - 1) / ncols );
        }

        double totalGapsWidth = (ncols - 1) * this.hgap;
        double widthWOInsets = container.width - (this.padding.left + this.padding.right);
        double widthOnComponent = Math.floor( (widthWOInsets - totalGapsWidth) / ncols );
        double extraWidthAvailable = Math.floor( (widthWOInsets - (widthOnComponent * ncols + totalGapsWidth)) / 2 );

        double totalGapsHeight = (nrows - 1) * this.vgap;
        double heightWOInsets = container.height - (this.padding.top + this.padding.bottom);
        double heightOnComponent = Math.floor( (heightWOInsets - totalGapsHeight) / nrows );
        double extraHeightAvailable = Math.floor( (heightWOInsets - (heightOnComponent * nrows + totalGapsHeight)) / 2 );

        for (double c = 0, x = this.padding.left + extraWidthAvailable; c < ncols ; c++, x += widthOnComponent + this.hgap) {
            for (double r = 0, y = this.padding.top + extraHeightAvailable; r < nrows ; r++, y += heightOnComponent + this.vgap) {
                int i = (int) (r * ncols + c);
                if (i < actors.size()) {
                    Actor child= actors.get(i);
                    if ( !child.preventLayout && child.isVisible() && child.isInAnimationFrame( CAAT.getCurrentSceneTime() ) ) {
                    if ( !this.animated ) {
                        child.setBounds(
                                x + (widthOnComponent-child.width)/2,
                                y,
                                widthOnComponent,
                                heightOnComponent);
                    } else {
                        if (child.width != widthOnComponent || child.height != heightOnComponent) {
                            child.setSize(widthOnComponent, heightOnComponent);
                            if (this.newChildren.indexOf(child) != -1) {
                                child.setPosition(
                                        x + (widthOnComponent-child.width)/2,
                                        y );
                                child.setScale(0.01, 0.01);
                                child.scaleTo(1d, 1d, 500d, 0d, .5, .5, LayoutManager.newElementInterpolator);
                            } else {
                                child.moveTo(
                                        x + (widthOnComponent-child.width)/2,
                                        y,
                                        500d,
                                        0d,
                                        this.moveElementInterpolator );
                            }
                        }
                    }
                    }
                }
            }
        }

        super.doLayout(container);
    }

    public Dimension getMinimumLayoutSize (ActorContainer container ) {
        int nrows = this.rows;
        int ncols = this.columns;
        int nchildren= container.getNumChildren();
        double w=0, h=0;
        int i;

        if (nrows > 0) {
            ncols = (int) Math.ceil( (nchildren + nrows - 1) / nrows );
        } else {
            nrows = (int) Math.ceil( (nchildren + ncols - 1) / ncols );
        }

        for ( i= 0; i < nchildren; i+=1 ) {
            Actor actor= container.getChildAt(i);
            if ( !actor.preventLayout &&  actor.isVisible() && actor.isInAnimationFrame( CAAT.getCurrentSceneTime() ) ) {
                Dimension d = actor.getMinimumSize();
                if (w < d.width) {
                    w = d.width;
                }
                if (h < d.height) {
                    h = d.height;
                }
            }
        }

        return new Dimension(
            this.padding.left + this.padding.right + ncols * w + (ncols - 1) * this.hgap,
            this.padding.top + this.padding.bottom + nrows * h + (nrows - 1) * this.vgap
        );
    }

    public Dimension getPreferredLayoutSize (ActorContainer container ) {

        int nrows = this.rows;
        int ncols = this.columns;
        int nchildren= container.getNumChildren();
        double w=0, h=0; 
        int i;

        if (nrows > 0) {
            ncols = (int) Math.ceil( (nchildren + nrows - 1) / nrows );
        } else {
            nrows = (int) Math.ceil( (nchildren + ncols - 1) / ncols );
        }

        for ( i= 0; i < nchildren; i+=1 ) {
            Actor actor= container.getChildAt(i);
            if ( !actor.preventLayout && actor.isVisible() && actor.isInAnimationFrame( CAAT.getCurrentSceneTime() ) ) {
                Dimension d = actor.getPreferredSize();
                if (w < d.width) {
                    w = d.width;
                }
                if (h < d.height) {
                    h = d.height;
                }
            }
        }

        return new Dimension(
            this.padding.left + this.padding.right + ncols * w + (ncols - 1) * this.hgap,
            this.padding.top + this.padding.bottom + nrows * h + (nrows - 1) * this.vgap
        );
    }

}
