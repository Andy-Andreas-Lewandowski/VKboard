package Main.UI;

import Main.Components.Sequencer.Sequencer;
import Main.Components.Sequencer.SequencerChannel;
import Main.Service.RootService;
import Main.UI.Keys.BlackKey;
import Main.UI.Keys.WhiteKey;
import Main.UI.Settings.SequenceChannelController;
import Main.UI.Settings.UiSettings;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;


public class UI {
    public final static int APPROX_WIDTH = 1280;
    public final static int APPROX_HEIGHT = 800;

    public final static int APPROX_BOTTOM_COMPONENT_HEIGHT = (APPROX_WIDTH/14)*4;
    public final static int APPROX_TOP_COMPONENT_HEIGHT = (APPROX_HEIGHT - APPROX_BOTTOM_COMPONENT_HEIGHT) - 50;


    // magic number 38 and i do not know why it works
    static int divider = APPROX_HEIGHT /2;

    public static final Font TITEL_FONT = new Font("Verdana",Font.BOLD,12);
    public static final Font SUBGROUP_FONT = new Font("Verdana",Font.BOLD,14);
    public static final Font GROUP_FONT = new Font("Verdana",Font.BOLD,16);
    public static final Border TITEL_BORDER = BorderFactory.createCompoundBorder();
    public static final Border SUBGROUP_BORDER = BorderFactory.createLineBorder(Color.BLACK,1);
    public static final Border GROUP_BORDER = BorderFactory.createEtchedBorder(1);
    static Keyboardroll kb;

    RootService rootService;
    MainFrame frame;
    Keyboardroll keyboardroll;
    KeyboardrollControlPane splitPane;

    public UI(RootService rootService){
        this.rootService = rootService;
        buildUI();
    }

    public JFrame buildUI(){
        frame = new MainFrame(new KeyInputHandler(rootService));
        frame.repaint();

        frame.doLayout();


        return frame;
    }





    class MainFrame extends JFrame{
        Dimension dimension;
        public MainFrame(KeyListener keyListener){
            UI.divider -= getInsets().bottom;
            addKeyListener(keyListener);
            setVisible(true);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


            //setLayout(new BorderLayout());
            // Build as Tree
            add(new KeyboardrollControlPane());
            //add(new Keyboardroll());

            setSize(UI.APPROX_WIDTH,UI.APPROX_HEIGHT);
        }
    }

    class KeyboardrollControlPane extends JPanel{
        public KeyboardrollControlPane(){
            // - LAYOUT
            //setSize(APPROX_WIDTH,APPROX_HEIGHT);
            GridBagLayout gbl      = new GridBagLayout();
            GridBagConstraints gbc = new GridBagConstraints();
            setLayout(gbl);
            gbc.insets = new Insets(0,0,0,0);
            // -- GENERAL CONSTRAINTS
            gbc.gridwidth = 0;
            gbc.gridheight = 1;
            gbc.fill = GridBagConstraints.NONE;
            gbc.anchor = GridBagConstraints.PAGE_END;


            // -- TOP
            // --- SET CONTRAINTS
            gbc.gridx = GridBagConstraints.RELATIVE;
            gbc.gridy = GridBagConstraints.RELATIVE;
            gbc.weighty = 0;
            // --- ADD COMPONENT
            ControlPanel controlPanel = new ControlPanel();
            gbl.addLayoutComponent(controlPanel,gbc);
            gbl.setConstraints(controlPanel,gbc);

            add(controlPanel);

            // -- BOTTOM
            // --- SET CONTRAINTS
            gbc.gridx = GridBagConstraints.RELATIVE;
            gbc.gridy = GridBagConstraints.RELATIVE;
            gbc.weighty = 1;
            gbc.weightx = 0.5F;

            // --- ADD COMPONENT
            Keyboardroll keyboardroll = new Keyboardroll();
            gbl.addLayoutComponent(keyboardroll,gbc);
            gbl.setConstraints(keyboardroll,gbc);

            add(keyboardroll);
        }
    }



/*
    class KeyboardrollControlPane extends JPanel{
        public KeyboardrollControlPane(){
            // - LAYOUT
            setSize(APPROX_WIDTH,APPROX_HEIGHT);

            setLayout(new GridLayout(2,1));
            add(new ControlPanel());
            add(new Keyboardroll());
        }
    }
*/



