/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.hof2.controls;

import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

/**
 *
 * @author Matthias
 */
public class HeadmasterControl extends BetterCharacterControl {
  
    
    public HeadmasterControl(float radius, float height, float mass) {
        super(radius, height, mass);
        
    }
    
    @Override
    public Vector3f getWalkDirection() {
        return super.getWalkDirection(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Vector3f getViewDirection() {
        return super.getViewDirection(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(float tpf) {
        super.update(tpf); //To change body of generated methods, choose Tools | Templates.
    }

    
}
