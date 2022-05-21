package Main.Components.Sequencer;

import Main.Components.EnumsAndMaps.Notes;
import Main.Components.Instrument.Pianoroll;
import Main.Components.Instrument.Synthesizer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Sequencer implements Pianoroll.PianorollObserver {
    private Sequencer(){}
    private static Sequencer sequencer = new Sequencer();
    public static Sequencer getInstance(){return sequencer;}

    // Beat
    public static final int MINUTE_IN_MILLI = 60000;
    public static final int MAX_BEATS = 360;
    public static final int STEPS_PER_BEAT = 32;

    static int bpm = 60;
    static int beatsInUse = 5;

    // Sequence Channels
    static boolean isRecording = false;
    static boolean allPlaying = false;
    static int     channelId   = 0;
    static ArrayList<SequencerChannel> channels = new ArrayList<>();
    static SequencerChannel            selectedChannel;




    // Setup
    public static void synchronizeSequencer(){}
    public static void loadSynthesizerToSequencer(){
        if(sequencer.isNotBlocked()) {
            Synthesizer synth = Pianoroll.cloneSynthesizer();
            selectedChannel.loadSynthesizer(synth);

            System.out.println("Synth loaded to channel!");
        }
    }
    public static void loadChannel(int id){
        if(isChannelIdValid(id) && !isRecording) {
            channelId = id;
            selectedChannel = channels.get(id);
        }
    }
    public static void loadNextSequencer(){
        loadChannel((channelId+1) % channels.size());
    }
    public static void clearSelectedChannel(){
        if(sequencer.isNotBlocked()){
            selectedChannel.clearSequences();
        }
    }

    private static boolean isChannelIdValid(int id){
        return id < channels.size();
    }

    // ###########
    // # Playing #
    // ###########


    public void playAllSequencer(){
        if(sequencer.isNotBlocked()){
            List threads = new ArrayList<>();
            allPlaying = true;
            for (SequencerChannel channel : channels) {
                channel.noteToSequences.keySet().forEach(note -> threads.add(new PlayNote(channel, note)));
                channel.isPlaying = true;
            }
            new PlaySequence(threads).start();
        }
    }
    public void stopAllSequencer(){
        allPlaying = false;
        for(SequencerChannel channel : channels){
            channel.isPlaying = false;
        }
    }



    public void playThisSequence(){
        if(isNotBlocked()){
            List threads = new ArrayList<>();
            selectedChannel.noteToSequences.keySet().forEach(note -> threads.add(new PlayNote(selectedChannel,note)));
            selectedChannel.isPlaying = true;
            new PlaySequence(threads).start();


            System.out.println("Started Playing!");
        }
    }
    public void stopThisSequence(){
        selectedChannel.isPlaying = false;
        System.out.println("Playing stopped!");
    }

    public class PlaySequence extends Thread{
        ExecutorService threadExecutor;
        List<PlayNote> threads;

        public PlaySequence(List<PlayNote> threads){
            threadExecutor = Executors.newFixedThreadPool(threads.size());
            this.threads = threads;
        }

        @Override
        public void run(){
            while(selectedChannel.isPlaying || allPlaying){
                long stepTime = (MINUTE_IN_MILLI / bpm) / STEPS_PER_BEAT;
                long startTime = System.currentTimeMillis();
                try {
                    threads.forEach(thread -> threadExecutor.execute(thread));
                    threadExecutor.awaitTermination(stepTime-2, TimeUnit.MILLISECONDS);
                    long timeLeft = ((startTime + stepTime) - 2L ) - System.currentTimeMillis();
                    if(timeLeft>0) Thread.sleep(timeLeft);
                }
                catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                selectedChannel.incrementStep();
            }
            threadExecutor.shutdown();
        }

    }

    public class PlayNote extends Thread{
        Notes note;
        Synthesizer synth;
        Byte[] sequence;

        public PlayNote(SequencerChannel sequencerChannel, Notes note){
            this.note = note;
            this.synth = sequencerChannel.synthesizer;
            this.sequence = sequencerChannel.noteToSequences.get(note);
        }

        @Override
        public void run(){
            byte command = sequence[selectedChannel.currentStep];
            if(command == -1){
                synth.playNote(note);
            }
            else if (command == -2) {
                synth.stopNote(note);
            }
        }
    }




    // #############
    // # Recording #
    // #############
    public void startRecording(){
        if(isNotBlocked()) {
            isRecording = true;
            new RecorderThread().start();

            System.out.println("Recording started!");
        }
    }

    public void stopRecording(){
        isRecording = false;
        System.out.println("Recording stopped!");
    }

    public boolean isNotBlocked(){
        return !isRecording && !selectedChannel.isPlaying && !allPlaying;
    }

    @Override
    public void onPlay(Notes note, int key) {
        if(isRecording) {
            selectedChannel.addNoteActivation(note);
        }
    }
    @Override
    public void onStop(Notes note, int key) {
        if(isRecording) {
            selectedChannel.addNoteDeactivation(note);
        }
    }

    public class RecorderThread extends Thread{
        @Override
        public void run(){
            while(isRecording) {
                long stepTime = (MINUTE_IN_MILLI / bpm) / STEPS_PER_BEAT;
                try {
                    Thread.sleep(stepTime);
                    Sequencer.selectedChannel.incrementStep();
                } catch (InterruptedException e) {
                    isRecording = false;
                    throw new RuntimeException(e);
                }
            }
        }
    }



    public static void init(){
        for(int i = 0; i<4; i++) channels.add(new SequencerChannel());
        loadChannel(0);
    }


}
