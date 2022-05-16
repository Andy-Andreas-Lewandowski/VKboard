package Main.Modell.InstrumentPresets;

public class Guitar extends InstrumentPreset {

    public Guitar(){
        velocity = 70;
        bank = 0;
        instrument = 29;
        name = "Guitar";
        lowerOctave = KeysToScaleBindings.getPianoLeftC3();
        upperOctave = KeysToScaleBindings.getPianoRightC4();

    }

    //0,29 OD Guitar

}
