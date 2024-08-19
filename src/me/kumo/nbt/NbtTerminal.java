package me.kumo.nbt;

import com.github.weisj.darklaf.components.OverlayScrollPane;
import com.github.weisj.darklaf.components.text.NumberedTextComponent;
import com.github.weisj.darklaf.iconset.AllIcons;
import me.kumo.mcf.MainFrame;
import me.kumo.res.FontLoader;
import me.kumo.utils.IconButton;
import me.kumo.utils.TextCompWriter;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class NbtTerminal extends Box {
    //    private final Component right;
    private final RSyntaxTextArea codeEditor;
    private final JTextPane result;
    private final MainFrame mainFrame;
    private final OverlayScrollPane right;

    public NbtTerminal(MainFrame mainFrame) {
        super(BoxLayout.X_AXIS);
        this.mainFrame = mainFrame;
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.setAlignmentY(Component.TOP_ALIGNMENT);
        toolbar.setOrientation(SwingConstants.VERTICAL);
        add(toolbar);
        add(new OverlayScrollPane(new RTextScrollPane(codeEditor = MainFrame.createEditor())));
        add(right = new OverlayScrollPane(new NumberedTextComponent(result = new JTextPane())));
//        right = new OverlayScrollPane(new NumberedTextComponent(result = new JTextPane()));
        codeEditor.setText(NbtTerminalExamples.TRAVERSE.code);
        result.setFont(FontLoader.fontMap.get("Regular").deriveFont(12f));
        result.setEditable(false);
        right.setVisible(false);
        toolbar.add(new IconButton(AllIcons.Action.Play.get(8, 8)) {{
            addActionListener(NbtTerminal.this::play);
        }});
        toolbar.add(new IconButton(AllIcons.Navigation.ArrowDivider.get(8, 8)) {{
            addActionListener(e -> right.setVisible(!right.isVisible()));
        }});
        toolbar.add(new IconButton(AllIcons.Action.Delete.get(8, 8)) {{
            addActionListener(e -> mainFrame.removeTerm(NbtTerminal.this));
        }});
    }

    private void play(ActionEvent actionEvent) {
        right.setVisible(true);
        result.setText("");
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        try (TextCompWriter w = new TextCompWriter(result); TextCompWriter ew = new TextCompWriter(result, Color.RED)) {
            engine.getContext().setWriter(w);
            engine.getContext().setErrorWriter(ew);
            try {
                if (mainFrame.getActiveTag() != null) {
                    Object o = engine.eval(String.format("JSON.parse('%s')", mainFrame.getActiveTag().toString().replace("\\", "\\\\")));
                    engine.getContext().setAttribute("data", o, ScriptContext.GLOBAL_SCOPE);
                }
                var r = engine.eval(codeEditor.getText());
                if (r != null) w.write(r + "\n\n");
                w.write("Exited successfully");
            } catch (ScriptException e) {
                ew.write("\n\nExited with error!\n");
                ew.write(e.getMessage());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
