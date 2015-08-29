package com.katspow.caatja.webgl.program;

//import com.google.gwt.user.client.DOM;
//import com.google.gwt.user.client.Element;
import com.katspow.caatja.core.Caatja;
import com.katspow.caatja.math.matrix.Matrix3;

//import elemental.html.WebGLRenderingContext;
//import elemental.html.WebGLShader;

/**
 * FIXME
 *
 */
public class Program {
    
    /**
     * Canvas 3D context.
     */
//    protected WebGLRenderingContext gl;
    
    // Add by me
    protected ShaderProgram shaderProgram;

//    public Program(WebGLRenderingContext gl) {
//        this.gl = gl;
//    }
    
    /**
     * Set fragment shader's alpha composite value.
     * @param alpha {number} float value 0..1.
     */
    public Program setAlpha (double alpha ) {
        return this;
    }
    
//    public WebGLShader getShader  (WebGLRenderingContext gl, String type, String str) {
//        WebGLShader shader;
//        if (type == "x-shader/x-fragment") {
//            shader = gl.createShader(gl.FRAGMENT_SHADER);
//        } else if (type == "x-shader/x-vertex") {
//            shader = gl.createShader(gl.VERTEX_SHADER);
//        } else {
//            return null;
//        }
//
//        gl.shaderSource(shader, str);
//        gl.compileShader(shader);
//
//        Object shaderParameter = gl.getShaderParameter(shader, WebGLRenderingContext.COMPILE_STATUS);
//        if (shaderParameter == null) {
//            Caatja.alert(gl.getShaderInfoLog(shader));
//            return null;
//        }
//
//        return shader;
//
//    }
//    public WebGLShader getDomShader (WebGLRenderingContext gl, String id) {
//        
//        Element shaderScript = DOM.getElementById(id);
//        
//        if (shaderScript == null) {
//            return null;
//        }
//
//        String str = "";
//        // FIXME TODO
////        Node k = shaderScript.getFirstChild();
////        while (k) {
////            if (k.nodeType == 3) {
////                str += k.textContent;
////            }
////            k = k.nextSibling;
////        }
//
//        WebGLShader shader = null;
//        
//        // FIXME TODO
////        if (shaderScript.type == "x-shader/x-fragment") {
////            shader = gl.createShader(gl.FRAGMENT_SHADER);
////        } else if (shaderScript.type == "x-shader/x-vertex") {
////            shader = gl.createShader(gl.VERTEX_SHADER);
////        } else {
////            return null;
////        }
//
//        gl.shaderSource(shader, str);
//        gl.compileShader(shader);
//
//        Object shaderParameter = gl.getShaderParameter(shader, WebGLRenderingContext.COMPILE_STATUS);
//        if (shaderParameter == null) {
//            Caatja.alert(gl.getShaderInfoLog(shader));
//            return null;
//        }
//
//        return shader;
//    }
    public Program initialize () {
        return this;
    }
//    public WebGLShader getFragmentShader () {
//        return null;
//    }
//    public WebGLShader getVertexShader () {
//        return null;
//    }
    public Program create () {
//        WebGLRenderingContext gl= this.gl;
//
//        this.shaderProgram.shaderProgram = gl.createProgram();
//        gl.attachShader(this.shaderProgram.shaderProgram, this.getVertexShader());
//        gl.attachShader(this.shaderProgram.shaderProgram, this.getFragmentShader());
//        gl.linkProgram(this.shaderProgram.shaderProgram);
//        gl.useProgram(this.shaderProgram.shaderProgram);
        return this;
    }
    
    public void setMatrixUniform (Matrix3 caatMatrix4 ) {
//        this.gl.uniformMatrix4fv(
//                this.shaderProgram.pMatrixUniform,
//                false,
//                WebGLUtils.createArrayOfFloat32(caatMatrix4.flatten()));

    }
    
    public Program useProgram () {
//        this.gl.useProgram(this.shaderProgram.shaderProgram);
        return this;
    }

}
