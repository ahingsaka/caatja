package com.katspow.caatja.foundation.ui;

import com.katspow.caatja.behavior.BaseBehavior;
import com.katspow.caatja.behavior.BehaviorListener;
import com.katspow.caatja.behavior.GenericBehavior;
import com.katspow.caatja.behavior.SetForTimeReturnValue;
import com.katspow.caatja.behavior.listener.BehaviorAppliedListener;
import com.katspow.caatja.behavior.listener.BehaviorExpiredListener;
import com.katspow.caatja.event.CAATMouseEvent;
import com.katspow.caatja.event.MouseListener;
import com.katspow.caatja.foundation.Scene;
import com.katspow.caatja.foundation.actor.Actor;
import com.katspow.caatja.foundation.actor.ActorContainer;
import com.katspow.caatja.foundation.timer.CallbackTimeout;
import com.katspow.caatja.foundation.timer.TimerTask;

/**
 *
 * In this file we'll be adding every useful Actor that is specific for certain purpose.
 *
 * + CAAT.Dock: a docking container that zooms in/out its actors.
 *
 */
/**
 * @name Dock
 * @memberOf CAAT.Foundation.UI
 * @extends CAAT.Foundation.ActorContainer
 * @constructor
 */
public class Dock extends ActorContainer {
    
    /**
     * This actor simulates a mac os-x docking component.
     * Every contained actor will be laid out in a row in the desired orientation.
     */
    public Dock() {
        super();
        setMouseExitListener(new MouseListener() {
            public void call(CAATMouseEvent e) throws Exception {
                actorNotPointed();
            }
        });
        
        setMouseMoveListener(new MouseListener() {
            public void call(CAATMouseEvent e) throws Exception {
                actorNotPointed();
            }
        });
    }
    
    /**
     * scene the actor is in.
     */
    public Scene scene=              null;
    
    /**
     * resetting dimension timer task.
     */
    public TimerTask ttask=              null;
    
    /**
     * min contained actor size.
     */
    public int minSize=            0;
    
    /**
     * max contained actor size
     */
    public int maxSize=            0;
    
    /**
     * aproximated number of elements affected.
     */
    public int range=              2;
    
    /**
     * Any value from CAAT.Foundation.Dock.UI.OP_LAYOUT_*
     */
    public OpLayout layoutOp=           OpLayout.BOTTOM;
    
    public enum OpLayout {
        BOTTOM(   0),
        TOP(      1),
        LEFT(     2),
        RIGHT(    3);
        
        private int val;

        private OpLayout(int val) {
            this.val = val;
        }
        
        public int getVal() {
            return val;
        }
    }
    
    public Dock initialize(Scene scene) {
        this.scene= scene;
        return this;
    }

    /**
     * Set the number of elements that will be affected (zoomed) when the mouse is inside the component.
     * @param range {number} a number. Defaults to 2.
     */
    public Dock setApplicationRange (int range ) {
        this.range= range;
        return this;
    }
    
    /**
     * Set layout orientation. Choose from
     * <ul>
     *  <li>CAAT.Dock.prototype.OP_LAYOUT_BOTTOM
     *  <li>CAAT.Dock.prototype.OP_LAYOUT_TOP
     *  <li>CAAT.Dock.prototype.OP_LAYOUT_BOTTOM
     *  <li>CAAT.Dock.prototype.OP_LAYOUT_RIGHT
     * </ul>
     * By default, the layou operation is OP_LAYOUT_BOTTOM, that is, elements zoom bottom anchored.
     *
     * @param lo {number} one of CAAT.Dock.OP_LAYOUT_BOTTOM, CAAT.Dock.OP_LAYOUT_TOP,
     * CAAT.Dock.OP_LAYOUT_BOTTOM, CAAT.Dock.OP_LAYOUT_RIGHT.
     *
     * @return this
     */
    public Dock setLayoutOp (OpLayout lo ) {
        this.layoutOp= lo;
        return this;
    }
    
    /**
    *
    * Set maximum and minimum size of docked elements. By default, every contained actor will be
    * of 'min' size, and will be scaled up to 'max' size.
    *
    * @param min {number}
    * @param max {number}
    * @return this
    */
    public Dock setSizes (int min, int max ) {
        this.minSize= min;
        this.maxSize= max;
        
        for (Actor child : this.childrenList) {
            child.width = min;
            child.height = min;
        }
        
        return this;
    }
    
