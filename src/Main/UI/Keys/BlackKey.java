package Main.UI.Keys;

import Main.Components.EnumsAndMaps.KeyboardMapping;
import Main.Components.EnumsAndMaps.Notes;
import Main.Components.Instrument.Pianoroll;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BlackKey extends JButton implements Pianoroll.PlayObserver, ActionListener {
    public   static Color       keyColor                = Color.BLACK;
    public   static int         bkWidth = 0;
    public   static int         bkHeight = 0;
    public   static int[]       idOnRoll                = {1, 3, 6, 8, 10, 13, 15, 18, 20, 22};
    public   static int[]       whiteBeforeBlackKeys    = {1,2,4,5,6,8,9,11,12,13};


    public int keyNumber = 0;
    public int keyMapping = 0;
    public Notes note = Notes.NONE;
    boolean actionOnListeningPianoroll = false;
    public boolean wasKeyboardClicked = false;
    public boolean wasMouseClicked = false;

    public BlackKey(int refrenceWidth, int keyNumber){
        this.keyNumber = keyNumber;
        calculateSize(refrenceWidth);
        init(refrenceWidth, keyNumber);
    }

    public void calculateSize(int refrenceWidth){
       bkWidth  = (int)(((refrenceWidth / 3) * 2) / 14);
       bkHeight = bkWidth * 3;
    }

    public void init(int refrenceWidth, int keyNumber){

        // - Data
        setKeyMapping();
        setNotes();
        subscribe();

        // - Appearance

        // -- Color & Fonts
        setVerticalTextPosition(SwingConstants.BOTTOM);
        setBackground(keyColor);
        setForeground(Color.WHITE);
        this.setFont(new Font("Impact", Font.PLAIN, 12));
        setText(note.toString());

        // -- Size
        Dimension size = new Dimension(bkWidth,bkHeight);
        setSize(size);
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);

        // -- Position
        int xPos = (int)((WhiteKey.wkWidth * whiteBeforeBlackKeys[keyNumber]) - (size.width /2));
        setBounds(xPos,0,size.width,size.height);

        // - Behavior
        setFocusable(false);
        setRolloverEnabled(false);
        addChangeListener(new OnBlackKey());



    }

    public void setActionListeners(){
        addActionListener(this);
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

    @Override
    public void actionPerformed(ActionEvent e) {
        Pianoroll.playNote(keyMapping);
    }

    public class OnBlackKey implements ChangeListener {
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



