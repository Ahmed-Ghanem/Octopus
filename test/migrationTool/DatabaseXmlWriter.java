/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package migrationTool;

import database.DatabaseConnection;
import gui.UserLogin;
import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;

/**
 *
 * @author Ahmed
 */

public class DatabaseXmlWriter {
    private Connection m_connection;
    private Document m_document = null;
    private String m_userName;
    private String m_password;
    private Node m_currentTableNode; //Pointer for the last table tag in the document
    private Node m_schemaNode;
    /**
     * Constructor that will initialized the object
     * @param connection current connection that the class will query from
     * @param _userName the user name that will be written in the schema tag
     * @param _password the password that will be written in the schema tag
     */
    public DatabaseXmlWriter(Connection connection, String _userName, String _password) {
        m_connection = connection;
        m_userName = _userName;
        m_password = _password;
       
    }
    /**
     * Do the writing process(Main Method)
     */
    public void doWrite() {
        startDocument();
        writeSchemaElement();

        try {
            Statement statement_1 = m_connection.createStatement();
            Statement statement_2 = m_connection.createStatement();
            ResultSet tableResultSet = statement_1.executeQuery("select TABLE_NAME from USER_TABLES");
            ResultSet columnResultSet = null;
            while(tableResultSet.next()) {
                writeTableElement(tableResultSet.getString(1));
                columnResultSet = statement_2.executeQuery("select * from " + tableResultSet.getString(1));
                ResultSetMetaData columnResultSetMetadata = columnResultSet.getMetaData();
                int columnCount = columnResultSetMetadata.getColumnCount();
                for(int i = 1; i <= columnCount; i++)
                    writeColumn(columnResultSetMetadata.getColumnName(i), columnResultSetMetadata.getColumnTypeName(i),
                            columnResultSetMetadata.getPrecision(i));
                writeRelation(tableResultSet.getString(1));
            }
        }catch (SQLException ex) {
           Logger.getLogger(DatabaseXmlWriter.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
        }
        
        transformDocument();
    }
    /**
     *
     * @return the document that contains the XML
     * will return null if not initialized by startDocument method
     */
    public Document getDocument() {
        return m_document;
    }
    /**
     * Transform document into either a file or an output stream
     * NOTE: Change that to change the output format
     */
    private void transformDocument() {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(m_document);
            StreamResult result = new StreamResult(new File("ahmed.xml"));
            transformer.transform(source, result);
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(DatabaseXmlWriter.class.getName()).log(Level.SEVERE, null, ex);
        }catch (TransformerException ex) {
            Logger.getLogger(DatabaseXmlWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /*
     * Initialize the document
     */
    private void startDocument() {
        DocumentBuilderFactory documentBuilderFactory =
                DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setValidating(true);
        try {
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                m_document = documentBuilder.newDocument();
        } catch (ParserConfigurationException ex) {
                Logger.getLogger(DatabaseXmlWriter.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println(ex.getMessage());
                
        }
    }
     /*
     * Write schema Element with this format
     * <schema_name name="username" password="password"/>
     */
    private void writeSchemaElement() {
        Element schemaElement = m_document.createElement(ConstantManager.SCHEMA_ELEMENT);
        //TODO: Make it take the current connected schema 
        schemaElement.setAttribute("username", m_userName);
        schemaElement.setAttribute("password", m_password);
        m_schemaNode = m_document.appendChild(schemaElement);
    }
    /*
     * Write table element with this format
     * <table_name name="Employees"/>
     */
    private void writeTableElement(String tableName) {
        Element tableElement = m_document.createElement(ConstantManager.TABLE_ELEMENT);
        tableElement.setAttribute("name", tableName);
        m_currentTableNode = m_schemaNode.appendChild(tableElement);
        
    }
    /*
     * Write column element with this format
     * <column column_name="name" column_type="varchar2" column_size="20"/>
     * 
     */
    private void writeColumn(String columnName, String columnType, int columnSize) {
        Element columnElement = m_document.createElement(ConstantManager.COLUMN_ELEMENT);
        columnElement.setAttribute("column_name", columnName);
        columnElement.setAttribute("column_type", columnType);
        if( columnSize != 0) {
            /*If the column_type is something has no size such as Date then
             * dont write the column_size attribute.
             */
            columnElement.setAttribute("column_size", String.valueOf(columnSize));
        }
        m_currentTableNode.appendChild(columnElement);
    }
    /**
     * Write a relation tag using this format
     * <relation column_name="column_name" table_name="table" />
     * where "column_name" is a foreign key in the current table and
     * "table_name" is the table that "column_name" refer to.
     * @param tableName table name that the foreign keys will be retrieved from 
     */
    private void writeRelation(String tableName){
        Element relationElement = m_document.createElement(ConstantManager.RELATION_ELEMENT);
        ResultSet rs = null;
        try {
            DatabaseMetaData metadata = m_connection.getMetaData();
            rs = metadata.getImportedKeys(m_connection.getCatalog(), null, tableName.toUpperCase());
            while (rs.next()) {
                String fkTableName = rs.getString("PKTABLE_NAME");
                String fkColumnName = rs.getString("FKCOLUMN_NAME");
                relationElement.setAttribute("table_name", fkTableName);
                relationElement.setAttribute("column_name", fkColumnName);
                m_currentTableNode.appendChild(relationElement);
            }
        }catch (SQLException ex) {
            Logger.getLogger(DatabaseXmlWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                 Connection conn;
            database.DatabaseConnection con = new DatabaseConnection();
            conn = con.getConnection();
            new DatabaseXmlWriter(conn, UserLogin.getUserName(),UserLogin.getPassWord()).doWrite();
            }
        });
    }
}
