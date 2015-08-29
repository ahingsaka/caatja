package com.katspow.caatja.foundation;

import java.util.ArrayList;
import java.util.List;

import com.katspow.caatja.behavior.AlphaBehavior;
import com.katspow.caatja.behavior.BaseBehavior;
import com.katspow.caatja.behavior.BehaviorListener;
import com.katspow.caatja.behavior.ContainerBehavior;
import com.katspow.caatja.behavior.Interpolator;
import com.katspow.caatja.behavior.PathBehavior;
import com.katspow.caatja.behavior.RotateBehavior;
import com.katspow.caatja.behavior.ScaleBehavior;
import com.katspow.caatja.behavior.listener.BehaviorExpiredListener;
import com.katspow.caatja.core.Caatja;
import com.katspow.caatja.core.canvas.CaatjaColor;
import com.katspow.caatja.core.canvas.CaatjaContext2d;
import com.katspow.caatja.foundation.actor.Actor;
import com.katspow.caatja.foundation.actor.ActorContainer;
import com.katspow.caatja.foundation.actor.ActorRender;
import com.katspow.caatja.foundation.timer.CallbackCancel;
import com.katspow.caatja.foundation.timer.CallbackTick;
import com.katspow.caatja.foundation.timer.CallbackTimeout;
import com.katspow.caatja.foundation.timer.TimerManager;
import com.katspow.caatja.foundation.timer.TimerTask;
import com.katspow.caatja.foundation.ui.ShapeActor;
import com.katspow.caatja.foundation.ui.ShapeActor.Shape;
import com.katspow.caatja.math.Pt;
import com.katspow.caatja.math.matrix.Matrix;
import com.katspow.caatja.pathutil.Path;

/**
 * Scene is the top level ActorContainer of the Director at any given time.
 * The only time when 2 scenes could be active will be during scene change.
 * An scene controls the way it enters/exits the scene graph. It is also the entry point for all
 * input related and timed related events to every actor on screen.
 *
 * @constructor
 * @extends CAAT.ActorContainer
 *
 */
public class Scene extends ActorContainer { // implements BehaviorListener {
    
    // Add by me
    public CaatjaContext2d ctx;
    public ActorRender onRenderStart;
    public ActorRender onRenderEnd;
    private BehaviorListener behaviorListener;

    public Scene() {
        super();
//        this.timerList = new ArrayList<TimerTask>();
        this.timerManager = new TimerManager();
        this.fillStyle = null;
        this.isGlobalAlpha= true;
        
        behaviorListener = new BehaviorListener();
        
        BehaviorExpiredListener behaviorExpiredListener = new BehaviorExpiredListener() {
            public void onExpired(BaseBehavior behavior, double time, Actor actor) {
                easeContainerBehaviourListener.easeEnd(Scene.this, easeIn);
            }
        };
        
        behaviorListener.setBehaviorExpiredListener(behaviorExpiredListener);
    }

    /**
     * Behavior container used uniquely for Scene switching.
     * @type {CAAT.Behavior.ContainerBehavior}
     * @private
     */
    private ContainerBehavior easeContainerBehaviour = null;
    
    /**
     * Array of container behaviour events observer.
     * @private
     */
    // TODO Director or ?
    private Director easeContainerBehaviourListener = null;
    
    /**
     * When Scene switching, this boolean identifies whether the Scene is being brought in, or taken away.
     * @type {boolean}
     * @private
     */
    private boolean easeIn = false;
    
    // Constant values to identify the type of Scene transition
    // to perform on Scene switching by the Director.
    public enum Ease {
        ROTATION(                  1),
        SCALE(                     2),
        TRANSLATE(                 3);
        
        private int val;

        private Ease(int val) {
            this.val = val;
        }
        
        public int getVal() {
            return val;
        }
    }
    
    /**
     * is this scene paused ?
     * @type {boolean}
     * @private
     */
    public boolean paused = false;
    
    /**
     * This scene�s timer manager.
     * @type {CAAT.Foundation.Timer.TimerManager}
     * @private
     */
    public TimerManager timerManager;
    
    public boolean isPaused() {
        return paused;
    }
    
    public void setPaused(boolean paused) {
        this.paused = paused;
    }
    
