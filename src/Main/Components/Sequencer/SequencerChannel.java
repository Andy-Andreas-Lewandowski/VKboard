package Main.Components.Sequencer;

import Main.Components.EnumsAndMaps.Notes;
import Main.Components.Instrument.Synthesizer;

import java.util.*;

public class SequencerChannel {
    public int     currentStep = 0;
    public boolean isPlaying   = false;
    public ArrayList<ChannelObserver> observer = new ArrayList<>();

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
    }

    public void incrementStep(){
        currentStep = (currentStep+1) % (Sequencer.beatsInUse*Sequencer.STEPS_PER_BEAT);
        notifyOnStep();
    }
    public void setIsPlaying(boolean isPlaying){
        this.isPlaying = isPlaying;
        notifyOnPlayChange();
    }

    public void addNoteActivation(Notes note){
        Byte[] sequence = noteToSequences.get(note);
        sequence[currentStep] = -1;
    }

    public void addNoteDeactivation(Notes note){
        Byte[] sequence = noteToSequences.get(note);
        sequence[currentStep] = -2;
    }

    public void clearSequences(){
        for(Byte[] sequence : noteToSequences.values()){
            Arrays.fill(sequence,(byte)0);
        }
    }

    // -Observer
    // -- Channel Observer

     public void subscribeToChannel(ChannelObserver o){observer.add(o);}
     public void notifyOnStep(){observer.forEach(o -> o.onStepChange(currentStep));}
     public void notifyOnPlayChange(){observer.forEach(o -> o.onPlayChange(isPlaying));}

    public interface ChannelObserver {
        void onStepChange(int step);
        void onPlayChange(boolean isPlaying);
    }

}
