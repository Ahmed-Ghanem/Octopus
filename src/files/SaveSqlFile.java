/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.WriteAbortedException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import utils.ConstantManager;

/**
 * Saving SQL file
 * @author Ahmed Ghanem
 */
public class SaveSqlFile {

    private JFileChooser sqlFile;
    private File file;
/**
 *
 * @param sqlData get the text from text area
 * @throws FileNotFoundException 
 */
    public SaveSqlFile(String sqlData) throws FileNotFoundException {
        sqlFile = new JFileChooser();
        int choice = sqlFile.showSaveDialog(sqlFile);
        if (choice == JFileChooser.CANCEL_OPTION) {
        }
        if (choice == JFileChooser.APPROVE_OPTION) {
            file = new File(sqlFile.getSelectedFile().getAbsolutePath() + ConstantManager.ALLOWED_FILE_EXTENTIONS[2]);
            if (file.exists()) {
                int x = JOptionPane.showConfirmDialog(null, "A file named " + sqlFile.getSelectedFile().getName() + ".sql"
                        + " already exists. Do you want to replace it ? ", "Question", JOptionPane.YES_NO_OPTION);
                if (x == JOptionPane.YES_OPTION) {
                    PrintWriter write = new PrintWriter(sqlFile.getSelectedFile() + ConstantManager.ALLOWED_FILE_EXTENTIONS[2]);
                    write.println(sqlData);
                    write.close();
                }

            } else {
                PrintWriter write = new PrintWriter(sqlFile.getSelectedFile() + ConstantManager.ALLOWED_FILE_EXTENTIONS[2]);
                write.println(sqlData);
                write.close();
            }
        }


    }
}
