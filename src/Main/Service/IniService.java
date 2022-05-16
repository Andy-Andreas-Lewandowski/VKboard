package Main.Service;

import Main.Modell.InstrumentPresets.Bass;
import Main.Modell.InstrumentPresets.Guitar;
import Main.Modell.InstrumentPresets.InstrumentPreset;
import Main.Modell.InstrumentPresets.GrandPiano;
import Main.Modell.SequenceChannel;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

public class IniService {

    public void initializePiano(Piano piano) throws MidiUnavailableException, InvalidMidiDataException {
        for(int i = 0 ; i < 4 ; i++)piano.sequenceChannels.add(new SequenceChannel());

        piano.instruments.add(new GrandPiano());
        piano.instruments.add(new Guitar());
        piano.instruments.add(new Bass());

        piano.nextInstrument();
        piano.nextSequenceChannel();



    }

}