    // ############
    // # Settings #
    // ############
    class ControlPanel extends JPanel{
        public ControlPanel(){
            // General Layout
            GridBagLayout layout = new GridBagLayout();
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridwidth = 1;
            constraints.gridheight = 0;
            constraints.insets = new Insets(0,0,0,0);
            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.anchor = GridBagConstraints.PAGE_START;



            // -- LEFT COMPONENT
            // --- SET CONSTRAINTS
            constraints.gridy = GridBagConstraints.RELATIVE;
            constraints.gridx = GridBagConstraints.RELATIVE;
            // --- ADD COMPONENT
            UiSettings uiSettings= new UiSettings();
            layout.setConstraints(uiSettings,constraints);
            layout.addLayoutComponent(uiSettings,constraints);
            add(uiSettings);

            //-- RIGHT COMPONENT
            // --- SET CONSTRAINTS
            constraints.gridy = GridBagConstraints.RELATIVE;
            constraints.gridx = GridBagConstraints.RELATIVE;

            // --- ADD COMPONENT
            SequenceChannelController sequenceChannelController = new SequenceChannelController(UI.this);
            layout.setConstraints(sequenceChannelController,constraints);
            layout.addLayoutComponent(sequenceChannelController,constraints);
            add(sequenceChannelController);

            /*setLayout(new GridLayout(1,3));
            add(new UiSettings());
            add(new SequenceChannelController());*/
        }

    }

    // #############
    // # Sequencer #
    // #############








    // ################
    // # Keyboardroll #
    // ################
    class Keyboardroll extends JLayeredPane{

        public Keyboardroll(){

            //cost.setHeight(Spring.height());

            // Layout
            Dimension dim = new Dimension(APPROX_WIDTH,APPROX_HEIGHT/2);
            setSize(dim);
            setMinimumSize(dim);
            setMaximumSize(dim);
            setPreferredSize(dim);

            //System.out.println(this.getAlignmentY());

            setBackground(Color.GRAY);
            for(int i = 0 ; i < 14 ; i++){
                WhiteKey key = new WhiteKey(UI.APPROX_WIDTH,i);
                add(key,JLayeredPane.DEFAULT_LAYER);
            }
            for(int i = 0 ; i < 10; i++)add(new BlackKey(UI.APPROX_WIDTH,i),JLayeredPane.PALETTE_LAYER);
            setSize(APPROX_WIDTH,APPROX_HEIGHT);

        }



        public void test(){
            System.out.println("Height:" + getHeight());
            System.out.println("Width:" + getWidth());
            System.out.println("X:" + getX());
            System.out.println("Y:" + getY());
        }

    }



    public class ComponentLabel extends JLabel{

        public ComponentLabel(String text){
            setFont(TITEL_FONT);
            setText(text);
            setHorizontalAlignment(SwingConstants.CENTER);
        }

    }



    public static Border getComponentTitleBorder(String title){
        return BorderFactory.createTitledBorder(TITEL_BORDER,title, TitledBorder.LEFT,TitledBorder.ABOVE_TOP, TITEL_FONT);
    }
    public static Border getSubgroupTitleBorder(String title){
        return BorderFactory.createTitledBorder(SUBGROUP_BORDER,title, TitledBorder.LEFT,TitledBorder.DEFAULT_POSITION, SUBGROUP_FONT);
    }
    public static Border getGroupTitleBorder(String title){
        return BorderFactory.createTitledBorder(GROUP_BORDER,title, TitledBorder.CENTER,TitledBorder.ABOVE_TOP, GROUP_FONT);
    }

}
