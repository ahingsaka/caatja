package com.katspow.caatjagwt.client;

import com.google.gwt.user.client.Window;
import com.katspow.caatja.core.interfaces.CaatjaWindow;

public class CaatjaGwtWindow implements CaatjaWindow {

	@Override
	public void alert(String msg) {
		Window.alert(msg);
	}

	@Override
	public int getClientHeight() {
		return Window.getClientHeight();
	}

	@Override
	public int getClientWidth() {
		return Window.getClientWidth();
	}

    @Override
    public boolean confirm(String msg) {
        return Window.confirm(msg);
    }

    @Override
    public void redirect(String url) {
        Window.Location.replace(url);
    }

}