    /**
     * Creates a timer task. Timertask object live and are related to scene's time, so when an Scene
     * is taken out of the Director the timer task is paused, and resumed on Scene restoration.
     *
     * @param startTime {number} an integer indicating the scene time this task must start executing at.
     * @param duration {number} an integer indicating the timerTask duration.
     * @param callback_timeout {function} timer on timeout callback function.
     * @param callback_tick {function} timer on tick callback function.
     * @param callback_cancel {function} timer on cancel callback function.
     *
     * @return {CAAT.TimerTask} a CAAT.TimerTask class instance.
     */
    public TimerTask createTimer (double startTime, double duration, CallbackTimeout callback_timeout, CallbackTick callback_tick, CallbackCancel callback_cancel ) {
        return this.timerManager.createTimer(startTime, duration, callback_timeout, callback_tick, callback_cancel, this);
    }
    
    public TimerTask setTimeout(double duration, CallbackTimeout callback_timeout, CallbackTick callback_tick,
            CallbackCancel callback_cancel) {
        return this.timerManager.createTimer(this.time, duration, callback_timeout, callback_tick, callback_cancel,
                this);
    }
    
    /**
     * Helper method to manage alpha transparency fading on Scene switch by the Director.
     * * @param time {number} time in milliseconds then fading will taableIne.
     * @param isIn {boolean} boolean indicating whether this Scene in the switch process is
     * being brought in.
     *
     * @private
     */
    private void createAlphaBehaviour(double time, boolean isIn) {
        AlphaBehavior ab= new AlphaBehavior();
        ab.setFrameTime( 0, time );
        ab.startAlpha= isIn ? 0 : 1;
        ab.endAlpha= isIn ? 1 : 0;
        this.easeContainerBehaviour.addBehavior(ab);               
    }
    
    /**
     * Called from CAAT.Director to bring in an Scene.
     * A helper method for easeTranslation.
     * @param time integer indicating time in milliseconds for the Scene to be brought in.
     * @param alpha boolean indicating whether fading will be applied to the Scene.
     * @param anchor integer indicating the Scene switch anchor.
     * @param interpolator CAAT.Interpolator to apply to the Scene transition.
     */
    public void easeTranslationIn ( double time,boolean  alpha, Anchor anchor,Interpolator interpolator ) {
        this.easeTranslation( time, alpha, anchor, true, interpolator );
    }
    
    /**
     * Called from CAAT.Director to bring in an Scene.
     * A helper method for easeTranslation.
     * @param time integer indicating time in milliseconds for the Scene to be taken away.
     * @param alpha boolean indicating whether fading will be applied to the Scene.
     * @param anchor integer indicating the Scene switch anchor.
     * @param interpolator CAAT.Interpolator to apply to the Scene transition.
     */
    public void easeTranslationOut ( double time,boolean  alpha, Anchor anchor,Interpolator interpolator ) {
        this.easeTranslation( time, alpha, anchor, false, interpolator );
    }
    
