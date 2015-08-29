package com.katspow.caatja.foundation.image;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.katspow.caatja.core.CAAT;
import com.katspow.caatja.core.canvas.CaatjaContext2d;
import com.katspow.caatja.core.canvas.CaatjaImage;
import com.katspow.caatja.foundation.Director;
import com.katspow.caatja.foundation.actor.Actor;
import com.katspow.caatja.math.Rectangle;
import com.katspow.caatja.modules.font.FontData;
import com.katspow.caatja.modules.texturepacker.TexturePage;

public class SpriteImage {

    /**
	 * 
	 * This class is used by CAAT.Actor to draw images. It differs from
	 * CAAT.CompoundImage in that it manages the subimage change based on time
	 * and a list of animation sub-image indexes. A common use of this class
	 * will be: <code>
	 *     var si= new CAAT.SpriteImage().
	 *          initialize( an_image_instance, rows, columns ).
	 *          setAnimationImageIndex( [2,1,0,1] ).                // cycle throwout image with these indexes
	 *          setChangeFPS( 200 ).                                // change sprite every 200 ms.
	 *          setSpriteTransformation( CAAT.SpriteImage.TR_xx);   // optionally draw images inverted, ...
	 * </code>
	 * 
	 * A SpriteImage is an sprite sheet. It encapsulates an Image and treates
	 * and references it as a two dimensional array of row by columns
	 * sub-images. The access form will be sequential so if defined a
	 * CompoundImage of more than one row, the subimages will be referenced by
	 * an index ranging from 0 to rows*columns-1. Each sumimage will be of size
	 * (image.width/columns) by (image.height/rows).
	 * 
	 * <p>
	 * It is able to draw its sub-images in the following ways:
	 * <ul>
	 * <li>no transformed (default)
	 * <li>flipped horizontally
	 * <li>flipped vertically
	 * <li>flipped both vertical and horizontally
	 * </ul>
	 * 
	 * <p>
	 * It is supposed to be used in conjunction with
	 * <code>CAAT.SpriteActor</code> instances.
	 * 
	 * @constructor
	 * 
	 * FIXME Completely broken ........
	 */
	public SpriteImage() {
		// TODO Cannot do this in Java
//		this.paint = this.paintN;
		this.setAnimationImageIndex(new int[]{0});
		this.mapInfo = new HashMap<Integer, SpriteImageHelper>();
		this.animationsMap = new HashMap<String, SpriteImageAnimationHelper>();
		
		// TODO ???
//        if ( arguments.length===1 ) {
//            this.initialize.call(this, arguments[0], 1, 1);
//        } else if ( arguments.length===3 ) {
//            this.initialize.apply(this, arguments);
//        }
		
	}

    /**
     * an Array defining the sprite frame sequence
     */
    int[] animationImageIndex = null;

    /**
     * Previous animation frame time.
     */
    double prevAnimationTime = -1;

    /**
     * how much Scene time to take before changing an Sprite frame.
     */
    int changeFPS = 1000;

    /**
     * any of the TR_* constants.
     */
    Tr transformation = Tr.NONE;

    /**
     * the current sprite frame
     */
    int spriteIndex = 0;

    /**
     * current index of sprite frames array.
     */
    int prevIndex = 0;

    /**
     * current animation name
     */
    String currentAnimation = null;

	public enum Tr {
		NONE(0), // constants used to determine how to draw the sprite image),
		FLIP_HORIZONTAL(1), FLIP_VERTICAL(2), FLIP_ALL(3), FIXED_TO_SIZE(4), TILE(5), FIXED_WIDTH_TO_SIZE(6);

		private int val;

		private Tr(int val) {
			this.val = val;
		}

		public int getVal() {
			return val;
		}

	}

    /**
     * Image to get frames from.
     */
	public CaatjaImage image = null;
	
	/**
     * Number of rows
     */
	public int rows = 1;
	
	/**
     * Number of columns.
     */
	public int columns = 1;

    /**
     * This sprite image image�s width
     */
	public int width = 0;
	
    /**
     * This sprite image image�s width
     */
	public int height = 0;
	
    /**
     * For each element in the sprite image array, its size.
     */
	public int singleWidth = 0;
	
	/**
     * For each element in the sprite image array, its height.
     */
	public int singleHeight = 0;

	int scaleX = 1;
	int scaleY = 1;

	/**
     * Displacement offset to get the sub image from. Useful to make images shift.
     */
	Integer offsetX = 0;
	
	/**
     * Displacement offset to get the sub image from. Useful to make images shift.
     */
	Integer offsetY = 0;
	
	/**
     * When nesting sprite images, this value is the star X position of this sprite image in the parent.
     */
	Integer parentOffsetX=0;    // para especificar una subimagen dentro un textmap.
	
	/**
     * When nesting sprite images, this value is the star Y position of this sprite image in the parent.
     */
    Integer parentOffsetY=0;

	double[][] xyCache = null;

	/**
     * The actor this sprite image belongs to.
     */
	Actor ownerActor = null;
	
