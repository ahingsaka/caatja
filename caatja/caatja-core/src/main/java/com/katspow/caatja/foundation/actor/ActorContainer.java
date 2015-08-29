package com.katspow.caatja.foundation.actor;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import com.katspow.caatja.behavior.BaseBehavior;
import com.katspow.caatja.core.CAAT;
import com.katspow.caatja.core.canvas.CaatjaContext2d;
import com.katspow.caatja.core.canvas.CaatjaColor;
import com.katspow.caatja.event.MouseListener;
import com.katspow.caatja.foundation.Director;
import com.katspow.caatja.foundation.ui.layout.LayoutManager;
import com.katspow.caatja.math.Pt;
import com.katspow.caatja.math.Rectangle;
import com.katspow.caatja.pathutil.Path;

/**
 * This class is a general container of CAAT.Actor instances. It extends the concept of an Actor
 * from a single entity on screen to a set of entities with a parent/children relationship among
 * them.
 * <p>
 * This mainly overrides default behavior of a single entity and exposes methods to manage its children
 * collection.
 *
 * @constructor
 * @extends CAAT.Actor
 */
public class ActorContainer extends Actor {
    
    public static final Cache __CD= Cache.DEEP;

    public ActorContainer() {
        super();
        this.childrenList = new ArrayList<Actor>();
        this.activeChildren = new ArrayList<Actor>();
        this.pendingChildrenList = new ArrayList<Actor>();
    }
    
    /**
     * Constructor delegate
     * @param hint {CAAT.Foundation.ActorContainer.AddHint}
     * @return {*}
     * @private
     */
    public ActorContainer(AddHint hint) {
        this();
        if (hint != null && hint != AddHint.NONE) {
            this.addHint = hint;
            this.boundingBox = new Rectangle();
        }
    }
    
    public enum AddHint {
        NONE(0), CONFORM(1);
        
        private int val;

        private AddHint(int val) {
            this.val = val;
        }
        
        public int getValue() {
            return val;
        }
    }
    
    // TODO Scene objects ??
    /**
     * This container children.
     * @type {Array.<CAAT.Foundation.Actor>}
     */
    public List<Actor> childrenList;
    
    /**
     * This container active children.
     * @type {Array.<CAAT.Foundation.Actor>}
     * @private
     */
    public List<Actor> activeChildren;
    
    /**
     * This container pending to be added children.
     * @type {Array.<CAAT.Foundation.Actor>}
     * @private
     */
    public List<Actor> pendingChildrenList;
    
    /**
     * Container redimension policy when adding children:
     *  0 : no resize.
     *  CAAT.Foundation.ActorContainer.AddHint.CONFORM : resize container to a bounding box.
     *
     * @type {number}
     * @private
     */
    public AddHint addHint = AddHint.NONE;
    
    /**
     * If container redimension on children add, use this rectangle as bounding box store.
     * @type {CAAT.Math.Rectangle}
     * @private
     */
    public Rectangle boundingBox = null;
    
    /**
     * Spare rectangle to avoid new allocations when adding children to this container.
     * @type {CAAT.Math.Rectangle}
     * @private
     */
    public Rectangle runion = new Rectangle();   // Watch out. one for every container.
    
    /**
     * Define a layout manager for this container that enforces children position and/or sizes.
     * @see demo26 for an example of layouts.
     * @type {CAAT.Foundation.UI.Layout.LayoutManager}
     */
    public LayoutManager layoutManager       =   null;  
    
    /**
     * @type {boolean}
     */
    public boolean layoutInvalidated   =   true;
    
    public ActorContainer setLayout (LayoutManager layout ) {
        this.layoutManager= layout;
        return this;
    }

    @Override
    public ActorContainer setBounds (double x,double y,double w,double h ) {
        super.setBounds(x,y,w,h );
        if ( CAAT.currentDirector != null && !CAAT.currentDirector.inValidation ) {
            this.invalidateLayout();
        }
        return this;
    }

    public void __validateLayout () {

        this.__validateTree();
        this.layoutInvalidated= false;
    }

    public void __validateTree () {
        if ( this.layoutManager != null && this.layoutManager.isInvalidated() ) {

            CAAT.currentDirector.inValidation= true;

            this.layoutManager.doLayout( this );

            for( int i=0; i<this.getNumChildren(); i+=1 ) {
                this.getChildAt(i).__validateLayout();
            }
        }
    }

