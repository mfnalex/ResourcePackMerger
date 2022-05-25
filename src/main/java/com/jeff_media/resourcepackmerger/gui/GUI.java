package com.jeff_media.resourcepackmerger.gui;

import com.jeff_media.resourcepackmerger.ResourcePackFileFilter;
import com.jeff_media.resourcepackmerger.ResourcePackMerger;
import com.jeff_media.resourcepackmerger.Utils;
import com.jeff_media.resourcepackmerger.data.Config;
import com.jeff_media.resourcepackmerger.data.ResourcePackVersion;
import com.jeff_media.resourcepackmerger.data.ZipCompression;
import com.jeff_media.resourcepackmerger.logging.GuiLogger;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.stream.Collectors;

public class GUI extends JFrame {
    final JLabel labelName = new JLabel();
     final JTextField fieldName = new JTextField();
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
     File lastFile = null;

    public GUI() {
        ResourcePackMerger.setLogger(new GuiLogger(log));
        initComponents();
        loadConfig();
    }

    private void loadConfig() {
        Config config = new Config();
        try {
            config = Config.fromFile();
            if(!config.isNewlyGenerated) {
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
            if(file.exists()) {
                listFilesModel.addElement(file);
            } else {
                ResourcePackMerger.getLogger().warn("Recently used Resource Pack " + name + " not found.");
            }
        });
        labelOutputFileValue.setText(config.outputFile);
    }

    private void initComponents() {

        int height = 0;
        Container contentPane = getContentPane();
        contentPane.setLayout(new MigLayout("hidemode 0",
                // columns
                "[fill]" + "[fill]" + "[fill]",
                // rows
                "[]" + "[]" + "[]" + "[]" + "[]" + "[]" + "[]"));


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
        headerPanel.add(new JLabel(""),"cell 1 " + height2 + " 1 1");
        height2++;
        headerPanel.add(new JLabel("Resource Pack Merger v" + ResourcePackMerger.getVersion()), "cell 1 " + height2 + " 1 1");
        height2++;
        headerPanel.add(new JLabel("Copyright " + Calendar.getInstance().get(Calendar.YEAR) + " JEFF Media GbR / mfnalex"), "cell 1 " + height2 + " 1 1");
        height2++;
        headerPanel.add(new JLabel(""),"cell 1 " + height2 + " 1 1");
        contentPane.add(headerPanel, "cell 0 " + height + " 3 1");

        //<editor-fold desc="Description">
        height++;
        labelName.setText("Description");
        contentPane.add(labelName, "cell 0 " + height);

        fieldName.setText("My Custom Resource Pack");
        contentPane.add(fieldName, "cell 1 " + height);
        //</editor-fold>

        //<editor-fold desc="Resource Pack Version">
        height++;
        labelFormat.setText("Resource Pack Format");
        contentPane.add(labelFormat, "cell 0 " + height);
        contentPane.add(fieldFormat, "cell 1 " + height);

        Arrays.stream(ResourcePackVersion.values()).forEachOrdered(version -> fieldFormat.addItem(version));
        fieldFormat.setRenderer(new ResourcePackVersionListRenderer());
        fieldFormat.setSelectedIndex(fieldFormat.getItemCount() - 1);
        //</editor-fold>

        //<editor-fold desc="Output file">
        height++;
        labelOutputFile.setText("Output file");
        contentPane.add(labelOutputFile, "cell 0 " + height);
        labelOutputFileValue.setText(new File("merged-resourcepack.zip").getAbsolutePath());
        contentPane.add(labelOutputFileValue, "cell 1 " + height);
        buttonOutputFile.setText("Change");
        contentPane.add(buttonOutputFile, "cell 2 " + height);

        buttonOutputFile.addActionListener(new ChooseOutputFileAction(this));
        //</editor-fold>

        //<editor-fold desc="Resource Packs">
        height++;
        labelFiles.setText("Resource Packs");
        contentPane.add(labelFiles, "cell 0 " + height);
        scrollPanelListFiles.setViewportView(listFiles);
        scrollPanelListFiles.setMinimumSize(new Dimension(500, 200));
        contentPane.add(scrollPanelListFiles, "cell 1 " + height);

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

        contentPane.add(fileButtons, "cell 2 " + height);

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
        contentPane.add(fieldCompression, "cell 1 " + height);

        fieldCompression.setRenderer(new CompressionLevelListRenderer());
        Arrays.stream(ZipCompression.values()).forEachOrdered(level -> fieldCompression.addItem(level));
        //</editor-fold>

        //<editor-fold desc="Log window">
        height++;
        log.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        scrollPaneLog.setViewportView(log);
        contentPane.add(scrollPaneLog, "cell 0 " + height + " 3 1,hmin 150");

        ResourcePackMerger.getLogger().info("Waiting for input. Add at least one resource pack, then click on \"Start\".");
        ResourcePackMerger.getLogger().info("JSON files will automatically be merged when two or more resource packs contain conflicting files.");
        ResourcePackMerger.getLogger().info("When files cannot be merged, the file of the resource pack that's higher in the list takes precedence.");
        //</editor-fold>

        //<editor-fold desc="Progress bar">
        height++;
        contentPane.add(progressBar, "cell 0 " + height + " 3 1");

        progressBar.setIndeterminate(false);
        //</editor-fold>

        //<editor-fold desc="Start Button">
        height++;
        buttonStart.setText("Start");
        contentPane.add(buttonStart, "cell 0 " + height + " 3 1");

        buttonStart.addActionListener(new StartButtonAction(this));
        //</editor-fold>

        pack();
        setLocationRelativeTo(getOwner());

        this.addWindowListener(new WindowListener(this));

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
            new Config(fieldName.getText(),
                    labelOutputFileValue.getText(),
                    Collections.list(listFilesModel.elements()).stream().map(file -> file.getAbsolutePath()).collect(Collectors.toList()),
                    (ResourcePackVersion) fieldFormat.getSelectedItem(),
                    (ZipCompression) fieldCompression.getSelectedItem()).saveFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
