package Main.Service;

import Main.Modell.Enums.Notes;
import Main.Modell.InstrumentPresets.InstrumentPreset;
import Main.Modell.Piano.Octave;
import Main.Modell.SequenceChannel;

import javax.sound.midi.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Piano {
    // Synthesizers
    HashMap<Integer, Octave> keyboardCodeToOctave = new HashMap<>(); // Quick access to Synth per playable octave
    HashMap<Integer, Notes>  keyboardCodeToNote   = new HashMap<>(); // Quick access to Keyboard and Notes binding
    ArrayList<Octave> octaves = new ArrayList<>(); // Saving the refrence to Octave Synthesizer when reinitializing them
    ArrayList<InstrumentPreset> instrumentList = new ArrayList<>(); // List of instrument presets to change octaves behavior
    int nxtInstrumentIndex = 0; // Index to select next instrument preset
    InstrumentPreset instrumentSelected; // Current instrument preset

    // Sequencer
    public ArrayList<SequenceChannel> sequenceChannelList = new ArrayList<>(); // Refs to available sequencerChannels
    public int nxtSequenceChannelIndex = 0;
    public SequenceChannel sequenceChannelSelected = new SequenceChannel();

    public boolean areAllSeqPlaying = false;


    // Metronome
    public Metronome metronome = new Metronome();

    public Piano() throws MidiUnavailableException, InvalidMidiDataException {
        octaves.add(new Octave());
        octaves.add(new Octave());
    }


    /**
     *  Loads the next sequenceChannel, so it becomes target for operations on sequencers.
     *  Then it increments the nxtSequenceChannelIndex.
     *  Can't be executed when currently selected sequenceChannel is in a recording state.
     */
    public void nextSequenceChannel(){
        if(sequenceChannelSelected.getIsRecording()){
            System.out.println("Can't change Channel while it is recording!");
            return;
        }
        sequenceChannelSelected = sequenceChannelList.get(nxtSequenceChannelIndex);
        System.out.println("Sequencer " + nxtSequenceChannelIndex + " selected.");
        nxtSequenceChannelIndex = (nxtSequenceChannelIndex + 1) % sequenceChannelList.size();
    }

    /**
     * Loads the next instrument preset to the current preset. Calls the loadInstrument() Method to initialize the
     * Octave Synthesizer.
     *
     * @throws MidiUnavailableException
     * @throws InvalidMidiDataException
     */
    public void nextInstrument() throws MidiUnavailableException, InvalidMidiDataException {
        loadInstrument(instrumentList.get(nxtInstrumentIndex));
        nxtInstrumentIndex = (nxtInstrumentIndex + 1) % instrumentList.size();
    }


    /**
     * Clears the keybaordcode access Maps. Initializes the octave synthesizers with the data of
     * the given InstrumentPreset. Then updates the keyboardcode access maps with the InstrumentPreset data.
     *
     * @param preset
     * @throws MidiUnavailableException
     * @throws InvalidMidiDataException
     */
    public void loadInstrument(InstrumentPreset preset) throws MidiUnavailableException, InvalidMidiDataException {
        // Clear the access Maps and updates the InstrumentPreset.
        keyboardCodeToOctave.clear();
        keyboardCodeToNote.clear();
        this.instrumentSelected = preset;

        // Loads instruments to octave synthesizers
        octaves.get(0).loadInstrument(preset,preset.lowerOctave.values());
        octaves.get(1).loadInstrument(preset,preset.upperOctave.values());

        // Transfers lower octave keyboard bindings from preset to access maps
        for(Integer keyboardCode : preset.lowerOctave.keySet()){
            keyboardCodeToOctave.put(keyboardCode,octaves.get(0));
            keyboardCodeToNote.put(keyboardCode,preset.lowerOctave.get(keyboardCode));
        }
        // Transfers higher octave keyboard bindings from preset to access maps
        for(Integer keyboardCode : preset.upperOctave.keySet()){
            keyboardCodeToOctave.put(keyboardCode,octaves.get(1));
            keyboardCodeToNote.put(keyboardCode,preset.upperOctave.get(keyboardCode));
        }

        System.out.println("Instrument " + instrumentList.get(nxtInstrumentIndex).toString() + " selected.");
    }

    /**
     * Calls the loadInstrument() method of the selected SequenceChannel. Enables recording of sequences.
     * Necessary so a change of InstrumentPreset is not overwritting a recorded sequence.
     *
     * @throws MidiUnavailableException
     * @throws InvalidMidiDataException
     */
    public void connectInstrumentToSequencer() throws MidiUnavailableException, InvalidMidiDataException {
        sequenceChannelSelected.loadInstrument(instrumentSelected);
    }


    /**
     * Takes a keyboardcode and looks the related Note in keyboardCodeToNote and the related octave synthesizer in
     * keyboardCodeToOctave up. Then it calls in given octave the play command with the given Note.
     * (Needs to be fast because delay may appear)
     * @param keyboardCode
     */
    public void play(int keyboardCode){
        Notes note = keyboardCodeToNote.getOrDefault(keyboardCode,Notes.NONE);
        if(note == Notes.NONE)return;
        keyboardCodeToOctave.get(keyboardCode).play(note);
        if(sequenceChannelSelected.getIsRecording()){
            sequenceChannelSelected.addNote(note,true);
        }
    }

    /**
     * Takes a keyboardcode and looks the related Note in keyboardCodeToNote and the related octave synthesizer in
     * keyboardCodeToOctave up. Then it calls in given octave the stop command with the given Note.
     * (Needs to be fast because delay may appear)
     * @param keyboardCode
     */
    public void stop(int keyboardCode) throws InvalidMidiDataException {
            Notes note = keyboardCodeToNote.getOrDefault(keyboardCode,Notes.NONE);
            if(note == Notes.NONE)return;
            keyboardCodeToOctave.get(keyboardCode).stop(note);

            if (sequenceChannelSelected.getIsRecording()){
                sequenceChannelSelected.addNote(note,false);
            }
    }



}
