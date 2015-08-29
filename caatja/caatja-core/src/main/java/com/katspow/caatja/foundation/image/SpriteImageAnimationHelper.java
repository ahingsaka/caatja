package com.katspow.caatja.foundation.image;

/**
 * TODO int[] or String[]
 */
public class SpriteImageAnimationHelper {

    /**
    *
    * Define an animation frame sequence, name it and supply with a callback which be called when the
    * sequence ends playing.
    *
    * @name SpriteImageAnimationHelper
    * @memberOf CAAT.Foundation
    * @constructor
    */
    public SpriteImageAnimationHelper(int[] animation, int time, SpriteImageAnimationCallback onEndPlayCallback) {
        this.animation = animation;
        this.time = time;
        this.onEndPlayCallback = onEndPlayCallback;
    }

    /**
     * A sequence of integer values defining a frame animation.
     * For example [1,2,3,4,3,2,3,4,3,2]
     * Array.<number>
     */
    public int[] animation = null;
    
    /**
     * Time between any two animation frames.
     */
    public int time = 0;
    
    /**
     * Call this callback function when the sequence ends.
     */
    public SpriteImageAnimationCallback onEndPlayCallback = null;

}
