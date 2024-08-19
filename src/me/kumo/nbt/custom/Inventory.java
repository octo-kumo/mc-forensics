package me.kumo.nbt.custom;

import net.querz.nbt.io.NamedTag;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URI;

public class Inventory {

    @CustomTagElement(path = "Inventory$", title = "Inventory")
    public static Component getInventoryElement(NamedTag tag) {
        JPanel inv = new JPanel(new GridLayout(4, 9, 2, 2));
        for (CompoundTag o : (ListTag<CompoundTag>) tag.getTag()) {
            try {
                inv.add(new JLabel(new ItemIcon(o)));
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }

        return inv;
    }

    public static class ItemIcon extends ImageIcon {
        public ItemIcon(CompoundTag tag) throws MalformedURLException {
            super(URI.create("https://raw.githubusercontent.com/PrismarineJS/minecraft-assets/master/data/1.20.2/items/" + tag.getString("id").substring("minecraft:".length()) + ".png").toURL());
        }
    }
}
