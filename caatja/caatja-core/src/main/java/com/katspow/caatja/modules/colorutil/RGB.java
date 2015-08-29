package com.katspow.caatja.modules.colorutil;

public class RGB {
    
    public int r= 255;
    public int g= 255;
    public int b= 255;
    
    public RGB(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }
    
    public static String random() {
        String a= "0123456789abcdef";
        String c= "#";
        for( int i=0; i<3; i++ ) {
            c+= a.charAt( (int) (Math.random()* a.length()) >> 0 );
        }
        return c;
    }
    
    public String toHex() {
        String substringR = Integer.toHexString(0x100 | this.r).substring(1);
        String substringG = Integer.toHexString(0x100 | this.g).substring(1);
        String substringB = Integer.toHexString(0x100 | this.b).substring(1);
        return substringR + substringG + substringB;
        // See: http://jsperf.com/rgb-decimal-to-hex/5
        // return Integer.toHexString(((this.r << 16) + (this.g << 8) + this.b & 0x00ffffff));
    }
    
    public static void main(String [] args) {
        
        RGB name = new RGB(0,255,255);
        
        RGB test2 = new RGB(0,0,0);
        
        System.out.println(name.toHex());
        System.out.println(test2.toHex());
        
    }

}
