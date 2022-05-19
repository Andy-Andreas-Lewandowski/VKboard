package Main.Service;

import Main.Modell.Enums.Notes;
import Main.Modell.Piano.SynthesizerComponent;
import Main.Modell.Settings;

import javax.sound.midi.Synthesizer;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class SynthController implements Settings.SettingsObserver{

        private final Settings settings = Settings.getInstance();
        HashMap<Integer, SynthesizerComponent> keyboardCodesToSynthComp;
        HashMap<Integer, Notes>                 keyboardCodeToNotes;

        RootService root;

        public SynthController(RootService root){
            this.root = root;
            update();
        }

        @Override
        public void update() {
            keyboardCodesToSynthComp = settings.getKeyboardCodeToSynthComp();
            keyboardCodeToNotes = settings.getKeyboardCodeToNote();
        }

        public void play(int keyboardCode){
            Notes note = keyboardCodeToNotes.get(keyboardCode);
            keyboardCodesToSynthComp.get(keyboardCode).play(note);
            notifyOnPlay(note);
        }

        public void stop(int keyboardCode){
            Notes note = keyboardCodeToNotes.get(keyboardCode);
            keyboardCodesToSynthComp.get(keyboardCode).stop(note);
            notifyOnStop(note);
        }

        private ArrayList<SynthesizerInputObserver> observerList = new ArrayList<>();
        public void subscribeToSynthesizerInput(SynthesizerInputObserver observer){
            observerList.add(observer);
        }

        private void notifyOnPlay(Notes note){observerList.forEach(observer -> observer.onPlay(note));}
        private void notifyOnStop(Notes note){observerList.forEach(observer -> observer.onStop(note));}



    public interface SynthesizerInputObserver {
        void onPlay(Notes note);
        void onStop(Notes note);
    }
}






