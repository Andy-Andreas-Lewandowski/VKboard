package Main.Service;


import Main.UI.UI;

public class RootService {
    public Piano piano;
    IniService iniService;

    UI ui;

    public RootService(){
        piano = new Piano();
        iniService = new IniService();
        iniService.initializePiano(piano);
        //ui = new UI(this);




    }



}