	/**
     * If the sprite image is defined out of a JSON object (sprite packer for example), this is
     * the subimages calculated definition map.
     */
	Map<Integer, SpriteImageHelper> mapInfo = null;
	
	/**
     * If the sprite image is defined out of a JSON object (sprite packer for example), this is
     * the subimages original definition map.
     * TODO ?
     */
//    map:null,
	
	/**
     * This property allows to have multiple different animations defined for one actor.
     * see demo31 for a sample.
     */
	private HashMap<String, SpriteImageAnimationHelper> animationsMap;
	
	/**
     * When an animation sequence ends, this callback function will be called.
     */
	private SpriteImageAnimationCallback callback; // on end animation callback
	
	 /**
     * pending: refactor -> font scale to a font object.
     */
    private double fontScale = 1;
	
	public Actor getOwnerActor () {
        return this.ownerActor;
    }

    /**
     * Add an animation to this sprite image.
     * An animation is defines by an array of pretend-to-be-played sprite sequence.
     *
     * @param name {string} animation name.
     * @param array {Array<number|string>} the sprite animation sequence array. It can be defined
     *              as number array for Grid-like sprite images or strings for a map-like sprite
     *              image.
     * @param time {number} change animation sequence every 'time' ms.
     * @param callback {function({SpriteImage},{string}} a callback function to invoke when the sprite
     *              animation sequence has ended.
     */
    public SpriteImage addAnimation (String name, int[] array, int time, SpriteImageAnimationCallback callback ) {
        this.animationsMap.put(name, new SpriteImageAnimationHelper(array,time,callback));
        return this;
    }

    public void setAnimationEndCallback (SpriteImageAnimationCallback f) {
        this.callback= f;
    }

    /**
     * Start playing a SpriteImage animation.
     * If it does not exist, nothing happens.
     * @param name
     */
    public SpriteImage playAnimation (String name) {
        if (name.equals(this.currentAnimation)) {
            return this;
        }

        SpriteImageAnimationHelper animation= this.animationsMap.get(name);
        if ( animation == null) {
            return this;
        }

        this.currentAnimation= name;

        // TODO
        this.setAnimationImageIndex( animation.animation );
        this.changeFPS= animation.time;
        this.callback= animation.onEndPlayCallback;

        return this;
    }

	public SpriteImage setOwner(Actor actor) {
		this.ownerActor = actor;
		return this;
	}

	public int getRows() {
		return this.rows;
	}

	public int getColumns() {
		return this.columns;
	}
	
	public double getWidth() {
	    SpriteImageHelper el= this.mapInfo.get(this.spriteIndex);
        return el.width;
    }

    public double getHeight() {
        SpriteImageHelper el= this.mapInfo.get(this.spriteIndex);
        return el.height;
    }
	
    public int getWrappedImageWidth() {
        return this.image.getWidth();
    }

    public int getWrappedImageHeight() {
        return this.image.getHeight();
    }

	/**
	 * Get a reference to the same image information (rows, columns, image and
	 * uv cache) of this SpriteImage. This means that re-initializing this
	 * objects image info (that is, calling initialize method) will change all
	 * reference's image information at the same time.
	 */
	public SpriteImage getRef() {
		SpriteImage ret = new SpriteImage();
		ret.image = this.image;
		ret.rows = this.rows;
		ret.columns = this.columns;
		ret.width = this.width;
		ret.height = this.height;
		ret.singleWidth = this.singleWidth;
		ret.singleHeight = this.singleHeight;
		ret.mapInfo=        this.mapInfo;
		ret.xyCache = this.xyCache;
		ret.offsetX = this.offsetX;
		ret.offsetY = this.offsetY;
		ret.scaleX = this.scaleX;
		ret.scaleY = this.scaleY;
		ret.animationsMap = this.animationsMap;
		ret.parentOffsetX= this.parentOffsetX;
        ret.parentOffsetY= this.parentOffsetY;
        // TODO Check
        ret.fontScale= this.fontScale;
        
		return ret;
	}

	/**
	 * Set horizontal displacement to draw image. Positive values means drawing
	 * the image more to the right.
	 * 
	 * @param x
	 *            {number}
	 * @return this
	 */
	public SpriteImage setOffsetX(int x) {
		this.offsetX = x;
		return this;
	}

	/**
	 * Set vertical displacement to draw image. Positive values means drawing
	 * the image more to the bottom.
	 * 
	 * @param y
	 *            {number}
	 * @return this
	 */
	public SpriteImage setOffsetY(int y) {
		this.offsetY = y;
		return this;
	}

	public SpriteImage setOffset(int x, int y) {
		this.offsetX = x;
		this.offsetY = y;
		return this;
	}

