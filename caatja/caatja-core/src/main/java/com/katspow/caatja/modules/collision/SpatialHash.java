package com.katspow.caatja.modules.collision;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.katspow.caatja.math.Rectangle;

public class SpatialHash {
    
    /**
     * A collection ob objects to test collision among them.
     */
    Map<Integer, List<SpatialHashObject>> elements    =   null;

    /**
     * Space width
     */
    public int width       =   0;
    
    /**
     * Space height
     */
    public int height      =   0;

    /**
     * Rows to partition the space.
     */
    public int rows        =   0;
    
    /**
     * Columns to partition the space.
     */
    public int columns     =   0;

    List<Integer> xcache      =   null;
    List<Integer> ycache      =   null;
    int[][] xycache     =   null;

    public Rectangle rectangle   =   null;
    
    /**
     * Spare rectangle to hold temporary calculations.
     */
    public Rectangle r0          =   null;
    
    /**
     * Spare rectangle to hold temporary calculations.
     */
    public Rectangle r1          =   null;

    public SpatialHash initialize (int w, int h, int rows, int columns ) {

        int i, j;

        this.elements= new HashMap<Integer, List<SpatialHashObject>>();
        for( i=0; i<rows*columns; i++ ) {
            this.elements.put(i, new ArrayList<SpatialHashObject>());
        }

        this.width=     w;
        this.height=    h;

        this.rows=      rows;
        this.columns=   columns;

        this.xcache= new ArrayList<Integer>();
        for( i=0; i<w; i++ ) {
            this.xcache.add( (i/(w/columns))>>0 );
        }

        this.ycache= new ArrayList<Integer>();
        for( i=0; i<h; i++ ) {
            this.ycache.add( (i/(h/rows))>>0 );
        }

        this.xycache= new int[rows][columns];
        
        for( i=0; i<this.rows; i++ ) {

            this.xycache[i] = new int[columns];
            for( j=0; j<this.columns; j++ ) {
                // TODO Check if it works
                this.xycache[i][j] =  j + i*columns;
            }
        }

        this.rectangle= new Rectangle().setBounds( 0, 0, w, h );
        this.r0=        new Rectangle();
        this.r1=        new Rectangle();

        return this;
    }

    public SpatialHash clearObject () {
        int i;

        for( i=0; i<this.rows*this.columns; i++ ) {
            this.elements.put(i, new ArrayList<SpatialHashObject>());
        }

        return this;
    }
    
    /**
     * Add an element of the form { id, x,y,width,height, rectangular }
     */
    public void addObject (SpatialHashObject obj  ) {
        int x= obj.x|0;
        int y= obj.y|0;
        int width= obj.width|0;
        int height= obj.height|0;

        List<Integer> cells= this.__getCells( x,y,width,height );
        for( int i=0; i<cells.size(); i++ ) {
            this.elements.get(cells.get(i)).add( obj );
        }
    }

    public List<Integer> __getCells (int x, int y,int width,int height ) {

        List<Integer> cells=new ArrayList<Integer>();
        int i;

        if ( this.rectangle.contains(x,y) ) {
            cells.add( this.xycache[ this.ycache.get(y) ][ this.xcache.get(x) ] );
        }

        /**
         * if both squares lay inside the same cell, it is not crossing a boundary.
         */
        if ( this.rectangle.contains(x+width-1,y+height-1) ) {
            int c= this.xycache[ this.ycache.get(y+height-1) ][ this.xcache.get(x+width-1) ];
            if ( c==cells.get(0) ) {
                return cells;
            }
            cells.add( c );
        }

        /**
         * the other two AABB points lie inside the screen as well.
         */
        if ( this.rectangle.contains(x+width-1,y) ) {
            int c= this.xycache[ this.ycache.get(y) ][ this.xcache.get(x+width-1) ];
            if ( c==cells.get(0) || c==cells.get(1) ) {
                return cells;
            }
            cells.add(c);
        }

        // worst case, touching 4 screen cells.
        if ( this.rectangle.contains(x+width-1,y+height-1) ) {
            int c= this.xycache[ this.ycache.get(y+height-1) ][ this.xcache.get(x) ];
            cells.add(c);
        }

        return cells;
    }
    
    public void solveCollision(SpatialHashCallbackTwoParameters callback ) {
        int i,j,k;

        for( i=0; i<this.elements.size(); i++ ) {
            List<SpatialHashObject> cell= this.elements.get(i);

            if ( cell.size()>1 ) {  // at least 2 elements could collide
                this._solveCollisionCell( cell, callback );
            }
        }
    }

    public void _solveCollisionCell(List<SpatialHashObject> cell, SpatialHashCallbackTwoParameters callback ) {
        int i,j;

        for( i=0; i<cell.size(); i++ ) {

            SpatialHashObject pivot= cell.get(i);
            this.r0.setBounds( pivot.x, pivot.y, pivot.width, pivot.height );

            for( j=i+1; j<cell.size(); j++ ) {
                SpatialHashObject c= cell.get(j);

                if ( this.r0.intersects( this.r1.setBounds( c.x, c.y, c.width, c.height ) ) ) {
                    callback.call( pivot, c );
                }
            }
        }
    }

    /**
     *
     * @param x
     * @param y
     * @param w
     * @param h
     * @param oncollide function that returns boolean. if returns true, stop testing collision.
     */
    public void collide (int x, int y, int w,int h, SpatialHashCallback oncollide ) {
        x|=0;
        y|=0;
        w|=0;
        h|=0;

        List<Integer> cells = this.__getCells( x,y,w,h );
        int i,j,l;
        Map<Integer, List<SpatialHashObject>> el = this.elements;

        this.r0.setBounds( x,y,w,h );

        for( i=0; i<cells.size(); i++ ) {
            int cell= cells.get(i);

            List<SpatialHashObject> elcell = el.get(cell);
            for( j=0, l=elcell.size(); j<l; j++ ) {
                SpatialHashObject obj = elcell.get(j);

                this.r1.setBounds( obj.x, obj.y, obj.width, obj.height );

                // collides
                if ( this.r0.intersects( this.r1 ) ) {
                    if ( oncollide.call(obj) ) {
                        return;
                    }
                }
            }
        }
    }

}
