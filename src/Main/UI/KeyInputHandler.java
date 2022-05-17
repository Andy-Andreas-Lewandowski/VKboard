package Main.UI;


import Main.Modell.SequenceChannel;
import Main.Service.Piano;
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
        Piano piano = root.piano;
        SequenceChannel seqChannel = piano.sequenceChannelSelected;

        if (pressedKeys.contains(e.getKeyCode())) return;
        // Num 1 - Start recording on selected Channel.
        if (e.getExtendedKeyCode() == 97) {
            try {
                if (seqChannel.getIsRecording()) {
                    seqChannel.stopRecording();
                } else {
                    seqChannel.startRecording();
                }
            } catch (MidiUnavailableException ex) {
                throw new RuntimeException(ex);
            } catch (InvalidMidiDataException ex) {
                throw new RuntimeException(ex);
            }
        } else if (e.getExtendedKeyCode() == 98) {
            try {
                piano.connectInstrumentToSequencer();
            } catch (MidiUnavailableException ex) {
                throw new RuntimeException(ex);
            } catch (InvalidMidiDataException ex) {
                throw new RuntimeException(ex);
            }
            // Num 3 - Clear selected Channel.
        } else if (e.getExtendedKeyCode() == 99) {
            seqChannel.clear();
        //Num 4 - Play selected Channel.
        } else if (e.getExtendedKeyCode() == 100) {
            if(seqChannel.isPlaying()){
                seqChannel.stopPlaying();
            }else{
                try {
                    seqChannel.playSequence();
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        //Num 6 - Play all Channels. TODO
        else if (e.getExtendedKeyCode() == 102) {
            if(piano.areAllSeqPlaying) piano.stopAllSeq();
            else piano.playAllSeq();

        }
        //Num 7 - Select Instrument.
        else if (e.getExtendedKeyCode() == 103) {
            try {
                piano.nextInstrument();
            } catch (MidiUnavailableException ex) {
                throw new RuntimeException(ex);
            } catch (InvalidMidiDataException ex) {
                throw new RuntimeException(ex);
            }
        // Num 9 - Change Sequence
        }else if(e.getExtendedKeyCode() == 105) {
            piano.nextSequenceChannel();
            //Num . - Start/Stop Metronome
        } else if (e.getExtendedKeyCode() == 110) {
            if(piano.metronome.getIsPlaying())piano.metronome.stop();
            else piano.metronome.start();
        }else{
            root.piano.play(e.getKeyCode());
        }
        pressedKeys.add(e.getKeyCode());

    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
        try {
            root.piano.stop(e.getKeyCode());
        } catch (InvalidMidiDataException ex) {
            throw new RuntimeException(ex);
        }
    }
}