	/**
	 * Initialize a grid of subimages out of a given image.
	 * 
	 * @param image
	 *            {HTMLImageElement|Image} an image object.
	 * @param rows
	 *            {number} number of rows.
	 * @param columns
	 *            {number} number of columns
	 * 
	 * @return this
	 */
	public SpriteImage initialize (CaatjaImage image, int rows,int columns) {
	    
	    if (image == null) {
            CAAT.log("Null image for SpriteImage.");
        }

	    // TODO CAnnot do that
//        if ( isString(image) ) {
//            image= CAAT.currentDirector.getImage(image);
//        }

        this.parentOffsetX= 0;
        this.parentOffsetY= 0;

        this.rows = rows;
        this.columns = columns;
        
        // TODO FIXME
//        if ( image instanceof CAAT.Foundation.SpriteImage || image instanceof CAAT.SpriteImage ) {
//            this.image =        image.image;
//            SpriteImageHelper sihelper= image.mapInfo.get(0);
//            this.width= (int) sihelper.width;
//            this.height= (int) sihelper.height;
//
//            this.parentOffsetX= (int) sihelper.x;
//            this.parentOffsetY= (int) sihelper.y;
//
//            this.width= image.mapInfo.get(0).width;
//            this.height= image.mapInfo.get(0).height;
//
//        } else {
            this.image = image;
            this.width = image.getWidth();
            this.height = image.getHeight();
            this.mapInfo = new HashMap<Integer, SpriteImageHelper>();
//        }

        this.singleWidth = (int) Math.floor(this.width / (double) columns);
        this.singleHeight = (int) Math.floor(this.height / (double) rows);
        
        // TODO rows * columns added by me
        this.mapInfo= new HashMap<Integer, SpriteImageHelper>();
        
        // TODO Check
        this.xyCache = new double[rows * columns][];

        int i,sx0,sy0;
        SpriteImageHelper helper;
        
        if (image.__texturePage != null) {
            image.__du = this.singleWidth / image.__texturePage.width;
            image.__dv = this.singleHeight / image.__texturePage.height;


            int w = this.singleWidth;
            int h = this.singleHeight;
            int mod = this.columns;
            if (image.inverted) {
            	int t = w;
                w = h;
                h = t;
                mod = this.rows;
            }

            double xt = this.image.__tx;
            double yt = this.image.__ty;

            TexturePage tp = this.image.__texturePage;

            for (i = 0; i < rows * columns; i++) {


            	int c = ((i % mod) >> 0);
            	int r = ((i / mod) >> 0);

            	double u = xt + c * w;  // esquina izq x
            	double v = yt + r * h;

            	double u1 = (u + w);
            	double v1 = v + h;

            	// TODO Cast to int ?
                helper = new SpriteImageHelper((int) u,(int) v,(int)(u1-u),(int)(v1-v),tp.width,tp.height).setGL(
                        u / tp.width,
                        v / tp.height,
                        u1 / tp.width,
                        v1 / tp.height );
                this.mapInfo.put(i, helper);
            }

        } else {
            for (i = 0; i < rows * columns; i++) {
                sx0 = ((i % this.columns) | 0) * this.singleWidth + this.parentOffsetX;
                sy0 = ((i / this.columns) | 0) * this.singleHeight + this.parentOffsetY;

//                this.xyCache[i] = new double[]{sx0,sy0};
                helper= new SpriteImageHelper( sx0, sy0, this.singleWidth, this.singleHeight, image.getWidth(), image.getHeight() );
                this.mapInfo.put(i, helper);
                
            }
        }

        return this;
    }
	
	/**
     * Create elements as director.getImage values.
     * Create as much as elements defined in this sprite image.
     * The elements will be named prefix+<the map info element name>
     * @param prefix
     */
    public void addElementsAsImages(String prefix ) {
        for( Integer i : this.mapInfo.keySet() ) {
            SpriteImage si= new SpriteImage().initialize( this.image, 1, 1 );
            si.addElement(0, this.mapInfo.get(i));
            si.setSpriteIndex(0);
            // FIXME Change spriteimage to caatjaimage 
//            CAAT.currentDirector.addImage( prefix+i, si, false);
        }
    }
	
	public SpriteImage copy(CaatjaImage other ) {
        this.initialize(other,1,1);
        // FIXME
//        this.mapInfo= other.mapInfo;
        return this;
    }
	
	 /**
     * Must be used to draw actor background and the actor should have setClip(true) so that the image tiles
     * properly.
     * @param director
     * @param time
     * @param x
     * @param y
     */
    public void paintTiled(Director director, double time, int x, int y ) {
        
        // PENDING: study using a pattern
        
        SpriteImageHelper el= this.mapInfo.get(this.spriteIndex);
        
        Rectangle r= new Rectangle();
        this.ownerActor.AABB.intersect( director.AABB, r );

        double w= this.getWidth();
        double h= this.getHeight();
        double xoff= ((this.offsetX-this.ownerActor.x) % w);
        if ( xoff> 0 ) {
            xoff= xoff-w;
        }
        double yoff= (this.offsetY-this.ownerActor.y) % h;
        if ( yoff> 0 ) {
            yoff= yoff-h;
        }

        int nw= ((int)((r.width-xoff)/w)>>0)+1;
        int nh= ((int)((r.height-yoff)/h)>>0)+1;
        int i,j;
        CaatjaContext2d ctx= director.ctx;

        for( i=0; i<nh; i++ ) {
            for( j=0; j<nw; j++ ) {
                ctx.drawImage(
                    this.image,
                    el.x, el.y,
                    el.width, el.height,
                    (int)(r.x-this.ownerActor.x+xoff+j*el.width)>>0, (int)(r.y-this.ownerActor.y+yoff+i*el.height)>>0,
                    el.width, el.height);
            }
        }
    }

