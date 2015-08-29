CAATJA
======

#### Java animation framework based on CAAT ####

This project is a JAVA conversion of [CAAT](https://github.com/hyperandroid/caat), a great Javascript framework.
The goal of CAATJA, is to be used by any Java drawing/rendering technology that supports or gives a canvas API similar to HTML5 Canvas.

Thus CAATJA alone, is useless. You must have an implementation.
There are 2 implementations : one in GWT (caatja-gwt) and the other one in JAVA FX (caatja-fx).

You can see CAATJA in action here : [http://caatja-gwt-demos.appspot.com/](http://caatja-gwt-demos.appspot.com/)

#### A short example ####

```java
// A red circle outlined in black
CaatjaCanvas canvas = Caatja.createCanvas();
Caatja.addCanvas(canvas);
Director director = new Director().initialize(100, 100, canvas);
        
Scene scene = director.createScene();
scene.setFillStyle("#ffffff");

ShapeActor circle = new ShapeActor()
	.setLocation(20, 20)
	.setSize(60, 60)
	.setFillStrokeStyle(CaatjaColor.valueOf("#ff0000"));
        
circle.setStrokeStyle(CaatjaColor.valueOf("#000000"));

scene.addChild(circle);
director.addScene(scene);

Caatja.loop(1);
```
