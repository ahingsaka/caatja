package com.katspow.caatja.modules.texturepacker;

public class FreeChunk {

    public static int position;
    public int size;

    public FreeChunk(int position, int size) {
        this.position = position;
        this.size = size;
    }

    public int getPosition() {
        
        if (position == 0) {
            return 1024;
        }
        
        return position;
    }

}
