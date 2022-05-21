package Main.UI.Keys;

import Main.Components.EnumsAndMaps.KeyboardMapping;
import Main.Components.EnumsAndMaps.Notes;
import Main.Components.Instrument.Pianoroll;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class WhiteKey extends JButton implements Pianoroll.PlayObserver {
    public static  Color       keyColor    = Color.WHITE;
    public static  int         wkWidth  = 0;
    public static  int         wkHeight = 0;
    public static  int[]       idOnRoll    = {0, 2, 4, 5, 7, 9, 11, 12, 14, 16, 17, 19, 21, 23};

    public int keyNumber = 0;
    public int keyMapping = 0;
    public Notes note = Notes.NONE;
    public boolean wasKeyboardClicked = false;
    public boolean wasMouseClicked = false;

    public WhiteKey(int refrenceWidth, int keyNumber){
        this.keyNumber = keyNumber;
        calculateSize(refrenceWidth);
        init(refrenceWidth, keyNumber);

    }

    public void calculateSize(int refrenceWidth){
        wkWidth  = (int) (refrenceWidth/14);
        wkHeight = wkWidth * 4;
    }

    public void init(int refrenceWidth, int keyNumber){


        // - Data
        setKeyMapping();
        setNotes();
        subscribe();

        // - Appearance

        // -- Color & Fonts & Text
        setVerticalTextPosition(SwingConstants.BOTTOM);
        setBackground(keyColor);
        this.setFont(new Font("Impact", Font.PLAIN, 12));
        setText(note.toString());

        // -- Size
        Dimension size = new Dimension((wkWidth),(wkHeight));
        setSize(size);
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);

        // -- Position
        setBounds((int)(keyNumber*wkWidth),0,size.width,size.height);

        // - Behavior
        setFocusable(false);
        setRolloverEnabled(false);
        addChangeListener(new OnWhiteKey());

    }

    public void setKeyMapping(){
        keyMapping = KeyboardMapping.getAllPianorollInputCodes().get(idOnRoll[keyNumber]);
    }
    public void setNotes(){
        note = Pianoroll.getNote(keyMapping);
    }
    public void subscribe(){
        Pianoroll.subscribeToPlay(this);
    }


    @Override
    public void onPlay(Notes note, int key) {
        if(key == keyMapping && !wasMouseClicked){
            wasKeyboardClicked = true;
            model.setPressed(true);
            model.setArmed(true);
        }
    }

    @Override
    public void onStop(Notes note, int key){
        if(key == keyMapping && !wasMouseClicked){
            model.setPressed(false);
            model.setArmed(false);

            wasKeyboardClicked = false;
        }
    }






public class OnWhiteKey implements ChangeListener {
    @Override
    public void stateChanged(ChangeEvent e) {
        if(!wasKeyboardClicked) {
            if(model.isPressed()) {
                wasMouseClicked =true;
                model.setArmed(true);
                Pianoroll.playNote(keyMapping);
            }else if(!model.isPressed()){
                Pianoroll.stopNote(keyMapping);
                model.setArmed(false);
                wasMouseClicked = false;
            }
        }
    }
}

}