    /**
     * This method will setup Scene behaviours to switch an Scene via a translation.
     * The anchor value can only be
     *  <li>CAAT.Actor.prototype.ANCHOR_LEFT
     *  <li>CAAT.Actor.prototype.ANCHOR_RIGHT
     *  <li>CAAT.Actor.prototype.ANCHOR_TOP
     *  <li>CAAT.Actor.prototype.ANCHOR_BOTTOM
     * if any other value is specified, any of the previous ones will be applied.
     *
     * @param time integer indicating time in milliseconds for the Scene.
     * @param alpha boolean indicating whether fading will be applied to the Scene.
     * @param anchor integer indicating the Scene switch anchor.
     * @param isIn boolean indicating whether the scene will be brought in.
     * @param interpolator CAAT.Interpolator to apply to the Scene transition.
     */
    public void easeTranslation ( double time,Boolean  alpha, Anchor anchor, boolean isIn, Interpolator interpolator ) {

        this.easeContainerBehaviour= new ContainerBehavior();
        this.easeIn= isIn;

        PathBehavior pb= new PathBehavior();
        if ( interpolator != null ) {
            pb.setInterpolator( interpolator );
        }

        pb.setFrameTime( 0, time );

        // BUGBUG anchors: 1..4
        int value = anchor.getValue();
//        value %= 4;
//        value++;
        if ( value <1 ) {
            value =1;
        } else if ( value>4 ) {
            value= 4;
        }
        
        switch(value) {
        case 1:
            if ( isIn ) {
                pb.setPath( new Path().setLinear( 0, -this.height+1, 0, 0) );
                this.setPosition(0,-this.height+1);
            } else {
                pb.setPath( new Path().setLinear( 0, 0, 0, -this.height+1) );
                this.setPosition(0,0);
            }
            break;
        case 2:
            if ( isIn ) {
                pb.setPath( new Path().setLinear( 0, this.height-1, 0, 0) );
                this.setPosition(0,this.height-1);
            } else {
                pb.setPath( new Path().setLinear( 0, 0, 0, this.height-1) );
                this.setPosition(0,0);
            }
            break;
        case 3:
            if ( isIn ) {
                pb.setPath( new Path().setLinear( -this.width+1, 0, 0, 0) );
                this.setPosition(-this.width+1,0);
            } else {
                pb.setPath( new Path().setLinear( 0, 0, -this.width+1, 0) );
                this.setPosition(0,0);
            }
            break;
        case 4:
            if ( isIn ) {
                pb.setPath( new Path().setLinear( this.width-1, 0, 0, 0) );
                this.setPosition(this.width-1,0);
            } else {
                pb.setPath( new Path().setLinear( 0, 0, this.width-1, 0) );
                this.setPosition(0,0);
            }
            break;
          default:
              System.out.println("ERROR");
              break;
        }

        if (alpha != null && alpha) {
            this.createAlphaBehaviour(time,isIn);
        }

        this.easeContainerBehaviour.addBehavior(pb);

        this.easeContainerBehaviour.setFrameTime( this.time, time );
        
        // TODO Change bt me
        this.easeContainerBehaviour.addListener(behaviorListener);
//        this.easeContainerBehaviour.addListener(this);

        this.emptyBehaviorList();
        
        super.addBehavior(easeContainerBehaviour);
        
    }
    
    /**
     * Called from CAAT.Director to bring in a Scene.
     * A helper method for easeScale.
     * @param time integer indicating time in milliseconds for the Scene to be brought in.
     * @param alpha boolean indicating whether fading will be applied to the Scene.
     * @param anchor integer indicating the Scene switch anchor.
     * @param interpolator {CAAT.Interpolator} a CAAT.Interpolator to apply to the Scene transition.
     * @param starttime integer indicating in milliseconds from which scene time the behavior will be applied.
     */
    public void easeScaleIn (double starttime,double time,boolean alpha,Anchor anchor,Interpolator interpolator) {
        this.easeScale(starttime,time,alpha,anchor,true,interpolator);
        this.easeIn= true;
    }
    
    /**
     * Called from CAAT.Director to take away a Scene.
     * A helper method for easeScale.
     * @param time integer indicating time in milliseconds for the Scene to be taken away.
     * @param alpha boolean indicating whether fading will be applied to the Scene.
     * @param anchor integer indicating the Scene switch anchor.
     * @param interpolator {CAAT.Interpolator} a CAAT.Interpolator instance to apply to the Scene transition.
     * @param starttime integer indicating in milliseconds from which scene time the behavior will be applied.
     */
    public void easeScaleOut (double starttime,double time,boolean alpha,Anchor anchor,Interpolator interpolator) {
        this.easeScale(starttime,time,alpha,anchor,false,interpolator);
        this.easeIn= false;
    }
    
