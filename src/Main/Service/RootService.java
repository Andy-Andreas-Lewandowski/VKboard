package Main.Service;


import Main.UI.UI;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

public class RootService {
    public Piano piano;
    IniService iniService;

    UI ui;

    public RootService() throws MidiUnavailableException, InvalidMidiDataException {
        piano = new Piano();
        iniService = new IniService();
        iniService.initializePiano(piano);
        //ui = new UI(this);




    }



}
