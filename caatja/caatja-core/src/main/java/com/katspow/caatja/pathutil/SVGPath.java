package com.katspow.caatja.pathutil;

/**
 * TODO Finish
 */
import java.util.List;

import com.katspow.caatja.core.CAAT;

/**
 * <p>
 * This class is a SVG Path parser.
 * By calling the method parsePath( svgpath ) an instance of CAAT.PathUtil.Path will be built by parsing
 * its contents.
 *
 * <p>
 * See <a href="../../demos/demo32/svgpath.html">demo32</a>
 *
 * @name SVGPath
 * @memberOf CAAT.PathUtil
 * @constructor
 */
public class SVGPath {
    
    int OK = 0;
    int EOF = 1;
    int NAN = 2;
    
    private int c = 0;
    private List<Double> bezierInfo = null;
    
    private void error(String pathInfo, int c) {
        int cpos = c;
        if (cpos < 0) {
            cpos = 0;
        }
        CAAT.log("parse error near ..." + pathInfo.substring(cpos, 20));
    }

    public int __skipBlank (String pathInfo, int c) {
        char p = pathInfo.charAt(c);
        while (c < pathInfo.length() && (p == ' ' || p == '\n' || p == '\t' || p == ',')) {
            ++c;
            p = pathInfo.charAt(c);
        }

        return c;
    }

    public boolean __maybeNumber (String pathInfo, int c) {

        if (c < pathInfo.length() - 2) {

            char p = pathInfo.charAt(c);
            char p1 = pathInfo.charAt(c + 1);

            return  p == '-' ||
                this.__isDigit(p) ||
                (p == '.' && this.__isDigit(p1) );
        }

        return false;
    }

    // TODO Check
    public boolean __isDigit (char c) {
        return c >= '0' && c <= '9';
    }


    public void __getNumber (String pathInfo, int c, List<Double> v, Ret error) {
        c = this.__skipBlank(pathInfo, c);
        if (c < pathInfo.length()) {
            int nc = this.__findNumber(pathInfo, c);
            if (nc != -1) {
                v.add(Double.parseDouble((pathInfo.substring(c, nc))));
                c = this.__skipBlank(pathInfo, nc);
                error.pos = c;
                error.result = OK;
                return;
            } else {
                error.result = NAN;
                return;
            }
        }

        error.result = EOF;
    }

    public int __getNumbers (String pathInfo, int c, List<Double> v, int n, Ret error) {

        for (int i = 0; i < n; i++) {
            this.__getNumber(pathInfo, c, v, error);
            if (error.result != OK) {
                break;
            } else {
                c = error.pos;
            }
        }

        return c;
    }


