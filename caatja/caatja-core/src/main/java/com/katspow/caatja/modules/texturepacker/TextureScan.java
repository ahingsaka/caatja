package com.katspow.caatja.modules.texturepacker;

import java.util.ArrayList;
import java.util.List;

import com.katspow.caatja.core.CAAT;

public class TextureScan {

    public List<FreeChunk> freeChunks;
    
    public TextureScan(int w) {
        FreeChunk.position = w;
        this.freeChunks = new ArrayList<FreeChunk>();
    }

    /**
     * return an array of values where a chunk of width size fits in this scan.
     * 
     * @param width
     */
    public List<Integer> findWhereFits(int width) {
        if (this.freeChunks.size() == 0) {
            return new ArrayList<Integer>();
        }

        List<Integer> fitsOnPosition = new ArrayList<Integer>();

        for (FreeChunk fc : this.freeChunks) {
            int pos = 0;
            while (pos + width <= fc.size) {
                fitsOnPosition.add(pos + fc.getPosition());
                pos += width;
            }
        }

        return fitsOnPosition;
    }

    public boolean fits(int position, int size) {
        for (FreeChunk fc : this.freeChunks) {
            if (fc.getPosition() <= position && position + size <= fc.getPosition() + fc.size) {
                return true;
            }
        }

        return false;
    }

    // FIXME Wont work
    public boolean substract(int position, int size) {

        int i = 0;

        for (FreeChunk fc : this.freeChunks) {

            if (fc.getPosition() <= position && position + size <= fc.getPosition() + fc.size) {
                int lp = 0;
                int ls = 0;
                int rp = 0;
                int rs = 0;

                lp = fc.getPosition();
                ls = position - fc.getPosition();

                rp = position + size;
                rs = fc.getPosition() + fc.size - rp;

                // this.freeChunks.splice(i,1);
                this.freeChunks.remove(i);

                if (ls > 0) {
                    this.freeChunks.add(i++, new FreeChunk(lp, ls));
                    // this.freeChunks.splice( i++,0, new FreeChunk(lp, ls) );
                }
                if (rs > 0) {
                    this.freeChunks.add(i, new FreeChunk(rp, rs));
                    // this.freeChunks.splice( i,0, new FreeChunk(rp, rs));
                }

                return true;
            }

            i++;
        }

        return false;
    }

    public void log(int index) {
        if (0 == this.freeChunks.size()) {
            CAAT.log("index " + index + " empty");
        } else {
            String str = "index " + index;

            for (FreeChunk fc : this.freeChunks) {
                str += "[" + fc.getPosition() + "," + fc.size + "]";
            }

            CAAT.log(str);
        }
    }

}
