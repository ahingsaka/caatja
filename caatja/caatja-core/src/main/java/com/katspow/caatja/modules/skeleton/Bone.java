package com.katspow.caatja.modules.skeleton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.katspow.caatja.behavior.BaseBehavior;
import com.katspow.caatja.behavior.BehaviorListener;
import com.katspow.caatja.behavior.ContainerBehavior;
import com.katspow.caatja.behavior.Interpolator;
import com.katspow.caatja.behavior.PathBehavior;
import com.katspow.caatja.behavior.RotateBehavior;
import com.katspow.caatja.behavior.ScaleBehavior;
import com.katspow.caatja.behavior.SetForTimeReturnValue;
import com.katspow.caatja.core.canvas.CaatjaContext2d;
import com.katspow.caatja.foundation.actor.Actor;
import com.katspow.caatja.math.Pt;
import com.katspow.caatja.math.matrix.Matrix;
import com.katspow.caatja.pathutil.Path;

/**
 * @name Skeleton
 * @memberof CAAT.Module
 * @namespace
 */

/**
 * @name Bone
 * @memberof CAAT.Module.Skeleton
 * @constructor
 */
public class Bone {
    public static final Pt defPoint = new Pt(0, 0 );
    
    // TODO Check
    //var defScale = { scaleX: 1, scaleY: 1 };
    public static final Pt defScale = new Pt(1, 1);
    public static final double defAngle = 0;

    double cangle;
    Pt cscale;
    Pt cpoint;

        String id = null;

        double wx = 0;
        double wy = 0;
        double wrotationAngle = 0;
        double wscaleX = 0;
        double wscaleY = 0;

        /**
         * Bone x position relative parent
         * @type number
         */
        int x = 0;

        /**
         * Bone y position relative parent
         * @type {number}
         */
        int y = 0;

        double positionAnchorX = 0;
        double positionAnchorY = 0;

        /**
         * Bone rotation angle
         * @type {number}
         */
        double rotationAngle = 0;
        double rotationAnchorX = 0;
        double rotationAnchorY = 0.5;

        double scaleX = 1;
        double scaleY = 1;
        double scaleAnchorX = .5;
        double scaleAnchorY = .5;

        /**
         * Bone size.
         * @type number
         */
        int size = 0;

        /**
         * @type CAAT.Math.Matrix
         */
        Matrix matrix = null;

        /**
         * @type CAAT.Math.Matrix
         */
        Matrix wmatrix = null;

        /**
         * @type CAAT.Skeleton.Bone
         */
        Bone parent = null;

        /**
         * @type CAAT.Behavior.ContainerBehavior
         */
        ContainerBehavior keyframesTranslate = null;

        /**
         * @type CAAT.PathUtil.Path
         */
        Path keyframesTranslatePath = null;

        /**
         * @type CAAT.Behavior.ContainerBehavior
         */
        ContainerBehavior keyframesScale = null;

        /**
         * @type CAAT.Behavior.ContainerBehavior
         */
        ContainerBehavior keyframesRotate = null;
        
        /**
         * @type object
         */
        Map<String, AnimData> keyframesByAnimation = null;
        
        AnimData currentAnimation = null;

        /**
         * @type Array.<CAAT.Skeleton.Bone>
         */
        List<Bone> children = null;
        
        double behaviorApplicationTime = -1;
        
        // Add by me
        double width = 0;
        double height = 0;
        
        private class AnimData {
            ContainerBehavior keyframesTranslate;
            ContainerBehavior keyframesScale;
            ContainerBehavior keyframesRotate;
            
            public AnimData(ContainerBehavior keyframesTranslate, ContainerBehavior keyframesScale,
                    ContainerBehavior keyframesRotate) {
                this.keyframesTranslate = keyframesTranslate;
                this.keyframesScale = keyframesScale;
                this.keyframesRotate = keyframesRotate;
            }
            
        }
        
