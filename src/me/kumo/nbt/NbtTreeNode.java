package me.kumo.nbt;

import net.querz.nbt.io.NamedTag;
import net.querz.nbt.tag.ArrayTag;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;

import javax.swing.tree.DefaultMutableTreeNode;

public class NbtTreeNode extends DefaultMutableTreeNode {
    public final String name;
    public final String path;
    public final NamedTag tag;

    public NbtTreeNode(String name, String path, NamedTag tag) {
        super();
        this.name = name;
        this.path = path;
        this.tag = tag;
    }

    @Override
    public String toString() {
        final var t = tag.getTag();
        var n = tag.getName();
        if (t == null) return n + " = null";
        switch (t) {
            case CompoundTag compoundTag -> n += " = {" + compoundTag.size() + "}";
            case ArrayTag<?> arrayTag -> n += " = [" + arrayTag.length() + "]";
            case ListTag<?> listTag -> n += " = [" + listTag.size() + "]";
            default -> n += " = " + t.valueToString();
        }
        return n + " : " + t.getClass().getSimpleName();
    }
}