    /**
     * Lay out the docking elements. The lay out will be a row with the orientation set by calling
     * the method <code>setLayoutOp</code>.
     *
     * @private
     */
    public void layout () {
        int i;

        if ( this.layoutOp==OpLayout.BOTTOM || this.layoutOp==OpLayout.TOP ) {

            double currentWidth=0, currentX=0;

            for( i=0; i<this.getNumChildren(); i++ ) {
                currentWidth+= this.getChildAt(i).width;
            }

            currentX= (this.width-currentWidth)/2;

            for( i=0; i<this.getNumChildren(); i++ ) {
                Actor actor= this.getChildAt(i);
                actor.x= currentX;
                currentX+= actor.width;

                if ( this.layoutOp==OpLayout.BOTTOM ) {
                    actor.y= this.maxSize- actor.height;
                } else {
                    actor.y= 0;
                }
            }
        } else {

            double currentHeight=0, currentY=0;

            for( i=0; i<this.getNumChildren(); i++ ) {
                currentHeight+= this.getChildAt(i).height;
            }

            currentY= (this.height-currentHeight)/2;

            for( i=0; i<this.getNumChildren(); i++ ) {
                Actor actor= this.getChildAt(i);
                actor.y= currentY;
                currentY+= actor.height;

                if ( this.layoutOp==OpLayout.LEFT ) {
                    actor.x= 0;
                } else {
                    actor.x= this.width - actor.width;
                }
            }

        }

    }
    
    // TODO Remove
//    @Override
//    public void mouseMove (CAATMouseEvent mouseEvent) {
//        this.actorNotPointed();
//    }
    
    // TODO Remove
//    @Override
//    public void mouseExit (CAATMouseEvent mouseEvent) {
//        this.actorNotPointed();
//    }
    
    /**
     * Performs operation when the mouse is not in the dock element.
     *
     * @private
     */
    public void actorNotPointed () {

        int i;

        for( i=0; i<this.getNumChildren(); i++ ) {
            final Actor actor= this.getChildAt(i);
            actor.emptyBehaviorList();
            actor.addBehavior(
                    new GenericBehavior().
                        setValues( actor.width, this.minSize, actor, "width", null ).
                        setFrameTime( this.scene.time, 250 ) ).
                addBehavior(
                    new GenericBehavior().
                        setValues( actor.height, this.minSize, actor, "height", null ).
                        setFrameTime( this.scene.time, 250 ) );

            if ( i==this.getNumChildren()-1 ) {
                
                
                BehaviorListener behaviorListener = new BehaviorListener();
                BehaviorAppliedListener behaviorAppliedListener = new BehaviorAppliedListener() {
                    public void onApplied(BaseBehavior behavior, double time, double normalizeTime, Actor targetActor, SetForTimeReturnValue value)
                            throws Exception {
                         // TODO Auto-generated method stub
                        ((Dock) targetActor.parent).layout();
                    }
                };
                
                BehaviorExpiredListener behaviorExpiredListener = new BehaviorExpiredListener() {
                    public void onExpired(BaseBehavior behavior, double time, Actor targetActor) {
                        for(int i=0; i< getNumChildren(); i++ ) {
                            Actor actor= getChildAt(i);
                            actor.width  = minSize;
                            actor.height = minSize;
                        }
                        ((Dock) targetActor.parent).layout();
                        
                    }
                };
                
                behaviorListener.setBehaviorAppliedListener(behaviorAppliedListener);
                behaviorListener.setBehaviorExpiredListener(behaviorExpiredListener);
                
                actor.behaviorList.get(0).addListener(behaviorListener);
                
//                actor.behaviorList.get(0).addListener(
//                        new BehaviorListener() {
//                            
//                            @Override
//                            public void behaviorExpired(BaseBehavior behavior, double time, Actor targetActor) {
//                                for(int i=0; i< getNumChildren(); i++ ) {
//                                    Actor actor= getChildAt(i);
//                                    actor.width  = minSize;
//                                    actor.height = minSize;
//                                }
//                                ((Dock) targetActor.parent).layout();
//                            }
//                            
//                            @Override
//                            public void behaviorApplied(BaseBehavior behavior, double time, double normalizeTime, Actor targetActor, SetForTimeReturnValue value) {
//                                ((Dock) targetActor.parent).layout();
//                                
//                            }
//
//                            @Override
//                            public void behaviorStarted(BaseBehavior behavior, double time, Actor actor) {
//                                
//                            }
//                        });
            }
        }
    }
    
