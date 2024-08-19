package me.kumo.mcf;

import com.github.weisj.darklaf.LafManager;
import com.github.weisj.darklaf.settings.SettingsConfiguration;
import com.github.weisj.darklaf.settings.ThemeSettings;
import me.kumo.nbt.DatReader;
import me.kumo.res.FontLoader;

import javax.swing.*;
import java.io.*;
import java.util.prefs.Preferences;

import static com.github.weisj.darklaf.LafManager.getPreferredThemeStyle;

public class MinecraftForensics extends JFrame {
    public MinecraftForensics() {
        super("Minecraft Forensics");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MainFrame mainFrame;
        setContentPane(mainFrame = new MainFrame());
        setJMenuBar(new MenuBar(mainFrame));
    }

    public static void main(String[] args) {
        load();
        System.setProperty("org.slf4j.simpleLogger.log.org.reflections", "off");
        Runtime.getRuntime().addShutdownHook(new Thread(MinecraftForensics::save));
        FontLoader.loadFonts("/fonts/", "JetBrainsMono");
        SwingUtilities.invokeLater(() -> {
            var w = new MinecraftForensics();
            w.setSize(1280, 720);
            w.setLocationRelativeTo(null);
            w.setVisible(true);
            try {
                DatReader.read();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static void load() {
        ThemeSettings settings = ThemeSettings.getInstance();
        Preferences preferences = Preferences.userNodeForPackage(MinecraftForensics.class);
        byte[] serializedSettings = preferences.getByteArray("themeSettings", null);
        if (serializedSettings != null) {
            try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(serializedSettings))) {
                SettingsConfiguration config = (SettingsConfiguration) in.readObject();
                settings.setConfiguration(config);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            settings.apply();
        } else {
            LafManager.themeForPreferredStyle(getPreferredThemeStyle());
        }
    }

    private static void save() {
        ThemeSettings settings = ThemeSettings.getInstance();
        SettingsConfiguration config = settings.exportConfiguration();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (ObjectOutputStream out = new ObjectOutputStream(bos)) {
            out.writeObject(config);
            out.flush();
            Preferences preferences = Preferences.userNodeForPackage(MinecraftForensics.class);
            preferences.putByteArray("themeSettings", bos.toByteArray());
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}
