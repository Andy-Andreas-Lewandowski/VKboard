package Main.UI;


import Main.Components.Instrument.Pianoroll;
import Main.Components.Sequencer.Metronome;
import Main.Components.Sequencer.Sequencer;
import Main.Service.RootService;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Set;
import java.util.TreeSet;

public class KeyInputHandler implements KeyListener {
    RootService root;
    Set<Integer> pressedKeys = new TreeSet<Integer>();

    Pianoroll pianoroll = Pianoroll.getInstance();
    Sequencer sequencer = Sequencer.getInstance();
    Metronome metronome = Metronome.getInstance();

    public KeyInputHandler(RootService root){

        this.root = root;

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (pressedKeys.contains(e.getKeyCode())) return;
        // Num 1 - Start recording on selected Channel.
        if (e.getExtendedKeyCode() == 97) {
            if(sequencer.isNotBlocked()) {
                sequencer.startRecording();
            }else {
                sequencer.stopRecording();
            }
        // Num 2 - Connect Instrument
        } else if (e.getExtendedKeyCode() == 98) {
            Sequencer.loadSynthesizerToSequencer(0);

        // Num 3 - Clear selected Channel.*/
        } else if (e.getExtendedKeyCode() == 99) {
            Sequencer.clearSelectedChannel();
        //Num 4 - Play selected Channel.
        } else if (e.getExtendedKeyCode() == 100) {
           if(sequencer.isNotBlocked()){
                sequencer.playThisSequence();
            }else{
                sequencer.stopThisSequence();
            }

/*        //Num 6 - Play all Channels. TODO
        else if (e.getExtendedKeyCode() == 102) {
           *//* boolean isASeqPlaying = false;
            for(SequencerChannel seq : piano.sequencerChannels){
                isASeqPlaying = seq.getIsPlaying() || isASeqPlaying;
            }
            if(isASeqPlaying){
                piano.stopAllSequencer();
            }else{
                piano.playAllSequencer();
            }
*//*
        }
        //Num 7 - Select Instrument.
        else if (e.getExtendedKeyCode() == 103) {
           *//* try {
                piano.nextInstrument();
            } catch (MidiUnavailableException ex) {
                throw new RuntimeException(ex);
            } catch (InvalidMidiDataException ex) {
                throw new RuntimeException(ex);
            }
        // Num 9 - Change Sequence*//*
        }else if(e.getExtendedKeyCode() == 105) {
*//*            piano.nextSequenceChannel();
        ;*/
        //Num . - Start/Stop Metronome*//*
        }
        //Num 7 - Select Instrument.
        else if (e.getExtendedKeyCode() == 103) {
            Pianoroll.loadNextPreset();
            System.out.println("Hey");
        }else if (e.getExtendedKeyCode() == 110) {
            if (!Metronome.getIsPlaying()){
                System.out.println("Metronome started!");
                metronome.startMetronome();
            }
            else{
                metronome.stopMetronome();
                System.out.println("Metronome stopped!");
            }
        }else{
            root.pianoroll.playNote(e.getKeyCode());
        }
        pressedKeys.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
            pianoroll.stopNote(e.getKeyCode());
    }
}
