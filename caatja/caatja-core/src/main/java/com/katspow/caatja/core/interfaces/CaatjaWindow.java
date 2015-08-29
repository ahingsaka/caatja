package com.katspow.caatja.core.interfaces;

public interface CaatjaWindow {

    public void alert(String msg);

    public int getClientHeight();

    public int getClientWidth();

    public boolean confirm(String msg);

    public void redirect(String url);
}
