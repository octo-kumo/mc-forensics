package me.kumo.utils;

import com.github.weisj.darklaf.ui.button.DarkButtonUI;

import javax.swing.*;

public class IconButton extends JButton {
    public IconButton(final Icon icon) {
        init(icon);
    }

    protected void init(Icon icon) {
        putClientProperty(DarkButtonUI.KEY_SQUARE, true);
        putClientProperty(DarkButtonUI.KEY_ROUND, true);
        putClientProperty(DarkButtonUI.KEY_NO_BORDERLESS_OVERWRITE, true);
        setIcon(icon);
        setDisabledIcon(icon);
    }
}