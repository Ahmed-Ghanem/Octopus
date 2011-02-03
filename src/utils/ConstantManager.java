/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 * contains global constant for octopus
 * @author ahmed
 */
public class ConstantManager {

    /**
     * Return Octopus name and version
     */
    public final static String OCTOPUS_VERSION = "Octopus V1.4";
    /**
     * Return Octopus icon
     */
    public final static String OCTOPUS_ICON_PATH = "splash/ocl.png";
    /**
     * Return Octopus web site URL 
     */
    public final static String OCTOPUS_URL = "http://ja-octopus.blogspot.com/";
    /**
     * Return Octopus splash screen
     */
    public final static String OCTOPUS_SPLASH_LOGO = "splash/splash.png";
    /**
     * Return allowed time for splash screen
     */
    public final static int OCTOPUS_SPLASH_TIME = 3000;
    /**
     * Return driver name to run using Class.forName()
     */
    public final static String ORACLE_DRIVER = "oracle.jdbc.OracleDriver";
    /**
     * Return all tables on schema
     */
    public final static String USER_TABLES = "select object_name from user_objects where object_type = 'TABLE'";
    /**
     * Return sql reserved words used in sqlEditor auto
     */
    public final static String[] SQLRESERVEDWORDS = {"access", "add", "all", "alter", "and", "any", "as", "asc", "audit", "between",
        "by", "char", "check", "cluster", "column", "comment", "compress", "connect",
        "create", "current", "date", "decimal", "default", "delete", "desc", "distinct",
        "drop", "else", "exclusive", "exists", "file", "float", "for", "from", "grant", "group",
        "having", "identified", "immediate", "in", "increment", "index", "initial", "insert", "integer",
        "intersect", "into", "is", "level", "like", "lock", "long", "maxextents", "minus", "mlslabel",
        "mode", "modify", "noaudit", "nocompress", "not", "nowait", "null", "number", "of", "offline",
        "on", "online", "option", "or", "order", "pctfree", "prior", "privileges", "public", "raw", "rename",
        "resource", "revoke", "row", "rowid", "rownum", "rows", "select", "session", "set", "share", "size",
        "smallint", "start", "successful", "synonym", "sysdate", "table", "then", "to", "trigger", "uid", "union",
        "unique", "update", "user", "validate", "values", "varchar", "varchar2", "view", "whenever", "where", "with"
    };
    /**
     * Return the allowed file extensions allowed in Octopus
     */
    public final static String[] ALLOWED_FILE_EXTENTIONS = {".dmp", ".log", ".sql",".png"};
    /**
     * Return number of oracle errors
     */
    public final static int ORACLE_ERRORS = 47992;
    /**
     * Return number using in De serialization
     */
    private final static long serialVersionUID = -2707712944901661771L;
}
