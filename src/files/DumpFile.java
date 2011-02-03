/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package files;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import utils.ConstantManager;

/**
 * Generator of dump files
 * @author ahmed
 */
public class DumpFile {

    private JFileChooser sqlFile;
    private File file;
    private String filePath = null;
    private String logPath = null;

    public DumpFile() {
        sqlFile = new JFileChooser();
        int choice = sqlFile.showSaveDialog(sqlFile);
        if (choice == JFileChooser.CANCEL_OPTION) {
        }
        if (choice == JFileChooser.APPROVE_OPTION) {
            file = new File(sqlFile.getSelectedFile().getAbsolutePath() + ConstantManager.ALLOWED_FILE_EXTENTIONS[0]);
            if (file.exists()) {
                int x = JOptionPane.showConfirmDialog(null, "A file named " + sqlFile.getSelectedFile().getName() + ".sql"
                        + " already exists. Do you want to replace it ? ", "Question", JOptionPane.YES_NO_OPTION);
                if (x == JOptionPane.YES_OPTION) {
                    filePath = sqlFile.getSelectedFile() + ConstantManager.ALLOWED_FILE_EXTENTIONS[0];
                    logPath = sqlFile.getSelectedFile() + ConstantManager.ALLOWED_FILE_EXTENTIONS[1];
                }

            } else {
                filePath = sqlFile.getSelectedFile() + ConstantManager.ALLOWED_FILE_EXTENTIONS[0];
                logPath = sqlFile.getSelectedFile() + ConstantManager.ALLOWED_FILE_EXTENTIONS[1];
            }
        }
        
    }
/**
 *
 * @return dump file path
 */
    public String getFilePath() {
        return filePath;
    }
/**
 *
 * @return log dile path
 */
    public String getLogPath() {
        return logPath;
    }
}
