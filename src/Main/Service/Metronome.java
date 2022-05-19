package Main.Service;

import Main.Modell.Settings;

import javax.sound.midi.*;

public class Metronome implements Settings.SettingsObserver {
    int bpm = 80;
    boolean isPlaying = false;
    Synthesizer metronome;
    Receiver receiver;
    ShortMessage tickOn;
    ShortMessage tickOff;


    public Metronome(RootService root) throws MidiUnavailableException, InvalidMidiDataException {
        metronome = MidiSystem.getSynthesizer();
        metronome.open();
        MidiChannel[] channels = metronome.getChannels();
        channels[8].programChange(0,115);
        channels[8].setMono(true);
        receiver = metronome.getReceiver();

        tickOn = new ShortMessage();
        tickOn.setMessage(ShortMessage.NOTE_ON,8,31,20);

        tickOff = new ShortMessage();
        tickOff.setMessage(ShortMessage.NOTE_OFF,8,31,20);

    }

    public void playIntro() throws InterruptedException {
        int sleepTime = 60000/bpm;
        for(int i = 0; i < 4; i++){
            receiver.send(tickOn,-1);
            receiver.send(tickOff,-1);
            Thread.sleep(sleepTime);
        }
    }

    public void start(){
        isPlaying = true;
        new PlayTempo().start();
    }

    public void stop(){
        isPlaying = false;
    }

    public boolean getIsPlaying(){
        return isPlaying;
    }

    @Override
    public void update() {

    }


    public class PlayTempo extends Thread{
        @Override
        public void run(){
            int sleepTime = 60000/bpm;
            while (isPlaying){
                //Sound
                    receiver.send(tickOn, -1);
                    receiver.send(tickOff,-1);
                //Wait
                try {Thread.sleep(sleepTime);}
                catch (InterruptedException e) {throw new RuntimeException(e);}
            }

        }

    }

}
