package com.katspow.caatjafx;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;

import com.katspow.caatja.core.canvas.CaatjaCanvas;
import com.katspow.caatja.core.interfaces.CaatjaRootPanel;

public class CaatjaFxRootPanel implements CaatjaRootPanel {
	
	public Pane root;
	private Scene fxScene;
	
	public CaatjaFxRootPanel() {
		root = new Pane();
		fxScene = new Scene(root);
	}
	
	public Scene getFxScene() {
		return fxScene;
	}

	@Override
	public void addCanvas(CaatjaCanvas canvas) {
		root.getChildren().add(((CaatjaFxCanvas) canvas).canvas);
	}

	@Override
	public void addCanvas(CaatjaCanvas caatjaCanvas, int x, int y) {
	    Canvas canvas = ((CaatjaFxCanvas) caatjaCanvas).canvas;
	    canvas.setTranslateX(x);
	    canvas.setTranslateY(y);
	    
	    root.getChildren().add(canvas);
	}

    public Pane getFxPane() {
        return root;
    }

}
