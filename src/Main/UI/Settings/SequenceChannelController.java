package Main.UI.Settings;

import Main.Components.Sequencer.Sequencer;
import Main.Components.Sequencer.SequencerChannel;
import Main.UI.UI;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SequenceChannelController extends JPanel {
    private final UI ui;
    Dimension rowSize = new Dimension(UI.APPROX_WIDTH * 8 / 11, (UI.APPROX_TOP_COMPONENT_HEIGHT - 10) / (Sequencer.channels.size() + 1));

    public SequenceChannelController(UI ui) {
        this.ui = ui;
        // - APPEARANCE
        setBorder(UI.getGroupTitleBorder("Channels"));
        Dimension dim = new Dimension((UI.APPROX_WIDTH * 6) / 8, UI.APPROX_TOP_COMPONENT_HEIGHT);
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
        gbc.gridy = 1;
        gbc.gridx = 0;
        ChannelPanel channelPanel = new ChannelPanel(0);
        gbl.addLayoutComponent(channelPanel, gbc);
        add(channelPanel);

        //CommandPanelForAllSequencers multiSequenceController = new CommandPanelForAllSequencers();
        //gbl.addLayoutComponent(multiSequenceController,gbc);
        //add(multiSequenceController);
        //multiSequenceController.setSizing(rowSize);
    }

    public void setSizing(Dimension size) {
        setPreferredSize(size);
        setMinimumSize(size);
    }



    public class ChannelPanel extends JPanel{
        Dimension   size         = new Dimension(UI.APPROX_WIDTH *8/11,(UI.APPROX_TOP_COMPONENT_HEIGHT-10)/(Sequencer.channels.size()));
        ButtonGroup radioButtons = new ButtonGroup();
        int         channelNo    = 0;

        public ChannelPanel(int channelNo){
            this.channelNo = channelNo;

            // - APPEARANCE
            setBorder(UI.getComponentTitleBorder("Channel " + channelNo + ":"));

            // - LAYOUT
            GridBagLayout      gbl  = new GridBagLayout();
            GridBagConstraints gbc  = new GridBagConstraints();
            setLayout(gbl);
            // -- GENERAL CONSTRAINTS
            gbc.insets = new Insets(0,5,0,5);
            gbc.gridwidth  = 3;
            gbc.gridheight = 1;
            gbc.gridx   = GridBagConstraints.RELATIVE;
            gbc.gridy   = 0;
            gbc.weightx = 0;
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // --  PLAY BUTTON
            // --- CONNECT
            PlayChannelButton playButton = new PlayChannelButton(channelNo);
            gbl.addLayoutComponent(playButton,gbc);
            add(playButton);

            // --  STOP BUTTON
            // --- CONNECT
            StopChannelButton stopButton = new StopChannelButton(channelNo);
            gbl.addLayoutComponent(stopButton,gbc);
            add(stopButton);

            // -- LOAD SYNTH
            // --- CONNECT
            LoadSynthButton loadSynthButton = new LoadSynthButton(channelNo);
            gbl.addLayoutComponent(loadSynthButton,gbc);
            add(loadSynthButton);
        }

        public class PlayChannelButton extends JButton implements SequencerChannel.ChannelObserver {
            boolean isPlaying = false;
            int channelNo = 0;

            public PlayChannelButton (int channelNo){
                this.channelNo = channelNo;
                // APPEARANCE
                setFont(UI.TITEL_FONT);
                setText("Play");
                // BEHAVIOR
                setFocusable(false);
                setRolloverEnabled(false);
                Sequencer.channels.get(channelNo).subscribeToChannel(this);

                addActionListener(new PlayChannelAction());
            }

            @Override
            public void onStepChange(int step){}
            @Override
            public void onPlayChange(boolean isPlaying) {
                if(this.isPlaying != isPlaying){
                    this.isPlaying = isPlaying;
                    setEnabled(!isPlaying);
                }
            }
            public class PlayChannelAction implements ActionListener {

                @Override
                public void actionPerformed(ActionEvent e) {
                    Sequencer.getInstance().playSequence(channelNo);
                }
            }
        }

        public class StopChannelButton extends JButton implements SequencerChannel.ChannelObserver{
            boolean isPlaying = false;
            int channelNo = 0;

            public StopChannelButton(int channelNo){
                this.channelNo = channelNo;
                // APPEARANCE
                setFont(UI.TITEL_FONT);
                setText("Stop");
                // BEHAVIOR
                setFocusable(false);
                setRolloverEnabled(false);
                setEnabled(false);

                Sequencer.channels.get(channelNo).subscribeToChannel(this);
                addActionListener(new StopChannelAction());
            }

            @Override
            public void onStepChange(int step) {}

            @Override
            public void onPlayChange(boolean isPlaying) {
                if(this.isPlaying != isPlaying){
                    setEnabled(isPlaying);
                    this.isPlaying = isPlaying;
                }
            }


            public class StopChannelAction implements ActionListener {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Sequencer.getInstance().stopSequencer(channelNo);
                }
            }
        }

        public class LoadSynthButton extends JButton implements Sequencer.SelectedChannelObserver, SequencerChannel.ChannelObserver {
            int channelNo = 0;

            public LoadSynthButton(int channelNo){
                this.channelNo = channelNo;
                // APPEARANCE
                setFont(UI.TITEL_FONT);
                setText("Load Synth");
                // BEHAVIOR
                setFocusable(false);
                setRolloverEnabled(false);

                Sequencer.subscribeToSelectedChannel(this);
                Sequencer.channels.get(channelNo).subscribeToChannel(this);
                addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Sequencer.loadSynthesizerToSequencer(Sequencer.channels.indexOf(Sequencer.selectedChannel));
                    }
                });
            }
            @Override
            public void onSelectedChannelChange(int channelNo) {}
            @Override
            public void onIsRecordingChange() {
                if(Sequencer.isRecording){
                    setEnabled(false);
                }else{
                    setEnabled(true);
                }
            }
            @Override
            public void onStepChange(int step) {}
            @Override
            public void onPlayChange(boolean isPlaying) {
                if(isPlaying){
                    setEnabled(false);
                }else{
                    setEnabled(true);
                }
            }
        }



        public class ChannelSelectButton extends JRadioButton implements Sequencer.SelectedChannelObserver {
            int channelNo = 0;

            public ChannelSelectButton(int channelNo){
                this.channelNo = channelNo;
                addChangeListener(new ChannelSelectAction());
                Sequencer.subscribeToSelectedChannel(this);
            }

            @Override
            public void onSelectedChannelChange(int channelNo) {
                if(this.channelNo == channelNo && !isSelected()){
                    setSelected(true);
                }else if(this.channelNo != channelNo && isSelected()){
                    setSelected(false);
                }
            }

            @Override
            public void onIsRecordingChange() {
            }


            public class ChannelSelectAction implements ChangeListener {
                @Override
                public void stateChanged(ChangeEvent e) {
                    if(isSelected()){
                        Sequencer.loadChannel(channelNo);
                    }
                }
            }

        }




    }

}
