package com.github.shap_po.pencilgames.client.ui.util;

import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

public class NumberField extends JTextField {
    public NumberField(Document doc, String text, int columns) {
        super(doc, text, columns);
        ((PlainDocument) getDocument()).setDocumentFilter(new NumberFilter());
    }

    public NumberField(String text, int columns) {
        this(null, text, columns);
    }

    public NumberField(int columns) {
        this(null, "", columns);
    }

    public NumberField() {
        this(null, "", 0);
    }

    public int getValue(){
        return Integer.parseInt(this.getText());
    }
}