    public Actor invalidateLayout () {
        this.layoutInvalidated= true;

        if ( this.layoutManager != null) {
            this.layoutManager.invalidateLayout(this);

            for( int i=0; i<this.getNumChildren(); i+=1 ) {
                this.getChildAt(i).invalidateLayout();
            }
        }
        
        return this;
    }

    public LayoutManager getLayout () {
        return this.layoutManager;
    }
    
    /**
     * Draws this ActorContainer and all of its children screen bounding box.
     *
     * @param director the CAAT.Director object instance that contains the Scene the Actor is in.
     * @param time an integer indicating the Scene time when the bounding box is to be drawn.
     */
    @Override
    public void drawScreenBoundingBox(Director director, double time) {
        
        if (!this.inFrame) {
            return;
        }
        
        for (Actor actor : this.activeChildren) {
            actor.drawScreenBoundingBox(director, time);
        }
        
        super.drawScreenBoundingBox(director, time);
    }
    
    /**
     * Removes all children from this ActorContainer.
     *
     * @return this
     */
    public ActorContainer emptyChildren() {
        this.childrenList.clear();
        return this;
    }
    
    /**
     * Private
     * Paints this container and every contained children.
     *
     * @param director the CAAT.Director object instance that contains the Scene the Actor is in.
     * @param time an integer indicating the Scene time when the bounding box is to be drawn.
     */
    public boolean paintActor(Director director, double time) {
        
        if (!this.visible) {
            return false;
        }
        
        CaatjaContext2d ctx= director.ctx;
        
        ctx.save();
        
        if (!super.paintActor(director, time)) {
            return false;
        }
        
        if ( this.cached==__CD ) {
            return false;
        }
        
        if (!this.isGlobalAlpha) {
            if (this.parent != null) {
                this.frameAlpha = this.parent.frameAlpha;
            } else {
                this.frameAlpha = 1;
            }
        }
        
        for (Actor actor : this.activeChildren) {
            if ( actor.visible ) {
                ctx.save();
                actor.paintActor(director, time);
                ctx.restore();
            }
        }
        
        // FIXME Ugly , move this to Actor ??
//        if (this instanceof SkeletonActor) {
//            SkeletonActor skeletonActor = (SkeletonActor) this;
//            skeletonActor.postPaint( director, time );
//        }
        
        ctx.restore();
        
        return true;
    }
    
    /**
     * TODO Check
     */
    public boolean __paintActor(Director director, double time ) {
        
        if (!this.visible) {
            return true;
        }

        CaatjaContext2d ctx= director.ctx;
        this.frameAlpha= this.parent != null ? this.parent.frameAlpha*this.alpha : 1;
            double[] m= this.worldModelViewMatrix.matrix;
            ctx.setTransform( m[0], m[3], m[1], m[4], m[2], m[5] );
//            ctx.setTransform( m[0], m[3], m[1], m[4], m[2], m[5], this.frameAlpha );
            this.paint(director, time);
            
            if ( !this.isGlobalAlpha ) {
                this.frameAlpha= this.parent != null ? this.parent.frameAlpha : 1;
            }

//        for( Actor actor= this.activeChildren; actor; actor=actor.__next ) {
            for (Actor actor : this.activeChildren) {
                actor.paintActor(director,time);
            }
        
        return true;
    }
    
    public boolean paintActorGL(Director director, double time) {

        if (!this.visible) {
            return true;
        }
        
        super.paintActorGL(director,time);

        if ( !this.isGlobalAlpha ) {
            this.frameAlpha= this.parent.frameAlpha;
        }
        
            for (Actor c : this.activeChildren) {
                c.paintActorGL(director,time);
            }

        // TODO Check return type
        return true;
    }

