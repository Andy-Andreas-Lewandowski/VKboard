package Main.Components.Instrument;

import Main.Components.EnumsAndMaps.KeyboardMapping;
import Main.Components.EnumsAndMaps.Notes;
import Main.Components.Preset.GrandPiano;
import Main.Components.Preset.SynthesizerPreset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Pianoroll {

    // Singleton Boilerplate
    private Pianoroll(){}
    private static Pianoroll synthSetting = new Pianoroll();
    public static Pianoroll getInstance(){
        return synthSetting;
    }
    public static void buildPresets(){

    }

    // Presets
    static int presetId = 0;
    static ArrayList<SynthesizerPreset> presets = new ArrayList<>();

    // Synth
    static Synthesizer synthesizer = new Synthesizer();

    // Access Maps
    static HashMap<Integer,Notes> keyToNote = new HashMap<>();
    // Listener
    static ArrayList<PianorollObserver> observer = new ArrayList<>();

    // Setup
    public static void loadPreset(int id){
        SynthesizerPreset   preset      = presets.get(id);
        Iterator<Notes>     notes       = preset.getNotes().iterator();
        Iterator<Integer>   inputCodes  = KeyboardMapping.getAllInputCodes().iterator();

        while(notes.hasNext() && inputCodes.hasNext()){
            Notes   note = notes.next();
            Integer code = inputCodes.next();
            keyToNote.put(code,note);
        }

        synthesizer.loadPreset(preset);
        presetId = id;
    }

    public static Synthesizer cloneSynthesizer(){return (Synthesizer) synthesizer.clone();}

    // Play
    public static void playNote(int code){
        if(isCodeValid(code)) {
            Notes note = keyToNote.get(code);
            synthesizer.playNote(note);
            notifyOnPlay(note);
        }
    }

    public static void stopNote(int code){
        if(isCodeValid(code)) {
            Notes note = keyToNote.get(code);
            synthesizer.stopNote(note);
            notifyOnStop(note);
        }
    }

    private static boolean isCodeValid(int code){
        return keyToNote.getOrDefault(code,null) != null;
    }

    // Observer
    public static void subscribe(PianorollObserver o)  {observer.add(o);}
    public static void notifyOnPlay(Notes note)        {observer.forEach(o -> o.onPlay(note));}
    public static void notifyOnStop(Notes note)        {observer.forEach(o -> o.onStop(note));}

    public interface PianorollObserver {
        void onPlay(Notes note);
        void onStop(Notes note);
    }

    public static void init(){
        presets.clear();
        presets.add(new GrandPiano());

        loadPreset(0);

    }

}









