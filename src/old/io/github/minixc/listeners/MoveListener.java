package old.io.github.minixc.listeners;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import old.io.github.minixc.controls.EntityControl;

public class MoveListener implements ActionListener {

    EntityControl player;
    InputManager input;

    public MoveListener(EntityControl player, InputManager input) {
        this.player = player;
        this.input = input;
        init();
    }
    
    private void init() {
        input.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        input.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        input.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
        input.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
        input.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
        input.addListener(this, "Left");
        input.addListener(this, "Right");
        input.addListener(this, "Up");
        input.addListener(this, "Down");
        input.addListener(this, "Jump");
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        switch (name) {
            case "Left":
                player.setLeft(isPressed);
                break;
            case "Right":
                player.setRight(isPressed);
                break;
            case "Up":
                player.setUp(isPressed);
                break;
            case "Down":
                player.setDown(isPressed);
                break;
            case "Jump":
                player.jump();
                break;
        }
    }
}