    /**
     * Private.
     * Performs the animate method for this ActorContainer and every contained Actor.
     *
     * @param director the CAAT.Director object instance that contains the Scene the Actor is in.
     * @param time an integer indicating the Scene time when the bounding box is to be drawn.
     *
     * @return {boolean} is this actor in active children list ??
     * @throws Exception 
     */
    public boolean animate(Director director, double time) throws Exception {
        
        if ( !this.visible ) {
            return false;
        }
        
        this.activeChildren.clear();
        
        if (false == super.animate(director, time)) {
            return false;
        }
        
        if ( this.cached ==__CD ) {
            return true;
        }
        
        this.__validateLayout();
        CAAT.currentDirector.inValidation= false;
        
        /**
         * Incluir los actores pendientes.
         * El momento es ahora, antes de procesar ninguno del contenedor.
         */
        for (Actor child : this.pendingChildrenList) {
        	this.addChildImmediately(child);
		}
        
        this.pendingChildrenList.clear();
        List<Actor> markDelete = new ArrayList<Actor>();
        
        this.size_active= 1;
        this.size_total= 1;
        
        // FIXME Concurrent exception ?
        try {
            
            for (Actor actor : this.childrenList) {
               actor.time = time;
               this.size_total+= actor.size_total;
               if (actor.animate(director, time)) {
                   
                   this.activeChildren.add(actor);
                   
                   this.size_active+= actor.size_active;
                   
               } else {
            	   if ( actor.expired && actor.discardable ) {
                       markDelete.add(actor);
                   }
               }
            }
        	
        } catch (ConcurrentModificationException e) {
            
        }
        
        for (Actor md : markDelete) {
            md.destroy(time);
            if ( director.dirtyRectsEnabled ) {
                director.addDirtyRect( md.AABB );
            }
        }
        
        return true;
     
    }
    
    /**
     * Removes Actors from this ActorContainer which are expired and flagged as Discardable.
     *
     * @param director the CAAT.Director object instance that contains the Scene the Actor is in.
     * @param time an integer indicating the Scene time when the bounding box is to be drawn.
     * 
     * @deprecated
     */
    @Deprecated
    public void endAnimate(Director director, double time) {
//        super.endAnimate(director, time);
//
//        int i;
//        // remove expired and discardable elements.
//        for (i = this.childrenList.size() - 1; i >= 0; i--) {
//            Actor actor = this.childrenList.get(i);
//            if (actor.expired && actor.discardable) {
//                actor.destroy(time);
//                this.childrenList.remove(i);
//            } else {
//                actor.endAnimate(director, time);
//            }
//        }
//        
//        for( i=0; i<this.pendingChildrenList.size(); i++ ) {
//            Actor child= this.pendingChildrenList.get(i);
//            child.parent =  this;
//            this.childrenList.add(child);
//        }
//        
//        this.pendingChildrenList.clear();
    }
    
    /**
     * Adds an Actor to this Container.
     * The Actor will be added ON METHOD CALL, despite the rendering pipeline stage being executed at
     * the time of method call.
     *
     * This method is only used by CAAT.Director's transitionScene.
     *
     * @param child a CAAT.Actor instance.
     * @return this.
     * @throws Exception 
     */
    public ActorContainer addChildImmediately(Actor child, String constraint) throws Exception {
        return this.addChild(child, null);
    }
    
    // Add by me
    public ActorContainer addChildImmediately(Actor child) throws Exception {
        return this.addChildImmediately(child, null);
    }
    
    public ActorContainer addActorImmediately(Actor child, String constraint) throws Exception {
        return this.addChildImmediately(child,constraint);
    }

    public ActorContainer addActor(Actor child, String constraint ) throws Exception {
        return this.addChild(child,constraint);
    }

    /**
     * Adds an Actor to this ActorContainer. The Actor will be added to the
     * container AFTER frame animation, and not on method call time. Except the
     * Director and in orther to avoid visual artifacts, the developer SHOULD
     * NOT call this method directly.
     * 
     * * If the container has addingHint as CAAT.ActorContainer.AddHint.CONFORM,
     * new continer size will be calculated by summing up the union of every
     * client actor bounding box. This method will not take into acount actor's
     * affine transformations, so the bounding box will be AABB.
     * 
     * @param child
     *            a CAAT.Actor object instance.
     * @return this
     * @throws Exception
     */
    public ActorContainer addChild(Actor child, String constraint) throws Exception {
        
        if (child.parent != null) {
            throw new Exception("adding to a container an element with parent.");
        }
        
        child.parent = this;
        this.childrenList.add(child);
        child.dirty= true;
        
        if (this.layoutManager != null) {
            this.layoutManager.addChild( child, constraint );
            this.invalidateLayout();
            
        } else {
            /**
             * if Conforming size, recalc new bountainer size.
             */
            if ( this.addHint== AddHint.CONFORM ) {
                this.recalcSize();
            }
            
        }
        
        
        return this;
    }
    
    // Add by me
    public ActorContainer addChild(Actor child) throws Exception {
        return this.addChildImmediately(child, null);
    }
    
