package me.kumo.mcf;

import com.github.weisj.darklaf.settings.ThemeSettings;

import javax.swing.*;
import java.awt.event.ActionListener;

public class MenuBar extends JMenuBar {
    public MenuBar(MainFrame mainFrame) {
        add(new JMenu("File") {{
            add(but("Open", e -> mainFrame.openFile(MenuActions.getFile())));
            add(new JSeparator());
            add(but("Theme", e -> ThemeSettings.showSettingsDialog(this.getRootPane())));
        }});
        add(new JMenu("Edit") {{
            add(but("New Script", e -> mainFrame.addTerm()));
        }});

//        add(new JMenu("Help") {{
//
//        }});
    }

    private static JMenuItem but(String name, ActionListener actionListener) {
        var i = new JMenuItem(name);
        i.addActionListener(actionListener);
        return i;
    }
}