	/**
	 * Draws the subimage pointed by imageIndex horizontally inverted.
	 * 
	 * @param canvas
	 *            a canvas context.
	 * @param imageIndex
	 *            {number} a subimage index.
	 * @param x
	 *            {number} x position in canvas to draw the image.
	 * @param y
	 *            {number} y position in canvas to draw the image.
	 * 
	 * @return this
	 */
	public SpriteImage paintInvertedH(Director director, double time, int x,
			int y) {
//		this.setSpriteIndexAtTime(time);
		SpriteImageHelper el = this.mapInfo.get(this.spriteIndex);

		CaatjaContext2d ctx = director.ctx;
		ctx.save();
//		ctx.translate(((int) (0.5 + x) | 0) + el.width,
//				(int) (0.5 + y) | 0);
		ctx.translate( (x|0) + el.width, y|0 );
		ctx.scale(-1, 1);

        ctx.drawImage(
                this.image,
                el.x, el.y,
                el.width, el.height,
                this.offsetX>>0, this.offsetY>>0,
                el.width, el.height );

		ctx.restore();

		return this;
	}

	/**
	 * Draws the subimage pointed by imageIndex vertically inverted.
	 * 
	 * @param canvas
	 *            a canvas context.
	 * @param imageIndex
	 *            {number} a subimage index.
	 * @param x
	 *            {number} x position in canvas to draw the image.
	 * @param y
	 *            {number} y position in canvas to draw the image.
	 * 
	 * @return this
	 */
	public SpriteImage paintInvertedV(Director director, double time, int x,
			int y) {
//		this.setSpriteIndexAtTime(time);
		SpriteImageHelper el= this.mapInfo.get(this.spriteIndex);

		CaatjaContext2d ctx = director.ctx;
		ctx.save();
//		ctx.translate((int) (x + 0.5) | 0,
//				(int) (0.5 + y + el.height) | 0);
		ctx.translate( x|0, (int)(y + el.height) | 0);
		ctx.scale(1, -1);

		ctx.drawImage(
                this.image,
                el.x, el.y,
                el.width, el.height,
                this.offsetX>>0,this.offsetY>>0,
                el.width, el.height);

		ctx.restore();

		return this;
	}

	/**
	 * Draws the subimage pointed by imageIndex both horizontal and vertically
	 * inverted.
	 * 
	 * @param canvas
	 *            a canvas context.
	 * @param imageIndex
	 *            {number} a subimage index.
	 * @param x
	 *            {number} x position in canvas to draw the image.
	 * @param y
	 *            {number} y position in canvas to draw the image.
	 * 
	 * @return this
	 */
	public SpriteImage paintInvertedHV(Director director, double time, int x,
			int y) {
//		this.setSpriteIndexAtTime(time);
		SpriteImageHelper el= this.mapInfo.get(this.spriteIndex);

		CaatjaContext2d ctx = director.ctx;
		ctx.save();
//		ctx.translate((int) (x + 0.5) | 0,
//				(int) (0.5 + y + el.height) | 0);
		ctx.translate( x | 0, (int)(y + el.height) | 0);
		ctx.scale(1, -1);
		ctx.translate(this.singleWidth, 0);
		ctx.scale(-1, 1);

		 ctx.drawImage(
	                this.image,
	                el.x, el.y,
	                el.width, el.height,
	                this.offsetX>>0, this.offsetY>>0,
	                el.width, el.height);

		ctx.restore();

		return this;
	}

	/**
	 * Draws the subimage pointed by imageIndex.
	 * 
	 * @param canvas
	 *            a canvas context.
	 * @param imageIndex
	 *            {number} a subimage index.
	 * @param x
	 *            {number} x position in canvas to draw the image.
	 * @param y
	 *            {number} y position in canvas to draw the image.
	 * 
	 * @return this
	 */
	public SpriteImage paintN(Director director, double time, int x, int y) {
//		this.setSpriteIndexAtTime(time);
		SpriteImageHelper el= this.mapInfo.get(this.spriteIndex);

//		director.ctx.drawImage(this.image.imageElement,
//				(int) this.xyCache[this.spriteIndex][0] >> 0,
//				(int) this.xyCache[this.spriteIndex][1] >> 0, this.singleWidth,
//				this.singleHeight, (this.offsetX + x) >> 0, (this.offsetY + y) >> 0,
//				this.singleWidth, this.singleHeight);
		
		director.ctx.drawImage(
                this.image,
                el.x, el.y,
                el.width, el.height,
                (this.offsetX+x)>>0, (this.offsetY+y)>>0,
                el.width, el.height);

		return this;
	}
	
