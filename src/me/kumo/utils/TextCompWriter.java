package me.kumo.utils;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.Writer;

public class TextCompWriter extends Writer {
    private final SimpleAttributeSet attributes;
    private JTextPane text;

    public TextCompWriter(JTextPane text) {
        this(text, null);
    }

    public TextCompWriter(JTextPane text, Color color) {
        this.text = text;
        this.attributes = new SimpleAttributeSet();
        if (color != null) StyleConstants.setForeground(attributes, color);
    }

    @Override
    public void write(char[] cbuf, int off, int len) {
        try {
            StyledDocument doc = text.getStyledDocument();
            doc.insertString(doc.getLength(), new String(cbuf, off, len), attributes);
            text.setCaretPosition(doc.getLength());
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() {
        text = null;
    }
}
