package com.katspow.caatjafx;

import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import com.katspow.caatja.core.CAAT;
import com.katspow.caatja.core.canvas.CaatjaCanvas;
import com.katspow.caatja.core.event.CaatjaEventManager;
import com.katspow.caatja.event.CAATMouseEvent;
import com.katspow.caatja.foundation.Director;
import com.katspow.caatja.foundation.actor.Actor;
import com.katspow.caatja.math.Pt;

public class CaatjaFxEventManager implements CaatjaEventManager {
	
	private Scene fxScene;

	public CaatjaFxEventManager(CaatjaFxRootPanel caatjaFxRootPanel) {
		fxScene = caatjaFxRootPanel.getFxScene();
	}

    @Override
    public void addMouseHandlers(final Director director, CaatjaCanvas caatjaCanvas) {
    	fxScene.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
    	    @Override
    	    public void handle(MouseEvent mouseEvent) {
    	    	if (director.touching) {
                    // TODO Check
    	    		CaatjaFxEventManager.this.onMouseUp(director, mouseEvent);
                }
    	    }
    	});
    	
    	fxScene.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
    	    @Override
    	    public void handle(MouseEvent mouseEvent) {
    	        EventTarget target = mouseEvent.getTarget();
    	        if (target instanceof Canvas) {
    	            CaatjaFxEventManager.this.onMouseDown(director, mouseEvent);
    	        }
    	    }
    	});
    	
		fxScene.addEventFilter(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent mouseEvent) {
					    EventTarget target = mouseEvent.getTarget();
						if (target instanceof Canvas && !director.dragging) {

							try {
								CaatjaFxEventManager.this.onMouseOver(director,
										mouseEvent);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
		});
    	
    	fxScene.addEventFilter(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
    	    @Override
    	    public void handle(MouseEvent mouseEvent) {
    	        EventTarget target = mouseEvent.getTarget();
    	    	if (target instanceof Canvas && !director.dragging ) {
                    try {
                    	CaatjaFxEventManager.this.onMouseOut(director, mouseEvent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
    	    	
    	    }
    	});
    	
    	
    	fxScene.addEventFilter(MouseEvent.MOUSE_MOVED, new EventHandler<MouseEvent>() {
    	    @Override
    	    public void handle(MouseEvent mouseEvent) {
    	    	CaatjaFxEventManager.this.onMouseMove(director, mouseEvent);
    	    }
    	});
    	
    	fxScene.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
    	    @Override
    	    public void handle(MouseEvent mouseEvent) {
    	        EventTarget target = mouseEvent.getTarget();
    	        if (target instanceof Canvas) {
        	    	if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
        	            if(mouseEvent.getClickCount() == 2){
        	            	CaatjaFxEventManager.this.onDoubleClick(director, mouseEvent);
        	            }
        	        }
    	        }
    	    }
    	});
    	
    	fxScene.addEventFilter(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                EventTarget target = mouseEvent.getTarget();
                if (target instanceof Canvas) {
                    CaatjaFxEventManager.this.onMouseDragged(director, mouseEvent);
                }
            }
        });
    	
    	
        
    }

    @Override
    public void addMultiTouchHandlers(Director director, CaatjaCanvas caatjaCanvas) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addTouchHandlers(Director director, CaatjaCanvas caatjaCanvas) {
        // TODO Auto-generated method stub
        
    }
    
    private void onMouseUp(Director director, MouseEvent event) {
        Pt mp= director.mousePoint;
        getCanvasCoord(director, mp, event);
        try {
            __mouseUpHandler(director, event);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        director.touching = false;
    }
    
    public void onMouseDown(Director director, MouseEvent event) {
        Pt mp= director.mousePoint;
        getCanvasCoord(director, mp, event);
        if ( mp.x<0 || mp.y<0 || mp.x>=director.width || mp.y>=director.height ) {
            return;
        }
        
        director.touching= true;
        
        try {
            __mouseDownHandler(director, event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void onMouseOver(Director director, MouseEvent event) throws Exception {
        Pt mp= director.mousePoint;
        getCanvasCoord(director, mp, event);
        if ( mp.x<0 || mp.y<0 || mp.x>=director.width || mp.y>=director.height ) {
            return;
        }
        __mouseOverHandler(director, event);
    }
    
    public void onMouseOut(Director director, MouseEvent event) throws Exception {
        Pt mp= director.mousePoint;
        getCanvasCoord(director, mp, event);
        __mouseOutHandler(director, event);
    }
    
    public void onMouseMove(Director director,MouseEvent event) {
        Pt mp= director.mousePoint;
        getCanvasCoord(director, mp, event);
        if ( !director.dragging && ( mp.x<0 || mp.y<0 || mp.x>=director.width || mp.y>=director.height ) ) {
            return;
        }
        
        try {
            __mouseMoveHandler(director, event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    protected void onMouseDragged(Director director, MouseEvent mouseEvent) {
        Pt mp= director.mousePoint;
        getCanvasCoord(director, mp, mouseEvent);
        if ( mp.x<0 || mp.y<0 || mp.x>=director.width || mp.y>=director.height) {
            return;
        }
        
        try {
            __mouseDragHandler(director, mouseEvent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void onDoubleClick(Director director, MouseEvent event) {
        Pt mp= director.mousePoint;
        getCanvasCoord(director, mp, event);
        if ( mp.x<0 || mp.y<0 || mp.x>=director.width || mp.y>=director.height ) {
            return;
        }
        
        try {
            __mouseDBLClickHandler(director, event);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    private void __mouseUpHandler(Director director, MouseEvent e) throws Exception {
        
        director.isMouseDown = false;
        getCanvasCoord(director, director.mousePoint, e);

        Pt pos= null;
        Actor lactor= director.lastSelectedActor;

        if (null != lactor) {
            pos = lactor.viewToModel(
                new Pt(director.screenMousePoint.x, director.screenMousePoint.y, 0));
            
            // TODO FIXME Remove comment when setAsButton is implemented
//            if ( lactor.actionPerformed && lactor.contains(pos.x, pos.y) ) {
//                lactor.actionPerformed(e)
//            }

            lactor.mouseUp(
                new CAATMouseEvent().init(
                    pos.x,
                    pos.y,
                    lactor,
                    director.screenMousePoint,
                    director.currentScene.time));
        }

        if (!director.dragging && null != lactor) {
            if (lactor.contains(pos.x, pos.y)) {
                lactor.mouseClick(
                    new CAATMouseEvent().init(
                        pos.x,
                        pos.y,
                        lactor,
                        director.screenMousePoint,
                        director.currentScene.time));
            }
        }

        director.dragging = false;
        director.in_=       false;
//        CAAT.setCursor("default");
    }
    
    public void __mouseDownHandler(Director director, MouseEvent e) throws Exception {
        
        /*
        was dragging and mousedown detected, can only mean a mouseOut's been performed and on mouseOver, no
        button was presses. Then, send a mouseUp for the previos actor, and return;
         */
        if ( director.dragging && director.lastSelectedActor != null ) {
            this.__mouseUpHandler(director, e);
            return;
        }

        this.getCanvasCoord(director, director.mousePoint, e);
        director.isMouseDown = true;
        Actor lactor = director.findActorAtPosition(director.mousePoint);

        if (null != lactor) {

            Pt pos = lactor.viewToModel(
                new Pt(director.screenMousePoint.x, director.screenMousePoint.y, 0));

            lactor.mouseDown(
                new CAATMouseEvent().init(
                    pos.x,
                    pos.y,
                    lactor,
                    new Pt(
                            director.screenMousePoint.x,
                            director.screenMousePoint.y ),
                            director.currentScene.time));
        }

        director.lastSelectedActor= lactor;
    }
    
    public void __mouseOverHandler (Director director, MouseEvent mouseEvent) throws Exception {
        
        if ( director.dragging ) {
            return;
        }

        Actor lactor;
        Pt pos;
        CAATMouseEvent ev;
        
        if ( null==director.lastSelectedActor ) {
            lactor= director.findActorAtPosition( director.mousePoint );

            if (null != lactor) {

                pos = lactor.viewToModel(
                    new Pt(director.screenMousePoint.x, director.screenMousePoint.y, 0));

                ev= new CAATMouseEvent().init(
                        pos.x,
                        pos.y,
                        lactor,
                        director.screenMousePoint,
                        director.currentScene.time);

                lactor.mouseOver(ev);
                lactor.mouseEnter(ev);
            }

            director.lastSelectedActor= lactor;
        } else {
            lactor= director.lastSelectedActor;
            pos = lactor.viewToModel(
                new Pt(director.screenMousePoint.x, director.screenMousePoint.y, 0));

            ev= new CAATMouseEvent().init(
                    pos.x,
                    pos.y,
                    lactor,
                    director.screenMousePoint,
                    director.currentScene != null ? director.currentScene.time : 0);

            lactor.mouseOver(ev);
            lactor.mouseEnter(ev);
            
        }

    }
    
    public void __mouseOutHandler(Director director, MouseEvent e) throws Exception {

        if (director.dragging) {
            return;
        }

        if (null != director.lastSelectedActor) {

            this.getCanvasCoord(director, director.mousePoint, e);
            Pt pos = new Pt(director.mousePoint.x, director.mousePoint.y, 0);
            director.lastSelectedActor.viewToModel(pos);

            CAATMouseEvent ev = new CAATMouseEvent().init(pos.x, pos.y, director.lastSelectedActor,
                    director.screenMousePoint, director.currentScene.time);

            director.lastSelectedActor.mouseExit(ev);
            director.lastSelectedActor.mouseOut(ev);

            if (!director.dragging) {
                director.lastSelectedActor = null;
            }
        } else {
            director.isMouseDown = false;
            director.in_ = false;
        }

    }
    
    private void __mouseDragHandler(Director director, MouseEvent e) throws Exception {
        
        Actor lactor;
        Pt pos;
        
        double ct = director.currentScene != null ? director.currentScene.time : 0;
        
        if (null != director.lastSelectedActor) {

            lactor = director.lastSelectedActor;
            pos = lactor.viewToModel(
                new Pt(director.screenMousePoint.x, director.screenMousePoint.y, 0));
            
            // TODO Check
            // check for mouse move threshold.
            if (!director.dragging) {
                if (Math.abs(director.prevMousePoint.x - director.mousePoint.x) < CAAT.DRAG_THRESHOLD_X &&
                    Math.abs(director.prevMousePoint.y - director.mousePoint.y) < CAAT.DRAG_THRESHOLD_Y) {
                    return;
                }
            }
            
            director.dragging = true;

            double px= lactor.x;
            double py= lactor.y;
            lactor.mouseDrag(
                    new CAATMouseEvent().init(
                        pos.x,
                        pos.y,
                        lactor,
                        new Pt(
                                director.screenMousePoint.x,
                                director.screenMousePoint.y),
                            ct));

            director.prevMousePoint.x= pos.x;
            director.prevMousePoint.y= pos.y;

            /**
             * Element has not moved after drag, so treat it as a button.
             */
            if ( px==lactor.x && py==lactor.y )   {

                boolean contains= lactor.contains(pos.x, pos.y);

                if (director.in_ && !contains) {
                    lactor.mouseExit(
                        new CAATMouseEvent().init(
                            pos.x,
                            pos.y,
                            lactor,
                            director.screenMousePoint,
                            ct));
                    director.in_ = false;
                }

                if (!director.in_ && contains ) {
                    lactor.mouseEnter(
                        new CAATMouseEvent().init(
                            pos.x,
                            pos.y,
                            lactor,
                            director.screenMousePoint,
                            ct));
                    director.in_ = true;
                }
            }
            
        }
        
    }
    
    public void __mouseMoveHandler (Director director, MouseEvent e) throws Exception {
//      this.getCanvasCoord(this.mousePoint, e);

      Actor lactor;
      Pt pos;
      
      double ct = director.currentScene != null ? director.currentScene.time : 0;

      // drag
      if (director.isMouseDown && null != director.lastSelectedActor) {
          System.out.println("dragging move");

          lactor = director.lastSelectedActor;
          pos = lactor.viewToModel(
              new Pt(director.screenMousePoint.x, director.screenMousePoint.y, 0));
          
          // check for mouse move threshold.
          if (!director.dragging) {
              if (Math.abs(director.prevMousePoint.x - director.mousePoint.x) < CAAT.DRAG_THRESHOLD_X &&
                  Math.abs(director.prevMousePoint.y - director.mousePoint.y) < CAAT.DRAG_THRESHOLD_Y) {
                  return;
              }
          }

          director.dragging = true;

          double px= lactor.x;
          double py= lactor.y;
          lactor.mouseDrag(
                  new CAATMouseEvent().init(
                      pos.x,
                      pos.y,
                      lactor,
                      new Pt(
                              director.screenMousePoint.x,
                              director.screenMousePoint.y),
                          ct));

          director.prevMousePoint.x= pos.x;
          director.prevMousePoint.y= pos.y;

          /**
           * Element has not moved after drag, so treat it as a button.
           */
          if ( px==lactor.x && py==lactor.y )   {

              boolean contains= lactor.contains(pos.x, pos.y);

              if (director.in_ && !contains) {
                  lactor.mouseExit(
                      new CAATMouseEvent().init(
                          pos.x,
                          pos.y,
                          lactor,
                          director.screenMousePoint,
                          ct));
                  director.in_ = false;
              }

              if (!director.in_ && contains ) {
                  lactor.mouseEnter(
                      new CAATMouseEvent().init(
                          pos.x,
                          pos.y,
                          lactor,
                          director.screenMousePoint,
                          ct));
                  director.in_ = true;
              }
          }

          return;
      }

      // mouse move.
      director.in_= true;

      lactor = director.findActorAtPosition(director.mousePoint);

      // cambiamos de actor.
      if (lactor != director.lastSelectedActor) {
          if (null != director.lastSelectedActor) {

              pos = director.lastSelectedActor.viewToModel(
                  new Pt(director.screenMousePoint.x, director.screenMousePoint.y, 0));

              director.lastSelectedActor.mouseExit(
                  new CAATMouseEvent().init(
                      pos.x,
                      pos.y,
                      director.lastSelectedActor,
                      director.screenMousePoint,
                      ct));
          }

          if (null != lactor) {
              pos = lactor.viewToModel(
                  new Pt( director.screenMousePoint.x, director.screenMousePoint.y, 0));

              lactor.mouseEnter(
                  new CAATMouseEvent().init(
                      pos.x,
                      pos.y,
                      lactor,
                      director.screenMousePoint,
                      ct));
          }
      }

      pos = lactor.viewToModel(
          new Pt(director.screenMousePoint.x, director.screenMousePoint.y, 0));

      if (null != lactor) {

          lactor.mouseMove(
              new CAATMouseEvent().init(
                  pos.x,
                  pos.y,
                  lactor,
                  director.screenMousePoint,
                  ct));
      }
      
      director.prevMousePoint.x= pos.x;
      director.prevMousePoint.y= pos.y;

      director.lastSelectedActor = lactor;
  }
    
    public void __mouseDBLClickHandler (Director director, MouseEvent e) throws Exception {

        this.getCanvasCoord(director, director.mousePoint, e);
        if (null != director.lastSelectedActor) {

//            Pt pos = this.lastSelectedActor.viewToModel(
//                new Pt(this.screenMousePoint.x, this.screenMousePoint.y, 0));

            director.lastSelectedActor.mouseDblClick(
                new CAATMouseEvent().init(
                        director.mousePoint.x,
                        director.mousePoint.y,
                        director.lastSelectedActor,
                        director.screenMousePoint,
                        director.currentScene.time));
        }
    }
    
    
    /**
     * Normalize input event coordinates to be related to (0,0) canvas position.
     * @param point {CAAT.Point} a CAAT.Point instance to hold the canvas coordinate.
     * @param e {MouseEvent} a mouse event from an input event.
     */
 // FIXME This method should be changed and handle Touch Event ! ...
    public void getCanvasCoord(Director director, Pt point, MouseEvent e) {
        
        Pt pt = new Pt();
        double posx = 0;
        double posy = 0;
        
        // TODO Check ???
//        if (e == null) e = window.event;
//        if (e.pageX || e.pageY)     {
//            posx = e.pageX;
//            posy = e.pageY;
//        } else if (e.clientX || e.clientY)    {
//            posx = e.clientX + document.body.scrollLeft
//                + document.documentElement.scrollLeft;
//            posy = e.clientY + document.body.scrollTop
//                + document.documentElement.scrollTop;
//        }
//
//        posx-= CAAT.director.canvas.offsetLeft;
//        posy-= CAAT.director.canvas.offsetTop;
        
        // TODO Check posx, posy !!!
        posx = e.getX();
        posy = e.getY();
        
        EventTarget target = e.getTarget();
        
        if (target instanceof Canvas) {
            Canvas c = (Canvas) target;
            double layX = c.getTranslateX();
            double layY = c.getTranslateY();

            posx -= layX;
            posy -= layY;
        }
        
        double pposx = posx;
        double pposy = posy;
        
//        Object node = e.getSource();
//        while (! (node instanceof RootPanel)) {
//            
//            Widget widget = (Widget) node;
//            
//            if (widget.getAbsoluteLeft() != 0 && widget.getAbsoluteTop() != 0) {
//                pposx -= widget.getAbsoluteLeft();
//                pposy -= widget.getAbsoluteTop();
//                break;
//            }
//            
//            node = widget.getParent();
//        }
        
//        pposx -= node.getAbsoluteLeft();
//        pposy -= node.getAbsoluteTop();
        
//        posx*= this.SCREEN_RATIO;
//        posy*= this.SCREEN_RATIO;

        //////////////
        // transformar coordenada inversamente con affine transform de director.
        pt.x= posx;
        pt.y= posy;
        if ( director.modelViewMatrixI == null) {
            director.modelViewMatrix.getInverse(director.modelViewMatrixI);
        }
        director.modelViewMatrixI.transformCoord(pt);
        posx= pt.x;
        posy= pt.y;
        
        point.set(pposx, pposy);
        director.screenMousePoint.set(pposx, pposy);

    }

}
