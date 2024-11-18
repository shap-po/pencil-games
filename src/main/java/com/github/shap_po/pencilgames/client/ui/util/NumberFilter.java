package com.github.shap_po.pencilgames.client.ui.util;

import javax.swing.text.DocumentFilter;

public class NumberFilter extends DocumentFilter {
    @Override
    public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, javax.swing.text.AttributeSet attr) throws javax.swing.text.BadLocationException {
        if (isTextValid(string)) {
            super.insertString(fb, offset, string, attr);
        }
    }

    @Override
    public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, javax.swing.text.AttributeSet attr) throws javax.swing.text.BadLocationException {
        if (isTextValid(text)) {
            super.replace(fb, offset, length, text, attr);
        }
    }

    private boolean isTextValid(String text) {
        return text.matches("\\d+");
    }
}
