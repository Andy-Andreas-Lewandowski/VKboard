package Main.Modell.InstrumentPresets;

import Main.Modell.Enums.Notes;

public class Guitar extends InstrumentPreset {

    public Guitar(){
        velocity = 100;
        bank = 0;
        instrument = 29;
        name = "Guitar";
        keyboardCodesToNotes = KeysToScaleBindings.getGuitarBindings();
        for(Notes note : keyboardCodesToNotes.values())notes.add(note);

    }

    //0,29 OD Guitar

}