	public SpriteImage paintAtRect(Director director, double time, int x, int y, int w, int h) {

	    SpriteImageHelper el= this.mapInfo.get(this.spriteIndex);

        director.ctx.drawImage(
            this.image,
            el.x, el.y,
            el.width, el.height,
            (this.offsetX + x) >> 0, (this.offsetY + y) >> 0,
            w, h);

        return this;
    }
	
	/**
     * Draws the subimage pointed by imageIndex.
     * @param canvas a canvas context.
     * @param imageIndex {number} a subimage index.
     * @param x {number} x position in canvas to draw the image.
     * @param y {number} y position in canvas to draw the image.
     *
     * @return this
     */
    public SpriteImage paintScaledWidth(Director director, double time, double x, double y) {
//        this.setSpriteIndexAtTime(time);
        SpriteImageHelper el= this.mapInfo.get(this.spriteIndex);

        director.ctx.drawImage(
            this.image,
            el.x, el.y,
            el.width, el.height,
            (int)(this.offsetX+x)>>0, (int)(this.offsetY+y)>>0,
            this.ownerActor.width, el.height);

        return this;
    }
	
	public void paintChunk(CaatjaContext2d ctx, double dx, double dy, double x, double y, double w,double  h ) {
        ctx.drawImage( this.image, x,y,w,h, dx,dy,w,h );
    }
	
    public SpriteImage paintTile(CaatjaContext2d ctx, int index, int x, int y) {
        SpriteImageHelper el= this.mapInfo.get(index);
        ctx.drawImage(
            this.image,
            el.x, el.y,
            el.width, el.height,
            (this.offsetX+x)>>0, (this.offsetY+y)>>0,
            el.width, el.height);

        return this;
    }

	/**
	 * Draws the subimage pointed by imageIndex scaled to the size of w and h.
	 * 
	 * @param canvas
	 *            a canvas context.
	 * @param imageIndex
	 *            {number} a subimage index.
	 * @param x
	 *            {number} x position in canvas to draw the image.
	 * @param y
	 *            {number} y position in canvas to draw the image.
	 * @param label
	 *            {number} new width of the subimage.
	 * @param h
	 *            {number} new height of the subimage.
	 * 
	 * @return this
	 */
	public SpriteImage paintScaled(Director director, double time, int x, int y) {
//		this.setSpriteIndexAtTime(time);
		SpriteImageHelper el= this.mapInfo.get(this.spriteIndex);
		
		director.ctx.drawImage(
                this.image,
                el.x, el.y,
                el.width, el.height,
                (this.offsetX+x)>>0, (this.offsetY+y)>>0,
                this.ownerActor.width, this.ownerActor.height );

		return this;
	}
	
    public String getCurrentSpriteImageCSSPosition() {
        
        SpriteImageHelper el= this.mapInfo.get(this.spriteIndex);
        
        double x = -(el.x + this.parentOffsetX - this.offsetX);
        double y = -(el.y + this.parentOffsetY - this.offsetY);
        
        // TODO ?
//        return ""+x+"px "+
//               y+"px "+
//                (this.ownerActor.transformation==Tr.TILE ? "repeat" : "no-repeat");
        return null;
    }

	/**
	 * Get the number of subimages in this compoundImage
	 * 
	 * @return {number}
	 */
	public int getNumImages() {
		return this.rows * this.columns;
	}

	/**
	 * TODO: set mapping coordinates for different transformations.
	 * 
	 * FIXME
	 * @param imageIndex
	 * @param uvBuffer
	 * @param uvIndex
	 */
//	public void setUV(Float32Array uv, Float32Array uvBuffer, int uvIndex) {
//	    CaatjaImage im = this.image;
//
//		if (im.__texturePage == null) {
//			return;
//		}
//
//		int index = uvIndex;
//		int sIndex = this.spriteIndex;
//		SpriteImageHelper el= this.mapInfo.get(this.spriteIndex);
//		
////		double u = this.xyCache[sIndex][0];
////		double v = this.xyCache[sIndex][1];
////		double u1 = this.xyCache[sIndex][2];
////		double v1 = this.xyCache[sIndex][3];
//		
//		double u=  el.u;
//		double v=  el.v;
//		double u1= el.u1;
//		double v1= el.v1;
//
//		// TODO Check null ?
//		if (this.offsetX != 0 || this.offsetY != 0) {
//			int w = (int) this.ownerActor.width;
//			int h = (int) this.ownerActor.height;
//
//			TexturePage tp = im.__texturePage;
//
//			double _u = -this.offsetX / tp.width;
//			double _v = -this.offsetY / tp.height;
//			double _u1 = (w - this.offsetX) / tp.width;
//			double _v1 = (h - this.offsetY) / tp.height;
//
//			u = (int) (_u + im.__u);
//			v = (int) (_v + im.__v);
//			u1 = (int) (_u1 + im.__u);
//			v1 = (int) (_v1 + im.__v);
//		}
//
//		if (im.inverted) {
//			uvBuffer.set(index++, (float) u1);
//			uvBuffer.set(index++, (float) v);
//
//			uvBuffer.set(index++, (float) u1);
//			uvBuffer.set(index++, (float) v1);
//
//			uvBuffer.set(index++, (float) u);
//			uvBuffer.set(index++, (float) v1);
//
//			uvBuffer.set(index++, (float) u);
//			uvBuffer.set(index++, (float) v);
//		} else {
//			uvBuffer.set(index++, (float) u);
//			uvBuffer.set(index++, (float) v);
//
//			uvBuffer.set(index++, (float) u1);
//			uvBuffer.set(index++, (float) v);
//
//			uvBuffer.set(index++, (float) u1);
//			uvBuffer.set(index++, (float) v1);
//
//			uvBuffer.set(index++, (float) u);
//			uvBuffer.set(index++, (float) v1);
//		}
//	}

