package Main;
import Main.Modell.Settings;
import Main.Service.RootService;
import Main.UI.UI;

import javax.sound.midi.*;


public class Main {
    static Settings settings;
    public static void main(String[] args) throws MidiUnavailableException, InvalidMidiDataException, InterruptedException {
        settings = Settings.getInstance();
        Settings.SettingsBuilder.buildSettings();


        RootService rootService = new RootService();
        UI ui = new UI(rootService);


    }

}
