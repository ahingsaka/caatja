package com.katspow.caatjagwt.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.GestureChangeEvent;
import com.google.gwt.event.dom.client.GestureChangeHandler;
import com.google.gwt.event.dom.client.GestureEndEvent;
import com.google.gwt.event.dom.client.GestureEndHandler;
import com.google.gwt.event.dom.client.GestureStartEvent;
import com.google.gwt.event.dom.client.GestureStartHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.TouchCancelEvent;
import com.google.gwt.event.dom.client.TouchCancelHandler;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchEndHandler;
import com.google.gwt.event.dom.client.TouchEvent;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchMoveHandler;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.katspow.caatja.core.CAAT;
import com.katspow.caatja.core.canvas.CaatjaCanvas;
import com.katspow.caatja.core.event.CaatjaEventManager;
import com.katspow.caatja.event.CAATMouseEvent;
import com.katspow.caatja.event.CAATTouchEvent;
import com.katspow.caatja.event.TouchEventData;
import com.katspow.caatja.event.TouchInfo;
import com.katspow.caatja.event.TouchInfoData;
import com.katspow.caatja.foundation.Director;
import com.katspow.caatja.foundation.actor.Actor;
import com.katspow.caatja.math.Pt;

public class CaatjaGwtEventManager implements CaatjaEventManager {

