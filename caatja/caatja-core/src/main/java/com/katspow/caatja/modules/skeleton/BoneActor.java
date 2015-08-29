package com.katspow.caatja.modules.skeleton;

import java.util.ArrayList;
import java.util.List;

import com.katspow.caatja.core.canvas.CaatjaContext2d;
import com.katspow.caatja.foundation.Director;
import com.katspow.caatja.foundation.actor.Actor;

/**
 * FIXME 
 *
 */
/**
 * @name BoneActor
 * @memberof CAAT.Module.Skeleton
 * @constructor
 */
public class BoneActor extends Actor {
    
    private Bone bone    = null;
    private List<Object> skinInfo = null;
//  private Map<String, Object> skinInfoByName = null;
//    currentSkinInfo = null;
    private List<SkinDataFrame> skinDataKeyframes = null;
    
    // Add by me
    private class SkinDataFrame {
        String name;
        double start;
        double duration;
        
        public SkinDataFrame(String name, double start, double duration) {
            this.name = name;
            this.start = start;
            this.duration = duration;
        }
    }

    public BoneActor() {
        super();
        this.skinInfo= new ArrayList<Object>();
//        this.skinInfoByName= new HashMap<String, Object>();
        this.skinDataKeyframes= new ArrayList<BoneActor.SkinDataFrame>();
        this.setSize(1,1);
    }

    public BoneActor setBone (Bone bone) {
        this.bone= bone;
        return this;
    }

//    public BoneActor addSkinInfo ( si ) {
//         if (null===this.currentSkinInfo) {
//                this.currentSkinInfo= si;
//            }
//            this.skinInfo.push( si );
//            this.skinInfoByName[ si.name ]= si;
//            return this;
//    }
    
    public BoneActor setDefaultSkinInfoByName(String name ) {
//        var v= this.skinInfoByName.get(name);
//        if (v != null) {
//            this.currentSkinInfo= v;
//        }

        return this;
    }
    
    public void emptySkinDataKeyframe() {
        this.skinDataKeyframes.clear();
    }
    
    public void addSkinDataKeyframe(String name, double start, double duration ) {
        this.skinDataKeyframes.add(new SkinDataFrame(
            name,
            start,
            duration
        ));
    }
    
//    public __getCurrentSkinInfo(double time) {
//    if ( this.skinDataKeyframes.length ) {
//        time=(time%1000)/1000;
//
//        for( var i=0, l=this.skinDataKeyframes.length; i<l; i+=1 ) {
//            var sdkf= this.skinDataKeyframes[i];
//            if ( time>=sdkf.start && time<=sdkf.start+sdkf.duration ) {
//                return this.currentSkinInfo= this.skinInfoByName[ sdkf.name ];
//            }
//        }
//
//        return null;
//    }
//
//    return this.currentSkinInfo;
//    }


    public void paint (Director director, double time ) {
        CaatjaContext2d ctx= director.ctx;

//            var skinInfo= this.__getCurrentSkinInfo(time);
//
//            if (skinInfo == null || skinInfo.image == null) {
//                return;
//            }
//
//            double w= skinInfo.width*.5;
//            double h= skinInfo.height*.5;
//
//            ctx.save();
//                ctx.translate(-w+skinInfo.x, -h+skinInfo.y );
//
//                ctx.translate(w, h);
//                ctx.rotate(skinInfo.angle);
//                ctx.translate( -w, -h);
//
//                ctx.drawImage( skinInfo.image, 0, 0, skinInfo.image.width, skinInfo.image.height );
//            ctx.restore();

    }

    @Override
    public Actor setModelViewMatrix () {
        this.modelViewMatrix.copy( this.bone.wmatrix );

        if (this.parent != null) {

            this.isAA = false;
            this.worldModelViewMatrix.copy(this.parent.worldModelViewMatrix);
            this.worldModelViewMatrix.multiply(this.modelViewMatrix);
            this.wdirty = true;

        } else {
            if (this.dirty) {
                this.wdirty = true;
            }

            this.worldModelViewMatrix.identity();
            this.isAA = this.rotationAngle == 0 && this.scaleX == 1 && this.scaleY == 1;
        }
        
        // Add by me
        return this;

    }

}
