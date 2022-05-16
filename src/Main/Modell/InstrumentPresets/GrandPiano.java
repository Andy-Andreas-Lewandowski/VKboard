package Main.Modell.InstrumentPresets;

public class GrandPiano extends InstrumentPreset {

    public GrandPiano(){
        velocity = 20;
        bank = 1024;
        instrument = 0;
        name = "Grand Piano";
        lowerOctave = KeysToScaleBindings.getPianoLeftC3();
        upperOctave = KeysToScaleBindings.getPianoRightC4();

    }
}
//bank = 1024;
//        instrument = 0;