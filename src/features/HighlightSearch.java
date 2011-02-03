package features;

import java.awt.Color;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
/**
 * This Class used to highlight the search words in SQL Editor.
 * @author Ahmed Ghanem.
 */
public class HighlightSearch {

    private Color color;
    private NewHighlighter myHighlighter = new NewHighlighter(Color.ORANGE);
/**
 *
 * @param textComp Component which use in highlight.
 * @param pattern  the search word pattern.
 */
    public void highlight(JTextComponent textComp, String pattern) {
        // First remove all old highlights.
        removeHighlights(textComp);
        try {
            Highlighter highLight = textComp.getHighlighter();
            Document doc = textComp.getDocument();
            String text = doc.getText(0, doc.getLength());
            int pointerPos = 0;
            // Search for pattern

            while ((pointerPos = text.indexOf(pattern, pointerPos)) >= 0) {
                highLight.addHighlight(pointerPos, pointerPos + pattern.length(), myHighlighter);
                pointerPos += pattern.length();
            }
        } catch (BadLocationException e) {
        }
    }
/***
 *
 * @param textComp remove highlight from component.
 */
    public void removeHighlights(JTextComponent textComp) {
        Highlighter hilite = textComp.getHighlighter();
        Highlighter.Highlight[] hilites = hilite.getHighlights();
        for (int i = 0; i < hilites.length; i++) {
            if (hilites[i].getPainter() instanceof NewHighlighter) {
                hilite.removeHighlight(hilites[i]);
            }
        }
    }
}
/**
 * This Class used to set the search highlight color.
 * @author ahmed
 */
class NewHighlighter extends DefaultHighlighter.DefaultHighlightPainter {
/***
 *
 * @param color set search highlight color.
 */
    public NewHighlighter(Color color) {
        super(color);
    }
}
