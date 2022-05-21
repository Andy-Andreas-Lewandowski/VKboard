package Main.Service;


import Main.Components.Instrument.Pianoroll;
import Main.Components.Sequencer.Metronome;
import Main.Components.Sequencer.Sequencer;
import Main.UI.UI;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

public class RootService {
    public Pianoroll pianoroll;
    public Sequencer sequencer;
    public Metronome metronome;

    UI ui;

    public RootService() throws MidiUnavailableException, InvalidMidiDataException {
        //System.out.println(Notes.getOctave(1));
        // Get instances
        pianoroll = Pianoroll.getInstance();
        pianoroll.init();


        sequencer = Sequencer.getInstance();
        sequencer.init();
        pianoroll.subscribeToPlay(sequencer);

        metronome = Metronome.getInstance();
        metronome.init();

        UI ui = new UI(this);


        //metronome = new Metronome(this);

        // Connect Instances




        //ui = new UI(this);





    }



}