	/**
	 * Set the elapsed time needed to change the image index.
	 * 
	 * @param fps
	 *            an integer indicating the time in milliseconds to change.
	 * @return this
	 */
	public SpriteImage setChangeFPS(int fps) {
		this.changeFPS = fps;
		return this;
	}

	/**
	 * Set the transformation to apply to the Sprite image. Any value of <li>
	 * TR_NONE <li>TR_FLIP_HORIZONTAL <li>TR_FLIP_VERTICAL <li>TR_FLIP_ALL
	 * 
	 * @param transformation
	 *            an integer indicating one of the previous values.
	 * @return this
	 */
	public SpriteImage setSpriteTransformation(Tr transformation) {
		this.transformation = transformation;
		// TODO Cannot do this in Java
//		switch (transformation) {
//		case FLIP_HORIZONTAL:
//			this.paint = this.paintInvertedH;
//			break;
//		case FLIP_VERTICAL:
//			this.paint = this.paintInvertedV;
//			break;
//		case FLIP_ALL:
//			this.paint = this.paintInvertedHV;
//			break;
//		case FIXED_TO_SIZE:
//			this.paint = this.paintScaled;
//			break;
//	case this.TR_FIXED_WIDTH_TO_SIZE:
//        this.paint= this.paintScaledWidth;
//        break;
//	case this.TR_TILE:
//        this.paint= this.paintTiled;
//        break;
//		default:
//			this.paint = this.paintN;
//		}
		this.ownerActor.invalidate();
		return this;
	}
	
	public SpriteImage resetAnimationTime() {
        this.prevAnimationTime=  -1;
        return this;
    }

	/**
     * Set the sprite animation images index. This method accepts an array of objects which define indexes to
     * subimages inside this sprite image.
     * If the SpriteImage is instantiated by calling the method initialize( image, rows, cols ), the value of
     * aAnimationImageIndex should be an array of numbers, which define the indexes into an array of subimages
     * with size rows*columns.
     * If the method InitializeFromMap( image, map ) is called, the value for aAnimationImageIndex is expected
     * to be an array of strings which are the names of the subobjects contained in the map object.
     *
     * TODO int or String ?
     *
     * @param aAnimationImageIndex an array indicating the Sprite's frames.
     */
	public SpriteImage setAnimationImageIndex(int[] aAnimationImageIndex) {
		this.animationImageIndex = aAnimationImageIndex;
		this.spriteIndex = aAnimationImageIndex[0];
		this.prevAnimationTime= -1;

		return this;
	}

	public SpriteImage setSpriteIndex(int index) {
		this.spriteIndex = index;
		return this;
	}

	/**
	 * Draws the sprite image calculated and stored in spriteIndex.
	 * 
	 * @param director
	 *            the CAAT.Director object instance that contains the Scene the
	 *            Actor is in.
	 * @param time
	 *            an integer indicating the Scene time when the bounding box is
	 *            to be drawn.
	 */
	public void setSpriteIndexAtTime(double time) {

		if (this.animationImageIndex.length > 1) {
			if (this.prevAnimationTime == -1) {
				this.prevAnimationTime = time;
				
				//thanks Phloog and ghthor, well spotted.
                this.spriteIndex= this.animationImageIndex[0];
                this.prevIndex= 0;
                this.ownerActor.invalidate();
			} else {
				double ttime = time;
				ttime -= this.prevAnimationTime;
				ttime /= this.changeFPS;
				ttime %= this.animationImageIndex.length;
//				int idx = this.animationImageIndex[(int) Math
//						.floor(ttime)];
				int idx = (int) Math.floor(ttime);
//				if ( this.spriteIndex!=idx ) {
				
//                    this.spriteIndex= idx;
                if ( idx<this.prevIndex ) {   // we are getting back in time, or ended playing the animation
                    if ( this.callback != null) {
                        this.callback.call(time );
                    }
                }

                this.prevIndex = idx;
                this.spriteIndex = this.animationImageIndex[idx];
                this.ownerActor.invalidate();
//                }
			}
		}
	}
	
    public SpriteImageHelper getMapInfo(int index) {
        return this.mapInfo.get(index);
    }

