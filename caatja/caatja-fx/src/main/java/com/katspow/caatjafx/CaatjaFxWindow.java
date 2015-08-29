package com.katspow.caatjafx;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

import com.katspow.caatja.core.interfaces.CaatjaWindow;
import com.katspow.caatjafx.popup.AlertDialog;
import com.katspow.caatjafx.popup.AlertConfirmDialog;;

public class CaatjaFxWindow implements CaatjaWindow {
    
    private Stage owner;

	@Override
	public void alert(String msg) {
	    new AlertDialog(this.owner, msg).showAndWait();
	}

	@Override
	public int getClientHeight() {
		Screen screen = Screen.getPrimary();
	    Rectangle2D bounds = screen.getVisualBounds();
	    return (int) bounds.getHeight();
	}

	@Override
	public int getClientWidth() {
		Screen screen = Screen.getPrimary();
	    Rectangle2D bounds = screen.getVisualBounds();
	    return (int) bounds.getWidth();
	}

    @Override
    public boolean confirm(String msg) {
        return AlertConfirmDialog.show(owner, msg);
    }
    
    public void setOwner(Stage owner) {
        this.owner = owner;
    }

    @Override
    public void redirect(String url) {
        // TODO
        System.exit(0);
    }

}
