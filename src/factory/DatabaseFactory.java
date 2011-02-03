package factory;

import gui.UserLogin;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import utils.ConstantManager;

/**
 * This Class get information about oracle using it's meta data
 * @author Ahmed Ghanem.
 */
public class DatabaseFactory {

    private DatabaseMetaData metaData;
    private UserLogin requestion;
    private boolean checkConnection = false; // login with error user name or password
    private UserLogin u;

    /***
     * connect
     * @throws ClassNotFoundException error in class
     */
    public DatabaseFactory() throws ClassNotFoundException {
        Class.forName(ConstantManager.ORACLE_DRIVER);
        System.out.println(UserLogin.getDbUrl());
        System.out.println(UserLogin.getUserName());
        System.out.println(UserLogin.getPassWord());
        Connection connection;
        try {
            connection = DriverManager.getConnection(UserLogin.getDbUrl(), UserLogin.getUserName(), UserLogin.getPassWord());
            metaData = connection.getMetaData();
            checkConnection = true;
        } catch (SQLException ex) {

            checkConnection = false;
            JOptionPane.showMessageDialog(null, "User name or Password is Wrong", "Error in Connection",
                    JOptionPane.ERROR_MESSAGE);
            u = new UserLogin();
            u.show();



        }

    }

    /***
     * set check
     * @param check is there a connection with database
     */
    public void setCheckConnection(boolean check) {
        this.checkConnection = check;
    }

    /***
     *
     * @return value of check connection
     */
    public boolean getCheckConnection() {
        return checkConnection;
    }

    /***
     *
     * @return oracle version
     * @throws SQLException error in SQL
     */
    public String getProductVersion() throws SQLException {
        return metaData.getDatabaseProductVersion();
    }

    /***
     *
     * @return info about oracle driver
     * @throws SQLException error in SQL
     */
    public String getOracleDriverName() throws SQLException {
        return metaData.getDriverName();
    }

    /***
     *
     * @return oracle version
     * @throws SQLException error in SQL
     */
    public String getOracleDriverVersion() throws SQLException {
        return "" + metaData.getDriverVersion();
    }

    /***
     *
     * @return database URL
     * @throws SQLException error in SQL
     */
    public String getConnectionUrl() throws SQLException {
        return "" + metaData.getURL();
    }
}
