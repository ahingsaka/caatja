package com.katspow.caatja.webgl.program;

import java.util.ArrayList;
import java.util.List;

import com.katspow.caatja.webgl.WebGLUtils;

//import elemental.html.ArrayBuffer;
//import elemental.html.Float32Array;
//import elemental.html.Uint16Array;
//import elemental.html.WebGLBuffer;
//import elemental.html.WebGLRenderingContext;
//import elemental.html.WebGLShader;
//import elemental.html.WebGLTexture;

/**
 * @name TextureProgram
 * @memberOf CAAT.WebGL
 * @extends CAAT.WebGL.Program
 * @constructor
 */
public class TextureProgram extends Program {

//    public TextureProgram(WebGLRenderingContext gl) {
//        super(gl);
//    }
    
    /**
     * VertextBuffer GLBuffer
     */
//    public WebGLBuffer vertexPositionBuffer=   null;
    
    /**
     * VertextBuffer Float32 Array
     */
//    public ArrayBuffer vertexPositionArray=    null;
    
    /**
     * UVBuffer GLBuffer
     */
//    public WebGLBuffer vertexUVBuffer=         null;
    
    /**
     * VertexBuffer Float32 Array
     */
//    public ArrayBuffer vertexUVArray=          null;
    
    /**
     * VertexIndex GLBuffer.
     */
//    public WebGLBuffer vertexIndexBuffer=      null;

    /**
     * Lines GLBuffer
     */
//    public WebGLBuffer linesBuffer=            null;
    
    public double prevAlpha=              -1;
    public float prevR=                  -1;
    public float prevG=                  -1;
    public float prevB=                  -1;
    public float prevA=                  -1;
//    public WebGLTexture prevTexture=            null;
    
//    public WebGLShader getFragmentShader () {
//        return this.getShader( this.gl, "x-shader/x-fragment",
//                "#ifdef GL_ES \n"+
//                "precision highp float; \n"+
//                "#endif \n"+
//
//                "varying vec2 vTextureCoord; \n"+
//                "uniform sampler2D uSampler; \n"+
//                "uniform float alpha; \n"+
//                "uniform bool uUseColor;\n"+
//                "uniform vec4 uColor;\n"+
//
//                "void main(void) { \n"+
//
//                "if ( uUseColor ) {\n"+
//                "  gl_FragColor= vec4(uColor.rgb, uColor.a*alpha);\n"+
//                "} else { \n"+
//                "  vec4 textureColor= texture2D(uSampler, vec2(vTextureCoord)); \n"+
//                "  gl_FragColor = vec4(textureColor.rgb, textureColor.a * alpha); \n"+
//                "}\n"+
//
//                "}\n"
//                );
//    }
    
//    public WebGLShader getVertexShader () {
//        return this.getShader(this.gl, "x-shader/x-vertex",
//                "attribute vec3 aVertexPosition; \n"+
//                "attribute vec2 aTextureCoord; \n"+
//
//                "uniform mat4 uPMatrix; \n"+
//
//                "varying vec2 vTextureCoord; \n"+
//
//                "void main(void) { \n"+
//                "gl_Position = uPMatrix * vec4(aVertexPosition, 1.0); \n"+
//                "vTextureCoord = aTextureCoord;\n"+
//                "}\n"
//                );
//    }
    
    @Override
    public TextureProgram useProgram () {
        super.useProgram();

//        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, this.vertexPositionBuffer );
//        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, this.vertexUVBuffer);
//        this.gl.bindBuffer(this.gl.ELEMENT_ARRAY_BUFFER, this.vertexIndexBuffer);
        
        // Add by me
        return this;
    }
    
    // Add by me
//    private Uint16Array linesBufferArray;
    
