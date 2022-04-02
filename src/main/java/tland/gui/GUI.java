package tland.gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import tland.gb.GameBoy;
import tland.gb.mem.CartridgeROM;

public class GUI {
    private JFrame frame;
    private CartridgeROM rom;

    public GUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            JFrame.setDefaultLookAndFeelDecorated(true);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            System.err.println("Could not set look and feel. Using defaults.");
        }
        frame = new JFrame();
        frame.setTitle("JGBE");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(160 * 3, 144 * 3);
    }

    public void showGUI() {
        final JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem openRom = new JMenuItem("Open ROM");

        openRom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent a) {
                JFileChooser chooser = new JFileChooser();

                File currDir = new File(System.getProperty("user.dir"));
                chooser.setCurrentDirectory(currDir);

                FileNameExtensionFilter filter = new FileNameExtensionFilter("Game Boy ROM files", "gb", "gbc");
                chooser.setFileFilter(filter);

                int ret = chooser.showOpenDialog(null);
                if (ret == JFileChooser.APPROVE_OPTION) {
                    loadROM(chooser.getSelectedFile().getPath());
                }
            }
        });

        fileMenu.add(openRom);
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);
    }

    private void loadROM(String pathString) {
        System.out.println(pathString);
        Path path = Paths.get(pathString);
        byte[] romFile;

        try {
            romFile = Files.readAllBytes(path);
        } catch (FileNotFoundException | NoSuchFileException e) {
            System.err.println("Could not read file " + path.getFileName() + ": file not found.");
            return;
        } catch (IOException e) {
            System.err.println("Could not read file " + path.getFileName() +
                    ": an exception occurred: " + e.toString());
            return;
        }
        rom = new CartridgeROM(romFile);
        GameBoy gb = new GameBoy(rom);

        gb.enableDebugger();

        gb.run();

    }
}
