package Main.Service;

import Main.Modell.InstrumentPresets.InstrumentPreset;
import Main.Modell.InstrumentPresets.OneScaleInstrument;

public class IniService {

    public void initializePiano(Piano piano){
        InstrumentPreset preset = new OneScaleInstrument();
        piano.loadInstrument(preset);

    }

}
