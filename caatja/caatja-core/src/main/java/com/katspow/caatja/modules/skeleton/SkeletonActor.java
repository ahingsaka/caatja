package com.katspow.caatja.modules.skeleton;

import java.awt.Canvas;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.katspow.caatja.foundation.Director;
import com.katspow.caatja.foundation.actor.Actor;

/**
 * FIXME
 *
 */
public class SkeletonActor extends Actor {
    
    /**
     * Holder to keep animation slots information.
     */
    private class SlotInfoData {
        String sortId;
        String attachment;
        String name;
        Bone bone;
        
        public SlotInfoData(String sortId, String attachment, String name, Bone bone) {
            super();
            this.sortId = sortId;
            this.attachment = attachment;
            this.name = name;
            this.bone = bone;
        }
    }
    
    /**
     * @lends CAAT.Module.Skeleton.SkeletonActor
     */
    private Skeleton skeleton = null;
    
    /**
     * @type object
     * @map < boneId{string}, SlotInfoData >
     */
     Map<String, SlotInfoData> slotInfo = null;
     
     /**
      * @type Array.<SlotInfoData>
      */
    List<SlotInfoData> slotInfoArray = null;
    
    /**
     * @type object
     * @map
     */
//    skinByName : null;
    
    /**
     * @type CAAT.Foundation.Director
     */
    private Director director = null;
    
    /**
     * @type boolean
     */
    private boolean _showBones = false;

    /**
     * Currently selected animation play time.
     * Zero to make it last for its default value.
     * @type number
     */
    double animationDuration = 0;
    
    boolean showAABB = false;
    List<Actor> bonesActor = null;
    
    public SkeletonActor(Director director, Skeleton skeleton ) {
        super();

        this.director= director;
        this.skeleton= skeleton;
        this.slotInfo= new HashMap<String, SkeletonActor.SlotInfoData>();
        this.slotInfoArray= new ArrayList<SkeletonActor.SlotInfoData>();
        this.bonesActor = new ArrayList<Actor>();
//        this.skinByName= {};
        
//        this.setSkin( );
        // TODO Check
        this.setAnimation("default", 0);
        
        // No return
//        return this;
    }
    
    public SkeletonActor showBones(boolean show) {
        this._showBones = show;
        return this;
    }
    
    /**
     * build an sprite-sheet composed of numSprites elements and organized in rows x columns
     * @param numSprites {number}
     * @param rows {number=}
     * @param columns {number=}
     */
    public Canvas buildSheet(int numSprites, int rows, int columns ) {

//        var i, j,l;
//        var AABBs= [];
//        var maxTime= 1000;  // BUGBUG search for animation time.
//        var ssItemWidth, ssItemHeight;  // sprite sheet item width and height
//        var ssItemMinX= Number.MAX_VALUE, ssItemMinY= Number.MAX_VALUE;
//        var ssItemMaxOffsetY, ssItemMaxOffsetX;
//
//        // prepare this actor's world model view matrix, but with no position.
//        var px= this.x;
//        var py= this.y;
//        this.x= this.y= 0;
//        this.setModelViewMatrix();
//
//
//        rows= rows || 1;
//        columns= columns || 1;
//
//        // calculate all sprite sheet frames aabb.
//        for( j=0; j<numSprites; j++ ) {
//            var aabb= new CAAT.Math.Rectangle();
//            var time= maxTime/numSprites*j;
//            AABBs.push( aabb );
//            this.skeleton.calculate( time, this.animationDuration );
//
//            for( i= 0, l= this.bonesActor.length; i<l; i+=1 ) {
//                var bone= this.bonesActor[i];
//                var boneAABB;
//                bone.setupAnimation(time);
//                boneAABB= bone.AABB;
//                aabb.unionRectangle(boneAABB);
//                if ( boneAABB.x < ssItemMinX ) {
//                    ssItemMinX= boneAABB.x;
//                }
//            }
//        }
//
//        // calculate offsets for each aabb and sprite-sheet element size.
//        ssItemWidth= 0;
//        ssItemHeight= 0;
//        ssItemMinX= Number.MAX_VALUE;
//        ssItemMinY= Number.MAX_VALUE;
//        for( i=0; i<AABBs.length; i++ ) {
//            if ( AABBs[i].x < ssItemMinX ) {
//                ssItemMinX= AABBs[i].x;
//            }
//            if ( AABBs[i].y < ssItemMinY ) {
//                ssItemMinY= AABBs[i].y;
//            }
//            if ( AABBs[i].width>ssItemWidth ) {
//                ssItemWidth= AABBs[i].width;
//            }
//            if ( AABBs[i].height>ssItemHeight ) {
//                ssItemHeight= AABBs[i].height;
//            }
//        }
//        ssItemWidth= (ssItemWidth|0)+1;
//        ssItemHeight= (ssItemHeight|0)+1;
//
//        // calculate every animation offset against biggest animation size.
//        ssItemMaxOffsetY= -Number.MAX_VALUE;
//        ssItemMaxOffsetX= -Number.MAX_VALUE;
//        var offsetMinX=Number.MAX_VALUE, offsetMaxX=-Number.MAX_VALUE;
//        for( i=0; i<AABBs.length; i++ ) {
//            var offsetX= (ssItemWidth - AABBs[i].width)/2;
//            var offsetY= (ssItemHeight - AABBs[i].height)/2;
//
//            if ( offsetY>ssItemMaxOffsetY ) {
//                ssItemMaxOffsetY= offsetY;
//            }
//
//            if ( offsetX>ssItemMaxOffsetX ) {
//                ssItemMaxOffsetX= offsetX;
//            }
//        }
//
//
//        // create a canvas of the neccessary size
//        var canvas= document.createElement("canvas");
//        canvas.width= ssItemWidth * numSprites;
//        canvas.height= ssItemHeight;
//        var ctx= canvas.getContext("2d");
//
//        // draw animation into canvas.
//        for( j=0; j<numSprites; j++ ) {
//
//            //this.x= j*ssItemWidth + offsetMaxX - ssItemMaxOffsetX ;
//            this.x= j*ssItemWidth - ssItemMinX;
//            this.y= ssItemHeight - ssItemMaxOffsetY/2 - 1;
//
//            this.setModelViewMatrix();
//
//            var time= maxTime/numSprites*j;
//            this.skeleton.calculate( time, this.animationDuration );
//
//            // prepare bones
//            for( i= 0, l= this.bonesActor.length; i<l; i+=1 ) {
//                this.bonesActor[i].setupAnimation(time);
//                this.bonesActor[i].paint( ctx, time );
//            }
//
//            ctx.restore();
//        }
//
//        this.x= px;
//        this.y= py;
//
//        return canvas;
        return null;
    }

