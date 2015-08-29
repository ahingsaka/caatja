package com.katspow.caatja.webgl.program;

import com.katspow.caatja.webgl.WebGLUtils;

//import elemental.html.ArrayBuffer;
//import elemental.html.WebGLBuffer;
//import elemental.html.WebGLRenderingContext;
//import elemental.html.WebGLShader;

public class ColorProgram extends Program {

    /**
     * @name ColorProgram
     * @memberOf CAAT.WebGL
     * @extends CAAT.WebGL.Program
     * @constructor
     */
//    public ColorProgram(WebGLRenderingContext gl) {
//        super(gl);
//    }
    
    /**
     * int32 Array for color Buffer
     */
//    WebGLBuffer colorBuffer=    null;
    
    /**
     * GLBuffer for vertex buffer.
     */
//    WebGLBuffer vertexPositionBuffer=   null;
    
    /**
     * Float32 Array for vertex buffer.
     */
//    ArrayBuffer vertexPositionArray=    null;
    
//    public WebGLShader getFragmentShader () {
//        return this.getShader(this.gl, "x-shader/x-fragment",
//                "#ifdef GL_ES \n"+
//                "precision highp float; \n"+
//                "#endif \n"+
//
//                "varying vec4 color; \n"+
//                        
//                "void main(void) { \n"+
//                "  gl_FragColor = color;\n"+
//                "}\n"
//                );
//
//    }
//    public WebGLShader getVertexShader () {
//        return this.getShader(this.gl, "x-shader/x-vertex",
//                "attribute vec3 aVertexPosition; \n"+
//                "attribute vec4 aColor; \n"+
//                "uniform mat4 uPMatrix; \n"+
//                "varying vec4 color; \n"+
//
//                "void main(void) { \n"+
//                "gl_Position = uPMatrix * vec4(aVertexPosition, 1.0); \n"+
//                "color= aColor; \n"+
//                "}\n"
//                );
//    }
    public ColorProgram initialize () {
//        this.shaderProgram.vertexPositionAttribute =
//                this.gl.getAttribLocation(this.shaderProgram.shaderProgram, "aVertexPosition");
//        this.gl.enableVertexAttribArray(
//                this.shaderProgram.vertexPositionAttribute);
//
//        this.shaderProgram.vertexColorAttribute =
//                this.gl.getAttribLocation(this.shaderProgram.shaderProgram, "aColor");
//        this.gl.enableVertexAttribArray(
//                this.shaderProgram.vertexColorAttribute);
//
//        this.shaderProgram.pMatrixUniform =
//                this.gl.getUniformLocation(this.shaderProgram.shaderProgram, "uPMatrix");
//
//        this.useProgram();
//
//        this.colorBuffer= this.gl.createBuffer();
//        this.setColor(new double[] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 });
//
//        int maxTris=4096, i;
//        /// set vertex data
//        this.vertexPositionBuffer = this.gl.createBuffer();
//        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, this.vertexPositionBuffer );
//        this.vertexPositionArray= (ArrayBuffer) WebGLUtils.createArrayOfFloat32(maxTris*12);
//        this.gl.bufferData(this.gl.ARRAY_BUFFER, this.vertexPositionArray, this.gl.DYNAMIC_DRAW);
//        this.gl.vertexAttribPointer(this.shaderProgram.vertexPositionAttribute, 3, this.gl.FLOAT, false, 0, 0);

        return (ColorProgram) super.initialize();
    }
    public void setColor (double[] colorArray ) {
//        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, this.colorBuffer );
//        this.gl.bufferData(this.gl.ARRAY_BUFFER, (ArrayBuffer) WebGLUtils.createArrayOfFloat32(colorArray), this.gl.STATIC_DRAW);
//
//        // FIXME colorBuffer or integer ???
//        this.gl.vertexAttribPointer(
//                this.shaderProgram.vertexColorAttribute,
//                1,
//                this.gl.FLOAT,
//                false,
//                0,
//                0);
    }

}
