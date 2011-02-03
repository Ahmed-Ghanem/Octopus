package main;

import java.awt.*;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import utils.ConstantManager;
/**
 * This class for showing splash screen
 * @author Ahmed Ghanem.
 */
public class SplashScreen extends JWindow {
/** Field Description of myIntField */

    private static int count = 1;
/***
 * show splash screen img
 */
    public SplashScreen() {
        ImageIcon splash = new ImageIcon(ConstantManager.OCTOPUS_SPLASH_LOGO);
        JScrollPane jsp = new JScrollPane(new JLabel(splash));
        getContentPane().add(jsp);

       
    }
/***
 *
 * @param time sec to show the splash (3000)
 */
    public void showSplash(int time) {
        ++count;
        setSize(452, 194);
        centerScreen(); // center method
        setVisible(true);
        if (time != 0) { // the time of show splash

            try {
                // the time of show splash
                Thread.sleep(time);
                dispose();
            } catch (InterruptedException ex) { // thread throw exception
                Logger.getLogger(SplashScreen.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
/***
 * new version ....
 * @return
 */
    public boolean getCount() { //call it in main screen to show once time
        if (count % 2 == 0) {
            return true;
        } else {
            return false;
        }
    }
/***
 * center the splash screen
 */
    private void centerScreen() {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize(); // dimension of screen
        int x = (int) ((d.getWidth() - getWidth()) / 2);
        int y = (int) ((d.getHeight() - getHeight()) / 2);
        setLocation(x, y);
    }

    public static void main(String[] args) { // test the class
        SplashScreen s = new SplashScreen();
        s.showSplash(3000);
        s.show();
    }
}


