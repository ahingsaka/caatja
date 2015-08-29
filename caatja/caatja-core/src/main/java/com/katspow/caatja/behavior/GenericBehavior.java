package com.katspow.caatja.behavior;

import com.katspow.caatja.foundation.actor.Actor;

/**
 * FIXME Object types ...
 */
public class GenericBehavior extends BaseBehavior {

    /**
     * <p>
     * A generic behavior is supposed to be extended to create new behaviors when the out-of-the-box
     * ones are not sufficient. It applies the behavior result to a given target object in two ways:
     *
     * <ol>
     * <li>defining the property parameter: the toolkit will perform target_object[property]= calculated_value_for_time.
     * <li>defining a callback function. Sometimes setting of a property is not enough. In example,
     * for a give property in a DOM element, it is needed to set object.style['left']= '70px';
     * With the property approach, you won't be able to add de 'px' suffix to the value, and hence won't
     * work correctly. The function callback will allow to take control by receiving as parameters the
     * target object, and the calculated value to apply by the behavior for the given time.
     * </ol>
     *
     * <p>
     * For example, this code will move a dom element from 0 to 400 px on x during 1 second:
     * <code>
     * <p>
     * var enterBehavior= new CAAT.GenericBehavior(). <br>
     * &nbsp;&nbsp;setFrameTime( scene.time, 1000 ). <br>
     * &nbsp;&nbsp;setValues( <br>
     * &nbsp;&nbsp;&nbsp;&nbsp;0, <br>
     * &nbsp;&nbsp;&nbsp;&nbsp;400, <br>
     * &nbsp;&nbsp;&nbsp;&nbsp;domElement, <br>
     * &nbsp;&nbsp;&nbsp;&nbsp;null, <br>
     * &nbsp;&nbsp;&nbsp;&nbsp;function( currentValue, target ) { <br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;target.style['left']= currentValue+'px'; <br>
     * &nbsp;&nbsp;&nbsp;&nbsp;} <br>
     * &nbsp;&nbsp;); <br>
     * </code>
     *
     * @constructor
     * @extends CAAT.Behavior
     *
     */
    public GenericBehavior() {
        super();
    }

    /**
     * starting value.
     */
    public double start = 0;
    
    /**
     * ending value.
     */
    public double end = 0;
    
    /**
     * target to apply this generic behvior.
     */
    public Actor target = null;
    
    /**
     * property to apply values to.
     */
    public String property = null;
    
    /**
     * this callback will be invoked for every behavior application.
     */
    public GenericBehaviorCallback callback = null;

    /**
     * Sets the target objects property to the corresponding value for the given time.
     * If a callback function is defined, it is called as well.
     *
     * @param time {number} the scene time to apply the behavior at.
     * @param actor {CAAT.Actor} a CAAT.Actor object instance.
     */
    @Override
    public SetForTimeReturnValue setForTime(double time, Actor actor) {
        double value = this.start + time * (this.end - this.start);

        if (this.callback != null) {
            return this.callback.call(value, target, actor);
        }
        
        if (this.property != null){
            // FIXME what kind of property ?
            // this.target[this.property]= value;
        }

        return null;
    }

    /**
     * Defines the values to apply this behavior.
     *
     * @param start {number} initial behavior value.
     * @param end {number} final behavior value.
     * @param target {object} an object. Usually a CAAT.Actor.
     * @param property {string} target object's property to set value to.
     * @param callback {function} a function of the form <code>function( target, value )</code>.
     */
    public GenericBehavior setValues(double start, double end, Actor target, String property, GenericBehaviorCallback callback) {
        this.start = start;
        this.end = end;
        this.target = target;
        this.property = property;
        this.callback = callback;
        return this;
    }

    // Add by me
    @Override
    public GenericBehavior setFrameTime(double startTime, double duration) {
        return (GenericBehavior) super.setFrameTime(startTime, duration);
    }

    @Override
    public GenericBehavior setCycle(boolean bool) {
        return (GenericBehavior) super.setCycle(bool);
    }
    
    

}