    /**
     * Recalc this container size by computing the union of every children bounding box.
     */
    public ActorContainer recalcSize() {
        Rectangle bb= this.boundingBox;
        bb.setEmpty();
        List<Actor> cl= this.childrenList;
        
        for (Actor ac : cl) {
            this.runion.setBounds(
                    ac.x<0 ? 0 : ac.x,
                    ac.y<0 ? 0 : ac.y,
                    ac.width,
                    ac.height );
                bb.unionRectangle( this.runion );
        }
        
        
        this.setSize( bb.x1, bb.y1 );

        return this;
    }

    
    /**
     * Add a child element and make it active in the next frame.
     * @param child {CAAT.Actor}
     */
    public ActorContainer addChildDelayed(Actor child) {
        this.pendingChildrenList.add(child);
        return this;
    }

    /**
     * Adds an Actor to this ActorContainer.
     *
     * @param child a CAAT.Actor object instance.
     *
     * @return this
     */
    public ActorContainer addChildAt(Actor child, int index) {
    	
		if( index <= 0 ) {
		    child.parent= this;
            child.dirty= true;
            this.childrenList.add(0, child);
            this.invalidateLayout();
			return this;
        } else {
            if ( index>=this.childrenList.size() ) {
                index= this.childrenList.size();
            }
        }

        child.parent= this;
        child.dirty= true;
        this.childrenList.add(index, child);
        this.invalidateLayout();

        return this;
    }
    
    /**
     * Find the first actor with the supplied ID.
     * This method is not recommended to be used since executes a linear search.
     * @param id
     * 
     * TODO Different from source
     */
    public Actor findActorById(String id) {
        
        // TODO Check
        if (super.findActorById(id) != null) {
            return this;
        }
        
        List<Actor> cl= this.childrenList;
        for (Actor actor : cl) {
            Actor ret = actor.findActorById(id);
            if (null!=ret) {
                return ret;
            }
        }
        
        return null;
    }

    /**
     * Private
     * Gets a contained Actor z-index on this ActorContainer.
     *
     * @param child a CAAT.Actor object instance.
     *
     * @return {number}
     */
    protected int findChild (Actor child) {
        return this.childrenList.indexOf(child);
    }
    
    /**
     * Removed all Actors from this ActorContainer.
     *
     * @return array of former children
     */
    public List<Actor> removeAllChildren() {
        List<Actor> cl = new ArrayList<Actor>(this.childrenList); // Make a shalow copy
        
        for (int pos = cl.size() - 1; pos >= 0; pos--) {
			this.removeChildAt(pos);
		}
        
        return cl;
    }
    
    public Actor removeChildAt(int pos ) {
        List<Actor> cl = this.childrenList;
        List<Actor> rm = null;
        if (-1 != pos) {
            cl.get(pos).setParent(null);
            // FIXME
            // rm= cl.splice(pos,1);
            rm = cl.subList(pos, 1);

            if (rm.get(0).isVisible() && CAAT.currentDirector.dirtyRectsEnabled) {
                CAAT.currentDirector.scheduleDirtyRect(rm.get(0).AABB);
            }

            return rm.get(0);

        }
        
        this.invalidateLayout();

        return null;
        
    }
    
    /**
     * TODO Check call above
     * Removed an Actor from this ActorContainer.
     * If the Actor is not contained into this Container, nothing happends.
     *
     * @param child a CAAT.Actor object instance.
     *
     * @return this
     */
    public ActorContainer removeChild(Actor child) {
        // TODO
//        int pos= this.findChild(child);
//        Actor ret= this.removeChildAt(pos);
        
        int pos = this.findChild(child);
        List<Actor> cl = this.childrenList;
        if (-1 != pos) {
            cl.get(pos).setParent(null);
            cl.remove(pos);
        }

        return this;
    }
    
    public Actor removeFirstChild() {
        Actor first= this.childrenList.remove(0);
        first.parent= null;
        if ( first.isVisible() && CAAT.currentDirector.dirtyRectsEnabled ) {
            CAAT.currentDirector.scheduleDirtyRect( first.AABB );
        }
        
        this.invalidateLayout();
        
        return first;
    }
    
    public Actor removeLastChild() {
        if ( this.childrenList.size() > 0 ) {
            Actor last= this.childrenList.get(this.childrenList.size() - 1);
            last.parent= null;
            if ( last.isVisible() && CAAT.currentDirector.dirtyRectsEnabled ) {
                CAAT.currentDirector.scheduleDirtyRect( last.AABB );
            }
            
            this.invalidateLayout();
            
            return last;
        }
        
        return null;
    }