    /**
    *
    * Perform the process of pointing a docking actor.
    *
    * @param x {number}
    * @param y {number}
    * @param pointedActor {CAAT.Actor}
    *
    * @private
    */
    public void actorPointed (double x, double y, Actor pointedActor) {

        int index= this.findChild(pointedActor);

       double across= 0;
        if ( this.layoutOp==OpLayout.BOTTOM || this.layoutOp==OpLayout.TOP ) {
            across= x / pointedActor.width;
        } else {
            across= y / pointedActor.height;
        }
        int i;

        for( i=0; i<this.childrenList.size(); i++ ) {
            Actor actor= this.childrenList.get(i);
            actor.emptyBehaviorList();

            double wwidth=0;
            if (i < index - this.range || i > index + this.range) {
                wwidth = this.minSize;
            } else if (i == index) {
                wwidth = this.maxSize;
            } else if (i < index) {
                wwidth=
                    this.minSize +
                    (this.maxSize-this.minSize) *
                    (Math.cos((i - index - across + 1) / this.range * Math.PI) + 1) /
                    2;
            } else {
                wwidth=
                    this.minSize +
                    (this.maxSize-this.minSize)*
                    (Math.cos( (i - index - across) / this.range * Math.PI) + 1) /
                    2;
            }

            actor.height= wwidth;
            actor.width= wwidth;
        }

        this.layout();
    }
    
    /**
     * Perform the process of exiting the docking element, that is, animate elements to the minimum
     * size.
     *
     * @param mouseEvent {CAAT.MouseEvent} a CAAT.MouseEvent object.
     *
     * @private
     */
    public void actorMouseExit (final CAATMouseEvent mouseEvent) {
        if ( null!=this.ttask ) {
            this.ttask.cancel();
        }

        this.ttask= this.scene.createTimer(
                this.scene.time,
                100,
                new CallbackTimeout() {
                    public void call(double time, double ttime, TimerTask timerTask) {
                        actorNotPointed();
                    }
                },
                null,
                null);
    }
    
    /**
     * Perform the beginning of docking elements.
     * @param mouseEvent {CAAT.MouseEvent} a CAAT.MouseEvent object.
     *
     * @private
     */
    public void actorMouseEnter (CAATMouseEvent mouseEvent) {
        if ( null!=this.ttask ) {
            this.ttask.cancel();
            this.ttask= null;
        }
    }
    
    /**
     * Adds an actor to Dock.
     * <p>
     * Be aware that actor mouse functions must be set prior to calling this method. The Dock actor
     * needs set his own actor input events functions for mouseEnter, mouseExit and mouseMove and
     * will then chain to the original methods set by the developer.
     *
     * @param actor {CAAT.Actor} a CAAT.Actor instance.
     *
     * @return this
     * 
     * FIXME This cannot be done easily in Java ...
     * @throws Exception 
     */
    public Dock addChild(Actor actor) throws Exception {

//        actor.__Dock_mouseEnter= actor.mouseEnter;
//        actor.__Dock_mouseExit=  actor.mouseExit;
//        actor.__Dock_mouseMove=  actor.mouseMove;
//
        /**
         * @ignore
         * @param mouseEvent
         */
//        actor.mouseEnter= function(mouseEvent) {
//            actorMouseEnter(mouseEvent);
//            this.__Dock_mouseEnter(mouseEvent);
//        }
        
        /**
         * @ignore
         * @param mouseEvent
         */
//        actor.mouseExit= function(mouseEvent) {
//            actorMouseExit(mouseEvent);
//            this.__Dock_mouseExit(mouseEvent);
//        }
        
        /**
         * @ignore
         * @param mouseEvent
         */
//        actor.mouseMove= function(mouseEvent) {
//            actorPointed( mouseEvent.point.x, mouseEvent.point.y, mouseEvent.source );
//            this.__Dock_mouseMove(mouseEvent);
//        }
        
        actor.width= this.minSize;
        actor.height= this.minSize;

        return (Dock) super.addChild(actor);
        
        
        
    }

    // Add by me
    @Override
    public Dock setBounds(double x, double y, double w, double h) {
        return (Dock) super.setBounds(x, y, w, h);
    }

}
