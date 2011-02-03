package main;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Set look and feel.
 * @author Ahmed Ghanem.
 */
public class LookAndFeel {

    private String[] lookAndFeel = {"com.jtattoo.plaf.bernstein.BernsteinLookAndFeel", "com.jtattoo.plaf.aero.AeroLookAndFeel", "com.jtattoo.plaf.luna.LunaLookAndFeel", "com.jtattoo.plaf.smart.SmartLookAndFeel", "com.jtattoo.plaf.mcwin.McWinLookAndFeel"};
    private int selectedLookAndFeel = 4;
/***
 *
 * @param lookAndFeel look and feel name.
 * @throws UnsupportedLookAndFeelException if un supported.
 */
    public void LookAndFeel(String lookAndFeel) throws UnsupportedLookAndFeelException {
        try {
            UIManager.setLookAndFeel(lookAndFeel);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
/***
 * look and feel
 */
    public void systemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(LookAndFeel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(LookAndFeel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(LookAndFeel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(LookAndFeel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
/***
 * look and feel
 * @throws UnsupportedLookAndFeelException UnsupportedLookAndFeel
 */
    public void LookAndFeel() throws UnsupportedLookAndFeelException {
        try {
            UIManager.setLookAndFeel(lookAndFeel[selectedLookAndFeel]);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the lookAndFeel
     */
    public String getLookAndFeel() {
        return lookAndFeel[getSelectedLookAndFeel()];

    }

    /**
     * @param lookAndFeel the lookAndFeel to set
     */
    public void setLookAndFeel(String lookAndFeel) {
        this.lookAndFeel[selectedLookAndFeel] = lookAndFeel;
    }

    /**
     * @return the selectedLookAndFeel
     */
    public int getSelectedLookAndFeel() {
        return selectedLookAndFeel;
    }

    /**
     * @param selectedLookAndFeel the selectedLookAndFeel to set
     */
    public void setSelectedLookAndFeel(int selectedLookAndFeel) throws UnsupportedLookAndFeelException {
        this.selectedLookAndFeel = selectedLookAndFeel;
        LookAndFeel();
    }
}
