package com.katspow.caatja.modules.circlemanager;

import java.util.ArrayList;
import java.util.List;

import com.katspow.caatja.math.Pt;
import com.katspow.caatja.math.Rectangle;

public class PackedCircleManager {
    
    public List<PackedCircle> allCircles;
    public int numberOfCollisionPasses=    1;
    public int numberOfTargetingPasses=    0;
    public Rectangle bounds=                     new Rectangle();
//    private boolean isFixed;
//    private double radius;
//    private double radiusSquared;
//    private Pt position;
//    private Pt targetPosition;
//    private Pt offset;
//    private int collisionMask;
//    private int collisionGroup;
    
    public PackedCircleManager() {
        this.allCircles = new ArrayList<PackedCircle>();
//        this.position = null;
//        this.offset = null;
//
//        this.targetPosition = null;
//
//        this.radius = 0;
//        this.radiusSquared = 0;
//
//        // Collision properties
//        this.isFixed = false;
//        this.collisionMask = 0;
//        this.collisionGroup = 0;
    }
    
    /**
     * Adds a circle to the simulation
     * @param aCircle
     */
    public PackedCircleManager addCircle(PackedCircle aCircle)
    {
        aCircle.id = this.allCircles.size();
        this.allCircles.add(aCircle);
        return this;
    }

    /**
     * Removes a circle from the simulations
     * @param aCircle   Circle to remove
     * @throws Exception 
     */
    public PackedCircleManager removeCircle(PackedCircle aCircle) throws Exception
    {
        int index = 0;
            boolean found = false;
            int len = this.allCircles.size();

        if(len == 0) {
            throw new Exception("Error: (PackedCircleManager) attempting to remove circle, and allCircles.length === 0!!");
        }

        while (len-- != 0) {
            if(this.allCircles.get(len).equals(aCircle)) {
                found = true;
                index = len;
                break;
            }
        }

        if(!found) {
            throw new Exception("Could not locate circle in allCircles array!");
        }

        // Remove
        this.allCircles.get(index).dealloc();
        
        // TODO Check ?
//        this.allCircles.get(index) = null;
        this.allCircles.remove(index);
        
        return this;
    }

    /**
     * Forces all circles to move to where their delegate position is
     * Assumes all targets have a 'position' property!
     */
    public void forceCirclesToMatchDelegatePositions()
    {
        int len = this.allCircles.size();

        // push toward target position
        for(int n = 0; n < len; n++)
        {
            PackedCircle aCircle = this.allCircles.get(n);
            if(aCircle == null || aCircle.delegate == null) {
                continue;
            }

            aCircle.position.set(aCircle.delegate.x + aCircle.offset.x,
                    aCircle.delegate.y + aCircle.offset.y);
        }
    }

    public void pushAllCirclesTowardTarget(Pt aTarget)
    {
        Pt v = new Pt(0,0,0);
            List<PackedCircle> circleList = this.allCircles;
            int len = circleList.size();

        // push toward target position
        for(int n = 0; n < this.numberOfTargetingPasses; n++)
        {
            for(int i = 0; i < len; i++)
            {
                PackedCircle c = circleList.get(i);

                if(c.isFixed) continue;

                v.x = c.position.x - (c.targetPosition.x+c.offset.x);
                v.y = c.position.y - (c.targetPosition.y+c.offset.y);
                v.multiply(c.targetChaseSpeed);

                c.position.x -= v.x;
                c.position.y -= v.y;
            }
        }
    }

    /**
     * Packs the circles towards the center of the bounds.
     * Each circle will have it's own 'targetPosition' later on
     */
    public void handleCollisions()
    {
        this.removeExpiredElements();

        Pt v = new Pt(0,0,0);
            List<PackedCircle> circleList = this.allCircles;
            int len = circleList.size();

        // Collide circles
        for(int n = 0; n < this.numberOfCollisionPasses; n++)
        {
            for(int i = 0; i < len; i++)
            {
                PackedCircle ci = circleList.get(i);

                for (int j = i + 1; j< len; j++)
                {
                    PackedCircle cj = circleList.get(j);

                    if( !this.circlesCanCollide(ci, cj) ) continue;   // It's us!


                    double dx = cj.position.x - ci.position.x,
                        dy = cj.position.y - ci.position.y;

                    // The distance between the two circles radii, but we're also gonna pad it a tiny bit
                    double r = (ci.radius + cj.radius) * 1.08,
                        d = ci.position.getDistanceSquared(cj.position);

                    /**
                     * Collision detected!
                     */
                    if (d < (r * r) - 0.02 )
                    {
                        v.x = dx;
                        v.y = dy;
                        v.normalize();

                        double inverseForce = (r - Math.sqrt(d)) * 0.5;
                        v.multiply(inverseForce);

                        // Move cj opposite of the collision as long as its not fixed
                        if(!cj.isFixed)
                        {
                            if(ci.isFixed) 
                                v.multiply(2.2); // Double inverse force to make up for the fact that the other object is fixed

                            // ADD the velocity
                            cj.position.translatePoint(v);
                            
                        }

                        // Move ci opposite of the collision as long as its not fixed
                        if(!ci.isFixed)
                        {
                            if(cj.isFixed) 
                                v.multiply(2.2); // Double inverse force to make up for the fact that the other object is fixed

                             // SUBTRACT the velocity
                            ci.position.subtract(v);
                            
                        }

                        // Emit the collision event from each circle, with itself as the first parameter
//                      if(this.dispatchCollisionEvents && n == this.numberOfCollisionPasses-1)
//                      {
//                          this.eventEmitter.emit('collision', cj, ci, v);
//                      }
                    }
                }
            }
        }
    }

