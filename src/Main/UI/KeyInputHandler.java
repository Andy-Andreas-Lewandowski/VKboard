package Main.UI;


import Main.Service.RootService;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Set;
import java.util.TreeSet;

public class KeyInputHandler implements KeyListener {
    RootService root;
    Set<Integer> pressedKeys = new TreeSet<Integer>();
    public KeyInputHandler(RootService root){
        this.root = root;


    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(pressedKeys.contains(e.getKeyCode())) return;
        System.out.println(e.getKeyChar());
        if(e.getExtendedKeyCode()==48){
            root.piano.unloadInstrument();
        }else{
            root.piano.play(e.getKeyCode());
        }
        pressedKeys.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
        root.piano.stop(e.getKeyCode());

    }
}
