package com.katspow.caatjafx;

import java.util.Date;

import com.katspow.caatja.core.interfaces.CaatjaDate;

public class CaatjaFxDate implements CaatjaDate {

	@Override
	public double getTime() {
		return new Date().getTime();
	}

}