    // FIXME fix this when it will be used 
    public void handleBoundaryForCircle(PackedCircle aCircle, int boundsRule)
    {
////      if(aCircle.boundsRule === true) return; // Ignore if being dragged
//
//        double xpos = aCircle.position.x;
//        double ypos = aCircle.position.y;
//
//        double radius = aCircle.radius;
//        double diameter = radius*2;
//
//        // Toggle these on and off,
//        // Wrap and bounce, are opposite behaviors so pick one or the other for each axis, or bad things will happen.
//        int wrapXMask = 1 << 0;
//        int wrapYMask = 1 << 2;
//        int constrainXMask = 1 << 3;
//        int constrainYMask = 1 << 4;
//        int emitEvent = 1 << 5;
//
//        // TODO: Promote to member variable
//        // Convert to bitmask - Uncomment the one you want, or concact your own :)
////      boundsRule = wrapY; // Wrap only Y axis
////      boundsRule = wrapX; // Wrap only X axis
////      boundsRule = wrapXMask | wrapYMask; // Wrap both X and Y axis
//        boundsRule = wrapYMask | constrainXMask;  // Wrap Y axis, but constrain horizontally
//
////      Wrap X
//        if(boundsRule & wrapXMask && xpos-diameter > this.bounds.right) {
//            aCircle.position.x = this.bounds.left + radius;
//        } else if(boundsRule & wrapXMask && xpos+diameter < this.bounds.left) {
//            aCircle.position.x = this.bounds.right - radius;
//        }
////      Wrap Y
//        if(boundsRule & wrapYMask && ypos-diameter > this.bounds.bottom) {
//            aCircle.position.y = this.bounds.top - radius;
//        } else if(boundsRule & wrapYMask && ypos+diameter < this.bounds.top) {
//            aCircle.position.y = this.bounds.bottom + radius;
//        }
//
////      Constrain X
//        if(boundsRule & constrainXMask && xpos+radius >= this.bounds.right) {
//            aCircle.position.x = aCircle.position.x = this.bounds.right-radius;
//        } else if(boundsRule & constrainXMask && xpos-radius < this.bounds.left) {
//            aCircle.position.x = this.bounds.left + radius;
//        }
//
////        Constrain Y
//        if(boundsRule & constrainYMask && ypos+radius > this.bounds.bottom) {
//            aCircle.position.y = this.bounds.bottom - radius;
//        } else if(boundsRule & constrainYMask && ypos-radius < this.bounds.top) {
//            aCircle.position.y = this.bounds.top + radius;
//        }
    }

    /**
     * Given an x,y position finds circle underneath and sets it to the currently grabbed circle
     * @param {Number} xpos     An x position
     * @param {Number} ypos     A y position
     * @param {Number} buffer   A radiusSquared around the point in question where something is considered to match
     */
    public PackedCircle getCircleAt(double xpos, double ypos, double buffer)
    {
        List<PackedCircle> circleList = this.allCircles;
        int len = circleList.size();
        Pt grabVector = new Pt(xpos, ypos, 0);

        // These are set every time a better match i found
        PackedCircle closestCircle = null;
        Double closestDistance = Double.MAX_VALUE;

        // Loop thru and find the closest match
        for(int i = 0; i < len; i++)
        {
            PackedCircle aCircle = circleList.get(i);
            if(aCircle == null) continue;
            double distanceSquared = aCircle.position.getDistanceSquared(grabVector);

            if(distanceSquared < closestDistance && distanceSquared < aCircle.radiusSquared + buffer)
            {
                closestDistance = distanceSquared;
                closestCircle = aCircle;
            }
        }

        return closestCircle;
    }

    public boolean circlesCanCollide(PackedCircle circleA,PackedCircle circleB)
    {
        if(circleA == null || circleB == null || circleA == circleB) return false;                   // one is null (will be deleted next loop), or both point to same obj.
//      if(circleA.delegate == null || circleB.delegate == null) return false;                  // This circle will be removed next loop, it's entity is already removed

//      if(circleA.isFixed & circleB.isFixed) return false;
//      if(circleA.delegate .clientID === circleB.delegate.clientID) return false;              // Don't let something collide with stuff it owns

        // They dont want to collide
//      if((circleA.collisionGroup & circleB.collisionMask) == 0) return false;
//      if((circleB.collisionGroup & circleA.collisionMask) == 0) return false;

        return true;
    }

    /**
     * Accessors
     */
    public void setBounds(int x, int y, int w, int h)
    {
        this.bounds.x = x;
        this.bounds.y = y;
        this.bounds.width = w;
        this.bounds.height = h;
    }

    public PackedCircleManager setNumberOfCollisionPasses(int value)
    {
        this.numberOfCollisionPasses = value;
        return this;
    }

    public PackedCircleManager setNumberOfTargetingPasses(int value)
    {
        this.numberOfTargetingPasses = value;
        return this;
    }

    /**
     * Helpers
     */
    public int sortOnDistanceToTarget(PackedCircle circleA, PackedCircle circleB)
    {
        int valueA = circleA.getDistanceSquaredFromPosition(circleA.targetPosition);
        int valueB = circleB.getDistanceSquaredFromPosition(circleA.targetPosition);
        int comparisonResult = 0;

        if(valueA > valueB) comparisonResult = -1;
        else if(valueA < valueB) comparisonResult = 1;

        return comparisonResult;
    }

    /**
     * Memory Management
     */

    public void removeExpiredElements()
    {
        // remove null elements check -1
        for (int k = this.allCircles.size() - 1; k >= 0; k--) {
            if (this.allCircles.get(k).equals(null))
                // TODO Check
                this.allCircles.remove(k);
        }
    }


}
