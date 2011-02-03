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
 * Get dump file
 * @author ahmed
 */
public class DumpOpenFile {

    private JFileChooser sqlFile;
    private File file;
    private String extension;

    public DumpOpenFile() {
        sqlFile = new JFileChooser();

        int choice = sqlFile.showOpenDialog(sqlFile);
        if (choice == JFileChooser.APPROVE_OPTION) {
            file = sqlFile.getSelectedFile();
            int dotPos = file.getAbsolutePath().lastIndexOf(".");
            extension = file.getAbsolutePath().substring(dotPos);
        }

    }

    public File getOpenFile() {
        return file;
    }

    public boolean isDmp() {
        return extension.equals(ConstantManager.ALLOWED_FILE_EXTENTIONS[0]);
    }
/**
 * check if .dmp or not
 */
    public void checkExtension() {
        if (isDmp() == false) {
            JOptionPane.showMessageDialog(null, "Not Supported File : *.dmp only", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
