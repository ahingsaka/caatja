package com.katspow.caatja.core.interfaces;

public interface CaatjaStorage {
	
	CaatjaStorage save(String key, String data);
	
	String load(String key);
	
	CaatjaStorage remove(String key);

}
