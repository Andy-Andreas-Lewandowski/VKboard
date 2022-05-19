package Main.Modell.Piano.InstrumentPresets;
import Main.Modell.Enums.Notes;
import java.util.List;

public class GrandPiano extends SynthesizerPreset {

    public GrandPiano(){
        velocity = 20;
        bank = 1024;
        instrument = 0;
        name = "Grand Piano";
        octavesList = List.of(Notes.getOctave(3),Notes.getOctave(4));

    }
}
//bank = 1024;
//        instrument = 0;