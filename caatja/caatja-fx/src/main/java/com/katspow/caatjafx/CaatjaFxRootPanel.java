package com.katspow.caatjafx;

import com.katspow.caatja.core.canvas.CaatjaCanvas;
import com.katspow.caatja.core.interfaces.CaatjaRootPanel;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;

public class CaatjaFxRootPanel implements CaatjaRootPanel {
	
	public Pane root;
	private Scene fxScene;
	
	public CaatjaFxRootPanel(int width, int height) {
		root = new Pane();
		fxScene = new Scene(root, width, height);
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
