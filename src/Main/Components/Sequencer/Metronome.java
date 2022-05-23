package Main.Components.Sequencer;




import Main.Components.EnumsAndMaps.Notes;

import javax.sound.midi.*;

public class Metronome{
    private Metronome(){}
    private static Metronome metronome = new Metronome();
    public static Metronome getInstance(){return metronome;}

    static Synthesizer  synthesizer;
    static Receiver     receiver;
    static ShortMessage tickOn;
    static ShortMessage tickOff;



    static boolean isPlaying = false;

    public static boolean getIsPlaying(){return isPlaying;}

    public void startMetronome(){
        if(!isPlaying){
            isPlaying = true;
            new MetronomePlayer().start();
        }
    }

    public void stopMetronome(){
        isPlaying = false;
    }



    public class MetronomePlayer extends Thread{
        @Override
        public void run(){
            while(isPlaying) {
                try {
                    long stepTime = (Sequencer.MINUTE_IN_MILLI / Sequencer.bpm);
                    long currentTime = System.currentTimeMillis();
                    receiver.send(tickOn,-1);
                    Thread.sleep(stepTime>>2);
                    receiver.send(tickOff,-1);
                    long diffrence = (currentTime+stepTime)-System.currentTimeMillis();
                    if (diffrence>0)Thread.sleep(diffrence);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }




    public void init(){
        try {
            synthesizer = MidiSystem.getSynthesizer();
            synthesizer.open();

            tickOn = new ShortMessage();
            tickOff = new ShortMessage();

            MidiChannel[] channels = synthesizer.getChannels();
            channels[0].programChange(128,121);

            tickOn.setMessage(ShortMessage.NOTE_ON,0, Notes.B4.getCode(),40);
            tickOff.setMessage(ShortMessage.NOTE_OFF,0, Notes.B4.getCode(),40);

            receiver = synthesizer.getReceiver();

        } catch (MidiUnavailableException | InvalidMidiDataException e){
            throw new RuntimeException(e);
        }


    }

    // bank = 128;
    // instrument = 121;


}