        // FIXME !!!
//        BehaviorListener fntr =  new BehaviorListener() {
//            @Override
//            public void behaviorStarted(BaseBehavior behavior, double time, Actor actor) {
//            }
//            
//            @Override
//            public void behaviorExpired(BaseBehavior behavior, double time, Actor actor) {
//            }
//            
//            @Override
//            public void behaviorApplied(BaseBehavior behavior, double time, double normalizeTime, Actor actor, SetForTimeReturnValue value)
//                    throws Exception {
//                // FIXME Ugly
//               // cpoint= (Pt) value;
//            }
//        };
//        
//        BehaviorListener fnsc = new BehaviorListener() {
//            @Override
//            public void behaviorExpired(BaseBehavior behavior, double time, Actor actor) {
//                
//            }
//
//            @Override
//            public void behaviorApplied(BaseBehavior behavior, double time, double normalizeTime, Actor actor,
//                    SetForTimeReturnValue value) throws Exception {
//             // FIXME Ugly
//               // cscale= (Pt) value;
//            }
//
//            @Override
//            public void behaviorStarted(BaseBehavior behavior, double time, Actor actor) {
//            }
//            
//        };
//        
//        BehaviorListener fnrt = new BehaviorListener() {
//            @Override
//            public void behaviorStarted(BaseBehavior behavior, double time, Actor actor) {
//            }
//            
//            @Override
//            public void behaviorExpired(BaseBehavior behavior, double time, Actor actor) {
//            }
//            
//            @Override
//            public void behaviorApplied(BaseBehavior behavior, double time, double normalizeTime, Actor actor, SetForTimeReturnValue value)
//                    throws Exception {
//             // FIXME Ugly
//               // cangle= (Double) value;
//            }
//        };

        public Bone(String id) {
            this.id= id;
            this.matrix= new Matrix();
            this.wmatrix= new Matrix();
            this.parent= null;
            this.children= new ArrayList<Bone>();

//            this.keyframesTranslate= new ContainerBehavior(true).setCycle(true).setId("keyframes_tr");
//            this.keyframesScale= new ContainerBehavior(true).setCycle(true).setId("keyframes_sc");
//            this.keyframesRotate= new ContainerBehavior(true).setCycle(true).setId("keyframes_rt");
//
//            this.keyframesTranslate.addListener(fntr);
//            this.keyframesScale.addListener( fnsc);
//            this.keyframesRotate.addListener(fnrt);
            
            // FIXME
//            this.keyframesByAnimation = {};

            // NO return
//            return this;
        }
        
        public Bone setBehaviorApplicationTime(double t) {
            this.behaviorApplicationTime= t;
            return this;
        }
        
    public AnimData __createAnimation(String name) {

        ContainerBehavior keyframesTranslate = new ContainerBehavior(true).setCycle(true).setId("keyframes_tr");
        ContainerBehavior keyframesScale = new ContainerBehavior(true).setCycle(true).setId("keyframes_sc");
        ContainerBehavior keyframesRotate = new ContainerBehavior(true).setCycle(true).setId("keyframes_rt");

        // FIXME !!!
        
//        keyframesTranslate.addListener(fntr);
//        keyframesScale.addListener(fnsc);
//        keyframesRotate.addListener(fnrt);

        AnimData animData = new AnimData(keyframesTranslate, keyframesScale, keyframesRotate);

        this.keyframesByAnimation.put(name, animData);

        return animData;
    }

        public AnimData __getAnimation(String name) {
            AnimData animation= this.keyframesByAnimation.get(name);
            if (animation == null) {
                animation= this.__createAnimation(name);
            }

            return animation;
        }

        /**
         *
         * @param parent {CAAT.Skeleton.Bone}
         * @returns {*}
         */
        public Bone __setParent (Bone parent ) {
            this.parent= parent;
            return this;
        }

        public Bone addBone (Bone bone ) {
            this.children.add(bone);
            bone.__setParent(this);
            return this;
        }

        public void __noValue (BaseBehavior keyframes ) {
            keyframes.doValueApplication= false;
            if ( keyframes instanceof ContainerBehavior ) {
                this.__noValue( keyframes );
            }
        }

        /**
         *
         * @param keyframes {CAAT.Behavior.ContainerBehavior}
         * @returns {*}
         */
        public Bone setTranslationKeyframes (ContainerBehavior keyframes ) {
            this.keyframesTranslate= keyframes;
            this.__noValue( keyframes );
            return this;
        }

        public void __setInterpolator (BaseBehavior behavior, int[] curve) {
            // TODO Check
            if (curve != null ) { //&& !curve.equals("stepped")) {
                behavior.setInterpolator(
                        Interpolator.createQuadricBezierInterpolator(
                                new Pt(0,0),
                                new Pt(curve[0], curve[1]),
                                new Pt(curve[2], curve[3]),
                                false
                        )
                );
            }
        }