    @Override
    public void addMouseHandlers(final Director director, final CaatjaCanvas caatjaCanvas) {
        
        final Canvas canvas = ((CaatjaGwtCanvas)caatjaCanvas).canvas;
        
        RootPanel.get().addDomHandler(new MouseUpHandler() {
            @Override
            public void onMouseUp(MouseUpEvent event) {
                
                if (director.touching) {
                    event.preventDefault();
                    
                    // TODO Check
                    event.stopPropagation();
                    
                    CaatjaGwtEventManager.this.onMouseUp(director, event);
                   
                }
            }
        }, MouseUpEvent.getType());
        
        RootPanel.get().addDomHandler(new MouseDownHandler() {
            @Override
            public void onMouseDown(MouseDownEvent event) {
                Element element = Element.as(event.getNativeEvent().getEventTarget());
                if (element == canvas.getElement()) {
                    event.preventDefault();
                    // TODO Check
                    event.stopPropagation();
                    
                    CaatjaGwtEventManager.this.onMouseDown(director, event);
                }
            }
        }, MouseDownEvent.getType());
        
        RootPanel.get().addDomHandler(new MouseOverHandler() {
            @Override
            public void onMouseOver(MouseOverEvent event) {
                Element element = Element.as(event.getNativeEvent().getEventTarget());
                if (element == canvas.getElement() && !director.dragging ) {
                    event.preventDefault();
                    // TODO Check
                    event.stopPropagation();
                    
                    try {
                        CaatjaGwtEventManager.this.onMouseOver(director, event);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, MouseOverEvent.getType());
        
        RootPanel.get().addDomHandler(new MouseOutHandler() {
            @Override
            public void onMouseOut(MouseOutEvent event) {
                Element element = Element.as(event.getNativeEvent().getEventTarget());
                if ( element == canvas.getElement() && !director.dragging ) {
                    event.preventDefault();
                    // TODO Check
                    event.stopPropagation();
                    
                    try {
                        CaatjaGwtEventManager.this.onMouseOut(director, event);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, MouseOutEvent.getType());
        
        RootPanel.get().addDomHandler(new MouseMoveHandler() {
            @Override
            public void onMouseMove(MouseMoveEvent event) {
                event.preventDefault();
                // TODO Check
                event.stopPropagation();
                
                CaatjaGwtEventManager.this.onMouseMove(director, event);
            }
        }, MouseMoveEvent.getType());
        
        RootPanel.get().addDomHandler(new DoubleClickHandler() {
            @Override
            public void onDoubleClick(DoubleClickEvent event) {
                Element element = Element.as(event.getNativeEvent().getEventTarget());
                if ( element ==canvas.getElement() ) {
                    event.preventDefault();
                    // TODO Check
                    event.stopPropagation();
                    
                    CaatjaGwtEventManager.this.onDoubleClick(director, event);
                }
            }
        }, DoubleClickEvent.getType());
        
        
    }

    @Override
    public void addTouchHandlers(final Director director, CaatjaCanvas caatjaCanvas) {
        
        final Canvas canvas = ((CaatjaGwtCanvas)caatjaCanvas).canvas;
        
        canvas.addDomHandler(new TouchStartHandler() {
            @Override
            public void onTouchStart(TouchStartEvent e) {
             // TODO Check
                if (e.getSource() == canvas) {
                    e.preventDefault();
                    // TODO
                    //e.returnValue = false;
                    
                    JsArray<Touch> targetTouches = e.getTargetTouches();
                    Touch touch = targetTouches.get(0);
                    
                    __touchStartHandler(director, e);
                }
            }
        }, TouchStartEvent.getType());
        
        canvas.addDomHandler(new TouchMoveHandler() {
            @Override
            public void onTouchMove(TouchMoveEvent e) {

                if (director.touching) {
                    e.preventDefault();
                    // TODO
                    // e.returnValue = false;

                    if (director.gesturing) {
                        return;
                    }

                    JsArray<Touch> targetTouches = e.getTargetTouches();

                    for (int i = 0; i < targetTouches.length(); i++) {
                        Touch ee = targetTouches.get(i);
                        __touchMoveHandler(director, e);
                    }
                }
            }
        }, TouchMoveEvent.getType());
        
        canvas.addDomHandler(new TouchEndHandler() {
            @Override
            public void onTouchEnd(TouchEndEvent e) {
                
                if (director.touching) {
                    e.preventDefault();
                 // TODO
                    //e.returnValue = false;
                    JsArray<Touch> targetTouches = e.getTargetTouches();
                    Touch touch = targetTouches.get(0);
                    
                    __touchEndHandler(director, e);
                }
            }
        }, TouchEndEvent.getType());
        
        canvas.addDomHandler(new GestureStartHandler() {
            @Override
            public void onGestureStart(GestureStartEvent event) {
                Element element = Element.as(event.getNativeEvent().getEventTarget());
                if ( element==canvas.getElement() ) {
                    event.preventDefault();
                    // TODO
//                    e.returnValue = false;
                    director.__gestureStart(event.getScale(), event.getRotation() );
                }
            }
        }, GestureStartEvent.getType());
        
        canvas.addDomHandler(new GestureEndHandler() {
            @Override
            public void onGestureEnd(GestureEndEvent event) {
                Element element = Element.as(event.getNativeEvent().getEventTarget());
                if ( element == canvas.getElement() ) {
                    event.preventDefault();
                    // TODO
//                  e.returnValue = false;
                    director.__gestureEnd(event.getScale(), event.getRotation() );
                }
            }
        }, GestureEndEvent.getType());
        
        canvas.addDomHandler(new GestureChangeHandler() {
            @Override
            public void onGestureChange(GestureChangeEvent event) {
                Element element = Element.as(event.getNativeEvent().getEventTarget());
                if ( element == canvas.getElement() ) {
                    event.preventDefault();
                    // TODO
//                  e.returnValue = false;
                    director.__gestureChange(event.getScale(), event.getRotation() );
                }
            }
        }, GestureChangeEvent.getType());
        
    }

    @Override
    public void addMultiTouchHandlers(final Director director, CaatjaCanvas caatjaCanvas) {
        
        final Canvas canvas = ((CaatjaGwtCanvas)caatjaCanvas).canvas;
        
        canvas.addDomHandler(new TouchStartHandler() {
            @Override
            public void onTouchStart(TouchStartEvent e) {
                
                e.preventDefault();
                // TODO
                   //e.returnValue = false;

                   int i;
                   List<Integer> recent= new ArrayList<Integer>();
                   boolean allInCanvas= true;

                   /**
                    * extrae actores afectados, y coordenadas relativas para ellos.
                    * crear una coleccion touch-id : { actor, touch-event }
                    */
                   JsArray<Touch> touches = e.getChangedTouches();
                   for( i=0; i< touches.length(); i++ ) {
                       Touch touch= touches.get(i);
                       int id= touch.getIdentifier();
                       Pt mp= director.mousePoint;
                       // FIXME
//                       this.getCanvasCoord(mp, touch);
                       if ( mp.x<0 || mp.y<0 || mp.x>=director.width || mp.y>=director.height ) {
                           allInCanvas = false;
                           continue;
                       }

                       Actor actor= director.findActorAtPosition(mp);
                       if ( actor!=null ) {
                           mp= actor.viewToModel(mp);

                           if ( director.touches.get(id) == null) {

                               director.touches.put(String.valueOf(id), new TouchInfoData( 
                                   actor,
                                   new TouchInfo(String.valueOf(id), (int)mp.x, (int)mp.y, actor )
                               ));

                               recent.add( id );
                           }
                       }
                   }
                   
                   /**
                    * para los touch identificados, extraer que actores se han afectado.
                    * crear eventos con la info de touch para cada uno.
                    */

                   Map<String, TouchEventData> actors= new HashMap<String, TouchEventData>();
                   for( i=0; i<recent.size(); i++ ) {
                       Integer touchId= recent.get(i);
                       Actor actor= director.touches.get( touchId ).actor;

                       if ( actors.get(actor.id) == null) {
                           actors.put(actor.id, new TouchEventData(
                               actor,
                               // TODO Check 
                               // new CAATTouchEvent().init( e, actor, director.currentScene.time )
                               new CAATTouchEvent().init(actor, director.currentScene.time )
                           ));
                       }

                       CAATTouchEvent ev= actors.get( actor.id ).touch;
                       ev.addTouch( director.touches.get( touchId).touch );
                       ev.addChangedTouch( director.touches.get( touchId ).touch );
                   }

                   /**
                    * notificar a todos los actores.
                    */
                   for( String pr : actors.keySet() ) {
                       TouchEventData data= actors.get(pr);
                       Actor actor= data.actor;
                       CAATTouchEvent touch= data.touch;

                       for( String actorId : director.touches.keySet() ) {
                           TouchInfoData tt= director.touches.get(actorId);
                           if ( tt.actor.id.equals(actor.id)) {
                               touch.addTouch( tt.touch );
                           }
                       }

                       actor.touchStart( touch );
                   }
                
            }
        }, TouchStartEvent.getType());
        
        canvas.addDomHandler(new TouchMoveHandler() {
            @Override
            public void onTouchMove(TouchMoveEvent e) {
                
                e.preventDefault();
                // TODO
                   //e.returnValue = false;

                   int i;
                   List<Integer> recent= new ArrayList<Integer>();

                   /**
                    * extrae actores afectados, y coordenadas relativas para ellos.
                    * crear una coleccion touch-id : { actor, touch-event }
                    */
                   JsArray<Touch> touches = e.getChangedTouches();
                   for( i=0; i< touches.length(); i++ ) {
                       Touch touch= touches.get(i);
                       int id= touch.getIdentifier();
                       
                       if (director.touches.get(String.valueOf(id)) != null) {
                           Pt mp = director.mousePoint;
                           // FIXME
//                           this.getCanvasCoord(mp, touch);
                           
                           Actor actor= director.touches.get( id ).actor;
                           mp= actor.viewToModel(mp);
                           
                           director.touches.put(String.valueOf(id), new TouchInfoData(actor, new TouchInfo(String.valueOf(id), (int) mp.x, (int) mp.y, actor )));

                           recent.add( id );
                       }



                   }

                   /**
                    * para los touch identificados, extraer que actores se han afectado.
                    * crear eventos con la info de touch para cada uno.
                    */

                   Map<String, TouchEventData> actors= new HashMap<String, TouchEventData>();
                   for( i=0; i<recent.size(); i++ ) {
                       int touchId= recent.get(i);
                       Actor actor= director.touches.get( touchId).actor;

                       if ( actors.get(actor.id) == null) {
                           actors.put(actor.id, new TouchEventData(
                               actor,
                               new CAATTouchEvent().init(actor, director.currentScene.time )
                           ));
                       }

                       CAATTouchEvent ev= actors.get(actor.id).touch;
                       ev.addTouch( director.touches.get( touchId ).touch );
                       ev.addChangedTouch( director.touches.get( touchId ).touch );
                   }

                   /**
                    * notificar a todos los actores.
                    */
                   for( String pr : actors.keySet() ) {
                       TouchEventData data= actors.get(pr);
                       Actor actor= data.actor;
                       CAATTouchEvent touch= data.touch;

                       for( String actorId : director.touches.keySet() ) {
                           TouchInfoData tt= director.touches.get(actorId);
                           if ( tt.actor.id.equals(actor.id )){
                               touch.addTouch( tt.touch );
                           }
                       }

                       actor.touchMove( touch );
                   }
                
            }
        }, TouchMoveEvent.getType());
        
        canvas.addDomHandler(new TouchEndHandler() {
            @Override
            public void onTouchEnd(TouchEndEvent e) {
                touchEndHandlerMT(director, e);
            }

        }, TouchEndEvent.getType());
        
        canvas.addDomHandler(new TouchCancelHandler() {
            @Override
            public void onTouchCancel(TouchCancelEvent e) {
                touchEndHandlerMT(director, e);
            }
        }, TouchCancelEvent.getType());
        
        canvas.addDomHandler(new GestureStartHandler() {
            @Override
            public void onGestureStart(GestureStartEvent event) {
                __touchGestureStartHandleMT(director, event);
            }
        }, GestureStartEvent.getType());
        
        canvas.addDomHandler(new GestureEndHandler() {
            @Override
            public void onGestureEnd(GestureEndEvent event) {
                __touchGestureEndHandleMT(director, event);
            }
        }, GestureEndEvent.getType());
        
        canvas.addDomHandler(new GestureChangeHandler() {
            @Override
            public void onGestureChange(GestureChangeEvent event) {
                __touchGestureChangeHandleMT(director, event);
            }
        }, GestureChangeEvent.getType());
        
    }
    
    private void touchEndHandlerMT(final Director director, TouchEvent e) {
        e.preventDefault();
        // TODO
           //e.returnValue = false;

           int i,j;
           List<Integer> recent= new ArrayList<Integer>();

           /**
            * extrae actores afectados, y coordenadas relativas para ellos.
            * crear una coleccion touch-id : { actor, touch-event }
            */
           JsArray<Touch> touches = e.getChangedTouches();
           for( i=0; i< touches.length(); i++ ) {
               Touch _touch= touches.get(i);
               int id= _touch.getIdentifier();
               recent.add( id );
           }

           /**
            * para los touch identificados, extraer que actores se han afectado.
            * crear eventos con la info de touch para cada uno.
            */

           Map<String, TouchEventData> actors= new HashMap<String, TouchEventData>();
           for( i=0; i<recent.size(); i++ ) {
               int touchId= recent.get( i );
               if ( director.touches.get( touchId ) != null) {
                   Actor actor= director.touches.get(touchId).actor;

                   if ( actors.get(actor.id) == null) {
                       actors.put(actor.id,new TouchEventData( 
                           actor,
                           new CAATTouchEvent().init(actor, director.currentScene.time )));
                   }

                   CAATTouchEvent ev= actors.get( actor.id ).touch;
                   ev.addChangedTouch( director.touches.get( touchId ).touch );
               }
           }

           /**
            * remove ended touch info.
            */
           for( i=0; i< touches.length(); i++ ) {
               Touch touch= touches.get(i);
               int id= touch.getIdentifier();
               director.touches.remove(id);
           }

           /**
            * notificar a todos los actores.
            */
           for( String pr : actors.keySet() ) {
               TouchEventData data= actors.get(pr);
               Actor actor= data.actor;
               CAATTouchEvent touch= data.touch;

               for( String actorId : director.touches.keySet() ) {
                   TouchInfoData tt= director.touches.get(actorId);
                   if ( tt.actor.id.equals(actor.id)) {
                       touch.addTouch( tt.touch );
                   }
               }

               actor.touchEnd( touch );
           }
    }
    
    
    private void onMouseUp(Director director, MouseUpEvent event) {
        Pt mp= director.mousePoint;
        getCanvasCoord(director, mp, event);
        try {
            __mouseUpHandler(director, event);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        director.touching = false;
    }
    
    public void onMouseDown(Director director, MouseDownEvent event) {
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
    
    public void onMouseOver(Director director, MouseOverEvent event) throws Exception {
        Pt mp= director.mousePoint;
        getCanvasCoord(director, mp, event);
        if ( mp.x<0 || mp.y<0 || mp.x>=director.width || mp.y>=director.height ) {
            return;
        }
        __mouseOverHandler(director, event);
    }
    
    public void onMouseOut(Director director, MouseOutEvent event) throws Exception {
        Pt mp= director.mousePoint;
        getCanvasCoord(director, mp, event);
        __mouseOutHandler(director, event);
    }
    
    public void onMouseMove(Director director,MouseMoveEvent event) {
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
    
    public void onDoubleClick(Director director, DoubleClickEvent event) {
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
    
    public void __mouseMoveHandler (Director director, MouseEvent e) throws Exception {
//      this.getCanvasCoord(this.mousePoint, e);

      Actor lactor;
      Pt pos;
      
      double ct = director.currentScene != null ? director.currentScene.time : 0;

      // drag

      if (director.isMouseDown && null != director.lastSelectedActor) {

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

  public void __mouseOverHandler (Director director, MouseEvent e) throws Exception {
      
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
  
    /**
     * Same as mouseDown but not preventing event. Will only take care of first touch.
     * 
     * @param e
     */
    public void __touchStartHandler(Director director, TouchEvent e) {

        Pt mp = director.mousePoint;
        // TODO FIXME
        // this.getCanvasCoord(mp, e);
        if (mp.x < 0 || mp.y < 0 || mp.x >= director.width || mp.y >= director.height) {
            return;
        }

        director.touching = true;

        // TODO FIXME
        // this.__mouseDownHandler(e);

    }

    public void __touchEndHandler(Director director, TouchEvent e) {

        Pt mp = director.mousePoint;

        // TODO FIXME
        // this.getCanvasCoord(mp, e);

        director.touching = false;

        // TODO FIXME
        // this.__mouseUpHandler(e);
    }

    public void __touchMoveHandler(Director director, TouchEvent e) {

        Pt mp = director.mousePoint;
        // TODO FIXME
        // this.getCanvasCoord(mp, ee);
        // TODO FIXME
        // this.__mouseMoveHandler(targetTouches.get(i));

    }
    
    public void __touchGestureStartHandleMT (Director director, GestureStartEvent e ) {
        Actor actor= director.__findTouchFirstActor();

        if ( actor!=null && actor.isGestureEnabled() ) {
            director.__gesturedActor= actor;
            director.__gestureRotation= actor.rotationAngle;
            director.__gestureSX= actor.scaleX - 1;
            director.__gestureSY= actor.scaleY - 1;


            actor.gestureStart(
                e.getRotation()* Math.PI / 180,
                e.getScale() + director.__gestureSX,
                e.getScale() + director.__gestureSY );
        }
    }

    public void __touchGestureEndHandleMT (Director director, GestureEndEvent e ) {

        if ( null!=director.__gesturedActor && director.__gesturedActor.isGestureEnabled()) {
            director.__gesturedActor.gestureEnd(
                e.getRotation()* Math.PI / 180,
                e.getScale() + director.__gestureSX,
                e.getScale() + director.__gestureSY );
        }

        director.__gestureRotation= 0;
        director.__gestureScale= 0;


    }

    public void __touchGestureChangeHandleMT (Director director, GestureChangeEvent e ) {

        if (director.__gesturedActor!= null && director.__gesturedActor.isGestureEnabled()) {
            director.__gesturedActor.gestureChange(
                e.getRotation()* Math.PI / 180,
                director.__gestureSX + e.getScale(),
                director.__gestureSY + e.getScale());
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
        posx = e.getClientX();
        posy = e.getClientY();

        double pposx = posx;
        double pposy = posy;
        
        Object node = e.getSource();
        while (! (node instanceof RootPanel)) {
            
            Widget widget = (Widget) node;
            
            if (widget.getAbsoluteLeft() != 0 && widget.getAbsoluteTop() != 0) {
                pposx -= widget.getAbsoluteLeft();
                pposy -= widget.getAbsoluteTop();
                break;
            }
            
            node = widget.getParent();
        }
        
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
