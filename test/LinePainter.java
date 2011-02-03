

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

public class LinePainter extends DefaultHighlighter.DefaultHighlightPainter
        implements CaretListener, DocumentListener {

    private JTextComponent component;
    private Element root;
    private Highlighter highlighter;
    private Object lastHighlight;

    public LinePainter(JTextComponent component, Color color) {
        super(color);
        this.component = component;

        root = component.getDocument().getDefaultRootElement();
        highlighter = component.getHighlighter();

        //  Add listeners so we know when to change highlighting

        component.addCaretListener(this);
        component.getDocument().addDocumentListener(this);

        //  Initially highlight the first line

        addHighlight(0);
    }

    /**
     * Paints a portion of a highlight.
     *
     * @param g the graphics context
     * @param offs0 the starting model offset >= 0
     * @param offs1 the ending model offset >= offs1
     * @param bounds the bounding box of the view, which is not
     *		necessarily the region to paint.
     * @param c the editor
     * @param view View painting for
     * @return region drawing occured in
     */
    public Shape paintLayer(Graphics g, int offs0, int offs1,
            Shape bounds, JTextComponent c, View view) {
        try {
            // Only use the first offset to get the line to highlight

            Rectangle r = c.modelToView(offs0);
            r.x = 0;
            r.width = c.getSize().width;

            // --- render ---

            g.setColor(getColor());
            g.fillRect(r.x, r.y, r.width, r.height);
            return r;
        } catch (BadLocationException e) {
            return null;
        }
    }

    //  Implement CaretListener
    public void caretUpdate(CaretEvent e) {
        resetHighlight();
    }

    //  Implement DocumentListener
    public void removeUpdate(DocumentEvent e) {
        resetHighlight();
    }

    public void insertUpdate(DocumentEvent e) {
    }

    public void changedUpdate(DocumentEvent e) {
    }

    /*
     *   Remove/add the highlight to make sure it gets repainted
     */
    private void resetHighlight() {
        highlighter.removeHighlight(lastHighlight);

        int line = root.getElementIndex(component.getCaretPosition());
        Element lineElement = root.getElement(line);
        int start = lineElement.getStartOffset();
        addHighlight(start);
    }

    private void addHighlight(int offset) {
        try {
            lastHighlight = highlighter.addHighlight(offset, offset + 1, this);
        } catch (BadLocationException ble) {
        }
    }
}
