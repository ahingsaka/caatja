package com.katspow.caatja.foundation.actor;

import java.util.Arrays;
import java.util.List;

import com.katspow.caatja.core.CAAT;
import com.katspow.caatja.core.canvas.CaatjaContext2d;
import com.katspow.caatja.foundation.Director;
import com.katspow.caatja.foundation.image.CompoundImage;

public class SpriteActor extends ActorContainer {

    /**
    *
    * <p>
    * This class defines a simple Sprite sheet. A Sprite sheet is given by an instance of CAAT.CompoundImage,
    * which given a single image, you can define rows by columns sub-images contained in it. Then an array
    * of values indicating indexes to these sub-images is used to draw the sprite.
    *
    * <p>
    * The following is a valid example of CAAT.SpriteActor declaration:
    * <p>
    * <code>
    *  // define a compound image as 1 row by 3 columns.<br>
    *  var conpoundimage = new CAAT.CompoundImage().initialize( director.getImage('fish'), 1, 3);<br>
    *  <br>
    *  // create a fish instance<br>
    *  var fish = new CAAT.SpriteActor().<br>
    *  &nbsp;&nbsp;create().<br>
    *  &nbsp;&nbsp;setAnimationImageIndex( [0,1,2,1] ).// rotating from subimages 0,1,2,1<br>
    *  &nbsp;&nbsp;setSpriteImage(conpoundimage).      // throughtout this compound image<br>
    *  &nbsp;&nbsp;setChangeFPS(350).                  // and change from image on the sheet every 350ms.<br>
    *  &nbsp;&nbsp;setLocation(10,10);                 // btw, the fish actor will be at 10,10 on screen.<br>
    * <br>
    * </code><br>
    *
    * @constructor
    * @extends CAAT.ActorContainer
    *
    */
    @Deprecated
    public SpriteActor() {
        super();
        this.glEnabled = true;
        this.setAnimationImageIndex(Arrays.asList(0));
    }

    private CompoundImage compoundbitmap = null;
    private List<Integer> animationImageIndex = null; // an Array defining the sprite frame sequence
    private double prevAnimationTime = -1; 
    public int changeFPS = 1000; // how much Scene time to take before changing an Sprite frame.
    public Tr transformation = Tr.NONE;
    protected int spriteIndex = 0;  // the current sprite frame

    // constants used to determine how to draw the sprite image,
    public enum Tr {
        NONE(0), FLIP_HORIZONTAL(1), FLIP_VERTICAL(2), FLIP_ALL(3);

        private int val;

        private Tr(int val) {
            this.val = val;
        }

        public int getVal() {
            return val;
        }
    }

    /**
     * Sets the Sprite image. The image will be treated as an array of rows by columns sub-images.
     *
     * @see CAAT.CompoundImage
     * @param conpoundimage a CAAT.ConpoundImage object instance.
     * @return this
     */
    public SpriteActor setSpriteImage(CompoundImage conpoundimage) {
        this.compoundbitmap = conpoundimage;
        this.width = conpoundimage.singleWidth;
        this.height = conpoundimage.singleHeight;
        
        return this;
    }
    
    /**
     * Set the elapsed time needed to change the image index.
     * @param fps an integer indicating the time in milliseconds to change.
     * @return this
     */
    @Override
    public SpriteActor setChangeFPS(int fps) {
        this.changeFPS = fps;
        return this;
    }
    
    /**
     * Set the transformation to apply to the Sprite image.
     * Any value of
     *  <li>TR_NONE
     *  <li>TR_FLIP_HORIZONTAL
     *  <li>TR_FLIP_VERTICAL
     *  <li>TR_FLIP_ALL
     *
     * @param transformation an integer indicating one of the previous values.
     * @return this
     */
    public SpriteActor setSpriteTransformation(Tr transformation) {
        this.transformation = transformation;
        
        // TODO Cannot do this in Java
//        switch(transformation)	{
//		case this.TR_FLIP_HORIZONTAL:
//			this.compoundbitmap.paint= this.compoundbitmap.paintInvertedH;
//			break;
//		case this.TR_FLIP_VERTICAL:
//			this.compoundbitmap.paint= this.compoundbitmap.paintInvertedV;
//			break;
//		case this.TR_FLIP_ALL:
//			this.compoundbitmap.paint= this.compoundbitmap.paintInvertedHV;
//			break;
//		default:
//			this.compoundbitmap.paint= this.compoundbitmap.paintN;
//	}
        
        return this;
    }