        /**
         * @param name {string} keyframe animation name
         * @param angleStart {number} rotation start angle
         * @param angleEnd {number} rotation end angle
         * @param timeStart {number} keyframe start time
         * @param timeEnd {number} keyframe end time
         * @param curve {Array.<number>=} 4 numbers definint a quadric bezier info. two first points
         *  assumed to be 0,0.
         */
        public void addRotationKeyframe (String name, double angleStart, double angleEnd, double timeStart, double timeEnd, int[] curve ) {

            double as= 2*Math.PI*angleStart/360;
            double ae= 2*Math.PI*angleEnd/360;
            
            // minimum distant between two angles.

            if ( as<-Math.PI ) {
                if (Math.abs(as+this.rotationAngle)>2*Math.PI) {
                    as= -(as+Math.PI);
                } else {
                    as= (as+Math.PI);
                }
            } else if (as > Math.PI) {
                as -= 2 * Math.PI;
            }

            if ( ae<-Math.PI ) {

                if (Math.abs(ae+this.rotationAngle)>2*Math.PI) {
                    ae= -(ae+Math.PI);
                } else {
                    ae= (ae+Math.PI);
                }
            } else if ( ae>Math.PI ) {
                ae-=2*Math.PI;
            }

            angleStart= -as;
            angleEnd= -ae;

            RotateBehavior behavior= new RotateBehavior().
                    setFrameTime( timeStart, timeEnd-timeStart+1).
                    setValues( angleStart, angleEnd, 0d, .5).
                    setValueApplication(false);

            this.__setInterpolator( behavior, curve );
            
            AnimData animation= this.__getAnimation(name);
            animation.keyframesRotate.addBehavior(behavior);
        }

        public void endRotationKeyframes () {

        }

        public void addTranslationKeyframe (String name, int startX, int startY, int endX, int endY, double timeStart, double timeEnd, int[] curve ) {
            PathBehavior behavior= new PathBehavior().
                setFrameTime( timeStart, timeEnd-timeStart+1).
                setValues( new Path().
                    setLinear( startX, startY, endX, endY )
                ).
                setValueApplication(false);

            this.__setInterpolator( behavior, curve );

            AnimData animation= this.__getAnimation(name);
            animation.keyframesTranslate.addBehavior( behavior );
        }
        
        public void addScaleKeyframe(String name, int scaleX, int endScaleX, int scaleY, int endScaleY, double timeStart, double timeEnd, int[] curve ) {
            ScaleBehavior behavior= new ScaleBehavior().
                setFrameTime( timeStart, timeEnd-timeStart+1).
                setValues( scaleX, endScaleX, scaleY, endScaleY ).
                setValueApplication(false);

            this.__setInterpolator( behavior, curve );

            AnimData animation= this.__getAnimation(name);
            animation.keyframesScale.addBehavior( behavior );
        }

        public void endTranslationKeyframes (String name) {

        }

        public void setSize (int s) {
            this.width= s;
            this.height= 0;
        }

        public void endScaleKeyframes (String name) {

        }
        
        public Bone setPosition(int x, int y) {
            this.x= x;
            this.y= -y;
            return this;
        }
        
        /**
         * default anchor values are for spine tool.
         * @param angle {number}
         * @param anchorX {number=}
         * @param anchorY {number=}
         * @returns {*}
         */
        public Bone setRotateTransform (double angle, Double anchorX, Double anchorY ) {
            this.rotationAngle= -angle*2*Math.PI/360;
            this.rotationAnchorX= anchorX!=null ? anchorX : 0;
            this.rotationAnchorY= anchorY!=null ? anchorY : .5;
            return this;
        }

        /**
         *
         * @param sx {number}
         * @param sy {number}
         * @param anchorX {number=} anchorX: .5 by default
         * @param anchorY {number=} anchorY. .5 by default
         * @returns {*}
         */
        public Bone setScaleTransform (double sx, double sy, Double anchorX, Double anchorY ) {
            this.scaleX= sx;
            this.scaleY= sy;
            this.scaleAnchorX= anchorX!= null ? anchorX : .5;
            this.scaleAnchorY= anchorY!= null ? anchorY : .5;
            return this;
        }