    /**
     * @private
     *
     * Gets the Actor inside this ActorContainer at a given Screen coordinate.
     *
     * @param point an object of the form { x: float, y: float }
     *
     * @return the Actor contained inside this ActorContainer if found, or the ActorContainer itself.
     */
    @Override
    public Actor findActorAtPosition(Pt point) {

        if (null == super.findActorAtPosition(point)) {
            return null;
        }

        // z-order
        List<Actor> cl = this.childrenList;
        for (int i = cl.size() - 1; i >= 0; i--) {
            Actor child = cl.get(i);

            Pt np = new Pt(point.x, point.y, 0);

            Actor contained = child.findActorAtPosition(np);
            if (null != contained) {
                return contained;
            }
        }

        return this;
    }
    
    /**
     * Destroys this ActorContainer.
     * The process falls down recursively for each contained Actor into this ActorContainer.
     *
     * @return this
     */
    @Override
    public void destroy(double time) {
        List<Actor> cl = this.childrenList;
        for( int i=cl.size()-1; i>=0; i-- ) {
            cl.get(i).destroy(time);
        }
        
        super.destroy(time);
    }
    
    /**
     * Get number of Actors into this container.
     * 
     * @return integer indicating the number of children.
     */
    public int getNumChildren() {
        return this.childrenList.size();
    }

    public int getNumActiveChildren() {
        return this.activeChildren.size();
    }

    /**
     * Returns the Actor at the iPosition(th) position.
     * 
     * @param iPosition
     *            an integer indicating the position array.
     * @return the CAAT.Actor object at position.
     */
    public Actor getChildAt(int iPosition) {
        return this.childrenList.get(iPosition);
    }
    
    /**
     * Changes an actor's ZOrder.
     * @param actor the actor to change ZOrder for
     * @param index an integer indicating the new ZOrder. a value greater than children list size means to be the
     * last ZOrder Actor.
     * 
     * Note : if an actor is added afterwards, its zorder will be bigger than previously added actors !
     */
    public void setZOrder(Actor actor, int index ) {
        int actorPos= this.findChild(actor);
        // the actor is present
        if (-1 != actorPos) {
            List<Actor> cl = this.childrenList;

            // trivial reject.
            if (index == actorPos) {
                return;
            }

            if (index >= cl.size()) {
                cl.remove(actorPos);
                cl.add(actor);
                
            } else {
                
                Actor nActor = cl.remove(actorPos);
                
                if (index < 0) {
                    index = 0;
                } else if (index > cl.size()) {
                    index = cl.size();
                }

                cl.add(index, nActor);
            }
            
            this.invalidateLayout();
        }
    }

    
    // Add by me
    
    @Override
    public ActorContainer enableEvents(boolean enable) {
        return (ActorContainer) super.enableEvents(enable);
    }

    @Override
    public ActorContainer addBehavior(BaseBehavior behaviour) {
        return (ActorContainer) super.addBehavior(behaviour);
    }

    @Override
    public ActorContainer setFillStrokeStyle(CaatjaColor style) {
        return (ActorContainer) super.setFillStrokeStyle(style);
    }

    @Override
    public ActorContainer setClip(boolean clip, Path clipPath) {
        return (ActorContainer) super.setClip(clip, clipPath);
    }
    
    @Override
    public ActorContainer setFillStyle(String fillStyle) {
        return (ActorContainer) super.setFillStyle(fillStyle);
    }
    
    @Override
    public ActorContainer setAlpha(double alpha) {
        return (ActorContainer) super.setAlpha(alpha);
    }
    
    @Override
    public ActorContainer setGlobalAlpha(boolean global) {
        return (ActorContainer) super.setGlobalAlpha(global);
    }
    
    @Override
    public ActorContainer setSize(double w, double h) {
        return (ActorContainer) super.setSize(w, h);
    }
    
    @Override
    public ActorContainer setLocation(double x, double y) {
        return (ActorContainer) super.setLocation(x, y);
    }
    
    @Override
    public ActorContainer setRotation(double angle) {
        return (ActorContainer) super.setRotation(angle);
    }

    public ActorContainer setClip(boolean clip) {
        return setClip(clip, null);
    }

    @Override
    public ActorContainer setScale(double sx, double sy) {
        return (ActorContainer) super.setScale(sx, sy);
    }

    @Override
    public ActorContainer setMouseClickListener(MouseListener mouseListener) {
        return (ActorContainer) super.setMouseClickListener(mouseListener);
    }
    
    
    
}
