package Main.Modell;

import Main.Modell.Enums.Notes;
import Main.Modell.InstrumentPresets.InstrumentPreset;
import Main.Modell.Piano.Key;


import javax.sound.midi.*;
import java.util.HashMap;
import java.util.LinkedList;

public class SequenceChannel {
    boolean isRecording = false;
    boolean isPlaying = false;
    HashMap<Notes,LinkedList<Long>> noteToSequences = new HashMap<Notes,LinkedList<Long>>();
    HashMap<Notes, Long> timeStamps = new HashMap<Notes, Long>();
    HashMap<Notes, Key> notesToKey = new HashMap<Notes, Key>();

    Long recordTime = 0L;
    Long startingTimeStamp = 0L;
    InstrumentPreset instrument;


    public boolean getIsRecording(){
        return isRecording;
    }

    public boolean isPlaying() {return isPlaying;}
    //Initializing Elements

    public void clear(){
        stopPlaying();
        stopRecording();
        noteToSequences.clear();
        timeStamps.clear();
        System.out.println("Sequence Channel is cleared!");
    }

    public void loadInstrument(InstrumentPreset instrument) throws MidiUnavailableException, InvalidMidiDataException {
        clear();
        this.instrument = instrument;
        for(Notes note : instrument.getNotes()){
            noteToSequences.put(note,new LinkedList());
            timeStamps.put(note,0L);
            Key key = new Key(note, instrument.getVelocity(), instrument.getBank(), instrument.getInstrument());
            notesToKey.put(note,key);
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

    public void playSequence() throws InterruptedException {
        stopRecording();
        isPlaying = true;
        PlayLoop playLoop = new PlayLoop();
        playLoop.start();
        System.out.println("Sequence started playing!");
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
                    PlayKeySequence play = new PlayKeySequence(notesToKey.get(key), noteToSequences.get(key));
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
        Key key;
        LinkedList<Long> sequence;
        public PlayKeySequence(Key key,LinkedList<Long> sequence){
            this.sequence = sequence;
            this.key = key;
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
                    key.play();
                }else if(elem == -2L){
                    key.stop();
                }
                if(!isPlaying)return;
            }
        }
    }
}
