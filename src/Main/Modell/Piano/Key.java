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
        synthesizer.open();

        // Change Instrument
        Soundbank soundBank = synthesizer.getDefaultSoundbank();
        MidiChannel[] channels = synthesizer.getChannels();
        channels[0].programChange(bank,instrument);


        receiver = synthesizer.getReceiver();

        // Initialize Messages
        hit = new ShortMessage();
        hit.setMessage(ShortMessage.NOTE_ON,0,note.getCode(),velocity);
        pause = new ShortMessage(ShortMessage.NOTE_OFF,0,note.getCode(),velocity);


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
