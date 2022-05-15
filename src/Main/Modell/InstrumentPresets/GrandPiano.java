package Main.Modell.InstrumentPresets;

import Main.Modell.Enums.Notes;

public class GrandPiano extends InstrumentPreset {

    public GrandPiano(){
        velocity = 100;
        bank = 1024;
        instrument = 0;
        keyboardCodesToNotes = KeysToScaleBindings.getPianoBindings();
        for(Notes note : keyboardCodesToNotes.values())notes.add(note);

    }

}
