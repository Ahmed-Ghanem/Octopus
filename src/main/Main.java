package main;

import database.DatabaseConnection;
import gui.UserLogin;
import javax.swing.UnsupportedLookAndFeelException;
import utils.ConstantManager;
/**
 * The Main Class for app
 * @author Ahmed Ghanem.
 */
public class Main {

    /***
     *
     * @param args command line args
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws UnsupportedLookAndFeelException
     */
    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
      
        SplashScreen splash = new SplashScreen();
        splash.showSplash(ConstantManager.OCTOPUS_SPLASH_TIME);
        DatabaseConnection check = new DatabaseConnection();
        UserLogin startQuestion = new UserLogin();
        startQuestion.show();
       


    }
}
