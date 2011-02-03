package features;

import gui.UserLogin;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class NewConnection extends Thread {

    public void run() {
        UserLogin u;
        try {
            u = new UserLogin();
            u.show();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Cmd.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
