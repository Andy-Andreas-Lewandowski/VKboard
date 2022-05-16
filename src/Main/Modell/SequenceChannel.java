package Main.Modell;

import Main.Modell.Enums.Notes;
import Main.Modell.InstrumentPresets.InstrumentPreset;

import Main.Modell.Piano.Octave;


import javax.sound.midi.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class SequenceChannel {
    boolean isRecording = false;
    boolean isPlaying = false;
    HashMap<Notes,LinkedList<Long>> noteToSequences = new HashMap<Notes,LinkedList<Long>>();
    HashMap<Notes, Long> timeStamps = new HashMap<Notes, Long>();
    HashMap<Notes, Octave> notesToOctave = new HashMap<Notes, Octave>();

    ArrayList<Octave> octaves = new ArrayList<>();

    public Long recordTime = 0L;
    Long startingTimeStamp = 0L;
    InstrumentPreset instrument;


    public boolean getIsRecording(){
        return isRecording;
    }

    public boolean isPlaying() {return isPlaying;}


    public void clear(){
        stopPlaying();
        stopRecording();
        noteToSequences.clear();
        timeStamps.clear();
        System.out.println("Sequence Channel is cleared!");
    }
    public SequenceChannel() throws MidiUnavailableException, InvalidMidiDataException {
        octaves.add(new Octave());
        octaves.add(new Octave());
    }

    public void loadInstrument(InstrumentPreset instrument) throws MidiUnavailableException, InvalidMidiDataException {
        clear();
        this.instrument = instrument;
        notesToOctave.clear();

        for(Notes note : instrument.lowerOctave.values()){
            noteToSequences.put(note,new LinkedList());
            notesToOctave.put(note,octaves.get(0));
            timeStamps.put(note,0L);
            octaves.get(0).loadInstrument(instrument,instrument.lowerOctave.values());
        }
        for(Notes note : instrument.upperOctave.values()){
            noteToSequences.put(note,new LinkedList());
            notesToOctave.put(note,octaves.get(1));
            timeStamps.put(note,0L);
            octaves.get(1).loadInstrument(instrument,instrument.upperOctave.values());
        }
        System.out.println("Instrument " + instrument.toString() + " is connected to Sequence!");
    }

    //Recording Part

    public void startRecording() throws MidiUnavailableException, InvalidMidiDataException {
        if(instrument == null){
            System.out.println("Please connect an Instrument with this Channel first!");
            return;
        }
        long timeStamp = System.currentTimeMillis();
        startingTimeStamp = timeStamp;

        for(Notes key : timeStamps.keySet()){
            timeStamps.put(key,timeStamp);
        }
        isRecording = true;
        System.out.println("Recording started!");
    }
    public void stopRecording(){
        isRecording = false;
        recordTime = System.currentTimeMillis() - startingTimeStamp;
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
        stopRecording();
        isPlaying = true;
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
            while(isPlaying) {
                for (Notes key : noteToSequences.keySet()) {
                    PlayKeySequence play = new PlayKeySequence(key, notesToOctave.get(key), noteToSequences.get(key));
                    play.start();
                }
                try {
                    Thread.sleep(recordTime);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    public class PlayKeySequence extends Thread{
        Octave octave;
        Notes note;
        LinkedList<Long> sequence;

        public PlayKeySequence(Notes note, Octave octave,LinkedList<Long> sequence){
            this.sequence = sequence;
            this.octave = octave;
            this.note = note;
        }

        @Override
        public void run(){
            for(Long elem : sequence){
                if(elem >= 0){
                    try {
                        Thread.sleep(elem);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }else if(elem == -1L){
                    octave.play(note);
                }else if(elem == -2L){
                    try {octave.stop(note);}
                    catch (InvalidMidiDataException e) {throw new RuntimeException(e);}
                }
                if(!isPlaying)return;
            }
        }
    }
}
