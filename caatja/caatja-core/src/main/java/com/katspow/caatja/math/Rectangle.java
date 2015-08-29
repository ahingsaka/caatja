package com.katspow.caatja.math;

/**
 * Rectangle Class.
 * Needed to compute Curve bounding box.
 * Needed to compute Actor affected area on change.
 *
 **/

/**
 * A Rectangle implementation, which defines an area positioned somewhere.
 * 
 * @constructor
 */
public class Rectangle {

    /**
     * Rectangle x position.
     */
    public double x = 0;
    
    /**
     * Rectangle y position.
     */
    public double y = 0;
    
    /**
     * Rectangle x1 position.
     */
    public double x1 = 0;
    
    /**
     * Rectangle y1 position.
     */
    public double y1 = 0;
    
    /**
     * Rectangle width.
     */
    public double width = -1;
    
    /**
     * Rectangle height.
     */
    public double height = -1;
    
    public Rectangle() {
        this.setEmpty();
    }

    public Rectangle(Double x, Double y, Double w, Double h) {
        this.setLocation(x, y);
        this.setDimension(w, h);
    }
    
    public Rectangle setEmpty() {
        this.width=-1;
        this.height=-1;
        this.x=         0;
        this.y=         0;
        this.x1=        0;
        this.y1=        0;
        return this;
    }

    /**
     * Set this rectangle's location.
     * 
     * @param x
     *            {number}
     * @param y
     *            {number}
     */
    public Rectangle setLocation(double x, double y) {
        this.x = x;
        this.y = y;
        this.x1 = this.x + this.width;
        this.y1 = this.y + this.height;
        return this;
    }

    /**
     * Set this rectangle's dimension.
     * 
     * @param w
     *            {number}
     * @param h
     *            {number}
     */
    public Rectangle setDimension(double w, double h) {
        this.width = w;
        this.height = h;
        this.x1 = this.x + this.width;
        this.y1 = this.y + this.height;
        return this;
    }
    
    public Rectangle setBounds(double x, double y, double w, double h) {
        this.setLocation(x, y);
        this.setDimension(w, h);
        return this;
    }

    /**
     * Return whether the coordinate is inside this rectangle.
     * @param px {number}
     * @param py {number}
     *
     * @return {boolean}
     */
    public boolean contains(double px, double py) {
        //return px >= 0 && px < this.width && py >= 0 && py < this.height;
        return px>=this.x && px<this.x1 && py>=this.y && py<this.y1;
    }

    /**
     * Return whether this rectangle is empty, that is, has zero dimension.
     * @return {boolean}
     */
    public boolean isEmpty() {
        return this.width == -1 && this.height == -1;
    }

    /**
     * Set this rectangle as the union of this rectangle and the given point.
     * @param px {number}
     * @param py {number}
     */
    public void union(double px, double py) {

        if ( this.isEmpty() ) {
            this.x= px;
            this.x1= px;
            this.y= py;
            this.y1= py;
            this.width=0;
            this.height=0;
            return;
        }

        this.x1 = this.x + this.width;
        this.y1 = this.y + this.height;

        if (py < this.y) {
            this.y = py;
        }
        if (px < this.x) {
            this.x = px;
        }
        if (py > this.y1) {
            this.y1 = py;
        }
        if (px > this.x1) {
            this.x1 = px;
        }

        this.width = this.x1 - this.x;
        this.height = this.y1 - this.y;
    }

    public Rectangle unionRectangle(Rectangle rectangle) {
        this.union(rectangle.x, rectangle.y);
        this.union(rectangle.x1, rectangle.y);
        this.union(rectangle.x, rectangle.y1);
        this.union(rectangle.x1, rectangle.y1);
        return this;
    }
    
    public boolean intersects(Rectangle r) {
        if ( r.isEmpty() || this.isEmpty() ) {
            return false;
        }
        
        if ( r.x1<= this.x ) {
            return false;
        }
        if ( r.x >= this.x1 ) {
            return false;
        }
        if ( r.y1<= this.y ) {
            return false;
        }
        if ( r.y>= this.y1 ) {
            return false;
        }

        return true;
    }
    
    public boolean intersectsRect(double x, double y, int w, int h ) {
        if ( -1==w || -1==h ) {
            return false;
        }

        double x1= x+w-1;
        double y1= y+h-1;

        if ( x1< this.x ) {
            return false;
        }
        if ( x > this.x1 ) {
            return false;
        }
        if ( y1< this.y ) {
            return false;
        }
        if ( y> this.y1 ) {
            return false;
        }

        return true;
    }
    
    public Rectangle intersect(Rectangle i, Rectangle r ) {
        if (r == null) {
            r= new Rectangle();
        }

        r.x= Math.max( this.x, i.x );
        r.y= Math.max( this.y, i.y );
        r.x1=Math.min( this.x1, i.x1 );
        r.y1=Math.min( this.y1, i.y1 );
        r.width= r.x1-r.x;
        r.height=r.y1-r.y;

        return r;
    }
}
