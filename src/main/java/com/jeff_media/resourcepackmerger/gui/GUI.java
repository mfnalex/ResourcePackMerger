package com.jeff_media.resourcepackmerger.gui;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.jeff_media.resourcepackmerger.ResourcePackMerger;
import com.jeff_media.resourcepackmerger.Utils;
import com.jeff_media.resourcepackmerger.data.Config;
import com.jeff_media.resourcepackmerger.data.Replacer;
import com.jeff_media.resourcepackmerger.data.ResourcePackVersion;
import com.jeff_media.resourcepackmerger.data.ZipCompression;
import com.jeff_media.resourcepackmerger.logging.GuiLogger;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.stream.Collectors;

public class GUI extends JFrame {

    public static final String INCEPTION_YEAR = "2022";

    final JLabel labelName = new JLabel();
    final JTextField fieldName = new JTextField();
    final JLabel labelIcon = new JLabel();
    final JLabel labelIconValue = new JLabel();
    final JButton buttonIcon = new JButton();
    final JButton buttonResetIcon = new JButton();
    final JLabel labelFormat = new JLabel();
    final JComboBox<ResourcePackVersion> fieldFormat = new JComboBox<>();
    final JComboBox<ZipCompression> fieldCompression = new JComboBox<>();
    final JLabel labelFiles = new JLabel();
    final JLabel labelOutputFile = new JLabel();
    final JLabel labelOutputFileValue = new JLabel();
    final JScrollPane scrollPanelListFiles = new JScrollPane();
    final DefaultListModel<File> listFilesModel = new DefaultListModel<>();
    final JList<File> listFiles = new JList<>(listFilesModel);
    final JPanel fileButtons = new JPanel();
    final JButton buttonAddFile = new JButton();
    final JButton buttonMoveUp = new JButton();
    final JButton buttonMoveDown = new JButton();
    final JButton buttonDeleteFile = new JButton();
    final JScrollPane scrollPaneLog = new JScrollPane();
    final JTextArea log = new JTextArea();
    final JButton buttonStart = new JButton();
    final JProgressBar progressBar = new JProgressBar();
    final JButton buttonOutputFile = new JButton();
    final JButton buttonAbout = new JButton("About");
    File lastFile = null;

    public GUI() {
        ResourcePackMerger.setLogger(new GuiLogger(log));
        initComponents();
        loadConfig();
    }

    private void initComponents() {

        int height = 0;
        Container contentPane = getContentPane();
        contentPane.setLayout(new MigLayout("hidemode 0",
                // columns
                "[fill]" + "[fill]" + "[fill][fill]",
                // rows
                "[]" + "[]" + "[]" + "[]" + "[]" + "[]" + "[]" + "[]"));


        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new MigLayout("hidemode 3",
                // columns
                "[fill][fill]",
                // rows
                "[][][][]"));