    /**
     * Called from CAAT.Director to bring in ot take away an Scene.
     * @param time integer indicating time in milliseconds for the Scene to be taken away.
     * @param alpha boolean indicating whether fading will be applied to the Scene.
     * @param anchor integer indicating the Scene switch anchor.
     * @param interpolator CAAT.Interpolator to apply to the Scene transition.
     * @param starttime integer indicating in milliseconds from which scene time the behavior will be applied.
     * @param isIn boolean indicating whether the Scene is being brought in.
     */
    public void easeScale (double starttime,double time,Boolean alpha,Anchor anchor,boolean isIn,Interpolator interpolator) {
        this.easeContainerBehaviour= new ContainerBehavior();

        double x=0;
        double y=0;
        double x2=0;
        double y2=0;
        
        switch(anchor) {
        case TOP_LEFT:
        case TOP_RIGHT:
        case BOTTOM_LEFT:
        case BOTTOM_RIGHT:
        case CENTER:
            x2=1;
            y2=1;
            break;
        case TOP:
        case BOTTOM:
            x=1;
            x2=1;
            y=0;
            y2=1;
            break;
        case LEFT:
        case RIGHT:
            y=1;
            y2=1;
            x=0;
            x2=1;
            break;
        default:
            Caatja.alert("scale anchor ?? " + anchor);
        }

        if ( !isIn ) {
            double tmp;
            tmp= x;
            x= x2;
            x2= tmp;
            
            tmp= y;
            y= y2;
            y2= tmp;
        }
        
        if (alpha != null && alpha) {
            this.createAlphaBehaviour(time,isIn);
        }
        
        Pt anchorPercent= this.getAnchorPercent(anchor.getValue());
        
        ScaleBehavior sb= new ScaleBehavior();
        sb.setFrameTime( starttime, time ).
            setValues(x,x2,y,y2, anchorPercent.x, anchorPercent.y);
        
        if ( interpolator != null ) {
            sb.setInterpolator(interpolator);
        }

        this.easeContainerBehaviour.addBehavior(sb);
        
        this.easeContainerBehaviour.setFrameTime( this.time, time );
        // TODO Change bt me
        this.easeContainerBehaviour.addListener(behaviorListener);
//        this.easeContainerBehaviour.addListener(this);
        
        this.emptyBehaviorList();
        
        super.addBehavior(easeContainerBehaviour);
    }
    
    /**
     * Overriden method to disallow default behavior.
     * Do not use directly.
     */
    @Override
    public Scene addBehavior(BaseBehavior behaviour) {
        return this;
    }
    
    /**
     * Called from CAAT.Director to use Rotations for bringing in.
     * This method is a Helper for the method easeRotation.
     * @param time integer indicating time in milliseconds for the Scene to be brought in.
     * @param alpha boolean indicating whether fading will be applied to the Scene.
     * @param anchor integer indicating the Scene switch anchor.
     * @param interpolator {CAAT.Interpolator} a CAAT.Interpolator to apply to the Scene transition.
     */
    public void easeRotationIn (double time,boolean alpha,Anchor anchor,Interpolator interpolator) {
        this.easeRotation(time,alpha,anchor,true, interpolator);
        this.easeIn= true;
    }
    
    /**
     * Called from CAAT.Director to use Rotations for taking Scenes away.
     * This method is a Helper for the method easeRotation.
     * @param time integer indicating time in milliseconds for the Scene to be taken away.
     * @param alpha boolean indicating whether fading will be applied to the Scene.
     * @param anchor integer indicating the Scene switch anchor.
     * @param interpolator {CAAT.Interpolator} a CAAT.Interpolator to apply to the Scene transition.
     */
    public void easeRotationOut (double time,boolean alpha,Anchor anchor,Interpolator interpolator) {
        this.easeRotation(time,alpha,anchor,false,interpolator);
        this.easeIn= false;
    }
    
    /**
     * Called from CAAT.Director to use Rotations for taking away or bringing Scenes in.
     * @param time integer indicating time in milliseconds for the Scene to be taken away or brought in.
     * @param alpha boolean indicating whether fading will be applied to the Scene.
     * @param anchor integer indicating the Scene switch anchor.
     * @param interpolator {CAAT.Interpolator} a CAAT.Interpolator to apply to the Scene transition.
     * @param isIn boolean indicating whehter the Scene is brought in.
     */
    public void easeRotation (double time,Boolean alpha,Anchor anchor,boolean isIn,Interpolator interpolator) {
        this.easeContainerBehaviour= new ContainerBehavior();
        
        double start=0;
        double end=0;

        if (anchor == Actor.Anchor.CENTER) {
            anchor = Actor.Anchor.TOP;
        }

        switch(anchor) {
        case CENTER:
            anchor= Anchor.TOP;
        case TOP:
        case BOTTOM:
        case LEFT:
        case RIGHT:
            start= Math.PI * (Math.random()<.5 ? 1 : -1);
            break;
        case TOP_LEFT:
        case TOP_RIGHT:
        case BOTTOM_LEFT:
        case BOTTOM_RIGHT:
            start= Math.PI/2 * (Math.random()<.5 ? 1 : -1);
            break;
        default:
            //alert('rot anchor ?? '+anchor);
            Caatja.alert("rot anchor ??"  + anchor);
        }

        if ( false==isIn ) {
            double tmp= start;
            start=end;
            end= tmp;
        }

        // TODO Check cond
        if ( alpha != null && alpha) {
            this.createAlphaBehaviour(time,isIn);
        }
        
        Pt anchorPercent= this.getAnchorPercent(anchor.getValue());
        
        RotateBehavior rb= new RotateBehavior();
        rb.setFrameTime( 0, time ).
        setValues(start, end, anchorPercent.x, anchorPercent.y);

        if ( interpolator != null) {
            rb.setInterpolator(interpolator);
        }
        this.easeContainerBehaviour.addBehavior(rb);
        
        
        this.easeContainerBehaviour.setFrameTime( this.time, time );
        
        // TODO Change bt me
        this.easeContainerBehaviour.addListener(behaviorListener);
        // this.easeContainerBehaviour.addListener(this);
        
        this.emptyBehaviorList();
        super.addBehavior(easeContainerBehaviour);
    }
    
