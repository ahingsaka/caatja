package com.katspow.caatja.modules.skeleton;

import java.util.List;
import java.util.Map;

import com.katspow.caatja.core.canvas.CaatjaContext2d;
import com.katspow.caatja.math.matrix.Matrix;

/**
 * FIXME
 *
 */
/**
 * @name Skeleton
 * @memberof CAAT.Module.Skeleton
 * @constructor
 */
public class Skeleton {
    
    /**
     * @lends CAAT.Module.Skeleton.Skeleton.prototype
     */
    Map<String, Bone> bones = null;
    
    List<Bone> bonesArray = null;
//    animation = null;
    Bone root  = null;
    String currentAnimationName = null;
//    skeletonDataFromFile = null;
    
    // Add by me
//    Map<String, Object> animations = null;
    
    public Skeleton() {
        
    }

//    public Skeleton(skeletonDataFromFile) {
//        this.bones= new HashMap<String, Bone>();
//        this.bonesArray= new ArrayList<Bone>();
//        this.animations= new HashMap<String, Object>;
//
//        // bones
//        if (skeletonDataFromFile != null) {
//          this.__setSkeleton(skeletonDataFromFile);
//        }
//    }
    
//    getSkeletonDataFromFile() {
//        return this.skeletonDataFromFile;
//    }
    
//    public void __setSkeleton( skeletonDataFromFile ) {
//        this.skeletonDataFromFile= skeletonDataFromFile;
//        for ( var i=0; i<skeletonDataFromFile.bones.length; i++ ) {
//            var boneInfo= skeletonDataFromFile.bones[i];
//            this.addBone(boneInfo);
//        }
//    }
    
//    setSkeletonFromFile(url) {
//        var me= this;
//        new CAAT.Module.Preloader.XHR().load(
//                function( result, content ) {
//                    if (result==="ok" ) {
//                        me.__setSkeleton( JSON.parse(content) );
//                    }
//                },
//                url,
//                false,
//                "GET"
//        );
//
//        return this;
//    }
    
//    addAnimationFromFile(name, url) {
//        var me= this;
//        new CAAT.Module.Preloader.XHR().load(
//                function( result, content ) {
//                    if (result==="ok" ) {
//                        me.addAnimation( name, JSON.parse(content) );
//                    }
//                },
//                url,
//                false,
//                "GET"
//        );
//
//        return this;
//    }
    
//    public Skeleton addAnimation(String name, animation) {
//
//        // bones animation
//        for( var bonename in animation.bones ) {
//
//            var boneanimation= animation.bones[bonename];
//
//            if ( boneanimation.rotate ) {
//
//                for( var i=0; i<boneanimation.rotate.length-1; i++ ) {
//                    this.addRotationKeyframe(
//                        name,
//                        {
//                            boneId : bonename,
//                            angleStart : boneanimation.rotate[i].angle,
//                            angleEnd : boneanimation.rotate[i+1].angle,
//                            timeStart : boneanimation.rotate[i].time*1000,
//                            timeEnd : boneanimation.rotate[i+1].time*1000,
//                            curve : boneanimation.rotate[i].curve
//                        } );
//                }
//            }
//
//            if (boneanimation.translate) {
//
//                for( var i=0; i<boneanimation.translate.length-1; i++ ) {
//
//                    this.addTranslationKeyframe(
//                        name,
//                        {
//                            boneId      : bonename,
//                            startX      : boneanimation.translate[i].x,
//                            startY      : -boneanimation.translate[i].y,
//                            endX        : boneanimation.translate[i+1].x,
//                            endY        : -boneanimation.translate[i+1].y,
//                            timeStart   : boneanimation.translate[i].time * 1000,
//                            timeEnd     : boneanimation.translate[i+1].time * 1000,
//                            curve       : "stepped" //boneanimation.translate[i].curve
//
//                        });
//                }
//            }
//
//            if ( boneanimation.scale ) {
//                for( var i=0; i<boneanimation.scale.length-1; i++ ) {
//                    this.addScaleKeyframe(
//                        name,
//                        {
//                            boneId : bonename,
//                            startScaleX : boneanimation.rotate[i].x,
//                            endScaleX : boneanimation.rotate[i+1].x,
//                            startScaleY : boneanimation.rotate[i].y,
//                            endScaleY : boneanimation.rotate[i+1].y,
//                            timeStart : boneanimation.rotate[i].time*1000,
//                            timeEnd : boneanimation.rotate[i+1].time*1000,
//                            curve : boneanimation.rotate[i].curve
//                        } );
//                }
//            }
//
//            this.endKeyframes( name, bonename );
//
//        }
//
//        if ( null==this.currentAnimationName ) {
//            this.animations[name]= animation;
//            this.setAnimation(name);
//        }
//
//        return this;
//    }
    
