package Main.Service;

import Main.Modell.InstrumentPresets.Bass;
import Main.Modell.InstrumentPresets.Guitar;
import Main.Modell.InstrumentPresets.GrandPiano;
import Main.Modell.StepSequencer;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

public class IniService {

    public void initializePiano(Piano piano) throws MidiUnavailableException, InvalidMidiDataException {
        for(int i = 0 ; i < 4 ; i++)piano.sequencerChannels.add(new StepSequencer(piano));

        piano.instrumentList.add(new GrandPiano());
        piano.instrumentList.add(new Guitar());
        piano.instrumentList.add(new Bass());

        piano.nextInstrument();
        piano.nextSequenceChannel();



    }

}
