package com.katspow.caatja.modules.collision;

import java.util.ArrayList;
import java.util.List;

import com.katspow.caatja.math.AABB;
import com.katspow.caatja.math.Rectangle;

/**
 * This file contains the definition for objects QuadTree and HashMap.
 * Quadtree offers an exact list of collisioning areas, while HashMap offers a list of potentially colliding elements.
 * Specially suited for static content.
 **/
public class QuadTree extends Rectangle {
    
    public static final int QT_MAX_ELEMENTS=    1;
    public static final int QT_MIN_WIDTH=       32;
    
    /**
     * For each quadtree level this keeps the list of overlapping elements.
     */
    List<AABB> bgActors      =   null;

    /**
     * For each quadtree, this quadData keeps another 4 quadtrees up to the  maximum recursion level.
     */
    List<QuadTree> quadData    =   null;
    
    public QuadTree create (double l, double t, double r, double b, List<AABB> backgroundElements, Integer minWidth, Integer maxElements ) {
        
        if ( minWidth==null ) {
            minWidth= QT_MIN_WIDTH;
        }
        if ( maxElements==null ) {
            maxElements= QT_MAX_ELEMENTS;
        }
        
        double cx= (l+r)/2;
        double cy= (t+b)/2;

        this.x=         l;
        this.y=         t;
        this.x1=        r;
        this.y1=        b;
        this.width=     r-l;
        this.height=    b-t;

        this.bgActors= this.__getOverlappingActorList( backgroundElements );

        if ( this.bgActors.size() <= maxElements || this.width <= minWidth  ) {
            return this;
        }

        this.quadData= new ArrayList<QuadTree>(4);
        this.quadData.set(0,new QuadTree().create( l,t,cx,cy, this.bgActors ));  // TL
        this.quadData.set(1,new QuadTree().create( cx,t,r,cy, this.bgActors ));  // TR
        this.quadData.set(2,new QuadTree().create( l,cy,cx,b, this.bgActors ));  // BL
        this.quadData.set(3,new QuadTree().create( cx,cy,r,b, this.bgActors ));

        return this;
    }
    
    // Add by me
    public QuadTree create (double l, double t, double r, double b, List<AABB> backgroundElements) {
        return create(l, t, r, b, backgroundElements, null, null);
    }
    

    public List<AABB> __getOverlappingActorList (List<AABB> actorList ) {
        List<AABB> tmpList= new ArrayList<AABB>();
        for( int i=0, l=actorList.size(); i<l; i++ ) {
            AABB actor= actorList.get(i);
            if ( this.intersects( actor.AABB ) ) {
                tmpList.add( actor );
            }
        }
        return tmpList;
    }

    /**
     * Call this method to thet the list of colliding elements with the parameter rectangle.
     * @param rectangle
     * @return {Array}
     */
    public List<AABB> getOverlappingActors (Rectangle rectangle ) {
        int i,j,l;
        List<AABB> overlappingActors= new ArrayList<AABB>();
        List<AABB> qoverlappingActors;
        List<AABB> actors= this.bgActors;
        AABB actor;

        if ( this.quadData != null) {
            for( i=0; i<4; i++ ) {
                if ( this.quadData.get(i).intersects( rectangle ) ) {
                    qoverlappingActors= this.quadData.get(i).getOverlappingActors(rectangle);
                    for( j=0,l=qoverlappingActors.size(); j<l; j++ ) {
                        overlappingActors.add( qoverlappingActors.get(j) );
                    }
                }
            }
        } else {
            for( i=0, l=actors.size(); i<l; i++ ) {
                actor= actors.get(i);
                if ( rectangle.intersects( actor.AABB ) ) {
                    overlappingActors.add( actor );
                }
            }
        }

        return overlappingActors;
    }

}