    public boolean animate(Director director, double time ) throws Exception {
//        this.skeleton.calculate(time, this.animationDuration);
        return super.animate(director, time);
    }
    
    public void postPaint(Director director, double time ) {

        if (!this._showBones || this.skeleton == null) {
            return;
        }

        this.skeleton.paint(this.worldModelViewMatrix, director.ctx);
    }
    
    public void calculateBoundingBox() {
//        this.skeletonBoundingBox.setEmpty();
    }
    
//    public SkeletonActor setSkin( skin ) {
//
//    this.emptyChildren();
//    this.slotInfoArray = [];
//    this.slotInfo = {};
//
//    var skeletonData = this.skeleton.getSkeletonDataFromFile();
//
//    // slots info
//    for (var slot = 0; slot < skeletonData.slots.length; slot++) {
//        var slotInfo = skeletonData.slots[slot];
//        var bone = this.skeleton.getBoneById(slotInfo.bone);
//        if (bone) {
//            var slotInfoData = new SlotInfoData(
//                    slot,
//                    slotInfo.attachment,
//                    slotInfo.name,
//                    slotInfo.bone );
//
//            this.slotInfo[ bone.id ] = slotInfoData;
//            this.slotInfoArray.push(slotInfoData);
//
//
//            var skinData = null;
//            if (skin) {
//                skinData = skeletonData.skins[skin][slotInfo.name];
//            }
//            if (!skinData) {
//                skinData = skeletonData.skins["default"][slotInfo.name];
//            }
//            if (skinData) {
//
//                //create an actor for each slot data found.
//                var boneActorSkin = new CAAT.Skeleton.BoneActor();
//                boneActorSkin.id = slotInfo.name;
//                boneActorSkin.setBone(bone);
//
//                this.addChild(boneActorSkin);
//                this.skinByName[slotInfo.name] = boneActorSkin;
//
//                // add skining info for each slot data.
//                for (var skinDef in skinData) {
//                    var skinInfo = skinData[skinDef];
//                    boneActorSkin.addSkinInfo({
//                        angle: -(skinInfo.rotation || 0) * 2 * Math.PI / 360,
//                        x: skinInfo.x,
//                        y: -skinInfo.y,
//                        width: skinInfo.width,
//                        height: skinInfo.height,
//                        image: this.director.getImage(skinData[skinDef].name ? skinData[skinDef].name : skinDef),
//                        name: skinDef
//                    });
//                }
//
//                boneActorSkin.setDefaultSkinInfoByName(slotInfo.attachment);
//            }
//        } else {
//            console.log("Unknown bone to apply skin: " + slotInfo.bone);
//        }
//    }
//
//    return this;
//    }

    public SkeletonActor setAnimation(String name, double animationDuration) {
//        this.animationDuration= animationDuration||0;
//
//        var animationInfo = this.skeleton.getAnimationDataByName(name);
//        if (!animationInfo) {
//            return;
//        }
//
//        var animationSlots = animationInfo.slots;
//        for (var animationSlot in animationSlots) {
//            var attachments = animationSlots[animationSlot].attachment;
//            var boneActor = this.skinByName[ animationSlot ];
//            if (boneActor) {
//                boneActor.emptySkinDataKeyframe();
//                for (var i = 0, l = attachments.length - 1; i < l; i += 1) {
//                    var start = attachments[i].time;
//                    var len = attachments[i + 1].time - attachments[i].time;
//                    boneActor.addSkinDataKeyframe(attachments[i].name, start, len);
//                }
//            } else {
//                console.log("Adding skinDataKeyframe to unkown boneActor: " + animationSlot);
//            }
//        }

        return this;
    }

}
