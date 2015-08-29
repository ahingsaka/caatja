package com.katspow.caatja.stats;

//import com.google.gwt.dom.client.Node;
//import com.google.gwt.dom.client.Style;
//import com.google.gwt.dom.client.Style.Cursor;
//import com.google.gwt.dom.client.Style.Display;
//import com.google.gwt.dom.client.Style.FontWeight;
//import com.google.gwt.dom.client.Style.Position;
//import com.google.gwt.dom.client.Style.Unit;
//import com.google.gwt.user.client.DOM;
//import com.google.gwt.user.client.Element;
//import com.google.gwt.user.client.Event;
//import com.google.gwt.user.client.EventListener;
import com.katspow.caatja.core.Caatja;

// Not fully working, but at least show fps and ms
// The fps graph does not show anything ?
public class Stats {
    
    private double startTime;
    private double prevTime;
    private double ms;
    private double msMin;
    private double msMax;
    private int fps;
    private int fpsMin;
    private int fpsMax;
    private int frames;
    private int mode;
    
//    public Element domElement;
//    private Element fpsDiv;
//    private Element fpsText;
//    private Element fpsGraph;
//    private Element msDiv;
//    private Element msText;
//    private Element msGraph;
    
    public Stats() {
        startTime = Caatja.getTime();
        prevTime = startTime;
        ms = 0;
        msMin = Double.MAX_VALUE;
        msMax = 0;
        fps = 0;
        fpsMin = Integer.MAX_VALUE;
        fpsMax = 0;
        frames = 0;
        mode = 0;
        
//        domElement = DOM.createDiv();
//        domElement.setId("stats");
//        DOM.sinkEvents(domElement, Event.ONMOUSEDOWN);
//        DOM.setEventListener(domElement, new EventListener() {
//            @Override
//            public void onBrowserEvent(Event event) {
//                event.preventDefault();
//                setMode(++ mode % 2);
//            }
//        });
//        domElement.getStyle().setWidth(80, Unit.PX);
//        domElement.getStyle().setOpacity(0.9);
//        domElement.getStyle().setCursor(Cursor.POINTER);
//        
//        fpsDiv = DOM.createDiv();
//        fpsDiv.setId("fps");
//        fpsDiv.getStyle().setPaddingTop(0, Unit.PX);
//        fpsDiv.getStyle().setPaddingRight(0, Unit.PX);
//        fpsDiv.getStyle().setPaddingBottom(3, Unit.PX);
//        fpsDiv.getStyle().setPaddingLeft(3, Unit.PX);
//        fpsDiv.getStyle().setBackgroundColor("#002");
//        DOM.setStyleAttribute(fpsDiv, "textAlign", "LEFT");
//        domElement.appendChild(fpsDiv);
//        
//        fpsText = DOM.createDiv();
//        fpsText.setId("fpsText");
//        fpsText.getStyle().setColor("#0ff");
//        fpsText.getStyle().setFontSize(9, Unit.PX);
//        fpsText.getStyle().setFontWeight(FontWeight.BOLD);
//        DOM.setStyleAttribute(fpsText, "fontFamily", "Helvetica,Arial,sans-serif");
//        DOM.setStyleAttribute(fpsText, "lineHeight", "15px");
//        fpsText.setInnerHTML("FPS");
//        fpsDiv.appendChild(fpsText);
//        
//        fpsGraph = DOM.createDiv();
//        fpsGraph.setId("fpsGraph");
//        fpsGraph.getStyle().setPosition(Position.RELATIVE);
//        fpsGraph.getStyle().setWidth(74, Unit.PX);
//        fpsGraph.getStyle().setHeight(30, Unit.PX);
//        fpsGraph.getStyle().setBackgroundColor("#0ff");
//        fpsDiv.appendChild(fpsGraph);
//        
//        while (fpsGraph.getChildCount() < 74) {
//            Element bar = DOM.createSpan();
//            Style style = bar.getStyle();
//            style.setWidth(1, Unit.PX);
//            style.setHeight(30, Unit.PX);
//            style.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
//            style.setBackgroundColor("#113");
//            fpsGraph.appendChild(bar);
//        }
//        
//        msDiv = DOM.createDiv();
//        msDiv.setId("ms");
//        msDiv.getStyle().setPaddingTop(0, Unit.PX);
//        msDiv.getStyle().setPaddingRight(0, Unit.PX);
//        msDiv.getStyle().setPaddingBottom(3, Unit.PX);
//        msDiv.getStyle().setPaddingLeft(3, Unit.PX);
//        DOM.setStyleAttribute(msDiv, "textAlign", "LEFT");
//        msDiv.getStyle().setBackgroundColor("#002");
//        msDiv.getStyle().setDisplay(Display.NONE);
//        domElement.appendChild(msDiv);
//        
//        msText = DOM.createDiv();
//        msText.setId("msText");
//        msText.getStyle().setColor("#0f0");
//        DOM.setStyleAttribute(msText, "fontFamily", "Helvetica,Arial,sans-serif");
//        msText.getStyle().setFontSize(9, Unit.PX);
//        msText.getStyle().setFontWeight(FontWeight.BOLD);
//        DOM.setStyleAttribute(msText, "lineHeight", "15px");
//        msText.setInnerHTML("MS");
//        msDiv.appendChild(msText);
//        
//        msGraph = DOM.createDiv();
//        msGraph.setId("msGraph");
//        msGraph.getStyle().setPosition(Position.RELATIVE);
//        msGraph.getStyle().setWidth(74, Unit.PX);
//        msGraph.getStyle().setHeight(30, Unit.PX);
//        msGraph.getStyle().setBackgroundColor("#0f0");
//        msDiv.appendChild(msGraph);
//        
//        while (msGraph.getChildCount() < 74) {
//            Element bar = DOM.createSpan();
//            Style style = bar.getStyle();
//            style.setWidth(1, Unit.PX);
//            style.setHeight(30, Unit.PX);
//            style.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
//            style.setBackgroundColor("#113");
//            msGraph.appendChild(bar);
//        }
        
    }
    
