package Main.Service;

import Main.Modell.Enums.Notes;
import Main.Modell.Settings;

public class SequencerController implements Settings.SettingsObserver, SynthController.SynthesizerInputObserver {
    RootService root;

    public SequencerController(RootService root){
        this.root = root;
        update();
    }












    @Override
    public void update() {

    }

    @Override
    public void onPlay(Notes note) {

    }

    @Override
    public void onStop(Notes note) {

    }
}
