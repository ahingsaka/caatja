CAATJA
======

#### Java animation framework based on CAAT ####

This project is a JAVA conversion of [CAAT](https://github.com/hyperandroid/caat), a great Javascript framework.
The goal of CAATJA, is to be used by any Java drawing/rendering technology that supports or gives a canvas API similar to HTML5 Canvas.

Thus CAATJA alone, is useless. You must have an implementation.
There are 2 implementations : one in GWT (caatja-gwt) and the other one in JAVA FX (caatja-fx).

You can see CAATJA in action here : [http://caatja-gwt-demos.appspot.com/](http://caatja-gwt-demos.appspot.com/)

#### A quick example ####

```java
package com.katspow.example;

import com.katspow.caatja.behavior.BaseBehavior;
import com.katspow.caatja.behavior.BehaviorListener;
import com.katspow.caatja.behavior.RotateBehavior;
import com.katspow.caatja.behavior.ScaleBehavior;
import com.katspow.caatja.behavior.listener.BehaviorExpiredListener;
import com.katspow.caatja.core.Caatja;
import com.katspow.caatja.core.canvas.CaatjaCanvas;
import com.katspow.caatja.core.canvas.CaatjaColor;
import com.katspow.caatja.foundation.Director;
import com.katspow.caatja.foundation.Scene;
import com.katspow.caatja.foundation.actor.Actor;
import com.katspow.caatja.foundation.ui.ShapeActor;
import com.katspow.caatja.foundation.ui.ShapeActor.Shape;
import com.katspow.caatjafx.CaatjaFx;

import javafx.application.Application;
import javafx.stage.Stage;

public class Example extends Application {

	private CaatjaCanvas canvas;
	private Director director;

	@Override
	public void start(Stage primaryStage) throws Exception {

		// Init some Caatja stuff
		CaatjaFx.init(320, 240, primaryStage, null);
		canvas = Caatja.createCanvas();
		Caatja.addCanvas(canvas);
		director = new Director();
		director.initialize(320, 240, canvas);
		Caatja.loop(24);

		// Let's create a scene and make the background black
		Scene scene = director.createScene();

		// Let's create a red square of 60x60
		ShapeActor square = new ShapeActor();
		square.setShape(Shape.RECTANGLE);
		square.setFillStrokeStyle(CaatjaColor.valueOf("#ff0000"));
		square.setSize(60, 60);

		// Let's place it on the scene
		scene.addChild(square);
		square.setLocation(50, 50);

		// Let's create a scaling animation for the square
		final ScaleBehavior scaleBehavior = new ScaleBehavior();
		scaleBehavior.setPingPong();
		scaleBehavior.setValues(1d, 2d, 1d, 2d, .5, .5);

		// The animation starts at time=2000ms and last for 3000ms
		scaleBehavior.setFrameTime(2000, 3000);

		// Let's create a rotating animation for the square
		final RotateBehavior rotateBehavior = new RotateBehavior();
		rotateBehavior.setValues(0, 2 * Math.PI, .5, .5);

		// Let's add those animations to the square
		square.addBehavior(scaleBehavior);
		square.addBehavior(rotateBehavior);

		// When the scale animation finishes, we start the rotating animation
		scaleBehavior.addListener(BehaviorListener.valueOfExpired(new BehaviorExpiredListener() {
			public void onExpired(BaseBehavior behavior, double time, Actor actor) {
				rotateBehavior.setFrameTime(time, 3000);
			}
		}));

		// When the rotating animation finishes, we start the caling animation
		rotateBehavior.addListener(BehaviorListener.valueOfExpired(new BehaviorExpiredListener() {
			public void onExpired(BaseBehavior behavior, double time, Actor actor) {
				scaleBehavior.setFrameTime(time, 3000);
			}
		}));

	}

	public static void main(String[] args) {
		launch(args);
	}

}
```

Here is the result :



#### Want to use it ? ####

If you want to use it in a Maven project, add this repository in the <repositories> definition of the POM file :

```xml
<repository>
   <id>bintray-repository</id>
   <url>https://dl.bintray.com/ahingsaka/maven</url>
</repository>
```

And add the following dependency (change the artifact id to 'caatja-gwt' for a GWT application) :

```xml
<dependency>
   <groupId>com.katspow.caatja</groupId>
   <artifactId>caatja-fx</artifactId>
   <version>0.7.6</version>
</dependency>
```