    public void setMode(int value) {
        mode = value;
//        switch (mode) {
//        case 0:
//            fpsDiv.getStyle().setDisplay(Display.BLOCK);
//            msDiv.getStyle().setDisplay(Display.NONE);
//            break;
//        case 1:
//            fpsDiv.getStyle().setDisplay(Display.NONE);
//            msDiv.getStyle().setDisplay(Display.BLOCK);
//            break;
//
//        default:
//            break;
//        }
    }
    
//    public void updateGraph(Element domElement, int value) {
//        Node child = domElement.appendChild(domElement.getChild(0));
//        DOM.setStyleAttribute((Element) child, "height", value + "px");
//    }
    
    public void begin() {
        startTime = Caatja.getTime();
    }
    
    // TODO Check if innerText works like textContent
    public double end() {
        double time = Caatja.getTime();
        
        ms = time - startTime;
        msMin = Math.min( msMin, ms );
        msMax = Math.max( msMax, ms );

//        msText.setInnerText(ms + " MS (" + msMin + "-" + msMax + ")");
//        updateGraph( msGraph, (int) Math.min( 30, 30 - ( ms / 200 ) * 30 ) );
//
//        frames ++;
//
//        if ( time > prevTime + 1000 ) {
//
//            fps = (int) Math.round( ( frames * 1000 ) / ( time - prevTime ) );
//            fpsMin = Math.min( fpsMin, fps );
//            fpsMax = Math.max( fpsMax, fps );
//
//            fpsText.setInnerText(fps + " FPS (" + fpsMin + "-" + fpsMax + ")");
//            updateGraph( fpsGraph, (int) Math.min( 30, 30 - ( fps / 100 ) * 30 ) );
//
//            prevTime = time;
//            frames = 0;
//
//        }
        
        return time;
    }
    
    public void update() {
        startTime = end();
    }

}