    public int __findNumber (String pathInfo, int c) {

        char p;

        if ((p = pathInfo.charAt(c)) == '-') {
            ++c;
        }

        if (!this.__isDigit((p = pathInfo.charAt(c)))) {
            if ((p = pathInfo.charAt(c)) != '.' || !this.__isDigit(pathInfo.charAt(c + 1))) {
                return -1;
            }
        }

        while (this.__isDigit((p = pathInfo.charAt(c)))) {
            ++c;
        }

        if ((p = pathInfo.charAt(c)) == '.') {
            ++c;
            if (!this.__isDigit((p = pathInfo.charAt(c)))) {   // asumo un numero [d+]\. como valido.
                return c;
            }
            while (this.__isDigit((p = pathInfo.charAt(c)))) {
                ++c;
            }
        }

        return c;
    }

//    public void __parseMoveTo (String pathInfo, int c, boolean absolute, Path path, Ret error) {
//
//        List<Double> numbers = new ArrayList<Double>();
//
//        c = this.getNumbers(pathInfo, c, numbers, 2, error);
//
//        if (error.result == OK) {
//            if (!absolute) {
//                numbers.set(0, numbers.get(0) + path.trackPathX);
//                numbers.set(1, numbers.get(1) + path.trackPathY);
//            }
//            path.beginPath(numbers.get(0), numbers.get(1));
//        } else {
//            return;
//        }
//
//        if (this.maybeNumber(pathInfo, c)) {
//            c = this.parseLine(pathInfo, c, absolute, path, error);
//        }
//
//        error.pos = c;
//    }
//
//    public void __parseLine (String pathInfo, int c, boolean absolute, Path path, Ret error) {
//
//        List<Double> numbers = new ArrayList<Double>();
//
//        do {
//            c = this.getNumbers(pathInfo, c, numbers, 2, error);
//            if (!absolute) {
//                numbers.set(0, numbers.get(0) + path.trackPathX);
//                numbers.set(1, numbers.get(1) + path.trackPathY);
//            }
//            path.addLineTo(numbers.get(0), numbers.get(1));
//
//        } while (this.maybeNumber(pathInfo, c));
//
//        error.pos = c;
//    }
//
//
//    public void __parseLineH (String pathInfo, int c,boolean absolute, Path path, Ret error) {
//
//        List<Double> numbers = new ArrayList<Double>();
//        
//        do {
//            c = this.getNumbers(pathInfo, c, numbers, 1, error);
//
//            if (!absolute) {
//                numbers.set(0, numbers.get(0) + path.trackPathX);
//            }
//            numbers[1].push(path.trackPathY);
//
//            path.addLineTo(numbers.get(0), numbers.get(1));
//
//        } while (this.maybeNumber(pathInfo, c));
//
//        error.pos = c;
//    }
//
//    public void __parseLineV (String pathInfo, int c,boolean absolute, Path path, Ret error) {
//
//    var numbers = [ path.trackPathX ];
//
//    do {
//        c = this.getNumbers(pathInfo, c, numbers, 1, error);
//
//        if (!absolute) {
//            numbers[1] += path.trackPathY;
//        }
//
//        path.addLineTo(numbers[0], numbers[1]);
//
//    } while (this.maybeNumber(pathInfo, c));
//
//    error.pos = c;
//    }
//
//    public void  __parseCubic (String pathInfo, int c, boolean absolute, Path path, Ret error) {
//
//        var v = [];
//
//        do {
//            c = this.getNumbers(pathInfo, c, v, 6, error);
//            if (error.result == OK) {
//                if (!absolute) {
//                    v[0] += path.trackPathX;
//                    v[1] += path.trackPathY;
//                    v[2] += path.trackPathX;
//                    v[3] += path.trackPathY;
//                    v[4] += path.trackPathX;
//                    v[5] += path.trackPathY;
//                }
//
//                path.addCubicTo(v[0], v[1], v[2], v[3], v[4], v[5]);
//
//
//                v.shift();
//                v.shift();
//                this.bezierInfo = v;
//
//            } else {
//                return;
//            }
//        } while (this.maybeNumber(pathInfo, c));
//
//        error.pos = c;
//    }
//
//    public void __parseCubicS (String pathInfo, int c, boolean absolute, Path path, Ret error) {
//
//        var v = [];
//
//        do {
//            c = this.getNumbers(pathInfo, c, v, 4, error);
//            if (error.result == OK) {
//                if (!absolute) {
//
//                    v[0] += path.trackPathX;
//                    v[1] += path.trackPathY;
//                    v[2] += path.trackPathX;
//                    v[3] += path.trackPathY;
//                }
//
//                var x, y;
//
//                x = this.bezierInfo.get(2) + (this.bezierInfo.get(2) - this.bezierInfo.get(0));
//                y = this.bezierInfo.get(3) + (this.bezierInfo.get(3) - this.bezierInfo.get(1));
//
//                path.addCubicTo(x, y, v[0], v[1], v[2], v[3]);
//
//                this.bezierInfo = v;
//
//            } else {
//                return;
//            }
//        } while (this.maybeNumber(c));
//
//        error.pos = c;
//    }
//
//    public void __parseQuadricS (String pathInfo, int c, boolean absolute, Path path,  Ret error) {
//
//        var v = [];
//
//        do {
//            c = this.getNumbers(pathInfo, c, v, 4, error);
//            if (error.result == OK) {
//
//                if (!absolute) {
//
//                    v[0] += path.trackPathX;
//                    v[1] += path.trackPathY;
//                }
//
//                double x, y;
//
//                x = this.bezierInfo.get(2) + (this.bezierInfo.get(2) - this.bezierInfo.get(0));
//                y = this.bezierInfo.get(3) + (this.bezierInfo.get(3) - this.bezierInfo.get(1));
//
//                path.addQuadricTo(x, y, v[0], v[1]);
//
//                this.bezierInfo = new ArrayList<Double>();
//                bezierInfo.add(x);
//                bezierInfo.add(y);
//                bezierInfo.add(v[0]);
//                bezierInfo.add(v[1]);
//
//
//            } else {
//                return;
//            }
//        } while (this.maybeNumber(c));
//
//        error.pos = c;
//    }
//
//
//    public void __parseQuadric (String pathInfo, int c, boolean absolute, Path path,  Ret error) {
//
//        var v = [];
//
//        do {
//            c = this.getNumbers(pathInfo, c, v, 4, error);
//            if (error.result == OK) {
//                if (!absolute) {
//
//                    v[0] += path.trackPathX;
//                    v[1] += path.trackPathY;
//                    v[2] += path.trackPathX;
//                    v[3] += path.trackPathY;
//                }
//
//                path.addQuadricTo(v[0], v[1], v[2], v[3]);
//
//                this.bezierInfo = v;
//            } else {
//                return;
//            }
//        } while (this.maybeNumber(c));
//
//        error.pos = c;
//    }
//
//    public void __parseClosePath (String pathInfo, int c, Path path, Ret error) {
//        path.closePath();
//        error.pos= c;
//    }
//    
//    /**
//     * This method will create a CAAT.PathUtil.Path object with as many contours as needed.
//     * @param pathInfo {string} a SVG path
//     * @return Array.<CAAT.PathUtil.Path>
//     */
//    public Path parsePath(String pathInfo) {
//
//        this.c = 0;
//        this.contours= new ArrayList<Path>();
//
//        Path path = new Path();
//        this.contours.add( path );
//
//        this.c = this.skipBlank(pathInfo, this.c);
//        if (this.c == pathInfo.length()) {
//            return path;
//        }
//
//        Ret ret = new Ret(0, 0);
//
//        while (this.c != pathInfo.length()) {
//            char segment = pathInfo.charAt(this.c);
//            switch (segment) {
//                case 'm':
//                    this.__parseMoveTo(pathInfo, this.c + 1, false, path, ret);
//                    break;
//                case 'M':
//                    this.__parseMoveTo(pathInfo, this.c + 1, true, path, ret);
//                    break;
//                case 'c':
//                    this.__parseCubic(pathInfo, this.c + 1, false, path, ret);
//                    break;
//                case 'C':
//                    this.__parseCubic(pathInfo, this.c + 1, true, path, ret);
//                    break;
//                case 's':
//                    this.__parseCubicS(pathInfo, this.c + 1, false, path, ret);
//                    break;
//                case 'S':
//                    this.__parseCubicS(pathInfo, this.c + 1, true, path, ret);
//                    break;
//                case 'q':
//                    this.__parseQuadric(pathInfo, this.c + 1, false, path, ret);
//                    break;
//                case 'Q':
//                    this.__parseQuadricS(pathInfo, this.c + 1, true, path, ret);
//                    break;
//                case 't':
//                    this.__parseQuadricS(pathInfo, this.c + 1, false, path, ret);
//                    break;
//                case 'T':
//                    this.__parseQuadric(pathInfo, this.c + 1, true, path, ret);
//                    break;
//                case 'l':
//                    this.__parseLine(pathInfo, this.c + 1, false, path, ret);
//                    break;
//                case 'L':
//                    this.__parseLine(pathInfo, this.c + 1, true, path, ret);
//                    break;
//                case 'h':
//                    this.__parseLineH(pathInfo, this.c + 1, false, path, ret);
//                    break;
//                case 'H':
//                    this.__parseLineH(pathInfo, this.c + 1, true, path, ret);
//                    break;
//                case 'v':
//                    this.__parseLineV(pathInfo, this.c + 1, false, path, ret);
//                    break;
//                case 'V':
//                    this.__parseLineV(pathInfo, this.c + 1, true, path, ret);
//                    break;
//                case 'z':
//                case 'Z':
//                    this.__parseClosePath(pathInfo, this.c + 1, path, ret);
//                    path= new Path();
//                    this.contours.add( path );
//                    break;
//                case 0:
//                    break;
//                default:
//                    error(pathInfo, this.c);
//                    break;
//            }
//
//            if (ret.result != OK) {
//                error(pathInfo, this.c);
//                break;
//            } else {
//                this.c = ret.pos;
//            }
//
//        } // while
//
//        int count= 0;
//        Path fpath= null;
//        for( int i=0; i<this.contours.length; i++ ) {
//            if ( !this.contours[i].isEmpty() ) {
//                fpath= this.contours[i];
//                if ( !fpath.closed ) {
//                    fpath.endPath();
//                }
//                count++;
//            }
//        }
//
//        if ( count==1 ) {
//            return fpath;
//        }
//
//        path= new Path();
//        for( int i=0; i<this.contours.length; i++ ) {
//            if ( !this.contours[i].isEmpty() ) {
//                path.addSegment( this.contours[i] );
//            }
//        }
//        return path.endPath();
//
//    }
    
    // Add by me
    public class Ret {
        public Ret(int pos, int result) {
            this.pos = pos;
            this.result = result;
        }
        int pos;
        int result;
    }
    
}