    // TODO
    public SpriteImage initializeFromGlyphDesigner(String text ) {
//        for (var i = 0; i < text.length; i++) {
//            if (0 === text[i].indexOf("char ")) {
//                var str = text[i].substring(5);
//                var pairs = str.split(' ');
//                var obj = {
//                    x: 0,
//                    y: 0,
//                    width: 0,
//                    height: 0,
//                    xadvance: 0,
//                    xoffset: 0,
//                    yoffset: 0
//                };
//
//                for (var j = 0; j < pairs.length; j++) {
//                    var pair = pairs[j];
//                    var pairData = pair.split("=");
//                    var key = pairData[0];
//                    var value = pairData[1];
//                    if (value.charAt(0) === '"' && value.charAt(value.length - 1) === '"') {
//                        value.substring(1, value.length - 1);
//                    }
//                    obj[ key ] = value;
//                }
//
//                this.addElement(String.fromCharCode(obj.id), obj);
//            }
//        }

        return this;
    }

	/**
     * This method takes the output generated from the tool at http://labs.hyperandroid.com/static/texture/spriter.html
     * and creates a map into that image.
     * @param image {Image|HTMLImageElement|Canvas} an image
     * @param map {object} the map into the image to define subimages.
     */
    public SpriteImage initializeFromMap(CaatjaImage image, Map<Integer, SpriteImageHelper> map ) {
        this.initialize( image, 1, 1 );

        SpriteImageHelper helper;
        int count=0;
        
        for (Integer key : map.keySet()) {
            SpriteImageHelper value = map.get(key);
            
            helper = new SpriteImageHelper(
                    value.x + this.parentOffsetX,
                    value.y + this.parentOffsetY,
                    value.width,
                    value.height,
                    image.getWidth(),
                    image.getHeight()
                );
            
            this.mapInfo.put(key, helper);
            
         // set a default spriteIndex
            if ( count == 0) {
                this.setAnimationImageIndex(new int[] {key});
            }

            count++;
        }

        return this;
    }
    
    // TODO
//    public void initializeFromTexturePackerJSON(CaatjaImage image, obj ) {
//
//        for( var img in obj.frames ) {
//            var imgData= obj.frames[img];
//
//            var si_obj= {
//                x: imgData.frame.x,
//                y: imgData.frame.y,
//                width: imgData.spriteSourceSize.w,
//                height: imgData.spriteSourceSize.h,
//                id: '0'
//            };
//
//            SpriteImage si= new SpriteImage().initialize( image, 1, 1 );
//            si.addElement(0,si_obj);
//            CAAT.currentDirector.addImage( img.substring(0,img.indexOf('.')), si, false );
//        }
//    }
    
    /**
     * TODO Check
     * 
     * Add one element to the spriteImage.
     * @param key {string|number} index or sprite identifier.
     * @param value object{
     *      x: {number},
     *      y: {number},
     *      width: {number},
     *      height: {number},
     *      xoffset: {number=},
     *      yoffset: {number=},
     *      xadvance: {number=}
     *      }
     * @return {*}
     */
    public SpriteImage addElement(Integer key, SpriteImageHelper value ) {
        SpriteImageHelper helper = new SpriteImageHelper(
            value.x + this.parentOffsetX,
            value.y + this.parentOffsetY,
            value.width,
            value.height,
            this.image.getWidth(),
            this.image.getHeight());

        helper.xoffset = value.xoffset == null ? 0 : value.xoffset;
        helper.yoffset =  value.yoffset == null ? 0 : value.yoffset;
        helper.xadvance =  (int) (value.xadvance == null ? value.width : value.xadvance);

        this.mapInfo.put(key, helper);

        return this;
    }

    
    /**
    *
    * @param image {Image|HTMLImageElement|Canvas}
    * @param map object with pairs "<a char>" : {
    *              id      : {number},
    *              height  : {number},
    *              xoffset : {number},
    *              letter  : {string},
    *              yoffset : {number},
    *              width   : {number},
    *              xadvance: {number},
    *              y       : {number},
    *              x       : {number}
    *          }
    */
   public SpriteImage initializeAsGlyphDesigner(CaatjaImage image, Map<String, SpriteImageHelper> map ) {
       this.initialize( image, 1, 1 );

       SpriteImageHelper helper;
       int count=0;
       
       for (String key : map.keySet()) {
           SpriteImageHelper value = map.get(key);
           
           helper= new SpriteImageHelper(
                   value.x + this.parentOffsetX,
                   value.y + this.parentOffsetY,
                   value.width,
                   value.height,
                   image.getWidth(),
                   image.getHeight()
               );

               helper.xoffset=  value.xoffset==null ? 0 : value.xoffset;
               helper.yoffset=  value.yoffset==null ? 0 : value.yoffset;
               helper.xadvance=  (int) (value.xadvance==null ? value.width : value.xadvance);

               // FIXME
//               this.mapInfo.set(key, helper);

               // set a default spriteIndex
               if ( count == 0) {
                   // FIXME
//                   this.setAnimationImageIndex(new int []{key} );
               }

               count++;
           
       }

       return this;

   }
    
    public class FontMap {
        String c;
        int width;
        
