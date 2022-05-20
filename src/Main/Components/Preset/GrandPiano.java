package Main.Components.Preset;
import Main.Components.EnumsAndMaps.Notes;
import java.util.List;

public class GrandPiano extends SynthesizerPreset {

    public GrandPiano(){
        velocity = 20;
        bank = 1024;
        instrument = 0;
        name = "Grand Piano";

        notes.addAll(Notes.getOctave(3));
        notes.addAll(Notes.getOctave(4));

    }
}
//bank = 1024;
//        instrument = 0;