        BufferedImage logo = Utils.getLogo();
        JLabel picLabel = new JLabel(new ImageIcon(logo));
        headerPanel.add(picLabel, "cell 0 0 1 4");
        int height2 = 0;
        headerPanel.add(new JLabel(""), "cell 1 " + height2 + " 1 1");
        height2++;
        headerPanel.add(new JLabel("Resource Pack Merger v" + ResourcePackMerger.getVersion()), "cell 1 " + height2 + " 1 1");
        height2++;
        headerPanel.add(new JLabel("Copyright " + INCEPTION_YEAR + " JEFF Media GbR / mfnalex"), "cell 1 " + height2 + " 1 1");
        height2++;
        headerPanel.add(buttonAbout, "cell 1 " + height2 + " 1 1");
        contentPane.add(headerPanel, "cell 0 " + height + " 3 1");
        buttonAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JFrame frame = new JFrame("About ResourcePackMerger");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setLocationByPlatform(true);
                frame.add(new JLabel(new Replacer().put("$version", Utils.getVersion()).put("$year", INCEPTION_YEAR).apply("<HTML>ResourcePackMerger $version<BR>Copyright $year Alexander Maja (mfnalex) / JEFF Media GbR<BR><BR>GitHub: <a href=\"https://github.com/JEFF-Media-GbR/ResourcePackMerger\">https://github.com/JEFF-Media-GbR/ResourcePackMerger</a><BR>Discord: <a href=\"https://discord.jeff-media.com\">https://discord.jeff-media.com</a><br>Donate: <a href=\"https://paypal.me/mfnalex\">https://paypal.me/mfnalex</a></HTML>")));
                frame.pack();
                frame.setVisible(true);
            }
        });

        //<editor-fold desc="Description">
        height++;
        labelName.setText("Description");
        contentPane.add(labelName, "cell 0 " + height);

        fieldName.setText("My Custom Resource Pack");
        contentPane.add(fieldName, "cell 1 " + height + " 2 1");
        //</editor-fold>

        //<editor-fold desc="Override Icon">
        height++;
        labelIcon.setText("Override Icon");
        contentPane.add(labelIcon, "cell 0 " + height);
        labelIconValue.setText("No file selected");
        contentPane.add(labelIconValue, "cell 1 " + height + " 2 1");

        JPanel iconButtonsPanel = new JPanel(new MigLayout("insets 0", "[fill][fill]", ""));
        buttonIcon.setText("Change");
        iconButtonsPanel.add(buttonIcon, "cell 0 0");
        buttonResetIcon.setText("Reset");
        iconButtonsPanel.add(buttonResetIcon, "cell 1 0");
        contentPane.add(iconButtonsPanel, "cell 3 " + height);

        buttonIcon.addActionListener(new ChooseIconFileAction(this));
        buttonResetIcon.addActionListener(e -> labelIconValue.setText("No file selected"));
        //</editor-fold>

        //<editor-fold desc="Resource Pack Version">
        height++;
        labelFormat.setText("Resource Pack Format");
        contentPane.add(labelFormat, "cell 0 " + height);
        contentPane.add(fieldFormat, "cell 1 " + height + " 2 1");

        Arrays.stream(ResourcePackVersion.values()).forEachOrdered(version -> fieldFormat.addItem(version));
        fieldFormat.setRenderer(new ResourcePackVersionListRenderer());
        fieldFormat.setSelectedIndex(fieldFormat.getItemCount() - 1);
        //</editor-fold>

        //<editor-fold desc="Output file">
        height++;
        labelOutputFile.setText("Output file");
        contentPane.add(labelOutputFile, "cell 0 " + height);
        labelOutputFileValue.setText(new File("merged-resourcepack.zip").getAbsolutePath());
        contentPane.add(labelOutputFileValue, "cell 1 " + height + " 2 1");

        JPanel outputFileButtonsPanel = new JPanel(new MigLayout("insets 0", "[fill][fill]", ""));
        buttonOutputFile.setText("Change");
        outputFileButtonsPanel.add(buttonOutputFile, "cell 0 0");
        contentPane.add(outputFileButtonsPanel, "cell 3 " + height);

        buttonOutputFile.addActionListener(new ChooseOutputFileAction(this));
        //</editor-fold>

        //<editor-fold desc="Resource Packs">
        height++;
        labelFiles.setText("Resource Packs");
        contentPane.add(labelFiles, "cell 0 " + height);
        scrollPanelListFiles.setViewportView(listFiles);
        scrollPanelListFiles.setMinimumSize(new Dimension(500, 200));
        contentPane.add(scrollPanelListFiles, "cell 1 " + height + " 2 1");

        fileButtons.setLayout(new MigLayout("hidemode 3",
                // columns
                "[fill]",
                // rows
                "[]" + "[]" + "[]" + "[]"));

        buttonAddFile.setText("Add...");
        fileButtons.add(buttonAddFile, "cell 0 0");

        buttonMoveUp.setText("Move Up");
        buttonMoveUp.setEnabled(false);
        fileButtons.add(buttonMoveUp, "cell 0 1");

        buttonMoveDown.setText("Move Down");
        buttonMoveDown.setEnabled(false);
        fileButtons.add(buttonMoveDown, "cell 0 2");

        buttonDeleteFile.setText("Delete");
        buttonDeleteFile.setEnabled(false);
        fileButtons.add(buttonDeleteFile, "cell 0 3");

        contentPane.add(fileButtons, "cell 3 " + height);

        buttonAddFile.addActionListener(new AddFileAction(this));
        listFiles.addListSelectionListener(__ -> updateFileButtons());
        buttonMoveDown.addActionListener(__ -> moveFileInList(1));
        buttonMoveUp.addActionListener(__ -> moveFileInList(-1));
        buttonDeleteFile.addActionListener(__ -> {
            listFilesModel.remove(listFiles.getSelectedIndex());
            updateFileButtons();
        });
        //</editor-fold>

        //<editor-fold desc="Compression">
        height++;
        contentPane.add(new JLabel("ZIP Compression"), "cell 0 " + height);
        contentPane.add(fieldCompression, "cell 1 " + height + " 2 1");

        fieldCompression.setRenderer(new CompressionLevelListRenderer());
        Arrays.stream(ZipCompression.values()).forEachOrdered(level -> fieldCompression.addItem(level));
        //</editor-fold>

        //<editor-fold desc="Log window">
        height++;
        log.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        scrollPaneLog.setViewportView(log);
        contentPane.add(scrollPaneLog, "cell 0 " + height + " 4 1,hmin 150");

        ResourcePackMerger.getLogger().info("Waiting for input. Add at least one resource pack, then click on \"Start\".");
        ResourcePackMerger.getLogger().info("JSON files will automatically be merged when two or more resource packs contain conflicting files.");
        ResourcePackMerger.getLogger().info("When files cannot be merged, the file of the resource pack that's higher in the list takes precedence.");
        //</editor-fold>

        //<editor-fold desc="Progress bar">
        height++;
        contentPane.add(progressBar, "cell 0 " + height + " 4 1");

        progressBar.setIndeterminate(false);
        //</editor-fold>

        //<editor-fold desc="Start Button">
        height++;
        buttonStart.setText("Start");
        contentPane.add(buttonStart, "cell 0 " + height + " 4 1");

        buttonStart.addActionListener(new StartButtonAction(this));
        //</editor-fold>

        pack();
        setLocationRelativeTo(getOwner());

        this.addWindowListener(new WindowListener(this));
    }

    private void loadConfig() {
        Config config = new Config();
        try {
            config = Config.fromFile();
            if (!config.isNewlyGenerated) {
                ResourcePackMerger.getLogger().debug("Config loaded successfully.");
            }
        } catch (IOException e) {
            ResourcePackMerger.getLogger().error("Could not load config: " + e.getMessage());
        }
        fieldName.setText(config.description);
        fieldFormat.setSelectedItem(config.resourcePackVersion);
        fieldCompression.setSelectedItem(config.compression);
        config.resourcePackFiles.stream().forEachOrdered(name -> {
            File file = Paths.get(name).toFile();
            if (file.exists()) {
                listFilesModel.addElement(file);
            } else {
                ResourcePackMerger.getLogger().warn("Recently used Resource Pack " + name + " not found.");
            }
        });
        labelOutputFileValue.setText(config.outputFile);
        labelIconValue.setText(config.overrideIcon != null ? config.overrideIcon : "No file selected");
    }

    void updateFileButtons() {
        boolean enabled = true;
        if (listFilesModel.isEmpty() || listFiles.getSelectedIndex() == -1) {
            enabled = false;
        }
        boolean moveUpAllowed = enabled;
        boolean moveDownAllowed = enabled;
        if (listFiles.getSelectedIndex() < 1) {
            moveUpAllowed = false;
        }
        if (listFiles.getSelectedIndex() >= listFilesModel.size() - 1) {
            moveDownAllowed = false;
        }
        buttonDeleteFile.setEnabled(enabled);
        buttonMoveUp.setEnabled(moveUpAllowed);
        buttonMoveDown.setEnabled(moveDownAllowed);
        buttonStart.setEnabled(enabled);
    }

    private void moveFileInList(int offset) {
        int selected = listFiles.getSelectedIndex();
        File current = listFilesModel.remove(selected);
        listFilesModel.add(selected + offset, current);
        listFiles.setSelectedIndex(selected + offset);
        updateFileButtons();
    }

    public void saveConfig() {
        try {
            new Config(fieldName.getText(), labelOutputFileValue.getText(), Collections.list(listFilesModel.elements()).stream().map(file -> file.getAbsolutePath()).collect(Collectors.toList()), (ResourcePackVersion) fieldFormat.getSelectedItem(), (ZipCompression) fieldCompression.getSelectedItem(), labelIconValue.getText().equals("No file selected") ? null : labelIconValue.getText()).saveFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ChooseIconFileAction extends AbstractAction {
        private final GUI parent;

        ChooseIconFileAction(GUI parent) {
            this.parent = parent;
        }

        @Override
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
                @Override
                public boolean accept(File f) {
                    return f.isDirectory() || f.getName().toLowerCase().endsWith(".png");
                }

                @Override
                public String getDescription() {
                    return "PNG files";
                }
            });
            int returnValue = fileChooser.showOpenDialog(parent);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                labelIconValue.setText(selectedFile.getAbsolutePath());
            }
        }
    }
}