    /**
     * Set the sprite animation images index.
     *
     * @param aAnimationImageIndex an array indicating the Sprite's frames.
     */
    public SpriteActor setAnimationImageIndex(List<Integer> aAnimationImageIndex) {
        this.animationImageIndex = aAnimationImageIndex;
        this.spriteIndex = aAnimationImageIndex.get(0);
        return this;
    }
    
    public SpriteActor setSpriteIndex(int index) {
        this.spriteIndex = index;
        return this;
    }

//    /**
//     * Customization of the default CAAT.Actor.animate method.
//     *
//     * @param director the CAAT.Director object instance that contains the Scene the Actor is in.
//     * @param time an integer indicating the Scene time when the bounding box is to be drawn.
//     *
//     * @return boolean
//     */
//    public boolean animate(Director director, double time) {
//
//        if (this.compoundbitmap != null && this.animationImageIndex != null) {
//
//            if (this.animationImageIndex.size() > 1) {
//                if (this.prevAnimationTime == -1) {
//                    this.prevAnimationTime = time;
//                } else {
//                    double ttime = time;
//                    ttime -= this.prevAnimationTime;
//                    ttime /= this.changeFPS;
//                    ttime %= this.animationImageIndex.size();
//                    // TODO Check round
//                    // Add by me
//                    int floor = (int)Math.floor(ttime);
//                    if (floor < 0) {
//                        floor = 0;
//                    }
//                    this.spriteIndex = this.animationImageIndex.get(floor);
//                }
//            }
//
//            return super.animate(director, time);
//        }
//        
//        return false;
//    }

    /**
     * Draws the sprite image calculated and stored in spriteIndex.
     *
     * @param director the CAAT.Director object instance that contains the Scene the Actor is in.
     * @param time an integer indicating the Scene time when the bounding box is to be drawn.
     */
    public void paint(Director director, double time) {
        
    	// TODO Check, Replace with setSpriteTransformation() ??
//        if ( -1==this.spriteIndex ) {
//            return;
//        }

        CaatjaContext2d canvas = director.ctx;

        // drawn at 0,0 because they're already affine-transformed.
        switch (this.transformation) {
        case FLIP_HORIZONTAL:
            this.compoundbitmap.paintInvertedH(canvas, this.spriteIndex, 0, 0);
            break;
        case FLIP_VERTICAL:
            this.compoundbitmap.paintInvertedV(canvas, this.spriteIndex, 0, 0);
            break;
        case FLIP_ALL:
            this.compoundbitmap.paintInvertedHV(canvas, this.spriteIndex, 0, 0);
            break;
        default:
        	
        	if ( this.animationImageIndex.size()>1 ) {
                if ( this.prevAnimationTime==-1 )	{
                    this.prevAnimationTime= time;
                }
                else	{
                    double ttime= time;
                    ttime-= this.prevAnimationTime;
                    ttime/= this.changeFPS;
                    ttime%= this.animationImageIndex.size();
                    this.spriteIndex= this.animationImageIndex.get((int)Math.floor(ttime));
                }
            }

        	// TODO Check
//			Context2d canvas= director.ctx;
        	
            this.compoundbitmap.paint(canvas, this.spriteIndex, 0, 0);
        }

    }
    
    public boolean paintActorGL(Director director, double time) {
        if ( -1==this.spriteIndex ) {
            // TODO Check return type
            return true;
        }

        return super.paintActorGL(director,time);
    }
    
    /**
    *
    * @param uv {Float32Array}
    * @param uvIndex {Number}
    * 
    * FIXME
    */
//   public void setUV (Float32Array uv, int uvIndex ) {
//       this.compoundbitmap.setUV(this.spriteIndex, uv, uvIndex);
//   }
   
   public boolean glNeedsFlush (Director director) {
       if ( this.compoundbitmap.image.__texturePage!=director.currentTexturePage ) {
           return true;
       }
       if ( this.frameAlpha!=director.currentOpacity ) {
           return true;
       }
       return false;
   }

   // Add by me

    @Override
    public SpriteActor enableEvents(boolean enable) {
        return (SpriteActor) super.enableEvents(enable);
    }

    @Override
    public SpriteActor setBounds(double x, double y, double w, double h) {
        return (SpriteActor) super.setBounds(x, y, w, h);
    }

    @Override
    public SpriteActor setId(String id) {
        return (SpriteActor) super.setId(id);
    }

    @Override
    public SpriteActor setLocation(double x, double y) {
        return (SpriteActor) super.setLocation(x, y);
    }
    
    
}
