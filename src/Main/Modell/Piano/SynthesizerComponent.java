package Main.Modell.Piano;

import Main.Modell.Enums.Notes;
import Main.Modell.Piano.InstrumentPresets.SynthesizerPreset;

import javax.sound.midi.*;
import java.util.*;

public class SynthesizerComponent {
    SynthesizerPreset preset;
    Synthesizer synthesizer;
    Receiver receiver;
    LinkedList<ShortMessage> playMessages = new LinkedList<>();
    LinkedList<ShortMessage> stopMessages = new LinkedList<>();
    HashMap<Notes,ShortMessage> noteToPlayMessage = new HashMap<Notes, ShortMessage>();
    HashMap<Notes,ShortMessage> noteToStopMessage = new HashMap<Notes, ShortMessage>();

    public SynthesizerComponent() throws MidiUnavailableException {
        synthesizer = MidiSystem.getSynthesizer();
        synthesizer.open();
        receiver = synthesizer.getReceiver();
        for(int i = 0; i <= 13; i++){
            ShortMessage play = new ShortMessage();
            ShortMessage stop = new ShortMessage();
            playMessages.add(play);
            stopMessages.add(stop);
        }


    }

    public void loadInstrument(SynthesizerPreset preset, List<Notes> notes) throws InvalidMidiDataException {
        //Initialize
        this.preset = preset;
        //Set messages for preset
        setMessages(preset,ShortMessage.NOTE_ON,playMessages,notes,noteToPlayMessage);
        setMessages(preset,ShortMessage.NOTE_OFF,playMessages,notes,noteToStopMessage);
    }

    private void setMessages(SynthesizerPreset preset, int command, List<ShortMessage> messages,
                              List<Notes> notes, HashMap<Notes,ShortMessage> hashMap) throws InvalidMidiDataException {
        hashMap.clear();
        // Initialize elements to iterate over
        Iterator<ShortMessage>  nxtMsg = messages.iterator();
        Iterator<Notes>         nxtNote = notes.iterator();
        int channelIndex = 0;
        while(channelIndex<synthesizer.getChannels().length && nxtMsg.hasNext() && nxtNote.hasNext()){
            // Skip MidiChannel for drumkit if necessary, otherwise ensure messages be on on DrumChannel
            if(!preset.getIsDrumset() && channelIndex == 8) channelIndex++;
            else if (preset.getIsDrumset()) {channelIndex = 8;}
            // Iterate over objects
            ShortMessage msg =      nxtMsg.next();
            Notes note =            nxtNote.next();
            //Setup message and put entry into Hashmap for quick access later
            msg.setMessage(command,channelIndex,note.getCode(),preset.getVelocity());
            hashMap.put(note,msg);
            // Nxt channel for nxt message
            channelIndex++;
        }
    }

    private void setMidiChannels(SynthesizerPreset preset){
        for(MidiChannel channel : synthesizer.getChannels()){
            channel.programChange(preset.getBank(), preset.getInstrument());
        }
    }


    public boolean play(Notes note){
        ShortMessage msg = noteToPlayMessage.get(note);
        if(msg != null){
            receiver.send(msg,-1);
            return true;
        }
        return false;
    }

    public boolean stop(Notes note) {
        ShortMessage msg = noteToStopMessage.get(note);
        if(msg != null){
            receiver.send(msg,-1);
            return true;
        }
        return false;
    }

}
