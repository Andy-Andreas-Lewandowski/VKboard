package Main.Service;

import Main.Modell.InstrumentPresets.Guitar;
import Main.Modell.InstrumentPresets.InstrumentPreset;
import Main.Modell.InstrumentPresets.GrandPiano;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

public class IniService {

    public void initializePiano(Piano piano) throws MidiUnavailableException, InvalidMidiDataException {
        InstrumentPreset preset = new Guitar();
        piano.loadInstrument(preset);

    }

}
