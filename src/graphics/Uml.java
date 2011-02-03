package graphics;

import com.mxgraph.io.mxCodec;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxResources;
import com.mxgraph.util.png.mxPngEncodeParam;
import com.mxgraph.util.png.mxPngImageEncoder;
import com.mxgraph.view.mxGraph;
import database.DatabaseConnection;
import gui.UmlToolBar;
import gui.UserLogin;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Vector;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import utils.ConstantManager;

public class Uml extends JFrame {

    /**
     * UML designer
     * @author Ahmed Ghanem.
     */
    private static final long serialVersionUID = -2707712944901661771L;
    private DatabaseConnection connection;
    private Vector databaseEntities;
    private Vector colHead;
    private int x = 40;
    private int y = 20;
    private Statement sqlStatement;
    private ResultSet sqlResultSet;
    private int colCount;
    private Image mainIcon = Toolkit.getDefaultToolkit().getImage(ConstantManager.OCTOPUS_ICON_PATH);
    private mxGraphComponent graphComponent;
    private mxGraph graph;
    private UmlToolBar toolBar;
    private JFileChooser savePngFile;
    private BufferedImage image;
    private mxCodec codec;
    private mxPngEncodeParam param;
    private FileOutputStream outputStream;
    private mxPngImageEncoder encoder;
    private PrinterJob pj;
    private PageFormat format;

    @SuppressWarnings("unused")
    public Uml() throws SQLException, FileNotFoundException, IOException {
        super(UserLogin.getUserName());
        toolBar = new UmlToolBar();
        connection = new DatabaseConnection();
        databaseEntities = new Vector();
        colHead = new Vector();
        try {
            databaseEntities = connection.getTablesNames();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "there is no tables");
        }
        graph = new mxGraph();
        Object parent = graph.getDefaultParent();

        graph.getModel().beginUpdate();
        try {
            for (int i = 0; i <= databaseEntities.size() - 1; i++) {
                graph.insertVertex(parent, null, databaseEntities.get(i), x + (i * 170), y + (i * 100), 140,
                        30);
                graph.insertVertex(parent, null, getAttributes(databaseEntities.get(i)), x + (i * 170), y + (i * 100) + 30, 140,
                        colCount * 20);
            }

        } finally {
            graph.getModel().endUpdate();
        }
        graphComponent = new mxGraphComponent(getGraph());
        graphComponent.setEnabled(false);
        setLayout(new BorderLayout());
        add(toolBar, BorderLayout.NORTH);
        add(graphComponent, BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 600);
        setVisible(true);
        setLocationRelativeTo(null);
        setIconImage(mainIcon);
        addUmlButtonListener();

    }
/**
 *
 * @param tableName oracle table name
 * @return attributes as string
 * @throws SQLException
 */
    public String getAttributes(Object tableName) throws SQLException {
        Vector x = getCols(tableName);
        String attributes = "";
        for (int i = 0; i <= x.size() - 1; i++) {
            attributes += x.get(i) + "\n";
        }
        return attributes;
    }
/**
 *
 * @param tableName oracle table name
 * @return columns
 * @throws SQLException
 */
    public Vector getCols(Object tableName) throws SQLException {
        colHead.clear();
        try {
            sqlStatement = connection.getConnection().createStatement();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "error getting information");
        }
        try {
            sqlResultSet = sqlStatement.executeQuery("select * from " + tableName);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Get column count
        colCount = sqlResultSet.getMetaData().getColumnCount();
        for (int i = 1; i <= colCount; i++) {
            colHead.addElement(
                    sqlResultSet.getMetaData().getColumnName(i));
        }

        return colHead;
    }
/**
 * add listeners for uml toolbar panel
 */
    public void addUmlButtonListener() {
        toolBar.getSaveBtn().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                savePngFile = new JFileChooser();
                int choice = savePngFile.showSaveDialog(savePngFile);
                if (choice == JFileChooser.CANCEL_OPTION) {
                }
                if (choice == JFileChooser.APPROVE_OPTION) {
                    try {
                        BufferedImage image = mxCellRenderer.createBufferedImage(graph, null, 1, graphComponent.getBackground(), graphComponent.isAntiAlias(), null, graphComponent.getCanvas());
                        codec = new mxCodec();
                        param = mxPngEncodeParam.getDefaultEncodeParam(image);
                        outputStream = new FileOutputStream(savePngFile.getSelectedFile() + ConstantManager.ALLOWED_FILE_EXTENTIONS[3]);
                        encoder = new mxPngImageEncoder(outputStream, param);
                        if (image != null) {
                            try {
                                encoder.encode(image);
                            } catch (IOException ex) {
                                Logger.getLogger(Uml.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (OutOfMemoryError o) {
                                JOptionPane.showMessageDialog(null, o.getMessage());
                            } finally {
                                try {
                                    outputStream.close();
                                } catch (IOException ex) {
                                    Logger.getLogger(Uml.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        } else {
                            JOptionPane.showMessageDialog(graphComponent, mxResources.get("noImageData"));
                        }
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(Uml.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
        });
        toolBar.getPageSetupBtn().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                pj = PrinterJob.getPrinterJob();
                format = pj.pageDialog(graphComponent.getPageFormat());
                if (format != null) {
                    graphComponent.setPageFormat(format);
                    graphComponent.zoomAndCenter();
                }
            }
        });
        toolBar.getPrintBtn().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                pj = PrinterJob.getPrinterJob();
                if (pj.printDialog()) {
                    PageFormat pf = graphComponent.getPageFormat();
                    Paper paper = new Paper();
                    double margin = 36;
                    paper.setImageableArea(margin, margin, paper.getWidth()
                            - margin * 2, paper.getHeight() - margin * 2);
                    pf.setPaper(paper);
                    pj.setPrintable(graphComponent, pf);

                    try {
                        pj.print();
                    } catch (PrinterException e2) {
                        System.out.println(e2);
                    }
                }
            }
        });
        toolBar.getJSlider().addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                graphComponent.zoomTo(toolBar.getJSlider().getValue() / 2, rootPaneCheckingEnabled);

            }
        });
    }

    /**
     * @return the graphComponent
     */
    public mxGraphComponent getGraphComponent() {
        return graphComponent;
    }

    /**
     * @param graphComponent the graphComponent to set
     */
    public void setGraphComponent(mxGraphComponent graphComponent) {
        this.graphComponent = graphComponent;
    }

    /**
     * @return the graph
     */
    public mxGraph getGraph() {
        return graph;
    }

    /**
     * @param graph the graph to set
     */
    public void setGraph(mxGraph graph) {
        this.graph = graph;
    }
}
