package Main.Components.Preset;

import Main.Components.EnumsAndMaps.Notes;

import java.util.ArrayList;
import java.util.List;

public abstract class SynthesizerPreset {
    int velocity;
    int bank;
    int instrument;
    boolean isDrumset = false;

    String name;

    List<Notes> notes = new ArrayList<>();

    public List<Notes> getNotes() {return notes;}

    public int getVelocity(){return velocity;}
    public int getBank(){return bank;}
    public int getInstrument(){return instrument;}
    public boolean getIsDrumset(){return isDrumset;}

    public String toString(){
        return name;
    }

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
    public class s extends SynthesizerPreset {

        public s(){
            velocity = 20;
            bank = 1024;
            instrument = 0;
            name = "Grand Piano";

            notes.addAll(Notes.getOctave(3));
            notes.addAll(Notes.getOctave(4));

        }
    }

}
