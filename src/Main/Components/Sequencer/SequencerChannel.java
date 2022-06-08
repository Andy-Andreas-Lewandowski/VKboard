package Main.Components.Sequencer;

import Main.Components.EnumsAndMaps.Notes;
import Main.Components.Instrument.Synthesizer;

import java.util.*;

/**
 * Class that organizes Javax.Sound.Midi components and a synthesizer object but also adds additional functionality
 * to it to record and play midi sequences. It resembles a step sequencer channel.
 */
public class SequencerChannel {
    // States
    public int absoluteStep = 0;
    public int stepInBeat = 0;
    public int beat = 0;
    public boolean isPlaying   = false;
    // Observer
    public ArrayList<StepObserver> stepObserver = new ArrayList<>();
    public ArrayList<PlayObserver> playObserver = new ArrayList<>();
    public ArrayList<ChannelSynthObserver> channelSynthObserver = new ArrayList<>();
    // Objects
    public HashMap<Notes, Byte[]> noteToSequences = new HashMap<Notes,Byte[]>(); // Sequence entity saved by Notes
    public Synthesizer synthesizer;


    /**
     *  Loads a Synthesizer to Sequence channel.
     * @param synthesizer - Instrument.Synthesizer.
     */
    public void loadSynthesizer(Synthesizer synthesizer){
        this.synthesizer = synthesizer;
        Collection<Notes> notes = synthesizer.getNotes();

        for(Notes note : notes){
            Byte[] sequence = new Byte[Sequencer.MAX_BEATS * Sequencer.STEPS_PER_BEAT];
            Arrays.fill(sequence,(byte) 0);
            noteToSequences.put(note,sequence);
        }
        notifyOnSynthLoad();
    }

    /**
     * Sets the current step in the sequence. This setter makes sure that the step, beat and step inside the beat is
     * a legal state. It does not throw an ERROR or Exception if the step is illegal and
     * instead uses modular arithmetics.
     * @param newAbsoluteStep - int for current step.
     */
    public void setStep(int newAbsoluteStep){
        beat = (int)(newAbsoluteStep/Sequencer.STEPS_PER_BEAT);
        absoluteStep = newAbsoluteStep % (Sequencer.beatsInUse*Sequencer.STEPS_PER_BEAT);
        stepInBeat = absoluteStep % Sequencer.STEPS_PER_BEAT;
        notifyOnStep();
    }

    /**
     * Increments current sequence step and makes sure that step, beat and beat in step is legal by using
     * modular arithmetics.
     */
    public void incrementStep(){setStep(absoluteStep +1);}

    /**
     * Sets state to isPlaying is true and notifies subscribers.
     */
    public void setIsPlaying(boolean isPlaying){
        this.isPlaying = isPlaying;
        notifyOnPlayChange();
    }

    /**
     * Adds noteOn to recorded Sequence.
     * @param note
     */
    public void addNoteActivation(Notes note){
        Byte[] sequence = noteToSequences.get(note);
        sequence[absoluteStep] = -1;
    }

    /**
     * Adds noteOff to recorded Sequence.
     * @param note
     */
    public void addNoteDeactivation(Notes note){
        Byte[] sequence = noteToSequences.get(note);
        sequence[absoluteStep] = -2;
    }

    /**
     * Clears recorded sequence.
     */
    public void clearSequences(){
        if(!isPlaying && !(this == Sequencer.selectedChannel && Sequencer.isRecording)) {
            for (Byte[] sequence : noteToSequences.values()) {
                Arrays.fill(sequence, (byte) 0);
            }
        }
    }

    // -Observer
    // -- Channel Observer

     public void subscribeToChannelStep(StepObserver o){
         stepObserver.add(o);}
     public void notifyOnStep(){
         stepObserver.forEach(o -> o.onStepChange(absoluteStep,stepInBeat,beat));}

    public interface StepObserver {
        void onStepChange(int absoluteStep, int stepInBeat, int beat);

    }


    public interface PlayObserver{
        void onPlayChange(boolean isPlaying);
    }
    public void notifyOnPlayChange(){
       playObserver.forEach(o -> o.onPlayChange(isPlaying));}
    public void subscribeToChannelPlay(PlayObserver o){
        playObserver.add(o);}


    public interface ChannelSynthObserver{
        void onSynthLoad(Synthesizer synthesizer);
    }
    public void notifyOnSynthLoad(){
        channelSynthObserver.forEach(o -> o.onSynthLoad(synthesizer));}
    public void subscribeToChannelSynth(ChannelSynthObserver o){
        channelSynthObserver.add(o);}
}
