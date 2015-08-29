package com.katspow.caatjagwt.client;

import com.google.gwt.storage.client.Storage;
import com.katspow.caatja.core.interfaces.CaatjaStorage;

public class CaatjaGwtStorage implements CaatjaStorage {

	private Storage storage = null;

	public CaatjaGwtStorage() {
		storage = Storage.getLocalStorageIfSupported();
	}

	@Override
	public CaatjaStorage save(String key, String data) {
		storage.setItem(key, data);
		return this;
	}

	@Override
	public String load(String key) {
		return storage.getItem(key);
	}

	@Override
	public CaatjaStorage remove(String key) {
		storage.removeItem(key);
		return this;
	}

}
