package com.katspow.caatja.modules.circlemanager;

import com.katspow.caatja.foundation.actor.Actor;
import com.katspow.caatja.math.Pt;

/**
####  #####  ##### ####    ###  #   # ###### ###### ##     ##  #####  #     #      ########    ##    #  #  #####
#   # #   #  ###   #   #  #####  ###    ##     ##   ##  #  ##    #    #     #     #   ##   #  #####  ###   ###
###  #   #  ##### ####   #   #   #   ######   ##   #########  #####  ##### ##### #   ##   #  #   #  #   # #####
-
File:
PackedCircle.js
Created By:
Mario Gonzalez
Project    :
None
Abstract:
A single packed circle.
Contains a reference to it's div, and information pertaining to it state.
Basic Usage:
http://onedayitwillmake.com/CirclePackJS/
*/
public class PackedCircle {

    public int id = 0;
    public Actor delegate = null;
    public Pt position = new Pt(0,0,0);
    public Pt offset = new Pt(0,0,0); // Offset from delegates position by this much

    public Pt targetPosition = null; // Where it wants to go
    public double targetChaseSpeed = 0.02;

    public boolean isFixed = false;
    private BoundsRule boundsRule;
    public int collisionMask = 0;
    public int collisionGroup = 0;

    // Add by me
    public double radius = 0;
    public double radiusSquared = 0;

    public enum BoundsRule {
        WRAP(1), // Wrap to otherside
        CONSTRAINT(2), // Constrain within bounds
        DESTROY(4), // Destroy when it reaches the edge
        IGNORE(8); // Ignore when reaching bounds

        private int val;

        private BoundsRule(int val) {
            this.val = val;
        }

        public int getVal() {
            return val;
        }
    }

    public PackedCircle() {
        this.boundsRule = BoundsRule.IGNORE;
        this.position = new Pt(0,0,0);
        this.offset = new Pt(0,0,0);
        this.targetPosition = new Pt(0,0,0);

        // this.delegate = null;
        // this.radius = 0;
        // this.radiusSquared = 0;
        //
        // // Collision properties
        // this.isFixed = false;
        // this.collisionMask = 0;
        // this.collisionGroup = 0;

    }

    public boolean containsPoint(Pt aPoint) {
        double distanceSquared = this.position.getDistanceSquared(aPoint);
        return distanceSquared < this.radiusSquared;
    }

    public int getDistanceSquaredFromPosition(Pt aPosition) {
        double distanceSquared = this.position.getDistanceSquared(aPosition);
        // if it's shorter than either radius, we intersect
        boolean result = distanceSquared < this.radiusSquared;

        if (result) {
            return 1;
        } else {
            return 0;
        }
    }

    public boolean intersects(PackedCircle aCircle) {
        double distanceSquared = this.position.getDistanceSquared(aCircle.position);
        return (distanceSquared < this.radiusSquared || distanceSquared < aCircle.radiusSquared);
    }

    /**
     * ACCESSORS
     */
    public PackedCircle setPosition(Pt aPosition) {
        this.position = aPosition;
        return this;
    }

    public PackedCircle setDelegate(Actor aDelegate) {
        this.delegate = aDelegate;
        return this;
    }

    public PackedCircle setOffset(Pt aPosition) {
        this.offset = aPosition;
        return this;
    }

    public PackedCircle setTargetPosition(Pt aTargetPosition) {
        this.targetPosition = aTargetPosition;
        return this;
    }
    
    public PackedCircle setTargetChaseSpeed(double aTargetChaseSpeed) {
        this.targetChaseSpeed = aTargetChaseSpeed;
        return this;
    }

    public PackedCircle setIsFixed(boolean value) {
        this.isFixed = value;
        return this;
    }

    public PackedCircle setCollisionMask(int aCollisionMask) {
        this.collisionMask = aCollisionMask;
        return this;
    }

    public PackedCircle setCollisionGroup(int aCollisionGroup) {
        this.collisionGroup = aCollisionGroup;
        return this;
    }

    public PackedCircle setRadius(double aRadius) {
        this.radius = aRadius;
        this.radiusSquared = this.radius * this.radius;
        return this;
    }

    public void dealloc() {
        this.position = null;
        this.offset = null;
        this.delegate = null;
        this.targetPosition = null;
    }

}
