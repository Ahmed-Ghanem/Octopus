package features;

import java.awt.Toolkit;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.PopupMenuListener;
import javax.swing.text.StyleContext;
import utils.ConstantManager;

/**
 * Show popup menus in sql editor
 * @author Ahmed Ghanem.
 */
public class Autocomplete {

    private JPopupMenu autoMenu;
    private JMenuItem[] autoItems;
    private JTextArea textPane;
    public static int items = 0; // items in auto menus
    private String totalText;
    private Vector history;

    /***
     * show SQL commands [Generated]
     */
    public Autocomplete() {
        history = new Vector();
    }
/**
 *
 * @param pane sql text used as reference
 */
    public void show(JTextArea pane) {
        textPane = pane;
    }
/**
 *
 * @param s used as append method for JTextPane
 * @see JTextPane
 */
    public void append(String s) { // better implementation--uses
        // StyleContext
        StyleContext sc = StyleContext.getDefaultStyleContext();
        int len = textPane.getDocument().getLength(); // same value as
        // getText().length();
        textPane.setCaretPosition(len); // place caret at the end (with no selection)
        textPane.replaceSelection(s); // there is no selection, so inserts at caret
    }
/**
 *
 * @param pattern search pattern
 * @return menu with matching list words
 */
    public JPopupMenu getMenu(String pattern) {

        autoMenu = new JPopupMenu("SQL");
        autoItems = new JMenuItem[ConstantManager.SQLRESERVEDWORDS.length];
        for (int i = 0; i < ConstantManager.SQLRESERVEDWORDS.length; i++) {
            autoItems[i] = new JMenuItem(ConstantManager.SQLRESERVEDWORDS[i]);
            if (ConstantManager.SQLRESERVEDWORDS[i].startsWith(pattern)) {
                autoMenu.add(autoItems[i]);
                ++items;
                autoItems[i].addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        totalText = textPane.getText();
                        int length = totalText.length();
                        if (totalText.indexOf(" ") == -1) {
                            textPane.setText("" + e.getActionCommand());
                        } else {
                            int lastSpace = totalText.lastIndexOf(" ");
                            totalText = totalText.substring(0, lastSpace);
                            textPane.setText(totalText);
                            textPane.append(" " + e.getActionCommand());
                        }
                    }
                });
            }

        }
        return autoMenu;
    }
}
