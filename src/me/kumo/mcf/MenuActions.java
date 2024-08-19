package me.kumo.mcf;

import javax.swing.*;
import java.io.File;

public class MenuActions {
    public static File getFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.showSaveDialog(null);
        return chooser.getSelectedFile();
    }
}
