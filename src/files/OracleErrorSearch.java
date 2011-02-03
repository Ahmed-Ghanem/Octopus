/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package files;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.JProgressBar;

/**
 * oracle error generator
 * @author ahmed
 */
public class OracleErrorSearch {

    private Scanner input;
    private boolean breakFlag;
    private JProgressBar bar;
    private int barValue = 0;

    public OracleErrorSearch(File file) throws FileNotFoundException {
        input = new Scanner(file);
        bar = new JProgressBar();
        breakFlag = true;
    }
/**
 *
 * @param pattern as search word
 * @return result for the search
 */
    public String search(String pattern) {
        String message = "";
        while (input.hasNext()) {

            if (breakFlag == false) {
                break;
            }
            bar.setValue(++barValue);
            String line = input.nextLine();
            if (line.startsWith(pattern)) {
                bar.setValue(47992);
                message += line + "\n";
                while (input.hasNext()) {
                    bar.setValue(47992);
                    String lineX = input.nextLine();
                    if (lineX.startsWith("ORA-")) {
                        breakFlag = false;
                        break;
                    } else {
                        bar.setValue(47992);
                        message += lineX;
                    }
                }
            }

        }
        input.close();
        return message;
    }
/**
 *
 * @param b reference for progress bar
 */
    public void progressRef(JProgressBar b) {
        bar = b;
    }
}
