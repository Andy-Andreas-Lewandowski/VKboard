package Main.Service;


import Main.Modell.Settings;
import Main.UI.UI;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

public class RootService {
    public Piano piano;
    public SynthController synthController;
    public SequencerController sequencerController;
    public Metronome metronome;
    public Settings settings;

    UI ui;

    public RootService() throws MidiUnavailableException, InvalidMidiDataException {
        // Get instances
        settings = Settings.getInstance();
        synthController = new SynthController(this);
        sequencerController = new SequencerController(this);
        metronome = new Metronome(this);

        // Connect Instances
        settings.subscribeToSynthesizer(synthController);
        settings.subscribeToSequencer(sequencerController);
        settings.subscribeToBeat(metronome);



        synthController.subscribeToSynthesizerInput(sequencerController);



        //ui = new UI(this);




    }



}
