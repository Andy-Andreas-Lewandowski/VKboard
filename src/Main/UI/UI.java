package Main.UI;

import Main.Components.Sequencer.Sequencer;
import Main.Service.RootService;
import Main.UI.Keys.BlackKey;
import Main.UI.Keys.WhiteKey;
import Main.UI.Settings.UiSettings;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.KeyListener;


public class UI {
    final static int APPROX_WIDTH = 1280;
    final static int APPROX_HEIGHT = 800;

    final static int APPROX_BOTTOM_COMPONENT_HEIGHT = (APPROX_WIDTH/14)*4;
    final static int APPROX_TOP_COMPONENT_HEIGHT = (APPROX_HEIGHT - APPROX_BOTTOM_COMPONENT_HEIGHT) - 50;


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
            SequencerController sequencerController = new SequencerController();
            layout.setConstraints(sequencerController,constraints);
            layout.addLayoutComponent(sequencerController,constraints);
            add(sequencerController);

            /*setLayout(new GridLayout(1,3));
            add(new UiSettings());
            add(new SequencerController());*/
        }

    }

    // #############
    // # Sequencer #
    // #############

    public class SequencerController extends JPanel {
        Dimension rowSize = new Dimension(APPROX_WIDTH *8/11,(APPROX_TOP_COMPONENT_HEIGHT-10)/(Sequencer.channels.size()+1));

        public SequencerController() {
            // - APPEARANCE
            setBorder(getGroupTitleBorder("Sequencer"));
            Dimension dim = new Dimension((APPROX_WIDTH*6)/8,APPROX_TOP_COMPONENT_HEIGHT);
            setPreferredSize(dim);
            setMinimumSize(dim);

            // - LAYOUT
            GridBagLayout gbl = new GridBagLayout();
            GridBagConstraints gbc = new GridBagConstraints();
            setLayout(gbl);

            // -- GENERAL CONSTRAINTS
            gbc.gridwidth = 1;
            gbc.gridheight = 4;
            gbc.anchor = GridBagConstraints.FIRST_LINE_START;
            gbc.fill = GridBagConstraints.HORIZONTAL;


            // -- FIRST ROW - GENERAL
            gbc.gridy = 0;
            gbc.gridx = 1;
            CommandPanelForAllSequencers multiSequenceController = new CommandPanelForAllSequencers();
            gbl.addLayoutComponent(multiSequenceController,gbc);
            add(multiSequenceController);
            multiSequenceController.setSizing(rowSize);
        }

        public void setSizing(Dimension size){
            setPreferredSize(size);
            setMinimumSize(size);
        }

    }
        public class CommandPanelForAllSequencers extends JPanel {

            public ChannelSelectController channelSelectController;
            public Dimension channelSelectControllerSize;

            public CommandPanelForAllSequencers(){
                setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
                channelSelectController = new ChannelSelectController();
                add(channelSelectController);
            }

            public void setSizing(Dimension size){
                setPreferredSize(size);
                setMinimumSize(size);
                channelSelectControllerSize = new Dimension((getWidth()/10)*4,50);
                channelSelectController.setSizing(channelSelectControllerSize);
            }

            public class ChannelSelectController extends JPanel {
                ButtonGroup radioButtonGroup = new ButtonGroup();

                public ChannelSelectController() {
                    // - APPEARANCE
                    setBorder(getSubgroupTitleBorder("Select Channel"));
                    // - LAYOUT
                    BoxLayout bl = new BoxLayout(this,BoxLayout.X_AXIS);
                    setLayout(bl);

                    // RADIO BUTTONS
                    for (int i = 0; i < Sequencer.channels.size(); i++) {
                        SelectSequencerRadioButton button = new SelectSequencerRadioButton(i);
                        radioButtonGroup.add(button);
                        button.setBorder(getComponentTitleBorder(String.valueOf(i)));
                        button.setBorderPainted(true);
                        add(button);
                    }

                    // RIGHT - START RECORDING BUTTON
                    //add(new StartRecordingButton());

                }

                public void setSizing(Dimension size){
                    setPreferredSize(size);
                    setMinimumSize(size);
                    setMaximumSize(new Dimension(Integer.MAX_VALUE,Integer.MAX_VALUE));
                }

                public class SelectSequencerRadioButton extends JRadioButton {
                    public int channelId = 0;

                    public SelectSequencerRadioButton(int channelId) {
                        this.channelId = channelId;
                        setName(String.valueOf(channelId));
                        setBorder(getComponentTitleBorder("X"));
                    }

                }


                public class StartRecordingButton extends JButton {
                    public StartRecordingButton() {
                        // APPEARANCE

                        // BEHAVIOR
                        setFocusable(false);
                        setText("Start Recording");
                    }
                }


            }


        }


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
