package features;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.swing.JOptionPane;

/**
 * This Class to read and write to Cmd.
 * @author Ahmed Ghanem.
 */
public class Cmd {

    /**
     * This Class to read and write from Cmd.
     * @author Ahmed Ghanem.
     */
    public class StreamWrapper extends Thread {

        /**
         * This Class responsable of starting Oracle services.
         * @author Ahmed Ghanem.
         */
        public InputStream is = null;
        public String type = null;
        public String message = null;

        /** get cmd message.*/
        public String getMessage() {
            return message;
        }

        StreamWrapper(InputStream is, String type) {
            this.is = is;
            this.type = type;
        }

        /** Run the thread to get cmd message.*/
        public void run() {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                StringBuffer buffer = new StringBuffer();
                String line = null;
                while ((line = br.readLine()) != null) {
                    buffer.append(line);//.append("\n");
                }
                message = buffer.toString();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    public StreamWrapper getStreamWrapper(InputStream is, String type) {
        return new StreamWrapper(is, type);
    }
}
