package com.katspow.caatja.modules.texturepacker;

import java.util.ArrayList;

/**
 * FIXME ?
 *
 */
public class TexturePageManager {
    
    public ArrayList<TexturePage> pages;

    public TexturePageManager() {
        this.pages = new ArrayList<TexturePage>();
    }
    
//    public void createPages(WebGLRenderingContext gl, int width, int height, Map<String, Image> imagesCache) {
//
//        boolean end= false;
//        while( !end ) {
//            TexturePage page= new TexturePage(width,height);
//            page.create(imagesCache);
//            page.initialize(gl);
//            page.endCreation();
//            this.pages.add(page);
//
//            end= true;
//            
//            for (Image image : imagesCache.values()) {
//             // imagen sin asociacion de textura
//                if (image.__texturePage != null) {
//                 // cabe en la pagina ?? continua con otras paginas.
//                    if ( image.imageElement.getWidth()<=width && image.imageElement.getHeight() <=height ) {
//                        end= false;
//                    }
//                    break;
//                }
//            }
//        }
//    }
    
    public void deletePages() {
        for (TexturePage page : pages) {
            page.deletePage();
        }
        
        this.pages= null;
    }

}
