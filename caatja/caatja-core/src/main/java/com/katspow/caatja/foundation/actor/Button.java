package com.katspow.caatja.foundation.actor;

import com.katspow.caatja.core.Caatja;
import com.katspow.caatja.event.CAATMouseEvent;
import com.katspow.caatja.foundation.image.CompoundImage;

public class Button extends SpriteActor {
    
    /**
     * This class works as a UI Button.
     * <p>
     * To fully define the button, four images should be supplied as well as a callback function.
     * The images define different button states:
     * <ul>
     *  <li>Normal state
     *  <li>Pointed
     *  <li>Pressed
     *  <li>Disabled
     * </ul>
     *
     * <p>
     * It is only compulsory to supply an image for the normal state. All images must be supplied in
     * a single image strip which containes all button states, concretely in a CAAT.CompoundImage
     * instance.
     *
     * @constructor
     * @extends CAAT.ActorContainer
     */
    @Deprecated
    public Button() {
        super();
        this.glEnabled = true;
    }
    
    int iNormal=        0;
    int iOver=          0;
    int iPress=         0;
    int iDisabled=      0;
    int iCurrent=       0;
    boolean enable = true;
	private ActorCallback fnOnClick;
    
    /**
     * Set enabled state for the button.
     * If the button is disabled, it will only show the disabled state and will discard mouse input.
     * @param enabled {boolean}
     */
    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    /**
     * Initialize the button with the given values. The button size will be set to the size of the
     * subimages contained in the buttonImage.
     *
     * @param buttonImage {CAAT.CompoundImage} an image used as a strip of button state images.
     * @param iNormal {number} an integer indicating which image index of the buttonImage corresponds
     * with the normal state.
     * @param iOver {number} an integer indicating which image index of the buttonImage corresponds
     * with the pointed state.
     * @param iPress {number} an integer indicating which image index of the buttonImage corresponds
     * with the pressed state.
     * @param iDisabled {number} an integer indicating which image index of the buttonImage corresponds
     * with the disabled state.
     * @param fn {function} callback function to call on mouse release inside the button actor. The
     * function receives as parameter the button that fired the event.
     */
    public Button initialize (CompoundImage buttonImage, Integer iNormal, Integer iOver,Integer  iPress, Integer iDisabled, ActorCallback fn) {
        this.setSpriteImage(buttonImage);
        this.iNormal=       iNormal != null ? iNormal : 0;
        this.iOver=         iOver != null ? iOver : this.iNormal;
        this.iPress=        iPress != null ?  iPress: this.iNormal;
        this.iDisabled=     iDisabled != null ? iDisabled : this.iNormal;
        this.iCurrent=      this.iNormal;
        this.width=         buttonImage.singleWidth;
        this.height=        buttonImage.singleHeight;
        this.fnOnClick=     fn;
        this.spriteIndex = this.iNormal;
        return this;
    }
    
    // Add by me
    public Button initialize (CompoundImage buttonImage, Integer iNormal, Integer iOver,Integer  iPress, Integer iDisabled) {
    	return initialize(buttonImage, iNormal, iOver, iPress, iDisabled, null);
    }
    
    /**
    *
    * @param mouseEvent {CAAT.MouseEvent}
    */
    public void mouseEnter (CAATMouseEvent mouseEvent) {
        this.setSpriteIndex(this.iOver);
        Caatja.setCursor("pointer");
    }
    
    /**
    *
    * @param mouseEvent {CAAT.MouseEvent}
    */
    public void mouseExit (CAATMouseEvent mouseEvent) {
    	 this.setSpriteIndex(this.iNormal);
    	 Caatja.setCursor("default");
    }
    
    /**
    *
    * @param mouseEvent {CAAT.MouseEvent}
    */
    public void mouseDown (CAATMouseEvent mouseEvent) {
    	 this.setSpriteIndex(this.iPress);
    }
    
    /**
    *
    * @param mouseEvent {CAAT.MouseEvent}
    */
    public void mouseUp (CAATMouseEvent mouseEvent) {
    	 this.setSpriteIndex(this.iNormal);
    }
    
    
    /**
    *
    * @param mouseEvent {CAAT.MouseEvent}
    */
    // Different from source to keep hyper number ok
    public void mouseClick (CAATMouseEvent mouseEvent) throws Exception {
    	if (this.enable && null != fnOnClick) {
    		fnOnClick.call(this);
    	} else {
    		this.fnOnClick();
    	}
    }
    
    public void fnOnClick() throws Exception {
    	
    }
    
    @Override
    public String toString() {
        return "CAAT.Button "+this.iNormal;
    }

    // Add by me
    @Override
    public Button setLocation(double x, double y) {
        return (Button) super.setLocation(x, y);
    }
    
}
