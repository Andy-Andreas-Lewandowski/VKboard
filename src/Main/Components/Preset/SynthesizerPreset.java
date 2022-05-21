package Main.Components.Preset;

import Main.Components.EnumsAndMaps.Notes;
import Main.Components.Instrument.Pianoroll;

import java.util.ArrayList;
import java.util.List;

public abstract class SynthesizerPreset {
    int velocity;
    int bank;
    int instrument;
    boolean isDrumset = false;

    String name = "";

    List<Notes> notes = new ArrayList<>();

    public List<Notes> getNotes() {return notes;}

    public int getVelocity(){return velocity;}
    public int getBank(){return bank;}
    public int getInstrument(){return instrument;}
    public boolean getIsDrumset(){return isDrumset;}
    public String getName(){return name;}

    public String toString(){
        return name;
    }

    public static class GrandPiano extends SynthesizerPreset {
        public GrandPiano(){
            velocity = 25;
            bank = 1024;
            instrument = 0;
            name = "Grand Piano";

            notes.addAll(Notes.getOctave(3));
            notes.addAll(Notes.getOctave(4));

        }
    }
    public static class Viola extends SynthesizerPreset {
        public Viola(){
            velocity = 17;
            bank = 0;
            instrument = 41;
            name = "Viola";

            notes.addAll(Notes.getOctave(3));
            notes.addAll(Notes.getOctave(4));
        }
    }
    public static class Trumpet extends SynthesizerPreset {
        public Trumpet(){
            velocity = 20;
            bank = 0;
            instrument = 56;
            name = "Trumpet";

            notes.addAll(Notes.getOctave(3));
            notes.addAll(Notes.getOctave(4));
        }
    }

    public static class Whistle extends SynthesizerPreset {
        public Whistle(){
            velocity = 20;
            bank = 0;
            instrument = 78;
            name = "Whistle";

            notes.addAll(Notes.getOctave(3));
            notes.addAll(Notes.getOctave(4));
        }
    }

    public static class Guitar extends SynthesizerPreset {
        public Guitar(){
            velocity = 20;
            bank = 4096;
            instrument = 24;
            name = "Guitar";

            notes.addAll(Notes.getOctave(3));
            notes.addAll(Notes.getOctave(4));
        }
    }
    public static class Harpsicord extends SynthesizerPreset {
        public Harpsicord(){
            velocity = 20;
            bank = 3072;
            instrument = 6;
            name = "Harpsicord";

            notes.addAll(Notes.getOctave(2));
            notes.addAll(Notes.getOctave(3));
        }
    }
    public static class Vibraphone extends SynthesizerPreset {
        public Vibraphone(){
            velocity = 20;
            bank = 1024;
            instrument = 12;
            name = "Vibraphone";

            notes.addAll(Notes.getOctave(3));
            notes.addAll(Notes.getOctave(4));
        }
    }
    public static class ClassicOrgan extends SynthesizerPreset {
        public ClassicOrgan(){
            velocity = 20;
            bank = 0;
            instrument = 19;
            name = "Classic Organ";

            notes.addAll(Notes.getOctave(3));
            notes.addAll(Notes.getOctave(4));
        }
    }

    public static class b extends SynthesizerPreset {
        public b(){
            velocity = 20;
            bank = 0;
            instrument = 16;
            name = "or1 1";

            notes.addAll(Notes.getOctave(3));
            notes.addAll(Notes.getOctave(4));
        }
    }

    public static class c extends SynthesizerPreset {
        public c(){
            velocity = 20;
            bank = 0;
            instrument = 17;
            name = "or 1 2";

            notes.addAll(Notes.getOctave(3));
            notes.addAll(Notes.getOctave(4));
        }
    }
    public static class d extends SynthesizerPreset {
        public d(){
            velocity = 20;
            bank = 0;
            instrument = 18;
            name = "or 1 3";

            notes.addAll(Notes.getOctave(3));
            notes.addAll(Notes.getOctave(4));
        }
    }
    public static class e extends SynthesizerPreset {
        public e(){
            velocity = 20;
            bank = 2048;
            instrument = 16;
            name = "60s organ";

            notes.addAll(Notes.getOctave(3));
            notes.addAll(Notes.getOctave(4));
        }
    }
    public static class f extends SynthesizerPreset {
        public f(){
            velocity = 20;
            bank = 0;
            instrument = 20;
            name = "reed or";

            notes.addAll(Notes.getOctave(3));
            notes.addAll(Notes.getOctave(4));
        }
    }
    public static class g extends SynthesizerPreset {
        public g(){
            velocity = 20;
            bank = 1024;
            instrument = 16;
            name = "det 1";

            notes.addAll(Notes.getOctave(3));
            notes.addAll(Notes.getOctave(4));
        }
    }
    public static class h extends SynthesizerPreset {
        public h(){
            velocity = 20;
            bank = 1024;
            instrument = 17;
            name = "det 2";

            notes.addAll(Notes.getOctave(3));
            notes.addAll(Notes.getOctave(4));
        }
    }
    public static class i extends SynthesizerPreset {
        public i(){
            velocity = 20;
            bank = 4096;
            instrument = 16;
            name = "or4";

            notes.addAll(Notes.getOctave(3));
            notes.addAll(Notes.getOctave(4));
        }
    }
    public static class j extends SynthesizerPreset {
        public j(){
            velocity = 20;
            bank = 2048;
            instrument = 19;
            name = "ch or 3";

            notes.addAll(Notes.getOctave(3));
            notes.addAll(Notes.getOctave(4));
        }
    }
    public static ArrayList<SynthesizerPreset> getPresets(){
        ArrayList<SynthesizerPreset> presets = new ArrayList<>();
        presets.add(new GrandPiano());
        presets.add(new Viola());
        presets.add(new Trumpet());
        presets.add(new Whistle());
        presets.add(new Harpsicord());
        presets.add(new Vibraphone());
       /* presets.add(new b());
        presets.add(new c());
        presets.add(new d());
        presets.add(new e());
        presets.add(new f());
        presets.add(new g());
        presets.add(new h());
        presets.add(new i());
        presets.add(new j());*/

        return presets;

    }

}




