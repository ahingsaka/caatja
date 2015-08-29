package com.katspow.caatja.math;

/**
 * Hold a 2D point information.
 * Think about the possibility of turning CAAT.Point into {x:,y:}.
 *
 **/
public class Pt {

    /**
     * point x coordinate.
     */
    public double x;
    
    /**
     * point y coordinate.
     */
    public double y;
    
    /**
     * point z coordinate.
     */
    public double z;

    public Pt() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    /**
    *
    * A point defined by two coordinates.
    *
    * @param xpos {number}
    * @param ypos {number}
    *
    * @constructor
    */
    public Pt(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public Pt(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Sets this point coordinates.
     * @param x {number}
     * @param y {number}
     *
     * @return this
     */
    public Pt set(double x, double y) {
        this.x = x;
        this.y = y;
        return this;
    }
    
    /**
     * Sets this point coordinates.
     * @param x {number}
     * @param y {number}
     * @param z {number=}
     *
     * @return this
     */
    public Pt set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    /**
     * Create a new CAAT.Point equal to this one.
     * @return {CAAT.Point}
     */
    public Pt clone() {
        Pt p = new Pt(this.x, this.y, this.z);
        return p;
    }

    /**
     * Translate this point to another position. The final point will be (point.x+x, point.y+y)
     * @param x {number}
     * @param y {number}
     *
     * @return this
     */
    public Pt translate(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;

        return this;
    }
    
    // Add by me
    public Pt translate(double x, double y) {
        this.x += x;
        this.y += y;
        return this;
    }

    /**
     * Translate this point to another point.
     * @param aPoint {CAAT.Point}
     * @return this
     */
    public Pt translatePoint(Pt aPoint) {
        this.x += aPoint.x;
        this.y += aPoint.y;
        this.z += aPoint.z;
        return this;
    }

    /**
     * Substract a point from this one.
     * @param aPoint {CAAT.Point}
     * @return this
     */
    public Pt subtract(Pt aPoint) {
        this.x -= aPoint.x;
        this.y -= aPoint.y;
        this.z -= aPoint.z;
        return this;
    }

    /**
     * Multiply this point by a scalar.
     * @param factor {number}
     * @return this
     */
    public Pt multiply(double factor) {
        this.x *= factor;
        this.y *= factor;
        this.z *= factor;
        return this;
    }

    /**
     * Rotate this point by an angle. The rotation is held by (0,0) coordinate as center.
     * @param angle {number}
     * @return this
     */
    public Pt rotate(double angle) {
        double x = this.x, y = this.y;
        this.x = x * Math.cos(angle) - Math.sin(angle) * y;
        this.y = x * Math.sin(angle) + Math.cos(angle) * y;
        this.z = 0;
        return this;
    }

    /**
    *
    * @param angle {number}
    * @return this
    */
    public Pt setAngle(double angle) {
        double len = this.getLength();
        this.x = Math.cos(angle) * len;
        this.y = Math.sin(angle) * len;
        this.z = 0;
        return this;
    }

    /**
    *
    * @param length {number}
    * @return this
    */
    public Pt setLength(double length) {
        Double len = this.getLength();
        if (len != null && len > 0)
            this.multiply(length / len);
        else
            this.x = this.y = this.z = length;
        return this;
    }

    /**
     * Normalize this point, that is, both set coordinates proportionally to values raning 0..1
     * @return this
     */
    public Pt normalize() {
        double len = this.getLength();
        this.x /= len;
        this.y /= len;
        this.z /= len;
        return this;
    }

    /**
     * Return the angle from -Pi to Pi of this point.
     * @return {number}
     */
    public double getAngle() {
        return Math.atan2(this.y, this.x);
    }

    /**
     * Set this point coordinates proportinally to a maximum value.
     * @param max {number}
     * @return this
     */
    public void limit(double max) {
        double aLenthSquared = this.getLengthSquared();
        if (aLenthSquared + 0.01 > max * max) {
            double aLength = Math.sqrt(aLenthSquared);
            this.x = (this.x / aLength) * max;
            this.y = (this.y / aLength) * max;
            this.z = (this.z/aLength) * max;
        }
    }

    /**
     * Get this point's lenght.
     * @return {number}
     */
    public Double getLength() {
        double length = Math.sqrt(this.x*this.x + this.y*this.y + this.z*this.z);
        if (length < 0.005 && length > -0.005)
            return 0.000001;
        return length;

    }

    /**
     * Get this point's squared length.
     * @return {number}
     */
    public double getLengthSquared() {
        double lengthSquared = this.x*this.x + this.y*this.y + this.z*this.z;
        if (lengthSquared < 0.005 && lengthSquared > -0.005)
            return 0;
        return lengthSquared;
    }

    /**
     * Get the distance between two points.
     * @param point {CAAT.Point}
     * @return {number}
     */
    public double getDistance(Pt point) {
        double deltaX = this.x - point.x;
        double deltaY = this.y - point.y;
        double deltaZ = this.z - point.z;
        return Math.sqrt( deltaX*deltaX + deltaY*deltaY + deltaZ*deltaZ );
    }

    /**
     * Get the squared distance between two points.
     * @param point {CAAT.Point}
     * @return {number}
     */
    public double getDistanceSquared(Pt point) {
        double deltaX = this.x - point.x;
        double deltaY = this.y - point.y;
        double deltaZ = this.z - point.z;
        return deltaX*deltaX + deltaY*deltaY + deltaZ*deltaZ;
    }

    /**
     * Get a string representation.
     * @return {string}
     */
    public String toString() {
        return "(CAAT.Point)" +
                " x:" + (Math.round(Math.floor(this.x*10))/10) +
                " y:" + (Math.round(Math.floor(this.y*10))/10) +
                " z:" + (Math.round(Math.floor(this.z*10))/10);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(z);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Pt other = (Pt) obj;
        if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
            return false;
        if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
            return false;
        if (Double.doubleToLongBits(z) != Double.doubleToLongBits(other.z))
            return false;
        return true;
    }

}
