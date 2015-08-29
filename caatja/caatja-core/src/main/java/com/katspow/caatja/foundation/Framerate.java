package com.katspow.caatja.foundation;

public class Framerate {

    public int refreshInterval; // refresh every ? ms; updating too quickly
                                // gives too large rounding errors
    public int frames; // number offrames since last refresh
    public double timeLastRefresh; // When was the framerate counter refreshed
                                   // last
    public int fps; // current framerate
    public int prevFps; // previously drawn FPS
    public int fpsMin; // minimum measured framerate
    public int fpsMax; // maximum measured framerate

}
