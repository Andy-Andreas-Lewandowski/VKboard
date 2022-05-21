package Main.Components.Instrument;

import Main.Components.EnumsAndMaps.KeyboardMapping;
import Main.Components.EnumsAndMaps.Notes;
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


    // Presets
    static int presetId = 0;
    public static ArrayList<SynthesizerPreset> presets = new ArrayList<>();

    // Synth
    static Synthesizer synthesizer = new Synthesizer();

    // Access Maps
    static HashMap<Integer,Notes> keyToNote = new HashMap<>();
    // Listener
    static ArrayList<PlayObserver>       playObserver       = new ArrayList<>();
    static ArrayList<InstrumentObserver> instrumentObserver = new ArrayList<>();

    // Setup
    public static void loadPreset(int id){
        SynthesizerPreset   preset      = presets.get(id);
        Iterator<Notes>     notes       = preset.getNotes().iterator();
        Iterator<Integer>   inputCodes  = KeyboardMapping.getAllPianorollInputCodes().iterator();

        while(notes.hasNext() && inputCodes.hasNext()){
            Notes   note = notes.next();
            Integer code = inputCodes.next();
            keyToNote.put(code,note);
        }

        synthesizer.loadPreset(preset);
        presetId = id;
        notifyOnInstrumentChange(presetId);
        System.out.println(preset.toString() + " is loaded!");
    }
    public static void loadNextPreset(){loadPreset((presetId+1)%presets.size());}

    public static Synthesizer cloneSynthesizer(){return (Synthesizer) synthesizer.clone();}
    public static Notes getNote(int key){
        return keyToNote.get(key);
    }

    // Play
    public static void playNote(int code){
        if(isCodeValid(code)) {
            Notes note = keyToNote.get(code);
            synthesizer.playNote(note);
            notifyOnPlay(note,code);
        }
    }

    public static void stopNote(int code){
        if(isCodeValid(code)) {
            Notes note = keyToNote.get(code);
            synthesizer.stopNote(note);
            notifyOnStop(note,code);
        }
    }

    private static boolean isCodeValid(int code){
        return keyToNote.getOrDefault(code,null) != null;
    }

    // - Observer
    // -- Play Observer
    public static void subscribeToPlay(PlayObserver o)  {playObserver.add(o);}
    public static void notifyOnPlay(Notes note, int key){playObserver.forEach(o -> o.onPlay(note, key));}
    public static void notifyOnStop(Notes note, int key){playObserver.forEach(o -> o.onStop(note, key));}

    public interface PlayObserver {void onPlay(Notes note, int key); void onStop(Notes note, int key);}

    // -- Instrument Observer
    public static void subscribeToInstrument(InstrumentObserver o){instrumentObserver.add(o);}
    public static void notifyOnInstrumentChange(int id){instrumentObserver.forEach(o -> o.onInstrumentChange(id));}

    public interface InstrumentObserver {void onInstrumentChange(int index);}


    public static void init(){
        presets.clear();

        presets = SynthesizerPreset.getPresets();

        loadPreset(0);

    }

}









