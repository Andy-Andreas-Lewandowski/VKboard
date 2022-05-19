package Main.Modell.Sequencer;

import Main.Modell.Enums.Notes;
import Main.Modell.Piano.InstrumentPresets.SynthesizerPreset;
import Main.Modell.Piano.SynthesizerComponent;
import Main.Service.Piano;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import java.util.*;
import java.util.concurrent.*;

public class StepSequencer {

    Piano piano;

    int currentStep = 0;


    boolean isRecording = false;
    boolean isPlaying = false;
    HashMap<Notes, Byte[]> noteToSequences = new HashMap<Notes,Byte[]>();
    HashMap<Notes, SynthesizerComponent> notesToOctave = new HashMap<Notes, SynthesizerComponent>();


    ArrayList<SynthesizerComponent> synthesizerComponents = new ArrayList<>();

    SynthesizerPreset instrument;
    public StepSequencer() throws MidiUnavailableException, InvalidMidiDataException {
        synthesizerComponents.add(new SynthesizerComponent());
        synthesizerComponents.add(new SynthesizerComponent());

    }
    // ###################
    // # Getter & Setter #
    // ###################
    // Getter
    public boolean getIsRecording(){
        return isRecording;
    }
    public boolean getIsPlaying() {return isPlaying;}
    // Setter
    public void  incrementStep(){currentStep = (currentStep + 1) % (piano.beatsInUse * piano.stepsPerBeat);}
    public void setIsPlaying(boolean isPlaying){
        if(isRecording){
            System.out.println("You can't play this sequence while recording!");
        }
        else{
            this.isPlaying =isPlaying;
            if(isPlaying){
                System.out.println("Started Playing!");
            }else{
                System.out.println("Stopped Playing!");
            }
        }
    }
    public void setCurrentStep(int step){
        currentStep = step;
    }

    public void clear(){
        if(isRecording || isPlaying){System.out.println("Can't clear while recording or playing.");}
        for(Byte[] hits : noteToSequences.values()){
            Arrays.fill(hits,(byte)0);
        }

        noteToSequences.clear();
        System.out.println("Sequence Channel is cleared!");
    }


    public void loadInstrument(SynthesizerPreset instrument) throws MidiUnavailableException, InvalidMidiDataException {
        clear();
        this.instrument = instrument;
        notesToOctave.clear();

        for(Notes note : instrument.lowerOctave.values()){
            Byte[] hits = new Byte[piano.stepsPerBeat * piano.maxBeats];
            Arrays.fill(hits,(byte)0);
            noteToSequences.put(note,hits);
            notesToOctave.put(note, synthesizerComponents.get(0));
            synthesizerComponents.get(0).loadInstrument(instrument,instrument.lowerOctave.values());
        }
        for(Notes note : instrument.upperOctave.values()){
            Byte[] hits = new Byte[piano.stepsPerBeat*piano.maxBeats];
            Arrays.fill(hits,(byte)0);
            noteToSequences.put(note,hits);
            notesToOctave.put(note, synthesizerComponents.get(1));
            synthesizerComponents.get(1).loadInstrument(instrument,instrument.upperOctave.values());
        }
        System.out.println("Instrument " + instrument.toString() + " is connected to Sequence!");
    }

    //Recording Part
    public void startRecording() throws MidiUnavailableException, InvalidMidiDataException {
        if(instrument == null){
            System.out.println("Please connect an Instrument with this Channel first!");
            return;
        }else if(isPlaying){
            System.out.println("Can't record while playing!");
            return;
        }
        isRecording = true;
        new StepClockRecord().start();
        System.out.println("Recording started!");
    }

    public void stopRecording(){
        isRecording = false;
        System.out.println("Stopped Recording!");
    }

    public void addNote(Notes note,boolean shouldActivate){
        if(!isRecording) return;

        Byte[] sequence = noteToSequences.get(note);
        if(shouldActivate)  sequence[currentStep] = -1;
        else                sequence[currentStep] = -2;
    }

    public class StepClockRecord extends Thread{
        long milliPerStep;

        @Override
        public void run(){
            while(isRecording) {
                milliPerStep = (60000 / piano.bpm) / piano.stepsPerBeat;
                try {
                    Thread.sleep(milliPerStep);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                incrementStep();
            }
        }
    }



    // Play record

    public void startPlaying(){
        // Prevent Illegal State
        if(isRecording){
            System.out.println("Can't play the while recording!");
            return;
        }

        isPlaying = true;
        new PlayThisSequence().start();

        System.out.println("Started Playing!");
    }

    public SequencePlayer getSequencePlayer(){
        return new SequencePlayer();
    }

    public void stopPlaying(){
        isPlaying = false;
        System.out.println("Stopped Playing!");
    }

    public ArrayList<PlayNote> getPlayNoteList(){
        ArrayList<PlayNote> noteList = new ArrayList<>();
        for(Notes note : noteToSequences.keySet()){
            SynthesizerComponent synthesizerComponent = notesToOctave.get(note);
            Byte[] sequence = noteToSequences.get(note);
            noteList.add(new PlayNote(note, synthesizerComponent,sequence));
        }

        return noteList;
    }





    public class PlayThisSequence extends Thread{

        @Override
        public void run() {
            ExecutorService executorService = Executors.newCachedThreadPool();
            long timePerStep = (long)Math.floor((60000/piano.bpm)/piano.stepsPerBeat);
            while (isPlaying) {
                long startTime = System.currentTimeMillis();
                //Execute Threads
                for(Notes note : noteToSequences.keySet()){
                    executorService.execute(new PlayNote(note,notesToOctave.get(note),noteToSequences.get(note)));
                }

                //Wait for execution
                try {
                    executorService.awaitTermination(timePerStep, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                // Next Sequence Step
                incrementStep();

                // Wait for next step
                long endTime = System.currentTimeMillis();
                long timeDiffrence = (startTime+timePerStep-2) - endTime;
                if(timeDiffrence > 0L){
                    try {
                        Thread.sleep(timeDiffrence);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
    public class SequencePlayer extends Thread{
        public SequencePlayer(){


        }

        @Override
        public void run() {
            System.out.println(currentStep);
            ExecutorService executorService = Executors.newCachedThreadPool();
            long timePerStep = (long)Math.floor((60000/piano.bpm)/piano.stepsPerBeat);
            long startTime = System.currentTimeMillis();
            //Execute Threads
            for(Notes note : noteToSequences.keySet()){
                executorService.execute(new PlayNote(note,notesToOctave.get(note),noteToSequences.get(note)));
            }

            //Wait for execution
            try {
                executorService.awaitTermination(timePerStep, TimeUnit.MILLISECONDS);
                executorService.shutdown();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            // Next Sequence Step
            incrementStep();
            piano.allSequencerFinished.countDown();

        }
    }



    public class PlayNote extends Thread {
        Notes note;
        SynthesizerComponent synthesizerComponent;
        Byte[] sequence;
        int step = currentStep;

        // Initialize NotePlayer
        public PlayNote(Notes note, SynthesizerComponent synthesizerComponent, Byte[] sequence) {
            this.note = note;
            this.synthesizerComponent = synthesizerComponent;
            this.sequence = sequence;
        }

        @Override
        public void run() {
            if (sequence[currentStep] == -1) {
                synthesizerComponent.play(note);
            }else if(sequence[currentStep] == -2){
                synthesizerComponent.stop(note);
            }
        }
    }





}
