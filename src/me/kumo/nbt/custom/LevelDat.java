package me.kumo.nbt.custom;

import com.github.weisj.darklaf.components.border.DarkBorders;
import net.querz.nbt.io.NamedTag;
import net.querz.nbt.tag.CompoundTag;

import javax.swing.*;
import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LevelDat {
    private static final DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    @CustomTagElement(file = "level.dat$", path = "^Data$", title = "level.dat")
    public static Component getDataElement(NamedTag tag) {
        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        CompoundTag t = (CompoundTag) tag.getTag();
        box.add(new JLabel(t.getString("LevelName")) {{
            setAlignmentX(0);
        }});
        ArrayList<String> tags = new ArrayList<>();
        tags.add(t.getCompoundTag("Version").getString("Name"));
        if (t.getCompoundTag("Version").getBoolean("Snapshot")) tags.add("snapshot");

        if (t.getBoolean("allowCommands")) tags.add("cheats");
        if (t.getDouble("BorderSize") != 60000000) tags.add("border/" + t.getDouble("BorderSize"));
        if (t.getBoolean("hardcore")) tags.add("hardcore");
        tags.add("last played " + format.format(new Date(t.getLong("LastPlayed"))));
        box.add(new JPanel(new FlowLayout(FlowLayout.LEADING)) {{
            setAlignmentX(0);
            setMinimumSize(new Dimension(100, 100));
            tags.forEach(s -> add(new JLabel(s) {{
                setBorder(DarkBorders.createLineBorder(1, 1, 1, 1));
            }}));
        }});
        return box;
    }


    @CustomTagElement(file = "level.dat$", path = "^Data.Player$", title = "player")
    public static Component getPlayerElement(NamedTag tag) {
        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        CompoundTag t = (CompoundTag) tag.getTag();
//        box.add(new JLabel(t.getString("LevelName")) {{}});
        return box;
    }
}
