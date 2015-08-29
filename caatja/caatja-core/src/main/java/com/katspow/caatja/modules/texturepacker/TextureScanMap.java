package com.katspow.caatja.modules.texturepacker;

import java.util.ArrayList;
import java.util.List;

import com.katspow.caatja.core.CAAT;
import com.katspow.caatja.math.Pt;

public class TextureScanMap {

    List<TextureScan> scanMap = null;
    int scanMapWidth = 0;
    int scanMapHeight = 0;
    
    public TextureScanMap(int w, int h) {
        this.scanMapHeight= h;
        this.scanMapWidth= w;

        this.scanMap= new ArrayList<TextureScan>();
        for( int i=0; i<this.scanMapHeight; i++ ) {
            this.scanMap.add( new TextureScan(this.scanMapWidth) );
        }
    }
    
    /**
     * Always try to fit a chunk of size width*height pixels from left-top.
     * @param width
     * @param height
     */
    public Pt whereFitsChunk (int width, int height ) {

        // trivial rejection:
        if ( width>this.scanMapWidth||height>this.scanMapHeight) {
            return null;
        }

        // find first fitting point
        int i,j,initialPosition= 0;

        while( initialPosition<=this.scanMapHeight-height) {

            // para buscar sitio se buscar� un sitio hasta el tama�o de alto del trozo.
            // mas abajo no va a caber.

            // fitHorizontalPosition es un array con todas las posiciones de este scan donde
            // cabe un chunk de tama�o width.
            List<Integer> fitHorizontalPositions = null;
            boolean foundPositionOnScan=    false;

            for( ; initialPosition<=this.scanMapHeight-height; initialPosition++ ) {
                fitHorizontalPositions= this.scanMap.get(initialPosition).findWhereFits( width );

                // si no es nulo el array de resultados, quiere decir que en alguno de los puntos
                // nos cabe un trozo de tama�o width.
                if ( null!=fitHorizontalPositions && fitHorizontalPositions.size()>0 ) {
                    foundPositionOnScan= true;
                    break;
                }
            }

            if ( foundPositionOnScan ) {
                // j es el scan donde cabe un trozo de tama�o width.
                // comprobamos desde este scan que en todos los scan verticales cabe el trozo.
                // se comprueba que cabe en alguno de los tama�os que la rutina de busqueda horizontal
                // nos ha devuelto antes.

                int minInitialPosition=Integer.MAX_VALUE;
                for( j=0; j<fitHorizontalPositions.size(); j++ ) {
                    boolean fits= true;
                    for( i=initialPosition; i<initialPosition+height; i++ ) {
                        // hay un trozo que no cabe
                        if ( !this.scanMap.get(i).fits( fitHorizontalPositions.get(j), width ) ) {
                            fits= false;
                            break;
                        }
                    }

                    // se ha encontrado un trozo donde la imagen entra.
                    // d.p.m. incluirla en posicion, y seguir con otra.
                    if ( fits ) {
                        return new Pt(fitHorizontalPositions.get(j), initialPosition);
                    }
                }

                initialPosition++;

            } else {
                // no hay sitio en ningun scan.
                return null;
            }
        }

        // no se ha podido encontrar un area en la textura para un trozo de tama�o width*height
        return null;
    }
    
    public void substract (int x, int y, int width, int height ) {
        for( int i=0; i<height; i++ ) {
            if ( !this.scanMap.get(i+y).substract(x,width) ) {
                CAAT.log("Error: removing chunk " + width + height + " at " + x + y);
            }
        }
    }
    
    public void log () {
        for( int i=0; i<this.scanMapHeight; i++ ) {
            this.scanMap.get(i).log(i);
        }
    }
    

}
