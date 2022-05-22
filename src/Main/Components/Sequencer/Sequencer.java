package Main.Components.Sequencer;

import Main.Components.EnumsAndMaps.Notes;
import Main.Components.Instrument.Pianoroll;
import Main.Components.Instrument.Synthesizer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Sequencer implements Pianoroll.PlayObserver {
    private Sequencer(){}
    private static Sequencer sequencer = new Sequencer();
    public static Sequencer getInstance(){return sequencer;}

    // Beat
    public static final int MINUTE_IN_MILLI = 60000;
    public static final int MAX_BEATS = 360;
    public static final int MAX_BPM = 120;
    public static final int MIN_BPM = 20;
    public static final int STEPS_PER_BEAT = 32;

    static int bpm = 60;
    static int beatsInUse = 5;

    // Sequence Channels
    public static boolean isRecording = false;
    public static boolean allPlaying = false;
    static int     channelId   = 0;
    public static ArrayList<SequencerChannel> channels = new ArrayList<>();
    public static SequencerChannel            selectedChannel;

    // Observer
    static ArrayList<BpmObserver>             bpmObserver = new ArrayList<>();
    static ArrayList<SelectedChannelObserver> selectedChannelObserver = new ArrayList();


    // Setup
    public static void synchronizeSequencer(){}
    public static void loadSynthesizerToSequencer(int channelId){
        if(sequencer.isNotBlocked() && !channels.get(channelId).isPlaying) {
            Synthesizer synth = Pianoroll.cloneSynthesizer();
            channels.get(channelId).loadSynthesizer(synth);

            System.out.println("Synth loaded to channel No." + channelId + "!");
        }
    }
    public static void loadChannel(int id){
        if(isChannelIdValid(id) && !isRecording) {
            channelId = id;
            selectedChannel = channels.get(id);
            notifyOnChannelChange();
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
                channel.setIsPlaying(true);
            }
            new PlaySequence(threads).start();
        }
    }
    public void stopAllSequencer(){
        allPlaying = false;
        for(SequencerChannel channel : channels){
            channel.setIsPlaying(false);
        }
    }

    public void playSequence(int channelId){
        if(isNotBlocked() && !channels.get(channelId).noteToSequences.isEmpty()){
            List threads = new ArrayList<>();
            channels.get(channelId).noteToSequences.keySet().forEach(note -> threads.add(new PlayNote(selectedChannel,note)));
            channels.get(channelId).setIsPlaying(true);
            new PlaySequence(threads).start();


            System.out.println("Channel No." + channelId + " started playing!");
        }
    }

    public void playThisSequence(){
        if(isNotBlocked()){
            playSequence(channelId);
        }
    }

    public void stopSequencer(int sequeceId){
        channels.get(sequeceId).setIsPlaying(false);
    }

    public void stopThisSequence(){
        selectedChannel.setIsPlaying(false);
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








    public static int   getBpm(){return bpm;}
    public static void  setBpm(int bpm){
        if(bpm <= MAX_BPM && bpm >= MIN_BPM && !isRecording){
            Sequencer.bpm = bpm;
            notifyOnBpmChange(bpm);
            System.out.println("Bpm was changed to: " + bpm);
        }
    }









    // - Observer
    // -- BPM Observer
    public static void subscribeToBpm(BpmObserver o){bpmObserver.add(o);}
    public static void notifyOnBpmChange(int id){bpmObserver.forEach(o -> o.onBpmChange(id));}

    public interface BpmObserver {void onBpmChange(int bpm);}
    // -- Channel Observer


    public static void subscribeToSelectedChannel(SelectedChannelObserver o) {selectedChannelObserver.add(o);}
    public static void notifyOnChannelChange()                               {selectedChannelObserver.forEach(o->o.onSelectedChannelChange(channelId));}

    public interface SelectedChannelObserver {void onSelectedChannelChange(int channelNo); void onIsRecordingChange();}

}
