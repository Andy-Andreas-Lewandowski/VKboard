package Main.Modell.Piano;

import Main.Modell.Enums.Notes;

import javax.sound.midi.*;

public class Key {
    Sequencer s = MidiSystem.getSequencer();
    Synthesizer synthesizer;
    Receiver receiver;
    ShortMessage hit;
    ShortMessage pause;
    Notes note;

    public Key(Notes note, int velocity, int bank, int instrument) throws MidiUnavailableException, InvalidMidiDataException {
        this.note = note;
        // Initialize Synth
        synthesizer = MidiSystem.getSynthesizer();

        receiver = synthesizer.getReceiver();

        // Initialize Messages
        hit = new ShortMessage();
        hit.setMessage(ShortMessage.NOTE_ON,note.getCode(),velocity);
        pause = new ShortMessage(ShortMessage.NOTE_OFF,note.getCode(),velocity);

        synthesizer.open();
    }

    public Notes play(){
        receiver.send(hit,-1);
        return note;
    }

    public Notes stop(){
        receiver.send(pause,-1);
        return note;
    }

    public void terminate(){
        synthesizer.close();
    }




}
