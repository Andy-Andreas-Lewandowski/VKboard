package Main.Components.Preset;
import Main.Components.EnumsAndMaps.Notes;

public class GrandPianoOld extends SynthesizerPreset {

    public GrandPianoOld(){
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