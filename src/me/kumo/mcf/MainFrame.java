package me.kumo.mcf;

import com.github.weisj.darklaf.LafManager;
import com.github.weisj.darklaf.components.OverlayScrollPane;
import com.github.weisj.darklaf.components.tabframe.JTabFrame;
import com.github.weisj.darklaf.components.tabframe.PanelPopup;
import com.github.weisj.darklaf.components.tabframe.TabFramePopup;
import com.github.weisj.darklaf.components.tabframe.TabbedPopup;
import com.github.weisj.darklaf.theme.event.ThemeInstalledListener;
import com.github.weisj.darklaf.util.Alignment;
import me.kumo.nbt.NbtTerminal;
import me.kumo.nbt.NbtTreeNode;
import me.kumo.nbt.NbtTreeRenderer;
import me.kumo.nbt.custom.CustomElements;
import me.kumo.res.FontLoader;
import me.kumo.utils.JSPretty;
import net.querz.nbt.io.NBTUtil;
import net.querz.nbt.io.NamedTag;
import net.querz.nbt.io.SNBTUtil;
import net.querz.nbt.tag.Tag;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static me.kumo.nbt.DatReader.handleTag;

public class MainFrame extends JTabFrame {
    private final RSyntaxTextArea editor;
    private final JLabel title;
    private final TabbedPopup nodeInfoPane;
    private JTree tree;
    private Tag<?> activeTag;

    private File file;
    private NamedTag root;


    public MainFrame() {
        Box b = new Box(BoxLayout.Y_AXIS);
        b.add(title = new JLabel());
        b.add(new OverlayScrollPane(new RTextScrollPane(editor = createEditor())));
        DropTarget target = new DropTarget() {
            @Override
            public synchronized void drop(DropTargetDropEvent evt) {
                try {
                    evt.acceptDrop(DnDConstants.ACTION_COPY);
                    @SuppressWarnings("unchecked")
                    List<File> droppedFiles = (List<File>) evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    System.out.println(droppedFiles);
                    if (!droppedFiles.isEmpty()) openFile(droppedFiles.getFirst());
                    evt.dropComplete(true);
                } catch (Exception e) {
                    evt.dropComplete(false);
                    throw new RuntimeException(e);
                }
            }
        };
        b.setDropTarget(target);
        setContent(b);
        Component treeWrap;
        addTab(treeWrap = getTree(), "Tree", Alignment.NORTH_WEST);
        openTab(treeWrap);
        setDropTarget(target);
        nodeInfoPane = new TabbedPopup("");
        addTab((TabFramePopup) nodeInfoPane, "Info", null, Alignment.EAST);
    }

    public static RSyntaxTextArea createEditor() {
        var editor = new RSyntaxTextArea(1, 60);
        editor.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
        editor.setCodeFoldingEnabled(true);
        editor.setFont(FontLoader.fontMap.get("Regular").deriveFont(12f));
        try {
            Theme syntaxTheme = Theme.load(MainFrame.class.getResourceAsStream("/org/fife/ui/rsyntaxtextarea/themes/dark.xml"));
            syntaxTheme.apply(editor);
            LafManager.addThemeChangeListener((ThemeInstalledListener) e -> syntaxTheme.apply(editor));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return editor;
    }

    private Component getTree() {
        tree = new JTree();
        tree.addTreeSelectionListener(e -> {
            NbtTreeNode node = (NbtTreeNode) tree.getLastSelectedPathComponent();
            if (node == null) return;
            setActiveNode(node.tag, node.path);
        });
        ((DefaultTreeModel) tree.getModel()).setRoot(null);
        tree.setCellRenderer(new NbtTreeRenderer());
        return new OverlayScrollPane(tree);
    }

    public void setActiveNode(NamedTag tag, String path) {
        if (tag == null) {
            activeTag = null;
            title.setText("");
            editor.setText("");
        } else SwingUtilities.invokeLater(() -> {
            try {
                activeTag = tag.getTag();
                title.setText(path);
                editor.setText(JSPretty.beautify(SNBTUtil.toSNBT(tag.getTag())));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public Tag<?> getActiveTag() {
        return activeTag;
    }

    public void addTerm() {
        addTab(new NbtTerminal(this), "Script", Alignment.SOUTH_WEST);
        revalidate();
    }

    public void removeTerm(NbtTerminal node) {
        List<TabFramePopup> compsForAlignment = compsForAlignment(Alignment.SOUTH_WEST);
        for (int i = 0; i < compsForAlignment.size(); i++) {
            if (compsForAlignment.get(i).getComponent() instanceof PanelPopup pop) {
                if (pop.getContentPane() == node) {
                    removeTab(Alignment.SOUTH_WEST, i);
                    break;
                }
            }
        }
    }

    public void makeTree() {
        nodeInfoPane.getTabbedPane().removeAll();
        ((DefaultTreeModel) tree.getModel()).setRoot(handleTag(root.getName(), "", root.getTag(), (tag, path) -> CustomElements.find(file.getName(), path, tag).forEach(c -> nodeInfoPane.getTabbedPane().addTab(c.getFirst(), c.getSecond()))));
    }

    public void openFile(File file) {
        try {
            this.file = file;
            this.root = NBTUtil.read(file);
            makeTree();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
