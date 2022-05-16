package Main.Service;

import Main.Modell.Enums.Notes;
import Main.Modell.InstrumentPresets.InstrumentPreset;
import Main.Modell.Piano.Octave;
import Main.Modell.SequenceChannel;

import javax.sound.midi.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Piano {
    // Synth Stuff
    HashMap<Integer, Octave> keyboardCodeToOctave = new HashMap<>();
    HashMap<Integer, Notes>  keyboardCodeToNote   = new HashMap<>();
    ArrayList<Octave> octaves = new ArrayList<>();
    ArrayList<InstrumentPreset> instruments = new ArrayList<>();
    int insSelectIndex = 0;
    InstrumentPreset preset;

    // Sequencer
    public ArrayList<SequenceChannel> sequenceChannels = new ArrayList<>();
    public int seqChannelSelectIndex = 0;
    public SequenceChannel selectedSequenceChannel = new SequenceChannel();

    public boolean areAllSeqPlaying = false;


    // Metronome
    public Metronome metronome = new Metronome();

    public Piano() throws MidiUnavailableException, InvalidMidiDataException {
        octaves.add(new Octave());
        octaves.add(new Octave());
    }


    public void nextSequenceChannel(){
        if(selectedSequenceChannel.getIsRecording()){
            System.out.println("Can't change Channel while it is recording!");
            return;
        }
        selectedSequenceChannel = sequenceChannels.get(seqChannelSelectIndex);
        System.out.println("Sequencer " + seqChannelSelectIndex + " selected.");
        seqChannelSelectIndex = (seqChannelSelectIndex + 1) % sequenceChannels.size();
    }

    public void nextInstrument() throws MidiUnavailableException, InvalidMidiDataException {
        loadInstrument(instruments.get(insSelectIndex));
        System.out.println("Instrument " + instruments.get(insSelectIndex).toString() + " selected.");
        insSelectIndex = (insSelectIndex + 1) % instruments.size();

    }

    public void loadInstrument(InstrumentPreset preset) throws MidiUnavailableException, InvalidMidiDataException {
       keyboardCodeToOctave.clear();
       keyboardCodeToNote.clear();

       this.preset = preset;

       octaves.get(0).loadInstrument(preset,preset.lowerOctave.values());
       octaves.get(1).loadInstrument(preset,preset.upperOctave.values());

       for(Integer keyboardCode : preset.lowerOctave.keySet()){
           keyboardCodeToOctave.put(keyboardCode,octaves.get(0));
           keyboardCodeToNote.put(keyboardCode,preset.lowerOctave.get(keyboardCode));
       }
       for(Integer keyboardCode : preset.upperOctave.keySet()){
           keyboardCodeToOctave.put(keyboardCode,octaves.get(1));
           keyboardCodeToNote.put(keyboardCode,preset.upperOctave.get(keyboardCode));
       }
    }

    public void connectInstrumentToSequencer() throws MidiUnavailableException, InvalidMidiDataException {
        selectedSequenceChannel.loadInstrument(preset);
    }


    public void unloadInstrument(){
        //for(Key key : keyboardCodeToKeys.values()){key.terminate();}
       // keyboardCodeToKeys.clear();
    }

    public void play(int keyboardCode){
        Notes note = keyboardCodeToNote.getOrDefault(keyboardCode,Notes.NONE);
        if(note == Notes.NONE)return;
        keyboardCodeToOctave.get(keyboardCode).play(note);
        if(selectedSequenceChannel.getIsRecording()){
            selectedSequenceChannel.addNote(note,true);
        }
    }

    public void stop(int keyboardCode) throws InvalidMidiDataException {
            Notes note = keyboardCodeToNote.getOrDefault(keyboardCode,Notes.NONE);
            if(note == Notes.NONE)return;
            keyboardCodeToOctave.get(keyboardCode).stop(note);

            if (selectedSequenceChannel.getIsRecording()){
                selectedSequenceChannel.addNote(note,false);
            }
    }

    public void playAllSeq(){
        new playAllSequencer().run();
    }
    public void stopAllSeq(){
        for(SequenceChannel seqChannel : sequenceChannels){
            seqChannel.stopPlaying();
        }
    }

        public class playAllSequencer extends Thread{
            @Override
            public void run(){
                ArrayList<Long> recordTimes = new ArrayList<>();
                ArrayList<SequenceChannel.PlayLoop> threads = new ArrayList<>();
                Long maxRecordTime = 0L;

                // Find maximum recordTime
                for(SequenceChannel channel : sequenceChannels){
                    recordTimes.add(channel.recordTime);
                    if(maxRecordTime<channel.recordTime)maxRecordTime=channel.recordTime;
                }
                // Set recordTime the same everywhere
                for(SequenceChannel channel : sequenceChannels){
                    channel.recordTime=maxRecordTime;
                }

                // Start Threads and
                for(SequenceChannel channel : sequenceChannels){
                    try {
                        threads.add(channel.playSequence());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                areAllSeqPlaying = true;
                // wait for Threads.
                for(SequenceChannel.PlayLoop loop : threads){
                    try {
                        loop.join(0);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                // Set recordTime back
                for (int i = 0 ; i < recordTimes.size();i++){
                    sequenceChannels.get(i).recordTime = recordTimes.get(i);
                }
                areAllSeqPlaying = false;

            }

        }



}