    public void setAnimation(String name) {
        // FIXME
//        this.root.setAnimation( name );
        this.currentAnimationName= name;
    }

//    public getCurrentAnimationData() {
//        return this.animations[ this.currentAnimationName ];
//    }

//    getAnimationDataByName (String name) {
//        return this.animations.get(name);
//    }

    public int getNumBones () {
        return this.bonesArray.size();
    }

    public Bone getRoot () {
        return this.root;   
    }

    public void calculate (double time, double animationTime) throws Exception {
        this.root.apply(time, animationTime);
    }

    public Bone getBoneById (String id) {
        return this.bones.get(id);
    }

    public Bone getBoneByIndex (int index) {
        return this.bonesArray.get( index );
    }

//    public void addBone ( boneInfo ) {
//        Bone bone= new Bone(boneInfo.name);
//
//        bone.setPosition(
//            typeof boneInfo.x!=null ? boneInfo.x : 0,
//            typeof boneInfo.y!=null ? boneInfo.y : 0 );
//        bone.setRotateTransform( boneInfo.rotation ? boneInfo.rotation : 0 );
//        bone.setSize( boneInfo.length ? boneInfo.length : 0, 0 );
//
//        this.bones.put(boneInfo.name, bone);
//
//        if (boneInfo.parent) {
//
//            var parent= this.bones[boneInfo.parent];
//            if ( parent != null) {
//                parent.addBone(bone);
//            } else {
//                CAAT.log("Referenced parent Bone '"+boneInfo.parent+"' which does not exist");
//            }
//        }
//
//        this.bonesArray.add(bone);
//
//        // BUGBUG should be an explicit root bone identification.
//        if (this.root == null) {
//            this.root= bone;
//        }
//    }
//
//    public void addRotationKeyframe (String name, keyframeInfo ) {
//        Bone bone= this.bones.get( keyframeInfo.boneId );
//        if ( bone != null) {
//            bone.addRotationKeyframe(
//                  name,
//                keyframeInfo.angleStart,
//                keyframeInfo.angleEnd,
//                keyframeInfo.timeStart,
//                keyframeInfo.timeEnd,
//                keyframeInfo.curve
//            );
//        } else {
//            console.log("Rotation Keyframe for non-existant bone: '"+keyframeInfo.boneId+"'" );
//        }
//    }
    
//    addScaleKeyframe(String name, keyframeInfo ) {
//        Bone bone= this.bones[ keyframeInfo.boneId ];
//        if ( bone ) {
//            bone.addRotationKeyframe(
//                name,
//                keyframeInfo.startScaleX,
//                keyframeInfo.endScaleX,
//                keyframeInfo.startScaleY,
//                keyframeInfo.endScaleY,
//                keyframeInfo.timeStart,
//                keyframeInfo.timeEnd,
//                keyframeInfo.curve
//            )
//        } else {
//            console.log("Scale Keyframe for non-existant bone: '"+keyframeInfo.boneId+"'" );
//        }
//    }
//
//    public void addTranslationKeyframe (String name, keyframeInfo ) {
//
//        Bone bone= this.bones[ keyframeInfo.boneId ];
//        if ( bone != null) {
//
//            bone.addTranslationKeyframe(
//                  name,
//                keyframeInfo.startX,
//                keyframeInfo.startY,
//                keyframeInfo.endX,
//                keyframeInfo.endY,
//                keyframeInfo.timeStart,
//                keyframeInfo.timeEnd,
//                keyframeInfo.curve
//            )
//        } else {
//            console.log("Translation Keyframe for non-existant bone: '"+keyframeInfo.boneId+"'" );
//        }
//    }

    public void endKeyframes (String name, String boneId ) {
        Bone bone= this.bones.get(boneId);
        if (bone != null) {
            // TODO Check param
            bone.endTranslationKeyframes(null);
            bone.endRotationKeyframes();
            // TODO Check param
            bone.endScaleKeyframes(null);
        }
    }

    public void paint (Matrix actorMatrix, CaatjaContext2d ctx ) {
        this.root.paint(actorMatrix,ctx);
    }

}
