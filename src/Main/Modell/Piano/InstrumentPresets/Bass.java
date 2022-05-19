package Main.Modell.Piano.InstrumentPresets;

public class Bass extends SynthesizerPreset {

    public Bass(){
        velocity = 80;
        bank = 0;
        instrument = 36;
        name = "Bass";
        lowerOctave = KeysToScaleBindings.getPianoLeftC2();
        upperOctave = KeysToScaleBindings.getPianoRightC3();
    }

}
