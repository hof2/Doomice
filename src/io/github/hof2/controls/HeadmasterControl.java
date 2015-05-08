/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.hof2.controls;

import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.input.ChaseCamera;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

/**
 *
 * @author Matthias
 */
public class HeadmasterControl extends BetterCharacterControl {
    
    Camera cam;
    
    public HeadmasterControl(float radius, float height, float mass, Camera cam) {
        super(radius, height, mass);
        this.cam=cam;
        
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
        super.update(tpf);
        System.out.println(cam.getDirection());
        System.out.println(this.getViewDirection());
        Vector3f newDir = cam.getDirection().setY(0);
        this.setViewDirection(newDir);
    }
    
}
