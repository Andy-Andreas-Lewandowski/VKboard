package Main.Components.Sequencer;

import Main.Components.EnumsAndMaps.Notes;
import Main.Components.Instrument.Synthesizer;

import java.util.*;

public class SequencerChannel {
    public int absoluteStep = 0;
    public int stepInBeat = 0;
    public int beat = 0;
    public boolean isPlaying   = false;
    public ArrayList<StepObserver> stepObserver = new ArrayList<>();
    public ArrayList<PlayObserver> playObserver = new ArrayList<>();
    public ArrayList<ChannelSynthObserver> channelSynthObserver = new ArrayList<>();

    public HashMap<Notes, Byte[]> noteToSequences = new HashMap<Notes,Byte[]>();
    public Synthesizer synthesizer;


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

    public void setStep(int newAbsoluteStep){
        beat = (int)(newAbsoluteStep/Sequencer.STEPS_PER_BEAT);
        absoluteStep = newAbsoluteStep % (Sequencer.beatsInUse*Sequencer.STEPS_PER_BEAT);
        stepInBeat = absoluteStep % Sequencer.STEPS_PER_BEAT;
        notifyOnStep();
    }

    public void incrementStep(){setStep(absoluteStep +1);}

    public void setIsPlaying(boolean isPlaying){
        this.isPlaying = isPlaying;
        notifyOnPlayChange();
    }

    public void addNoteActivation(Notes note){
        Byte[] sequence = noteToSequences.get(note);
        sequence[absoluteStep] = -1;
    }

    public void addNoteDeactivation(Notes note){
        Byte[] sequence = noteToSequences.get(note);
        sequence[absoluteStep] = -2;
    }

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
