package Main.Modell.InstrumentPresets;

import Main.Modell.Enums.Notes;

public class OneScaleInstrument extends InstrumentPreset {

    public OneScaleInstrument(){
        velocity = 100;
        bank = 0;
        instrument = 0;
        notes = Notes.getThirdOctave();
    }

}
