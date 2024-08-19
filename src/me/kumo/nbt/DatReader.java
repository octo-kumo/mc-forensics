package me.kumo.nbt;

import net.querz.nbt.io.NamedTag;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;
import net.querz.nbt.tag.Tag;

import java.io.IOException;
import java.util.Map;
import java.util.function.BiConsumer;

public class DatReader {
    public static void read() throws IOException {
//        System.out.println(namedTag.getName() + " " + namedTag.getTag());
    }

    public static NbtTreeNode handleTag(String name, String path, Tag<?> _tag, BiConsumer<NamedTag, String> consumer) {
        NamedTag tag = new NamedTag(name, _tag);
        NbtTreeNode root = new NbtTreeNode(name, path, tag);
        consumer.accept(tag, name);

        if (_tag instanceof CompoundTag compoundTag) {
            compoundTag.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(entry -> root.add(handleTag(entry.getKey(), (path.isEmpty() ? "" : path + ".") + entry.getKey(), entry.getValue(), consumer)));
            return root;
        } else if (_tag instanceof ListTag<? extends Tag<?>> list) {
            for (int i = 0; i < list.size(); i++) {
                root.add(handleTag(i + "", path + "[" + i + "]", list.get(i), consumer));
            }
            return root;
        }
        return root;
    }
}
