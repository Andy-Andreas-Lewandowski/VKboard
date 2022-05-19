package Main.Modell;

import Main.Modell.Enums.KeyboardCodes;
import Main.Modell.Enums.Notes;
import Main.Modell.Piano.InstrumentPresets.Bass;
import Main.Modell.Piano.InstrumentPresets.GrandPiano;
import Main.Modell.Piano.InstrumentPresets.Guitar;
import Main.Modell.Piano.InstrumentPresets.SynthesizerPreset;
import Main.Modell.Piano.SynthesizerComponent;
import Main.Modell.Sequencer.StepSequencer;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import java.util.*;

public class Settings {


    // - Singleton Boilerplate
    private Settings(){}
    private static Settings settings = new Settings();

    public static Settings getInstance(){
        return settings;
    }

    // - DATA LAYER

    // -- Beats

    // --- Constants
    public final int maxBeats = 360;
    public final int stepsPerBeat = 32; // Entspricht: 1/32 Noten

    // --- Variables
    private static int bpm = 60;
    public void setBpm(int bpm){
        this.bpm = bpm;
        notifyBeatObservers();
    }
    public int getBpm(){return bpm;}

    private static int beatsInUse = 6;
    public void setBeatsInUse(int beatsInUse){
        assert 1 <= beatsInUse              : "Main.Modell.Sequencer must use at least one Beat!";
        assert maxBeats >= beatsInUse       : "Main.Modell.Sequencer can't use more than " + maxBeats + "!";
        this.beatsInUse = beatsInUse;

        notifySequenceObservers();
    }
    public int getBeatsInUse(){return beatsInUse;}


    // -- Instruments

    // --- Synthesizer Presets
    private static int synthPresetIndex = 0;
    private static void setSynthPresetIndex(int synthPresetIndex) {
        assert synthPresetIndex >= 0                        : "Synthesizer preset index can't be below 0";
        assert synthPresetIndex < synthPresetList.size()    : "Synthesizer preset index can't be below 0";
        Settings.synthPresetIndex = synthPresetIndex;
    }
    public static void loadNxtSynthPreset() throws InvalidMidiDataException {
        loadSynthPreset((synthPresetIndex + 1) % synthPresetList.size());
    }
    public static int getSynthPresetIndex() {return synthPresetIndex;}

    public static ArrayList<SynthesizerPreset> synthPresetList = new ArrayList<>();


    private static SynthesizerPreset synthPresetLoaded;
    public static SynthesizerPreset getSynthPresetLoaded() {return synthPresetLoaded;}
    public static void loadSynthPreset(int synthPresetIndex) throws InvalidMidiDataException {
        // Set index for preset
        setSynthPresetIndex(synthPresetIndex);
        synthPresetLoaded = synthPresetList.get(synthPresetIndex);
        // Initialize Iterators for Data connection
        Iterator<List<Notes>> notesPerOctave                    = synthPresetLoaded.getOctavesList().iterator();
        Iterator<List<KeyboardCodes>> keyboardCodesPerOctave    = musicKeys.iterator();
        Iterator<SynthesizerComponent> synthComponents          = synthCompList.iterator();
        // Iterate over Data
        while(notesPerOctave.hasNext() && keyboardCodesPerOctave.hasNext() && synthComponents.hasNext()){
            List<Notes> notes                = notesPerOctave.next();
            List<KeyboardCodes> codes        = keyboardCodesPerOctave.next();
            SynthesizerComponent synthComp   = synthComponents.next();
            // Load into Synthesizer and connect keyboard to notes and synthComps
            synthComp.loadInstrument(synthPresetLoaded, notes);
            connectKeyboardCodeToNotes(notes,codes);
            connectKeyboardCodeToSynthComp(synthComp,codes);
        }
        notifySynthObservers();
    }

    private static void connectKeyboardCodeToNotes(List<Notes> notes, List<KeyboardCodes> codes){
        Iterator<Notes>         nxtNote = notes.iterator();
        Iterator<KeyboardCodes> nxtCode = codes.iterator();

        while(nxtNote.hasNext() && nxtCode.hasNext()){
            Notes note         = nxtNote.next();
            KeyboardCodes code = nxtCode.next();

            keyboardCodeToNote.put(code.getCode(),note);;
        }
    }

    private static void connectKeyboardCodeToSynthComp(SynthesizerComponent synthComp, List<KeyboardCodes> codes){
        for(KeyboardCodes code : codes) keyboardCodeToSynthComp.put(code.getCode(),synthComp);
    }


