package com.katspow.caatjagwt.client;

import com.google.gwt.user.client.Window.Navigator;
import com.katspow.caatja.core.interfaces.CaatjaNavigator;

public class CaatjaGwtNavigator implements CaatjaNavigator {

	@Override
	public String getUserAgent() {
		return Navigator.getUserAgent();
	}

	@Override
	public String getPlatform() {
		return Navigator.getPlatform();
	}

	@Override
	public String getAppVersion() {
		return Navigator.getAppVersion();
	}

}