        public FontMap(String c, int width) {
            this.c = c;
            this.width = width;
        }
    }
    
    /**
    *
    * @param image
    * @param map: array of {c: "a", width: 40}
    * FIXME TODO Not working at all ...
    */
   public SpriteImage initializeAsFontMap (CaatjaImage image,  List<FontMap> chars ) {
       this.initialize( image, 1, 1 );

       SpriteImageHelper helper;
       int x = 0;
       
       for (int i=0;i<chars.size();i++ ) {
           FontMap value= chars.get(i);
           
           helper= new SpriteImageHelper(
                   x + this.parentOffsetX,
                   0 + this.parentOffsetY,
                   value.width,
                   image.getHeight(),
                   image.getWidth(),
                   image.getHeight()
               );
           
           x += value.width;
           
           // FIXME
//           this.mapInfo.set(chars.get(i).c, helper);
           
           // set a default spriteIndex
           if (i == 0) {
               // FIXME this cannot work at all ...
               //this.setAnimationImageIndex(chars.get(i).c);
           }
           
       }

       return this;
   }
   
   /**
    * This method creates a font sprite image based on a proportional font
    * It assumes the font is evenly spaced in the image
    * Example:
    * var font =   new CAAT.SpriteImage().initializeAsMonoTypeFontMap(
    *  director.getImage('numbers'),
    *  "0123456789"
    * );
    */

   public SpriteImage initializeAsMonoTypeFontMap(CaatjaImage image,  String chars ) {
       List<FontMap> map = new ArrayList<SpriteImage.FontMap>();
       String[] charArr = chars.split("");
       
       double w = image.getWidth() / charArr.length >> 0;

       for( int i=0;i<charArr.length;i++ ) {
           map.add(new FontMap(charArr[i], (int) w));
       }

       return this.initializeAsFontMap(image,map);
   }

   public int stringWidth (String str ) {
       int i,l,w=0;

       for( i=0, l=str.length(); i<l; i++ ) {
           // FIXME Cannot work ! key !
           SpriteImageHelper charInfo= this.mapInfo.get(str.charAt(i));
             if ( charInfo != null) {
                 w+= charInfo.width  * this.fontScale;
             }
       }

       return w;
   }
   
   // Add by me 
   public Integer fontHeight;
   
   // TODO Check if it works
   public int stringHeight() {
       if ( this.fontHeight != null) {
           return (int) (this.fontHeight  * this.fontScale);
       }

       double y= 0;
       for( Integer i : this.mapInfo.keySet() ) {
           SpriteImageHelper mi= this.mapInfo.get(i);

           double h= mi.height+mi.yoffset;
           if ( h>y ) {
               y=h;
           }
       }

       this.fontHeight= (int) y;
       return (int) (this.fontHeight  * this.fontScale);
   }

   public void drawText (CaatjaContext2d ctx, String str,int x, int y ) {
       int i, l, w;

       for( i=0; i<str.length(); i++ ) {
           
           // FIXME Cannot work at all !
//           SpriteImageHelper charInfo= this.mapInfo.get( str.charAt(i) );
           SpriteImageHelper charInfo = null;
           if ( charInfo != null) {
               w= (int) charInfo.width;
               ctx.drawImage(
                   this.image,
                   charInfo.x, charInfo.y,
                   w, charInfo.height,

                   x + charInfo.xoffset* this.fontScale, y + charInfo.yoffset* this.fontScale,
                   w * this.fontScale, charInfo.height * this.fontScale);

               x+= charInfo.xadvance  * this.fontScale;
           } 
           
//           SpriteImageHelper charInfo= this.mapInfo.get( str.charAt(i) );
//             if ( charInfo != null) {
//                 w= charInfo.width;
//           if ( w>0 && charInfo.height>0 ) {
//                 ctx.drawImage(
//                     this.image.imageElement,
//                     charInfo.x, charInfo.y,
//                     w, charInfo.height,
//
//                     x + charInfo.xoffset, y + charInfo.yoffset,
//                     w, charInfo.height );
//              }
//                 x+= charInfo.xadvance;
//             }
         }
   }
   
   public FontData getFontData() {
       int as= (int) (this.stringHeight() *.8)>>0;
       return new FontData(
           this.stringHeight(),
           as,
           this.stringHeight() - as
       );
   }

	// Add by me
	public SpriteImage paint(Director director, double time, int x, int y) {
		switch (this.transformation) {
		case FLIP_HORIZONTAL:
			this.paintInvertedH(director, time, x, y);
			break;
		case FLIP_VERTICAL:
			this.paintInvertedV(director, time, x, y);
			break;
		case FLIP_ALL:
			this.paintInvertedHV(director, time, x, y);
			break;
		case FIXED_TO_SIZE:
			this.paintScaled(director, time, x, y);
			break;
		case FIXED_WIDTH_TO_SIZE:
		    this.paintScaledWidth(director, time, x, y);
		    break;
		case TILE:
		    this.paintTiled(director, time, x, y);
		    break;
		default:
			this.paintN(director, time, x, y);
		}
		return this;
	}

}
