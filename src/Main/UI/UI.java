package Main.UI;

import Main.Service.RootService;

import javax.swing.*;

public class UI {
    RootService rootService;
    JFrame frame;
    public UI(RootService rootService){
        this.rootService = rootService;
        buildUI();
    }

    public JFrame buildUI(){
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(100,100);
        frame.setVisible(true);
        frame.addKeyListener(new KeyInputHandler(rootService));

        return frame;

    }

}