        public void __setModelViewMatrix () {
            double c, s, _m00, _m01, _m10, _m11;
            double mm0, mm1, mm2, mm3, mm4, mm5;
//            var mm;

            double[] mm = this.matrix.matrix;

            mm0 = 1;
            mm1 = 0;
            mm3 = 0;
            mm4 = 1;

            mm2 = this.wx - this.positionAnchorX * this.width;
            mm5 = this.wy - this.positionAnchorY * this.height;

            if (this.wrotationAngle != 0) {

                double rx = this.rotationAnchorX * this.width;
                double ry = this.rotationAnchorY * this.height;

                mm2 += mm0 * rx + mm1 * ry;
                mm5 += mm3 * rx + mm4 * ry;

                c = Math.cos(this.wrotationAngle);
                s = Math.sin(this.wrotationAngle);
                _m00 = mm0;
                _m01 = mm1;
                _m10 = mm3;
                _m11 = mm4;
                mm0 = _m00 * c + _m01 * s;
                mm1 = -_m00 * s + _m01 * c;
                mm3 = _m10 * c + _m11 * s;
                mm4 = -_m10 * s + _m11 * c;

                mm2 += -mm0 * rx - mm1 * ry;
                mm5 += -mm3 * rx - mm4 * ry;
            }
            if (this.wscaleX != 1 || this.wscaleY != 1) {

                double sx = this.scaleAnchorX * this.width;
                double sy = this.scaleAnchorY * this.height;

                mm2 += mm0 * sx + mm1 * sy;
                mm5 += mm3 * sx + mm4 * sy;

                mm0 = mm0 * this.wscaleX;
                mm1 = mm1 * this.wscaleY;
                mm3 = mm3 * this.wscaleX;
                mm4 = mm4 * this.wscaleY;

                mm2 += -mm0 * sx - mm1 * sy;
                mm5 += -mm3 * sx - mm4 * sy;
            }

            mm[0] = mm0;
            mm[1] = mm1;
            mm[2] = mm2;
            mm[3] = mm3;
            mm[4] = mm4;
            mm[5] = mm5;

            if (this.parent != null) {
                this.wmatrix.copy(this.parent.wmatrix);
                this.wmatrix.multiply(this.matrix);
            } else {
                this.wmatrix.identity();
            }
        }
        
        public void setAnimation(String name) {
            AnimData animation= this.keyframesByAnimation.get(name);
            if (animation != null) {
                this.keyframesRotate= animation.keyframesRotate;
                this.keyframesScale= animation.keyframesScale;
                this.keyframesTranslate= animation.keyframesTranslate;
            }

            for (Bone bone : this.children) {
                bone.setAnimation(name);
            }
        }

        /**
         * @param time {number}
         * @throws Exception 
         */
        public void apply (double time, double animationTime) throws Exception {

            cpoint= defPoint;
            cangle= defAngle;
            cscale= defScale;

            if (this.keyframesTranslate != null) {
                this.keyframesTranslate.apply(time, null);
            }
            
            if ( this.keyframesRotate != null) {
                this.keyframesRotate.apply(time, null);
            }
            
            if ( this.keyframesScale != null) {
                this.keyframesScale.apply(time, null);
            }

            this.wx= cpoint.x + this.x;
            this.wy= cpoint.y + this.y;

            this.wrotationAngle = cangle + this.rotationAngle;

            // TODO cscale.scaleX
            this.wscaleX= cscale.x * this.scaleX;
            
            // TODO cscale.scaleY
            this.wscaleY= cscale.y * this.scaleY;

            this.__setModelViewMatrix();

            for( int i=0; i<this.children.size(); i++ ) {
                // TODO Check
                this.children.get(i).apply(time, animationTime);
            }
        }

        public void transformContext (CaatjaContext2d ctx) {
            double[] m= this.wmatrix.matrix;
            ctx.transform( m[0], m[3], m[1], m[4], m[2], m[5] );
        }

        public void paint (Matrix actorMatrix, CaatjaContext2d ctx ) {
            ctx.save();
                this.transformContext(ctx);

                ctx.setStrokeStyle("blue");
                ctx.beginPath();
                ctx.moveTo(0,-2);
                ctx.lineTo(this.width,this.height);
                ctx.lineTo(0,2);
                ctx.lineTo(0,-2);
                ctx.stroke();
            ctx.restore();

            for( int i=0; i<this.children.size(); i++ ) {
                this.children.get(i).paint(actorMatrix, ctx);
            }


        }

}