    // --- Synthesizer Components
    private static ArrayList<SynthesizerComponent> synthCompList = new ArrayList<>();


    // --- Instrument Access Maps
    private static HashMap<Integer, SynthesizerComponent> keyboardCodeToSynthComp = new HashMap<>();
    public static  HashMap<Integer, SynthesizerComponent> getKeyboardCodeToSynthComp(){return keyboardCodeToSynthComp;}


    private static HashMap<Integer, Notes> keyboardCodeToNote = new HashMap<>();
    public static  HashMap<Integer, Notes> getKeyboardCodeToNote(){return keyboardCodeToNote;}

    // -- Sequencer
    private static int sequencerIndex = 0;
    private static void setSequencerIndex(int sequencerIndex){
        assert sequencerIndex >= 0             : "Main.Modell.Sequencer Index must be positive";
        assert sequencerIndex < seqList.size() : "There are only " + sequencerIndex + "sequencers available";

        Settings.sequencerIndex = sequencerIndex;
    }
    public static void loadNxtSequencer(){
        loadSequencer((sequencerIndex+1)%seqList.size());
    }

    private static ArrayList<StepSequencer> seqList = new ArrayList<>();
    public static ArrayList<StepSequencer> getSequencerList(){return seqList;}


    private static StepSequencer stepSeqLoaded;
    public static StepSequencer getStepSequencerLoaded(){return stepSeqLoaded;}
    public static void loadSequencer(int sequencerIndex){
        setSequencerIndex(sequencerIndex);
        notifySequenceObservers();
    }


    // -- Keyboard Settings
    public static final List<KeyboardCodes> leftMusicKeys = List.of(
            KeyboardCodes.Q,
            KeyboardCodes.TWO,
            KeyboardCodes.W,
            KeyboardCodes.THREE,
            KeyboardCodes.E,
            KeyboardCodes.R,
            KeyboardCodes.FIVE,
            KeyboardCodes.T,
            KeyboardCodes.SIX,
            KeyboardCodes.Y,
            KeyboardCodes.SEVEN,
            KeyboardCodes.U
    );

    public static final List<KeyboardCodes> rightMusicKeys = List.of(
            KeyboardCodes.V,
            KeyboardCodes.G,
            KeyboardCodes.B,
            KeyboardCodes.H,
            KeyboardCodes.N,
            KeyboardCodes.M,
            KeyboardCodes.K,
            KeyboardCodes.KOMMA,
            KeyboardCodes.L,
            KeyboardCodes.PERIOD,
            KeyboardCodes.SEMICOLON,
            KeyboardCodes.SLASH
    );

    public final static List<List<KeyboardCodes>> musicKeys = List.of(leftMusicKeys, rightMusicKeys);

    // - Event System
    public interface SettingsObserver{void update();}
    // -- Synthesizer Observer
    private static ArrayList<SettingsObserver> synthesizerObservers = new ArrayList<>();
    public static void subscribeToSynthesizer(SettingsObserver observer) {
        synthesizerObservers.add(observer);
    }
    private static void notifySynthObservers(){
        synthesizerObservers.forEach(observer -> observer.update());
    }

    // -- Sequencer Observer
    private static ArrayList<SettingsObserver> sequencerObservers = new ArrayList<>();
    public static void subscribeToSequencer(SettingsObserver observer) {
        synthesizerObservers.add(observer);
    }
    private static void notifySequenceObservers(){
        sequencerObservers.forEach(observer -> observer.update());
    }

    // -- Beats Observer
    private static ArrayList<SettingsObserver> beatObservers = new ArrayList<>();
    public static void subscribeToBeat (SettingsObserver observer){
        synthesizerObservers.add(observer);
    }
    private static void notifyBeatObservers(){
        beatObservers.forEach(observer -> observer.update());
    }



    public static class SettingsBuilder{
        public static void buildSettings() throws MidiUnavailableException, InvalidMidiDataException {
            // Build Main.Modell.Sequencer
            for(int i = 0 ; i < 4 ; i++)seqList.add(new StepSequencer());

            // Build Presets
            synthPresetList.add(new GrandPiano());
            synthPresetList.add(new Guitar());
            synthPresetList.add(new Bass());

            // Load Preset into Synth
            Settings.loadSynthPreset(0);
            // Load Main.Modell.Sequencer
            Settings.loadSequencer(0);

        }
    }

}
