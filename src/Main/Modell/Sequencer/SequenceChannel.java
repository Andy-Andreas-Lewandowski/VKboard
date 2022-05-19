package Main.Modell.Sequencer;

import Main.Modell.Enums.Notes;
import Main.Modell.Piano.InstrumentPresets.SynthesizerPreset;

import Main.Modell.Piano.SynthesizerComponent;

//TODO: Make step sequencer
import javax.sound.midi.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SequenceChannel {
    boolean isRecording = false;
    boolean isPlaying = false;
    HashMap<Notes,LinkedList<Long>> noteToSequences = new HashMap<Notes,LinkedList<Long>>();
    HashMap<Notes, Long> timeStamps = new HashMap<Notes, Long>();
    HashMap<Notes, SynthesizerComponent> notesToOctave = new HashMap<Notes, SynthesizerComponent>();

    ArrayList<SynthesizerComponent> synthesizerComponents = new ArrayList<>();

    Long recordTime = 0L;
    Long startingTimeStamp = 0L;
    SynthesizerPreset instrument;


    public boolean getIsRecording(){return isRecording;}


    public boolean isPlaying() {return isPlaying;}

    public Long getRecordTime() {
        return recordTime;
    }

    public void clear(){
        if(isRecording || isPlaying){System.out.println("Can't clear while recording or playing.");}
        noteToSequences.clear();
        timeStamps.clear();
        System.out.println("Sequence Channel is cleared!");
    }
    public SequenceChannel() throws MidiUnavailableException, InvalidMidiDataException {
        synthesizerComponents.add(new SynthesizerComponent());
        synthesizerComponents.add(new SynthesizerComponent());
    }

    public void loadInstrument(SynthesizerPreset instrument) throws MidiUnavailableException, InvalidMidiDataException {
        clear();
        this.instrument = instrument;
        notesToOctave.clear();

        for(Notes note : instrument.lowerOctave.values()){
            noteToSequences.put(note,new LinkedList());
            notesToOctave.put(note, synthesizerComponents.get(0));
            timeStamps.put(note,0L);
            synthesizerComponents.get(0).loadInstrument(instrument,instrument.lowerOctave.values());
        }
        for(Notes note : instrument.upperOctave.values()){
            noteToSequences.put(note,new LinkedList());
            notesToOctave.put(note, synthesizerComponents.get(1));
            timeStamps.put(note,0L);
            synthesizerComponents.get(1).loadInstrument(instrument,instrument.upperOctave.values());
        }
        System.out.println("Instrument " + instrument.toString() + " is connected to Sequence!");
    }

    //Recording Part

    public void startRecording() throws MidiUnavailableException, InvalidMidiDataException {
        if(instrument == null){
            System.out.println("Please connect an Instrument with this Channel first!");
            return;
        }
        long timeStamp = System.currentTimeMillis() + 20;
        startingTimeStamp = timeStamp;

        for(Notes key : timeStamps.keySet()){
            timeStamps.put(key,timeStamp);
        }
        while(System.currentTimeMillis()<startingTimeStamp)

        isRecording = true;
        System.out.println("Recording started!");
    }
    public void stopRecording(){
        System.out.println(recordTime);
        isRecording = false;
        Long timeStamp = System.currentTimeMillis();
        for(Notes note : noteToSequences.keySet()){
            LinkedList list = noteToSequences.get(note);
            Long newPause = timeStamp - timeStamps.get(note);
            list.add(newPause);
            System.out.println(list);
        }


        recordTime = (System.currentTimeMillis() - startingTimeStamp) +recordTime;



        System.out.println(recordTime);

        System.out.println("Recording stopped!");
    }
    public void addNote(Notes note,boolean shouldActivate){
        LinkedList sequence = noteToSequences.get(note);
        Long newTimeStamp = System.currentTimeMillis();
        sequence.add(newTimeStamp-timeStamps.get(note));
        if(shouldActivate) {
            sequence.add(-1L);
        }else{
            sequence.add(-2L);
        }
        timeStamps.put(note,newTimeStamp);
    }


    // Play sequence Part.

    public PlayLoop playSequence() throws InterruptedException {
        if(isRecording){
            System.out.println("Can't play sequence while recording!");
            return null;
        }
        PlayLoop playLoop = new PlayLoop();
        playLoop.start();
        System.out.println("Sequence started playing!");
        return playLoop;
    }

    public void stopPlaying(){
        isPlaying = false;
        System.out.println("Sequence stopped playing!");
    }

    public class PlayLoop extends Thread{
        @Override
        public void run(){
            isRecording = false;
            isPlaying = true;
            ExecutorService executorService = Executors.newCachedThreadPool();

            while(isPlaying) {
                System.out.println("StepClockRecord Time: " + recordTime);
                Long startTimeStamp = System.currentTimeMillis();
                for (Notes key : noteToSequences.keySet()) {
                    executorService.execute(new PlayKeySequence(key, notesToOctave.get(key), noteToSequences.get(key)));
                }
                try {
                    executorService.awaitTermination(recordTime, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }


            }
        }
    }


    public class PlayKeySequence extends Thread{
        SynthesizerComponent synthesizerComponent;
        Notes note;
        LinkedList<Long> sequence;

        public PlayKeySequence(Notes note, SynthesizerComponent synthesizerComponent, LinkedList<Long> sequence){
            this.sequence = sequence;
            this.synthesizerComponent = synthesizerComponent;
            this.note = note;
        }

        @Override
        public void run(){
            System.out.println(sequence);
            for(Long elem : sequence){
                if(elem >= 0){
                    try {
                        Thread.sleep(elem);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }else if(elem == -1L){
                    synthesizerComponent.play(note);
                }else if(elem == -2L){
                    try {
                        synthesizerComponent.stop(note);}
                    catch (Exception e) {throw new RuntimeException(e);}
                }
                if(!isPlaying)return;
            }
        }
    }
}