    /**
     * Registers a listener for listen for transitions events.
     * Al least, the Director registers himself as Scene easing transition listener.
     * When the transition is done, it restores the Scene's capability of receiving events.
     * @param listener {function(caat_behavior,time,actor)} an object which contains a method of the form <code>
     * behaviorExpired( caat_behaviour, time, actor);
     */
    public void setEaseListener (Director listener ) {
        this.easeContainerBehaviourListener=listener;
    }
    
    // TODO Check which method is ok ???
    /**
     * Private.
     * listener for the Scene's easeContainerBehaviour.
     * @param actor
     */
    public void behaviorExpired (Actor actor) {
        this.easeContainerBehaviourListener.easeEnd(this, this.easeIn);
    }
    
    /**
     * Private.
     * listener for the Scene's easeContainerBehaviour.
     * @param actor
     */
//    @Override
//    public void behaviorExpired(BaseBehavior behaviour, double time, Actor actor) {
//        // TODO Check
//        this.easeContainerBehaviourListener.easeEnd(this, this.easeIn);
//    }
    
    /**
     * This method should be overriden in case the developer wants to do some special actions when
     * the scene has just been brought in.
     * 
     */
    public void activated () {
        
    }
    
    /**
     * Scenes, do not expire the same way Actors do.
     * It simply will be set expired=true, but the frameTime won't be modified.
     */
    @Override
    public Actor setExpired(double time) {
        // TODO Check ???
//        this.expired = expired;
        return this;
    }
    
    // TODO Check ???
    public Scene setExpired(boolean bExpired) {
        this.expired = bExpired;
        return this;
    }

    /**
     * A scene by default does not paint anything because has not fillStyle set.
     * @param director
     * @param time
     */
    @Override
    public void paint(Director director, double time) {
        if (this.fillStyle != null) {
            CaatjaContext2d ctx= director.ctx;
            ctx.setFillStyle(this.fillStyle!=null ? this.fillStyle : CaatjaColor.valueOf("white"));
            ctx.fillRect(0,0,this.width,this.height );
        }
    }
    
    // Add by me
    private List<List<Actor>> inputList;
    
    /**
     * Find a pointed actor at position point.
     * This method tries lo find the correctly pointed actor in two different ways.
     *  + first of all, if inputList is defined, it will look for an actor in it.
     *  + if no inputList is defined, it will traverse the scene graph trying to find a pointed actor.
     * @param point <CAAT.Point>
     */
    public Actor findActorAtPosition (Pt point) {
        int i,j;

        Pt p= new Pt();

        if ( this.inputList != null) {
            List<List<Actor>> il= this.inputList;
            for( i=0; i<il.size(); i++ ) {
                List<Actor> ill= il.get(i);
                for( j=0; j<ill.size(); j++ ) {
                    if ( ill.get(j).visible ) {
                        p.set(point.x, point.y);
                        Matrix modelViewMatrixI= ill.get(j).worldModelViewMatrix.getInverse();
                        modelViewMatrixI.transformCoord(p);
                        if ( ill.get(j).contains(p.x, p.y) ) {
                            return ill.get(j);
                        }
                    }
                }
            }
        }

        p.set(point.x, point.y);
        return super.findActorAtPosition(p);
    }

