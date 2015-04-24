
package io.github.hof2;

import com.jme3.app.SimpleApplication;
import io.github.hof2.states.GameAppState;

public class Main extends SimpleApplication {

    @Override
    public void simpleInitApp() {
        stateManager.attach(new GameAppState());
    }
    
    public static void main(String[] args) {
        new Main().start();
    }
    
}
