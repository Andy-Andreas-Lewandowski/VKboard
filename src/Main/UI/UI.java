package Main.UI;

import Main.Service.RootService;
import Main.UI.Keys.BlackKey;
import Main.UI.Keys.WhiteKey;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;


public class UI {
    final static int APPROX_WIDTH = 1200;
    final static int APPROX_HEIGHT = 700;
    // magic number 38 and i do not know why it works
    static int divider = APPROX_HEIGHT /2;
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

            // Build as Tree
            //add(new SettingPanel());
            add(new Keyboardroll());


        }
    }

    class KeyboardrollSettingPane extends JSplitPane{
        public KeyboardrollSettingPane(){
            // Initialize JSplitPane
            setOrientation(JSplitPane.VERTICAL_SPLIT);

            setTopComponent(new SettingPanel());
            setBottomComponent(new Keyboardroll());
            // Set Divider
            setDividerSize(0);

        }


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
            for(int i = 0 ; i < 14 ; i++){
                add(new WhiteKey(UI.APPROX_WIDTH,i),JLayeredPane.DEFAULT_LAYER);
            }
            for(int i = 0 ; i < 10; i++)add(new BlackKey(UI.APPROX_WIDTH,i),JLayeredPane.PALETTE_LAYER);
            setSize(APPROX_WIDTH,APPROX_HEIGHT);
        }

    }


}
