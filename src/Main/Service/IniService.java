package Main.Service;

import Main.Modell.InstrumentPresets.InstrumentPreset;
import Main.Modell.InstrumentPresets.OneScaleInstrument;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

public class IniService {

    public void initializePiano(Piano piano) throws MidiUnavailableException, InvalidMidiDataException {
        InstrumentPreset preset = new OneScaleInstrument();
        piano.loadInstrument(preset);

    }

}
