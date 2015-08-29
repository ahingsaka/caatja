package com.katspow.caatja.foundation.ui;

public class UI {
    
    public static boolean DEBUG = false;
    public static double JUSTIFY_RATIO = .8;
    
    public static String defaultFont = "24px Arial";
    
    public enum AXIS {
        X(0), Y(1);
        
        private int val;

        private AXIS(int val) {
            this.val = val;
        }
        
        public int getVal() {
            return val;
        }
    }
    
    public enum ALIGNMENT {
        LEFT (  0),
        RIGHT(  1),
        CENTER( 2),
        TOP(    3),
        BOTTOM( 4),
        JUSTIFY(5);
        
        private int val;

        private ALIGNMENT(int val) {
            this.val = val;
        }
        
        public int getVal() {
            return val;
        }
    }

}