    /**
     * Enable a number of input lists.
     * These lists are set in case the developer doesn't want the to traverse the scene graph to find the pointed
     * actor. The lists are a shortcut whete the developer can set what actors to look for input at first instance.
     * The system will traverse the whole lists in order trying to find a pointed actor.
     *
     * Elements are added to each list either in head or tail.
     *
     * @param size <number> number of lists.
     */
    public Scene enableInputList (int size ) {
        this.inputList= new ArrayList<List<Actor>>();
        for( int i=0; i<size; i++ ) {
            this.inputList.add(new ArrayList<Actor>());
        }

        return this;
    }

    /**
     * Add an actor to a given inputList.
     * @param actor <CAAT.Actor> an actor instance
     * @param index <number> the inputList index to add the actor to. This value will be clamped to the number of
     * available lists.
     * @param position <number> the position on the selected inputList to add the actor at. This value will be
     * clamped to the number of available lists.
     */
    public Scene addActorToInputList (Actor actor, int index, Integer position ) {
        if ( index<0 ) index=0; else if ( index>=this.inputList.size() ) index= this.inputList.size()-1;
        List<Actor> il= this.inputList.get(index);

        if ( position== null || position>=il.size() ) {
            il.add( actor );
        } else if (position<=0) {
            il.set(0, actor );
        } else {
            il.set(position, actor);
        }

        return this;
    }
    
    // Add by me
    public Scene addActorToInputList (Actor actor, int index) {
        return addActorToInputList(actor, index, null);
    }

    /**
     * Remove all elements from an input list.
     * @param index <number> the inputList index to add the actor to. This value will be clamped to the number of
     * available lists so take care when emptying a non existant inputList index since you could end up emptying
     * an undesired input list.
     */
    public Scene emptyInputList (int index ) {
        if ( index<0 ) index=0; else if ( index>=this.inputList.size() ) index= this.inputList.size()-1;
        this.inputList.set(index, new ArrayList<Actor>());
        return this;
    }

    /**
     * remove an actor from a given input list index.
     * If no index is supplied, the actor will be removed from every input list.
     * @param actor <CAAT.Actor>
     * @param index <!number> an optional input list index. This value will be clamped to the number of
     * available lists.
     */
    public Scene removeActorFromInputList (Actor actor, Integer index ) {
        if ( index == null ) {
            int i,j;
            for( i=0; i<this.inputList.size(); i++ ) {
                List<Actor> il= this.inputList.get(i);
                for( j=0; j<il.size(); j++ ) {
                    if ( il.get(j).equals(actor)) {
                        // TODO Check
                        il.remove( j);
                    }
                }
            }
            return this;
        }

        if ( index<0 ) index=0; else if ( index>=this.inputList.size() ) index= this.inputList.size()-1;
        List<Actor> il= this.inputList.get(index);
        for(int j=0; j<il.size(); j++ ) {
            if ( il.get(j).equals(actor)) {
                // TODO Check
                il.remove( j);
            }
        }

        return this;
    }
    
    public void getIn(Scene out_scene ) {

    }

    public void goOut(Scene in_scene ) {

    }

    
    // Add by me
    @Override
    public Scene setBounds(double x, double y, double w, double h) {
        return (Scene) super.setBounds(x, y, w, h);
    }

//    @Override
//    public void behaviorApplied(BaseBehavior behavior, double time, double normalizeTime, Actor actor, SetForTimeReturnValue value) {
//        
//    }
//    
//    @Override
//    public void behaviorStarted(BaseBehavior behavior, double time, Actor actor) {
//        
//    }

    @Override
    public Scene setFillStyle(String fillStyle) {
        return (Scene) super.setFillStyle(fillStyle);
    }

    @Override
    public Scene setFillStrokeStyle(CaatjaColor style) {
        return (Scene) super.setFillStrokeStyle(style);
    }
    
    private Actor loadingActor;
    
    protected void loading() throws Exception {
        if (loadingActor == null) {
            this.loadingActor = Caatja.getLoading().getLoadingActor();
            this.addChild(this.loadingActor);
        }
    }
    
    protected void stopLoading() {
        if (loadingActor != null) {
            this.removeChild(loadingActor);
            loadingActor = null;
        }
    }

}
