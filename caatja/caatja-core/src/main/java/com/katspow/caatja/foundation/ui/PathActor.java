package com.katspow.caatja.foundation.ui;

import com.katspow.caatja.core.canvas.CaatjaContext2d;
import com.katspow.caatja.event.CAATMouseEvent;
import com.katspow.caatja.foundation.Director;
import com.katspow.caatja.foundation.actor.Actor;
import com.katspow.caatja.math.Rectangle;
import com.katspow.caatja.pathutil.Path;
import com.katspow.caatja.pathutil.PathActorUpdateCallback;

/**
 * An actor to show the path and its handles in the scene graph. 
 *
 **/
public class PathActor extends Actor {

    /**
     * This class paints and handles the interactive behavior of a path.
     *
     * @constructor
     * @extends CAAT.ActorContainer
     */
    public PathActor() {
        super();
    }

    // TODO Check type
    /**
     * Path to draw.
     * @type {CAAT.PathUtil.Path}
     */
    private Path path = null;
    
    /**
     * Calculated path�s bounding box.
     */
    private Rectangle pathBoundingRectangle = null;
    
    /**
     * draw the bounding rectangle too ?
     */
    private boolean bOutline = false;
    
    /**
     * Outline the path in this color.
     */
    private String outlineColor =        "black";
    
    /**
     * If the path is interactive, some handlers are shown to modify the path.
     * This callback function will be called when the path is interactively changed.
     */
    private PathActorUpdateCallback onUpdateCallback        = null;
    
    /**
     * Set this path as interactive.
     */
    private boolean interactive             = false;
    private boolean showBBox = false;
    
    /**
     * Return the contained path.
     * @return {CAAT.Path}
     */
    public Path getPath() {
        return path;
    }

    /**
     * Sets the path to manage.
     * @param path {CAAT.PathSegment}
     * @return this
     */
    public PathActor setPath(Path path) {
        this.path = path;
        if ( path!=null ) {
            this.pathBoundingRectangle= path.getBoundingBox();
            this.setInteractive( this.interactive );
        }
        return this;
    }

    /**
     * Paint this actor.
     * @param director {CAAT.Director}
     * @param time {number}. Scene time.
     */
    public void paint(Director director, double time) {
        
        super.paint(director, time);
        
        if ( this.path == null) {
            return;
        }

        CaatjaContext2d ctx= director.ctx;
        
        ctx.setStrokeStyle("#000");
        this.path.paint(director, this.interactive);

        if (this.bOutline) {
            ctx.setStrokeStyle(this.outlineColor);
            ctx.strokeRect(
                    this.pathBoundingRectangle.x,
                    this.pathBoundingRectangle.y,
                    this.pathBoundingRectangle.width,
                    this.pathBoundingRectangle.height
                );
        }
       
    }
    
    /**
     * Enables/disables drawing of the contained path's bounding box.
     * @param show {boolean} whether to show the bounding box
     * @param color {=string} optional parameter defining the path's bounding box stroke style.
     */
    public PathActor showBoundingBox(boolean show, String color) {
        this.bOutline= show;
        if ( show && color != null) {
            this.outlineColor= color;
        }
        
        return this;
    }
    /**
     * Set the contained path as interactive. This means it can be changed on the fly by manipulation
     * of its control points.
     * @param interactive
     * @return 
     */
    public PathActor setInteractive(boolean interactive) {
        this.interactive= interactive;
        if ( this.path != null) {
            this.path.setInteractive(interactive);
        }
        return this;
    }
    
    public PathActor setOnUpdateCallback(PathActorUpdateCallback fn ) {
        this.onUpdateCallback= fn;
        return this;
    }

    /**
     * Route mouse dragging functionality to the contained path.
     * @param mouseEvent {CAAT.MouseEvent}
     */
    public void mouseDrag(CAATMouseEvent mouseEvent) {
        this.path.drag(mouseEvent.point.x, mouseEvent.point.y, this.onUpdateCallback);
    }

    /**
     * Route mouse down functionality to the contained path.
     * @param mouseEvent {CAAT.MouseEvent}
     */
    public void mouseDown(CAATMouseEvent mouseEvent) {
        this.path.press(mouseEvent.point.x, mouseEvent.point.y);
    }

    /**
     * Route mouse up functionality to the contained path.
     * @param mouseEvent {CAAT.MouseEvent}
     */
    public void mouseUp(CAATMouseEvent mouseEvent) {
        this.path.release();
    }

    // Add by me
    @Override
    public PathActor setBounds(double x, double y, double w, double h) {
        return (PathActor) super.setBounds(x, y, w, h);
    }

    @Override
    public PathActor setFillStyle(String fillStyle) {
        return (PathActor) super.setFillStyle(fillStyle);
    }

    @Override
    public PathActor setSize(double w, double h) {
        return (PathActor) super.setSize(w, h);
    }
    
}
