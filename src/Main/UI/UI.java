package Main.UI;

import Main.Components.EnumsAndMaps.KeyToInputCode;
import Main.Components.EnumsAndMaps.Notes;
import Main.Components.Instrument.Pianoroll.PianorollObserver;
import Main.Service.RootService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;


public class UI {
    final static int APPROX_WIDTH = 980;
    final static int APPROX_HEIGHT = 500;
    // magic number 38 and i do not know why it works
    static int divider = APPROX_HEIGHT - (Key.WHITE_KEY_HEIGHT + 0);
    //final static int DIVIDER = (HEIGHT - Key.WHITE_KEY_HEIGHT)-2;

    RootService rootService;
    MainFrame frame;
    Keyboardroll keyboardroll;
    KeyboardrollSettingPane splitPane;

    public UI(RootService rootService){
        this.rootService = rootService;
        buildUI();
    }

    public JFrame buildUI(){
        frame = new MainFrame(new KeyInputHandler(rootService));
        frame.repaint();

        keyboardroll = new Keyboardroll();

        // Add for layout works
        SettingPanel settingPanel = new SettingPanel();



        return frame;
    }





    class MainFrame extends JFrame{
        Dimension dimension;
        public MainFrame(KeyListener keyListener){
            UI.divider -= getInsets().bottom;
            addKeyListener(keyListener);
            setVisible(true);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            setSize(UI.APPROX_WIDTH,UI.APPROX_HEIGHT);

            //setResizable(false);



            // Layout
            setLayout(new BorderLayout());

            // Build as Tree
            add(new KeyboardrollSettingPane());
        }
    }

    class KeyboardrollSettingPane extends JSplitPane{
        Rectangle size;
        Dimension sizeDim;
        public KeyboardrollSettingPane(){
            // Initialize JSplitPane
            setOrientation(JSplitPane.VERTICAL_SPLIT);



            //setD

            //build as tree
            setTopComponent(new SettingPanel());
            setBottomComponent(new Keyboardroll());
            this.setAlignmentY(0);
            // Set Divider
            //setDividerLocation(getTopComponent().getHeight());
            setDividerSize(0);

/*            int width = Math.max(getTopComponent().getWidth(),getBottomComponent().getWidth());
            int height = getTopComponent().getHeight() + getBottomComponent().getHeight();
            setSize(width,height);*/

        }

/*        @Override
        public int getDividerLocation(){
            return  UI.divider;
        }
        @Override
        public int getLastDividerLocation(){
            return UI.divider;
        }*/

    }

    // ############
    // # Settings #
    // ############
    class SettingPanel extends JPanel{
        Rectangle size;
        Dimension sizeDim;
        public SettingPanel(){

            setLayout(new FlowLayout());
            setBackground(new Color(20,20,20));
        }

    }

    // ################
    // # Keyboardroll #
    // ################
    class Keyboardroll extends JLayeredPane{


        public Keyboardroll(){


            // Layout

            //System.out.println(this.getAlignmentY());

            setBackground(Color.GRAY);
            for(int i = 0 ; i < 14 ; i++)add(new WhiteKey(i),JLayeredPane.DEFAULT_LAYER);
            for(int i = 0 ; i < 10; i++)add(new BlackKey(i),JLayeredPane.PALETTE_LAYER);


            // Size
            int height = Key.WHITE_KEY_HEIGHT;
            int width = Key.WHITE_KEY_WIDTH*14;
            setSize(width,height);
        }

    }
    abstract class Key extends JButton implements PianorollObserver {
        Notes note = Notes.CS1;
        static final int WHITE_KEY_WIDTH = (UI.APPROX_WIDTH -14) / 14;
        static final int WHITE_KEY_HEIGHT = (int) (WHITE_KEY_WIDTH * 4.0);
        final Dimension WHITE_KEY_DIM = new Dimension(WHITE_KEY_WIDTH,WHITE_KEY_HEIGHT);

        static final int BLACK_KEY_WIDTH = (int)(WHITE_KEY_WIDTH/2.5) * 2;
        static final int BLACK_KEY_HEIGHT = (int) (BLACK_KEY_WIDTH * 3.0);
        final Dimension BLACK_KEY_DIM = new Dimension(BLACK_KEY_WIDTH,BLACK_KEY_HEIGHT);

        @Override
        public void onPlay(Notes note) {

        }
        @Override
        public void onStop(Notes note){

        }
    }

    class WhiteKey extends Key{
        KeyToInputCode key;
        public WhiteKey(int keyNumber){
            // Color
            setBackground(Color.WHITE);

            // Frame Allignment and Dimensions
            setMaximumSize(WHITE_KEY_DIM);
            setMinimumSize(WHITE_KEY_DIM);
            setBounds(keyNumber* WHITE_KEY_WIDTH,0, WHITE_KEY_WIDTH, WHITE_KEY_HEIGHT);
            setPreferredSize(WHITE_KEY_DIM);

            // Text
            this.setText(note.toString());
            this.setVerticalAlignment(SwingConstants.BOTTOM);
            this.setFont(new Font("Consolas",Font.PLAIN,11));
        }
        //@Override
        //public Dimension get
    }


    class BlackKey extends Key{

        KeyToInputCode key;

        public BlackKey(int keyNumber){
            // Color
            setBackground(Color.BLACK);
            this.setForeground(Color.WHITE);

            setMaximumSize(BLACK_KEY_DIM);
            setMinimumSize(BLACK_KEY_DIM);
            setBounds(getKeyPosition(keyNumber),0, BLACK_KEY_WIDTH, BLACK_KEY_HEIGHT);
            setPreferredSize(BLACK_KEY_DIM);

            // Text
            this.setText(note.toString());
            this.setVerticalAlignment(SwingConstants.BOTTOM);
            this.setFont(new Font("Consolas",Font.PLAIN,8));

        }

        int getKeyPosition(int keyNumber){
            int[] whiteKeysBeforeBlackKey = {1,2,4,5,6,8,9,11,12,13};

            return WHITE_KEY_WIDTH * whiteKeysBeforeBlackKey[keyNumber] - (BLACK_KEY_WIDTH/2);
        }

        //@Override
        //public Dimension get
    }

}
