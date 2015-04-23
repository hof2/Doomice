package old.io.github.minixc.controls;

import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

public class HeadmasterControl extends EntityControl {

    Camera cam;

    public HeadmasterControl(Camera cam) {
        this.cam = cam;
    }
    
    @Override
    Vector3f getDirection() {
        return cam.getDirection();
    }
}