    public TextureProgram initialize () {

//        this.linesBuffer= this.gl.createBuffer();
//        
//        this.gl.bindBuffer(this.gl.ELEMENT_ARRAY_BUFFER, this.linesBuffer );
//        Integer[] arr= new Integer[1024];
//        for( int i=0; i<1024; i++ ) {
//            arr[i]= i;
//        }
//        this.linesBufferArray= WebGLUtils.createArrayOfUInt16(arr);
//        this.gl.bufferData(this.gl.ELEMENT_ARRAY_BUFFER, this.linesBufferArray, this.gl.DYNAMIC_DRAW);
//
//        this.shaderProgram.vertexPositionAttribute =
//                this.gl.getAttribLocation(this.shaderProgram.shaderProgram, "aVertexPosition");
//        this.gl.enableVertexAttribArray(
//                this.shaderProgram.vertexPositionAttribute);
//
//        this.shaderProgram.textureCoordAttribute =
//                this.gl.getAttribLocation(this.shaderProgram.shaderProgram, "aTextureCoord");
//        this.gl.enableVertexAttribArray(
//                this.shaderProgram.textureCoordAttribute);
//
//        this.shaderProgram.pMatrixUniform =
//                this.gl.getUniformLocation(this.shaderProgram.shaderProgram, "uPMatrix");
//        this.shaderProgram.samplerUniform =
//                this.gl.getUniformLocation(this.shaderProgram.shaderProgram, "uSampler");
//        this.shaderProgram.alphaUniform   =
//                this.gl.getUniformLocation(this.shaderProgram.shaderProgram, "alpha");
//        this.shaderProgram.useColor =
//                this.gl.getUniformLocation(this.shaderProgram.shaderProgram, "uUseColor");
//        this.shaderProgram.color =
//                this.gl.getUniformLocation(this.shaderProgram.shaderProgram, "uColor");
//
//        this.setAlpha(1);
//        this.setUseColor(false, 0, 0, 0, 0);
//
//        int maxTris=4096, i;
//        /// set vertex data
//        this.vertexPositionBuffer = this.gl.createBuffer();
//        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, this.vertexPositionBuffer );
//        this.vertexPositionArray= (ArrayBuffer) WebGLUtils.createArrayOfFloat32(maxTris*12);
//        this.gl.bufferData(this.gl.ARRAY_BUFFER, this.vertexPositionArray, this.gl.DYNAMIC_DRAW);
//        this.gl.vertexAttribPointer(this.shaderProgram.vertexPositionAttribute, 3, this.gl.FLOAT, false, 0, 0);
//
//        // uv info
//        this.vertexUVBuffer= this.gl.createBuffer();
//        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, this.vertexUVBuffer);
//        this.vertexUVArray= (ArrayBuffer) WebGLUtils.createArrayOfFloat32(maxTris*8);
//        this.gl.bufferData(this.gl.ARRAY_BUFFER, this.vertexUVArray, this.gl.DYNAMIC_DRAW);
//        this.gl.vertexAttribPointer(this.shaderProgram.textureCoordAttribute, 2, this.gl.FLOAT, false, 0, 0);
//
//        // vertex index
//        this.vertexIndexBuffer = this.gl.createBuffer();
//        this.gl.bindBuffer(this.gl.ELEMENT_ARRAY_BUFFER, this.vertexIndexBuffer);
//        
//        // TODO Check size init
//        List<Integer> vertexIndex = new ArrayList<Integer>();
//        for( i=0; i<maxTris; i++ ) {
//            vertexIndex.add(0 + i*4); vertexIndex.add(1 + i*4); vertexIndex.add(2 + i*4);
//            vertexIndex.add(0 + i*4); vertexIndex.add(2 + i*4); vertexIndex.add(3 + i*4);
//        }
//        this.gl.bufferData(this.gl.ELEMENT_ARRAY_BUFFER, WebGLUtils.createArrayOfUInt16((Integer[]) vertexIndex.toArray()), this.gl.STATIC_DRAW);            

        return (TextureProgram) super.initialize();
    }
    public void setUseColor (boolean use, float r, float g, float b,float a ) {
//        this.gl.uniform1i(this.shaderProgram.useColor, use?1:0);
//        if ( use ) {
//            if ( this.prevA!=a || this.prevR!=r || this.prevG!=g || this.prevB!=b ) {
//                this.gl.uniform4f(this.shaderProgram.color, r,g,b,a );
//                this.prevA= a;
//                this.prevR= r;
//                this.prevG= g;
//                this.prevB= b;
//            }
//        }
    }
//    public TextureProgram setTexture (WebGLTexture glTexture ) {
//        if (this.prevTexture != glTexture) {
//            WebGLRenderingContext gl = this.gl;
//
//            gl.activeTexture(gl.TEXTURE0);
//            gl.bindTexture(gl.TEXTURE_2D, glTexture);
//            gl.uniform1i(this.shaderProgram.samplerUniform, 0);
//            this.prevTexture = glTexture;
//        }
//
//        return this;
//    }
    
//    public TextureProgram updateVertexBuffer (Float32Array vertexArray) {
//        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, this.vertexPositionBuffer );
//        this.gl.bufferSubData(WebGLRenderingContext.ARRAY_BUFFER, 0, (ArrayBuffer) vertexArray);
//        return this;
//    }
    
//    public TextureProgram updateUVBuffer (Float32Array uvArray) {
//        this.gl.bindBuffer(this.gl.ARRAY_BUFFER, this.vertexUVBuffer );
//        this.gl.bufferSubData(WebGLRenderingContext.ARRAY_BUFFER, 0, (ArrayBuffer) uvArray);
//        return this;
//    }
    
    public TextureProgram setAlpha (double alpha) {
        if (this.prevAlpha != alpha) {
//            this.gl.uniform1f(this.shaderProgram.alphaUniform, (float) alpha);
            this.prevAlpha = alpha;
        }
        return this;
    }
    /**
     *
     * @param lines_data {Float32Array} array of number with x,y,z coords for each line point.
     * @param size {number} number of lines to draw.
     * @param r
     * @param g
     * @param b
     * @param a
     * @param lineWidth {number} drawing line size.
     */
//    public void drawLines (Float32Array lines_data, int size, float r, float g,float b, float a, int lineWidth ) {
//
//        WebGLRenderingContext gl = this.gl;
//
//        this.setAlpha( a );
//        
//        gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, this.linesBuffer );
//        gl.lineWidth(lineWidth);
//
//        this.updateVertexBuffer(lines_data);
//        this.setUseColor(true, r,g,b,1 );
//        gl.drawElements(gl.LINES, size, gl.UNSIGNED_SHORT, 0);
//
//        /// restore
//        this.setAlpha( 1 );
//        this.setUseColor(false, 0, 0, 0, 0);
//        gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, this.vertexIndexBuffer);
//        
//    }
    
    /**
     * 
     * @param polyline_data
     * @param size
     * @param r
     * @param g
     * @param b
     * @param a
     * @param lineWidth
     */
//    public void drawPolylines (Float32Array polyline_data, int size, float r, float g, float b, float a, int lineWidth ) {
//
//        WebGLRenderingContext gl= this.gl;
//        
//        gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, this.linesBuffer );
//        gl.lineWidth(lineWidth);
//
//        this.setAlpha(a);
//
//        this.updateVertexBuffer(polyline_data);
//        this.setUseColor(true, r,g,b,1 );
//        gl.drawElements(gl.LINE_STRIP, size, gl.UNSIGNED_SHORT, 0);
//
//        /// restore
//        this.setAlpha( 1 );
//        this.setUseColor(false, 0, 0, 0, 0);
//        gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, this.vertexIndexBuffer);
//
//    }

}
