package me.kumo.nbt;

import com.github.weisj.darklaf.iconset.AllIcons;
import com.github.weisj.darklaf.iconset.IconSet;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.StringTag;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

public class NbtTreeRenderer extends DefaultTreeCellRenderer {
    @Override
    public Component getTreeCellRendererComponent(JTree tree,
                                                  Object value, boolean selected, boolean expanded,
                                                  boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        if (!(value instanceof NbtTreeNode nodo)) return this;
        if (tree.getModel().getRoot().equals(nodo)) {
            setIcon(IconSet.iconLoader().getIcon("files/homeFolder.svg", true));
        } else {
            if (nodo.tag.getTag() instanceof CompoundTag) {
                setIcon(AllIcons.Files.Folder.get());
            } else if (nodo.tag.getTag() instanceof StringTag) {
                setIcon(AllIcons.Files.Text.get());
            } else {
                setIcon(null);
            }
        }
        return this;
    }
}
