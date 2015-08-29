package com.katspow.caatjafx;

import java.util.prefs.Preferences;
import com.katspow.caatja.core.interfaces.CaatjaStorage;

public class CaatjaFxStorage implements CaatjaStorage {
	
	private Preferences prefs;
	
	public CaatjaFxStorage() {
		prefs = Preferences.userRoot().node(this.getClass().getName());
	}

	@Override
	public CaatjaStorage save(String key, String data) {
		prefs.put(key, data);
		return this;
	}

	@Override
	public String load(String key) {
		return prefs.get(key, null);
	}

	@Override
	public CaatjaStorage remove(String key) {
		prefs.remove(key);
		return this;
	}

}
