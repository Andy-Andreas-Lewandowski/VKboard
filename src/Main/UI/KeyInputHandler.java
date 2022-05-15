package Main.UI;


import Main.Service.RootService;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
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
        if (pressedKeys.contains(e.getKeyCode())) return;
        System.out.println(e.getKeyChar());
        if (e.getExtendedKeyCode() == 48) {
            root.piano.unloadInstrument();
        } else if (e.getExtendedKeyCode() == 66) {
            try {
                root.piano.sequenceChannel.startRecording();
            } catch (MidiUnavailableException ex) {
                throw new RuntimeException(ex);
            } catch (InvalidMidiDataException ex) {
                throw new RuntimeException(ex);
            }
        } else if (e.getExtendedKeyCode() == 78) {
            root.piano.sequenceChannel.stopRecording();
        }
        else if (e.getExtendedKeyCode() == 89) {
            root.piano.sequenceChannel.stopPlaying();
        } else if (e.getExtendedKeyCode() == 77) {
            try {
                root.piano.sequenceChannel.playSequence();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
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
