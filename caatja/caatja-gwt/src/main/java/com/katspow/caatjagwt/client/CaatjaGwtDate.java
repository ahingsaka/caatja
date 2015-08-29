package com.katspow.caatjagwt.client;

import com.google.gwt.core.client.JsDate;
import com.katspow.caatja.core.interfaces.CaatjaDate;

public class CaatjaGwtDate implements CaatjaDate {

	@Override
	public double getTime() {
		return JsDate.create().getTime();
	}

}
