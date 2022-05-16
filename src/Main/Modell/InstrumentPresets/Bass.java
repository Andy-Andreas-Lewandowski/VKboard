package Main.Modell.InstrumentPresets;

import Main.Modell.Enums.Notes;

public class Bass extends InstrumentPreset {

    public Bass(){
        velocity = 100;
        bank = 0;
        instrument = 29;
        name = "Bass";
        keyboardCodesToNotes = KeysToScaleBindings.getGuitarBindings();
        for(Notes note : keyboardCodesToNotes.values())notes.add(note);
    }

}
